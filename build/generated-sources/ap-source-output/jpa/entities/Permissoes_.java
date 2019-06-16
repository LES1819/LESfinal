package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.PerfilhasPermissoes;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-16T15:56:39")
@StaticMetamodel(Permissoes.class)
public class Permissoes_ { 

    public static volatile CollectionAttribute<Permissoes, PerfilhasPermissoes> perfilhasPermissoesCollection;
    public static volatile SingularAttribute<Permissoes, String> tema;
    public static volatile SingularAttribute<Permissoes, Integer> idPermissoes;
    public static volatile SingularAttribute<Permissoes, String> mascara;
    public static volatile SingularAttribute<Permissoes, Date> dataCriacao;

}