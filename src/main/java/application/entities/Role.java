package application.entities;

import org.springframework.security.core.GrantedAuthority;


// хоть это и не класс, а enum, значения enum-a и являются реализацией этого класса
public enum Role implements GrantedAuthority {
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
