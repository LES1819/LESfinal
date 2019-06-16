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
 * @author Paulo
 */
@Entity
@Table(name = "Agrupamento_has_Padrao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgrupamentohasPadrao.findAll", query = "SELECT a FROM AgrupamentohasPadrao a"),
    @NamedQuery(name = "AgrupamentohasPadrao.findByAgrupamentoidAgrupamento", query = "SELECT a FROM AgrupamentohasPadrao a WHERE a.agrupamentohasPadraoPK.agrupamentoidAgrupamento = :agrupamentoidAgrupamento"),
    @NamedQuery(name = "AgrupamentohasPadrao.findByIdPadraoIdAgrup", query = "SELECT a FROM AgrupamentohasPadrao a WHERE (a.agrupamentohasPadraoPK.agrupamentoidAgrupamento = :idAgrup AND a.agrupamentohasPadraoPK.padraoidPadrao = :idPadrao)"),
    @NamedQuery(name = "AgrupamentohasPadrao.findByPadraoidPadrao", query = "SELECT a FROM AgrupamentohasPadrao a WHERE a.agrupamentohasPadraoPK.padraoidPadrao = :padraoidPadrao"),
    @NamedQuery(name = "AgrupamentohasPadrao.associatedAgrupamentos", query = "SELECT a FROM AgrupamentohasPadrao a WHERE a.agrupamento = :agrupamento"),
    @NamedQuery(name = "AgrupamentohasPadrao.removeByIdPadraoIdAgrup", query = "SELECT p FROM AgrupamentohasPadrao p WHERE (p.agrupamentohasPadraoPK.padraoidPadrao = :idPadrao AND p.agrupamentohasPadraoPK.agrupamentoidAgrupamento = :idAgrup)"),
    @NamedQuery(name = "AgrupamentohasPadrao.findByIdAssociacao", query = "SELECT a FROM AgrupamentohasPadrao a WHERE a.idAssociacao = :idAssociacao"),
    @NamedQuery(name = "AgrupamentohasPadrao.findByDataCriacao", query = "SELECT a FROM AgrupamentohasPadrao a WHERE a.dataCriacao = :dataCriacao")})
public class AgrupamentohasPadrao implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AgrupamentohasPadraoPK agrupamentohasPadraoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_Associacao")
    private int idAssociacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @JoinColumn(name = "Agrupamento_idAgrupamento", referencedColumnName = "idAgrupamento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Agrupamento agrupamento;
    @JoinColumn(name = "Padrao_idPadrao", referencedColumnName = "idPadrao", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Padrao padrao;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public AgrupamentohasPadrao() {
    }

    public AgrupamentohasPadrao(AgrupamentohasPadraoPK agrupamentohasPadraoPK) {
        this.agrupamentohasPadraoPK = agrupamentohasPadraoPK;
    }

    public AgrupamentohasPadrao(AgrupamentohasPadraoPK agrupamentohasPadraoPK, int idAssociacao, Date dataCriacao) {
        this.agrupamentohasPadraoPK = agrupamentohasPadraoPK;
        this.idAssociacao = idAssociacao;
        this.dataCriacao = dataCriacao;
    }

    public AgrupamentohasPadrao(int agrupamentoidAgrupamento, int padraoidPadrao) {
        this.agrupamentohasPadraoPK = new AgrupamentohasPadraoPK(agrupamentoidAgrupamento, padraoidPadrao);
    }

    public AgrupamentohasPadraoPK getAgrupamentohasPadraoPK() {
        return agrupamentohasPadraoPK;
    }

    public void setAgrupamentohasPadraoPK(AgrupamentohasPadraoPK agrupamentohasPadraoPK) {
        this.agrupamentohasPadraoPK = agrupamentohasPadraoPK;
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

    public Agrupamento getAgrupamento() {
        return agrupamento;
    }

    public void setAgrupamento(Agrupamento agrupamento) {
        this.agrupamento = agrupamento;
    }

    public Padrao getPadrao() {
        return padrao;
    }

    public void setPadrao(Padrao padrao) {
        this.padrao = padrao;
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
        hash += (agrupamentohasPadraoPK != null ? agrupamentohasPadraoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgrupamentohasPadrao)) {
            return false;
        }
        AgrupamentohasPadrao other = (AgrupamentohasPadrao) object;
        if ((this.agrupamentohasPadraoPK == null && other.agrupamentohasPadraoPK != null) || (this.agrupamentohasPadraoPK != null && !this.agrupamentohasPadraoPK.equals(other.agrupamentohasPadraoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AgrupamentohasPadrao[ agrupamentohasPadraoPK=" + agrupamentohasPadraoPK + " ]";
    }
    
}








