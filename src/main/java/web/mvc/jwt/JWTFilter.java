package web.mvc.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import web.mvc.domain.Member;
import web.mvc.security.CustomMemberDetails;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            //System.out.println("token null");
            log.info("token null");
            filterChain.doFilter(request, response); //다음 필터를 호출...

            //조건이 해당되면 메소드 종료 (필수)
            return;//더이상 아래 문장을 실행하지 않는다.
        }

        log.info("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            log.info("token expired");

            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String memberNo = jwtUtil.getMemberNo(token);
        String username = jwtUtil.getUsername(token);
        String id = jwtUtil.getId(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set
        Member member = new Member();
        member.setMemberNo( Long.parseLong(memberNo));
        member.setId(id);
        member.setName(username);
        member.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        //세션에 사용자 등록 - 세션이 만들어짐.
        SecurityContextHolder.getContext().setAuthentication(authToken); //저장
        filterChain.doFilter(request, response);//다음 필터를 호출
    }
}