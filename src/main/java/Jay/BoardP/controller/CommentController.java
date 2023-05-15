package Jay.BoardP.controller;


import Jay.BoardP.controller.dto.CommentDto;
import Jay.BoardP.controller.dto.User;
import Jay.BoardP.controller.form.CommentForm;
import Jay.BoardP.service.CommentLikeService;
import Jay.BoardP.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {


    private final CommentLikeService commentLikeService;
    private final CommentService commentService;




//    @PostMapping("/comment/{boardId}/reply")
//    public String addComment(@PathVariable Long boardId,
//        @Validated @ModelAttribute("commentDto") CommentDto commentDto
//        , BindingResult bindingResult, @AuthenticationPrincipal User user,
//        RedirectAttributes redirectAttributes) {
//
//        Long memberId = user.getId();
//
//        if (bindingResult.hasErrors()) {
//            System.out.println("bindingResult = " + bindingResult);
//            return "board/boardDetailed";
//        }
//
//        commentService.addComment(memberId, boardId, commentDto.getContent());
//        redirectAttributes.addAttribute("boardId", boardId);
//
//        return "redirect:/boards/{boardId}";
//    }

//    @PostMapping("/comment/{boardId}/{parentId}/reply")
//    public String addReComment(@PathVariable Long boardId, @PathVariable Long parentId,
//        String content, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
//
//        Long memberId = user.getId();
//        commentService.addComment(memberId, boardId, content, parentId);
//        redirectAttributes.addAttribute("boardId", boardId);
//
//        return "redirect:/boards/{boardId}";
//    }
//


    // form + 서비스단 전송용객체 dto 추가 -> 파라미터 개수 --  , 컨트롤러 의존성 --
    @PostMapping("/comment/{boardId}/reply")
    public String addCommentV2(@PathVariable Long boardId,
        @Validated @ModelAttribute("commentForm") CommentForm commentForm
        , BindingResult bindingResult, @AuthenticationPrincipal User user,
        RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult = " + bindingResult);
            return "board/boardDetailed";
        }

        CommentDto commentDto = CommentDto.create(commentForm.getContent(), user.getId(), boardId);

        commentService.addCommentV2(commentDto);

        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";
    }

    @PostMapping("/comment/{boardId}/{parentId}/reply")
    public String addReCommentV2(@PathVariable Long boardId, @PathVariable Long parentId,
        String content, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {

        Long memberId = user.getId();

        CommentDto commentDto = CommentDto.create(content, memberId, boardId, parentId);

        commentService.addCommentV2(commentDto);

        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/{boardId}";
    }





    @PostMapping("/comment/{boardId}/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, @PathVariable Long boardId,
        RedirectAttributes redirectAttributes) {
        Boolean aBoolean = commentService.deleteComment(commentId , boardId);
        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/boards/{boardId}";
    }


    @PostMapping("/comment/{boardId}/{commentId}/like")
    public String commentLike(@PathVariable Long commentId, @PathVariable Long boardId,
        @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {

        Boolean aBoolean = commentLikeService.pushLikeButton(user.getId(), commentId , boardId);

        return "redirect:/boards/{boardId}";
    }


    @GetMapping("/comment/{nickName}")
    public String commentToBoardList(@PathVariable String nickName, Model model,
        RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("nickname", nickName);
        redirectAttributes.addAttribute("categoryCode", "ALL");

        return "redirect:/boards/{categoryCode}/category";


    }


}
