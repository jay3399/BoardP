package Jay.BoardP.service;


import Jay.BoardP.controller.dto.MemberFormDto;
import Jay.BoardP.controller.dto.Role;
import Jay.BoardP.controller.form.UpdateForm;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class memberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long save(MemberFormDto member) {

        memberRepository.save(member.createMember(passwordEncoder.encode(member.getPassword())));

        return member.getId();

    }

    public Member findById(Long id) {
        return memberRepository.findMember(id);
    }

    public Member findOne(Long id) {
        return memberRepository.findMember(id);
    }

    public Member findByUserId(String userId) {
        Optional<Member> memberByUserId = memberRepository.findMemberByUserId(userId);
        return memberByUserId.get();
    }

    public Member findByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }


    public Member isExistEmailAndUserId(String email , String userId) {

        Member memberByEmail = memberRepository.findMemberByEmailAndUserId(email, userId);

        return memberByEmail;
    }


    @Transactional
    public void editPassword(Long memberId, String password) {
        Member member = memberRepository.findMember(memberId);
        member.setPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void deleteMember(Member member) {
        member.setRole(Role.DELETED);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    @Transactional
    public void editMember(Long id, UpdateForm updateForm) {
        Member member = memberRepository.findMember(id);
        member.setNickname(updateForm.getNickname());
        member.setBirthDate(updateForm.getBirthDate());
    }

    public void duplicated(String userId) {

        Member byUserId = findByUserId(userId);

        if (byUserId != null) {
            throw new IllegalStateException("이미존재하는 아이디입니다.");
        }
    }


    @Transactional
    public void releaseHuman(Long id) {

        Member member = memberRepository.findMember(id);
        member.setRole(Role.USER);

    }
}
