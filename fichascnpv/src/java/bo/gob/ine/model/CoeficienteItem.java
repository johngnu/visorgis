/*
 * ICG SRL - International Consulting Group 2012
 */
package bo.gob.ine.model;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author John Castillo
 */
public class CoeficienteItem {

    private Long coeficiente_id;
    private Long catalogoitem;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fecha;
    private Double valor;

    public Long getCoeficiente_id() {
        return coeficiente_id;
    }

    public void setCoeficiente_id(Long coeficiente_id) {
        this.coeficiente_id = coeficiente_id;
    }

    public Long getCatalogoitem() {
        return catalogoitem;
    }

    public void setCatalogoitem(Long catalogoitem) {
        this.catalogoitem = catalogoitem;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
