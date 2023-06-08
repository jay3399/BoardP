package Jay.BoardP.repository;

import static Jay.BoardP.domain.QFile.file;
import static Jay.BoardP.domain.QMember.*;

import Jay.BoardP.controller.dto.FileDto;
import Jay.BoardP.controller.dto.MemberListDto;
import Jay.BoardP.domain.QMember;
import Jay.BoardP.repository.customInter.MemberRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;


public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  @Autowired
  public MemberRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<MemberListDto> getMemberListDto() {

    return jpaQueryFactory.select(
        Projections.constructor(
            MemberListDto.class,
            member.id,
            member.userId,
            member.nickname,
            member.createdDate,
            member.role,
            member.phoneNumber,
            member.email,
            member.countOfBoards,
            member.countOfComments)).from(member).fetch();
  }


}
