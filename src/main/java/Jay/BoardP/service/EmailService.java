package Jay.BoardP.service;


import java.time.Duration;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    public final JavaMailSender javaMailSender;

    private final RedisTemplate redisTemplate;


    @Async("async")
    public void mailCheck(String email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);

        Random random = new Random();
        String key = "";

        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(25)+65;
            key += (char) index;
        }

        int numIndex = random.nextInt(9999)+1000 ;

        key += numIndex;

        message.setSubject("인증번호 입력을 위한 메일 전송");
        message.setText("인증번호 : " + key);

        redisTemplate.opsForValue().set(email, key, Duration.ofMinutes(3));

        javaMailSender.send(message);

    }

    @Async("async")
    public void mailCheckForUserId(String email, String userId) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("아이디확인을 위한 메일 전송");
        message.setText("아이디 : " + userId);

        javaMailSender.send(message);


    }




}
