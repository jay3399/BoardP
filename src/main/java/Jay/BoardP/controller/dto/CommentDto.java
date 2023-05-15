package Jay.BoardP.controller.dto;


import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {


    private String content;

    private Long memberId;

    private Long boardId;

    private Long parentId;

    private void setDto(String content, Long memberId, Long boardId) {
        this.content = content;
        this.memberId = memberId;
        this.boardId = boardId;
    }

    private void setDto(String content, Long memberId, Long boardId, Long parentId) {
        this.content = content;
        this.memberId = memberId;
        this.boardId = boardId;
        this.parentId = parentId;
    }


    public static CommentDto create(String content, Long memberId, Long boardId) {

        CommentDto commentDto = new CommentDto();

        commentDto.setDto(content, memberId, boardId);

        return commentDto;

    }

    public static CommentDto create(String content, Long memberId, Long boardId , Long parentId) {

        CommentDto commentDto = new CommentDto();

        commentDto.setDto(content, memberId, boardId, parentId);

        return commentDto;

    }






}
