/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.web;

import bo.gob.ine.services.IEntityServices;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @since 18-11-2011
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@RequestMapping(value = "/accesscontrol")
public class AccessController {

    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);
    @Autowired
    private IEntityServices service;

    @RequestMapping(value = "/log")
    @ResponseBody
    public Map<String, ? extends Object> log(HttpServletRequest request, HttpServletResponse response) {
        logger.info("access control log");
        Map<String, Object> data = new HashMap<>();
        try {

            Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
            String h = s.hasNext() ? s.next() : "";
            System.out.println(h);

            //System.out.println(ClientInfo.getClientInfoMap(request));
            Map<String, Object> c = ClientInfo.getClientInfoMap(request);
            c.put("register", new Date());
            service.persist(c, "security", "accesslog");

            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error to register log web" + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);

        return data;
    }
}
