package de.noellang.javapasswordmanager.feature.passwords.repository;

import de.noellang.javapasswordmanager.domain.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {

}
