package Jay.BoardP.controller.dto;


import Jay.BoardP.domain.Board;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AdBoardListDto {

    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime createDate;
    private Long viewCount;
    private Long likeCount;
    private Long penaltyCount;

    private Long totalComments;

    private Boolean isDeleted;


    public void set(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.nickname = board.getCreateBy();
        this.createDate = board.getCreatedDate();
        this.viewCount = board.getCountVisit();
        this.isDeleted = board.getIsDeleted();
        this.likeCount = Long.valueOf(board.getCountOfLikes());
        this.penaltyCount = Long.valueOf(board.getCountOfPenalties());
        this.totalComments = Long.valueOf(board.getCountOfComments());
    }

    public static AdBoardListDto createAdBoardList(Board board) {
        AdBoardListDto adBoardListDto = new AdBoardListDto();
        adBoardListDto.set(board);
        return adBoardListDto;
    }


}
