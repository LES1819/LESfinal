package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Agrupamento;
import jpa.entities.AgrupamentohasPadraoPK;
import jpa.entities.Padrao;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T17:52:35")
@StaticMetamodel(AgrupamentohasPadrao.class)
public class AgrupamentohasPadrao_ { 

    public static volatile SingularAttribute<AgrupamentohasPadrao, Agrupamento> agrupamento;
    public static volatile SingularAttribute<AgrupamentohasPadrao, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<AgrupamentohasPadrao, AgrupamentohasPadraoPK> agrupamentohasPadraoPK;
    public static volatile SingularAttribute<AgrupamentohasPadrao, Integer> idAssociacao;
    public static volatile SingularAttribute<AgrupamentohasPadrao, Padrao> padrao;
    public static volatile SingularAttribute<AgrupamentohasPadrao, Date> dataCriacao;

}