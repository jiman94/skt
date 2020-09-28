package oss.member.infra;

import oss.core.event.AbstractEventHandler;
import oss.core.event.EventStore;
import oss.core.snapshot.SnapshotRepository;
import oss.member.model.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
public class MemberEventHandler extends AbstractEventHandler<Member, String> {

	@Autowired
	public MemberEventHandler(EventStore eventStore,
		SnapshotRepository snapshotRepository) {
		super(eventStore, snapshotRepository);
	}
}
