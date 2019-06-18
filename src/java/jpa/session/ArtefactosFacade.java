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
import jpa.entities.Artefactos;

/**
 *
 * @author diogo
 */
@Stateless
public class ArtefactosFacade extends AbstractFacade<Artefactos> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArtefactosFacade() {
        super(Artefactos.class);
    }
    
    public List getOriginal(){
        return em.createNamedQuery("Artefactos.findOriginal").getResultList();
    }
    
    public int countOriginal(){
        return getOriginal().size();
    }

     public void destroyArtefactos(Artefactos Artefactos){
       
       
    }
}
