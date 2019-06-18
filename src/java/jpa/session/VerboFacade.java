/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.Verbo;

/**
 *
 * @author andre
 */
@Stateless
public class VerboFacade extends AbstractFacade<Verbo> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VerboFacade() {
        super(Verbo.class);
    }
    
    /**
     * obter a lista de frases com aquele verbo
     * @param id id do verbo
     * @return lista de frases com aquele verbo
     */
    public List pesquisaVerbos(int id) {
        return em.createNamedQuery("Frase.findByVerboId").setParameter("verbo_id", id).getResultList();
    }

    public void removeId(int id) {
        em.createNamedQuery("Verbo.findByVerboIdRemove").setParameter("verbo_id", id).getResultList();
    }

}
