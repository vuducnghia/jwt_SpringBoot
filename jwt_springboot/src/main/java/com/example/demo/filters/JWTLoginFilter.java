package com.example.demo.filters;


import com.example.demo.models.AccountCredentials;
import com.example.demo.services.TokenAuthenticationService;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    //url mà ta truyền vào chính là url để thực hiện cho việc login đó. Trong WebSecurityConfig hiện tại mình có truyền là "/login"
    public JWTLoginFilter(String url, AuthenticationManager authManager) {

        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override //kiểm tra các data cần thiết mà phía người dùng gửi lên và trả về một đối tượng Authentication
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        nhận data từ phía user gửi lên, trích xuất user-password và được sử dụng để AuthenticationManager sử dụng để authenticate
//        trong file WebSecurityConfig

        String test, username = "", password = "";
        JSONObject jsonObject;

        if ("POST".equalsIgnoreCase(request.getMethod()))
        {
            test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            jsonObject = (JSONObject) JSONValue.parse(test);
            username = (String) jsonObject.get("username");
            password = (String) jsonObject.get("password");
        }

        AccountCredentials credentials = new AccountCredentials(username, password);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        TokenAuthenticationService.addAuthentication(response, authResult.getName());
    }
}
