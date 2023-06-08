package Jay.BoardP.repository;

import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.customInter.MemberRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>  , MemberRepositoryCustom {

    Optional<Member> findMemberByUserId(String userId);

    Member findMemberByEmail(String email);

    @Query("select m from Member m where m.id =:id")
    Member findMember(@Param("id") Long id);

    Member findMemberByEmailAndUserId(String email, String userId);


}
