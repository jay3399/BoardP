package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.FileType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileDto {

    private Long id;
    private String originalFilename;
    private String storedFilename;
    private FileType fileType;

    @QueryProjection
    public FileDto(String originalFilename , String storedFilename , FileType fileType) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.fileType = fileType;
    }
}
