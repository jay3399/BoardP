package Jay.BoardP.controller.dto;


import Jay.BoardP.domain.BoardComment;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyCommentDto {

    private Long id;
    private String content;

    private Long likeNum;
    private Long disLikeNum;

    private Long boardNum;

    private LocalDateTime createdDate;

    private String boardTitle;


    public void setBoardComment(BoardComment boardComment) {
        this.id = boardComment.getId();
        this.content = boardComment.getContent();
        this.likeNum = boardComment.getCountsOfLike();
        this.disLikeNum = boardComment.getCountsOfDisLike();
        this.createdDate = boardComment.getCreatedDate();
        this.boardNum = boardComment.getBoard().getId();
    }

    public static MyCommentDto createMyComment(BoardComment boardComment) {
        MyCommentDto myComment = new MyCommentDto();
        myComment.setBoardComment(boardComment);
        return myComment;
    }


}
