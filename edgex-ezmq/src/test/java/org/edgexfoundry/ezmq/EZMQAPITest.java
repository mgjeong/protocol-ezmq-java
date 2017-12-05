package org.edgexfoundry.ezmq;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EZMQAPITest {

    @Test
    public void getInstanceTest() {
        EZMQAPI instance = EZMQAPI.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void initializeTest() {
        EZMQAPI instance = EZMQAPI.getInstance();
        assertEquals(EZMQErrorCode.EZMQ_OK, instance.initialize());
    }

    @Test
    public void terminateTest() {
        EZMQAPI instance = EZMQAPI.getInstance();
        assertEquals(EZMQErrorCode.EZMQ_OK, instance.initialize());
        assertEquals(EZMQErrorCode.EZMQ_OK, instance.terminate());
    }

    @Test
    public void getContextTest() {
        EZMQAPI instance = EZMQAPI.getInstance();
        assertEquals(EZMQErrorCode.EZMQ_OK, instance.initialize());
        assertNotNull(instance.getContext());
    }
}
