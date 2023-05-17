package wf.transotas.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link wf.transotas.domain.Reportes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportesDTO implements Serializable {

    private Long id;

    private String titulo;

    private String caso;

    private String img;

    private String autor;

    private String tituloix;

    private String autorix;

    private Instant fechaix;

    private String imgix;

    private String ciudad;

    private String estado;

    private String pais;

    private String extra1;

    private String extra2;

    private String extra3;

    private String extra4;

    private String extra5;

    private String extra6;

    private String extra7;

    private String extra8;

    private String extra9;

    private String extra10;

    private InformacionDTO informacion;

    private CasoTextDTO casoText;

    private Set<CategorysDTO> categorys = new HashSet<>();

    private Set<ComentariosDTO> comentarios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCaso() {
        return caso;
    }

    public void setCaso(String caso) {
        this.caso = caso;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTituloix() {
        return tituloix;
    }

    public void setTituloix(String tituloix) {
        this.tituloix = tituloix;
    }

    public String getAutorix() {
        return autorix;
    }

    public void setAutorix(String autorix) {
        this.autorix = autorix;
    }

    public Instant getFechaix() {
        return fechaix;
    }

    public void setFechaix(Instant fechaix) {
        this.fechaix = fechaix;
    }

    public String getImgix() {
        return imgix;
    }

    public void setImgix(String imgix) {
        this.imgix = imgix;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return extra4;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getExtra5() {
        return extra5;
    }

    public void setExtra5(String extra5) {
        this.extra5 = extra5;
    }

    public String getExtra6() {
        return extra6;
    }

    public void setExtra6(String extra6) {
        this.extra6 = extra6;
    }

    public String getExtra7() {
        return extra7;
    }

    public void setExtra7(String extra7) {
        this.extra7 = extra7;
    }

    public String getExtra8() {
        return extra8;
    }

    public void setExtra8(String extra8) {
        this.extra8 = extra8;
    }

    public String getExtra9() {
        return extra9;
    }

    public void setExtra9(String extra9) {
        this.extra9 = extra9;
    }

    public String getExtra10() {
        return extra10;
    }

    public void setExtra10(String extra10) {
        this.extra10 = extra10;
    }

    public InformacionDTO getInformacion() {
        return informacion;
    }

    public void setInformacion(InformacionDTO informacion) {
        this.informacion = informacion;
    }

    public CasoTextDTO getCasoText() {
        return casoText;
    }

    public void setCasoText(CasoTextDTO casoText) {
        this.casoText = casoText;
    }

    public Set<CategorysDTO> getCategorys() {
        return categorys;
    }

    public void setCategorys(Set<CategorysDTO> categorys) {
        this.categorys = categorys;
    }

    public Set<ComentariosDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<ComentariosDTO> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportesDTO)) {
            return false;
        }

        ReportesDTO reportesDTO = (ReportesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportesDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", caso='" + getCaso() + "'" +
            ", img='" + getImg() + "'" +
            ", autor='" + getAutor() + "'" +
            ", tituloix='" + getTituloix() + "'" +
            ", autorix='" + getAutorix() + "'" +
            ", fechaix='" + getFechaix() + "'" +
            ", imgix='" + getImgix() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", estado='" + getEstado() + "'" +
            ", pais='" + getPais() + "'" +
            ", extra1='" + getExtra1() + "'" +
            ", extra2='" + getExtra2() + "'" +
            ", extra3='" + getExtra3() + "'" +
            ", extra4='" + getExtra4() + "'" +
            ", extra5='" + getExtra5() + "'" +
            ", extra6='" + getExtra6() + "'" +
            ", extra7='" + getExtra7() + "'" +
            ", extra8='" + getExtra8() + "'" +
            ", extra9='" + getExtra9() + "'" +
            ", extra10='" + getExtra10() + "'" +
            ", informacion=" + getInformacion() +
            ", casoText=" + getCasoText() +
            ", categorys=" + getCategorys() +
            ", comentarios=" + getComentarios() +
            "}";
    }
}
