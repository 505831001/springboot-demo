package com.liuweiwei.component;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BbcUserDetails implements UserDetails {
    private Long id;
    private String nickName;

    private List<GrantedAuthority> authorities;
    private String username;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    private Boolean isAccountNonExpired = true;
    private Boolean isAccountNonLocked = true;
    private Boolean isCredentialsNonExpired = true;
    private Boolean isEnabled = true;

    /**
     * 判断账号是否已[过期]
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    /**
     * 判断账号是否已[锁定]
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    /**
     * 判断凭据是否已[过期]
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    /**
     * 判断账号是否已[启用]
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
