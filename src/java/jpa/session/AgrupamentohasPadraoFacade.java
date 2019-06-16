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
import jpa.entities.AgrupamentohasPadrao;
import jpa.entities.Agrupamento;

/**
 *
 * @author Paulo
 */
@Stateless
public class AgrupamentohasPadraoFacade extends AbstractFacade<AgrupamentohasPadrao> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgrupamentohasPadraoFacade() {
        super(AgrupamentohasPadrao.class);
    }
    
    public List getAssociatedAgrupamentos(Agrupamento agrupamento){
        return em.createNamedQuery("AgrupamentohasPadrao.associatedAgrupamentos").setParameter("agrupamento", agrupamento).getResultList();
    }
    
    public int countAssociatedAgrupamentos(Agrupamento agrupamento) {
        return getAssociatedAgrupamentos(agrupamento).size();
    }
    
}






