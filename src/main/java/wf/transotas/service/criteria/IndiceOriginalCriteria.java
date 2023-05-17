package wf.transotas.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link wf.transotas.domain.IndiceOriginal} entity. This class is used
 * in {@link wf.transotas.web.rest.IndiceOriginalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /indice-originals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndiceOriginalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter img;

    private StringFilter titulo;

    private StringFilter url;

    private StringFilter autor;

    private InstantFilter fecha;

    private StringFilter ciudad;

    private StringFilter estado;

    private StringFilter pais;

    private IntegerFilter comentarios;

    private IntegerFilter vistas;

    private IntegerFilter rating;

    private Boolean distinct;

    public IndiceOriginalCriteria() {}

    public IndiceOriginalCriteria(IndiceOriginalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.img = other.img == null ? null : other.img.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.autor = other.autor == null ? null : other.autor.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.ciudad = other.ciudad == null ? null : other.ciudad.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.pais = other.pais == null ? null : other.pais.copy();
        this.comentarios = other.comentarios == null ? null : other.comentarios.copy();
        this.vistas = other.vistas == null ? null : other.vistas.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IndiceOriginalCriteria copy() {
        return new IndiceOriginalCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImg() {
        return img;
    }

    public StringFilter img() {
        if (img == null) {
            img = new StringFilter();
        }
        return img;
    }

    public void setImg(StringFilter img) {
        this.img = img;
    }

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public StringFilter getUrl() {
        return url;
    }

    public StringFilter url() {
        if (url == null) {
            url = new StringFilter();
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getAutor() {
        return autor;
    }

    public StringFilter autor() {
        if (autor == null) {
            autor = new StringFilter();
        }
        return autor;
    }

    public void setAutor(StringFilter autor) {
        this.autor = autor;
    }

    public InstantFilter getFecha() {
        return fecha;
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            fecha = new InstantFilter();
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
    }

    public StringFilter getCiudad() {
        return ciudad;
    }

    public StringFilter ciudad() {
        if (ciudad == null) {
            ciudad = new StringFilter();
        }
        return ciudad;
    }

    public void setCiudad(StringFilter ciudad) {
        this.ciudad = ciudad;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public StringFilter estado() {
        if (estado == null) {
            estado = new StringFilter();
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public StringFilter getPais() {
        return pais;
    }

    public StringFilter pais() {
        if (pais == null) {
            pais = new StringFilter();
        }
        return pais;
    }

    public void setPais(StringFilter pais) {
        this.pais = pais;
    }

    public IntegerFilter getComentarios() {
        return comentarios;
    }

    public IntegerFilter comentarios() {
        if (comentarios == null) {
            comentarios = new IntegerFilter();
        }
        return comentarios;
    }

    public void setComentarios(IntegerFilter comentarios) {
        this.comentarios = comentarios;
    }

    public IntegerFilter getVistas() {
        return vistas;
    }

    public IntegerFilter vistas() {
        if (vistas == null) {
            vistas = new IntegerFilter();
        }
        return vistas;
    }

    public void setVistas(IntegerFilter vistas) {
        this.vistas = vistas;
    }

    public IntegerFilter getRating() {
        return rating;
    }

    public IntegerFilter rating() {
        if (rating == null) {
            rating = new IntegerFilter();
        }
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IndiceOriginalCriteria that = (IndiceOriginalCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(img, that.img) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(url, that.url) &&
            Objects.equals(autor, that.autor) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(ciudad, that.ciudad) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(pais, that.pais) &&
            Objects.equals(comentarios, that.comentarios) &&
            Objects.equals(vistas, that.vistas) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, img, titulo, url, autor, fecha, ciudad, estado, pais, comentarios, vistas, rating, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndiceOriginalCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (img != null ? "img=" + img + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (url != null ? "url=" + url + ", " : "") +
            (autor != null ? "autor=" + autor + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (ciudad != null ? "ciudad=" + ciudad + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (pais != null ? "pais=" + pais + ", " : "") +
            (comentarios != null ? "comentarios=" + comentarios + ", " : "") +
            (vistas != null ? "vistas=" + vistas + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
