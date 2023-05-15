package Jay.BoardP.service;


import Jay.BoardP.controller.dto.BoardCommentDto;
import Jay.BoardP.controller.dto.CommentDto;
import Jay.BoardP.controller.dto.MyCommentDto;
import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.BoardComment;
import Jay.BoardP.domain.DeleteStatus;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.BoardRepository;
import Jay.BoardP.repository.CommentRepositoryRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepositoryRepository repository;
    private final memberService memberService;
    private final BoardRepository boardRepository;


    @Caching(evict = {
        @CacheEvict(value = "board", key = "#boardId"),
        @CacheEvict(value = "comment", key = "#boardId")}
    )
    @Transactional
    public Long addCommentV2(CommentDto commentDto) {

        Member member = memberService.findOne(commentDto.getMemberId());
        Board board = boardRepository.findBoard(commentDto.getBoardId());
        String content = commentDto.getContent();

        if (commentDto.getParentId() == null) {

            BoardComment comment = BoardComment.createComment(board, member, content);

            repository.save(comment);

            return comment.getId();
        }

        BoardComment parent = findOne(commentDto.getParentId());
        BoardComment comment = BoardComment.createComment(board, member, content, parent);

        repository.save(comment);
        return comment.getId();
    }

//    @Caching(evict = {
//        @CacheEvict(value = "board", key = "#boardId"),
//        @CacheEvict(value = "comment", key = "#boardId")}
//    )
//    @Transactional
//    public Long addComment(Long memberId, Long boardId, String content, Long parentId) {
//
//        Member member = memberService.findOne(memberId);
//
//        Board board = boardRepository.findBoard(boardId);
//
//        BoardComment parent = findOne(parentId);
//
//        BoardComment comment = BoardComment.createComment(board, member, content, parent);
//
//        repository.save(comment);
//
//        return comment.getId();
//    }
//
//
//
//    @Caching(evict = {
//        @CacheEvict(value = "board", key = "#boardId"),
//        @CacheEvict(value = "comment", key = "#boardId")}
//    )
//    @Transactional
//    public Long addComment(Long memberId, Long boardId, String content) {
//
//        Member member = memberService.findOne(memberId);
//        Board board = boardRepository.findBoard(boardId);
//
//        BoardComment comment = BoardComment.createComment(board, member, content);
//
//        repository.save(comment);
//
//        return comment.getId();
//
//    }




    public BoardComment findOne(Long commentId) {
        return repository.findComment(commentId);
    }

    public List<BoardCommentDto> findByBoardId(Long boardId) {
        List<BoardComment> byBoardId = repository.findByBoardId(boardId);
        return byBoardId.stream().map(BoardCommentDto::fromEntity)
            .collect(
                Collectors.toList());
    }

    @Cacheable(value = "comment", cacheManager = "cacheManagerV3", key = "#boardId")
    public List<BoardCommentDto> findByBoardIdV2(Long boardId) {
        List<BoardComment> comments = repository.findByBoardIdV2(boardId);


        List<BoardCommentDto> result = new ArrayList<>();
        Map<Long, BoardCommentDto> map = new HashMap<>();

        comments.stream().forEach(boardComment -> {

                BoardCommentDto boardCommentDto = BoardCommentDto.fromEntity(boardComment);
                //n+1 문제때문에 ,  계속해서 쿼라문을 날린다 .

                map.put(boardCommentDto.getId(), boardCommentDto);
                //dto생성후 , map에 해당 id와 value값 넣고
                //부모가 존재하면 = 자식이면 ->
                //해당 dto객체에 부모 id값 셋팅후 ,맵에서 부모아이디값으로 child벨류값 뽑고 해당 dto 넣기
            if (boardComment.getParent() != null) {
                boardCommentDto.setParentId(boardComment.getParent().getId());
                Long id = boardComment.getParent().getId();
                map.get(boardComment.getParent().getId()).getChild().add(boardCommentDto);
            } else {
                result.add(boardCommentDto);
            }
            }
        );
        return result;
    }

    public List<MyCommentDto> getBoardComments(Long memberId) {
        List<BoardComment> allByMember_id = repository.findAllByMember_Id(memberId);

        return allByMember_id.stream().map(
            MyCommentDto::createMyComment
        ).collect(Collectors.toList());


    }


    public void UpdateComment(Long commentId, String updateComment) {
        BoardComment comment = repository.findComment(commentId);
//        comment.setContent(updateComment);
        BoardComment.updateComment(comment, updateComment);
    }


    public Long totalComments(Long boardId) {
        return repository.totalComments(boardId);
    }


    @Caching(evict = {
        @CacheEvict(value = "board", key = "#boardId"),
        @CacheEvict(value = "comment", key = "#boardId")}
    )
    @Transactional
    public Boolean deleteComment(Long commentId, Long boardId) {
        BoardComment comment = repository.findByIdWithParent(commentId).orElseThrow(null);

        if (comment.getChild().size() != 0) {
            comment.changeDeletedStatus(DeleteStatus.Y);
        } else {
            repository.delete(getDeletableAncestorComment(comment));
        }

        return true;
    }

    private BoardComment getDeletableAncestorComment(BoardComment comment) {
        BoardComment parent = comment.getParent();
        if (parent != null && parent.getChild().size() == 1
            && parent.getIsDeleted() == DeleteStatus.Y) {
            repository.delete(comment);
            return getDeletableAncestorComment(parent);
        }
        return comment;
    }


}
