package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.Member;
import web.mvc.service.MemberService;

@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "MemberController API", description = "Security Swagger 테스트용 API")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/test")
    public String test(){
        log.info("test 요청됨...");
        return "Spring security Setting 완료!";
    }

    /**
     * 아이디 중복체크
     * */
    @GetMapping("/members/{id}")
    public String duplicateIdCheck(@PathVariable String id){
        System.out.println("id = " + id);
        return memberService.duplicateCheck(id);
    }

    /**
     *  회원가입
     * */
    @PostMapping("/members") // 멤버정보가 다들어와....
    public String signUp(@RequestBody Member member){

        memberService.signUp(member); //로그인= 글쓰는사람 다르면 예외처리
        return "ok";
    }
}
