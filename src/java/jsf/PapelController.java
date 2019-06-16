package jsf;

import static com.journaldev.jsf.util.SessionUtils.getUserId;
import jpa.entities.Papel;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.PapelFacade;

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
import jpa.entities.Atividade;
import jpa.entities.PapelhasAtividade;
import jpa.entities.PapelhasAtividadePK;
import jpa.entities.Utilizador;
import jpa.session.PapelhasAtividadeFacade;

@Named("papelController")
@SessionScoped
public class PapelController implements Serializable {

    @EJB
    private PapelhasAtividadeFacade papelhasAtividadeFacade;
    private Papel current;
    private DataModel items = null;
    @EJB
    private jpa.session.PapelFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Atividade atividade;
    private Utilizador utilizador;
    private PapelhasAtividade association;

    // map for selected stuff on the JSF page
    private Map<Papel, Boolean> selectedItems;
    // products to be associated
    private List<Papel> papersOnList;

    public Papel getSelected() {
        if (current == null) {
            current = new Papel();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PapelFacade getFacade() {
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
        return "/papel/List";
    }

    public String prepareView() {
        current = (Papel) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Papel();
        selectedItemIndex = -1;
        current.setUtilizadoridUtilizador(new Utilizador(Integer.parseInt(getUserId())));
        current.setDataCriacao(new Date());
        return "Create";
    }

    public String prepareEdit() {
        current = (Papel) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String destroy() {
        current = (Papel) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    public String destroyAndList() {
        checkedFromView = true;
        performDestroyFull();
        recreateModel();
        recreatePagination();
        return "List";

    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
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

    public Papel getPapel(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Papel.class)
    public static class PapelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PapelController controller = (PapelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "papelController");
            return controller.getPapel(getKey(value));
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
            if (object instanceof Papel) {
                Papel o = (Papel) object;
                return getStringKey(o.getIdPapel());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Papel.class.getName());
            }
        }

    }

    // start of our code
    public String create() {
        recreateModel();
        recreatePagination();
        try {
            int k = getFacade().countRepeated(current.getNome());
            if (!hasSomething(current.getNome())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
            } else {
                if (k > 0) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("papelAlreadyExists"));
                } else {
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelCreated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("PapelCreated2"));
                }
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        recreateModel();
        recreatePagination();
        try {
            String name = getFacade().find(current.getIdPapel()).getNome();
            if (name.equals(current.getNome())) {
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("PapelUpdated2"));
            } else {
                int k = getFacade().countRepeated(current.getNome());
                if (!hasSomething(current.getNome())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
                    return null;
                } else {
                    if (k > 0) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("papelAlreadyExists"));
                        return null;
                    } else {
                        getFacade().edit(current);
                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PapelUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("PapelUpdated2"));
                    }
                }
            }
            return "View";
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
        papersOnList = new ArrayList<Papel>();
        for (Papel p : selectedItems.keySet()) {
            if (selectedItems.get(p) == true) {
                papersOnList.add(p);
            }
        }
    }

    //get HashMap
    public Map<Papel, Boolean> getSelectedItems() {
        return selectedItems;
    }

    public PaginationHelper getPaginationNotAssociated() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countNotAssociate(atividade);
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getNotAssociated(atividade));
                }
            };
        }
        return pagination;
    }

    public String associateAllPapers(int saida) {
        prepareSelectedList();
        for (int i = 0; i < papersOnList.size(); i++) {
            associate(papersOnList.get(i));
        }
        selectedItems = new HashMap<>();
        papersOnList = new ArrayList<>();
        checked = false;
        if(saida == 0)
            return "View";
        else
            return "ViewCopy";
    }

    public String prepareAssociate(Atividade ativi, int saida) {
        recreateModel();
        recreatePagination();
        current = new Papel();
        selectedItemIndex = -1;
        atividade = ativi;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        if(saida == 0)
            return "/atividade/associatePapel";
        else
            return "/atividade/associatePapelCopy";
    }

public void associate(Papel p) {
        current.setIdPapel(p.getIdPapel());
        current.setDescricao(p.getDescricao());
        current.setNome(p.getNome());
        current.setDataCriacao(p.getDataCriacao());

        association = new PapelhasAtividade();

        PapelhasAtividadePK pk = new PapelhasAtividadePK(current.getIdPapel(), atividade.getIdAtividades());
        Date date = new Date();
        association.setAtividade(atividade);
        association.setPapel(current);
        association.setUtilizadoridUtilizador(current.getUtilizadoridUtilizador());
        association.setPapelhasAtividadePK(pk);
        association.setDataCriacao(date);
        papelhasAtividadeFacade.create(association);
        
        String toSend = new String();
        toSend += "Papéis(s) ";
        String toSend2 = new String();
        toSend2 += "Foram associados ";
        if (checked == false) {
                if(papersOnList.size() < 8) {
                    for (int i = 0; i < papersOnList.size(); i++) {
                        toSend += papersOnList.get(i).getNome() + ", ";
                    }
                    toSend += " associado(s) com sucesso. ";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend);
                }
                else {
                    toSend2 += Integer.toString(papersOnList.size());
                    toSend2 += " papéis com sucesso. ";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend2);
                }
            }
    }

    public String prepareListOfPapers() {
        recreatePagination();
        recreateModel();
        return "/papel/List";
    }

    
    
    public String destroyPapers() {
        prepareSelectedList();
        for (Papel p : papersOnList) {
            destroyPaper(p);
        }
        selectedItems = new HashMap<>();
        papersOnList = new ArrayList<>();
        checked2 = false;
        return "List";
    }
    
       public void destroyPaper(Papel a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String nextNotAssociated() {
        getPaginationNotAssociated().nextPage();
        recreateModel();
        return "List";
    }

    public String previousNotAssociated() {
        getPaginationNotAssociated().previousPage();
        recreateModel();
        return "List";
    }

    public DataModel getItemsNotAssociated() {
        items = getPaginationNotAssociated().createPageDataModel();
        return items;
    }

    public void destroyPapel(Papel a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    private void performDestroyFull() {
        recreatePagination();
        recreateModel();
        String toSend = new String();
        toSend += "Papéis ";
        String toSend2 = new String();
        toSend2 += "Foram apagados ";
        try {
            getFacade().destroyPapel(current);
            getFacade().remove(current);
            if (checked2 == false && checkedFromView == false) {
                if (papersOnList.size() < 8) {
                    for (int i = 0; i < papersOnList.size(); i++) {
                        toSend += papersOnList.get(i).getNome() + ", ";
                    }
                    toSend += " apagado(s) com sucesso.";
                    checked2 = true;
                    JsfUtil.addSuccessMessage(toSend);
                } else {
                    toSend2 += Integer.toString(papersOnList.size());
                    toSend2 += " papéis com sucesso.";
                    checked2 = true;
                    JsfUtil.addSuccessMessage(toSend2);
                }
            } else if(checkedFromView == true) {
                String toSendView = "Papel " + current.getNome() + " apagado com sucesso.";
                JsfUtil.addSuccessMessage(toSendView);
                checkedFromView = false;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public PapelController() {
        // map for selected stuff on the JSF page
        selectedItems = new HashMap<Papel, Boolean>();
        papersOnList = new ArrayList<>();
    }
    
    private boolean checked = false;
    private boolean checked2 = false;
    private boolean checkedFromView = false;

}
