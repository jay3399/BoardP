package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Board;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyBoardListDto {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime date;

    private Long countVisit;

    private Long postLike;

    private Long totalComments;

    private void set(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.date = board.getCreatedDate();
        this.countVisit = board.getCountVisit();
        this.postLike = Long.valueOf(board.getCountOfLikes());
        this.totalComments = Long.valueOf(board.getCountOfComments());
    }

    public static MyBoardListDto createMyBoardList(Board board) {
        MyBoardListDto result = new MyBoardListDto();
        result.set(board);
        return result;
    }





}
