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
import jpa.entities.Atividade;
import jpa.entities.Empresa;

/**
 *
 * @author andre
 */
@Stateless
public class AtividadeFacade extends AbstractFacade<Atividade> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AtividadeFacade() {
        super(Atividade.class);
    }

    //start of our code
    public List getOriginal(int pid) {
        return em.createNamedQuery("Atividade.findOriginal").setParameter("param", pid).getResultList();
    }

    public int countOriginal(int pid) {
        return getOriginal(pid).size();
    }

    public int countOriginalAtividades() {
        return getOriginalAtividades().size();
    }

    public List getOriginalAtividades() {
        return em.createNamedQuery("Atividade.getOriginal").getResultList();
    }

    public void destroyAtividade(Atividade atividade) {
        em.createNamedQuery("Atividade.setIdOriginalNull").setParameter("atividade", atividade).executeUpdate();
    }

    public List alreadyExists(String n) {
        return em.createNamedQuery("Atividade.alreadyExists").setParameter("nome", n).getResultList();
    }

    public int countRepeated(String n) {
        return alreadyExists(n).size();
    }
    
    public List findByIdPadraoIdAtiv(int padr, int ativ){
        return em.createNamedQuery("AtividadehasPadrao.removeByIdPadraoIdAtiv").setParameter("idAtiv", ativ).setParameter("idPadrao", padr).getResultList();
    }
    
    public List findByName(Empresa empresa, String nome){
        return em.createNamedQuery("Atividade.byname").setParameter("empresa", empresa).setParameter("nome", nome).getResultList();
    }
    
    public List getOriginalAtividadesemp(Empresa empresa) {
        return em.createNamedQuery("Atividade.getOriginalemp").setParameter("empresa", empresa).getResultList();
    }
    
    public List findOriginalemp(Empresa empresa, int pid) {
        return em.createNamedQuery("Atividade.findOriginalemp").setParameter("empresa", empresa).setParameter("param", pid).getResultList();
    }
}
