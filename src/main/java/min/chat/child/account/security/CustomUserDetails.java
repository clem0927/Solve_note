package min.chat.child.account.security;

import lombok.Getter;
import min.chat.child.account.entity.Account;
import min.chat.child.grade.entity.Grade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String nickname;
    private final String role;
    private final Grade grade;
    private final String password;

    public CustomUserDetails(Account account) {
        this.email = account.getEmail();
        this.nickname = account.getNickname();
        this.role = account.getRole();
        this.grade = account.getGrade(); // Grade 객체 저장
        this.password = account.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}