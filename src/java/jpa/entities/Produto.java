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
@Table(name = "Produto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p"),
    @NamedQuery(name = "Produto.findAllemp", query = "SELECT p FROM Produto p WHERE p.utilizadoridUtilizador IN (SELECT u FROM Utilizador u WHERE u.empresaid= :empresa)"),
    @NamedQuery(name = "Produto.findByIdProduto", query = "SELECT p FROM Produto p WHERE p.idProduto = :idProduto"),
    @NamedQuery(name = "Produto.notAssociated", query = "SELECT p FROM Produto p WHERE p NOT IN(SELECT a.produto FROM ProdutohasAtividade a WHERE a.atividade = :atividade)"),
    @NamedQuery(name = "Produto.notAssociatedemp", query = "SELECT p FROM Produto p WHERE p NOT IN(SELECT a.produto FROM ProdutohasAtividade a WHERE a.atividade = :atividade) AND p.utilizadoridUtilizador IN (SELECT u FROM Utilizador u WHERE u.empresaid = :empresa)"),
    @NamedQuery(name = "Produto.alreadyExists", query = "SELECT p FROM Produto p, Empresa e WHERE p.nome = :nome AND e.id = 1"),
    @NamedQuery(name = "Produto.findByNome", query = "SELECT p FROM Produto p WHERE p.nome = :nome"),
    @NamedQuery(name = "Produto.findByDescricao", query = "SELECT p FROM Produto p WHERE p.descricao = :descricao"),
    @NamedQuery(name = "Produto.destroyAssociations", query = "DELETE FROM ProdutohasAtividade p WHERE p.produto = :produto"),
    @NamedQuery(name = "Produto.getAssociations", query = "SELECT p.produto FROM ProdutohasAtividade p WHERE p.produto =:product"),
    @NamedQuery(name = "Produto.byname", query = "SELECT p FROM Produto p WHERE p.nome= :nome AND p.utilizadoridUtilizador IN (SELECT u FROM Utilizador u WHERE u.empresaid = :empresa)"),
    @NamedQuery(name = "Produto.findByTipo", query = "SELECT p FROM Produto p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Produto.findByDataCriacao", query = "SELECT p FROM Produto p WHERE p.dataCriacao = :dataCriacao")})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idProduto")
    private Integer idProduto;
    @Column(name = "nome")
    private String nome;
    @Size(max = 500)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "Tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produto")
    private Collection<ProdutohasAtividade> produtohasAtividadeCollection;
    @JoinColumn(name = "Utilizador_idUtilizador", referencedColumnName = "idUtilizador")
    @ManyToOne(optional = false)
    private Utilizador utilizadoridUtilizador;

    public Produto() {
    }

    public Produto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Produto(Integer idProduto, String nome, String tipo, Date dataCriacao) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @XmlTransient
    public Collection<ProdutohasAtividade> getProdutohasAtividadeCollection() {
        return produtohasAtividadeCollection;
    }

    public void setProdutohasAtividadeCollection(Collection<ProdutohasAtividade> produtohasAtividadeCollection) {
        this.produtohasAtividadeCollection = produtohasAtividadeCollection;
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
        hash += (idProduto != null ? idProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.idProduto == null && other.idProduto != null) || (this.idProduto != null && !this.idProduto.equals(other.idProduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Produto[ idProduto=" + idProduto + " ]";
    }

}
