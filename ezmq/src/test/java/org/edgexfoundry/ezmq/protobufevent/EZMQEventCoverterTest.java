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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.edgexfoundry.ezmq.domain.core.Event;
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
