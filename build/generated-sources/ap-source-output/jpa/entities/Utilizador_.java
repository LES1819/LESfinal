package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Agrupamento;
import jpa.entities.AgrupamentohasFrase;
import jpa.entities.AgrupamentohasPadrao;
import jpa.entities.Atividade;
import jpa.entities.AtividadehasPadrao;
import jpa.entities.Empresa;
import jpa.entities.Frase;
import jpa.entities.Padrao;
import jpa.entities.Papel;
import jpa.entities.PapelhasAtividade;
import jpa.entities.Perfil;
import jpa.entities.PerfilhasPermissoes;
import jpa.entities.Processo;
import jpa.entities.Produto;
import jpa.entities.ProdutohasAtividade;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-17T17:52:35")
@StaticMetamodel(Utilizador.class)
public class Utilizador_ { 

    public static volatile CollectionAttribute<Utilizador, Frase> fraseCollection;
    public static volatile SingularAttribute<Utilizador, Empresa> empresaid;
    public static volatile CollectionAttribute<Utilizador, Agrupamento> agrupamentoCollection;
    public static volatile CollectionAttribute<Utilizador, ProdutohasAtividade> produtohasAtividadeCollection;
    public static volatile SingularAttribute<Utilizador, String> nome;
    public static volatile CollectionAttribute<Utilizador, AgrupamentohasPadrao> agrupamentohasPadraoCollection;
    public static volatile CollectionAttribute<Utilizador, Produto> produtoCollection;
    public static volatile SingularAttribute<Utilizador, Integer> idUtilizador;
    public static volatile SingularAttribute<Utilizador, String> password;
    public static volatile CollectionAttribute<Utilizador, PerfilhasPermissoes> perfilhasPermissoesCollection;
    public static volatile CollectionAttribute<Utilizador, Processo> processoCollection;
    public static volatile CollectionAttribute<Utilizador, AgrupamentohasFrase> agrupamentohasFraseCollection;
    public static volatile SingularAttribute<Utilizador, Perfil> perfilid;
    public static volatile CollectionAttribute<Utilizador, Papel> papelCollection;
    public static volatile CollectionAttribute<Utilizador, Atividade> atividadeCollection;
    public static volatile CollectionAttribute<Utilizador, Padrao> padraoCollection;
    public static volatile CollectionAttribute<Utilizador, PapelhasAtividade> papelhasAtividadeCollection;
    public static volatile SingularAttribute<Utilizador, Date> dataCriacao;
    public static volatile SingularAttribute<Utilizador, String> email;
    public static volatile CollectionAttribute<Utilizador, AtividadehasPadrao> atividadehasPadraoCollection;
    public static volatile SingularAttribute<Utilizador, String> nomeCompleto;

}