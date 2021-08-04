/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import bo.gob.ine.services.IEntityServices;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

/**
 * CustomUserDetailsService
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
@Service
public class CustomUserDetailsService {

    @Autowired
    IEntityServices service;

    public CustomUserDetails loadUserByUsername(String username, String literalkey) throws UsernameNotFoundException {
        Usuario user = null;
        if (username.equals("admin")) {
            user = this.getDefaultUser();
        } else {
            //get database user
            try {
                user = this.getLocalUser(username);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (user == null) {
            System.out.println("webapp __ user not found");
            Map<String, Object> in = new HashMap<>();
            in.put("dateaccess", new Date());
            in.put("success", Boolean.FALSE);
            in.put("attrs", "{username:" + username + "}");
            throw new UsernameNotFoundException("user not found");
        } else {
            //set roles
            if (user.getRol() != null) {
                user.setRoles(this.getRoles(user.getRol()));
            }
            //set access switches
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;

            CustomUserDetails userDetail = new CustomUserDetails(
                    username,
                    user.getClave(),
                    user.getActivo(),
                    accountNonExpired,
                    accountNonLocked,
                    credentialsNonExpired,
                    user.getRoles());

            userDetail.setNombre(user.getNombres());
            userDetail.setApellido(user.getApellidos());
            userDetail.setRole(user.getRol());
            userDetail.setId(user.getUsuario_id());
            userDetail.setPrincipal(user.getPrincipal());

            return userDetail;
        }
    }

    private Usuario getDefaultUser() {
        ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
        return (Usuario) ctx.getBean("defaultuser");
    }

    private Usuario getLocalUser(String username) throws ParseException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> muser = service.getExistObject("security", "usuario", "usuario", username);
        if (muser != null) {
            Usuario u = new Usuario();
            BeanUtils.populate(u, service.ignoreNullValues(muser));
            return u;
        }
        return null;
    }

    private Set<String> getRoles(String role) {
        Set<String> roles = new HashSet<>();
        String[] rls = role.split(",");
        for (String r : rls) {
            roles.add(r);
        }
        return roles;
    }
}
