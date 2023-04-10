package Jay.BoardP.repository;

import static Jay.BoardP.domain.QPostLike.postLike;

import Jay.BoardP.domain.PostLike;
import Jay.BoardP.domain.QPostLike;
import Jay.BoardP.repository.customInter.PostLikeCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

//@Repository
public class PostLikeRepositoryImpl implements PostLikeCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public PostLikeRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<PostLike> exist(Long memberId, Long boardId) {

        QPostLike qPostLike = new QPostLike("q");

        PostLike postLike = jpaQueryFactory.selectFrom(qPostLike)
            .where(qPostLike.member.id.eq(memberId), qPostLike.board.id.eq(boardId)).fetchFirst();

        return Optional.ofNullable(postLike);


    }

    @Override
    public Long findPostLikeNum(Long boardId) {
        return jpaQueryFactory.selectFrom(postLike).where(postLike.board.id.eq(boardId))
            .fetchCount();
    }
}
