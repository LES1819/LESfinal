package jsf;

import jpa.entities.Artefactos;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ArtefactosFacade;

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
import jpa.entities.Utilizador;


@Named("artefactosController")
@SessionScoped
public class ArtefactosController implements Serializable {

    private Artefactos current;
    private DataModel items = null;
    @EJB
    private jpa.session.ArtefactosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Utilizador utilizador;
    private Artefactos Artefactos;

    private Map<Artefactos, Boolean> selectedItems = new HashMap<Artefactos, Boolean>();
    private List<Artefactos> artefactosesOnList;

    public ArtefactosController() {
        selectedItems = new HashMap<>();
        artefactosesOnList = new ArrayList<>();
    }

    public Artefactos getSelected() {
        if (current == null) {
            current = new Artefactos();
            selectedItemIndex = -1;
        }
        return current;
    }

     private void prepareSelectedList(){
        artefactosesOnList = new ArrayList<Artefactos>();
        for(Artefactos a : selectedItems.keySet()){
            if(selectedItems.get(a) == true){
                artefactosesOnList.add(a);
            }
        }
    }

    public Map<Artefactos, Boolean> getSelectedItems(){
        return selectedItems;
    }

    private ArtefactosFacade getFacade() {
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

    public PaginationHelper getOriginalPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().countOriginal();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().getOriginal());
                }
                
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        recreatePagination();
        return "/artefactos/List";
    }

    public String prepareView() {
        current = (Artefactos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Artefactos();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        recreatePagination();
        recreateModel();
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ArtefactosCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

public String createAndList() {
        create();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "List";
    }

    public String prepareEdit() {
        current = (Artefactos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public void associateSelectedList(){
        prepareSelectedList();
        for(Artefactos a : artefactosesOnList){
            FinalAssociate(a);
        }
    }

    
    public String FinalAssociate(Artefactos a){        
        current.setIdArtefactos(0);
        current.setNome(a.getNome());
        current.setDescricao(a.getDescricao());
        associate();
        recreatePagination();
        recreateModel();
        return "Associate";
    }
    
    public String associate() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Artefacto Associado");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }


    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ArtefactosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String updateAndView() {
        update();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "View";
    }

       public String updateAndEdit() {
        update();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "Edit";
    }

      public void destroyArtefactos(Artefactos a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyArtefactoses() {
        prepareSelectedList();
        for (int i = 0; i < artefactosesOnList.size(); i++) {
            destroyArtefactos(artefactosesOnList.get(i));
        }
        selectedItems = new HashMap<>();
        return "List";
    }

    public String destroy() {
        current = (Artefactos) getItems().getRowData();
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
        performDestroyFull();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "List";
    }

    public void viewAux() {
        current = (Artefactos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }
    
    private void performDestroyFull() {
        try {
            getFacade().destroyArtefactos(current);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ArtefactosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ArtefactosDeleted"));
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

     public String view(Artefactos artefactos) {
        current = artefactos;
        return "View";
    }
    
    public String edit(Artefactos artefactos){
        current = artefactos;
        return "Edit";
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

    
    public DataModel getOriginalItems(){
        recreatePagination();
        recreateModel();
        if(items == null) {
            items = getOriginalPagination().createPageDataModel();
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

    public Artefactos getArtefactos(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Artefactos.class)
    public static class ArtefactosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArtefactosController controller = (ArtefactosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "artefactosController");
            return controller.getArtefactos(getKey(value));
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
            if (object instanceof Artefactos) {
                Artefactos o = (Artefactos) object;
                return getStringKey(o.getIdArtefactos());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Artefactos.class.getName());
            }
        }

    }

}
