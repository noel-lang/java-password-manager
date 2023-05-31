package de.noellang.javapasswordmanager.feature.passwords.repository;

import de.noellang.javapasswordmanager.domain.Password;
import de.noellang.javapasswordmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {

	List<Password> findAllByUser(User user);

}
