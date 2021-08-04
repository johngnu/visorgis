/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.services;

import com.icg.entityclassutils.Attribute;
import com.icg.entityclassutils.EntityResult;
import com.icg.entityclassutils.SimpleFilter;
import bo.gob.ine.model.Entidad;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanMap;
import org.heyma.packageutils.extja.treeloader.Element;
import org.heyma.packageutils.extja.treeloader.Folder;
import org.springframework.web.multipart.MultipartFile;

/**
 * IEntityServices
 *
 * @since 2011-02-13
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 */
public interface IEntityServices {

    List<Folder> getEntities(String pkg) throws ClassNotFoundException, IOException;

    void executeSQLStatement(String sql);

    public List<Attribute> getAttributesFromEntity(Object entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    public void updateEntityStruct(Object entity);

    public boolean existTable(String schema, String tablename);

    public boolean existColumn(String schema, String tablename, String columnname);

    public void alterDropColumn(Attribute attr);

    public List<Entidad> getEntities();

    public List<Element> EntitiesToExtTreeElements();

    public <T> List<T> getListFromStructProperty(Object entity, Class<T> type, String lstProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    public void setListToStructProperty(Object entity, List<? extends Object> objects, String lstProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    public <T> T jsonToObject(String JSON, Class<T> type);

    void createEntity(Entidad entidad);

    public List<Map<String, Object>> schemaTables(String schema);

    public List<Map<String, Object>> tableColumns(String schema, String tableName);

    public List<Map<String, Object>> tableColumns(List<Map<String, Object>> schemaColumns, String tableName);

    public List<Map<String, Object>> schemaColumns(String schema);

    public Map<String, String> tableColumnsMap(String schema, String tableName);

    public Map<String, String> tableColumnsMap(List<Map<String, Object>> columns);

    public void destroyEntity(Entidad entidad);

    public List<Map<String, Object>> noEntityDataBase();

    public EntityResult get(String entity, Object id);

    public EntityResult get(String entity, Object id, String[] fields);

    public EntityResult get(String schema, String entity, Object id);

    public EntityResult get(String schema, String entity, Object id, String[] fields);

    public EntityResult find(String entity);

    public EntityResult find(String schema, String entity);

    public EntityResult find(String schema, String entity, String field);

    public EntityResult find(String schema, String entity, String[] orderFields);

    public EntityResult find(String entity, SimpleFilter filter) throws ParseException;

    public EntityResult find(String schema, String entity, SimpleFilter filter) throws ParseException;

    public EntityResult find(String entity, SimpleFilter filter, String[] fields);

    public EntityResult find(String schema, String entity, SimpleFilter filter, String[] fields);

    public EntityResult persist(Map<String, Object> insertable, String entity);

    public EntityResult persist(Map<String, Object> insertable, String schema, String entity);

    public EntityResult persist(Map<String, Object> insertable, String schema, String entity, Object id);

    public EntityResult delete(String schema, String entity, Long id);

    public EntityResult delete(String entity, Long id);

    public EntityResult delete(String schema, String entity, Long id, String pk);

    public List<Map<String, Object>> getAddOnEntitiess();

    public boolean existObject(String schema, String entity, String field, Object value);

    public Map<String, Object> getExistObject(String schema, String entity, String field, Object value) throws ParseException;

    public Map<String, Object> getExistObject(String schema, String entity, String field, Object value, String[] fields);

    public Map<String, Object> buildPersistenObjectFromRequest(String schema, String entity, HttpServletRequest request) throws ParseException;

    public Map<String, Object> buildPersistenObjectFromRequest(String schema, String entity, Map<String, Object> input) throws ParseException;

    public Set<String> tableMapToPostgisColumns(Set<Entry<String, String>> entries);

    public EntityResult contains(String entity, String geomText);

    public EntityResult contains(String schema, String entity, String geomText);

    public EntityResult contains(String schema, String entity, String geomText, String[] orderFields);

    public EntityResult selectQueryForName(String name, Map<String, Object> stringParams) throws ParseException;

    public EntityResult selectQueryForName(String name);

    public EntityResult selectQueryForName(String name, Object[] params) throws ParseException;

    public EntityResult selectQueryForName(String name, Object value) throws ParseException;

    public HashMap<String, Object> getJsonMap(String jsondata) throws IOException;

    public Map<String, Object> getInsertableFields(String schema, String entity, Map<String, Object> input);

    public Map<String, Object> convertBeanMapToMap(BeanMap bm);

    public Map<String, Object> ignoreNullValues(Map<String, Object> input);

    public Long countEntity(String schema, String entity);

    public void updateSingleAttribute(String schema, String entity, Object id, String attib, Object value);

    public void cargarArchivo(MultipartFile webFile, String path) throws IOException;

    public void cargarArchivo(MultipartFile webFile, String path, String filename) throws IOException;

    public Map<String, String> systemParams();

    public String pin();

    public Object saveFile(MultipartFile file, String fileDestination) throws IOException;
    
    public EntityResult nativeQueryFind(String sql, Object value);
    
    public EntityResult nativeQueryFind(String sql);
}
