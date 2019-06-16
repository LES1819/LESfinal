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
public class AtividadehasPadraoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Atividade_idAtividades")
    private int atividadeidAtividades;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Padrao_idPadrao")
    private int padraoidPadrao;

    public AtividadehasPadraoPK() {
    }

    public AtividadehasPadraoPK(int atividadeidAtividades, int padraoidPadrao) {
        this.atividadeidAtividades = atividadeidAtividades;
        this.padraoidPadrao = padraoidPadrao;
    }

    public int getAtividadeidAtividades() {
        return atividadeidAtividades;
    }

    public void setAtividadeidAtividades(int atividadeidAtividades) {
        this.atividadeidAtividades = atividadeidAtividades;
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
        hash += (int) atividadeidAtividades;
        hash += (int) padraoidPadrao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtividadehasPadraoPK)) {
            return false;
        }
        AtividadehasPadraoPK other = (AtividadehasPadraoPK) object;
        if (this.atividadeidAtividades != other.atividadeidAtividades) {
            return false;
        }
        if (this.padraoidPadrao != other.padraoidPadrao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.AtividadehasPadraoPK[ atividadeidAtividades=" + atividadeidAtividades + ", padraoidPadrao=" + padraoidPadrao + " ]";
    }

}
