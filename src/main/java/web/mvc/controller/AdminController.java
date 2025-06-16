package web.mvc.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.Member;
import web.mvc.security.CustomMemberDetails;

import java.util.Collection;
import java.util.Iterator;

@RestController
@Slf4j
@Tag(name = "AdminController API", description = "Security Swagger 테스트용 API") //------------------------------------------스웨거 추가
public class AdminController {
   /* @GetMapping("/admin")
    public String admin(){
        //시큐리티에 저장된 정보 조회
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authentication getName =  {} " , name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails customMemberDetails = (CustomMemberDetails)authentication.getPrincipal();
        Member m = customMemberDetails.getMember();
        log.info("customMemberDetails =  {} ,{} ,{} " , m.getId(), m.getName(), m.getRole());


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        while(iter.hasNext()){
            GrantedAuthority auth = iter.next();
            String role = auth.getAuthority();
            log.info("Authentication role =  {} " , role);
        }

        return "admin 입니다.";
    }*/

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal CustomMemberDetails customMemberDetails ){

        log.info("CustomMemberDetails customMemberDetails =  {} " , customMemberDetails);


        Member m = customMemberDetails.getMember();
        log.info("customMemberDetails =  {} ,{} ,{} " , m.getId(), m.getName(), m.getRole());


        Collection<? extends GrantedAuthority> authorities = customMemberDetails.getAuthorities();

        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        while(iter.hasNext()){
            GrantedAuthority auth = iter.next();
            String role = auth.getAuthority();
            log.info("Authentication role =  {} " , role);
        }

        return "admin 입니다.";
    }
}
