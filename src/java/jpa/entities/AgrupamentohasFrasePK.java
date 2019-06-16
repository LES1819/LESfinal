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

/**
 *
 * @author andre
 */
@Embeddable
public class AgrupamentohasFrasePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Agrupamento_idAgrupamento")
    private int agrupamentoidAgrupamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Frase_idFrase")
    private int fraseidFrase;

    public AgrupamentohasFrasePK() {
    }

    public AgrupamentohasFrasePK(int agrupamentoidAgrupamento, int fraseidFrase) {
        this.agrupamentoidAgrupamento = agrupamentoidAgrupamento;
        this.fraseidFrase = fraseidFrase;
    }

    public int getAgrupamentoidAgrupamento() {
        return agrupamentoidAgrupamento;
    }

    public void setAgrupamentoidAgrupamento(int agrupamentoidAgrupamento) {
        this.agrupamentoidAgrupamento = agrupamentoidAgrupamento;
    }

    public int getFraseidFrase() {
        return fraseidFrase;
    }

    public void setFraseidFrase(int fraseidFrase) {
        this.fraseidFrase = fraseidFrase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) agrupamentoidAgrupamento;
        hash += (int) fraseidFrase;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgrupamentohasFrasePK)) {
            return false;
        }
        AgrupamentohasFrasePK other = (AgrupamentohasFrasePK) object;
        if (this.agrupamentoidAgrupamento != other.agrupamentoidAgrupamento) {
            return false;
        }
        if (this.fraseidFrase != other.fraseidFrase) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AgrupamentohasFrasePK[ agrupamentoidAgrupamento=" + agrupamentoidAgrupamento + ", fraseidFrase=" + fraseidFrase + " ]";
    }

}
