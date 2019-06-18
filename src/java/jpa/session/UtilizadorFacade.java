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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import jpa.entities.Utilizador;
import jsf.util.SessionUtils;

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

    public int countRepeatednome(String n) {
        return alreadyExistsnome(n).size();
    }

    public int countRepeatedemail(String n) {
        return alreadyExistsemail(n).size();
    }

    public List findUser(String n1, String n2) {
        return em.createNamedQuery("Utilizador.login").setParameter("nome", n1).setParameter("password", n2).getResultList();
    }

    public int validate(String n1, String n2) {
        return findUser(n1, n2).size();
    }

    public Integer findID(String n) {
        List<Utilizador> results = em.createNamedQuery("Utilizador.findID").setParameter("nome", n).getResultList();
        Integer s = results.get(0).getIdUtilizador();
        return s;
    }
}
