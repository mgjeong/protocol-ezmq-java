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

package org.edgexfoundry.ezmq.protobufevent;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.domain.core.Reading;
import org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Event.Builder;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;

/**
 * This class represents the EZMQEventConverter It provides methods to convert
 * edgeX message to protocol buffer message.
 */
public class EZMQEventConverter {

    private final static EdgeXLogger logger = EdgeXLoggerFactory
            .getEdgeXLogger(EZMQEventConverter.class);

    /**
     * Convert EdgeX event to protocol buffer event byte array.
     *
     * @param event
     *            {@link Event}
     * @return Converted Protobuf event as byte array.
     */
    public static byte[] toProtoBuf(Event event) {

        if (null == event) {
            return null;
        }
        org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Event protoEventObj = null;
        org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Event protoEvent = null;
        try {

            protoEventObj = new org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Event(event);
            Builder eventbuild = EZMQProtoEvent.Event.newBuilder();
            eventbuild.setEdgeXReading(event);

            for (Reading reading : event.getReadings()) {
                org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Reading protoReading = new org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Reading(
                        reading);
                org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Reading.Builder readingBuild = EZMQProtoEvent.Reading
                        .newBuilder();
                readingBuild.setEdgeXReading(reading);
                eventbuild.addReading(readingBuild);
            }

            protoEvent = eventbuild.build();
        } catch (Exception e) {
            logger.error("toEvent: Invalid byte array", e.getMessage());
        }
        if (null == protoEvent) {
            return null;
        }
        return protoEvent.toByteArray();
    }

    /**
     * Convert byte[] array of Protocol buffer event to edgeX event.
     *
     * @param event
     *            Byte array to be converted to Event.
     *
     * @return EdgeX event.
     */
    public static Event toEdgeXEvent(byte[] event) {

        if (null == event) {
            return null;
        }

        Event edgexEvent = null;
        try {
            org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Event eventObj = org.edgexfoundry.ezmq.protobufevent.EZMQProtoEvent.Event
                    .parseFrom(event);
            if(null != eventObj) {
                edgexEvent = eventObj.getEdgeXEventObject();
            }
        } catch (Exception e) {
            logger.error("toEvent: Invalid byte array", e.getMessage());
        }
        return edgexEvent;
    }
}
