/*
 * INE - Instituto Nacional de Estadistica 2021
 */
package bo.gob.ine.web;

import bo.gob.ine.dao.Dao;
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
 * AmanzanadoController
 *
 * @since 30-01-2018
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@RequestMapping(value = "/datos")
public class DatosController {

    private static final Logger logger = LoggerFactory.getLogger(DatosController.class);
    @Autowired
    private Dao dao;

    @RequestMapping(value = "/selected", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> selected(@RequestParam Integer gestion, @RequestParam Integer mes, @RequestParam String evento) {
        logger.info("GET selected data by params");
        Map<String, Object> data = new HashMap<>();
        try {
            String sql = "select dpto, prov, mun, comu, prod, st_astext(geom) as geom \n"                    
                    + "from agro.t_ipp_eventos \n"
                    + "where gestion = :gestion \n"
                    + "and mes = :mes \n" 
                    + "and \"" + evento + "\" is not null";
                    
            Map<String, Object> params = new HashMap<>();
            params.put("gestion", gestion);
            params.put("mes", mes);
            //params.put("id_dato", id_dato);
            List<Map<String, Object>> res = dao.find(sql, params);

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }
    
    @RequestMapping(value = "/videci", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> videci(@RequestParam Integer gestion, @RequestParam Integer mes, @RequestParam String evento) {
        logger.info("GET selected data by params");
        Map<String, Object> data = new HashMap<>();
        try {
            String sql = "select evento, departamento, municipio, st_astext(geom) as geom \n"                    
                    + "from agro.tmp_videci_eventos \n"
                    + "where anio = :anio \n"
                    + "and mes = :mes \n" 
                    + "and evento = :evento";
   
            Map<String, Object> params = new HashMap<>();
            params.put("anio", gestion);
            params.put("mes", mes);
            params.put("evento", evento);
            List<Map<String, Object>> res = dao.find(sql, params);

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }
    
    @RequestMapping(value = "/riego", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> riego() {
        logger.info("GET selected data by params");
        Map<String, Object> data = new HashMap<>();
        try {
            String sql = "select proyecto, programa, nom_prog, depto, municipio, inver, lon, lat, st_astext(geom) as geom \n"                    
                    + "from agro.t_riego ";
                    
            // execute
            List<Map<String, Object>> res = dao.selectSQLMapResult(sql);

            data.put("data", res);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }
}
