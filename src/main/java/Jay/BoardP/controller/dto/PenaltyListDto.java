package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Penalty;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PenaltyListDto {

    private Long id;
    private String userId;
    private String code;

    private String content;

    private LocalDateTime created;


    private void set(Penalty penalty) {
        this.id = penalty.getId();
        this.userId = penalty.getMember().getUserId();
        this.code = (penalty.getCode().equals("F")) ? "욕설" : (penalty.getCode().equals("R")) ? "규정" : null;
        this.content = penalty.getContent();
        this.created = penalty.getCreatedDate();
    }

    public static PenaltyListDto createPenaltyDto(Penalty penalty) {
        PenaltyListDto result = new PenaltyListDto();
        result.set(penalty);
        return result;
    }

}
