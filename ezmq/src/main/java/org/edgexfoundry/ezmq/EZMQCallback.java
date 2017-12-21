package org.edgexfoundry.ezmq;

/**
 * Interface to receive callback from EZMQ. Note: As of now not being used.
 */
public interface EZMQCallback {

    /**
     * Invoked on start of PUB/SUB instance.
     *
     * @param code
     *            {@link EZMQErrorCode}
     */
    public void onStartCB(EZMQErrorCode code);

    /**
     * Invoked on stop of PUB/SUB instance.
     *
     * @param code
     *            {@link EZMQErrorCode}
     */
    public void onStopCB(EZMQErrorCode code);

    /**
     * Invoked on error in EZMQ.
     *
     * @param code
     *            {@link EZMQErrorCode}
     */
    public void onErrorCB(EZMQErrorCode code);

}
