package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.AgrupamentohasPadrao;
import jpa.entities.AtividadehasPadrao;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T17:52:35")
@StaticMetamodel(Padrao.class)
public class Padrao_ { 

    public static volatile SingularAttribute<Padrao, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<Padrao, String> img;
    public static volatile SingularAttribute<Padrao, String> nome;
    public static volatile CollectionAttribute<Padrao, AgrupamentohasPadrao> agrupamentohasPadraoCollection;
    public static volatile SingularAttribute<Padrao, Integer> idPadrao;
    public static volatile SingularAttribute<Padrao, Date> dataCriacao;
    public static volatile CollectionAttribute<Padrao, AtividadehasPadrao> atividadehasPadraoCollection;
    public static volatile SingularAttribute<Padrao, String> descricao;

}