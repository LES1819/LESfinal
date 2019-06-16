package jsf;

import jpa.entities.Empresa;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.EmpresaFacade;

import java.io.Serializable;
import java.util.Date;
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

@Named("empresaController")
@SessionScoped
public class EmpresaController implements Serializable {

    private Empresa current;
    private DataModel items = null;
    @EJB
    private jpa.session.EmpresaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public EmpresaController() {
    }

    public Empresa getSelected() {
        if (current == null) {
            current = new Empresa();
            selectedItemIndex = -1;
        }
        return current;
    }

    private EmpresaFacade getFacade() {
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
        return "List";
    }

    public String prepareView() {
        current = (Empresa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Empresa();
        selectedItemIndex = -1;
        current.setDataCriacao(new Date());
        return "Create";
    }

    public String create() {

        try {
            int k = getFacade().countRepeatednome(current.getNome());
            int j = getFacade().countRepeatednif(current.getNif());
            if (!hasSomethingnumbers(current.getNif())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaNifvazio"));
            } else if(!hasSomething(current.getNome())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaNomevazio"));
            } else {
                if (j > 0) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaNifalreadyExists"));
                } else if (k > 0) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaNomealreadyExists"));
                } else {
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaCreated"));
                }
            }
            return prepareCreate();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public boolean hasSomethingnumbers(String s) {
        for (int i = 0; i < s.length(); i++) {
            boolean atleastOneChar = s.matches("[0-9]+");
            if (atleastOneChar == true) {
                return true;
            }
        }
        return false;
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
    

    public String prepareEdit() {
        current = (Empresa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Empresa) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("EmpresaDeleted"));
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

    public Empresa getEmpresa(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Empresa.class)
    public static class EmpresaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmpresaController controller = (EmpresaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "empresaController");
            return controller.getEmpresa(getKey(value));
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
            if (object instanceof Empresa) {
                Empresa o = (Empresa) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Empresa.class.getName());
            }
        }

    }

}
