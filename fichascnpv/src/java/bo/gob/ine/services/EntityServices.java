/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.services;

import com.google.gson.Gson;
import com.icg.entityclassutils.Attribute;
import com.icg.entityclassutils.DynamicEntityMap;
import com.icg.entityclassutils.Entity;
import com.icg.entityclassutils.EntityResult;
import com.icg.entityclassutils.Restriction;
import com.icg.entityclassutils.SimpleFilter;
import bo.gob.ine.dao.Dao;
import bo.gob.ine.model.Entidad;
import bo.gob.ine.model.HttpParam;
import bo.gob.ine.model.SQLCommand;
import java.io.File;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.heyma.db.utils.StatementUtil;
import org.heyma.db.utils.Table;
import org.heyma.packageutils.HeymaPakkageUtils;
import org.heyma.packageutils.extja.treeloader.Element;
import org.heyma.packageutils.extja.treeloader.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * EntityServices
 *
 * @since 2011-02-13
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 */
@Service
public class EntityServices implements IEntityServices {

    @Autowired
    Dao dao;
    @Autowired
    private HttpServletRequest request;

    /**
     * Generates
     *
     * @return Un Array [] JSON de los paquetes y clases para el "treeloader"
     * del entity manager
     */
    public List<Folder> getEntities(String pkg) throws ClassNotFoundException, IOException {
        List<Folder> folders = HeymaPakkageUtils.getFoldersFromPackage(pkg);
        return folders;
    }

    @Override
    public List<Element> EntitiesToExtTreeElements() {
        List<Element> elements = new ArrayList();
        for (Entidad e : this.getEntities()) {
            Element el = new Element();
            el.setLeaf(true);
            el.setText(e.getLabel());
            el.setId(e.getName());
            elements.add(el);
        }
        return elements;
    }

    public List<Entidad> getEntities() {
        List<Entidad> entities = dao.find("FROM Entidad WHERE parent = NULL");
        List<Map<String, Object>> stables = schemaTables("public");
        for (Entidad en : entities) {
            en.setData(this.entidadHasDataBase(en, stables));
        }
        return entities;
    }

    private boolean entidadHasDataBase(Entidad entidad, List<Map<String, Object>> tables) {
        for (Map<String, Object> table : tables) {
            if (table.get(Table.TABLE_NAME).equals(entidad.getName())) {
                return true;
            }
        }
        return false;
    }

    public void executeSQLStatement(String sql) {
        dao.execute(sql);
    }

