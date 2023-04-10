package Jay.BoardP.domain;

import Jay.BoardP.controller.dto.MemberFormDto;
import Jay.BoardP.controller.dto.Role;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Member extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String userId;

    private String password;

    private String nickname;

    private Date birthDate;

    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private LocalDateTime loginDate;

    @OneToMany(mappedBy = "member")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BoardComment> boardComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<CommentDisLike> commentDisLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikeList = new ArrayList<>();

    // @연관관계가 둘과잡혀있다 . 유일한소유자가 아니므로 , 전파는 오류를 발생할 확률이 굉장히 높음
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Penalty> penaltyList = new ArrayList<>();

    // @위와마찬가지
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();


    @Formula(value = "(select count(*) from BOARD b where b.member_id = member_id)")
    private Long countOfBoards;

    @Formula(value = "(select count(*) from BOARD_COMMENT b where b.member_id = member_id)")
    private Long countOfComments;

    @Enumerated(EnumType.STRING)
    private Role role;


    public void setMember(MemberFormDto dto) {
        this.userId = dto.getUserId();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
        this.birthDate = dto.getBirthDate();
        this.phoneNumber = dto.getPhone();
        this.email = dto.getEmail();
        this.role = Role.USER;
    }

    public static Member createMember(MemberFormDto dto) {
        Member member = new Member();
        member.setMember(dto);
        return member;
    }

    @Builder
    public Member(String userId , String password , String nickname , Date birthDate , String phoneNumber , String email) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.phoneNumber =phoneNumber;
        this.email = email;
        this.role = Role.USER;
    }


}
