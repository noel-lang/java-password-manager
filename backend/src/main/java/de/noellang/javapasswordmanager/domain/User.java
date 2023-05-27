package de.noellang.javapasswordmanager.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long internalId;

	@Column(updatable = false, nullable = false, unique = true)
	private UUID publicId = UUID.randomUUID();

	private String username;

	private String hashedPassword;

	private String salt;

	public Long getInternalId() {
		return internalId;
	}

	public void setInternalId(Long internalId) {
		this.internalId = internalId;
	}

	public UUID getPublicId() {
		return publicId;
	}

	public void setPublicId(UUID publicId) {
		this.publicId = publicId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
