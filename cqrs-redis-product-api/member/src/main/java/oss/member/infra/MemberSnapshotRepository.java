package oss.member.infra;

import oss.core.snapshot.Snapshot;
import oss.core.snapshot.SnapshotRepository;
import oss.member.model.Member;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jaceshim on 2017. 3. 9..
 */
@Repository
public class MemberSnapshotRepository implements SnapshotRepository<Member, String> {

	private final List<Snapshot<Member, String>> snapshots = new CopyOnWriteArrayList<>();

	@Override
	public Optional<Snapshot<Member, String>> findLatest(String identifier) {
		return snapshots.stream()
			.filter(snapshot -> snapshot.getIdentifier().equals(identifier))
			.reduce((s1, s2) -> s1.getVersion() > s2.getVersion() ? s1 : s2);
	}

	@Override
	public void save(Snapshot<Member, String> snapshot) {
		snapshots.add(snapshot);
	}
}
