package application.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usr") // usr, а не user, потому что в postgres user это уже занятое служебное имя
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean active; // признак активности пользователя

    // у пользователей будет ролевая система (админ-пользователь-привилегированный пользователь)
    // @ElementCollection позволяет спрингу самому формировать доп.таблицу для хранения enum-a
    // fetch - параметр, который определяет, как данные будут подгружаться относительно основной сущности
    // EAGER - жадный способ - Hibernate сразу же при запросе пользователя подгружает его роли.
    // LAZY - Hibernate подгрузит роли только когда пользователь реально обратиться к этому полю
    // @CollectionTable описывает, что данное поле будет храниться в отдельной таблице для которой мы не описывали mapping

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING) // показывает, что это enum и что хранить его будем в виде строки
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
