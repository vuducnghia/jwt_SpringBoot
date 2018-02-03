package com.example.demo.configs;

import com.example.demo.filters.JWTAuthenticationFilter;
import com.example.demo.filters.JWTLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){ // ma hoa mat khau
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable().authorizeRequests()
//                .antMatchers("/**").permitAll()// Có nghĩa là request "/" ko cần phải đc xác thực
//                .antMatchers("/member/").hasRole("MEMBER")// Có nghĩa là request "/" ko cần phải đc xác thực
                .antMatchers("/api/admin/").hasRole("ADMIN")
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll() //Request dạng POST tới "/login" luôn được phép truy cập dù là đã authenticated hay chưa
                .anyRequest().authenticated() // Các request còn lại đều cần được authenticated
                .and()

                // Add các filter vào ứng dụng của chúng ta, thứ mà sẽ hứng các request để xử lý trước khi tới các xử lý trong controllers.
                // Về thứ tự của các filter, các bạn tham khảo thêm tại http://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html mục 7.3 Filter Ordering
                .addFilterBefore(new JWTLoginFilter("/api/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class) //sinh JWT và trả về cho phía User
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  //Nhận JWT gửi lên phục vụ cho việc xác thực
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery("select username, password from \"user\" where username=?")
//                .authoritiesByUsernameQuery("select username , role from \"user\" where username =?")
//                .passwordEncoder(new BCryptPasswordEncoder(64));
        /*
       // Mình comment phần dưới này vì chúng ta ko sử dụng DB nhé. Nếu các bạn sử dụng, bỏ comment và config query sao cho phù hợp. Các bạn có thể GG để tìm hiểu thêm
       auth.jdbcAuthentication().dataSource(dataSource)
               .usersByUsernameQuery("select username,password, enabled from users where username=?")
               .authoritiesByUsernameQuery("select username, role from user_roles where username=?");
       */
    }
}
