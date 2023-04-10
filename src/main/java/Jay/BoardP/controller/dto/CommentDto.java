package Jay.BoardP.controller.dto;


import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {


    @NotBlank(message = "내용을 입력해주세요.")
    private String content;




}
