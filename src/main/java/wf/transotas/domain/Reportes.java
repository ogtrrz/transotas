package wf.transotas.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reportes.
 */
@Entity
@Table(name = "reportes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reportes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reportes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "caso")
    private String caso;

    @Column(name = "img")
    private String img;

    @Column(name = "autor")
    private String autor;

    @Column(name = "tituloix")
    private String tituloix;

    @Column(name = "autorix")
    private String autorix;

    @Column(name = "fechaix")
    private Instant fechaix;

    @Column(name = "imgix")
    private String imgix;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "estado")
    private String estado;

    @Column(name = "pais")
    private String pais;

    @Column(name = "extra_1")
    private String extra1;

    @Column(name = "extra_2")
    private String extra2;

    @Column(name = "extra_3")
    private String extra3;

    @Column(name = "extra_4")
    private String extra4;

    @Column(name = "extra_5")
    private String extra5;

    @Column(name = "extra_6")
    private String extra6;

    @Column(name = "extra_7")
    private String extra7;

    @Column(name = "extra_8")
    private String extra8;

    @Column(name = "extra_9")
    private String extra9;

    @Column(name = "extra_10")
    private String extra10;

    @OneToOne
    @JoinColumn(unique = true)
    private Informacion informacion;

    @ManyToMany(mappedBy = "reportes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "reportes" }, allowSetters = true)
    private Set<Categorys> categorys = new HashSet<>();

    @ManyToMany(mappedBy = "reportes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "reportes" }, allowSetters = true)
    private Set<Comentarios> comentarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reportes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Reportes titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCaso() {
        return this.caso;
    }

    public Reportes caso(String caso) {
        this.setCaso(caso);
        return this;
    }

    public void setCaso(String caso) {
        this.caso = caso;
    }

    public String getImg() {
        return this.img;
    }

    public Reportes img(String img) {
        this.setImg(img);
        return this;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAutor() {
        return this.autor;
    }

    public Reportes autor(String autor) {
        this.setAutor(autor);
        return this;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTituloix() {
        return this.tituloix;
    }

    public Reportes tituloix(String tituloix) {
        this.setTituloix(tituloix);
        return this;
    }

    public void setTituloix(String tituloix) {
        this.tituloix = tituloix;
    }

    public String getAutorix() {
        return this.autorix;
    }

    public Reportes autorix(String autorix) {
        this.setAutorix(autorix);
        return this;
    }

    public void setAutorix(String autorix) {
        this.autorix = autorix;
    }

    public Instant getFechaix() {
        return this.fechaix;
    }

    public Reportes fechaix(Instant fechaix) {
        this.setFechaix(fechaix);
        return this;
    }

    public void setFechaix(Instant fechaix) {
        this.fechaix = fechaix;
    }

    public String getImgix() {
        return this.imgix;
    }

    public Reportes imgix(String imgix) {
        this.setImgix(imgix);
        return this;
    }

    public void setImgix(String imgix) {
        this.imgix = imgix;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public Reportes ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return this.estado;
    }

    public Reportes estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return this.pais;
    }

    public Reportes pais(String pais) {
        this.setPais(pais);
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getExtra1() {
        return this.extra1;
    }

    public Reportes extra1(String extra1) {
        this.setExtra1(extra1);
        return this;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return this.extra2;
    }

    public Reportes extra2(String extra2) {
        this.setExtra2(extra2);
        return this;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return this.extra3;
    }

    public Reportes extra3(String extra3) {
        this.setExtra3(extra3);
        return this;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }

    public String getExtra4() {
        return this.extra4;
    }

    public Reportes extra4(String extra4) {
        this.setExtra4(extra4);
        return this;
    }

    public void setExtra4(String extra4) {
        this.extra4 = extra4;
    }

    public String getExtra5() {
        return this.extra5;
    }

    public Reportes extra5(String extra5) {
        this.setExtra5(extra5);
        return this;
    }

    public void setExtra5(String extra5) {
        this.extra5 = extra5;
    }

    public String getExtra6() {
        return this.extra6;
    }

    public Reportes extra6(String extra6) {
        this.setExtra6(extra6);
        return this;
    }

    public void setExtra6(String extra6) {
        this.extra6 = extra6;
    }

    public String getExtra7() {
        return this.extra7;
    }

    public Reportes extra7(String extra7) {
        this.setExtra7(extra7);
        return this;
    }

    public void setExtra7(String extra7) {
        this.extra7 = extra7;
    }

    public String getExtra8() {
        return this.extra8;
    }

    public Reportes extra8(String extra8) {
        this.setExtra8(extra8);
        return this;
    }

    public void setExtra8(String extra8) {
        this.extra8 = extra8;
    }

    public String getExtra9() {
        return this.extra9;
    }

    public Reportes extra9(String extra9) {
        this.setExtra9(extra9);
        return this;
    }

    public void setExtra9(String extra9) {
        this.extra9 = extra9;
    }

    public String getExtra10() {
        return this.extra10;
    }

    public Reportes extra10(String extra10) {
        this.setExtra10(extra10);
        return this;
    }

    public void setExtra10(String extra10) {
        this.extra10 = extra10;
    }

    public Informacion getInformacion() {
        return this.informacion;
    }

    public void setInformacion(Informacion informacion) {
        this.informacion = informacion;
    }

    public Reportes informacion(Informacion informacion) {
        this.setInformacion(informacion);
        return this;
    }

    public Set<Categorys> getCategorys() {
        return this.categorys;
    }

    public void setCategorys(Set<Categorys> categorys) {
        if (this.categorys != null) {
            this.categorys.forEach(i -> i.removeReportes(this));
        }
        if (categorys != null) {
            categorys.forEach(i -> i.addReportes(this));
        }
        this.categorys = categorys;
    }

    public Reportes categorys(Set<Categorys> categorys) {
        this.setCategorys(categorys);
        return this;
    }

    public Reportes addCategorys(Categorys categorys) {
        this.categorys.add(categorys);
        categorys.getReportes().add(this);
        return this;
    }

    public Reportes removeCategorys(Categorys categorys) {
        this.categorys.remove(categorys);
        categorys.getReportes().remove(this);
        return this;
    }

    public Set<Comentarios> getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(Set<Comentarios> comentarios) {
        if (this.comentarios != null) {
            this.comentarios.forEach(i -> i.removeReportes(this));
        }
        if (comentarios != null) {
            comentarios.forEach(i -> i.addReportes(this));
        }
        this.comentarios = comentarios;
    }

    public Reportes comentarios(Set<Comentarios> comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public Reportes addComentarios(Comentarios comentarios) {
        this.comentarios.add(comentarios);
        comentarios.getReportes().add(this);
        return this;
    }

    public Reportes removeComentarios(Comentarios comentarios) {
        this.comentarios.remove(comentarios);
        comentarios.getReportes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reportes)) {
            return false;
        }
        return id != null && id.equals(((Reportes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reportes{" +
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
            "}";
    }
}
