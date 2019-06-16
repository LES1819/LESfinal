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
@Table(name = "Utilizador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Utilizador.findAll", query = "SELECT u FROM Utilizador u"),
    @NamedQuery(name = "Utilizador.findByIdUtilizador", query = "SELECT u FROM Utilizador u WHERE u.idUtilizador = :idUtilizador"),
    @NamedQuery(name = "Utilizador.findByNome", query = "SELECT u FROM Utilizador u WHERE u.nome = :nome"),
    @NamedQuery(name = "Utilizador.findByPassword", query = "SELECT u FROM Utilizador u WHERE u.password = :password"),
    @NamedQuery(name = "Utilizador.findByEmail", query = "SELECT u FROM Utilizador u WHERE u.email = :email"),
    @NamedQuery(name = "Utilizador.findByNomeCompleto", query = "SELECT u FROM Utilizador u WHERE u.nomeCompleto = :nomeCompleto"),
    @NamedQuery(name = "Utilizador.findByDataCriacao", query = "SELECT u FROM Utilizador u WHERE u.dataCriacao = :dataCriacao")})
public class Utilizador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUtilizador")
    private Integer idUtilizador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "email")
    private String email;
    @Size(max = 45)
    @Column(name = "nomeCompleto")
    private String nomeCompleto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dataCriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<AtividadehasPadrao> atividadehasPadraoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Padrao> padraoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Agrupamento> agrupamentoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Frase> fraseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<AgrupamentohasFrase> agrupamentohasFraseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<ProdutohasAtividade> produtohasAtividadeCollection;
    @JoinColumn(name = "Empresa_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresa empresaid;
    @JoinColumn(name = "Perfil_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Perfil perfilid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Atividade> atividadeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Processo> processoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Papel> papelCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<PerfilhasPermissoes> perfilhasPermissoesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<PapelhasAtividade> papelhasAtividadeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilizadoridUtilizador")
    private Collection<Produto> produtoCollection;

    public Utilizador() {
    }

    public Utilizador(Integer idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public Utilizador(Integer idUtilizador, String nome, String password, String email, Date dataCriacao) {
        this.idUtilizador = idUtilizador;
        this.nome = nome;
        this.password = password;
        this.email = email;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(Integer idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
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

    @XmlTransient
    public Collection<Padrao> getPadraoCollection() {
        return padraoCollection;
    }

    public void setPadraoCollection(Collection<Padrao> padraoCollection) {
        this.padraoCollection = padraoCollection;
    }

    @XmlTransient
    public Collection<Agrupamento> getAgrupamentoCollection() {
        return agrupamentoCollection;
    }

    public void setAgrupamentoCollection(Collection<Agrupamento> agrupamentoCollection) {
        this.agrupamentoCollection = agrupamentoCollection;
    }

    @XmlTransient
    public Collection<Frase> getFraseCollection() {
        return fraseCollection;
    }

    public void setFraseCollection(Collection<Frase> fraseCollection) {
        this.fraseCollection = fraseCollection;
    }

    @XmlTransient
    public Collection<AgrupamentohasFrase> getAgrupamentohasFraseCollection() {
        return agrupamentohasFraseCollection;
    }

    public void setAgrupamentohasFraseCollection(Collection<AgrupamentohasFrase> agrupamentohasFraseCollection) {
        this.agrupamentohasFraseCollection = agrupamentohasFraseCollection;
    }

    @XmlTransient
    public Collection<ProdutohasAtividade> getProdutohasAtividadeCollection() {
        return produtohasAtividadeCollection;
    }

    public void setProdutohasAtividadeCollection(Collection<ProdutohasAtividade> produtohasAtividadeCollection) {
        this.produtohasAtividadeCollection = produtohasAtividadeCollection;
    }

    public Empresa getEmpresaid() {
        return empresaid;
    }

    public void setEmpresaid(Empresa empresaid) {
        this.empresaid = empresaid;
    }

    public Perfil getPerfilid() {
        return perfilid;
    }

    public void setPerfilid(Perfil perfilid) {
        this.perfilid = perfilid;
    }

    @XmlTransient
    public Collection<Atividade> getAtividadeCollection() {
        return atividadeCollection;
    }

    public void setAtividadeCollection(Collection<Atividade> atividadeCollection) {
        this.atividadeCollection = atividadeCollection;
    }

    @XmlTransient
    public Collection<Processo> getProcessoCollection() {
        return processoCollection;
    }

    public void setProcessoCollection(Collection<Processo> processoCollection) {
        this.processoCollection = processoCollection;
    }

    @XmlTransient
    public Collection<Papel> getPapelCollection() {
        return papelCollection;
    }

    public void setPapelCollection(Collection<Papel> papelCollection) {
        this.papelCollection = papelCollection;
    }

    @XmlTransient
    public Collection<PerfilhasPermissoes> getPerfilhasPermissoesCollection() {
        return perfilhasPermissoesCollection;
    }

    public void setPerfilhasPermissoesCollection(Collection<PerfilhasPermissoes> perfilhasPermissoesCollection) {
        this.perfilhasPermissoesCollection = perfilhasPermissoesCollection;
    }

    @XmlTransient
    public Collection<AgrupamentohasPadrao> getAgrupamentohasPadraoCollection() {
        return agrupamentohasPadraoCollection;
    }

    public void setAgrupamentohasPadraoCollection(Collection<AgrupamentohasPadrao> agrupamentohasPadraoCollection) {
        this.agrupamentohasPadraoCollection = agrupamentohasPadraoCollection;
    }

    @XmlTransient
    public Collection<PapelhasAtividade> getPapelhasAtividadeCollection() {
        return papelhasAtividadeCollection;
    }

    public void setPapelhasAtividadeCollection(Collection<PapelhasAtividade> papelhasAtividadeCollection) {
        this.papelhasAtividadeCollection = papelhasAtividadeCollection;
    }

    @XmlTransient
    public Collection<Produto> getProdutoCollection() {
        return produtoCollection;
    }

    public void setProdutoCollection(Collection<Produto> produtoCollection) {
        this.produtoCollection = produtoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUtilizador != null ? idUtilizador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilizador)) {
            return false;
        }
        Utilizador other = (Utilizador) object;
        if ((this.idUtilizador == null && other.idUtilizador != null) || (this.idUtilizador != null && !this.idUtilizador.equals(other.idUtilizador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Utilizador[ idUtilizador=" + idUtilizador + " ]";
    }

}
