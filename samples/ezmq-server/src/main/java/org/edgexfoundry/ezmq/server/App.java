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

package org.edgexfoundry.ezmq.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.edgexfoundry.ezmq.domain.core.Event;
import org.edgexfoundry.ezmq.domain.core.Reading;
import org.edgexfoundry.ezmq.EZMQAPI;
import org.edgexfoundry.ezmq.EZMQCallback;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.edgexfoundry.ezmq.EZMQPublisher;
import org.edgexfoundry.ezmq.bytedata.EZMQByteData;

public class App {
  private static EZMQCallback mCallback;
  private static int mPort;
  private static int mIsSecured;
  private static EZMQErrorCode result = EZMQErrorCode.EZMQ_ERROR;
  public static EZMQAPI apiInstance = EZMQAPI.getInstance();
  public static EZMQPublisher pubInstance = null;
  // put server key
  public static String mServerSecretKey = "";

  private static void callback() {
    mCallback = new EZMQCallback() {

      @Override
      public void onStopCB(EZMQErrorCode code) {}

      @Override
      public void onStartCB(EZMQErrorCode code) {}

      @Override
      public void onErrorCB(EZMQErrorCode code) {}
    };
  }

  public static org.edgexfoundry.ezmq.domain.core.Event getEdgeXEvent() {

    List<Reading> readings = null;
    readings = new ArrayList<Reading>();
    Random rand = new Random();
    Reading reading1 = new Reading();
    reading1.setName("TestReading1");
    reading1.setValue(rand.nextLong() + "");
    reading1.setCreated(0);
    reading1.setDevice("TestDevice1");
    reading1.setModified(10);
    reading1.setId("id1");
    reading1.setOrigin(15);
    reading1.setPushed(20);

    Reading reading2 = new Reading();
    reading2.setName("TestReading2");
    reading2.setValue(rand.nextLong() + "");
    reading2.setCreated(25);
    reading2.setDevice("TestDevice2");
    reading2.setModified(30);
    reading2.setId("id2");
    reading2.setOrigin(35);
    reading2.setPushed(30);

    readings.add(reading1);
    readings.add(reading2);

    org.edgexfoundry.ezmq.domain.core.Event event = new Event("Test", readings);
    event.setCreated(10);
    event.setModified(20);
    event.setId("id");
    event.markPushed(new Timestamp(System.currentTimeMillis()).getTime());
    event.setOrigin(new Timestamp(System.currentTimeMillis()).getTime());

    return event;
  }

  private static void printError() {
    System.out.println("\nRe-run the application as shown in below examples: ");
    System.out.println("\n  (1) For publishing without topic: ");
    System.out.println("      java -jar target/edgex-ezmq-publisher-sample.jar -port 5562");
    System.out.println("\n  (2) For publishing without topic: [Secured]");
    System.out
        .println("      java -jar target/edgex-ezmq-publisher-sample.jar -port 5562 -secured 1");
    System.out.println("\n  (3) For publishing with topic: ");
    System.out
        .println("      java -jar target/edgex-ezmq-publisher-sample.jar -port 5562 -t topic1");
    System.out.println("\n  (4) For publishing with topic: [Secured]");
    System.out.println(
        "      java -jar target/edgex-ezmq-publisher-sample.jar -port 5562 -t topic1 -secured 1");
    System.exit(-1);
  }

  public static EZMQByteData getEzmqByteData() {
    byte[] byteData = {0x40, 0x05, 0x10, 0x11, 0x12};
    EZMQByteData data = new EZMQByteData(byteData);
    return data;
  }

  public static void main(String[] args) throws InterruptedException {
    // get port and topic from command line arguments
    int n = 0;
    String topic = null;
    if (args.length != 2 && args.length != 4 && args.length != 6) {
      printError();
    }
    while (n < args.length) {
      if (args[n].equalsIgnoreCase("-port")) {
        mPort = Integer.parseInt(args[n + 1]);
        System.out.println("Given Port: " + mPort);
        n = n + 2;
      } else if (args[n].equalsIgnoreCase("-t")) {
        topic = args[n + 1];
        System.out.println("Topic is : " + topic);
        n = n + 2;
      } else if (args[n].equalsIgnoreCase("-secured")) {
        mIsSecured = Integer.parseInt(args[n + 1]);
        System.out.println("Secured: " + mIsSecured);
        n = n + 2;
      } else {
        printError();
      }
    }

    apiInstance.initialize();
    callback();
    pubInstance = new EZMQPublisher(mPort, mCallback);
    if (1 == mIsSecured) {
      try {
        result = pubInstance.setServerPrivateKey(mServerSecretKey);
      } catch (Exception e) {
        System.out.println("setServerPrivateKey exception: " + e.getMessage());
        e.printStackTrace();
      }
      if (result != EZMQErrorCode.EZMQ_OK) {
        System.out.println("Set server private key API: error occured");
        return;
      }
    }
    result = pubInstance.start();
    if (result != EZMQErrorCode.EZMQ_OK) {
      System.out.println("start API: error occured");
      return;
    }

    // handle command line ctrl+c signal and call stop publisher
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      public void run() {
        result = pubInstance.stop();
        if (result != EZMQErrorCode.EZMQ_OK) {
          System.out.println("stop API: error occured");
        }
        result = apiInstance.terminate();
        if (result != EZMQErrorCode.EZMQ_OK) {
          System.out.println("Terminate API: error occured");
        }
        System.out.println("!!!!! Exiting !!!!");
      }
    }));

    // This delay is added to prevent JeroMQ first packet drop during
    // initial connection of publisher and subscriber.
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    org.edgexfoundry.ezmq.domain.core.Event event = getEdgeXEvent();
    int i = 1;
    System.out.println("--------- Will Publish 15 events at interval of 2 seconds ---------");
    while (i <= 15) {
      if (null == topic) {
        result = pubInstance.publish(event);
      } else {
        result = pubInstance.publish(topic, event);
      }
      if (result != EZMQErrorCode.EZMQ_OK) {
        System.out.println("publish API: error occured");
        return;
      }
      System.out.println("Event " + i + " Published!");
      Thread.sleep(2000);
      i++;
    }
  }
}
