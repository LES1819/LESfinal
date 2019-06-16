package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Atividade;
import jpa.entities.AtividadehasPadraoPK;
import jpa.entities.Padrao;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-16T15:56:39")
@StaticMetamodel(AtividadehasPadrao.class)
public class AtividadehasPadrao_ { 

    public static volatile SingularAttribute<AtividadehasPadrao, Atividade> atividade;
    public static volatile SingularAttribute<AtividadehasPadrao, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<AtividadehasPadrao, AtividadehasPadraoPK> atividadehasPadraoPK;
    public static volatile SingularAttribute<AtividadehasPadrao, Integer> idAssociacao;
    public static volatile SingularAttribute<AtividadehasPadrao, Padrao> padrao;
    public static volatile SingularAttribute<AtividadehasPadrao, Date> dataCriacao;

}