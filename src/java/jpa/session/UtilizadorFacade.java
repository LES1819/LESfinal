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
import jpa.entities.Utilizador;

/**
 *
 * @author andre
 */
@Stateless
public class UtilizadorFacade extends AbstractFacade<Utilizador> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilizadorFacade() {
        super(Utilizador.class);
    }
    
    public List alreadyExistsnome(String n) {
        return em.createNamedQuery("Utilizador.findByNome").setParameter("nome", n).getResultList();
    }
    
    public List alreadyExistsemail(String n) {
        return em.createNamedQuery("Utilizador.findByEmail").setParameter("email", n).getResultList();
    }
    
    public int countRepeatednome(String n){
        return alreadyExistsnome(n).size();
    }
    
    public int countRepeatedemail(String n){
        return alreadyExistsemail(n).size();
    }

}
