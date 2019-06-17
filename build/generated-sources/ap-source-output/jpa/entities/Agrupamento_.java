package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.AgrupamentohasFrase;
import jpa.entities.AgrupamentohasPadrao;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T22:13:29")
@StaticMetamodel(Agrupamento.class)
public class Agrupamento_ { 

    public static volatile SingularAttribute<Agrupamento, Utilizador> utilizadoridUtilizador;
    public static volatile CollectionAttribute<Agrupamento, AgrupamentohasFrase> agrupamentohasFraseCollection;
    public static volatile SingularAttribute<Agrupamento, String> nome;
    public static volatile CollectionAttribute<Agrupamento, AgrupamentohasPadrao> agrupamentohasPadraoCollection;
    public static volatile SingularAttribute<Agrupamento, Date> dataCriacao;
    public static volatile SingularAttribute<Agrupamento, Integer> idAgrupamento;
    public static volatile SingularAttribute<Agrupamento, String> descricao;

}