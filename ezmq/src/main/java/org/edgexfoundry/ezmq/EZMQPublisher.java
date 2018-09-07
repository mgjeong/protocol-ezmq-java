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
import org.edgexfoundry.ezmq.domain.core.Event;
import org.edgexfoundry.ezmq.bytedata.EZMQByteData;
import org.edgexfoundry.ezmq.protobufevent.EZMQEventConverter;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.zeromq.ZMQ;

/**
 * This class represents the EZMQ Publisher.
 */
public class EZMQPublisher {

  private int mPort;
  @SuppressWarnings("unused")
  private EZMQCallback mCallback;

  private ZMQ.Socket mPublisher;
  private ZMQ.Context mContext;

  //Secure mode
  private boolean mIsSecured;
  private StringBuffer mServerSecretKey;

  // Thread safety lock
  private ReentrantLock mPubLock;

  private final String PUB_TCP_PREFIX = "tcp://*:";
  private final String TOPIC_PATTERN = "[a-zA-Z0-9-_./]+";
  private final int EZMQ_VERSION = 1;
  private final int CONTENT_TYPE_OFFSET = 5;
  private final int VERSION_OFFSET = 2;
  private final int KEY_LENGTH = 40;

  private final static EdgeXLogger logger = EdgeXLoggerFactory.getEdgeXLogger(EZMQPublisher.class);

