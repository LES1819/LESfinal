package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Atividade;
import jpa.entities.Papel;
import jpa.entities.PapelhasAtividadePK;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-16T15:56:39")
@StaticMetamodel(PapelhasAtividade.class)
public class PapelhasAtividade_ { 

    public static volatile SingularAttribute<PapelhasAtividade, Atividade> atividade;
    public static volatile SingularAttribute<PapelhasAtividade, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<PapelhasAtividade, PapelhasAtividadePK> papelhasAtividadePK;
    public static volatile SingularAttribute<PapelhasAtividade, Integer> idAssociacao;
    public static volatile SingularAttribute<PapelhasAtividade, Date> dataCriacao;
    public static volatile SingularAttribute<PapelhasAtividade, Papel> papel;

}