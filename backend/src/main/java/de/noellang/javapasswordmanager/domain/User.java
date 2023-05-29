package de.noellang.javapasswordmanager.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(updatable = false, nullable = false, unique = true)
	private UUID publicId = UUID.randomUUID();

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String hashedPassword;

	@Column(nullable = false)
	private String salt;

	@OneToMany(mappedBy = "user")
	private List<Password> passwords = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long internalId) {
		this.id = internalId;
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

	public List<Password> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<Password> passwords) {
		this.passwords = passwords;
	}
	
}
