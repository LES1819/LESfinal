package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Atividade;
import jpa.entities.AtividadehasPadrao;
import jpa.entities.PapelhasAtividade;
import jpa.entities.Processo;
import jpa.entities.ProdutohasAtividade;
import jpa.entities.Utilizador;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-18T14:33:24")
@StaticMetamodel(Atividade.class)
public class Atividade_ { 

    public static volatile SingularAttribute<Atividade, Utilizador> utilizadoridUtilizador;
    public static volatile SingularAttribute<Atividade, Atividade> idAtividadeOriginal;
    public static volatile SingularAttribute<Atividade, Integer> idAtividades;
    public static volatile CollectionAttribute<Atividade, ProdutohasAtividade> produtohasAtividadeCollection;
    public static volatile SingularAttribute<Atividade, String> nome;
    public static volatile CollectionAttribute<Atividade, Atividade> atividadeCollection;
    public static volatile SingularAttribute<Atividade, Processo> processoidProcesso;
    public static volatile CollectionAttribute<Atividade, PapelhasAtividade> papelhasAtividadeCollection;
    public static volatile SingularAttribute<Atividade, Date> dataCriacao;
    public static volatile CollectionAttribute<Atividade, AtividadehasPadrao> atividadehasPadraoCollection;
    public static volatile SingularAttribute<Atividade, String> descricao;

}