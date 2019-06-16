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
@Table(name = "Permissoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permissoes.findAll", query = "SELECT p FROM Permissoes p"),
    @NamedQuery(name = "Permissoes.findByIdPermissoes", query = "SELECT p FROM Permissoes p WHERE p.idPermissoes = :idPermissoes"),
    @NamedQuery(name = "Permissoes.findByMascara", query = "SELECT p FROM Permissoes p WHERE p.mascara = :mascara"),
    @NamedQuery(name = "Permissoes.findByTema", query = "SELECT p FROM Permissoes p WHERE p.tema = :tema"),
    @NamedQuery(name = "Permissoes.findByDataCriacao", query = "SELECT p FROM Permissoes p WHERE p.dataCriacao = :dataCriacao")})
public class Permissoes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPermissoes")
    private Integer idPermissoes;
    @Size(max = 40)
    @Column(name = "mascara")
    private String mascara;
    @Size(max = 11)
    @Column(name = "Tema")
    private String tema;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "permissoes")
    private Collection<PerfilhasPermissoes> perfilhasPermissoesCollection;

    public Permissoes() {
    }

    public Permissoes(Integer idPermissoes) {
        this.idPermissoes = idPermissoes;
    }

    public Permissoes(Integer idPermissoes, Date dataCriacao) {
        this.idPermissoes = idPermissoes;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdPermissoes() {
        return idPermissoes;
    }

    public void setIdPermissoes(Integer idPermissoes) {
        this.idPermissoes = idPermissoes;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @XmlTransient
    public Collection<PerfilhasPermissoes> getPerfilhasPermissoesCollection() {
        return perfilhasPermissoesCollection;
    }

    public void setPerfilhasPermissoesCollection(Collection<PerfilhasPermissoes> perfilhasPermissoesCollection) {
        this.perfilhasPermissoesCollection = perfilhasPermissoesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPermissoes != null ? idPermissoes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permissoes)) {
            return false;
        }
        Permissoes other = (Permissoes) object;
        if ((this.idPermissoes == null && other.idPermissoes != null) || (this.idPermissoes != null && !this.idPermissoes.equals(other.idPermissoes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Permissoes[ idPermissoes=" + idPermissoes + " ]";
    }

}
