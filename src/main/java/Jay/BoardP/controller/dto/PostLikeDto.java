package Jay.BoardP.controller.dto;

import Jay.BoardP.domain.Board;
import Jay.BoardP.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostLikeDto {


    private Member member;
    private Board board;



}
