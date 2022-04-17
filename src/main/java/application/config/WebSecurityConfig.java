package application.config;

/**
 * Класс, который при старте приложения конфигурирует web-security.
 */

import application.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // включается авторизация
                    .antMatchers("/", "/registration").permitAll() // главной страничке предоставляется полный доступ
                    .anyRequest().authenticated() // для остальных запросов включаем авторизацию
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }

    // этот сервис создает в памяти менеджер, который обслуживает учетные записи пользователей
    /*@Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                // метод помечен, как deprecated, потому что он нужен только для отладки, он ничего не шифрует, ничего не хранит
                // при каждом запуске приложения создает пользователей
                User.withDefaultPasswordEncoder()
                        .username("dog")
                        .password("richi")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}
