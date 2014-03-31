package ar.com.glasit.rom.Model;

import java.util.UUID;

public class User {

	private UUID userId;
	private String user;
	private String password;

	public User(String user, String password)
	{
		this.user = user;
		this.password = password;
	}

	public UUID getUserId() { return userId; }
	public void setUserId(UUID userId) { this.userId = userId; }

	public String getUser() { return user; }
	public void setUser(String user) { this.user = user; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
}