  /**
   * Constructor for EZMQ publisher.
   *
   * @param port
   *            port for publishing message/events.
   * @param callback
   *            {@link EZMQCallback}
   */
  public EZMQPublisher(int port, EZMQCallback callback) {
    mPort = port;
    mCallback = callback;
    mContext = EZMQAPI.getInstance().getContext();
    mIsSecured = EZMQAPI.getInstance().isSecured();
    mPubLock = new ReentrantLock(true);
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
   * Set the server private/secret key.
   * Note: <br>
   * (1) Key should be 40-character string encoded in the Z85 encoding format <br>
   * (2) This API should be called before start() API.
   *
   * @param key
   *            Server private/Secret key.
   * @return {@link EZMQErrorCode}
   * @throws Exception 
   *            Throws, if security is not enabled.  
   */
  public EZMQErrorCode setServerPrivateKey(String key) throws Exception  {
    if (!mIsSecured) {
      logger.error("Security is not enabled");
      throw new Exception("Security is not enabled");
    }
    if (null == key || key.length() != KEY_LENGTH) {
      logger.error("Invalid key length");
      return EZMQErrorCode.EZMQ_ERROR;
    }
    mServerSecretKey = new StringBuffer(key);
    return EZMQErrorCode.EZMQ_OK;
  }

  /**
   * Starts PUB instance.
   *
   * @return {@link EZMQErrorCode}
   */
  public EZMQErrorCode start() {
    if (null == mContext) {
      logger.error("Context is null");
      return EZMQErrorCode.EZMQ_ERROR;
    }

    try {
      mPubLock.lock();
      if (null == mPublisher) {
        mPublisher = mContext.socket(ZMQ.PUB);
        if (mIsSecured && mServerSecretKey != null) {
          //Set server key
          mPublisher.setCurveServer(true);
          mPublisher.setCurveSecretKey(mServerSecretKey.toString().getBytes());
        }
        mPublisher.bind(getSocketAddress());
      }
    } catch (Exception e) {
      logger.error("Exception while starting publisher: " + e.getMessage());
      mPublisher = null;
      return EZMQErrorCode.EZMQ_ERROR;
    } finally {
      mPubLock.unlock();
      //clear the key
      if (mServerSecretKey != null) {
        mServerSecretKey.delete(0, mServerSecretKey.length());
      }
    }
    logger.debug("Publisher started [address]: " + getSocketAddress());
    return EZMQErrorCode.EZMQ_OK;
  }

  private byte[] getHeader(EZMQContentType contentType) {
    byte ezmqHeader = 0x00;
    byte ezmqVersion = EZMQ_VERSION;
    ezmqVersion = (byte) (ezmqVersion << VERSION_OFFSET);
    ezmqHeader = (byte) (ezmqHeader | ezmqVersion);
    byte ezmqContentType = (byte) contentType.ordinal();
    ezmqContentType = (byte) (ezmqContentType << CONTENT_TYPE_OFFSET);
    ezmqHeader = (byte) (ezmqHeader | ezmqContentType);
    byte[] header = {ezmqHeader};
    return header;
  }

  private EZMQErrorCode publishInternal(String topic, EZMQMessage ezmqMessage) {
    EZMQContentType contentType = ezmqMessage.getContentType();

    // form the EZMQ header
    byte[] header = getHeader(contentType);

    // form the EZMQ data
    byte[] byteEvent = null;
    if (EZMQContentType.EZMQ_CONTENT_TYPE_PROTOBUF == contentType) {
      Event event = (Event) ezmqMessage;
      byteEvent = EZMQEventConverter.toProtoBuf(event);
    } else if (EZMQContentType.EZMQ_CONTENT_TYPE_BYTEDATA == contentType) {
      EZMQByteData byteData = (EZMQByteData) ezmqMessage;
      byteEvent = byteData.getByteData();
    } else {
      return EZMQErrorCode.EZMQ_INVALID_CONTENT_TYPE;
    }

    if (null == byteEvent) {
      logger.error("ByteEvent is null");
      return EZMQErrorCode.EZMQ_ERROR;
    }

    // Send on Socket
    boolean result = false;
    try {
      mPubLock.lock();
      if (null != mPublisher) {

        // send topic [if any]
        if (!(topic.isEmpty())) {
          result = mPublisher.sendMore(topic);
          if (false == result) {
            logger.error("SendMore failed [topic]");
            return EZMQErrorCode.EZMQ_ERROR;
          }
        }

        // send header
        result = mPublisher.sendMore(header);
        if (false == result) {
          logger.error("SendMore failed [header]");
          return EZMQErrorCode.EZMQ_ERROR;
        }

        // send data
        result = mPublisher.send(byteEvent);
      } else {
        return EZMQErrorCode.EZMQ_ERROR;
      }
    } catch (Exception e) {
      logger.error("Exception while publishing: " + e.getMessage());
      return EZMQErrorCode.EZMQ_ERROR;
    } finally {
      mPubLock.unlock();
    }
    if (false == result) {
      logger.error("Publish failed [data]");
      return EZMQErrorCode.EZMQ_ERROR;
    }
    logger.debug("Published data");
    return EZMQErrorCode.EZMQ_OK;
  }

  /**
   * Publish events on the socket for subscribers.
   *
   * @param ezmqMessage
   *            {@link Event} {@link EZMQByteData}
   * @return {@link EZMQErrorCode}
   */
  public EZMQErrorCode publish(EZMQMessage ezmqMessage) {
    if (null == mPublisher || null == ezmqMessage) {
      logger.error("Publisher or event is null");
      return EZMQErrorCode.EZMQ_ERROR;
    }
    return publishInternal("", ezmqMessage);
  }

  /**
   * Publish events on a specific topic on socket for subscribers.
   *
   * Note:<br>
   * (1) Topic name should be as path format. For example: home/livingroom/ <br>
   * (2) Topic name can have letters [a-z, A-z], numerics [0-9] and special characters _ - . and / <br>
   * (3) Topic will be appended with forward slash [/] in case, if application has not appended it.
   *
   * @param topic
   *            Topic on which event needs to be published.
   * @param ezmqMessage
   *            {@link Event} {@link EZMQByteData}
   * @return {@link EZMQErrorCode}
   */
  public EZMQErrorCode publish(String topic, EZMQMessage ezmqMessage) {
    if (null == mPublisher || null == ezmqMessage) {
      logger.error("Publisher or event is null");
      return EZMQErrorCode.EZMQ_ERROR;
    }

    // validate topic
    String validTopic = validateTopic(topic);
    if (null == validTopic) {
      logger.error("Invalid topic: " + topic);
      return EZMQErrorCode.EZMQ_INVALID_TOPIC;
    }
    return publishInternal(validTopic, ezmqMessage);
  }

  /**
   * Publish an events on list of topics on socket for subscribers. On any of
   * the topic in list, if it failed to publish event it will return
   * EZMQ_ERROR/EZMQ_INVALID_TOPIC.
   *
   * Note:<br>
   * (1) Topic name should be as path format. For example: home/livingroom/ <br>
   * (2) Topic name can have letters [a-z, A-z], numerics [0-9] and special characters _ - . and / <br>
   * (3) Topic will be appended with forward slash [/] in case, if application has not appended it.
   *
   * @param topics
   *            Topic on which event needs to be published.
   * @param ezmqMessage
   *            {@link Event} {@link EZMQByteData}
   * @return {@link EZMQErrorCode}
   *
   */
  public EZMQErrorCode publish(List<String> topics, EZMQMessage ezmqMessage) {
    if (null == ezmqMessage) {
      logger.error("Event is null");
      return EZMQErrorCode.EZMQ_ERROR;
    }

    if (null == topics) {
      logger.error("Topic list is null");
      return EZMQErrorCode.EZMQ_INVALID_TOPIC;
    }

    EZMQErrorCode result = EZMQErrorCode.EZMQ_OK;
    for (String topic : topics) {
      result = publish(topic, ezmqMessage);
      if (result != EZMQErrorCode.EZMQ_OK) {
        return result;
      }
    }
    return result;
  }

  /**
   * Stops PUB instance.
   *
   * @return {@link EZMQErrorCode}
   */
  public EZMQErrorCode stop() {
    if (null == mPublisher) {
      logger.error("Publisher is null");
      return EZMQErrorCode.EZMQ_ERROR;
    }

    EZMQErrorCode result = EZMQErrorCode.EZMQ_ERROR;
    try {
      mPubLock.lock();
      if (null != mPublisher) {
        // Sync close
        result = syncClose();
      } else {
        return result;
      }
      mPublisher = null;
      // Clear the key
      if (null != mServerSecretKey) {
        mServerSecretKey.delete(0, mServerSecretKey.length());
      }
    } finally {
      mPubLock.unlock();
    }
    logger.debug("Publisher stopped");
    return result;
  }

  /**
   * Get the port of the publisher.
   *
   * @return port number as integer.
   */
  public int getPort() {
    return mPort;
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

  private String getSocketAddress() {
    return PUB_TCP_PREFIX + Integer.toString(mPort);
  }

  private static String getMonitorAddress() {
    String MONITOR_PREFIX = "inproc://monitor-";
    return MONITOR_PREFIX + UUID.randomUUID().toString();
  }

  private EZMQErrorCode syncClose() {
    String address = getMonitorAddress();
    boolean result = mPublisher.monitor(address, ZMQ.EVENT_CLOSED);
    if (false == result) {
      logger.info("Error in monitor");
    }

    ZMQ.Socket socket = mContext.socket(ZMQ.PAIR);
    if (socket != null) {
      result = socket.connect(address);
      logger.info("Pair socket connection result: " + result);
    } else {
      logger.info("Pair socket creation failed");
    }

    try {
      mPublisher.close();
      logger.debug("Closed publisher socket");
    } catch (Exception e) {
      logger.error("Exception while closing socket: " + e.getMessage());
      return EZMQErrorCode.EZMQ_ERROR;
    }

    if (null == socket || false == result) {
      return EZMQErrorCode.EZMQ_OK;
    }

    // set receive timeout
    socket.setReceiveTimeOut(1000);
    while (true) {
      ZMQ.Event event = null;
      try {
        event = ZMQ.Event.recv(socket);
      } catch (Exception e) {
        logger.info("Exception while receiving Event");
        socket.close();
        break;
      }
      if (event == null) {
        logger.info("Event is null, timeout occured");
        socket.close();
        break;
      }

      logger.debug(
          "Event received for socket: " + event.getEvent() + "  address: " + event.getAddress());
      if (ZMQ.EVENT_CLOSED == event.getEvent()) {
        logger.debug("Received EVENT_CLOSED from socket");
        socket.close();
        break;
      }
    }
    return EZMQErrorCode.EZMQ_OK;
  }
}
