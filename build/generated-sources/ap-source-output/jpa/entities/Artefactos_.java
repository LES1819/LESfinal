package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Frase;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-18T14:33:24")
@StaticMetamodel(Artefactos.class)
public class Artefactos_ { 

    public static volatile CollectionAttribute<Artefactos, Frase> fraseCollection;
    public static volatile SingularAttribute<Artefactos, String> nome;
    public static volatile SingularAttribute<Artefactos, Integer> idArtefactos;
    public static volatile SingularAttribute<Artefactos, String> descricao;

}