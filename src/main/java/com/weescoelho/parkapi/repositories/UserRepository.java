package com.weescoelho.parkapi.repositories;

import com.weescoelho.parkapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}