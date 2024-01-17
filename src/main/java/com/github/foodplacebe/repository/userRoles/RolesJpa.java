package com.github.foodplacebe.repository.userRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesJpa extends JpaRepository<Roles, Integer> {
    Roles findByName(String name);
}
