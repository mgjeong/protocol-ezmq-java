/*******************************************************************************
 * Copyright 2017 Samsung Electronics All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/

package org.edgexfoundry.ezmq;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.ezmq.bytedata.EZMQByteData;
import org.edgexfoundry.ezmq.protobufevent.EZMQEventConverter;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;

/**
 * This class represents the EZMQ Subscriber.
 */
public class EZMQSubscriber {

    private String mServiceName;
    private EZMQSubCallback mCallback;
    private String mIp;
    private int mPort;
    private Thread mThread;

    // ZMQ Subscriber socket
    private ZMQ.Socket mSubscriber;
    private ZMQ.Context mContext;

    // ZMQ poller
    private Poller mPoller;

    // ZMQ shut-down socket
    private ZMQ.Socket mShutdownServer;
    private ZMQ.Socket mShutdownClient;

    // Thread safety lock
    private ReentrantLock mSubLock;

    private final String TCP_PREFIX = "tcp://";
    private final String INPROC_PREFIX = "inproc://shutdown-";
    private final String TOPIC_PATTERN = "[a-zA-Z0-9-_./]+";
    private final int CONTENT_TYPE_OFFSET = 5;
    private final int CONTENT_TYPE_MASK = 0xFF;
    private final static EdgeXLogger logger = EdgeXLoggerFactory
            .getEdgeXLogger(EZMQSubscriber.class);

    /**
     * Subscribe to publisher at given IP and Port.
     *
     * @param ip
     *            IP address
     * @param port
     *            port number
     * @param callback
     *            {@link EZMQCallback}
     */
    public EZMQSubscriber(String ip, int port, EZMQSubCallback callback) {
        mIp = ip;
        mPort = port;
        mCallback = callback;
        mContext = EZMQAPI.getInstance().getContext();
        mSubLock = new ReentrantLock(true);
    }

    /**
     * Subscribe to given serviceName.
     *
     * @param serviceName
     *            service for which subscription is required.
     * @param callback
     *            {@link EZMQSubCallback}
     */
    public EZMQSubscriber(String serviceName, EZMQSubCallback callback) {
        mServiceName = serviceName;
        // For given serviceName get the ip and port [TODO]
        mCallback = callback;
        mContext = EZMQAPI.getInstance().getContext();
        mSubLock = new ReentrantLock(true);
    }

    // finalize method to be called by Java Garbage collector before destroying
    // this object.
    @Override
    protected void finalize() {
        logger.debug("In finalize");
        stop();
        try {
            super.finalize();
        } catch (Throwable e) {
        }
    }

    /**
     * Interface to receive message callback from EZMQ subscriber.
     */
    public interface EZMQSubCallback {
        /**
         * Invoked when message is received.
         *
         * @param ezmqMessage
         *            {@link Event}
         */
        public void onMessageCB(EZMQMessage ezmqMessage);

        /**
         * Invoked when message is received for a specific topic.
         *
         * @param topic
         *            Topic for the received event.
         *
         * @param ezmqMessage
         *            {@link Event}
         */
        public void onMessageCB(String topic, EZMQMessage ezmqMessage);
    }

