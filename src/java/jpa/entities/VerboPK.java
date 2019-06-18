/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author andre
 */
@Embeddable
public class VerboPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "idVerbo")
    private int idVerbo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nome")
    private String nome;

    public VerboPK() {
    }

    public VerboPK(int idVerbo, String nome) {
        this.idVerbo = idVerbo;
        this.nome = nome;
    }

    public int getIdVerbo() {
        return idVerbo;
    }

    public void setIdVerbo(int idVerbo) {
        this.idVerbo = idVerbo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idVerbo;
        hash += (nome != null ? nome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VerboPK)) {
            return false;
        }
        VerboPK other = (VerboPK) object;
        if (this.idVerbo != other.idVerbo) {
            return false;
        }
        if ((this.nome == null && other.nome != null) || (this.nome != null && !this.nome.equals(other.nome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
