package org.edgexfoundry.ezmq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.edgexfoundry.ezmq.EZMQSubscriber.EZMQSubCallback;
import org.junit.Before;
import org.junit.Test;

public class EZMQSubscriberTest {

    private static EZMQSubCallback mCallback;
    private static final String mTopic = "topic";
    private static final String mServiceName = "service1";
    private static final String mip = "0.0.0.0";
    private static final int mPort = 5562;

    @Before
    public void setup() {
    }

    @Test
    public void constructorTest1() {
        EZMQSubscriber instance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(instance);
    }

    @Test
    public void constructorTest2() {
        EZMQSubscriber instance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(instance);
    }

    @Test
    public void constructorTest3() {
        EZMQSubscriber instance = new EZMQSubscriber(mServiceName, mCallback);
        assertNotNull(instance);
    }

    @Test
    public void startTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTest1() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTest2() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTest3() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(mTopic));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTest4() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(mTopic));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTest5() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());

        List<String> topics = new ArrayList<String>();
        topics.add("topic1");
        topics.add("topic2");
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(topics));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void SubscribeTopicTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());

        // Empty topic test
        String testingTopic = "";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(testingTopic));

        // Alphabet test
        testingTopic = "topic";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Numeric test
        testingTopic = "123";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Alpha-Numeric test
        testingTopic = "1a2b3";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Alphabet forward slash test
        testingTopic = "topic/";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Alphabet-Numeric, forward slash test
        testingTopic = "topic/13/4jtjos/";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Alphabet-Numeric, forward slash test
        testingTopic = "123a/1this3/4jtjos";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Alphabet, backslash test
        testingTopic = "topic\";";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(testingTopic));

        // Alphabet-Numeric, forward slash and space test
        testingTopic = "topic/13/4jtjos/ ";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(testingTopic));

        // Special character test
        testingTopic = "*123a";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(testingTopic));

        // Sentence test
        testingTopic = "This is a topic";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(testingTopic));

        // Topic contain forward slash at last
        testingTopic = "topic/122/livingroom/";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Topic contain -
        testingTopic = "topic/122/livingroom/-";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Topic contain _
        testingTopic = "topic/122/livingroom_";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        // Topic contain .
        testingTopic = "topic/122.livingroom.";
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(testingTopic));

        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTestNegative1() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());

        String topic = null;
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(topic));

        List<String> topics = null;
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(topics));

        topics = new ArrayList<String>();
        topics.add("topic1");
        topics.add(null);

        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.subscribe(topics));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void subscribeTestNegative2() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());

        // invalid port
        EZMQSubscriber subInstance = new EZMQSubscriber("localhost", -1, mCallback);
        EZMQErrorCode errorCode = subInstance.start();
        assertEquals(EZMQErrorCode.EZMQ_ERROR, errorCode);
        subInstance.stop();
        apiInstance.terminate();
    }

    @Test
    public void unsubscribeTest1() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe("topic"));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.unSubscribe("topic"));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void unsubscribeTest2() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());

        List<String> topics = new ArrayList<String>();
        topics.add("topic1");
        topics.add("topic2");
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe(topics));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.unSubscribe(topics));
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void unsubscribeNegativeTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.subscribe("topic"));

        String topic = null;
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.unSubscribe(topic));

        topic = "!$tpoic";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.unSubscribe(topic));

        List<String> topics = new ArrayList<String>();
        topics.add("topic1");
        topics.add(null);
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, subInstance.unSubscribe(topics));

        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void stopTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        assertNotNull(subInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
    }

    @Test
    public void startStopTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQSubscriber subInstance = new EZMQSubscriber(mip, mPort, mCallback);
        for (int i = 1; i <= 10; i++) {
            assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.start());
            assertEquals(EZMQErrorCode.EZMQ_OK, subInstance.stop());
        }
    }

    @Test
    public void uniqueAddressTest() {
        for (int i = 1; i <= 100; i++) {
            System.out.println("Address: " + TestUtils.getInProcUniqueAddress());
        }
    }

    @Test
    public void getIpTest() {
        EZMQSubscriber instance = new EZMQSubscriber(mip, mPort, mCallback);
        assertEquals(mip, instance.getIp());
    }

    @Test
    public void getPortTest() {
        EZMQSubscriber instance = new EZMQSubscriber(mip, mPort, mCallback);
        assertEquals(mPort, instance.getPort());
    }

    @Test
    public void getServiceNameTest() {
        EZMQSubscriber instance = new EZMQSubscriber(mServiceName, mCallback);
        assertEquals(mServiceName, instance.getServiceName());
    }
}
