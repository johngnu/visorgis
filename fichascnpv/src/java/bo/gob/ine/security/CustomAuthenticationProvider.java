/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * CustomAuthenticationProvider
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String literalkey = authentication.getCredentials().toString();
        String password = literalkey;
        try {
            password = this.hashMD5Password(literalkey);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        System.out.println("webapp auth for : " + name);
        CustomUserDetails user = userService.loadUserByUsername(name, literalkey);

        if (user == null || !user.getUsername().equalsIgnoreCase(name)) {
            throw new BadCredentialsException("Username not found.");
        }

        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("la cuenta no esta activa.");
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

    private String hashMD5Password(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();
        //convert the byte to hex format method 1
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
