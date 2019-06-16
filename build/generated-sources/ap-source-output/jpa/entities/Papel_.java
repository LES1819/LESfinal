package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.PapelhasAtividade;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-16T22:48:26")
@StaticMetamodel(Papel.class)
public class Papel_ { 

    public static volatile SingularAttribute<Papel, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<Papel, Integer> idPapel;
    public static volatile SingularAttribute<Papel, String> nome;
    public static volatile CollectionAttribute<Papel, PapelhasAtividade> papelhasAtividadeCollection;
    public static volatile SingularAttribute<Papel, Date> dataCriacao;
    public static volatile SingularAttribute<Papel, String> descricao;

}