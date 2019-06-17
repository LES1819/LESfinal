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
import jpa.entities.Produto;

/**
 *
 * @author tiago
 */
@Stateless
public class ProdutoFacade extends AbstractFacade<Produto> {

    @PersistenceContext(unitName = "GPDPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProdutoFacade() {
        super(Produto.class);
    }

    //
    // start of our code
    public List getNotAssociated(Atividade a) {
        return em.createNamedQuery("Produto.notAssociated").setParameter("atividade", a).getResultList();
    }

    public int countNotAssociate(Atividade a) {
        return getNotAssociated(a).size();
    }

    public void destroyProduto(Produto produto) {
        em.createNamedQuery("Produto.destroyAssociations").setParameter("produto", produto).executeUpdate();
    }

    public List alreadyExists(String n) {
        return em.createNamedQuery("Produto.alreadyExists").setParameter("nome", n).getResultList();
    }

    public int countRepeated(String n) {
        return alreadyExists(n).size();
    }

    /**
     * Returns the message to delete a Product; If it has been associated with
     * at least one Activity then return the appropriate message. Else return
     * the message to indicate no associations to Activities.
     *
     * @param product
     * @return The message to display in the modal.
     */
    public String getDeleteProductMessage(Produto product) {
        // se tiver associações dizer que tem associaçoes. else delete normal
        if (!em.createNamedQuery("Produto.getAssociations").setParameter("product", product).getResultList().isEmpty()) {
            return "Atenção! O produto " + product.getNome() + " está associado a atividades, deseja continuar?";
        }
        return "Irá apagar o produto " + product.getNome() + ".";
    }

          public List findByName(Empresa empresa, String nome){
        return em.createNamedQuery("Produto.byname").setParameter("empresa", empresa).setParameter("nome", nome).getResultList();
    }
          
     public List findAllemp(Empresa empresa){
        return em.createNamedQuery("Produto.findAllemp").setParameter("empresa", empresa).getResultList();
    }
     
         public List getNotAssociatedemp(Atividade a, Empresa empresa) {
        return em.createNamedQuery("Produto.notAssociatedemp").setParameter("atividade", a).setParameter("empresa", empresa).getResultList();
    }
}
