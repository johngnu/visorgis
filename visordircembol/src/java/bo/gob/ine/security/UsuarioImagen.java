/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import org.springframework.web.multipart.MultipartFile;

/**
 * UsuarioImagen
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
public class UsuarioImagen {

    private Long user_id;
    private MultipartFile user_image;

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public MultipartFile getUser_image() {
        return user_image;
    }

    public void setUser_image(MultipartFile user_image) {
        this.user_image = user_image;
    }

}
