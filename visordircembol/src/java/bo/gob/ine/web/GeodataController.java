/*
 * INE - Instituto Nacional de Estadistica 2021
 */
package bo.gob.ine.web;

import bo.gob.ine.dao.Dao;
import com.icg.entityclassutils.EntityResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * GeodataController
 *
 * @since 30-01-2018
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@RequestMapping(value = "/geodata")
public class GeodataController {

    private static final Logger logger = LoggerFactory.getLogger(GeodataController.class);
    @Autowired
    private Dao dao;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> geodata(@RequestParam String view, @RequestParam String field) {
        Map<String, Object> data = new HashMap<>();
        try {
            String SQL = "select "+ field +" as value, st_astext(ST_Simplify(geom, 0.01)) as geom from " + view;
            List<Map<String, Object>> result = dao.selectSQLMapResult(SQL);

            data.put("data", result);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

}
