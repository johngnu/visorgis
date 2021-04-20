/*
 * ICG SRL - International Consulting Group 2011
 */
package bo.gob.ine.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Usuario
 *
 * @author Johnston Castillo Valencia email: john.gnu@gmail.com
 * @since 01-03-2011
 */
public class Usuario implements Serializable {

    private Long usuario_id;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String descripcion;
    private String usuario;
    private String email;
    private String telefonos;
    private String domicilio;
    private String tipo_documento;
    private String nro_documento;
    private String sexo;
    private String rol;
    private String rolesJson;
    private String clave;
    private String pin;
    private Boolean activo = false;
    private Boolean plus = false;
    private Boolean externo = false;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date caducaen;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date iniciaen;
    private Long persona;
    private String usuario_externo;
    private Set<String> roles;
    private Long cliente;
    private Long principal;

    public boolean hasRole(String srch) {
        if (rol != null) {
            return StringUtils.contains(rol, srch);
        } else {
            return false;
        }
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getPlus() {
        return plus;
    }

    public void setPlus(Boolean plus) {
        this.plus = plus;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getNro_documento() {
        return nro_documento;
    }

    public void setNro_documento(String nro_documento) {
        this.nro_documento = nro_documento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Long getPersona() {
        return persona;
    }

    public void setPersona(Long persona) {
        this.persona = persona;
    }

    public String getRolesJson() {
        return rolesJson;
    }

    public void setRolesJson(String rolesJson) {
        this.rolesJson = rolesJson;
    }

    public Boolean getExterno() {
        return externo;
    }

    public void setExterno(Boolean externo) {
        this.externo = externo;
    }

    public Date getCaducaen() {
        return caducaen;
    }

    public void setCaducaen(Date caducaen) {
        this.caducaen = caducaen;
    }

    public Date getIniciaen() {
        return iniciaen;
    }

    public void setIniciaen(Date iniciaen) {
        this.iniciaen = iniciaen;
    }

    public String getUsuario_externo() {
        return usuario_externo;
    }

    public void setUsuario_externo(String usuario_externo) {
        this.usuario_externo = usuario_externo;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Long getPrincipal() {
        return principal;
    }

    public void setPrincipal(Long principal) {
        this.principal = principal;
    }

}
