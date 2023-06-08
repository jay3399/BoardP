package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.MemberRepository;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListDto {

    private Long id;
    private String userId;
    private String nickname;
    private LocalDateTime createDate;
    private Role role;

    private String phoneNumber;
    private String email;

    private Long countOfBoards;

    private Long countOfComments;


    @QueryProjection
    public MemberListDto(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.createDate = member.getCreatedDate();
        this.role = member.getRole();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
        this.countOfBoards = member.getCountOfBoards();
        this.countOfComments = member.getCountOfComments();
    }
//
//
//    public void setMemberListDto(Member member) {
//        this.id = member.getId();
//        this.userId = member.getUserId();
//        this.nickname = member.getNickname();
//        this.createDate = member.getCreatedDate();
//        this.role = member.getRole();
//        this.phoneNumber = member.getPhoneNumber();
//        this.email = member.getEmail();
//        this.countOfBoards = member.getCountOfBoards();
//        this.countOfComments = member.getCountOfComments();
//    }
//
//
//    public static MemberListDto from(Member member) {
//
//        MemberListDto dto = new MemberListDto();
//        dto.setMemberListDto(member);
//        return dto;
//
//    }
//
//









}
