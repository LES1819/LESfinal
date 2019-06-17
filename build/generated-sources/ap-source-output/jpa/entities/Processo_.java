package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Atividade;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T21:53:05")
@StaticMetamodel(Processo.class)
public class Processo_ { 

    public static volatile SingularAttribute<Processo, Integer> idProcesso;
    public static volatile SingularAttribute<Processo, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<Processo, String> nome;
    public static volatile CollectionAttribute<Processo, Atividade> atividadeCollection;
    public static volatile SingularAttribute<Processo, Date> dataCriacao;
    public static volatile SingularAttribute<Processo, String> descricao;

}