package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Member;
import java.sql.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long id;

    private String userId;

    private String password;

    private String nickname;

    private Date birthDate;

    private String phoneNumber;

    private String email;



    public void setMember(Member member) {
        this.id = member.getId();
        this.userId = member.getUserId();
        this.password = member.getPassword();
        this.nickname =  member.getNickname();
        this.birthDate = member.getBirthDate();
        this.phoneNumber = member.getPhoneNumber();
        this.email = member.getEmail();
    }


    public static MemberDto crateMemberDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMember(member);
        return memberDto;
    }




}
