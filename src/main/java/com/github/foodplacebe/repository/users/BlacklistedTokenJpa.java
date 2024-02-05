package com.github.foodplacebe.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenJpa extends JpaRepository<BlacklistedToken, String> {
}
