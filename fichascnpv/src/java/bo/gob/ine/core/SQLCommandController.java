/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icg.entityclassutils.DynamicEntityMap;
import com.icg.entityclassutils.EntityResult;
import com.icg.entityclassutils.Restriction;
import com.icg.entityclassutils.SimpleFilter;
import bo.gob.ine.dao.Dao;
import bo.gob.ine.model.HttpParam;
import bo.gob.ine.model.SQLCommand;
import bo.gob.ine.services.IEntityServices;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.heyma.db.utils.StatementUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @since 18-11-2011
 * @author Johns Castillo Valencia email: john.gnu@gmail.com
 */
@Controller
@RequestMapping(value = "/command")
public class SQLCommandController {

    private static final Logger logger = LoggerFactory.getLogger(SQLCommandController.class);
    @Autowired
    private Dao dao;
    @Autowired
    private IEntityServices service;

    @RequestMapping
    public String command() {
        return "core/SQLCommandManager";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String commandSearch(Model model, @RequestParam(required = false) String qname) {
        try {
            logger.info("Find and List SQL Commands");
            if (qname == null) {
                model.addAttribute("results", service.find("logic", "sqlstore", new String[]{"createon", "qname"}).getListData());
            } else {
                Map<String, Object> params = new HashMap<>();
                params.put("qname", "%" + qname + "%");
                SimpleFilter sf = new SimpleFilter(params);
                sf.addORRestriction(new Restriction("qname", Restriction.LIKE, "qname"));
                model.addAttribute("results", service.find("logic", "sqlstore", sf).getListData());
                model.addAttribute("qname", qname);
            }
            return "core/SQLCommandSearch";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    @RequestMapping(value = "/crearsentecia", method = RequestMethod.POST)
    public String crearSentecia(Model model, HttpServletRequest request) {
        logger.info("HTTP save SQL Statement");
        try {
            Map<String, Object> in = service.buildPersistenObjectFromRequest("logic", "sqlstore", request);
            String on = request.getParameter("locked");
            in.put("createon", new Date());
            in.put("locked", (on != null));
            EntityResult resf = service.persist(in, "logic", "sqlstore");

            return "redirect:" + "/command/edit?id=" + resf.getPrimaryKey();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";

    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String commandEdit(Model model, @RequestParam Long id) {
        try {
            logger.info("Find and List SQL Commands");
            // GET sqlCommand
            Map<String, Object> cmd = service.get("logic", "sqlstore", id).getObjectData();

            Map<String, String> tipo = new HashMap<>();
            tipo.put("character varying", "Cadena");
            tipo.put("text", "Texto");
            tipo.put("integer", "Entero");
            tipo.put("bigint", "Entero Largo (bigint)");
            tipo.put("double precision", "Real");
            tipo.put("boolean", "Boolean");
            cmd.put("tipo", tipo);

            //get param list
            if (cmd.get("jsonparams") != null) {
                Type listType = new TypeToken<ArrayList<HttpParam>>() {
                }.getType();
                List<HttpParam> lst = new Gson().fromJson((String) cmd.get("jsonparams"), listType);
                cmd.put("params", lst);
            }

            if (cmd.get("sqlcommand") == null) {
                cmd.put("sqlcommand", "-- SQL Statement");
            }

            model.addAttribute("cmd", cmd);
            return "core/SQLCommandEdit";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    @RequestMapping(value = "/savesqlcommand", method = RequestMethod.POST)
    public String saveSQLCommand(Model model, String sql, Long id) {
        logger.info("HTTP save SQL Statement");
        try {
            Map<String, Object> cmd = service.get("logic", "sqlstore", id).getObjectData();
            cmd.put("sqlcommand", sql);
            service.persist(cmd, "logic", "sqlstore");

            return "redirect:" + "/command/edit?id=" + id;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    /*OBTENER DATOS DE UNA SENTENCIA POR ID*/
    @RequestMapping(value = "/savesqlcommand/find/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findSentencia(@PathVariable("id") long id) {

        logger.info("HTTP save Param Statement");
        try {
            Map<String, Object> cmd = service.get("logic", "sqlstore", id).getObjectData();

            System.out.println(cmd);

            return cmd;
        } catch (Exception e) {
            e.printStackTrace();
            new HashMap<>();
        }
        return new HashMap<>();
    }

    @RequestMapping(value = "/saveparam", method = RequestMethod.POST)
    public String saveParam(Model model, HttpParam param, Long id) {
        logger.info("HTTP save Param Statement");
        try {
            Map<String, Object> cmd = service.get("logic", "sqlstore", id).getObjectData();
            Type listType = new TypeToken<ArrayList<HttpParam>>() {
            }.getType();
            List<HttpParam> lst = new Gson().fromJson((String) cmd.get("jsonparams"), listType);

            if (lst == null) {
                lst = new ArrayList<>();
            }

            Map<String, Object> parametro = new HashMap<>();
            //name="param",  typeClass="integer",  description=""}
            //parametro.put(null, lst)
            lst.add(param);
            cmd.put("jsonparams", new Gson().toJson(lst));
            service.persist(cmd, "logic", "sqlstore");

            return "redirect:" + "/command/edit?id=" + id;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "errorPage";
    }

    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> executeQuery(long id, HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        System.out.println(id);
        Map<String, Object> input = DynamicEntityMap.requestMapToEntityMap(request.getParameterMap());
        for (Entry<String, Object> entrySet : input.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            System.out.println(key + ":" + value);
        }
        Map<String, Object> cmd = service.get("logic", "sqlstore", id).getObjectData();
        String sqlQuery = cmd.get("qname").toString();
        System.out.println("query: " + sqlQuery);
        Map<String, Object> data = new HashMap<>();
        try {
            EntityResult res = service.selectQueryForName(sqlQuery, input);
            data.put("data", res.getListData());
            data.put("success", res.getSuccess());
        } catch (Exception e) {
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
            System.out.println("error: " + e.getMessage());
        }

        return result;
    }

    /*OLD COMMAND*/
    @RequestMapping(value = "/listcommands")
    public @ResponseBody
    Map<String, ? extends Object> listCommands() {
        logger.info("SQL list Commands");
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("data", dao.find(SQLCommand.class));
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/migrate")
    public @ResponseBody
    Map<String, ? extends Object> migrate() {
        logger.info("SQL list Commands");
        Map<String, Object> data = new HashMap<>();
        try {
            //data.put("data", dao.find(SQLCommand.class));
            List<SQLCommand> lst = dao.find(SQLCommand.class);
            for (SQLCommand cmd : lst) {
                Map<String, Object> rec = new HashMap<>();
                rec.put("createon", cmd.getCreateOn());
                rec.put("description", cmd.getDescription());
                rec.put("jsonparams", cmd.getJsonParams());
                rec.put("locked", Boolean.TRUE);
                rec.put("qname", cmd.getName());
                rec.put("sqlcommand", cmd.getSqlCommand());
                rec.put("modifiedon", new Date());

                service.persist(rec, "logic", "sqlstore");
            }

            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    /*CREAR NUEVA SENTENCIA*/
    @RequestMapping(value = "/createcommand", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ? extends Object> createCommand(SQLCommand command) {
        logger.info("SQL create Command");
        Map<String, Object> data = new HashMap<>();
        try {
            command.setCreateOn(new Date());
            dao.persist(command);
            data.put("data", command);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/deletecommand", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ? extends Object> deleteCommand(SQLCommand command) {
        logger.info("SQL delete Command");
        Map<String, Object> data = new HashMap<>();
        try {
            dao.remove(command);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/createparam", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ? extends Object> createParam(HttpParam param, Long id) {
        logger.info("HTTP create param");
        Map<String, Object> data = new HashMap<>();
        try {
            SQLCommand c = dao.get(SQLCommand.class, id);
            c.addParam(param);
            dao.update(c);
            data.put("data", c);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/removeparam", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ? extends Object> removeParam(String key, Long id) {
        logger.info("HTTP remove param");
        Map<String, Object> data = new HashMap<>();
        try {
            SQLCommand c = dao.get(SQLCommand.class, id);
            c.removeParam(key);
            dao.update(c);
            data.put("data", c);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/listparams")
    @ResponseBody
    public Map<String, ? extends Object> listParamss(Long id) {
        logger.info("SQL list params");
        Map<String, Object> data = new HashMap<>();
        try {
            SQLCommand c = dao.get(SQLCommand.class, id);
            //System.out.println(c.getJsonParams());
            data.put("data", c.getParams());
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/savesql", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ? extends Object> saveSQL(String sql, Long id) {
        logger.info("HTTP save SQL Statement");
        Map<String, Object> data = new HashMap<>();
        try {
            SQLCommand c = dao.get(SQLCommand.class, id);
            c.setSqlCommand(sql);
            dao.update(c);
            //data.put("data", c);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    /**
     *
     * @param sql
     * @return
     */
    @RequestMapping(value = "/select")
    public @ResponseBody
    Map<String, ? extends Object> selectSQL(@RequestParam String sql) {
        logger.info("SQL Command input");
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("data", dao.selectSQLMapResult(sql));
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    @RequestMapping(value = "/select/query/{name}")
    @ResponseBody
    public Map<String, ? extends Object> selectQueryForName(HttpServletResponse response, @PathVariable String name, HttpServletRequest request) {
        logger.info("SQL select query for name");
        Map<String, Object> data = new HashMap<>();
        try {
            Map<String, Object> input = DynamicEntityMap.requestMapToEntityMap(request.getParameterMap());
            EntityResult res = service.selectQueryForName(name, input);
            data.put("data", res.getListData());
            data.put("success", res.getSuccess());
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     *
     * @param response
     * @param schema
     * @param table
     * @param order
     * @return
     */
    @RequestMapping(value = "/select/list/{schema}/{table}")
    public @ResponseBody
    Map<String, ? extends Object> selectListMap(HttpServletResponse response, @PathVariable String schema, @PathVariable String table, @RequestParam(required = false) String[] order) {
        logger.info("SQL Command (*) {schema}/{table}");
        Map<String, Object> data = new HashMap<>();
        try {

            Map<String, String> tableMap = service.tableColumnsMap(schema, table);
            String SQL = StatementUtil.simpleSelectTable(service.tableMapToPostgisColumns(tableMap.entrySet()), schema, table, order);

            data.put("data", dao.selectSQLMapResult(SQL));
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     *
     * @param schema
     * @param table
     * @param parent_id
     * @param fk
     * @param order
     * @return
     */
    @RequestMapping(value = "/select/sublist/{schema}/{table}")
    @ResponseBody
    public Map<String, ? extends Object> selectSubListMap(@PathVariable String schema, @PathVariable String table, @RequestParam Long parent_id, @RequestParam String fk, @RequestParam(required = false) String[] order) {
        logger.info("SQL Command input");
        Map<String, Object> data = new HashMap<>();
        try {
            Map<String, String> tableMap = service.tableColumnsMap(schema, table);

            Map<String, Object> params = new HashMap<>();
            params.put(fk, parent_id);

            //String SQL = StatementUtil.selectTableANDparams(tableMap, schema, table, params, false);
            SimpleFilter sf = new SimpleFilter(params);
            sf.addORRestriction(new Restriction(fk, Restriction.EQUALS, fk));
            //String SQL = StatementUtil.simpleSelectTable(tableMap.keySet(), schema, table, sf);
            String SQL = StatementUtil.simpleSelectTable(service.tableMapToPostgisColumns(tableMap.entrySet()), schema, table, sf, order);

            System.out.println(SQL);

            data.put("data", dao.find(SQL, params));
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    /**
     * EN COSTRUCCION
     *
     * @param schema
     * @param table
     * @param key
     * @param operator
     * @param value
     * @return
     */
    @RequestMapping(value = "/select/paramlist/{schema}/{table}")
    public @ResponseBody
    Map<String, ? extends Object> selectSubListMap(@PathVariable String schema, @PathVariable String table, @RequestParam String key, @RequestParam String operator, @RequestParam String value) {
        logger.info("SQL Command input");
        Map<String, Object> data = new HashMap<>();
        try {

            SimpleFilter sf = new SimpleFilter();
            sf.addValue(key, value);
            sf.addANDRestriction(new Restriction(key, operator));

            data.put("data", service.find(schema, table, sf).getListData());
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    /**
     *
     * @param response
     * @param schema
     * @param table
     * @param id
     * @return
     */
    @RequestMapping(value = "/select/object/{schema}/{table}")
    public @ResponseBody
    Map<String, ? extends Object> selectMap(HttpServletResponse response, @PathVariable String schema, @PathVariable String table, @RequestParam Long id) {
        logger.info("SQL Command get object by ID");
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("data", service.get(schema, table, id).getObjectData());
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
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

    @RequestMapping(value = "/select/geojson/item/{schema}/{table}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String selectJsonItem(HttpServletResponse response, @PathVariable String schema, @PathVariable String table, @RequestParam Long id) {
        logger.info("SQL Command get object by ID");
        JSONObject json_obj = new JSONObject();
        json_obj.put("type", "FeatureCollection");
        Map<String, Object> map = service.get(schema, table, id).getObjectData();
        JSONArray json_arr = new JSONArray();
        JSONObject geom_object = new JSONObject();

        geom_object.put("geometry", new JSONObject(map.get("_geo_json").toString()));
        geom_object.put("type", "Feature");
        JSONObject properties_object = new JSONObject();
        properties_object.put("id", id);
        properties_object.put("name", map.get("nombre").toString());
        properties_object.put("color", "red");
        properties_object.put("codigo", map.get("codigo").toString());
        properties_object.put("ascii", "71");
        geom_object.put("properties", properties_object);
        json_arr.put(geom_object);
        json_obj.put("features", json_arr);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);
        return json_obj.toString();
    }

    @RequestMapping(value = "/select/geojson/{schema}/{table}/{base_dato}", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String selectJson(HttpServletResponse response, @PathVariable String schema, @PathVariable String table, @PathVariable long base_dato) {
        JSONObject json_obj = new JSONObject();
        json_obj.put("type", "FeatureCollection");
        JSONArray json_arr = new JSONArray();
        try {
            EntityResult main_result = service.selectQueryForName("getbase" + table, base_dato);
            if (main_result.getSize() > 0) {

                EntityResult result = service.selectQueryForName(table + "simplify", main_result.getObjectData().get("base_" + table + "_id"));
                System.out.println(result.getSize());
                for (Map<String, Object> map : result.getListData()) {
                    if (map.get("the_geom") != null) {
                        JSONObject geom_object = new JSONObject();
                        geom_object.put("geometry", new JSONObject(map.get("the_geom").toString()));
                        geom_object.put("type", "Feature");
                        JSONObject properties_object = new JSONObject();
                        properties_object.put("id", map.get(table + "_id"));
                        properties_object.put("name", map.get("nombre").toString());
                        properties_object.put("codigo", map.get("codigo").toString());
                        properties_object.put("color", "orange");
                        geom_object.put("properties", properties_object);
                        json_arr.put(geom_object);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);
        json_obj.put("features", json_arr);
        return json_obj.toString();
    }

    /**
     *
     * @param request
     * @param schema
     * @param entity
     * @return
     */
    @RequestMapping(value = "/persist/{schema}/{entity}", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, ? extends Object> persistMap(HttpServletRequest request, @PathVariable String schema, @PathVariable String entity) {
        logger.info("SQL insert Command input");
        Map<String, Object> data = new HashMap<>();
        try {
            Map<String, Object> insertable = service.buildPersistenObjectFromRequest(schema, entity, request);
            EntityResult er = service.persist(insertable, schema, entity);
            data.put("primaryKey", er.getPrimaryKey());
            data.put("success", er.getSuccess());
        } catch (Exception e) {
            e.printStackTrace();
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

    /**
     *
     * @param schema
     * @param table
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{schema}/{table}")
    public @ResponseBody
    Map<String, ? extends Object> deleteObject(@PathVariable String schema, @PathVariable String table, @RequestParam Long id) {
        logger.info("Delete Command");
        Map<String, Object> data = new HashMap<>();
        try {
            String SQL = StatementUtil.deleteObject(schema, table, table + "_id");
            dao.remove(SQL, id);
            data.put("success", Boolean.TRUE);
        } catch (Exception e) {
            logger.error("Error al obtener ejecutar SQL: " + e.getMessage());
            data.put("success", Boolean.FALSE);
            data.put("errorMessage", e.getMessage());
        }
        return data;
    }

}