    /**
     *
     * @param ID
     * @return Retorna un Objeto de la Entidad
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Object getObjectEntityByID(String ID) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        /*
         * Debe devolver 1 objeto unicamente SI es Entidad Virtual
         */
        List lentity = dao.find("FROM Entidad WHERE name='" + ID + "'");
        /*
         * Si la Lista esta vacia es una Entidad Fisica (Clase con anotaciones
         * JPA)
         */
        if (lentity.isEmpty()) {
            Object entity = dao.get(Class.forName(HeymaPakkageUtils.getElementPackagePathFromID(ID) + "." + HeymaPakkageUtils.getSimpleElementNameFromID(ID)), 1L);
            if (entity == null) {
                entity = Class.forName(HeymaPakkageUtils.getElementPackagePathFromID(ID) + "." + HeymaPakkageUtils.getSimpleElementNameFromID(ID)).newInstance();
                PropertyUtils.setProperty(entity, "name", HeymaPakkageUtils.getSimpleElementNameFromID(ID));
            }
            return (Object) entity;
        } else {
            Entidad en = (Entidad) lentity.get(0);
            return (Object) en;
        }
    }

    public List<Attribute> getAttributesFromEntity(Object entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String struct = PropertyUtils.getProperty(entity, "struct").toString();
        JSONArray jsonArray = JSONArray.fromObject(struct);
        return (List<Attribute>) JSONArray.toCollection(jsonArray, Attribute.class);
    }

    public <T> List<T> getListFromStructProperty(Object entity, Class<T> type, String lstProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String struct = PropertyUtils.getProperty(entity, lstProperty).toString();
        JSONArray jsonArray = JSONArray.fromObject(struct);
        return (List<T>) JSONArray.toCollection(jsonArray, type);
    }

    public void setListToStructProperty(Object entity, List<? extends Object> objects, String lstProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Gson gson = new Gson();
        PropertyUtils.setSimpleProperty(entity, lstProperty, gson.toJson(objects));
    }

    public <T> T jsonToObject(String JSON, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(JSON, type);
    }

    public void updateEntityStruct(Object entity) {
        dao.persist(entity);
    }

    public boolean existTable(String schema, String tablename) {
        return dao.existTable(schema, tablename);
    }

    public boolean existColumn(String schema, String tablename, String columnname) {
        return dao.existColumn(schema, tablename, columnname);
    }

    public void alterDropColumn(Attribute attr) {
        dao.execute(StatementUtil.alterDropColumn(attr));
    }

    public List<Entidad> getAllEntities() throws ClassNotFoundException, IOException {
        List<Entidad> le = dao.find(Entidad.class);
        List<Class> fe = HeymaPakkageUtils.getClasses("com.icg.sitmax.model.logic");

        for (Class cl : fe) {
            Entidad e = new Entidad();
            e.setName(HeymaPakkageUtils.getElementIDFromPackagePath(cl.getName()));
            e.setLabel(cl.getSimpleName());
            le.add(e);
        }
        return le;
    }

    public void createEntity(Entidad entidad) {
        dao.persist(entidad);
        dao.execute(StatementUtil.createEntityDataBase("public", entidad.getName(), entidad.getName() + "_id"));
    }

    public List<Map<String, Object>> schemaTables(String schema) {
        String SQL = StatementUtil.getInfSchemaTables(schema);
        return dao.selectSQLMapResult(SQL);
    }

    public List<Map<String, Object>> tableColumns(String schema, String tableName) {
        String[] fields = new String[]{"ordinal_position",
            "column_name",
            "is_nullable",
            "data_type",
            "character_maximum_length",
            "udt_name",
            "numeric_precision"};
        SimpleFilter sf = new SimpleFilter();
        sf.addValue("table_schema", schema);
        sf.addValue("table_name", tableName);
        sf.addANDRestriction(new Restriction("table_schema", Restriction.EQUALS, "table_schema"));
        sf.addANDRestriction(new Restriction("table_name", Restriction.EQUALS, "table_name"));
        String SQL = StatementUtil.simpleSelectTable(fields, "information_schema", "columns", sf);
        return dao.find(SQL, sf.getValues());
    }

    public List<Map<String, Object>> tableColumns(List<Map<String, Object>> schemaColumns, String tableName) {
        List<Map<String, Object>> nl = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> t : schemaColumns) {
            if (t.get(Table.TABLE_NAME).equals(tableName)) {
                nl.add(t);
            }
        }
        return nl;
    }

    public List<Map<String, Object>> schemaColumns(String schema) {
        String SQL = StatementUtil.getInfSchemaColumns(schema);
        return dao.selectSQLMapResult(SQL);
    }

    public Map<String, String> tableColumnsMap(String schema, String tableName) {
        List<Map<String, Object>> columns = this.tableColumns(schema, tableName);
        Map<String, String> mapcolumns = new HashMap<String, String>();
        for (Map<String, Object> c : columns) {
            if (c.get("data_type").equals("USER-DEFINED")) {
                mapcolumns.put(c.get("column_name").toString(), c.get("udt_name").toString());
            } else {
                mapcolumns.put(c.get("column_name").toString(), c.get("data_type").toString());
            }
        }
        return mapcolumns;
    }

    public String getPrimaryKey(String schema, String entity) {
        String SQL = StatementUtil.getPrimaryKeyStatement(schema, entity);
        Map<String, Object> r = dao.get(SQL);
        return (String) r.get("attname");
    }

    private boolean isInArray(Object value, Object[] array) {
        for (Object o : array) {
            if (o.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, String> tableColumnsMap(String schema, String tableName, String[] nofields) {
        List<Map<String, Object>> columns = this.tableColumns(schema, tableName);
        Map<String, String> mapcolumns = new HashMap<String, String>();
        for (Map<String, Object> c : columns) {
            if (!isInArray(c.get("column_name").toString(), nofields)) {
                if (c.get("data_type").equals("USER-DEFINED")) {
                    mapcolumns.put(c.get("column_name").toString(), c.get("udt_name").toString());
                } else {
                    mapcolumns.put(c.get("column_name").toString(), c.get("data_type").toString());
                }
            }
        }
        return mapcolumns;
    }

    public Map<String, String> tableColumnsMap(List<Map<String, Object>> columns) {
        Map<String, String> mapcolumns = new HashMap<String, String>();
        for (Map<String, Object> c : columns) {
            mapcolumns.put(c.get("column_name").toString(), c.get("data_type").toString());
        }
        return mapcolumns;
    }

    public void destroyEntity(Entidad entidad) {
        if (entidad.isData()) {
            Entidad en = dao.get(Entidad.class, entidad.getId());
            if (existTable("public", en.getName())) {
                dao.execute(StatementUtil.deleteTable("public", en.getName()));
            }
        }

        if (entidad.isEntity()) {
            dao.remove(entidad);
        }
    }

    public List<Map<String, Object>> noEntityDataBase() {
        List<Entidad> entities = dao.find("from Entidad order by label");
        List<Map<String, Object>> stables = schemaTables("public");
        List<Map<String, Object>> ntables = new ArrayList<>();
        for (Map<String, Object> table : stables) {
            boolean exist = false;
            for (Entidad en : entities) {
                if (table.get(Table.TABLE_NAME).equals(en.getName())) {
                    exist = true;
                }
            }
            if (!exist) {
                ntables.add(table);
            }
        }
        return ntables;
    }

    public EntityResult get(String entity, Object id) {
        return this.get("public", entity, id);
    }

    public EntityResult get(String entity, Object id, String[] fields) {
        return this.get("public", entity, id, fields);
    }

    public EntityResult get(String schema, String entity, Object id) {
        EntityResult result = new EntityResult();
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity);
        String SQL = StatementUtil.simpleSelectObject(this.tableMapToPostgisColumns(tableMap.entrySet()), schema, entity);
        result.setObjectData(dao.get(SQL, id));
        result.setSuccess((result.getObjectData() != null));
        if (result.getSuccess()) {
            result.addToListData(result.getObjectData());
        }
        return result;
    }

    public EntityResult get(String schema, String entity, Object id, String[] fields) {
        EntityResult result = new EntityResult();
        String SQL = StatementUtil.simpleSelectObject(fields, schema, entity);
        System.out.println(SQL);
        result.setObjectData(dao.get(SQL, id));
        return result;
    }

    public EntityResult find(String entity) {
        return this.find("public", entity, new String[0]);
    }

    public EntityResult find(String schema, String entity) {
        return this.find(schema, entity, new String[0]);
    }

    public EntityResult find(String schema, String entity, String field) {
        return this.find(schema, entity, new String[0], field);
    }

    public EntityResult find(String schema, String entity, String[] orderFields, String field) {
        EntityResult result = new EntityResult();
        String[] nofields = {"the_geom"};
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity, nofields);
        String SQL = StatementUtil.simpleSelectTable(tableMap.keySet(), schema, entity, orderFields);
        SQL += " ORDER BY " + field + " ASC";
        result.setListData(dao.selectSQLMapResult(SQL));
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public EntityResult find(String schema, String entity, String[] orderFields) {
        EntityResult result = new EntityResult();
        String[] nofields = {"the_geom"};
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity, nofields);
        String SQL = StatementUtil.simpleSelectTable(tableMap.keySet(), schema, entity, orderFields);
        System.out.println(SQL);
        result.setListData(dao.selectSQLMapResult(SQL));
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public EntityResult find(String entity, SimpleFilter filter) throws ParseException {
        return this.find("public", entity, filter);
    }

    public EntityResult find(String schema, String entity, SimpleFilter filter) throws ParseException {
        EntityResult result = new EntityResult();
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity);
        String SQL = StatementUtil.simpleSelectTable(this.tableMapToPostgisColumns(tableMap.entrySet()), schema, entity, filter);
        System.out.println(SQL);
        Map<String, Object> insertable = DynamicEntityMap.parseInputMap(filter.getValues(), tableMap);
        result.setListData(dao.selectSQLMapResult(SQL, insertable));
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public EntityResult find(String entity, SimpleFilter filter, String[] fields) {
        return this.find("public", entity, filter, fields);
    }

    public EntityResult find(String schema, String entity, SimpleFilter filter, String[] fields) {
        EntityResult result = new EntityResult();
        String SQL = StatementUtil.simpleSelectTable(fields, schema, entity, filter);
        System.out.println(SQL);
        result.setListData(dao.selectSQLMapResult(SQL, filter.getValues()));
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public EntityResult persist(Map<String, Object> insertable, String entity) {
        return this.persist(insertable, "public", entity);
    }

    public EntityResult persist(Map<String, Object> insertable, String schema, String entity) {
        return this.persist(insertable, schema, entity, System.nanoTime());
    }

    public EntityResult persist(Map<String, Object> insertable, String schema, String entity, Object id) {

        Map<String, Object> Transactional = new HashMap<>();

        String _pk = "";
        if (insertable.get("___pk") == null) {
            _pk = entity + "_id";
        } else {
            _pk = (String) insertable.get("___pk");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Transactional.put("usuario", authentication.getName());
        } else {
            Transactional.put("usuario", "[client]");
        }
        Transactional.put("fecha_accion", new Date());
        Transactional.put("esquema", schema);
        Transactional.put("tabla", entity);
        Transactional.put("http", request.getHeader("host"));
        EntityResult result = new EntityResult();
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity);
        if (insertable.get(_pk) == null) {
            insertable.put(_pk, id);
            Map<String, String> ifields = DynamicEntityMap.getInsertableFields(tableMap, insertable);
            String SQL = StatementUtil.getPersistSQLStatement(tableMap, ifields, schema, entity);
            System.out.println(SQL);

            dao.persist(SQL, ifields, insertable);
            result.setPrimaryKey(id);
            Transactional.put("id_row", id);
            Transactional.put("accion", "INSERT");
        } else {
            Map<String, String> ifields = DynamicEntityMap.getInsertableFields(tableMap, insertable);
            String SQL = StatementUtil.getUpdateSQLStatement(tableMap, insertable, schema, entity, _pk);
            System.out.println(SQL);

            dao.update(SQL, ifields, insertable);
            result.setPrimaryKey(insertable.get(_pk));
            Transactional.put("id_row", insertable.get(_pk));
            Transactional.put("accion", "UPDATE");
        }
        result.setSuccess(Boolean.TRUE);

        Transactional.put("jornal_audit_id", System.nanoTime());
        Map<String, String> tableMapTransaction = this.tableColumnsMap("transaction", "jornal_audit");
        Map<String, String> ifields = DynamicEntityMap.getInsertableFields(tableMapTransaction, Transactional);

        String SQL = StatementUtil.getPersistSQLStatement(tableMapTransaction, ifields, "transaction", "jornal_audit");

        dao.persist(SQL, ifields, Transactional);
        return result;
    }

    public List<Map<String, Object>> getAddOnEntitiess() {
        return dao.selectSQLMapResult("select id, label, name from logic.entidad where visible is not null");
    }

    public boolean existObject(String schema, String entity, String field, Object value) {
        return dao.existObject(schema, entity, field, value);
    }

    public Map<String, Object> getExistObject(String schema, String entity, String field, Object value) throws ParseException {
        SimpleFilter sf = new SimpleFilter();
        sf.addANDRestriction(new Restriction(field, Restriction.EQUALS));
        sf.addValue(field, value);
        return this.find(schema, entity, sf).getObjectData();
    }

    public Map<String, Object> getExistObject(String schema, String entity, String field, Object value, String[] fields) {
        SimpleFilter sf = new SimpleFilter();
        sf.addANDRestriction(new Restriction(field, Restriction.EQUALS));
        sf.addValue(field, value);
        return this.find(schema, entity, sf, fields).getObjectData();
    }

    public Map<String, Object> buildPersistenObjectFromRequest(String schema, String entity, HttpServletRequest request) throws ParseException {
        Map<String, Object> input = DynamicEntityMap.requestMapToEntityMap(request.getParameterMap());
        Map<String, String> tableMap = tableColumnsMap(schema, entity);
        Map<String, Object> insertable = DynamicEntityMap.parseInputMap(input, tableMap);
        return insertable;
    }

    public Map<String, Object> buildPersistenObjectFromRequest(String schema, String entity, Map<String, Object> input) throws ParseException {
        Map<String, String> tableMap = tableColumnsMap(schema, entity);
        Map<String, Object> insertable = DynamicEntityMap.parseInputMap(input, tableMap);
        return insertable;
    }

    public Map<String, Object> getInsertableFields(String schema, String entity, Map<String, Object> input) {
        Map<String, String> tableMap = tableColumnsMap(schema, entity);
        Map<String, Object> insertable = new HashMap<>(); //DynamicEntityMap.parseInputMap(input, tableMap);
        for (Entry<String, String> c : tableMap.entrySet()) {
            if (input.get(c.getKey()) != null) {
                insertable.put(c.getKey(), input.get(c.getKey()));
            }
        }
        return insertable;
    }

    public Map<String, Object> convertBeanMapToMap(BeanMap bm) {
        Map<String, Object> hm = new HashMap<>();
        Iterator<String> it = bm.keyIterator();
        while (it.hasNext()) {
            String key = it.next();
            if (bm.get(key) != null) {
                hm.put(key, bm.get(key));
            }
        }
        return hm;
    }

    public Map<String, Object> ignoreNullValues(Map<String, Object> input) {
        Map<String, Object> output = new HashMap<>();
        for (Entry<String, Object> e : input.entrySet()) {
            if (e.getValue() != null) {
                output.put(e.getKey(), e.getValue());
            }
        }
        return output;
    }

    public Set<String> tableMapToPostgisColumns(Set<Entry<String, String>> entries) {
        Set<String> cols = new HashSet<>();
        for (Entry e : entries) {
            System.out.println("---- " + e.getKey() + " - " + e.getValue());
            if (e.getValue().equals(Attribute.GEOMETRY)) {
                cols.add("ST_AsText(\"" + e.getKey() + "\") AS \"" + e.getKey() + "\""
                        + ", ST_AsGeoJSON(CAST(\"" + e.getKey() + "\" AS text)) AS \"_geo_json\"");
            } else {
                cols.add((String) "\"" + e.getKey() + "\"");
            }
        }
        return cols;
    }

    @Override
    public EntityResult delete(String entity, Long id) {
        return this.delete("public", entity, id);
    }

    @Override
    public EntityResult delete(String schema, String entity, Long id) {
        return this.delete(schema, entity, id, entity + "_id");
    }

    @Override
    public EntityResult delete(String schema, String entity, Long id, String pk) {
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity);
        String SQL = StatementUtil.deleteObject(schema, entity, pk);
        dao.remove(SQL, id);
        EntityResult er = new EntityResult();
        er.setPrimaryKey(id);
        er.setSuccess(Boolean.TRUE);

        Map<String, Object> Transactional = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Transactional.put("usuario", authentication.getName());
        } else {
            Transactional.put("usuario", "[client]");
        }
        Transactional.put("fecha_accion", new Date());
        Transactional.put("esquema", schema);
        Transactional.put("tabla", entity);
        Transactional.put("http", request.getHeader("host"));

        Transactional.put("id_row", id);
        Transactional.put("accion", "DELETE");
        Transactional.put("jornal_audit_id", System.nanoTime());
        Map<String, String> tableMapTransaction = this.tableColumnsMap("transaction", "jornal_audit");
        Map<String, String> ifields = DynamicEntityMap.getInsertableFields(tableMapTransaction, Transactional);

        String SQLTrans = StatementUtil.getPersistSQLStatement(tableMapTransaction, ifields, "transaction", "jornal_audit");

        dao.persist(SQLTrans, ifields, Transactional);

        return er;
    }

    public EntityResult contains(String entity, String geomText) {
        return this.contains("public", entity, geomText);
    }

    public EntityResult contains(String schema, String entity, String geomText) {
        return this.contains(schema, entity, geomText, new String[0]);
    }

    public EntityResult contains(String schema, String entity, String geomText, String[] orderFields) {
        EntityResult result = new EntityResult();
        Map<String, String> tableMap = this.tableColumnsMap(schema, entity);
        String SQL = StatementUtil.selectContains(this.tableMapToPostgisColumns(tableMap.entrySet()), schema, entity, "the_geom", orderFields);

        System.out.println(SQL);
        System.out.println(geomText);

        Map<String, Object> params = new HashMap<>();
        params.put("geomtext", geomText);
        result.setListData(dao.find(SQL, params));

        result.setSuccess(Boolean.TRUE);
        return result;
    }

    @Override
    public EntityResult selectQueryForName(String name) {
        EntityResult result = new EntityResult();
        SQLCommand in = new SQLCommand();
        in.setName(name);
        SQLCommand c = dao.findHqlForUniqueResult("from SQLCommand where name = :name", in);
        logForExecuteSQL(c);
        if (c != null) {
            result.setListData(dao.selectSQLMapResult(c.getSqlCommand()));
            result.setSuccess(Boolean.TRUE);
        } else {
            result.setSuccess(Boolean.FALSE);
        }
        return result;
    }

    @Override
    public EntityResult selectQueryForName(String name, Object[] params) throws ParseException {
        EntityResult result = new EntityResult();
        SQLCommand in = new SQLCommand();
        in.setName(name);
        SQLCommand c = dao.findHqlForUniqueResult("from SQLCommand where name = :name", in);
        logForExecuteSQL(c);
        if (c != null) {
            result.setListData(dao.selectSQLMapResult(c.getSqlCommand(), params));
            result.setSuccess(Boolean.TRUE);
        } else {
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage("SQL Statement by name '" + name + "' not found");
        }
        return result;
    }

    @Override
    public EntityResult selectQueryForName(String name, Object value) throws ParseException {
        EntityResult result = new EntityResult();
        SQLCommand in = new SQLCommand();
        in.setName(name);
        SQLCommand c = dao.findHqlForUniqueResult("from SQLCommand where name = :name", in);
        logForExecuteSQL(c);
        if (c != null) {
            result.setListData(dao.selectSQLMapResult(c.getSqlCommand(), value));
            result.setSuccess(Boolean.TRUE);
        } else {
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage("SQL Statement by name '" + name + "' not found");
        }
        return result;
    }

    @Override
    public EntityResult selectQueryForName(String name, Map<String, Object> stringParams) throws ParseException {
        EntityResult result = new EntityResult();
        SQLCommand in = new SQLCommand();
        in.setName(name);
        SQLCommand c = dao.findHqlForUniqueResult("from SQLCommand where name = :name", in);
        logForExecuteSQL(c);
        if (c != null) {
            if (c.getParams() != null) {
                System.out.println("params length: " + c.getParams().size());
                Map<String, String> params = new HashMap<>();
                for (HttpParam p : c.getParams()) {
                    params.put(p.getName(), p.getTypeClass());
                }
                Map<String, Object> insertable = DynamicEntityMap.parseInputMap(stringParams, params);

                result.setListData(dao.selectSQLMapResult(c.getSqlCommand(), insertable));
            } else {
                result.setListData(dao.selectSQLMapResult(c.getSqlCommand()));
            }
            result.setSuccess(Boolean.TRUE);
        } else {
            result.setSuccess(Boolean.FALSE);
            result.setErrorMessage("SQL Statement by name '" + name + "' not found");
        }
        return result;
    }

    private void logForExecuteSQL(SQLCommand c) {
        if (c != null) {
            System.out.println("Load SQL for name: " + c.getName());
            System.out.println("SQL description: " + c.getDescription());
            System.out.println("SQL params: " + c.getJsonParams());
            System.out.println("SQL statement: " + c.getSqlCommand());
        } else {
            System.out.println("Load SQL for name: NOT FOUND");
        }
    }

    public HashMap<String, Object> getJsonMap(String jsondata) throws IOException {
        return new ObjectMapper().readValue(jsondata, HashMap.class);
    }

    @Override
    public Long countEntity(String schema, String entity) {
        Map<String, Object> res = dao.get(StatementUtil.getCount(schema, entity));
        BigInteger bi = (BigInteger) res.get("count");
        return bi.longValue();
    }

    @Override
    public void updateSingleAttribute(String schema, String entity, Object id, String attib, Object value) {
        Entity en = new Entity();
        en.setId(entity);
        en.setSchema(schema);
        en.setAttributes(new ArrayList<>());
        Attribute a = new Attribute();
        a.setName(attib);
        a.setType(Attribute.VARCHAR);
        en.addAttribute(a);

        Map<String, Object> in = new HashMap<>();
        in.put(attib, value);
        in.put(en.primaryKeyName(), id);

        dao.persist(StatementUtil.getUpdateSQLStatement(en), in);
    }

    @Override
    public void cargarArchivo(MultipartFile webFile, String path) throws IOException {
        cargarArchivo(webFile, path, webFile.getOriginalFilename());
    }

    @Override
    public void cargarArchivo(MultipartFile webFile, String path, String filename) throws IOException {
        FileUtils.copyInputStreamToFile(webFile.getInputStream(), new File(path + filename));
    }

    @Override
    public Map<String, String> systemParams() {
        Map<String, String> ps = new HashMap<>();
        EntityResult er = find("logic", "param");
        for (Map<String, Object> e : er.getListData()) {
            ps.put(e.get("key").toString(), e.get("value") == null ? "" : e.get("value").toString());
        }
        return ps;
    }

    @Override
    public String pin() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(10000);
        String formatted = String.format("%04d", num);
        return formatted;
    }

    @Override
    public Object saveFile(MultipartFile file, String fileDestination) throws IOException {
        String filename = Long.toString(System.nanoTime());

        Map<String, Object> in = new HashMap<>();
        in.put("contenttype", file.getContentType());
        in.put("serverpath", fileDestination);
        in.put("size", file.getSize());
        in.put("originalfilename", file.getOriginalFilename());
        in.put("serverfilename", filename);
        in.put("registro", new Date());

        // create file
        EntityResult er = this.persist(in, "files", "filedata");
        this.cargarArchivo(file, fileDestination, filename);

        // return ID file
        return er.getPrimaryKey();
    }

    @Override
    public EntityResult nativeQueryFind(String sql, Object value) {
        EntityResult result = new EntityResult();
        result.setListData(dao.selectSQLMapResult(sql, value));
        result.setSuccess(Boolean.TRUE);
        return result;
    }
    
    @Override
    public EntityResult nativeQueryFind(String sql) {
        EntityResult result = new EntityResult();
        result.setListData(dao.selectSQLMapResult(sql));
        result.setSuccess(Boolean.TRUE);
        return result;
    }

}
