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
import jpa.entities.Empresa;
import jpa.entities.Processo;

/**
 *
 * @author paulosrh
 */
@Stateless
public class ProcessoFacade extends AbstractFacade<Processo> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProcessoFacade() {
        super(Processo.class);
    }

    // start of our code
    public void destroyProcesso(Processo processo) {
        em.createNamedQuery("Atividade.destroyAssociatedProcesso").setParameter("processo", processo);
    }

    public List getAssociated(Processo p) {
        return em.createNamedQuery("Processo.associatedAtividade").setParameter("processo", p).getResultList();
    }

    public List alreadyExists(String n) {
        return em.createNamedQuery("Processo.alreadyExists").setParameter("nome", n).getResultList();
    }

    public int countRepeated(String n) {
        return alreadyExists(n).size();
    }
    
    public List findByName(Empresa empresa, String nome){
        return em.createNamedQuery("Processo.byname").setParameter("empresa", empresa).setParameter("nome", nome).getResultList();
    }
    
    public List findAllemp(Empresa empresa){
        return em.createNamedQuery("Processo.findAllemp").setParameter("empresa", empresa).getResultList();
    }

}
