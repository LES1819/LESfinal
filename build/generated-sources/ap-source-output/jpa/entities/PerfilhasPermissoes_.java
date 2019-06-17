package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Perfil;
import jpa.entities.PerfilhasPermissoesPK;
import jpa.entities.Permissoes;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T22:13:29")
@StaticMetamodel(PerfilhasPermissoes.class)
public class PerfilhasPermissoes_ { 

    public static volatile SingularAttribute<PerfilhasPermissoes, Permissoes> permissoes;
    public static volatile SingularAttribute<PerfilhasPermissoes, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<PerfilhasPermissoes, PerfilhasPermissoesPK> perfilhasPermissoesPK;
    public static volatile SingularAttribute<PerfilhasPermissoes, Integer> idAssociacao;
    public static volatile SingularAttribute<PerfilhasPermissoes, Date> dataCriacao;
    public static volatile SingularAttribute<PerfilhasPermissoes, Perfil> perfil;

}