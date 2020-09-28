package oss.core.event;

import java.util.List;

import oss.core.domain.AggregateRoot;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
public interface EventHandler<A extends AggregateRoot, ID> {

	/**
	 * Save the aggregate
	 *
	 * @param aggregate
	 */
	void save(A aggregate);

	/**
	 * Get the aggregate
	 *
	 * @param identifier
	 * @return
	 */
	A find(ID identifier);

	/**
	 * Get the All aggregate
	 * @return
	 */
	List<A> findAll();
}
