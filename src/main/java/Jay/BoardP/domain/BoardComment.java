package Jay.BoardP.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends TimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_coment_id")
    private Long id;


    private String nickName;
    private String content;
    private LocalDateTime time;
//    private CommentStatus status;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<BoardComment> child = new ArrayList<>();


    @OneToMany(mappedBy = "boardComment", cascade = CascadeType.ALL)
    private List<CommentDisLike> disLikes = new ArrayList<>();

    @OneToMany(mappedBy = "boardComment", cascade = CascadeType.ALL)
    private List<CommentLike> likes = new ArrayList<>();


//    @Formula("select count(*) from comment_like l where l.board_coment_id = board_coment_id")
    @Formula(value = "(select count(*) from comment_like l where l.board_coment_id = board_coment_id)")
    private Long countsOfLike;

    @Formula(value = "(select count(*) from comment_dis_like d where d.board_coment_id = board_coment_id)")
    private Long countsOfDisLike;

    public void setBoard(Board board) {

        this.board = board;
        board.getComments().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getBoardComments().add(this);
    }

    public void addChild(BoardComment comment) {
        this.child.add(comment);
        comment.setParent(this); // vs this.parent = this
    }

    public void setStatus() {
        this.isDeleted = DeleteStatus.N;
    }


    public static BoardComment createComment(Board board, Member member, String content,
        BoardComment parent) {
        BoardComment comment = new BoardComment();
        comment.setBoard(board);
        comment.setMember(member);
        comment.setNickName(member.getNickname());
        comment.setContent(content);
        comment.setParent(parent);
        comment.setStatus();

        return comment;
    }

    public static BoardComment createComment(Board board, Member member, String content) {
        BoardComment comment = new BoardComment();
        comment.setBoard(board);
        comment.setMember(member);
        comment.setNickName(member.getNickname());
        comment.setContent(content);
        comment.setStatus();
        return comment;
    }

    @Builder
    public BoardComment(Board board, Member member, String content, BoardComment parent,
        String nickName) {
        this.board = board;
        this.member = member;
        this.nickName = nickName;
        this.content = content;
        this.parent = parent;
    }

    public static void updateComment(BoardComment comment, String updateContent) {
        comment.setContent(updateContent);
    }

    public void changeDeletedStatus(DeleteStatus deleteStatus) {
        this.isDeleted = deleteStatus;
    }


}
