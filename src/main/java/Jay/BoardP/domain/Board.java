package Jay.BoardP.domain;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Setter
public class Board extends TimeEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    private String createBy;

    private String ipAddress;

    private Boolean isDeleted;

    //연관관계의 주인은 , file -> 주인쪽에서 joinColum + set
    //부모는 ,Board -> 부모가 생성 & 삭제시 전파 -> 자식자동 생성 & 자동삭제
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();

    @ColumnDefault("0")
    private Long countVisit = 0L;

    // 카운트쿼리최적화
    // 1. 양방향매핑 , size이용시 쿼리가 비효율적으로 나간다
    // 2. jpql count는 조금은 생략이 가능하지만 , 그래도 여전히 쿼리가 많이나간다
    // 3. formula를 이용 , 생성시점에서 가상의 컬럼을 카운트쿼리로 만를어놓고 사용한다

    @Formula( value = "(select count(*) from POST_LIKE l where l.board_board_id = board_id)")
    private int countOfLikes;

    @Formula(value = "(select count(*) from BOARD_COMMENT C where C.board_id = board_id)")
    private int countOfComments;

    @Formula( value = "(select count(*) from PENALTY P where P.board_id = board_id)")
    private int countOfPenalties;


    @OneToMany(mappedBy = "board"  , cascade = CascadeType.ALL)
    @OrderBy("id asc")
    private List<BoardComment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "board" , cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Penalty> penalties = new ArrayList<>();

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //??
    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;



    public void setBoard(Member member , String title, String content, String ipAddress , String categoryCode) {
        this.title = title;
        this.content = content;
        this.createBy = member.getNickname();
        this.ipAddress = ipAddress;
        this.isDeleted = false;
        this.member = member;
        member.getBoardList().add(this);
        this.category = Category.createCategory(categoryCode);
        category.getBoards().add(this);
    }


    // setter 제거 !!!!!
//    public static Board createBoard(String userId, String title, String content ,String ipAddress) {
//        Board board = new Board();
//        board.setTitle(title);
//        board.setContent(content);
//        board.setCreateBy(userId);
//        board.setIpAddress(ipAddress);
//        return board;
//    }

    public static Board createBoard(String title, String content, String ipAddress, Member member , String categoryCode) {
        Board board = new Board();
        board.setBoard(member, title, content, ipAddress , categoryCode);
        return board;
    }


}
