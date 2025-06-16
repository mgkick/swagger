package web.mvc;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import web.mvc.domain.Member;
import web.mvc.repository.MemberRepository;

@SpringBootTest
@Slf4j
class Security04RegisterApplicationTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 패스워드 암호화 테스트
     * */
    @Test
    @DisplayName("암호화 test")
    void passwordTest(){
        String rawPassword="8253jang";//평문

        //비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(rawPassword);//평문 -> 암호화
        log.info("encodedPassword = {}" , encodedPassword);

        // 비밀번호 매칭 확인
        boolean isPasswordMathch = passwordEncoder.matches(rawPassword, encodedPassword);
        log.info("Password mathch : {}" , isPasswordMathch);
    }

    /**
     * 관리자 등록
     * */
    @Test
    @DisplayName("관리자 계정추가")
    void memberInsert() {
        String encPwd = passwordEncoder.encode("1234");//비번 암호화

        memberRepository.save(
                Member.builder()
                        .id("admin")
                        .pwd(encPwd)
                        .role("ROLE_ADMIN")
                        .address("오리역")
                        .name("장희정")
                        .build());
    }

}
