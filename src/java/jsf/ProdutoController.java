package jsf;

import static com.journaldev.jsf.util.SessionUtils.getUserId;
import jpa.entities.Produto;
import jpa.entities.Atividade;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ProdutoFacade;

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
import jpa.entities.ProdutohasAtividade;
import jpa.entities.ProdutohasAtividadePK;
import jpa.entities.Utilizador;
import jpa.session.ProdutohasAtividadeFacade;

@Named("produtoController")
@SessionScoped
public class ProdutoController implements Serializable {

    @EJB
    private ProdutohasAtividadeFacade produtohasAtividadeFacade;
    private Produto current;
    private DataModel items = null;
    @EJB
    private jpa.session.ProdutoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Utilizador utilizador;
    private Atividade atividade;

    public Produto getSelected() {
        if (current == null) {
            current = new Produto();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProdutoFacade getFacade() {
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
        return "List";
    }

    public String prepareView() {
        current = (Produto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareEdit() {
        current = (Produto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoDeleted"));
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

    public Produto getProduto(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Produto.class)
    public static class ProdutoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProdutoController controller = (ProdutoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "produtoController");
            return controller.getProduto(getKey(value));
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
            if (object instanceof Produto) {
                Produto o = (Produto) object;
                return getStringKey(o.getIdProduto());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Produto.class.getName());
            }
        }

    }

    // start of our code
    // map for selected stuff on the JSF page
    private Map<Produto, Boolean> selectedItems;
    // products to be associated
    private List<Produto> productsOnList;

    public ProdutoController() {
        // map for selected stuff on the JSF page
        selectedItems = new HashMap<>();
        productsOnList = new ArrayList<>();
    }

    private void prepareSelectedList() {
        productsOnList = new ArrayList<>();
        for (Produto p : selectedItems.keySet()) {
            if (selectedItems.get(p) == true) {
                productsOnList.add(p);
            }
        }
    }

    public String create() {
        recreateModel();
        recreatePagination();
        try {
            int k = getFacade().countRepeated(current.getNome());
            if (!hasSomething(current.getNome())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
            } else {
                if (k > 0) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("produtoAlreadyExists"));
                } else {
                    getFacade().create(current);
                    JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoCreated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoCreated2"));
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
            String name = getFacade().find(current.getIdProduto()).getNome();
            if (name.equals(current.getNome())) {
                getFacade().edit(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoUpdated2"));
            } else {
                int k = getFacade().countRepeated(current.getNome());
                if (!hasSomething(current.getNome())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("nameEmpty"));
                    return null;
                } else {
                    if (k > 0) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/resources/Bundle").getString("produtoAlreadyExists"));
                        return null;
                    } else {
                        getFacade().edit(current);
                        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoUpdated1") + current.getNome() + " " + ResourceBundle.getBundle("/resources/Bundle").getString("ProdutoUpdated2"));
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

    //get HashMap
    public Map<Produto, Boolean> getSelectedItems() {
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

    public String prepareCreate() {
        current = new Produto();
        selectedItemIndex = -1;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        current.setDataCriacao(new Date());
        return "Create";
    }

    public String prepareAssociate(Atividade ativi, int saida) {
        current = new Produto();
        selectedItemIndex = -1;
        atividade = ativi;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        if (saida == 0) {
            return "/atividade/associateProduct";
        } else {
            return "/atividade/associateProductCopy";
        }
    }

    public String associateAllProducts(int saida) {
        prepareSelectedList();
        for (int i = 0; i < productsOnList.size(); i++) {
            associate(productsOnList.get(i));
        }
        selectedItems = new HashMap<>();
        productsOnList = new ArrayList<>();
        checked = false;
        if (saida == 0) {
            return "View";
        } else {
            return "ViewCopy";
        }
    }

    public void associate(Produto p) {
        current.setIdProduto(p.getIdProduto());
        current.setDataCriacao(p.getDataCriacao());
        current.setNome(p.getNome());
        current.setTipo(p.getTipo());

        Date date = new Date();
        ProdutohasAtividade association = new ProdutohasAtividade();
        ProdutohasAtividadePK pk = new ProdutohasAtividadePK(current.getIdProduto(), atividade.getIdAtividades());
        association.setAtividade(atividade);
        association.setProduto(current);
        association.setUtilizadoridUtilizador(current.getUtilizadoridUtilizador());
        association.setProdutohasAtividadePK(pk);
        association.setDataCriacao(date);

        produtohasAtividadeFacade.create(association);

        String toSend = new String();
        toSend += "Produto(s) ";
        String toSend2 = new String();
        toSend2 += "Foram associados ";
        if (checked == false) {
            if (productsOnList.size() < 8) {
                for (int i = 0; i < productsOnList.size(); i++) {
                    toSend += productsOnList.get(i).getNome() + ", ";
                }
                toSend += " associado(s) com sucesso. ";
                checked = true;
                JsfUtil.addSuccessMessage(toSend);
            } else {
                toSend2 += Integer.toString(productsOnList.size());
                toSend2 += " produtos com sucesso. ";
                checked = true;
                JsfUtil.addSuccessMessage(toSend2);
            }
        }

    }

    public String prepareListOfProducts() {
        recreateModel();
        recreatePagination();
        return "/produto/List";
    }

    public String destroyAndList() {
        checkedFromView = true;
        performDestroyFull();
        recreateModel();
        recreatePagination();
        checked2 = false;
        return "List";
    }

    /* acrestcenti*/
    public void destroyProduto(Produto a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyProducts() {
        prepareSelectedList();
        for (int i = 0; i < productsOnList.size(); i++) {
            destroyProduto(productsOnList.get(i));
        }
        selectedItems = new HashMap<>();
        productsOnList = new ArrayList<>();
        checked2 = false;
        return "List";
    }

    private void performDestroyFull() {
        recreatePagination();
        recreateModel();
        String toSend = new String();
        toSend += "Produtos ";
        String toSend2 = new String();
        toSend2 += "Foram apagados ";
        try {
            getFacade().destroyProduto(current);
            getFacade().remove(current);
            if (checked2 == false) {
                if (productsOnList.size() < 8) {
                    for (int i = 0; i < productsOnList.size(); i++) {
                        toSend += productsOnList.get(i).getNome() + ", ";
                    }
                    toSend += " apagado(s) com sucesso.";
                    checked2 = true;
                    JsfUtil.addSuccessMessage(toSend);
                } else {
                    toSend2 += Integer.toString(productsOnList.size());
                    toSend2 += " produtos com sucesso.";
                    checked2 = true;
                    JsfUtil.addSuccessMessage(toSend2);
                }
            } else if (checkedFromView == true) {
                String toSendView = "Produto " + current.getNome() + " apagado com sucesso.";
                JsfUtil.addSuccessMessage(toSendView);
                checkedFromView = false;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /*final acrecesto*/
    public String destroy() {
        current = (Produto) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public DataModel getItemsNotAssociated() {
        items = getPaginationNotAssociated().createPageDataModel();
        return items;
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

    public String deleteProductMessage(Produto product) {
        return getFacade().getDeleteProductMessage(product);
    }

    public String dateToString(Date date) {
        String dateInString = date.toString();
        int length = dateInString.length();
        String year = dateInString.substring(length - 4, length);
        String month = getMonth(dateInString.substring(4, 7));
        String day = "" + dateInString.charAt(8) + dateInString.charAt(9);
        String time = dateInString.substring(11, 19);
        String finale = year + "/" + month + "/" + day + " " + time;
        return finale;
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

    private boolean checked = false;
    private boolean checked2 = false;
    private boolean checkedFromView = false;
}
