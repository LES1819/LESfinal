package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Atividade;
import jpa.entities.Produto;
import jpa.entities.ProdutohasAtividadePK;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T22:13:29")
@StaticMetamodel(ProdutohasAtividade.class)
public class ProdutohasAtividade_ { 

    public static volatile SingularAttribute<ProdutohasAtividade, ProdutohasAtividadePK> produtohasAtividadePK;
    public static volatile SingularAttribute<ProdutohasAtividade, Atividade> atividade;
    public static volatile SingularAttribute<ProdutohasAtividade, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<ProdutohasAtividade, Produto> produto;
    public static volatile SingularAttribute<ProdutohasAtividade, Integer> idAssociacao;
    public static volatile SingularAttribute<ProdutohasAtividade, Date> dataCriacao;

}