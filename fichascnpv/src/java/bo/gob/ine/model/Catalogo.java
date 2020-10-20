/*
 * ICG SRL - International Consulting Group 2012
 */
package bo.gob.ine.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 *
 * @author John Castillo
 */
@Entity
@Table(schema = "LOGIC")
public class Catalogo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalogo_id")
    private Long id;
    @Column(unique = true)
    private String nombre;
    private String descripcion;
    private boolean lock;
    @OneToMany(mappedBy = "catalogo", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CatalogoItem> data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<CatalogoItem> getData() {
        return data;
    }

    public void setData(List<CatalogoItem> data) {
        this.data = data;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean existeValor(String valor) {
        boolean existeValor = false;
        for (CatalogoItem ci : getData()) {
            if (ci.getValor().equals(valor)) {
                existeValor = true;
            }
        }
        return existeValor;
    }
}
