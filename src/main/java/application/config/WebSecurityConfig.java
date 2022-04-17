package application.config;

/**
 * Класс, который при старте приложения конфигурирует web-security.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

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
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("select username, password, active from usr where username=?")
                .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?");
    }
}
