package Jay.BoardP.service;

import Jay.BoardP.repository.BoardRepository;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RedisService {


    private final Long ExpireDuration = 30L;
    private final RedisTemplate redisTemplate;

    private final BoardRepository boardRepository;


    public boolean isFirstRequest(String clientAddress, Long postId) {
        String key = getKey(clientAddress, postId);

        if (redisTemplate.hasKey(key)) {
            return false;
        }
        return true;
    }
    public void writeRequest(String clientAddress, Long postId) {
        String key = getKey(clientAddress, postId);

        redisTemplate.opsForValue().set(key, clientAddress);
        redisTemplate.expire(key, 1L, TimeUnit.DAYS);
        //중복방지 -> 하루동안


    }

    @Scheduled(cron = "0 */2 * * * *")
    public void deleteViewCountFromRedis() {
        Set<String> keys = redisTemplate.keys("viewCnt*");

        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String result = iterator.next();
            Long boardId = Long.parseLong(result.split("::")[1]);
            Long viewCount = Long.parseLong(
                String.valueOf(redisTemplate.opsForValue().get(result)));
            boardRepository.addViewCountWithRedis(boardId, viewCount);

            redisTemplate.delete(result);
        }



    }

    private static String getKey(String clientAddress, Long postId) {
        return clientAddress + "+" + postId;
    }



}
