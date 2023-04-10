package Jay.BoardP.domain;


import Jay.BoardP.controller.form.PenaltyForm;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Penalty extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String title;
    private String content;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;



    public void set(Board board , Member member , PenaltyForm penaltyForm) {
        this.code = penaltyForm.getCode();
        this.title = penaltyForm.getTitle();
        this.content = penaltyForm.getContent();
        this.board = board;
        this.member = member;
        board.getPenalties().add(this);
        member.getPenaltyList().add(this);
    }

    public static Penalty createPenalty(Board board ,Member member , PenaltyForm penaltyForm) {
        Penalty penalty = new Penalty();
        penalty.set(board, member, penaltyForm);
        return penalty;
    }
}
