package jsf;

import jpa.entities.Sujeito;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.SujeitoFacade;

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

@Named("sujeitoController")
@SessionScoped
public class SujeitoController implements Serializable {

    private Sujeito current;
    private DataModel items = null;
    @EJB
    private jpa.session.SujeitoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Map<Sujeito, Boolean> selectedItems = new HashMap<Sujeito, Boolean>();
    private List<Sujeito> sujeitosOnList;
    
    public SujeitoController() {
        selectedItems = new HashMap<>();
        sujeitosOnList = new ArrayList<>();
    }

    public Sujeito getSelected() {
        if (current == null) {
            current = new Sujeito();
            selectedItemIndex = -1;
        }
        return current;
    }
         private void prepareSelectedList(){
        sujeitosOnList = new ArrayList<Sujeito>();
        for(Sujeito a : selectedItems.keySet()){
            if(selectedItems.get(a) == true){
                sujeitosOnList.add(a);
            }
        }
    }

    public Map<Sujeito, Boolean> getSelectedItems(){
        return selectedItems;
    }

    private SujeitoFacade getFacade() {
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
        return "List";
    }

    public String prepareView() {
        current = (Sujeito) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Sujeito();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("SujeitoCreated"));
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
        current = (Sujeito) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
      public void associateSelectedList(){
        prepareSelectedList();
        for(Sujeito a : sujeitosOnList){
            FinalAssociate(a);
        }
    }
    
      public String FinalAssociate(Sujeito a){        
        current.setIdSujeito(0);
        current.setNome(a.getNome());
        associate();
        recreatePagination();
        recreateModel();
        return "Associate";
    }  
      
      public String associate() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Sujeito Associado");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }  
      
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("SujeitoUpdated"));
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

    public String view(Sujeito sujeito) {
        current = sujeito;
        return "View";
    }
    
    public String edit(Sujeito sujeito){
        current = sujeito;
        return "Edit";
    }  
       
       public String updateAndEdit() {
        update();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "Edit";
    }
    
    public void destroySujeito(Sujeito a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }
    
      public String destroyAndList() {
        performDestroyFull();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "List";
    }
    
    public String destroySujeitos() {
        prepareSelectedList();
        for (int i = 0; i < sujeitosOnList.size(); i++) {
            destroySujeito(sujeitosOnList.get(i));
        }
        selectedItems = new HashMap<>();
        return "List";
    }   
       
    public String destroy() {
        current = (Sujeito) getItems().getRowData();
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
    
     private void performDestroyFull() {
        try {
            getFacade().destroySujeito(current);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("SujeitosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("SujeitoDeleted"));
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

    public Sujeito getSujeito(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Sujeito.class)
    public static class SujeitoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SujeitoController controller = (SujeitoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sujeitoController");
            return controller.getSujeito(getKey(value));
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
            if (object instanceof Sujeito) {
                Sujeito o = (Sujeito) object;
                return getStringKey(o.getIdSujeito());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Sujeito.class.getName());
            }
        }

    }

}
