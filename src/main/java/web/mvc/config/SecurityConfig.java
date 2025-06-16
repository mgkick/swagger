package web.mvc.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import web.mvc.jwt.JWTFilter;
import web.mvc.jwt.JWTUtil;
import web.mvc.jwt.LoginFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;// 주입
    private final JWTUtil jwtUtil; //주입(JWT 토큰을 생성하는 객체)

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        log.info("authenticationManager ---= {}", configuration);
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        log.info("bCryptPasswordEncoder call.....");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain filterChain(HttpSecurity http) call.....");
       /////////////////////////////////
        //csrf disable
        http.csrf((auth) -> auth.disable()); //csrf공격을 방어하기 위한 토큰 주고 받는 부분을 비활성화!
        //Form 로그인 방식 disable -> React, JWT 인증 방식으로 변겨예정
        //disable 를 설정하면 시큐리티의 UsernamePasswordAuthenticationFilter비활성됨.
        http.formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) ->
                auth
                        .requestMatchers("/test", "/members", "/members/**").permitAll()

                        // 스웨거 추가부분 -----------------------------------------------------------
                        .requestMatchers(
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // [1] GET 요청: 누구나 접근 가능
                        .requestMatchers(HttpMethod.GET, "/boards").permitAll()

                        // [2] POST 요청: 인증 필요
                        .requestMatchers(HttpMethod.POST, "/boards").authenticated()
                        // [3] PUT 요청: 인증 필요
                       // .requestMatchers(HttpMethod.PUT, "/boards/**").authenticated()
                        // [4] DELETE 요청: 인증 필요
                       // .requestMatchers(HttpMethod.DELETE, "/boards/**").authenticated()

                        /*https://docs.spring.io/spring-security/site/docs/5.5.6/api/org/springframework/security/config/annotation/web/configurers/AuthorizeHttpRequestsConfigurer.AuthorizedUrl.html#hasAnyRole(java.lang.String...)
                         * 사용자가 적어도 하나 이상 가져야 하는 역할(예: ADMIN, USER 등). 각 역할은 ROLE_로 시작하면 안 됩니다. 이미 자동으로 ROLE_이 붙기 때문입니
                         * */
                        .requestMatchers("/admin").hasRole("ADMIN") // 자동으로 ROLE_ 붙는다.
                        .anyRequest().authenticated());
        //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를넣어야 함)
//addFilterAt은 UsernamePasswordAuthenticationFilter의 자리에 LoginFilter가 실행되도록 설정하는 것
        http.addFilterAt(
                new LoginFilter(
                        this.authenticationManager(authenticationConfiguration), //AuthenticationManager
                        jwtUtil), //JWTUtil
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        return http.build();
    }
}