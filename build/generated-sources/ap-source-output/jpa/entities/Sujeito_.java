package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Frase;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T17:52:35")
@StaticMetamodel(Sujeito.class)
public class Sujeito_ { 

    public static volatile CollectionAttribute<Sujeito, Frase> fraseCollection;
    public static volatile SingularAttribute<Sujeito, Integer> idSujeito;
    public static volatile SingularAttribute<Sujeito, String> nome;

}