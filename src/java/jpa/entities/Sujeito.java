/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * @author andre
 */
@Entity
@Table(name = "Sujeito")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sujeito.findAll", query = "SELECT s FROM Sujeito s"),
    @NamedQuery(name = "Sujeito.findByNome", query = "SELECT s FROM Sujeito s WHERE s.nome = :nome"),
    @NamedQuery(name = "Sujeito.findByIdSujeito", query = "SELECT s FROM Sujeito s WHERE s.idSujeito = :idSujeito")})
public class Sujeito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nome")
    private String nome;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSujeito")
    private Integer idSujeito;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sujeitoidSujeito")
    private Collection<Frase> fraseCollection;

    public Sujeito() {
    }

    public Sujeito(Integer idSujeito) {
        this.idSujeito = idSujeito;
    }

    public Sujeito(Integer idSujeito, String nome) {
        this.idSujeito = idSujeito;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdSujeito() {
        return idSujeito;
    }

    public void setIdSujeito(Integer idSujeito) {
        this.idSujeito = idSujeito;
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
        hash += (idSujeito != null ? idSujeito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sujeito)) {
            return false;
        }
        Sujeito other = (Sujeito) object;
        if ((this.idSujeito == null && other.idSujeito != null) || (this.idSujeito != null && !this.idSujeito.equals(other.idSujeito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Sujeito[ idSujeito=" + idSujeito + " ]";
    }

}
