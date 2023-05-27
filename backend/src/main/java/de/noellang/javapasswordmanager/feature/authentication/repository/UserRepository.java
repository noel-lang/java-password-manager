package de.noellang.javapasswordmanager.feature.authentication.repository;

import de.noellang.javapasswordmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
