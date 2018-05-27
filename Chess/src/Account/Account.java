package Account;

public class Account {
	private String username;
	private String email;

	public Account(String username, String email) {
		this.username = username;
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
}
