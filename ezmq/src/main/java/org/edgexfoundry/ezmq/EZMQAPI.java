package org.edgexfoundry.ezmq;

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

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
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

    // For EZMQ internal use
    public ZMQ.Context getContext() {
        return mContext;
    }
}
