package com.github.foodplacebe.repository.userRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesJpa extends JpaRepository<UserRoles, Integer> {
}
