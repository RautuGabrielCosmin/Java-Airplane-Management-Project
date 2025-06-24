package gui.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class HashUtil {

    private static final SecureRandom RNG = new SecureRandom();

    private HashUtil() {}

    /** returns 32‑byte random salt, Base64‑encoded */
    public static String newSalt() {
        byte[] salt = new byte[32];
        RNG.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /** salted SHA‑256 → Base64 */
    public static String hash(String plain, String base64Salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(base64Salt));
            byte[] out = md.digest(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception ex) { throw new RuntimeException(ex); }
    }

    /** verify */
    public static boolean check(String plain, String salt, String storedHash){
        return hash(plain,salt).equals(storedHash);
    }
}
