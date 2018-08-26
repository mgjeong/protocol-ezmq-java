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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.edgexfoundry.support.logging.client.EdgeXLogger;
import org.edgexfoundry.support.logging.client.EdgeXLoggerFactory;
import org.zeromq.ZMQ;

/**
 * Contains EZMQ APIs related to initialization, termination of EZMQ stack.
 */
public class EZMQAPI {

  private static EZMQAPI mInstance;
  public EZMQStatusCode status = EZMQStatusCode.EZMQ_Unknown;
  private ZMQ.Context mContext;
  private static boolean mIsSecured;

  // setting log level as per application.properties
  static {
    InputStream stream = null;
    try {
      Properties props = new Properties();
      stream = EZMQAPI.class.getResourceAsStream("/application.properties");
      props.load(stream);
      String mode = props.getProperty("ezmq.logging.level");
      if ((null != mode) && (mode.equalsIgnoreCase("DEBUG"))) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
      }
      String isSecured = props.getProperty("ezmq.security");
      if ((null != isSecured) && (isSecured.equalsIgnoreCase("TRUE"))) {
        mIsSecured = true;
        System.out.println("Security is enabled");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (null != stream) {
        try {
          stream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  private final static EdgeXLogger logger = EdgeXLoggerFactory.getEdgeXLogger(EZMQAPI.class);

  private EZMQAPI() {
    status = EZMQStatusCode.EZMQ_Constructed;
  }

  /**
   * Get instance of EZMQAPI Class.
   *
   * @return Instance of EZMQAPI.
   */
  public static synchronized EZMQAPI getInstance() {
    if (null == mInstance) {
      mInstance = new EZMQAPI();
    }
    return mInstance;
  }

  /**
   * Initialize required EZMQ components. This API should be called first,
   * before using any EZMQ APIs.
   *
   * @return {@link EZMQErrorCode}
   */
  public EZMQErrorCode initialize() {
    mContext = ZMQ.context(1);
    status = EZMQStatusCode.EZMQ_Initialized;
    logger.debug("EZMQ initialized");
    return EZMQErrorCode.EZMQ_OK;
  }

  /**
   * Perform cleanup of EZMQ components.
   *
   * @return {@link EZMQErrorCode}
   */
  public EZMQErrorCode terminate() {
    if (null != mContext) {
      mContext.term();
      mContext = null;
      status = EZMQStatusCode.EZMQ_Terminated;
      logger.debug("EZMQ terminated");
    }
    return EZMQErrorCode.EZMQ_OK;
  }

  /**
   * Return status of EZMQ service.
   *
   * @return {@link EZMQStatusCode}
   */
  public EZMQStatusCode getStatus() {
    return status;
  }

  ZMQ.Context getContext() {
    return mContext;
  }

  boolean isSecured() {
    return mIsSecured;
  }
}
