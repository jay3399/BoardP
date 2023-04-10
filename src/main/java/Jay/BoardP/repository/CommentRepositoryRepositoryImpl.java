package Jay.BoardP.repository;

import static Jay.BoardP.domain.QBoardComment.boardComment;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.domain.QBoardComment;
import Jay.BoardP.repository.customInter.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentRepositoryRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;


    @Autowired
    public CommentRepositoryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BoardComment> findByBoardId(Long boardId) {
        Board board = em.find(Board.class, boardId);
        return board.getComments();
    }

    @Override
    public List<BoardComment> findByBoardIdV2(Long boardId) {
        QBoardComment qBoardComment = new QBoardComment("q");
        return queryFactory.selectFrom(qBoardComment)
            .leftJoin(qBoardComment.parent).fetchJoin()
            .where(qBoardComment.board.id.eq(boardId))
            .orderBy(qBoardComment.parent.id.asc().nullsFirst(), qBoardComment.createdDate.asc()
            ).fetch();    }

    @Override
    public Long totalComments(Long boardId) {
        int size = queryFactory.selectFrom(boardComment).where(boardComment.board.id.eq(boardId))
            .fetch()
            .size();

        return (long) size;    }

    @Override
    public Optional<BoardComment> findByIdWithParent(Long commentId) {
        String jpql = "select c from BoardComment c left join fetch c.parent where c.id=:id";

        BoardComment id = (BoardComment) em.createQuery(jpql).setParameter("id", commentId).getSingleResult();

        return Optional.of(id);    }





}
