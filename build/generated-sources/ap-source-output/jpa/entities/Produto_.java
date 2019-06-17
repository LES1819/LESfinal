package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.ProdutohasAtividade;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T21:53:05")
@StaticMetamodel(Produto.class)
public class Produto_ { 

    public static volatile SingularAttribute<Produto, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<Produto, String> tipo;
    public static volatile SingularAttribute<Produto, Integer> idProduto;
    public static volatile CollectionAttribute<Produto, ProdutohasAtividade> produtohasAtividadeCollection;
    public static volatile SingularAttribute<Produto, String> nome;
    public static volatile SingularAttribute<Produto, Date> dataCriacao;
    public static volatile SingularAttribute<Produto, String> descricao;

}