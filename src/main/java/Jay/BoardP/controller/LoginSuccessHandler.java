package Jay.BoardP.controller;

import static Jay.BoardP.controller.RedisAttributes.*;

import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.MemberRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Transactional
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;

    private final RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {


        String name = authentication.getName();

        Member member = memberRepository.findMemberByUserId(name).get();

        member.setLoginDate(LocalDateTime.now());

        makeUpdateCount(SIGNINPERDAY);

        response.sendRedirect("/");


    }

    private void makeUpdateCount(String key) {
        if (!redisTemplate.hasKey(key)) {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set(key, 0L);
            valueOperations.increment(key);
        } else {
            redisTemplate.opsForValue().increment(key);
        }
    }

}
