package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.PerfilhasPermissoes;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-16T15:56:39")
@StaticMetamodel(Perfil.class)
public class Perfil_ { 

    public static volatile CollectionAttribute<Perfil, PerfilhasPermissoes> perfilhasPermissoesCollection;
    public static volatile SingularAttribute<Perfil, String> nome;
    public static volatile CollectionAttribute<Perfil, Utilizador> utilizadorCollection;
    public static volatile SingularAttribute<Perfil, Integer> id;
    public static volatile SingularAttribute<Perfil, Date> dataCriacao;

}