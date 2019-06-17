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
import javax.persistence.Lob;
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
@Table(name = "Padrao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Padrao.findAll", query = "SELECT p FROM Padrao p"),
    @NamedQuery(name = "Padrao.findByIdPadrao", query = "SELECT p FROM Padrao p WHERE p.idPadrao = :idPadrao"),
    @NamedQuery(name = "Padrao.notAssociated", query = "SELECT p FROM Padrao p WHERE p NOT IN(SELECT a.padrao FROM AtividadehasPadrao a WHERE a.atividade =:atividade)"),
    @NamedQuery(name = "Padrao.notAssociatedemp", query = "SELECT p FROM Padrao p WHERE p NOT IN(SELECT a.padrao FROM AtividadehasPadrao a WHERE a.atividade =:atividade) AND p.utilizadoridUtilizador IN (SELECT u FROM Utilizador u WHERE u.empresaid = :empresa)"),
    @NamedQuery(name = "Padrao.findByNome", query = "SELECT p FROM Padrao p WHERE p.nome = :nome"),
     @NamedQuery(name = "Padrao.destroyAssociatedAgrups", query ="DELETE FROM AgrupamentohasPadrao p WHERE p.padrao = :padrao"),
    @NamedQuery(name = "Padrao.destroyAssociatedActivs", query ="DELETE FROM AtividadehasPadrao p WHERE p.padrao = :padrao"),
    @NamedQuery(name = "Padrao.findByDescricao", query = "SELECT p FROM Padrao p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "Padrao.findByDataCriacao", query = "SELECT p FROM Padrao p WHERE p.dataCriacao = :dataCriacao")})
public class Padrao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPadrao")
    private Integer idPadrao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nome")
    private String nome;
    @Lob
    @Column(name = "Img")
    private String img;
    @Size(max = 500)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padrao")
    private Collection<AtividadehasPadrao> atividadehasPadraoCollection;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padrao")
    private Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection;

    public Padrao() {
    }

    public Padrao(Integer idPadrao) {
        this.idPadrao = idPadrao;
    }

    public Padrao(Integer idPadrao, String nome, Date dataCriacao) {
        this.idPadrao = idPadrao;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdPadrao() {
        return idPadrao;
    }

    public void setIdPadrao(Integer idPadrao) {
        this.idPadrao = idPadrao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @XmlTransient
    public Collection<AtividadehasPadrao> getAtividadehasPadraoCollection() {
        return atividadehasPadraoCollection;
    }

    public void setAtividadehasPadraoCollection(Collection<AtividadehasPadrao> atividadehasPadraoCollection) {
        this.atividadehasPadraoCollection = atividadehasPadraoCollection;
    }

    public Utilizador getUtilizadoridUtilizador() {
        return utilizadoridUtilizador;
    }

    public void setUtilizadoridUtilizador(Utilizador utilizadoridUtilizador) {
        this.utilizadoridUtilizador = utilizadoridUtilizador;
    }

    @XmlTransient
    public Collection<AgrupamentohasPadrao> getAgrupamentohasPadraoCollection() {
        return agrupamentohasPadraoCollection;
    }

    public void setAgrupamentohasPadraoCollection(Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection) {
        this.agrupamentohasPadraoCollection = agrupamentohasPadraoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPadrao != null ? idPadrao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Padrao)) {
            return false;
        }
        Padrao other = (Padrao) object;
        if ((this.idPadrao == null && other.idPadrao != null) || (this.idPadrao != null && !this.idPadrao.equals(other.idPadrao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Padrao[ idPadrao=" + idPadrao + " ]";
    }

}
