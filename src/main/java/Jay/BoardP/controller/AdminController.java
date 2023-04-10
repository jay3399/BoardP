package Jay.BoardP.controller;


import Jay.BoardP.controller.dto.AdBoardListDto;
import Jay.BoardP.controller.dto.MemberListDto;
import Jay.BoardP.controller.dto.PenaltyListDto;
import Jay.BoardP.controller.dto.SortValue;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.MemberRepository;
import Jay.BoardP.repository.SpringDataBoardRepository;
import Jay.BoardP.service.BoardService;
import Jay.BoardP.service.PenaltyService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {


    private final BoardService boardService;


    private final SpringDataBoardRepository repository;

    private final PenaltyService penaltyService;

    private final MemberRepository memberRepository;


    @GetMapping("/members")
    public String getMember(Model model) {

        List<Member> members = memberRepository.findAll();

        List<MemberListDto> memberList = members.stream().map(
            member -> MemberListDto.from(member)
        ).collect(Collectors.toList());

        model.addAttribute("members", members);

        return "/admin/adMemberList";

    }

    @GetMapping("/boards")
    public String getBoard(Model model ,@RequestParam(defaultValue = "0") Integer page) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<AdBoardListDto> adminBoardList = boardService.getAdminBoardSort(pageable);


        model.addAttribute("pageSortValue", "id");
        model.addAttribute("pageList", adminBoardList);
        return "/admin/adBoardList";
    }

    @GetMapping("/boards/{sortValue}/sort")
    public String getBoardBySort(@PathVariable(required = false)  String sortValue, Model model, @RequestParam(defaultValue = "0") Integer page , Pageable pageable) {

        switch (sortValue) {
            case "countOfComments":
                pageable = PageRequest.of(page, 10, Sort.by("countOfComments").descending());
                break;
            case "countOfLikes":
                pageable = PageRequest.of(page, 10, Sort.by("countOfLikes").descending());
                break;
            case "countOfPenalties":
                pageable = PageRequest.of(page, 10, Sort.by("countOfPenalties").descending());
                break;
            case "countVisit":
                pageable = PageRequest.of(page, 10, Sort.by("countVisit").descending());
                break;
            case "id":
                pageable = PageRequest.of(page, 10, Sort.by("id").descending());
                break;
        }

        Page<AdBoardListDto> adminBoardSort = boardService.getAdminBoardSort(pageable);

        model.addAttribute("pageSortValue", sortValue);
        model.addAttribute("pageList", adminBoardSort);

        return "/admin/adBoardList";
    }

    @GetMapping("/deleteBoard")
    public String deleteBoard(@RequestParam List<Long> boardIdList) {

        for (Long aLong : boardIdList) {
            repository.deleteById(aLong);
        }

        return "redirect:/admin/boards";
    }

    @GetMapping("/deleteAll")
    @Transactional
    public String deleteBoardByCondition() {

        repository.deleteAllByIsDeleted(true);

        return "redirect:/admin/boards";

    }


    @GetMapping("/penalty/{boardId}")
    public String getPenaltyList(@PathVariable Long boardId, Model model) {
        List<PenaltyListDto> penalties = penaltyService.getPenalties(boardId);
        model.addAttribute("penalty", penalties);

        return "/admin/penaltyList";
    }



    @ModelAttribute("sortValues")
    private List<SortValue> sortValues() {
        List<SortValue> sortValues = new ArrayList<>();
        sortValues.add(new SortValue("countVisit", "조회수"));
        sortValues.add(new SortValue("countOfPenalties", "신고수"));
        sortValues.add(new SortValue("countOfLikes", "좋아요수"));
        sortValues.add(new SortValue("countOfComments", "댓글수"));

        return sortValues;
    }

}
