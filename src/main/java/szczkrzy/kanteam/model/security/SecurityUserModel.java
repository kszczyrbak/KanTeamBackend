package szczkrzy.kanteam.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import szczkrzy.kanteam.model.entities.KTUser;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecurityUserModel implements UserDetails {

    private String login;
    private String password;

    public SecurityUserModel(KTUser KTUser) {
        this.login = KTUser.getEmail();
        this.password = KTUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ADMIN");
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
