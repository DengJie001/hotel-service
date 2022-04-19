package com.easonhotel.admin.security;

import com.easonhotel.common.utils.PasswordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String inputName = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(inputName);
        if (!userDetails.getPassword().equals(PasswordUtil.encrypt(inputPassword))) {
            throw new BadCredentialsException("密码错误");
        }
        if (!userDetails.isAccountNonLocked()) {
            throw new DisabledException("账号被锁");
        }
        return new UsernamePasswordAuthenticationToken(inputName, inputPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
