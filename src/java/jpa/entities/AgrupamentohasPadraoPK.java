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
public class AgrupamentohasPadraoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Agrupamento_idAgrupamento")
    private int agrupamentoidAgrupamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Padrao_idPadrao")
    private int padraoidPadrao;

    public AgrupamentohasPadraoPK() {
    }

    public AgrupamentohasPadraoPK(int agrupamentoidAgrupamento, int padraoidPadrao) {
        this.agrupamentoidAgrupamento = agrupamentoidAgrupamento;
        this.padraoidPadrao = padraoidPadrao;
    }

    public int getAgrupamentoidAgrupamento() {
        return agrupamentoidAgrupamento;
    }

    public void setAgrupamentoidAgrupamento(int agrupamentoidAgrupamento) {
        this.agrupamentoidAgrupamento = agrupamentoidAgrupamento;
    }

    public int getPadraoidPadrao() {
        return padraoidPadrao;
    }

    public void setPadraoidPadrao(int padraoidPadrao) {
        this.padraoidPadrao = padraoidPadrao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) agrupamentoidAgrupamento;
        hash += (int) padraoidPadrao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgrupamentohasPadraoPK)) {
            return false;
        }
        AgrupamentohasPadraoPK other = (AgrupamentohasPadraoPK) object;
        if (this.agrupamentoidAgrupamento != other.agrupamentoidAgrupamento) {
            return false;
        }
        if (this.padraoidPadrao != other.padraoidPadrao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AgrupamentohasPadraoPK[ agrupamentoidAgrupamento=" + agrupamentoidAgrupamento + ", padraoidPadrao=" + padraoidPadrao + " ]";
    }

}
