package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Agrupamento;
import jpa.entities.AgrupamentohasFrasePK;
import jpa.entities.Frase;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-18T14:33:24")
@StaticMetamodel(AgrupamentohasFrase.class)
public class AgrupamentohasFrase_ { 

    public static volatile SingularAttribute<AgrupamentohasFrase, Agrupamento> agrupamento;
    public static volatile SingularAttribute<AgrupamentohasFrase, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<AgrupamentohasFrase, AgrupamentohasFrasePK> agrupamentohasFrasePK;
    public static volatile SingularAttribute<AgrupamentohasFrase, Frase> frase;
    public static volatile SingularAttribute<AgrupamentohasFrase, Integer> idAssociacao;
    public static volatile SingularAttribute<AgrupamentohasFrase, Date> dataCriacao;

}