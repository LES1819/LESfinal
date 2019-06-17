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
import jpa.entities.Padrao;

/**
 *
 * @author andre
 */
@Stateless
public class PadraoFacade extends AbstractFacade<Padrao> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PadraoFacade() {
        super(Padrao.class);
    }

    // start of our code
    public List getNotAssociated(Atividade a) {
        return em.createNamedQuery("Padrao.notAssociated").setParameter("atividade", a).getResultList();
    }

    public int countNotAssociate(Atividade a) {
        return getNotAssociated(a).size();
    }
    
    // grupo 6
     public void destroyPadrao(Padrao padrao){
       destroyAssociates(padrao);
    }
    
    public void destroyAssociates(Padrao padrao){
        em.createNamedQuery("Padrao.destroyAssociatedAgrups").setParameter("padrao", padrao);
        em.createNamedQuery("Padrao.destroyAssociatedActivs").setParameter("padrao", padrao);
    }
    
    public List pesquisaAgrupamento(int id) {
        return em.createNamedQuery("AgrupamentohasPadrao.findByPadraoidPadrao").setParameter("padraoidPadrao", id).getResultList();
    }
    
    public List pesquisaAtividade(int id) {
        return em.createNamedQuery("AtividadehasPadrao.findByPadraoidPadrao").setParameter("padraoidPadrao", id).getResultList();
    }
    
    public List getNotAssociatedemp(Atividade a, Empresa empresa) {
        return em.createNamedQuery("Padrao.notAssociatedemp").setParameter("atividade", a).setParameter("empresa", empresa).getResultList();
    }
}
