package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.AgrupamentohasFrase;
import jpa.entities.Artefactos;
import jpa.entities.Sujeito;
import jpa.entities.Utilizador;
import jpa.entities.Verbo;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T21:53:05")
@StaticMetamodel(Frase.class)
public class Frase_ { 

    public static volatile SingularAttribute<Frase, String> recurso;
    public static volatile SingularAttribute<Frase, String> dataRealizacao;
    public static volatile SingularAttribute<Frase, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<Frase, Date> datCriacao;
    public static volatile SingularAttribute<Frase, Sujeito> sujeitoidSujeito;
    public static volatile CollectionAttribute<Frase, AgrupamentohasFrase> agrupamentohasFraseCollection;
    public static volatile SingularAttribute<Frase, Artefactos> artefactosidArtefactos;
    public static volatile SingularAttribute<Frase, Integer> idFrase;
    public static volatile SingularAttribute<Frase, Verbo> verbo;
    public static volatile SingularAttribute<Frase, String> destinatario;

}