    /**
     * Starts SUB instance.
     *
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode start() {
        if (null == mContext) {
            logger.error("Context is null");
            return EZMQErrorCode.EZMQ_ERROR;
        }

        String address = getInProcUniqueAddress();
        try {
            mSubLock.lock();
            // Shutdown server sockets
            if (null == mShutdownServer) {
                mShutdownServer = mContext.socket(ZMQ.PAIR);
                mShutdownServer.bind(address);
            }

            // Shutdown client sockets
            if (null == mShutdownClient) {
                mShutdownClient = mContext.socket(ZMQ.PAIR);
                mShutdownClient.connect(address);
            }

            // Subscriber socket
            if (null == mSubscriber) {
                mSubscriber = mContext.socket(ZMQ.SUB);
                mSubscriber.connect(getSocketAddress(mIp, mPort));
            }

            // Register sockets to poller
            if (null == mPoller) {
                mPoller = mContext.poller(2);
                mPoller.register(mSubscriber, Poller.POLLIN | Poller.POLLERR);
                mPoller.register(mShutdownClient, Poller.POLLIN | Poller.POLLERR);
            }
        } catch (Exception e) {
            logger.error("Exception while starting subscriber: " + e.getMessage());
            mSubLock.unlock();
            stop();
            return EZMQErrorCode.EZMQ_ERROR;
        }

        // Receiver thread
        if (null == mThread) {
            mThread = new Thread(new Runnable() {
                public void run() {
                    receive();
                }
            });
            mThread.start();
            logger.debug("Receiver thread started");
        }

        mSubLock.unlock();
        return EZMQErrorCode.EZMQ_OK;
    }

    private String getSocketAddress(String ip, int port) {
        return TCP_PREFIX + ip + ":" + port;
    }

    private String getInProcUniqueAddress() {
        String address = INPROC_PREFIX + UUID.randomUUID().toString();
        return address;
    }

    /**
     * Subscribe for event/messages.
     *
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode subscribe() {
        return subscribeInternal("");
    }

    /**
     * Subscribe for event/messages on a particular topic.
     *
     * Note (1) Topic name should be as path format. For example:
     * home/livingroom/ (2) Topic name can have letters [a-z, A-z], numerics
     * [0-9] and special characters _ - . and / (3) Topic will be appended with
     * forward slash [/] in case, if application has not appended it.
     *
     * @param topic
     *            Topic to be subscribed.
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode subscribe(String topic) {
        // validate the topic
        String validTopic = validateTopic(topic);
        if (null == validTopic) {
            logger.error("Invalid topic: " + topic);
            return EZMQErrorCode.EZMQ_INVALID_TOPIC;
        }
        logger.debug("Topic is: " + validTopic);
        return subscribeInternal(validTopic);
    }

    private EZMQErrorCode subscribeInternal(String topic) {
        if (null == mSubscriber) {
            logger.error("Subscriber is null");
            return EZMQErrorCode.EZMQ_ERROR;
        }

        // subscribe for messages
        try {
            mSubLock.lock();
            if (null != mSubscriber) {
                mSubscriber.subscribe(topic.getBytes());
            } else {
                return EZMQErrorCode.EZMQ_ERROR;
            }
        } finally {
            mSubLock.unlock();
        }
        logger.debug("subscribed for events");
        return EZMQErrorCode.EZMQ_OK;
    }

    /**
     * Subscribe for event/messages on given list of topics. On any of the topic
     * in list, if it failed to subscribe events it will return
     * EZMQ_ERROR/EZMQ_INVALID_TOPIC.
     *
     * Note: (1) Topic name should be as path format. For example:
     * home/livingroom/ (2) Topic name can have letters [a-z, A-z], numerics
     * [0-9] and special characters _ - . and / (3) Topic will be appended with
     * forward slash [/] in case, if application has not appended it.
     *
     * @param topics
     *            List of topics to be subscribed.
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode subscribe(List<String> topics) {
        if (null == topics) {
            logger.error("Topic list is null");
            return EZMQErrorCode.EZMQ_INVALID_TOPIC;
        }

        EZMQErrorCode result = EZMQErrorCode.EZMQ_OK;
        for (String topic : topics) {
            result = subscribe(topic);
            if (result != EZMQErrorCode.EZMQ_OK) {
                return result;
            }
        }
        return result;
    }

    /**
     * Subscribe for event/messages from given IP:Port on the given topic.
     *
     * Note (1) It will be using same Subscriber socket for connecting to given
     * ip:port. (2) To unsubcribe use un-subscribe API with the same topic. (3)
     * Topic name should be as path format. For example: home/livingroom/ (4)
     * Topic name can have letters [a-z, A-z], numerics [0-9] and special
     * characters _ - . and / (5) Topic will be appended with forward slash [/]
     * in case, if application has not appended it.
     *
     * @param ip
     *            Target[Publisher] IP address.
     * @param port
     *            Target[Publisher] Port number.
     * @param topic
     *            Topic to be subscribed.
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode subscribe(String ip, int port, String topic) {
        if (null == ip || port < 0) {
            logger.error("Invalid param");
            return EZMQErrorCode.EZMQ_ERROR;
        }

        // validate the topic
        String validTopic = validateTopic(topic);
        if (null == validTopic) {
            logger.error("Invalid topic: " + topic);
            return EZMQErrorCode.EZMQ_INVALID_TOPIC;
        }
        logger.debug("Topic is: " + validTopic);
        return subscribeInternal(ip, port, validTopic);
    }

    private EZMQErrorCode subscribeInternal(String ip, int port, String topic) {
        if (null == mSubscriber) {
            logger.error("Subscriber is null");
            return EZMQErrorCode.EZMQ_ERROR;
        }

        // subscribe for messages
        try {
            mSubLock.lock();
            if (null != mSubscriber) {
                mSubscriber.connect(getSocketAddress(ip, port));
                mSubscriber.subscribe(topic.getBytes());
            } else {
                return EZMQErrorCode.EZMQ_ERROR;
            }
        } finally {
            mSubLock.unlock();
        }
        logger.debug("subscribed for events IP: " + ip + " Port: " + port + " Topic: " + topic);
        return EZMQErrorCode.EZMQ_OK;
    }

    private void parseData() {
        byte[] frame1 = null;
        byte[] frame2 = null;
        byte[] frame3 = null;
        Event event = null;
        EZMQByteData byteData = null;
        String recvTopic = null;
        boolean isTopic = false;

        // Read data frames from socket
        try {
            mSubLock.lock();
            if (null != mSubscriber) {
                frame1 = mSubscriber.recv();
                if (mSubscriber.hasReceiveMore()) {
                    frame2 = mSubscriber.recv();
                    if (mSubscriber.hasReceiveMore()) {
                        isTopic = true;
                        frame3 = mSubscriber.recv();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception while receiving: " + e.getMessage());
        } finally {
            mSubLock.unlock();
        }

        if (false == isTopic) {
            frame3 = frame2;
            frame2 = frame1;
        } else {
            recvTopic = new String(frame1);
            if (recvTopic.endsWith("/")) {
                recvTopic = recvTopic.substring(0, recvTopic.length() - 1);
            }
        }

        if (null == frame2) {
            logger.error("Received header is NULL");
            return;
        }

        // Parse the header
        byte ezmqHeader = frame2[0];
        int contentType = ((ezmqHeader & CONTENT_TYPE_MASK) >> CONTENT_TYPE_OFFSET);

        // Parse the data
        if (contentType == EZMQContentType.EZMQ_CONTENT_TYPE_PROTOBUF.ordinal()) {
            event = EZMQEventConverter.toEdgeXEvent(frame3);
            if (false == isTopic) {
                mCallback.onMessageCB(event);
            } else {
                mCallback.onMessageCB(recvTopic, event);
            }
        } else if (contentType == EZMQContentType.EZMQ_CONTENT_TYPE_BYTEDATA.ordinal()) {
            byteData = new EZMQByteData(frame3);
            if (false == isTopic) {
                mCallback.onMessageCB(byteData);
            } else {
                mCallback.onMessageCB(recvTopic, byteData);
            }
        } else {
            logger.error("Not a supported type");
        }

    }

    private void receive() {
        while (null != mThread && !mThread.isInterrupted()) {
            if (null == mSubscriber || null == mPoller) {
                logger.error("Subscriber or poller is null");
                return;
            }
            mPoller.poll();
            if (mPoller.pollin(0)) {
                parseData();
            } else if (mPoller.pollin(1)) {
                logger.debug("Received shut down request");
                break;
            }
        }
    }

    /**
     * Un-subscribe for all the events from publisher.
     *
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode unSubscribe() {
        return unSubscribeInternal("");
    }

    /**
     * Un-subscribe for a specific topic events.
     *
     * Note: (1) Topic name should be as path format. For example:
     * home/livingroom/ (2) Topic name can have letters [a-z, A-z], numerics
     * [0-9] and special characters _ - . and / (3) Topic will be appended with
     * forward slash [/] in case, if application has not appended it.
     *
     * @param topic
     *            topic to be unsubscribed.
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode unSubscribe(String topic) {
        // validate topic
        String validTopic = validateTopic(topic);
        if (null == validTopic) {
            logger.error("Invalid topic: " + topic);
            return EZMQErrorCode.EZMQ_INVALID_TOPIC;
        }
        logger.debug("Topic is: " + validTopic);
        return unSubscribeInternal(validTopic);

    }

    private EZMQErrorCode unSubscribeInternal(String topic) {
        if (null == mSubscriber) {
            logger.error("Subscriber is null");
            return EZMQErrorCode.EZMQ_ERROR;
        }
        try {
            mSubLock.lock();
            if (null != mSubscriber) {
                mSubscriber.unsubscribe(topic.getBytes());
            } else {
                return EZMQErrorCode.EZMQ_ERROR;
            }
        } finally {
            mSubLock.unlock();
        }
        logger.debug("Un-subscribed for events");
        return EZMQErrorCode.EZMQ_OK;
    }

    /**
     * Un-subscribe for given list of topic events. On any of the topic in list,
     * if it failed to unsubscribe events it will return
     * EZMQ_ERROR/EZMQ_INVALID_TOPIC.
     *
     * Note: (1) Topic name should be as path format. For example:
     * home/livingroom/ (2) Topic name can have letters [a-z, A-z], numerics
     * [0-9] and special characters _ - . and / (3) Topic will be appended with
     * forward slash [/] in case, if application has not appended it.
     *
     * @param topics
     *            List of topics to be unsubscribed.
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode unSubscribe(List<String> topics) {
        if (null == topics) {
            logger.error("Topic list is null");
            return EZMQErrorCode.EZMQ_INVALID_TOPIC;
        }
        EZMQErrorCode result = EZMQErrorCode.EZMQ_OK;
        for (String topic : topics) {
            result = unSubscribe(topic);
            if (result != EZMQErrorCode.EZMQ_OK) {
                return result;
            }
        }
        return result;
    }

    /**
     * Stop the SUB instance.
     *
     * @return {@link EZMQErrorCode}
     */
    public EZMQErrorCode stop() {

        try {
            mSubLock.lock();

            // Send a shutdown message to receiver thread
            if (null != mShutdownServer) {
                boolean result = mShutdownServer.send("shutdown");
                logger.debug("Shutdown send result: " + result);
            }

            // wait for receiver thread to stop
            try {
                if (null != mThread) {
                    mThread.join();
                }
            } catch (InterruptedException e) {
                logger.error("Thread join exception" + e.getMessage());
            }

            // Unregister sockets from poller
            if (null != mPoller) {
                mPoller.unregister(mSubscriber);
                mPoller.unregister(mShutdownClient);
            }

            // close shut down sockets
            if (null != mShutdownClient) {
                mShutdownClient.close();
            }
            if (null != mShutdownServer) {
                mShutdownServer.close();
            }

            // Close subscriber
            if (null != mSubscriber) {
                mSubscriber.close();
            }

            mSubscriber = null;
            mPoller = null;
            mShutdownClient = null;
            mShutdownServer = null;
            mThread = null;
        } catch (Exception e) {
            logger.error("Exception while stopping subscriber: " + e.getMessage());
            return EZMQErrorCode.EZMQ_ERROR;
        } finally {
            mSubLock.unlock();
        }

        logger.debug("Subscriber stopped");
        return EZMQErrorCode.EZMQ_OK;
    }

    /**
     * Get the IP address.
     *
     * @return IP address as String.
     */
    public String getIp() {
        return mIp;
    }

    /**
     * Get the port of the subscriber.
     *
     * @return port number as integer.
     */
    public int getPort() {
        return mPort;
    }

    /**
     * Get the service name.
     *
     * @return Service name.
     */
    public String getServiceName() {
        return mServiceName;
    }

    private String validateTopic(String topic) {
        if (null == topic || topic.isEmpty()) {
            return null;
        }

        // check whether topic contains only alphabet, digits and special
        // characters _ - . and /
        if (!topic.matches(TOPIC_PATTERN)) {
            return null;
        }

        // check whether last character is forward slash or not
        // if not append and return the string
        if (!topic.endsWith("/")) {
            topic = topic + "/";
        }
        return topic;
    }
}
