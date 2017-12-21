package org.edgexfoundry.ezmq;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.domain.core.Reading;

public class TestUtils {

    public static Event getEdgeXEvent() {

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

        Event event = new Event("Test", readings);
        event.setCreated(10);
        event.setModified(20);
        event.setId("id");
        event.markPushed(new Timestamp(System.currentTimeMillis()).getTime());
        event.setOrigin(new Timestamp(System.currentTimeMillis()).getTime());

        return event;
    }

    // Function is added for negative test cases of EZMQ event converter.
    public static Event getWrongEvent() {

        List<Reading> readings = null;
        readings = new ArrayList<Reading>();

        Random rand = new Random();
        Reading reading = new Reading();
        reading.setName("TestReading");
        reading.setValue(rand.nextLong() + "");
        reading.setCreated(0);
        reading.setDevice("TestDevice");
        reading.setModified(0);

        readings.add(reading);

        Event event = new Event("Test", readings);
        event.setId("id1");
        event.markPushed(new Timestamp(System.currentTimeMillis()).getTime());
        event.setOrigin(new Timestamp(System.currentTimeMillis()).getTime());

        return event;
    }

    // This method is private in EZMQ SDK, that is why
    // same API is written here to test
    public static String getInProcUniqueAddress() {
        String address = "inproc://shutdown-" + UUID.randomUUID().toString();
        return address;
    }
}
