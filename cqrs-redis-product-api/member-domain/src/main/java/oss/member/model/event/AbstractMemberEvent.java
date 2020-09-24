package oss.member.model.event;

import lombok.Getter;
import oss.core.event.Event;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public abstract class AbstractMemberEvent implements Event<String> {
	protected String id;

	@Override
	public String getIdentifier() {
		return this.id;
	}
}
