package Jay.BoardP.controller.form;

import Jay.BoardP.controller.dto.BoardDetailedDto;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEditForm {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 20 , message = "20자이상 입력해주세요")
    private String content;

    @NotBlank(message = "카테고리가 비어있습니다")
    private String categoryCode;

    private List<MultipartFile> files = new ArrayList<>();

    private void set(BoardDetailedDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.categoryCode = dto.getCategoryName();
    }



    public static BoardEditForm createBoardEditForm(BoardDetailedDto dto) {
        BoardEditForm form = new BoardEditForm();
        form.set(dto);
        return form;
    }



}
