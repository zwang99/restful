package com.example.shop_project_detail.service;

import com.example.shop_project_detail.dao.LoginDao;
import com.example.shop_project_detail.domain.entity.User;
import com.example.shop_project_detail.security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class LoginService implements UserDetailsService {
    LoginDao loginDao;

    @Autowired
    public LoginService(LoginDao loginDao) {
        this.loginDao = loginDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = loginDao.loadUserByUsername(username);

        if (!userOptional.isPresent()){
            throw new UsernameNotFoundException("Username does not exist");
        }

        User user = userOptional.get(); // database user

        //System.out.println("sout User: " + user);

        return AuthUserDetail.builder() // spring security's userDetail
                .username(user.getUsername())
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .authorities(getAuthoritiesFromUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    private List<GrantedAuthority> getAuthoritiesFromUser(User user){
        List<GrantedAuthority> userAuthorities = new ArrayList<>();

        for (String permission :  loginDao.getPermissionsByUserId(user.getUser_id())){
            userAuthorities.add(new SimpleGrantedAuthority(permission));    // SimpleGrantedAuthority can be created from role Strings
        }

        //System.out.println(userAuthorities);
        return userAuthorities;
    }

    public List<String> getPermissionsByUserId(int user_id) {
        return loginDao.getPermissionsByUserId(user_id);
    }

    public User getUserByUserId(int user_id) {
        return loginDao.getUserByUserId(user_id);
    }

}
