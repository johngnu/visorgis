/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import com.google.gson.Gson;
import bo.gob.ine.services.IEntityServices;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LoginController
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    IEntityServices service;

    @RequestMapping(value = "/loginpage", method = RequestMethod.GET)
    public String getLoginPage(Model model,
            @RequestParam(value = "error", required = false) boolean key,
            @RequestParam(required = false) Long cid,
            HttpServletRequest request) {
        System.out.println("bacheo loginpage open: " + key);

        if (key) {
            model.addAttribute("key", key);
        }

        // client data if exist
        if (cid != null) {
            model.addAttribute("client", service.get("crm", "cliente", cid).getObjectData());
        }

        return "login/login";
    }
}
