/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "Perfil_has_Permissoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PerfilhasPermissoes.findAll", query = "SELECT p FROM PerfilhasPermissoes p"),
    @NamedQuery(name = "PerfilhasPermissoes.findByPerfilid", query = "SELECT p FROM PerfilhasPermissoes p WHERE p.perfilhasPermissoesPK.perfilid = :perfilid"),
    @NamedQuery(name = "PerfilhasPermissoes.findByPermissoesidPermissoes", query = "SELECT p FROM PerfilhasPermissoes p WHERE p.perfilhasPermissoesPK.permissoesidPermissoes = :permissoesidPermissoes"),
    @NamedQuery(name = "PerfilhasPermissoes.findByIdAssociacao", query = "SELECT p FROM PerfilhasPermissoes p WHERE p.idAssociacao = :idAssociacao"),
    @NamedQuery(name = "PerfilhasPermissoes.findByDataCriacao", query = "SELECT p FROM PerfilhasPermissoes p WHERE p.dataCriacao = :dataCriacao")})
public class PerfilhasPermissoes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PerfilhasPermissoesPK perfilhasPermissoesPK;
    @Basic(optional = false)
    @Column(name = "id_Associacao")
    private int idAssociacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @JoinColumn(name = "Perfil_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Perfil perfil;
    @JoinColumn(name = "Permissoes_idPermissoes", referencedColumnName = "idPermissoes", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permissoes permissoes;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public PerfilhasPermissoes() {
    }

    public PerfilhasPermissoes(PerfilhasPermissoesPK perfilhasPermissoesPK) {
        this.perfilhasPermissoesPK = perfilhasPermissoesPK;
    }

    public PerfilhasPermissoes(PerfilhasPermissoesPK perfilhasPermissoesPK, int idAssociacao, Date dataCriacao) {
        this.perfilhasPermissoesPK = perfilhasPermissoesPK;
        this.idAssociacao = idAssociacao;
        this.dataCriacao = dataCriacao;
    }

    public PerfilhasPermissoes(int perfilid, int permissoesidPermissoes) {
        this.perfilhasPermissoesPK = new PerfilhasPermissoesPK(perfilid, permissoesidPermissoes);
    }

    public PerfilhasPermissoesPK getPerfilhasPermissoesPK() {
        return perfilhasPermissoesPK;
    }

    public void setPerfilhasPermissoesPK(PerfilhasPermissoesPK perfilhasPermissoesPK) {
        this.perfilhasPermissoesPK = perfilhasPermissoesPK;
    }

    public int getIdAssociacao() {
        return idAssociacao;
    }

    public void setIdAssociacao(int idAssociacao) {
        this.idAssociacao = idAssociacao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Permissoes getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Permissoes permissoes) {
        this.permissoes = permissoes;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfilhasPermissoesPK != null ? perfilhasPermissoesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilhasPermissoes)) {
            return false;
        }
        PerfilhasPermissoes other = (PerfilhasPermissoes) object;
        if ((this.perfilhasPermissoesPK == null && other.perfilhasPermissoesPK != null) || (this.perfilhasPermissoesPK != null && !this.perfilhasPermissoesPK.equals(other.perfilhasPermissoesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.PerfilhasPermissoes[ perfilhasPermissoesPK=" + perfilhasPermissoesPK + " ]";
    }

}
