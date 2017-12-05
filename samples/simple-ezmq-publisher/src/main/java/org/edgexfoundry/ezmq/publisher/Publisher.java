package org.edgexfoundry.ezmq.publisher;

import java.util.List;

import org.edgexfoundry.domain.core.Event;
import org.edgexfoundry.ezmq.EZMQAPI;
import org.edgexfoundry.ezmq.EZMQCallback;
import org.edgexfoundry.ezmq.EZMQErrorCode;
import org.edgexfoundry.ezmq.EZMQPublisher;
import org.edgexfoundry.ezmq.EZMQStatusCode;

/**
 * This class is singleton style wrapper of EZMQ Publish function.
 */
public class Publisher {
	private static Publisher singleton;
	private static EZMQAPI apiInstance;
	private static EZMQPublisher pubInstance;
	private static EZMQErrorCode result = EZMQErrorCode.EZMQ_ERROR;
	private static EZMQStatusCode status = EZMQStatusCode.EZMQ_Unknown;

	private Publisher() {
		apiInstance = EZMQAPI.getInstance();
		if (apiInstance == null) {
			throw new NullPointerException("apiInstance is null");
		}
		apiInstance.initialize();
		status = apiInstance.getStatus();
		if (status != EZMQStatusCode.EZMQ_Initialized) {
			apiInstance.initialize();
		}
	}

	/**
	 * Get Publisher class instance
	 *
	 * @return {@link Publisher}
	 */
	public static Publisher getInstance() {
		if (singleton == null) {
			singleton = new Publisher();
		}
		return singleton;
	}

	/**
	 * Initialize EZMQ stack for publish
	 *
	 * @param port
	 *            port for publish
	 * @param cb
	 *            {@link EZMQCallback}
	 *
	 * @return {@link EZMQErrorCode}
	 *
	 * @throws {@link NullPointerException}
	 */
	public EZMQErrorCode startPublisher(int port, EZMQCallback cb) {
		if (cb == null) {
			throw new NullPointerException("Callback is null");
		}

		pubInstance = new EZMQPublisher(port, cb);
		if (pubInstance == null) {
			throw new NullPointerException("pubInstance is null");
		}
		result = pubInstance.start();

		if (result != EZMQErrorCode.EZMQ_OK) {
			pubInstance = null;
			System.out.println("Could not start EZMQ...");
		}
		return result;
	}

	/**
	 * Stop EZMQ stack
	 *
	 * @return {@link EZMQErrorCode}
	 */
	public EZMQErrorCode stopPublisher() {
		if (pubInstance != null) {
			result = pubInstance.stop();
			if (result != EZMQErrorCode.EZMQ_OK) {
				return result;
			} else {
				pubInstance = null;
				return EZMQErrorCode.EZMQ_OK;
			}
		}

		return EZMQErrorCode.EZMQ_OK;
	}

	/**
	 * Initialize EZMQ stack for publish
	 *
	 * @param event
	 *            EdgeX Event class instance for publish
	 *
	 * @return {@link EZMQErrorCode}
	 *
	 * @throws {@link NullPointerException}
	 */
	public EZMQErrorCode publishEvent(Event event) {
		EZMQErrorCode ret = EZMQErrorCode.EZMQ_ERROR;
		if (event == null) {
			throw new NullPointerException("Delivered argument is null");
		} else {
			ret = pubInstance.publish(event);
		}

		return ret;
	}

	/**
	 * Initialize EZMQ stack for publish
	 *
	 * @param topic
	 *            Topic on which event needs to be published
	 *
	 * @param event
	 *            EdgeX Event class instance for publish
	 *
	 * @return {@link EZMQErrorCode}
	 *
	 * @throws {@link NullPointerException}
	 */
	public EZMQErrorCode publishEvent(String topic, Event event) {
		EZMQErrorCode ret = EZMQErrorCode.EZMQ_ERROR;
		if (event == null || topic == null) {
			throw new NullPointerException("Delivered arguments are null");
		} else {
			ret = pubInstance.publish(topic, event);
		}

		return ret;
	}

	/**
	 * Initialize EZMQ stack for publish
	 *
	 * @param topics
	 *            Topics on which event needs to be published
	 *
	 * @param event
	 *            EdgeX Event class instance for publish
	 *
	 * @return {@link EZMQErrorCode}
	 *
	 * @throws {@link NullPointerException}
	 */
	public EZMQErrorCode publishEvent(List<String> topics, Event event) {
		EZMQErrorCode ret = EZMQErrorCode.EZMQ_ERROR;
		if (event == null || topics == null) {
			throw new NullPointerException("Delivered arguments are null");
		} else {
			ret = pubInstance.publish(topics, event);
		}

		return ret;
	}

}
