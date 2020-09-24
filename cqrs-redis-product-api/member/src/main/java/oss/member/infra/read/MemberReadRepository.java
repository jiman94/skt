package oss.member.infra.read;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oss.member.model.read.Member;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Repository
public interface MemberReadRepository extends JpaRepository<Member, String> {
	Member findAllById(String id);
}
