package Jay.BoardP.controller.form;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentForm {

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;


}
