package com.easonhotel.admin.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.dao.sys.entity.Resource;
import com.easonhotel.dao.sys.entity.User;
import com.easonhotel.dao.sys.service.IResourceService;
import com.easonhotel.dao.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserService userService;

    @Autowired
    private IResourceService resourceService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User sysUser = userService.getOne(Wrappers.<User>query()
                .eq("deleted", 0)
                .eq("loginName", username)
                .last("limit 0, 1"));
        if (sysUser == null) {
            throw new UsernameNotFoundException("该账号不存在");
        } else {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List<Resource> resources = resourceService.getAuthedResource(sysUser.getId());
            for (Resource resource : resources) {
                if (StringUtils.isNotBlank(resource.getPath())) {
                    authorities.add(new SimpleGrantedAuthority(resource.getPath()));
                }
            }
            org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                    sysUser.getLoginName(), sysUser.getPassword(), true, true, true, true, authorities
            );
            return user;
        }
    }
}
