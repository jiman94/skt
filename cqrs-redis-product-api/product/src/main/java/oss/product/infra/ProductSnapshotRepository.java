package oss.product.infra;

import oss.core.snapshot.Snapshot;
import oss.core.snapshot.SnapshotRepository;
import oss.product.model.Product;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jaceshim on 2017. 3. 9..
 */
@Repository
public class ProductSnapshotRepository implements SnapshotRepository<Product, Long> {

	private final List<Snapshot<Product, Long>> snapshots = new CopyOnWriteArrayList<>();

	@Override
	public Optional<Snapshot<Product, Long>> findLatest(Long identifier) {
		return snapshots.stream()
			.filter(snapshot -> snapshot.getIdentifier().equals(identifier))
			.reduce((s1, s2) -> s1.getVersion() > s2.getVersion() ? s1 : s2);
	}

	@Override
	public void save(Snapshot<Product, Long> snapshot) {
		snapshots.add(snapshot);
	}
}
