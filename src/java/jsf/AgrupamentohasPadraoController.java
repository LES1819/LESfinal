package jsf;

import jpa.entities.AgrupamentohasPadrao;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AgrupamentohasPadraoFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
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


@Named("agrupamentohasPadraoController")
@SessionScoped
public class AgrupamentohasPadraoController implements Serializable {


    private AgrupamentohasPadrao current;
    private DataModel items = null;
    @EJB private jpa.session.AgrupamentohasPadraoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public AgrupamentohasPadraoController() {
    }

    public AgrupamentohasPadrao getSelected() {
        if (current == null) {
            current = new AgrupamentohasPadrao();
            current.setAgrupamentohasPadraoPK(new jpa.entities.AgrupamentohasPadraoPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private AgrupamentohasPadraoFacade getFacade() {
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
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        items = getPagination().createPageDataModel();
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (AgrupamentohasPadrao)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new AgrupamentohasPadrao();
        current.setAgrupamentohasPadraoPK(new jpa.entities.AgrupamentohasPadraoPK());
        current.setIdAssociacao(items.getRowCount() + 1);
        Date date = new Date();
        current.setDataCriacao(cvtToGMT(date));
        selectedItemIndex = -1;
        return "Create";
    }
    
    public Date cvtToGMT(Date date){
      TimeZone tz = TimeZone.getDefault();
      Date ret = new Date(date.getTime());
      if(tz.inDaylightTime(ret)){
        Date dstDate = new Date(ret.getTime() + tz.getDSTSavings());
        if(tz.inDaylightTime(dstDate)){
          ret = dstDate;
        }
      }
      return ret;
    }

    public String create() {
        try {
            current.getAgrupamentohasPadraoPK().setPadraoidPadrao(current.getPadrao().getIdPadrao());
            current.getAgrupamentohasPadraoPK().setAgrupamentoidAgrupamento(current.getAgrupamento().getIdAgrupamento());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasPadraoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (AgrupamentohasPadrao)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getAgrupamentohasPadraoPK().setPadraoidPadrao(current.getPadrao().getIdPadrao());
            current.getAgrupamentohasPadraoPK().setAgrupamentoidAgrupamento(current.getAgrupamento().getIdAgrupamento());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasPadraoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (AgrupamentohasPadrao)getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasPadraoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
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

    public AgrupamentohasPadrao getAgrupamentohasPadrao(jpa.entities.AgrupamentohasPadraoPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass=AgrupamentohasPadrao.class)
    public static class AgrupamentohasPadraoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AgrupamentohasPadraoController controller = (AgrupamentohasPadraoController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "agrupamentohasPadraoController");
            return controller.getAgrupamentohasPadrao(getKey(value));
        }

        jpa.entities.AgrupamentohasPadraoPK getKey(String value) {
            jpa.entities.AgrupamentohasPadraoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.AgrupamentohasPadraoPK();
            key.setAgrupamentoidAgrupamento(Integer.parseInt(values[0]));
            key.setPadraoidPadrao(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(jpa.entities.AgrupamentohasPadraoPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getAgrupamentoidAgrupamento());
            sb.append(SEPARATOR);
            sb.append(value.getPadraoidPadrao());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof AgrupamentohasPadrao) {
                AgrupamentohasPadrao o = (AgrupamentohasPadrao) object;
                return getStringKey(o.getAgrupamentohasPadraoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+AgrupamentohasPadrao.class.getName());
            }
        }

    }

}
