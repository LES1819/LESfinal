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
public class PerfilhasPermissoesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Perfil_id")
    private int perfilid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Permissoes_idPermissoes")
    private int permissoesidPermissoes;

    public PerfilhasPermissoesPK() {
    }

    public PerfilhasPermissoesPK(int perfilid, int permissoesidPermissoes) {
        this.perfilid = perfilid;
        this.permissoesidPermissoes = permissoesidPermissoes;
    }

    public int getPerfilid() {
        return perfilid;
    }

    public void setPerfilid(int perfilid) {
        this.perfilid = perfilid;
    }

    public int getPermissoesidPermissoes() {
        return permissoesidPermissoes;
    }

    public void setPermissoesidPermissoes(int permissoesidPermissoes) {
        this.permissoesidPermissoes = permissoesidPermissoes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) perfilid;
        hash += (int) permissoesidPermissoes;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilhasPermissoesPK)) {
            return false;
        }
        PerfilhasPermissoesPK other = (PerfilhasPermissoesPK) object;
        if (this.perfilid != other.perfilid) {
            return false;
        }
        if (this.permissoesidPermissoes != other.permissoesidPermissoes) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.PerfilhasPermissoesPK[ perfilid=" + perfilid + ", permissoesidPermissoes=" + permissoesidPermissoes + " ]";
    }

}
