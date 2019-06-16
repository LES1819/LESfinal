/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.PerfilhasPermissoes;

/**
 *
 * @author andre
 */
@Stateless
public class PerfilhasPermissoesFacade extends AbstractFacade<PerfilhasPermissoes> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PerfilhasPermissoesFacade() {
        super(PerfilhasPermissoes.class);
    }

}
