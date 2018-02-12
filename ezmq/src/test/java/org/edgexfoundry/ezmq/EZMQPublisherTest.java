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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.ezmq.bytedata.EZMQByteData;
import org.junit.Before;
import org.junit.Test;

public class EZMQPublisherTest {

    private static EZMQCallback mCallback;
    private static final String mTopic = "topic";
    private static final int mPort = 5562;

    @Before
    public void setup() {
        mCallback = new EZMQCallback() {
            public void onStopCB(EZMQErrorCode code) {
            }

            public void onStartCB(EZMQErrorCode code) {
            }

            public void onErrorCB(EZMQErrorCode code) {
            }
        };
    }

    @Test
    public void constructorTest1() {
        EZMQPublisher instance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(instance);
    }

    @Test
    public void constructorTest2() {
        EZMQPublisher instance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(instance);
    }

    @Test
    public void startTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishTest1() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());

        Event event = TestUtils.getEdgeXEvent();
        EZMQByteData byteData = TestUtils.getEzmqByteData();

        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(byteData));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishTest2() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());

        Event event = TestUtils.getEdgeXEvent();
        EZMQByteData byteData = TestUtils.getEzmqByteData();

        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(mTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(mTopic, byteData));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishTest3() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());

        Event event = TestUtils.getEdgeXEvent();
        EZMQByteData byteData = TestUtils.getEzmqByteData();

        List<String> topics = new ArrayList<String>();
        topics.add("topic1");
        topics.add("topic2");

        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(topics, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(topics, byteData));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishTopicTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());

        Event event = TestUtils.getEdgeXEvent();
        EZMQByteData byteData = TestUtils.getEzmqByteData();

        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(mTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(mTopic, byteData));

        // Empty topic test
        String testingTopic = "";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, byteData));

        // Alphabet test
        testingTopic = "topic";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Numeric test
        testingTopic = "123";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Alpha-Numeric test
        testingTopic = "1a2b3";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Alphabet forward slash test
        testingTopic = "topic/";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Alphabet-Numeric, forward slash test
        testingTopic = "topic/13/4jtjos/";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Alphabet-Numeric, forward slash test
        testingTopic = "123a/1this3/4jtjos";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Alphabet, backslash test
        testingTopic = "topic\";";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, byteData));

        // Alphabet-Numeric, forward slash and space test
        testingTopic = "topic/13/4jtjos/ ";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, byteData));

        // Special character test
        testingTopic = "*123a";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, byteData));

        // Sentence test
        testingTopic = "This is a topic";
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(testingTopic, byteData));

        // Topic contain forward slash at last
        testingTopic = "topic/122/livingroom/";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Topic contain -
        testingTopic = "topic/122/livingroom/-";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Topic contain _
        testingTopic = "topic/122/livingroom_";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        // Topic contain .
        testingTopic = "topic/122.livingroom.";
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, event));
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.publish(testingTopic, byteData));

        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishNegativeTest1() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.start());

        Event event = TestUtils.getWrongEvent();
        EZMQByteData byteData = TestUtils.getEzmqByteData();

        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.publish(event));
        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.publish(byteData));

        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishNegativeTest2() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.start());

        Event event = TestUtils.getWrongEvent();
        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.publish(mTopic, event));
        EZMQByteData byteData = new EZMQByteData(null);
        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.publish(mTopic, byteData));

        assertEquals(EZMQErrorCode.EZMQ_ERROR, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void publishNegativeTest3() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertNotNull(pubInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());

        Event event = TestUtils.getEdgeXEvent();
        EZMQByteData byteData = TestUtils.getEzmqByteData();
        String topic = null;
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(topic, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(topic, byteData));

        List<String> topics = null;
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(topics, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(topics, byteData));

        topics = new ArrayList<String>();
        topics.add("topic1");
        topics.add(null);
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(topics, event));
        assertEquals(EZMQErrorCode.EZMQ_INVALID_TOPIC, pubInstance.publish(topics, byteData));

        assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.terminate());
    }

    @Test
    public void startStopTest() {
        EZMQAPI apiInstance = EZMQAPI.getInstance();
        assertNotNull(apiInstance);
        assertEquals(EZMQErrorCode.EZMQ_OK, apiInstance.initialize());
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        for (int i = 1; i <= 10; i++) {
            assertNotNull(pubInstance);
            assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.start());
            assertEquals(EZMQErrorCode.EZMQ_OK, pubInstance.stop());
        }
    }

    @Test
    public void getPortTest() {
        EZMQPublisher pubInstance = new EZMQPublisher(mPort, mCallback);
        assertEquals(mPort, pubInstance.getPort());
    }
}
