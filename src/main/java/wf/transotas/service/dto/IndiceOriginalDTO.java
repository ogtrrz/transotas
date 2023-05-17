package wf.transotas.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link wf.transotas.domain.IndiceOriginal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndiceOriginalDTO implements Serializable {

    private Long id;

    private String img;

    private String titulo;

    private String url;

    private String autor;

    private Instant fecha;

    private String ciudad;

    private String estado;

    private String pais;

    private Integer comentarios;

    private Integer vistas;

    private Integer rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
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

    public Integer getComentarios() {
        return comentarios;
    }

    public void setComentarios(Integer comentarios) {
        this.comentarios = comentarios;
    }

    public Integer getVistas() {
        return vistas;
    }

    public void setVistas(Integer vistas) {
        this.vistas = vistas;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndiceOriginalDTO)) {
            return false;
        }

        IndiceOriginalDTO indiceOriginalDTO = (IndiceOriginalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, indiceOriginalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndiceOriginalDTO{" +
            "id=" + getId() +
            ", img='" + getImg() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", url='" + getUrl() + "'" +
            ", autor='" + getAutor() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", estado='" + getEstado() + "'" +
            ", pais='" + getPais() + "'" +
            ", comentarios=" + getComentarios() +
            ", vistas=" + getVistas() +
            ", rating=" + getRating() +
            "}";
    }
}
