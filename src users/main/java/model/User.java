package model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import config.GrantedAuthoritySerializer;
import jakarta.persistence.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @JsonSerialize(contentUsing = GrantedAuthoritySerializer.class)
    private Collection<? extends GrantedAuthority> authorities;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public User() {
    }

    public User(String name, String password, Role role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Role getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + '\'' + ", role=" + role + '}';
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    public void setAuthorities(Object o) {
        if (o instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) o;

            if (!collection.isEmpty() && collection.iterator().next() instanceof GrantedAuthority) {
                this.authorities = (Collection<? extends GrantedAuthority>) collection;
            }
            else if (!collection.isEmpty() && collection.iterator().next() instanceof String) {
                this.authorities = collection.stream()
                        .map(role -> new SimpleGrantedAuthority((String) role))
                        .collect(Collectors.toList());
            }
        } else {
            throw new IllegalArgumentException("Expected a Collection of GrantedAuthority or role strings");
        }
    }
}
