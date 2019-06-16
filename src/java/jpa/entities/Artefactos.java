/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "Artefactos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Artefactos.findAll", query = "SELECT a FROM Artefactos a"),
    @NamedQuery(name = "Artefactos.findByIdArtefactos", query = "SELECT a FROM Artefactos a WHERE a.idArtefactos = :idArtefactos"),
    @NamedQuery(name = "Artefactos.findByNome", query = "SELECT a FROM Artefactos a WHERE a.nome = :nome"),
    @NamedQuery(name = "Artefactos.findByDescricao", query = "SELECT a FROM Artefactos a WHERE a.descricao = :descricao")})
public class Artefactos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idArtefactos")
    private Integer idArtefactos;
    @Size(max = 45)
    @Column(name = "nome")
    private String nome;
    @Size(max = 500)
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(mappedBy = "artefactosidArtefactos")
    private Collection<Frase> fraseCollection;

    public Artefactos() {
    }

    public Artefactos(Integer idArtefactos) {
        this.idArtefactos = idArtefactos;
    }

    public Integer getIdArtefactos() {
        return idArtefactos;
    }

    public void setIdArtefactos(Integer idArtefactos) {
        this.idArtefactos = idArtefactos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Frase> getFraseCollection() {
        return fraseCollection;
    }

    public void setFraseCollection(Collection<Frase> fraseCollection) {
        this.fraseCollection = fraseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArtefactos != null ? idArtefactos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artefactos)) {
            return false;
        }
        Artefactos other = (Artefactos) object;
        if ((this.idArtefactos == null && other.idArtefactos != null) || (this.idArtefactos != null && !this.idArtefactos.equals(other.idArtefactos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Artefactos[ idArtefactos=" + idArtefactos + " ]";
    }

}
