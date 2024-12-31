package com.mo.bank.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "pet_name", length = 30)
    private String petName;

    @Column(name = "favorite_toy", length = 30)
    private String favoriteToy;

    @Column(name = "favorite_color", length = 30)
    private String favoriteColor;

    @Column(name = "favorite_movie", length = 100)
    private String favoriteMovie;

    public Token(Integer accountId, String petName, String favoriteToy, String favoriteColor, String favoriteMovie) {
        this.accountId = accountId;
        this.petName = petName;
        this.favoriteToy = favoriteToy;
        this.favoriteColor = favoriteColor;
        this.favoriteMovie = favoriteMovie;
    }
}
