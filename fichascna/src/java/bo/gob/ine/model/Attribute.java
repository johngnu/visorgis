/*
 * ICG SRL - International Consulting Group 2012
 */
package bo.gob.ine.model;

import java.io.Serializable;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.codehaus.jackson.annotate.JsonBackReference;

/**
 *
 * @author John Castillo
 */
@javax.persistence.Entity
@Table(schema = "logic")
public class Attribute implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private Long id;
    private String name;
    private String label;
    private String description;
    private String typeClass;
    @Lob
    private String jsonStruct;
    @Transient
    private Map<String, Object> properties;
    @Transient
    private boolean readOnly;
    @Transient
    private boolean hasError;
    @ManyToOne
    @JsonBackReference
    private Entity entity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    public String getJsonStruct() {
        return jsonStruct;
    }

    public void setJsonStruct(String jsonStruct) {
        this.jsonStruct = jsonStruct;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

}
