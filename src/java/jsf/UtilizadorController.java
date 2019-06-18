package jsf;

import jpa.entities.Utilizador;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.UtilizadorFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import jpa.entities.Utilizador;
import jsf.util.SessionUtils;

@Named("utilizadorController")
@SessionScoped
public class UtilizadorController implements Serializable {

    private Utilizador current;
    private DataModel items = null;
    @EJB
    private jpa.session.UtilizadorFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /**
     * private UtilizadorDAO utilizadorDAO = new UtilizadorDAO(); private
     * Utilizador utilizador = new Utilizador();
     *
     * public String login() { utilizador =
     * utilizadorDAO.getUtilizador(utilizador.getNome(),
     * utilizador.getPassword()); if (utilizador == null) {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
     * "Erro no Login!")); return null; } else {
     * FacesContext.getCurrentInstance().addMessage( null, new
     * FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
     * "DONNE!")); return null; } }
     *
     * public Utilizador getUsuario() { return utilizador; }
     *
     * public void setUtilizador(Utilizador utilizador) { this.utilizador =
     * utilizador;
    }
     */
    public UtilizadorController() {
    }

    public Utilizador getSelected() {
        if (current == null) {
            current = new Utilizador();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UtilizadorFacade getFacade() {
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

    public String view(Utilizador u) {
        current = u;
        return "View";
    }

    public String edit(Utilizador u) {
        current = u;
        return "Edit";
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Utilizador) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareLogin() {
        current = new Utilizador();
        return "login";
    }

    public String prepareCreate() {
        current = new Utilizador();
        selectedItemIndex = -1;
        current.setDataCriacao(new Date());
        return "Create";
    }

    public String create() {
        try {
            int j = getFacade().countRepeatednome(current.getNome());
            int k = getFacade().countRepeatedemail(current.getEmail());

            if (j > 0) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nomeUtilizadorExists"));
            } else if (k > 0) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("emailUtilizadorExists"));

            } else {
                getFacade().create(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("UtilizadorCreated"));
            }

            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
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
    
    public String dateToString(Date date) {
        String dateInString = date.toString();
        int length = dateInString.length();
        String year = dateInString.substring(length - 4, length);
        String month = getMonth(dateInString.substring(4, 7));
        String day = "" + dateInString.charAt(8) + dateInString.charAt(9);
        String time = dateInString.substring(11, 19);
        String finalDate = year + "/" + month + "/" + day + " " + time;
        return finalDate;
    }

    public String getMonth(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return null;
        }
    }

    public String validateUsernamePassword(String nome, String password) {
        try {
            int k = getFacade().validate(nome, password);
            if (k>0) {
                Integer id = getFacade().findID(nome);
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("username", nome);
                session.setAttribute("userid", Integer.toString(id));
                return "index";
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Incorrect Username and Passowrd",
                                "Please enter correct username and Password"));
                return "login";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public String sessionName(){
        return SessionUtils.getUserName();
    }
    
    public String sessionID(){
        return SessionUtils.getUserId();
    }


    public String prepareEdit() {
        current = (Utilizador) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            String name = getFacade().find(current.getIdUtilizador()).getNome();
            String email = getFacade().find(current.getIdUtilizador()).getEmail();
            int j = getFacade().countRepeatednome(current.getNome());
            int k = getFacade().countRepeatedemail(current.getEmail());

            if (j > 1) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nomeUtilizadorExists"));
            } else if (k > 1) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("emailUtilizadorExists"));

            } else {
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("UtilizadorUpdated"));
            }

            return "view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Utilizador) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("UtilizadorDeleted"));
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

    public Utilizador getUtilizador(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Utilizador.class)
    public static class UtilizadorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilizadorController controller = (UtilizadorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilizadorController");
            return controller.getUtilizador(getKey(value));
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
            if (object instanceof Utilizador) {
                Utilizador o = (Utilizador) object;
                return getStringKey(o.getIdUtilizador());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Utilizador.class.getName());
            }
        }

    }
}
