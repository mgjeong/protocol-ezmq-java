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

package org.edgexfoundry.ezmq.client;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.domain.core.Reading;
import org.edgexfoundry.ezmq.EZMQAPI;
import org.edgexfoundry.ezmq.EZMQContentType;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.edgexfoundry.ezmq.EZMQMessage;
import org.edgexfoundry.ezmq.EZMQStatusCode;
import org.edgexfoundry.ezmq.EZMQSubscriber;
import org.edgexfoundry.ezmq.EZMQSubscriber.EZMQSubCallback;
import org.edgexfoundry.ezmq.bytedata.EZMQByteData;

public class App {
    private static EZMQSubCallback mCallback;
    // use host name or ip address of publisher on mip when test with docker.
    private static String mip;
    private static int mPort;
    private static EZMQErrorCode result = EZMQErrorCode.EZMQ_ERROR;
    public static EZMQAPI apiInstance = EZMQAPI.getInstance();
    public static EZMQSubscriber subInstance = null;
    public static Lock terminateLock = new ReentrantLock();
    public static java.util.concurrent.locks.Condition condVar = terminateLock.newCondition();

    private static void printEvent(Event event) {
        System.out.println("Device: " + event.getDevice());
        System.out.println("Readings: ");
        List<Reading> readings = event.getReadings();
        for (Reading reading : readings) {
            System.out.print("Key: " + reading.getName());
            System.out.println("  Value: " + reading.getValue());
        }
    }

    private static void printByteData(EZMQByteData byteData) {
        byte[] data = byteData.getByteData();
        System.out.println("Byte Data Size: " + data.length);
        for (int i = 0; i < data.length; i++) {
            System.out.print(" " + data[i]);
        }
        System.out.println();
    }

    private static void callback() {
        mCallback = new EZMQSubCallback() {
            public void onMessageCB(EZMQMessage ezmqMsg) {
                System.out.println("[-------------------------------------");
                System.out.println("[APP: onMessageCB]");
                if (null == ezmqMsg) {
                    System.out.println("ezmqMsg is null");
                    return;
                }
                EZMQContentType type = ezmqMsg.getContentType();
                System.out.println("Cotent type: " + type);

                if (EZMQContentType.EZMQ_CONTENT_TYPE_PROTOBUF == type) {
                    printEvent((Event) ezmqMsg);
                } else if (EZMQContentType.EZMQ_CONTENT_TYPE_BYTEDATA == type) {
                    printByteData((EZMQByteData) ezmqMsg);
                }
                System.out.println("----------------------------------------");
            }

            public void onMessageCB(String topic, EZMQMessage ezmqMsg) {
                System.out.println("[-------------------------------------");
                System.out.println("[APP: onMessageCB]");
                System.out.println("Topic: " + topic);
                if (null == ezmqMsg) {
                    System.out.println("ezmqMsg is null");
                    return;
                }
                EZMQContentType type = ezmqMsg.getContentType();
                System.out.println("Cotent type: " + type);

                if (EZMQContentType.EZMQ_CONTENT_TYPE_PROTOBUF == type) {
                    printEvent((Event) ezmqMsg);

                } else if (EZMQContentType.EZMQ_CONTENT_TYPE_BYTEDATA == type) {
                    printByteData((EZMQByteData) ezmqMsg);
                }
                System.out.println("----------------------------------------");
            }
        };
    }

    private static void printError() {
        System.out.println("\nRe-run the application as shown in below examples: ");
        System.out.println("\n  (1) For subscribing without topic: ");
        System.out.println(
                "      java -jar target/edgex-ezmq-subscriber-sample.jar -ip 107.108.81.116 -port 5562");
        System.out.println(
                "      java -jar target/edgex-ezmq-subscriber-sample.jar -ip localhost -port 5562");
        System.out.println("\n  (2) For subscribing with topic: ");
        System.out.println(
                "      java -jar target/edgex-ezmq-subscriber-sample.jar -ip 107.108.81.116 -port 5562 -t topic1");
        System.out.println(
                "      java -jar target/edgex-ezmq-subscriber-sample.jar -ip localhost -port 5562 -t topic1");
        System.exit(-1);
    }

    public static void main(String[] args) {

        // get ip and port and topic from command line arguments
        if (args.length != 4 && args.length != 6) {
            printError();
        }

        int n = 0;
        String topic = null;
        while (n < args.length) {
            if (args[n].equalsIgnoreCase("-ip")) {
                mip = args[n + 1];
                System.out.println("Given Ip: " + mip);
                n = n + 2;
            } else if (args[n].equalsIgnoreCase("-port")) {
                mPort = Integer.parseInt(args[n + 1]);
                System.out.println("Given Port: " + mPort);
                n = n + 2;
            } else if (args[n].equalsIgnoreCase("-t")) {
                topic = args[n + 1];
                System.out.println("Topic is : " + topic);
                n = n + 2;
            } else {
                printError();
            }
        }

        apiInstance.initialize();
        callback();
        subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        result = subInstance.start();
        if (result != EZMQErrorCode.EZMQ_OK) {
            System.out.println("start API: error occured");
            return;
        }

        // handle command line ctrl+c signal, stop subscriber and signal main
        // program to exit
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    // stopping the subscriber
                    result = subInstance.stop();
                    if (result != EZMQErrorCode.EZMQ_OK) {
                        System.out.println("Stop API: error occured");
                    }
                    result = apiInstance.terminate();
                    if (result != EZMQErrorCode.EZMQ_OK) {
                        System.out.println("Terminate API: error occured");
                    }
                    terminateLock.lock();
                    condVar.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    terminateLock.unlock();
                }
            }
        }));

        if (null == topic) {
            result = subInstance.subscribe();
        } else {
            result = subInstance.subscribe(topic);
        }

        if (result != EZMQErrorCode.EZMQ_OK) {
            System.out.println("subscribe API: error occured");
            return;
        }

        System.out.println("Suscribed to publisher.. -- Waiting for Events --");

        // Prevent main thread from exit
        try {
            terminateLock.lock();
            while(apiInstance.getStatus() != EZMQStatusCode.EZMQ_Terminated) {
                condVar.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            terminateLock.unlock();
        }
    }
}
