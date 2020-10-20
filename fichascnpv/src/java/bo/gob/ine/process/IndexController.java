/*
 * ICG SRL - International Consulting Group 2017
 */
package bo.gob.ine.process;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * IndexController
 *
 * @since 18-11-2017
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        try {
            String url = request.getRequestURL().toString();
            model.addAttribute("url", url);
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(Model model) {
        try {
            return "errorPage";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }
}
