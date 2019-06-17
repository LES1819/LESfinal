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
import jpa.entities.Papel;

/**
 *
 * @author samuel
 */
@Stateless
public class PapelFacade extends AbstractFacade<Papel> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PapelFacade() {
        super(Papel.class);
    }

    // start of our code
    public void destroyPapel(Papel papel) {
        em.createNamedQuery("Papel.destroyAssociatedAtividade").setParameter("papel", papel).executeUpdate();
    }

    public List getNotAssociated(Atividade a) {
        return em.createNamedQuery("Papel.notAssociated").setParameter("atividade", a).getResultList();
    }

    public int countNotAssociate(Atividade a) {
        return getNotAssociated(a).size();
    }

    public List alreadyExists(String n) {
        return em.createNamedQuery("Papel.alreadyExists").setParameter("nome", n).getResultList();
    }

    public int countRepeated(String n) {
        return alreadyExists(n).size();
    }
    
       public List findByName(Empresa empresa, String nome){
        return em.createNamedQuery("Papel.byname").setParameter("empresa", empresa).setParameter("nome", nome).getResultList();
    }

       public List findAllemp(Empresa empresa){
        return em.createNamedQuery("Papel.findAllemp").setParameter("empresa", empresa).getResultList();
    }
       
      public List getNotAssociatedemp(Atividade a, Empresa empresa) {
        return em.createNamedQuery("Papel.notAssociatedemp").setParameter("atividade", a).setParameter("empresa", empresa).getResultList();
    }
}
