package web.mvc.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.mvc.domain.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Slf4j
public class CustomMemberDetails implements UserDetails {
    private final Member member;
    public CustomMemberDetails(Member member) {
        this.member = member;
        log.info("CustomMemberDetails : {}", member);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("getAuthorities.....");
        Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();;
        collection.add(()->member.getRole()); // ROLE_xx저장
        return collection;
    }

    @Override
    public String getPassword() {
        log.info("getPassword....");
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        log.info("getUsername....");
        return member.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        log.info("isAccountNonExpired....");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.info("isAccountNonLocked....");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("isCredentialsNonExpired....");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("isEnabled....");
        return true;
    }
}
