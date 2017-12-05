package org.edgexfoundry.ezmq.subscriber;

import org.edgexfoundry.ezmq.EZMQAPI;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.edgexfoundry.ezmq.EZMQStatusCode;
import org.edgexfoundry.ezmq.EZMQSubscriber;
import org.edgexfoundry.ezmq.EZMQSubscriber.EZMQSubCallback;

/**
 * This class is singleton style wrapper of EZMQ Subscribe function.
 */
public class Subscriber {
	private static Subscriber singleton;
	private static EZMQAPI apiInstance;
	private static EZMQSubscriber subInstance;
	private static EZMQErrorCode result = EZMQErrorCode.EZMQ_ERROR;
	private static EZMQStatusCode status = EZMQStatusCode.EZMQ_Unknown;

	private Subscriber() {
		apiInstance = EZMQAPI.getInstance();
		if (apiInstance == null) {
			throw new NullPointerException("apiInstance is null");
		}
		status = apiInstance.getStatus();
		if (status != EZMQStatusCode.EZMQ_Initialized) {
			apiInstance.initialize();
		}
	}

	/**
	 * Get Subscriber class instance
	 *
	 * @return {@link Subscriber}
	 */
	public static Subscriber getInstance() {
		if (singleton == null) {
			singleton = new Subscriber();
		}
		return singleton;
	}

	/**
	 * Initialize EZMQ stack and subscribe with host:port and topic
	 *
	 * @param host
	 *            host for subscribe
	 * @param port
	 *            port for subscribe
	 * @param @Nullable
	 *            topic topic for subscribe, it can be null
	 * @param cb
	 *            {@link EZMQSubCallback}
	 * @return {@link EZMQErrorCode}
	 *
	 * @throws {@link NullPointerException}
	 */
	public EZMQErrorCode startSubscriber(String host, int port, String topic, EZMQSubCallback cb) {
		if (host == null || cb == null) {
			throw new NullPointerException("Callback is null");
		}

		result = stopSubscriber();
		if (result != EZMQErrorCode.EZMQ_OK) {
			return result;
		}

		subInstance = new EZMQSubscriber(host, port, cb);
		if (subInstance == null) {
			throw new NullPointerException("subInstance is null");
		}
		result = subInstance.start();
		if (result != EZMQErrorCode.EZMQ_OK) {
			return result;
		}

		if (topic == null) {
			result = subInstance.subscribe();
		} else {
			result = subInstance.subscribe(topic);
		}

		return result;
	}

	/**
	 * Initialize EZMQ stack and subscribe with host:port and topic
	 *
	 * @param serviceName
	 *            serviceName for subscribe
	 * @param @Nullable
	 *            topic topic for subscribe, it can be null
	 * @param cb
	 *            {@link EZMQSubCallback}
	 * @return {@link EZMQErrorCode}
	 *
	 * @throws {@link NullPointerException}
	 */
	public EZMQErrorCode startSubscriber(String serviceName, String topic, EZMQSubCallback cb) {
		if (serviceName == null || cb == null) {
			throw new NullPointerException("Callback is null");
		}

		result = stopSubscriber();
		if (result != EZMQErrorCode.EZMQ_OK) {
			return result;
		}

		subInstance = new EZMQSubscriber(serviceName, cb);
		if (subInstance == null) {
			throw new NullPointerException("subInstance is null");
		}
		result = subInstance.start();
		if (result != EZMQErrorCode.EZMQ_OK) {
			return result;
		}

		if (topic == null) {
			result = subInstance.subscribe();
		} else {
			result = subInstance.subscribe(topic);
		}

		return result;
	}

	/**
	 * Stop EZMQ stack
	 *
	 * @return {@link EZMQErrorCode}
	 */
	public EZMQErrorCode stopSubscriber() {
		if (subInstance != null) {
			result = subInstance.stop();
			if (result != EZMQErrorCode.EZMQ_OK) {
				System.out.println("already stopped");
				return result;
			} else {
				subInstance = null;
				return EZMQErrorCode.EZMQ_OK;
			}
		}
		return EZMQErrorCode.EZMQ_OK;
	}
}
