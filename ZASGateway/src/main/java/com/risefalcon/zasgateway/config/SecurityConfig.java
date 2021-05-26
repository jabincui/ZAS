package com.risefalcon.zasgateway.config;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.*;
import com.risefalcon.zasgateway.security.JWTAuthenticationEntryPoint;
import com.risefalcon.zasgateway.security.JWTAuthenticationFilter;
import com.risefalcon.zasgateway.security.JWTTokenAuthorFilter;
import com.risefalcon.zasgateway.service.RedisService;
import com.risefalcon.zasgateway.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private RedisService redisService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        // 仅执行一次
//        User userSysAdmin = new User("cnc2020", "cnc2020");
//        redisService.put(Constant.USER, userSysAdmin.getUsername(),
//                JSONObject.toJSONString(userSysAdmin));
//        Microservice zas = new Microservice("ZASGateway");
//        redisService.put(Constant.MICROSERVICE, zas.getId(),
//                JSONObject.toJSONString(zas));
//        Role roleSysAdmin = new Role(zas.getId(), Constant.SYSADMIN);
//        redisService.put(Constant.ROLE, roleSysAdmin.getId(),
//                JSONObject.toJSONString(roleSysAdmin));
//        UserRole userRoleSysAdmin = new UserRole(userSysAdmin.getUsername(), roleSysAdmin.getId());
//        redisService.put(Constant.USER_ROLE, userRoleSysAdmin.getRoleId(),
//                JSONObject.toJSONString(userRoleSysAdmin));


        // 跨域共享
        http.cors()
                .and()
                // 跨域伪造请求限制无效
                .csrf().disable()
                .authorizeRequests()
                // 控制台访问控制
                .antMatchers(
                        "/authority/**",
                        "/microservice/**",
                        "/role/**",
                        "/signup/**",
                        "/url/**",
                        "/user/**",
                        "/user_role/**"
                ).hasRole(Constant.SYSADMIN)
                // 放行GET和静态资源
                .antMatchers(
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**"
                ).permitAll();

        // 从AUTHORITY表中取出所有路径-角色关系
        for (Authority authority: redisService.getValues(Constant.AUTHORITY, Authority.class)) {
            if (!redisService.exist(Constant.MICROSERVICE, authority.getMsId())) {
                continue;
            }
            String msName = redisService.get(Constant.MICROSERVICE, authority.getMsId(), Microservice.class).getName();
            String path = redisService.get(Constant.URL, authority.getUrlId(), URL.class).getPath();
            String role = authority.getRoleId();
            // 自动补全路径前缀
            String antPattern = "/" + msName + (path.startsWith("/") ? "" : "/") + path;
            if (authority.getId().startsWith(Constant.PA)) {
                http.authorizeRequests().antMatchers(antPattern).permitAll();
            } else {
                http.authorizeRequests().antMatchers(antPattern).hasRole(role);
            }
        }

        http
                // 添加JWT登录拦截器
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                // 添加JWT鉴权拦截器
                .addFilter(new JWTTokenAuthorFilter(authenticationManager()))
                .sessionManagement()
                // 设置Session的创建策略为：Spring Security永不创建HttpSession 不使用HttpSession来获取SecurityContext
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 异常处理
                .exceptionHandling()
                // 匿名用户访问无权限资源时的异常
                .authenticationEntryPoint(new JWTAuthenticationEntryPoint());

        //---------------------------------------------------------------------


//        // 跨域共享
//        http.cors()
//                .and()
//                // 跨域伪造请求限制无效
//                .csrf().disable()
//                .authorizeRequests()
//                // 访问/test/**需要USER角色
//                // TODO: 自定义拦截和权限规则
//                .antMatchers("/EurekaClient/hello").hasRole("USER")
//                // 放行GET和静态资源
//                .antMatchers(
//                        "/*.html",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/webSocket/**"
//                ).permitAll()
//                .anyRequest().permitAll()
//                .and()
//                // 添加JWT登录拦截器
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
//                // 添加JWT鉴权拦截器
//                .addFilter(new JWTTokenAuthorFilter(authenticationManager()))
//                .sessionManagement()
//                // 设置Session的创建策略为：Spring Security永不创建HttpSession 不使用HttpSession来获取SecurityContext
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                // 异常处理
//                .exceptionHandling()
//                // 匿名用户访问无权限资源时的异常
//                .authenticationEntryPoint(new JWTAuthenticationEntryPoint());

    }

    /**
     * 跨域配置
     * @return 基于URL的跨域配置信息
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 注册跨域配置
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 配置地址栏不能识别 // 的情况
     * @return
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //此处可添加别的规则,目前只设置 允许双 //
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

}
