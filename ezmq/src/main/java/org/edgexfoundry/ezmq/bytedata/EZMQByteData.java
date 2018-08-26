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

package org.edgexfoundry.ezmq.bytedata;

import org.edgexfoundry.ezmq.EZMQContentType;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.edgexfoundry.ezmq.EZMQMessage;

/**
 * This class represents EZMQ ByteData message format.
 */
public class EZMQByteData implements EZMQMessage {

  private byte[] mByteData;

  /**
   * Constructor for EZMQ Byte Data.
   *
   * @param data
   *            Byte data.
   */
  public EZMQByteData(byte[] data) {
    mByteData = data;
  }

  /**
   * Get the byte data.
   *
   * @return byte data.
   */
  public byte[] getByteData() {
    return mByteData;
  }

  /**
   * Set the byte data.
   *
   * @param byteData
   *            byteData to set
   *
   * @return byte data.
   */
  public EZMQErrorCode setByteData(byte[] byteData) {
    if (null == byteData) {
      return EZMQErrorCode.EZMQ_ERROR;
    }
    mByteData = byteData;
    return EZMQErrorCode.EZMQ_OK;
  }

  /**
   * Get Content type.
   *
   * @return Content type for Byte Data.
   */
  @Override
  public EZMQContentType getContentType() {
    return EZMQContentType.EZMQ_CONTENT_TYPE_BYTEDATA;
  }
}
