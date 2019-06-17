package jsf;

import static com.journaldev.jsf.util.SessionUtils.getUserId;
import jpa.entities.Atividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AtividadeFacade;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.AtividadehasPadrao;
import jpa.entities.AtividadehasPadraoPK;
import jpa.entities.Padrao;
import jpa.entities.PapelhasAtividade;
import jpa.entities.PapelhasAtividadePK;
import jpa.entities.Processo;
import jpa.entities.ProdutohasAtividade;
import jpa.entities.ProdutohasAtividadePK;
import jpa.entities.Utilizador;
import jpa.session.AtividadehasPadraoFacade;
import jpa.session.PapelhasAtividadeFacade;
import jpa.session.ProdutohasAtividadeFacade;
import jpa.session.UtilizadorFacade;

@Named("atividadeController")
@SessionScoped
public class AtividadeController implements Serializable {

    private Atividade current;
    private DataModel items = null;
    @EJB
    private jpa.session.AtividadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Utilizador utilizador;
    private Processo processo;

    private Map<Atividade, Boolean> selectedItems = new HashMap<Atividade, Boolean>();
    private List<Atividade> atividadesOnList;

    public AtividadeController() {
        selectedItems = new HashMap<>();
        atividadesOnList = new ArrayList<>();
    }

    public Atividade getSelected() {
        if (current == null) {
            current = new Atividade();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AtividadeFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        recreatePagination();
        return "/atividade/List";
    }

    public String prepareView() {
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareEdit() {
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String destroy() {
        current = (Atividade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("resources/Bundle").getString("AtividadeDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Atividade getAtividade(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Atividade.class)
    public static class AtividadeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AtividadeController controller = (AtividadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "atividadeController");
            return controller.getAtividade(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Atividade) {
                Atividade o = (Atividade) object;
                return getStringKey(o.getIdAtividades());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Atividade.class.getName());
            }
        }

    }

    //start of our code
    public String create() {
        recreateModel();
        recreatePagination();
        try {
            int k = getFacade().findByName(utilizadorFacade.find(Integer.parseInt(getUserId())).getEmpresaid(), current.getNome()).size();
            if (!hasSomething(current.getNome())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
            } else {
                if (k > 0) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("atividadeAlreadyExists"));
                } else {
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeCreated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeCreated2"));
                   return prepareList();
                }
            }
            return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update(int saida) {
        recreateModel();
        recreatePagination();
        if(saida==0){
            getFacade().destroyAtividade(current);
        }
        else if(saida==1) current.setIdAtividadeOriginal(null);
        try {
            String name = getFacade().find(current.getIdAtividades()).getNome();
            if (name.equals(current.getNome())) {
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeUpdated2"));
            } else {
                 int k = getFacade().findByName(utilizadorFacade.find(Integer.parseInt(getUserId())).getEmpresaid(), current.getNome()).size();
                if (!hasSomething(current.getNome())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
                    return null;
                } else {
                    if (k > 0) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("atividadeAlreadyExists"));
                        return null;
                    } else {
                        getFacade().edit(current);
                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeUpdated2"));
                    }
                }
            }
            if (saida == 0) {
                return "View";
            } else {
                return "ViewCopy";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public boolean hasSomething(String s) {
        for (int i = 0; i < s.length(); i++) {
            boolean atleastOneChar = s.matches(".*[a-zA-Z]+.*");
            if (atleastOneChar == true) {
                return true;
            }
        }
        return false;
    }

    private void prepareSelectedList() {
        atividadesOnList = new ArrayList<Atividade>();
        for (Atividade a : selectedItems.keySet()) {
            if (selectedItems.get(a) == true) {
                atividadesOnList.add(a);
            }
        }
    }

    public Map<Atividade, Boolean> getSelectedItems() {
        return selectedItems;
    }

    public String nextOriginalAtividades() {
        getOriginalAtividadesPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previousOriginalAtividades() {
        getOriginalAtividadesPagination().previousPage();
        recreateModel();
        return "List";
    }

    public DataModel getOriginalAtividadeItems() {
        recreatePagination();
        recreateModel();
        items = new ListDataModel(getFacade().getOriginalAtividades());
        return items;
    }
   

    public PaginationHelper getOriginalAtividadesPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(6) {

                @Override
                public int getItemsCount() {
                    return getFacade().countOriginalAtividades();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getOriginalAtividades());
                }

            };
        }
        return pagination;
    }

    public PaginationHelper getOriginalPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(6) {

                @Override
                public int getItemsCount() {
                    return getFacade().countOriginal(current.getProcessoidProcesso().getIdProcesso());
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getOriginal(current.getProcessoidProcesso().getIdProcesso()));
                }

            };
        }
        return pagination;
    }

    public String prepareCreate() {
        current = new Atividade();
        selectedItemIndex = -1;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        current.setDataCriacao(new Date());
        return "Create";
    }

    public void destroyAtividade(Atividade a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
        checked = false;
    }

    public String destroyActivities() {
        prepareSelectedList();
        for (int i = 0; i < atividadesOnList.size(); i++) {
            destroyAtividade(atividadesOnList.get(i));
        }
        selectedItems = new HashMap<>();
        atividadesOnList = new ArrayList<>();
        checked2 = false;
        return "List";
    }

    public String destroyAndList(int saida) {
        checkedFromView = true;
        performDestroyFull();
        recreateModel();
        recreatePagination();
        checked2 = false;
        if (saida == 0) {
            return "List";
        } else {
            return "/processo/View";
        }
    }

    private void performDestroyFull() {
        recreatePagination();
        recreateModel();
        String toSend = new String();
        toSend += "Atividade(s) ";
        String toSend2 = new String();
        toSend2 += "Foram apagadas ";
        try {
            getFacade().destroyAtividade(current);
            destroyAssociations(current);
            getFacade().remove(current);
            if (checked2 == false && checkedFromView == false) {
                if (atividadesOnList.size() < 8) {
                    for (int i = 0; i < atividadesOnList.size(); i++) {
                        toSend += atividadesOnList.get(i).getNome() + ", ";
                    }
                    toSend += " apagada(s) com sucesso.";
                    checked2 = true;
                    JsfUtil.addSuccessMessage(toSend);
                } else {
                    toSend2 += Integer.toString(atividadesOnList.size());
                    toSend2 += " atividades com sucesso.";
                    checked2 = true;
                    JsfUtil.addSuccessMessage(toSend2);
                } 
            } else if (checkedFromView == true) {
                String toSendView = "Atividade " + current.getNome() + " apagada com sucesso.";
                JsfUtil.addSuccessMessage(toSendView);
                checkedFromView = false;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void destroyAssociations(Atividade atividade) {
        papelhasatividadeFacade.destroyAsso(atividade);
        produtohasatividadeFacade.destroyAsso(atividade);
        atividadehaspadraoFacade.destroyAsso(atividade);
    }

    public DataModel getOriginalItems() {
        recreatePagination();
        recreateModel();
        items = getOriginalPagination().createPageDataModel();
        return items;
    }

    public String nextOriginal() {
        getOriginalPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previousOriginal() {
        getOriginalPagination().previousPage();
        recreateModel();
        return "List";
    }

    public String associateSelectedList() {
        prepareSelectedList();
        for (Atividade a : atividadesOnList) {
            FinalAssociate(a);
        }
        selectedItems = new HashMap<>();
        atividadesOnList = new ArrayList<>();
        recreatePagination();
        recreateModel();
        return "View";
    }

    public String prepareAssociate(Processo proc) {
        current = new Atividade();
        selectedItemIndex = -1;
        processo = proc;
        current.setProcessoidProcesso(processo);
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(1);
        current.setUtilizadoridUtilizador(utilizador);
        return "/processo/Associate";
    }

    public void FinalAssociate(Atividade a) {
        current.setIdAtividades(0);
        current.setNome(a.getNome());
        current.setDescricao(a.getDescricao());
        current.setDataCriacao(new Date(System.currentTimeMillis()));
        current.setIdAtividadeOriginal(a);/*
        current.setAtividadehasPadraoCollection(a.getAtividadehasPadraoCollection());
        current.setPapelhasAtividadeCollection(a.getPapelhasAtividadeCollection());
        current.setProdutohasAtividadeCollection(a.getProdutohasAtividadeCollection());/*
        current = a;
        current.setIdAtividades(0);
        current.setDataCriacao(new Date());
        current.setIdAtividadeOriginal(a);
        Utilizador user = new Utilizador(1);
        current.setUtilizadoridUtilizador(user);*/
        associate();
        recreatePagination();
        recreateModel();
    }

    public void associate() {
        String toSend = new String();
        toSend += "Atividades(s): ";
        String toSend2 = new String();
        toSend2 += "Foram associadas ";
        try {
            System.out.println(current.getUtilizadoridUtilizador());
            current.setUtilizadoridUtilizador(utilizadorFacade.find(1));
            getFacade().create(current);

            associatePapeisProdutos();
            if (checked == false) {
                if (atividadesOnList.size() < 8) {
                    for (int i = 0; i < atividadesOnList.size(); i++) {
                        toSend += atividadesOnList.get(i).getNome() + ", ";
                    }
                    toSend += " associada(s) com sucesso.";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend);
                } else {
                    toSend2 += Integer.toString(atividadesOnList.size());
                    toSend2 += " atividades com sucesso.";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend2);
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void associatePapeisProdutos() {
        for (Object association1 : papelhasAtividadeFacade.getAssociatedPapers(current.getIdAtividadeOriginal())) {
            PapelhasAtividade association = (PapelhasAtividade) association1;
            association.setAtividade(current);
            association.setDataCriacao(new Date());
            association.setUtilizadoridUtilizador(utilizador);
            association.setIdAssociacao(0);

            PapelhasAtividadePK pk = new PapelhasAtividadePK(association.getPapel().getIdPapel(), current.getIdAtividades());
            association.setPapelhasAtividadePK(pk);

            papelhasAtividadeFacade.create(association);
        }

        for (Object association1 : produtohasAtividadeFacade.getAssociatedProducts(current.getIdAtividadeOriginal())) {
            ProdutohasAtividade association = (ProdutohasAtividade) association1;
            association.setAtividade(current);
            association.setDataCriacao(new Date());
            association.setUtilizadoridUtilizador(utilizador);
            association.setIdAssociacao(0);

            ProdutohasAtividadePK pk = new ProdutohasAtividadePK(association.getProduto().getIdProduto(), current.getIdAtividades());
            association.setProdutohasAtividadePK(pk);

            produtohasAtividadeFacade.create(association);
        }

        for (Object association1 : atividadehaspadraoFacade.getAssociatedPatterns(current.getIdAtividadeOriginal())) {
            AtividadehasPadrao association = (AtividadehasPadrao) association1;
            association.setAtividade(current);
            association.setDataCriacao(new Date());
            association.setUtilizadoridUtilizador(utilizador);
            association.setIdAssociacao(0);

            AtividadehasPadraoPK pk = new AtividadehasPadraoPK(current.getIdAtividades(), association.getPadrao().getIdPadrao());
            association.setAtividadehasPadraoPK(pk);

            padraohasAtividadeFacade.create(association);
        }

    }

    public String viewCopiedAtividade(Atividade atividade) {
        current = atividade;
        return "/atividade/ViewCopy";
    }

    public String view(Atividade atividade) {
        current = atividade;
        return "View";
    }
    
    public String edit(Atividade atividade){
        current = atividade;
        return "Edit";
    }

    public void viewAux(Atividade item) {
       current = item;
    }

    public String destroyCopy(Atividade atividade) {
        checkedFromView = true;
        current = atividade;
        performDestroyFull();
        recreateModel();
        recreatePagination();
        return "View";
    }
    
    // grupo 6
    public String desassociateAtividade(Padrao padr, Atividade ativ) {
        List<AtividadehasPadrao> ahp = getFacade().findByIdPadraoIdAtiv(padr.getIdPadrao(), ativ.getIdAtividades());
        if (ahp.size() == 1) {
            AtividadehasPadrao ap = ahp.get(0);
            try {
                atividadehaspadraoFacade.remove(ap);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AtividadeDesassociated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            }
        }
        items = getPagination().createPageDataModel();
        recreatePagination();
        recreateModel();
        
        return "View";
    }
     public DataModel getOriginalAtividadeItemsemp() {
        items = new ListDataModel(getFacade().getOriginalAtividadesemp(utilizadorFacade.find(Integer.parseInt(getUserId())).getEmpresaid()));
        return items;
    }
    
    
     public DataModel getOriginalItemsemp() {
        items = new ListDataModel(getFacade().findOriginalemp(utilizadorFacade.find(Integer.parseInt(getUserId())).getEmpresaid(),current.getProcessoidProcesso().getIdProcesso()));
        return items;
    }
    
    @EJB
    private PapelhasAtividadeFacade papelhasatividadeFacade;
    @EJB
    private ProdutohasAtividadeFacade produtohasatividadeFacade;
    @EJB
    private AtividadehasPadraoFacade atividadehaspadraoFacade;
    @EJB
    private PapelhasAtividadeFacade papelhasAtividadeFacade;
    @EJB
    private ProdutohasAtividadeFacade produtohasAtividadeFacade;
    @EJB
    private AtividadehasPadraoFacade padraohasAtividadeFacade;
    @EJB
    private UtilizadorFacade utilizadorFacade;
    private Boolean checked = false;
    private Boolean checked2 = false;
    private Boolean checkedFromView = false;

}
