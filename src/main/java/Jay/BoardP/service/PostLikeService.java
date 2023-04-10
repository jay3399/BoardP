package Jay.BoardP.service;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.Member;
import Jay.BoardP.domain.PostLike;
import Jay.BoardP.repository.BoardRepository;
import Jay.BoardP.repository.MemberRepository;
import Jay.BoardP.repository.PostLikeRepository;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final BoardRepository boardRepository;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final PostLikeRepository repository;


    public Boolean pushLike(Long boardId,  Long memberId) {

        String key = "postLikeCnt::" + boardId + "::" + memberId;

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String s = UUID.randomUUID().toString();

        if (valueOperations.get(key) == null) {
            valueOperations.set(key, s);
            return true;
        }

        return false;
    }

    @Scheduled(cron = "59 * * * * *")
    public void pushLikeToDB() {
        Set<String> keys = redisTemplate.keys("postLikeCnt*");

        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String result = iterator.next();
            Long boardId = Long.parseLong(result.split("::")[1]);
            Long memberId = Long.parseLong(result.split("::")[2]);


            Board board1 = boardRepository.findOne(boardId);
            Member member = memberRepository.findMember(memberId);

            repository.exist(memberId, boardId)
                .ifPresentOrElse(
                    (postLike) -> removePostLike(postLike, member , board1)
                    ,
                    () -> repository.save(PostLike.createPostLike(member, board1)));



            redisTemplate.delete(result);
        }

    }


    @CacheEvict(value = "board" , key = "#boardId")
    public Boolean pushLikeButton(Long boardId  , Long memberId) {

        Board board1 = boardRepository.findBoard(boardId);
        Member member = memberRepository.findMember(memberId);

        repository.exist(memberId, boardId)
            .ifPresentOrElse(
                (postLike) -> removePostLike(postLike, member , board1)
                ,
                () -> repository.save(PostLike.createPostLike(member, board1)));

        return true;
    }

    public Long totalNumOfLikeOnBoard(Long boardId) {
        return repository.findPostLikeNum(boardId);
    }

    private void removePostLike(PostLike postLike, Member member , Board board ) {
        postLike.removeMember(member, board);
        repository.deleteById(postLike.getId());
    }





}
