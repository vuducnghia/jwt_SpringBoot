package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {  // Tài khoản người dùng và các vai trò của người dùng đó

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println(111);
        System.out.println(user.getUsername());
        System.out.println(user.getRole());
        //Một GrantedAuthority là một quyền được ban cho principal. Các quyền đều có tiền tố là ROLE_
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        Set<Role> roles = user.getRoles();

//        System.out.println(roles.size());
//        for (Role role : roles) {
//            System.out.println(role.getName());
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }

        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
