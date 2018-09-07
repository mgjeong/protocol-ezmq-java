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
  public void getStatusTest() {
    EZMQAPI instance = EZMQAPI.getInstance();
    assertEquals(EZMQErrorCode.EZMQ_OK, instance.initialize());
    assertEquals(EZMQStatusCode.EZMQ_Initialized, instance.getStatus());
    assertEquals(EZMQErrorCode.EZMQ_OK, instance.terminate());
    assertEquals(EZMQStatusCode.EZMQ_Terminated, instance.getStatus());
  }

  @Test
  public void getContextTest() {
    EZMQAPI instance = EZMQAPI.getInstance();
    assertEquals(EZMQErrorCode.EZMQ_OK, instance.initialize());
    assertNotNull(instance.getContext());
  }
}
