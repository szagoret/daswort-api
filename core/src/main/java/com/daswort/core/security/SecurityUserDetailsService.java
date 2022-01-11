package com.daswort.core.security;

import com.daswort.core.user.domain.User;
import com.daswort.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optUser = userRepository.findByUsername(username);
        return optUser.map(u ->
                new SecurityUserDetails(
                        u.getUsername(),
                        u.getPasswordHash(),
                        false,
                        List.of()
                )
        ).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private static class SecurityUserDetails implements UserDetails {
        private final String username;
        private final String password;
        private final boolean disabled;
        private final List<GrantedAuthority> authorities;

        public SecurityUserDetails(String username,
                                   String password,
                                   boolean disabled,
                                   List<GrantedAuthority> authorities) {
            this.username = username;
            this.password = password;
            this.disabled = disabled;
            this.authorities = authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of();
        }

        @Override
        public String getPassword() {
            return this.password;
        }

        @Override
        public String getUsername() {
            return this.username;
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
            return !disabled;
        }
    }
}
