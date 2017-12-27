package org.edgexfoundry.ezmq.client;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    public static EZMQAPI apiInstance = EZMQAPI.getInstance();
    public static EZMQSubscriber subInstance = null;
    public static Lock terminateLock = new ReentrantLock();
    public static java.util.concurrent.locks.Condition condVar = terminateLock.newCondition();

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
        apiInstance.initialize();
        callback();

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
            condVar.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            terminateLock.unlock();
        }
    }
}
