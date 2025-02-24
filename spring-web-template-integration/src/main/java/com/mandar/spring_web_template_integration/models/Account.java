package com.mandar.spring_web_template_integration.models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Email(message = "Invalid email")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Firstname cannot be empty")
    private String firstname;

    @NotEmpty(message = "Lastname cannot be empty")
    private String lastname;

    private String role;

    // one to many relationship with table Posts -> one account can have one or
    // multiple posts
    @OneToMany(mappedBy = "account")
    private List<Post> posts;

    // relation between account and authority tables
    @ManyToMany
    @JoinTable(name = "ACCOUNT_AUTHORITY", joinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "id"))
    private Set<Authority> authorities;

}
