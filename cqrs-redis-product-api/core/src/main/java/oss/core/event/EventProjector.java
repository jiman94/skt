package oss.core.event;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
public interface EventProjector {
	void handle(Event event);
}
