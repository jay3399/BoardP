package Jay.BoardP.controller.dto;
import Jay.BoardP.domain.Board;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDetailedDto{

    private Long id;

    private String nickName;
    private String title;
    private String content;

//    private Long countVisit;

    private Long postLike;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    private String ipAddress;

    private String categoryName;

    private Boolean isDeleted;

    private Long commentsSize;

    private List<BoardCommentDto> comments = new ArrayList<>();

    public void setBoardDetailed(Board board) {
        this.id = board.getId();
        this.nickName = board.getCreateBy();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createDate = board.getCreatedDate();
        this.postLike = Long.valueOf(board.getCountOfLikes());
        this.commentsSize = Long.valueOf(board.getCountOfComments());
        this.ipAddress = board.getIpAddress();
        this.isDeleted = board.getIsDeleted();
        this.categoryName = (board.getCategory().getCode().equals("FREE")) ? "잡담" :
            (board.getCategory().getCode().equals("QNA")) ? "질문" :
                (board.getCategory().getCode().equals("FUN")) ? "유머" :
                    (board.getCategory().getCode().equals("NOTICE")) ? "공지사항" : null;
    }


    public static BoardDetailedDto from(Board board) {
        BoardDetailedDto boardDetailedDto = new BoardDetailedDto();
        boardDetailedDto.setBoardDetailed(board);
        return boardDetailedDto;
    }



}
