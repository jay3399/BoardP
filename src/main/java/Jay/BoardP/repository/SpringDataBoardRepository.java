package Jay.BoardP.repository;

import Jay.BoardP.domain.Board;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface SpringDataBoardRepository extends JpaRepository<Board, Long> ,
    QuerydslPredicateExecutor<Board> {


    Page<Board> findAllByCategory_Code(Pageable pageable,String categoryCode);



    List<Board> findAllByMember_Id(Long memberId);


    //EntityGraph == fetchJoin
    //한번에 가져오지않고 , 조회할때마다 쿼리를 날린다
    //n+1 문제 해결

    //pageable && native query 함께써도 전혀 문제없다
    //메서드 이름또한 바꿔도 상관없다 , 하지만 아래는 이름규칙을따라서 마음대로 바꾸면 안된다 .
    @EntityGraph(attributePaths = {"category", "files"})
    @Query(
        value = "select b from Board b inner join Category c on b.category.id = c.id and b.isDeleted = false order by case when c.code ='NOTICE' then 1 else 2 end, b.id desc ")
    Page<Board> findAllNotice(Pageable pageable);


    @EntityGraph(attributePaths = {"category", "files"})
    Page<Board> findAll(Predicate predicate, Pageable pageable);

    //아우터조인 가능하고 , 컬렉션가져와도 뻥튀기 오류가 안뜬다.
    //페치조인은 '둘이상' 의 컬랙션을 가져오면 안된다  -> 컬렉션은 무조건 1개! , 엔티티는 상관이없다 .
    //하지만 , 페이징api에서는 일대다는 데이터뻥튀기문제떄문에 경고가 난다 , 위 참고
    //em.find n+1이슈 -> 패치조인해결
    //일대다 두개 페치조인상태 -> 위험하다  , -> 분리한다 .
    @Query("select b from Board b join fetch b.category left join fetch b.files where b.id = :id")
    Board findBoard(@Param("id") Long id);


    List<Board> findByIsDeletedAndModifiedDateLessThan(Boolean isDeleted,
        LocalDateTime modifiedDate);


    void deleteAllByIsDeleted(Boolean isDeleted);

}

