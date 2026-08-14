package org.wallet.ewallet.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.wallet.ewallet.wallet.Wallet;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Wallet> wallets;
}
