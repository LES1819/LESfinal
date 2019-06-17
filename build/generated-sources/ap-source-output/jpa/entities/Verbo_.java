package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Frase;
import jpa.entities.VerboPK;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T21:53:05")
@StaticMetamodel(Verbo.class)
public class Verbo_ { 

    public static volatile CollectionAttribute<Verbo, Frase> fraseCollection;
    public static volatile SingularAttribute<Verbo, String> tipo;
    public static volatile SingularAttribute<Verbo, VerboPK> verboPK;
    public static volatile SingularAttribute<Verbo, Date> dataCriacao;

}