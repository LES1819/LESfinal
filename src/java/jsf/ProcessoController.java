package jsf;

import com.journaldev.jsf.util.SessionUtils;
import static com.journaldev.jsf.util.SessionUtils.getUserId;
import jpa.entities.Processo;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ProcessoFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import jpa.entities.Atividade;
import jpa.entities.AtividadehasPadrao;
import jpa.entities.AtividadehasPadraoPK;
import jpa.entities.ProdutohasAtividade;
import jpa.entities.ProdutohasAtividadePK;
import jpa.entities.Utilizador;
import jpa.session.AtividadeFacade;
import jpa.session.AtividadehasPadraoFacade;
import jpa.session.PapelhasAtividadeFacade;
import jpa.session.ProdutohasAtividadeFacade;

@Named("processoController")
@SessionScoped
public class ProcessoController implements Serializable {

    private Processo current;
    private DataModel items = null;
    @EJB
    private jpa.session.ProcessoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Utilizador utilizador;
    private Processo processo;
    private Cookie tiago = new Cookie("e", "gay");

    public Processo getSelected() {
        if (current == null) {
            current = new Processo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProcessoFacade getFacade() {
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
        return "/processo/List";
    }

    public String prepareView() {
        current = (Processo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareEdit() {
        current = (Processo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String destroy() {
        current = (Processo) getItems().getRowData();
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

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoDeleted"));
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
    
    public DataModel getItemsList() {
        items = new ListDataModel(getFacade().findAll());
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

    public Processo getProcesso(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Processo.class)
    public static class ProcessoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProcessoController controller = (ProcessoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "processoController");
            return controller.getProcesso(getKey(value));
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
            if (object instanceof Processo) {
                Processo o = (Processo) object;
                return getStringKey(o.getIdProcesso());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Processo.class.getName());
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
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("processoAlreadyExists"));
                } else {
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoCreated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoCreated2"));
                }
            }
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        recreateModel();
        recreatePagination();
        try {
            String name = getFacade().find(current.getIdProcesso()).getNome();
            if (name.equals(current.getNome())) {
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoUpdated2"));
            } else {
                int k = getFacade().countRepeated(current.getNome());
                if (!hasSomething(current.getNome())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
                    return null;
                } else {
                    if (k > 0) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("processoAlreadyExists"));
                        return null;
                    } else {
                        getFacade().edit(current);
                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("ProcessoUpdated2"));
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

    public ProcessoController() {
        selectedItems = new HashMap<>();
        processosOnList = new ArrayList<>();
    }

    private void prepareSelectedList() {
        processosOnList = new ArrayList<Processo>();
        for (Processo a : selectedItems.keySet()) {
            if (selectedItems.get(a) == true) {
                processosOnList.add(a);
            }
        }
    }

    public Map<Processo, Boolean> getSelectedItems() {
        return selectedItems;
    }

    public String getvalor() {
        return tiago.getValue();

    }

    public String prepareCreate() {
      
        current = new Processo();
        selectedItemIndex = -1;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        current.setDataCriacao(new Date());
        return "Create";
    }

    public String associate() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Processo Associado");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    private void performDestroyFull() {
        recreatePagination();
        recreateModel();
        String toSend = new String();
        toSend += "Processo(s) ";
        String toSend2 = new String();
        toSend2 += "Foram apagados ";
        try {
            getFacade().destroyProcesso(current);
            getFacade().remove(current);
            if (checked == false  && checkedFromView == false) {
                if (processosOnList.size() < 8) {
                    for (int i = 0; i < processosOnList.size(); i++) {
                        toSend += processosOnList.get(i).getNome() + ", ";
                    }
                    toSend += " apagado(s) com sucesso.";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend);
                } else {
                    toSend2 += Integer.toString(processosOnList.size());
                    toSend2 += " processos com sucesso.";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend2);
                }
            } else if (checkedFromView == true) {
                String toSendView = "Processo " + current.getNome() + " apagado com sucesso.";
                JsfUtil.addSuccessMessage(toSendView);
                checkedFromView = false;
            }
            
        } catch (Exception e) {
            System.out.println("Erro no destroyFull");
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public void destroyAssociations() {
        for (Atividade c : current.getAtividadeCollection()) {
            papelhasatividadeFacade.destroyAsso(c);
            produtohasatividadeFacade.destroyAsso(c);
            atividadehaspadraoFacade.destroyAsso(c);
            atividadeFacade.remove(c);
        }
    }

    public void destroyProcesso(Processo a) {
        current = a;
        destroyAssociations();
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyProcessos() {
        prepareSelectedList();
        for (int i = 0; i < processosOnList.size(); i++) {
            destroyProcesso(processosOnList.get(i));
        }
        System.out.println(selectedItems);
        
        selectedItems = new HashMap<>();
        processosOnList = new ArrayList<>();
        checked = false;
        return "List";
    }

    public String destroyAndList() {
        checkedFromView = true;
        destroyAssociations();
        performDestroyFull();
        recreateModel();
        checked = false;
        return "List";
    }

    public List getAssociatedAtividade() {
        return getFacade().getAssociated(current);
    }

    public void viewAux(Processo item) {
        current = item;
    }

    @EJB
    private PapelhasAtividadeFacade papelhasatividadeFacade;
    @EJB
    private ProdutohasAtividadeFacade produtohasatividadeFacade;
    @EJB
    private AtividadehasPadraoFacade atividadehaspadraoFacade;
    @EJB
    private AtividadeFacade atividadeFacade;
    private Map<Processo, Boolean> selectedItems = new HashMap<Processo, Boolean>();
    private List<Processo> processosOnList;
    private boolean checked = false;
    private boolean checkedFromView = false;
}
