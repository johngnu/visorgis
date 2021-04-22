/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.ine.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author John Castillo
 */
@javax.persistence.Entity
@Table(schema = "logic")
public class SQLCommand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    @Lob
    private String jsonParams;
    @Lob
    private String sqlCommand;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createOn;
    private boolean locked;
    @Transient
    private List<HttpParam> params;

    public void addParam(HttpParam param) {
        if (this.getParams() == null) {
            params = new ArrayList<>();
        }
        params.add(param);
        Gson gson = new Gson();
        this.setJsonParams(gson.toJson(params));
    }

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

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }

    public String getSqlCommand() {
        return sqlCommand;
    }

    public void setSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public List<HttpParam> getParams() {
        Type listType = new TypeToken<ArrayList<HttpParam>>() {
        }.getType();
        List<HttpParam> lst = new Gson().fromJson(this.jsonParams, listType);
        this.params = lst;
        return lst;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void removeParam(String key) {
        List<HttpParam> lst = getParams();
        List<HttpParam> newlst = new ArrayList<>();
        for (HttpParam p : lst) {
            if (!p.getName().equals(key)) {
                newlst.add(p);
            }
        }
        Gson gson = new Gson();
        this.setJsonParams(gson.toJson(newlst));
    }

}
