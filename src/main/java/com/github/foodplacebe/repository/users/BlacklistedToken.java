package com.github.foodplacebe.repository.users;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "blacklisted_token")
public class BlacklistedToken {
    @Id
    private String token;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
}
