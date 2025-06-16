package web.mvc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.domain.Member;
import web.mvc.repository.MemberRepository;

/**
 * 인증처리 서비스 -  repository에서 정보를 찾아서 인증....
 * */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username ={}" , username); //아이디

        //디비에 id에 해당하는 정보가 있는지 찾기
       Member findMember = memberRepository.findById(username);
       if(findMember!=null){
           log.info("findMember  찾았다~~ ={}" , findMember);
           return new CustomMemberDetails(findMember);// UserDetails
       }
        return null;
    }
}
