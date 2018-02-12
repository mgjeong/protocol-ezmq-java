package org.edgexfoundry.ezmq.bytedata;

import static org.junit.Assert.assertEquals;

import org.edgexfoundry.ezmq.EZMQContentType;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.junit.Test;

public class EZMQByteDataTest {

    @Test
    public void constructorTest() {
        byte[] byteData = { 0x40, 0x05, 0x10, 0x11, 0x12 };
        EZMQByteData data = new EZMQByteData(byteData);
        assertEquals(byteData, data.getByteData());
    }

    @Test
    public void getSetByteData() {
        byte[] byteData = { 0x40, 0x05, 0x10, 0x11, 0x12 };
        EZMQByteData data = new EZMQByteData(byteData);
        assertEquals(byteData, data.getByteData());
        byte[] byteData1 = { 0x45, 0x15, 0x11, 0x21, 0x22 };
        assertEquals(EZMQErrorCode.EZMQ_OK, data.setByteData(byteData1));
        assertEquals(byteData1, data.getByteData());
        //set null byte data
        assertEquals(EZMQErrorCode.EZMQ_ERROR, data.setByteData(null));
    }

    @Test
    public void getCotentTypeTest() {
        byte[] byteData = { 0x40, 0x05, 0x10, 0x11, 0x12 };
        EZMQByteData data = new EZMQByteData(byteData);
        assertEquals(EZMQContentType.EZMQ_CONTENT_TYPE_BYTEDATA, data.getContentType());
    }
}
