/*
 * ICG SRL - International Consulting Group 2017
 */
package bo.gob.ine.model;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author John Castillo
 */
public class Archivo {

    private Long basse;
    private Long variable;
    private MultipartFile file;

    public Long getBasse() {
        return basse;
    }

    public void setBasse(Long basse) {
        this.basse = basse;
    }

    public Long getVariable() {
        return variable;
    }

    public void setVariable(Long variable) {
        this.variable = variable;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
