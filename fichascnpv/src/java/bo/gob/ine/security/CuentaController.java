/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import bo.gob.ine.services.IEntityServices;
import com.icg.entityclassutils.EntityResult;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * CuentaController
 *
 * @author Edy Huiza Yampasi
 */
@Controller
@RequestMapping(value = "/cuenta")
public class CuentaController {

    @Autowired
    IEntityServices service;
    final static Logger logger = Logger.getLogger(CuentaController.class);

    @RequestMapping()
    public String userHeader(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Map<String, Object> params = new HashMap<>();
            params.put("user", user.getId());
            EntityResult userER = service.selectQueryForName("usergetdatabyid", params);
            model.addAttribute("usuario", userER.getObjectData());
            return "/utils/userHeader";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    @RequestMapping(value = "/perfil", method = RequestMethod.GET)
    public String userProfile(Model model, boolean avt, boolean pwe, boolean pws) {
        try {
            if (avt) {
                model.addAttribute("avt", true);
            }
            if (pwe) {
                model.addAttribute("pwe", true);
            }
            if (pws) {
                model.addAttribute("pws", true);
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            Map<String, Object> params = new HashMap<>();
            params.put("user", user.getId());
            EntityResult userER = service.selectQueryForName("usergetdatabyid", params);
            model.addAttribute("usuario", userER.getObjectData());
            return "/usuario/userProfile";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    @RequestMapping(value = "/contraseña/update", method = POST)
    public String ActualizarContraseña(Model model, HttpServletRequest request, String current_password, String user_password) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            EntityResult userER = service.get("security", "user", user.getId());
            Map<String, Object> insertable = userER.getObjectData();
            if (insertable.get("user_password").toString().equals(hashMD5Password(current_password))) {
                insertable.put("user_password", hashMD5Password(user_password));
                service.persist(insertable, "security", "user");
                logger.info("Se ha cambiado la contraseña del usuario: " + user.getUsername());
                return "redirect:" + "/cuenta/perfil" + "?pws=true";
            } else {
                return "redirect:" + "/cuenta/perfil" + "?pwe=true";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
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
