/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.ine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 *
 * @author John Castillo
 */
@javax.persistence.Entity
@Table(schema = "logic")
public class Entity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emtity_id")
    private Long id;
    @Column(unique = true)
    private String name;
    @OneToOne
    private Entity parent;
    private String label;
    @Column(length = 2000)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createOn;
    private boolean locked;
    @OneToMany(mappedBy = "entity", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Attribute> attrs;

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

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public List<Attribute> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Attribute> attrs) {
        this.attrs = attrs;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean existAttributeByName(String nameAttribute) {
        for (Attribute a : this.getAllAttrs()) {
            if (a.getName().equals(nameAttribute)) {
                return true;
            }
        }
        return false;
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
    }

    public List<Attribute> getAllAttrs() {
        List<Attribute> lst = new ArrayList<Attribute>();
        if (this.parent != null) {
            for (Attribute a : this.parent.getAllAttrs()) {
                a.setReadOnly(Boolean.TRUE);
                lst.add(a);
            }
            for (Attribute a : this.attrs) {
                if (this.parent.existAttributeByName(a.getName())) {
                    a.setHasError(Boolean.TRUE);
                }
                lst.add(a);
            }
        } else {
            lst.addAll(this.attrs);
        }
        return lst;
    }
}
