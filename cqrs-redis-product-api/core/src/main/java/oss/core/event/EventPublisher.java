package oss.core.event;

/**
 * Created by jaceshim on 2017. 3. 17..
 */
public interface EventPublisher<T extends RawEvent> {

	void publish(T rawEvent);
}
