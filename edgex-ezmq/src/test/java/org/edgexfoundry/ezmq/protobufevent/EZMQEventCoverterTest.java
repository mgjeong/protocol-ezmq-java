package org.edgexfoundry.ezmq.protobufevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.ezmq.TestUtils;
import org.junit.Test;

public class EZMQEventCoverterTest {

    @Test
    public void toProtoBufTest() {
        Event event = TestUtils.getEdgeXEvent();
        byte[] byteEvent = EZMQEventConverter.toProtoBuf(event);
        assertNotNull(byteEvent);
    }

    @Test
    public void toProtoBufNegativeTest1() {
        Event event = TestUtils.getWrongEvent();
        byte[] byteEvent = EZMQEventConverter.toProtoBuf(event);
        assertEquals(null, byteEvent);
    }

    @Test
    public void toProtoBufNegativeTest2() {
        byte[] byteEvent = EZMQEventConverter.toProtoBuf(null);
        assertEquals(null, byteEvent);
    }

    @Test
    public void toEdgeXEventTest() {
        Event event = TestUtils.getEdgeXEvent();
        byte[] byteEvent = EZMQEventConverter.toProtoBuf(event);
        Event edgexEvent = EZMQEventConverter.toEdgeXEvent(byteEvent);
        assertNotNull(event);
        assertEquals(edgexEvent, event);
    }

    @Test
    public void toEdgeXEventNegativeTest() {
        Event edgexEvent = EZMQEventConverter.toEdgeXEvent(null);
        assertEquals(null, edgexEvent);
    }

}
