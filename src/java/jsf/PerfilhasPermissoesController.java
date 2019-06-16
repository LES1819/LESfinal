package jsf;

import jpa.entities.PerfilhasPermissoes;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.PerfilhasPermissoesFacade;

import java.io.Serializable;
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

@Named("perfilhasPermissoesController")
@SessionScoped
public class PerfilhasPermissoesController implements Serializable {

    private PerfilhasPermissoes current;
    private DataModel items = null;
    @EJB
    private jpa.session.PerfilhasPermissoesFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public PerfilhasPermissoesController() {
    }

    public PerfilhasPermissoes getSelected() {
        if (current == null) {
            current = new PerfilhasPermissoes();
            current.setPerfilhasPermissoesPK(new jpa.entities.PerfilhasPermissoesPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private PerfilhasPermissoesFacade getFacade() {
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
        current = (PerfilhasPermissoes) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new PerfilhasPermissoes();
        current.setPerfilhasPermissoesPK(new jpa.entities.PerfilhasPermissoesPK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getPerfilhasPermissoesPK().setPerfilid(current.getPerfil().getId());
            current.getPerfilhasPermissoesPK().setPermissoesidPermissoes(current.getPermissoes().getIdPermissoes());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PerfilhasPermissoesCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (PerfilhasPermissoes) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getPerfilhasPermissoesPK().setPerfilid(current.getPerfil().getId());
            current.getPerfilhasPermissoesPK().setPermissoesidPermissoes(current.getPermissoes().getIdPermissoes());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PerfilhasPermissoesUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (PerfilhasPermissoes) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PerfilhasPermissoesDeleted"));
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

    public PerfilhasPermissoes getPerfilhasPermissoes(jpa.entities.PerfilhasPermissoesPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = PerfilhasPermissoes.class)
    public static class PerfilhasPermissoesControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PerfilhasPermissoesController controller = (PerfilhasPermissoesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "perfilhasPermissoesController");
            return controller.getPerfilhasPermissoes(getKey(value));
        }

        jpa.entities.PerfilhasPermissoesPK getKey(String value) {
            jpa.entities.PerfilhasPermissoesPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.PerfilhasPermissoesPK();
            key.setPerfilid(Integer.parseInt(values[0]));
            key.setPermissoesidPermissoes(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(jpa.entities.PerfilhasPermissoesPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getPerfilid());
            sb.append(SEPARATOR);
            sb.append(value.getPermissoesidPermissoes());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PerfilhasPermissoes) {
                PerfilhasPermissoes o = (PerfilhasPermissoes) object;
                return getStringKey(o.getPerfilhasPermissoesPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PerfilhasPermissoes.class.getName());
            }
        }

    }

}
