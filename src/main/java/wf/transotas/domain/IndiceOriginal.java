package wf.transotas.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IndiceOriginal.
 */
@Entity
@Table(name = "indice_original")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "indiceoriginal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IndiceOriginal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "img")
    private String img;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "url")
    private String url;

    @Column(name = "autor")
    private String autor;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "estado")
    private String estado;

    @Column(name = "pais")
    private String pais;

    @Column(name = "comentarios")
    private Integer comentarios;

    @Column(name = "vistas")
    private Integer vistas;

    @Column(name = "rating")
    private Integer rating;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IndiceOriginal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return this.img;
    }

    public IndiceOriginal img(String img) {
        this.setImg(img);
        return this;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public IndiceOriginal titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return this.url;
    }

    public IndiceOriginal url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAutor() {
        return this.autor;
    }

    public IndiceOriginal autor(String autor) {
        this.setAutor(autor);
        return this;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public IndiceOriginal fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public IndiceOriginal ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return this.estado;
    }

    public IndiceOriginal estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return this.pais;
    }

    public IndiceOriginal pais(String pais) {
        this.setPais(pais);
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Integer getComentarios() {
        return this.comentarios;
    }

    public IndiceOriginal comentarios(Integer comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public void setComentarios(Integer comentarios) {
        this.comentarios = comentarios;
    }

    public Integer getVistas() {
        return this.vistas;
    }

    public IndiceOriginal vistas(Integer vistas) {
        this.setVistas(vistas);
        return this;
    }

    public void setVistas(Integer vistas) {
        this.vistas = vistas;
    }

    public Integer getRating() {
        return this.rating;
    }

    public IndiceOriginal rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndiceOriginal)) {
            return false;
        }
        return id != null && id.equals(((IndiceOriginal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndiceOriginal{" +
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
