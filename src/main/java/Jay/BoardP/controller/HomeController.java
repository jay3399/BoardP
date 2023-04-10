package Jay.BoardP.controller;


import static org.springframework.util.StringUtils.hasText;

import Jay.BoardP.controller.form.ResetPasswordForm;
import Jay.BoardP.domain.Member;
import Jay.BoardP.repository.SpringDataTotalVisitRepository;
import Jay.BoardP.service.EmailService;
import Jay.BoardP.service.memberService;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RedisTemplate redisTemplate;

    private final SpringDataTotalVisitRepository repository;

    private final memberService memberService;

    private final EmailService emailService;


    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request,
        Model model) {

        String ipAddress = getIpAddress(request);

        // 첫요청일시
        if (isFirstRequest(ipAddress)) {
            writeRequest(ipAddress);   // 중복방지
            makeUpdateCount(); //일일 전체 보드 카운트수
        }


        model.addAttribute("visitPerDay", redisTemplate.opsForValue().get("VisitCountPerDay"));
//        model.addAttribute("totalVisit", repository.findById(1L).get().getCount());

        return "homeV2";
    }


    @GetMapping("/find/userId")
    public String findUserIdForm() {
        return "findUserIdForm";
    }

    @PostMapping("/find/userId")
    public String findUserId(@RequestParam String email, RedirectAttributes redirectAttributes) {

        Member member = memberService.findByEmail(email);

        if (member == null) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/find/userId";
        }
        redirectAttributes.addAttribute("success", true);
        emailService.mailCheckForUserId(email, member.getUserId());

        return "redirect:/find/userId";
    }


    @GetMapping("/find/password")
    public String findPasswordForm() {
        return "findPasswordForm";
    }

    @PostMapping("/find/password")
    public String findPassword(String email, String userId, RedirectAttributes redirectAttributes,
        Model model) {

        if (!hasText(email) || !hasText(userId)) {
            redirectAttributes.addAttribute("status", true);
            return "redirect:/findPassword";
        }

        Member member = memberService.isExistEmailAndUserId(email, userId);


        if (member == null) {
            redirectAttributes.addAttribute("incorrect", true);
            return "redirect:/findPassword";
        }


        emailService.mailCheck(email);
        model.addAttribute("id", member.getId());
        model.addAttribute("resetForm", new ResetPasswordForm());

        return "resetPasswordForm";
    }

    @PostMapping("/find/{memberId}/resetPassword")
    public String resetPassword(@Validated @ModelAttribute(name = "resetForm") ResetPasswordForm form,
        BindingResult bindingResult, @PathVariable Long memberId) {

        String email = memberService.findOne(memberId).getEmail();

        String code = String.valueOf(redisTemplate.opsForValue().get(email));

        if (!code.equals(form.getCode())) {
            bindingResult.reject("incorrectCode" , "이메일 인증번호가 일치하지않습니다");
        }

        if (!form.getEditPassword().equals(form.getCheckEditPassword())) {
            bindingResult.reject("incorrectPassword", "패스워드가 일치하지않습니다");
        }

        if (bindingResult.hasErrors()) {
            return "resetPasswordForm";
        }


        memberService.editPassword(memberId, form.getEditPassword());

        redisTemplate.delete(email);

        return "redirect:/";
    }






    private void makeUpdateCount() {

        if (!redisTemplate.hasKey("VisitCountPerDay")) {
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set("VisitCountPerDay", 0L);
            valueOperations.increment("VisitCountPerDay");
        } else {
            redisTemplate.opsForValue().increment("VisitCountPerDay");
        }
    }
    public boolean isFirstRequest(String clientAddress) {
        String key = getKey(clientAddress);
        return !redisTemplate.hasKey(key);
    }
    public void writeRequest(String clientAddress) {
        String key = getKey(clientAddress);

        redisTemplate.opsForValue().set(key, clientAddress); // 데이터집계 순간에 모두 딜리트 .
        redisTemplate.expire(key, 1L, TimeUnit.DAYS);
    }

    private static String getKey(String clientAddress) {

        return "firstVisitReq::" + clientAddress;
    }

    private static String getIpAddress(HttpServletRequest req) {


        String ip = req.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = req.getRemoteAddr();
        }

        return ip;
    }
}


