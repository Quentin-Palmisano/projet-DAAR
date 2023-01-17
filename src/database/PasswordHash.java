package database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {
	
	private static byte[] salt = new byte[] {12,54,86,25};
	
	public static byte[] hash(String password) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(salt);
		return md.digest(password.getBytes(StandardCharsets.UTF_8));

	}
	
}
