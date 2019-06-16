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

/**
 *
 * @author andre
 */
@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
    public List alreadyExistsnome(String n) {
        return em.createNamedQuery("Empresa.findByNome").setParameter("nome", n).getResultList();
    }
    
    public List alreadyExistsnif(String n) {
        return em.createNamedQuery("Empresa.findByNif").setParameter("nif", n).getResultList();
    }
    
    public int countRepeatednome(String n) {
        return alreadyExistsnome(n).size();
    }
    
    public int countRepeatednif(String n) {
        return alreadyExistsnif(n).size();
    }

}
