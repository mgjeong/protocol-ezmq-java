package org.edgexfoundry.ezmq.client;

import java.util.List;
import java.util.Scanner;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.domain.core.Reading;
import org.edgexfoundry.ezmq.EZMQAPI;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.edgexfoundry.ezmq.EZMQSubscriber;
import org.edgexfoundry.ezmq.EZMQSubscriber.EZMQSubCallback;

public class App {
    private static EZMQSubCallback mCallback;
    // use host name or ip address of publisher on mip when test with docker.
    private static final String mip = "localhost";
    private static final int mPort = 5562;
    private static EZMQErrorCode result = EZMQErrorCode.EZMQ_ERROR;

    private static void callback() {
        mCallback = new EZMQSubCallback() {
            public void onMessageCB(Event event) {
                System.out.println("[-------------------------------------");
                System.out.println("[APP: onMessageCB]");
                System.out.println("Device: " + event.getDevice());
                System.out.println("Readings: ");
                List<Reading> readings = event.getReadings();
                for (Reading reading : readings) {
                    System.out.print("Key: " + reading.getName());
                    System.out.println("  Value: " + reading.getValue());
                }
                System.out.println("----------------------------------------");
            }

            public void onMessageCB(String topic, Event event) {
                System.out.println("[-------------------------------------");
                System.out.println("[APP: onMessageCB]");
                System.out.println("Topic: " + topic);
                System.out.println("Device: " + event.getDevice());
                System.out.println("Readings: ");
                List<Reading> readings = event.getReadings();
                for (Reading reading : readings) {
                    System.out.print("Key: " + reading.getName());
                    System.out.println("  Value: " + reading.getValue());
                }
                System.out.println("----------------------------------------");
            }
        };
    }

    public static void main(String[] args) {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        apiInstance.initialize();
        callback();

        EZMQSubscriber subInstance = null;
        int choice = -1;
        String topic = null;

        System.out.println("Enter 1 for General Event testing");
        System.out.println("Enter 2 for Topic Based delivery");

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        choice = scanner.nextInt();

        switch (choice) {
        case 1:
            subInstance = new EZMQSubscriber(mip, mPort, mCallback);
            break;
        case 2:
            subInstance = new EZMQSubscriber(mip, mPort, mCallback);
            System.out.print("Enter the topic: ");
            topic = scanner.next();
            System.out.println("Topic is : " + topic);
            break;
        default:
            System.out.println("Invalid choice..[Re-run application]");
            return;
        }

        result = subInstance.start();
        if (result != EZMQErrorCode.EZMQ_OK) {
            System.out.println("start API: error occured");
            return;
        }
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

        // infinite loop for receiving messages....
        while (true) {
        }
    }
}
