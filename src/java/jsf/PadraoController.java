package jsf;

import static com.journaldev.jsf.util.SessionUtils.getUserId;
import jpa.entities.Padrao;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.PadraoFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.xml.bind.DatatypeConverter;
import jpa.entities.Agrupamento;
import jpa.entities.AgrupamentohasPadrao;
import jpa.entities.Atividade;
import jpa.entities.AtividadehasPadrao;
import jpa.entities.AtividadehasPadraoPK;
import jpa.entities.Utilizador;
import jpa.session.AtividadehasPadraoFacade;
import jpa.session.UtilizadorFacade;

@Named("padraoController")
@SessionScoped
public class PadraoController implements Serializable {

    private Padrao current;
    private DataModel items = null;
    @EJB
    private jpa.session.PadraoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;



    public Padrao getSelected() {
        if (current == null) {
            current = new Padrao();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PadraoFacade getFacade() {
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



    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }


    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Padrao) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoDeleted"));
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

    public Padrao getPadrao(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Padrao.class)
    public static class PadraoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PadraoController controller = (PadraoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "padraoController");
            return controller.getPadrao(getKey(value));
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
            if (object instanceof Padrao) {
                Padrao o = (Padrao) object;
                return getStringKey(o.getIdPadrao());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Padrao.class.getName());
            }
        }

    }

    /*eu*/
    private Atividade atividade;
    private Utilizador utilizador;
    private AtividadehasPadrao association;
    @EJB
    private AtividadehasPadraoFacade atividadehasPadraoFacade;
    private Map<Padrao, Boolean> selectedItems;
    private List<Padrao> patternsOnList;

    public String prepareAssociate(Atividade ativi, int saida) {
        current = new Padrao();
        selectedItemIndex = -1;
        atividade = ativi;
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        selectedItems = new HashMap<Padrao, Boolean>();
        if(saida == 0)
            return "/atividade/associatePadrao";
        else
            return "/atividade/associatePadraoCopy";
    }

   public void associate(Padrao p) {
        current.setIdPadrao(p.getIdPadrao());
        current.setDescricao(p.getDescricao());
        current.setNome(p.getNome());
        current.setDataCriacao(p.getDataCriacao());
        current.setImg(p.getImg());
        
        association = new AtividadehasPadrao();
        
        AtividadehasPadraoPK pk = new AtividadehasPadraoPK(atividade.getIdAtividades(), current.getIdPadrao());
        association.setAtividade(atividade);
        association.setPadrao(current);
        association.setUtilizadoridUtilizador(current.getUtilizadoridUtilizador());
        association.setAtividadehasPadraoPK(pk);
        association.setDataCriacao(new Date());
        atividadehasPadraoFacade.create(association);
        
        String toSend = new String();
        toSend += "Padrões(s) ";
        String toSend2 = new String();
        toSend2 += "Foram associados ";
        if (checked == false) {
                if(patternsOnList.size() < 8) {
                    for (int i = 0; i < patternsOnList.size(); i++) {
                        toSend += patternsOnList.get(i).getNome() + ", ";
                    }
                    toSend += " associado(s) com sucesso. ";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend);
                }
                else {
                    toSend2 += Integer.toString(patternsOnList.size());
                    toSend2 += " padrões com sucesso. ";
                    checked = true;
                    JsfUtil.addSuccessMessage(toSend2);
                }
            }
    }

    


    public String prepareListOfPatterns() {
        recreatePagination();
        recreateModel();
        return "/papel/List";
    }

    public String associateAllPatterns(int saida) {
        prepareSelectedList();
        for (int i = 0; i < patternsOnList.size(); i++) {
            associate(patternsOnList.get(i));
        }
        selectedItems = new HashMap<>();
        patternsOnList = new ArrayList<>();
        checked = false;
        if(saida == 0) 
            return "View";
        else
            return "ViewCopy";
    }

    public Map<Padrao, Boolean> getSelectedItems() {
        return selectedItems;
    }

    private void prepareSelectedList() {
        patternsOnList = new ArrayList<Padrao>();
        for (Padrao p : selectedItems.keySet()) {
            if (selectedItems.get(p) == true) {
                patternsOnList.add(p);
            }
        }
    }
    
    private boolean checked = false;
    
      //What was changed/added ------------------------------------------------------------------------------------------
    private Agrupamento agrupamento;
    DataModel<Agrupamento> agrupamentos = null;
    DataModel<Atividade> atividades = null;

    public PadraoController() {
        selectedItems = new HashMap<>();
        patternsOnList = new ArrayList<>();
    }

    public String refreshPage() {
        recreatePagination();
        recreateModel();
        items = getPagination().createPageDataModel();
        return "/padrao/List";
    }

    //sometimes img is already in url cause science so we try and convert if it fails stays the same
    public String prepareView() {
        current = (Padrao) getItems().getRowData();
        prepareSelectedListAgrupamentos();
        prepareSelectedListAtividades();
        try {
            byte[] bytes = DatatypeConverter.parseHexBinary(current.getImg());
            String result = new String(bytes);
            current.setImg(result);
        } catch (Exception e) {
            String result = current.getImg();
            current.setImg(result);
        }
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    public void viewAux() {
        current = (Padrao) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }

    public String prepareCreate() {
        current = new Padrao();
        Date date = new Date();
        current.setDataCriacao(cvtToGMT(date));
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId())); 
        current.setUtilizadoridUtilizador(utilizador);
        selectedItemIndex = -1;
        items = getPagination().createPageDataModel();
        return "Create";
    }

    public Date cvtToGMT(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime());
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() + tz.getDSTSavings());
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }

    public String prepareEdit() {
        current = (Padrao) getItems().getRowData();
        try {
            byte[] bytes = DatatypeConverter.parseHexBinary(current.getImg());
            String result = new String(bytes);
            current.setImg(result);
        } catch (Exception e) {
            String result = current.getImg();
            current.setImg(result);
        }
        Date date = new Date();
        current.setDataCriacao(cvtToGMT(date));
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }


    public void destroyPadrao(Padrao p) {
        current = p;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    private void performDestroyFull() {
        try {
            getFacade().destroyPadrao(current);
            getFacade().remove(current);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public String destroyPadroes() {
        prepareSelectedList();
        int counter = 0;
        for (int i = 0; i < patternsOnList.size(); i++) {
            destroyPadrao(patternsOnList.get(i));
            counter++;
        }
        if (counter == 1) {
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadraoDeleted"));
        } else {
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("PadroesDeleted"));
        }
        selectedItems = new HashMap<>();
        patternsOnList = new ArrayList<>();
        items = getPagination().createPageDataModel();
        return "List";
    }

    public void prepareSelectedListAgrupamentos() {
        List<AgrupamentohasPadrao> result = new ArrayList<>();
        List<Agrupamento> result2 = new ArrayList<>();
        result.addAll(getFacade().pesquisaAgrupamento(current.getIdPadrao()));
        for (AgrupamentohasPadrao ap : result) {
            result2.add(ap.getAgrupamento());
        }

        agrupamentos = new ListDataModel(result2);
    }

    public DataModel<Agrupamento> getAgrupamentos() {
        return agrupamentos;
    }

    public void setAgrupamentos(DataModel<Agrupamento> agrupamentos) {
        this.agrupamentos = agrupamentos;
    }

    public void prepareSelectedListAtividades() {
        List<AtividadehasPadrao> result = new ArrayList<>();
        List<Atividade> result2 = new ArrayList<>();
        result.addAll(getFacade().pesquisaAtividade(current.getIdPadrao()));
        for (AtividadehasPadrao ap : result) {
            result2.add(ap.getAtividade());
        }
        atividades = new ListDataModel(result2);
    }

    public DataModel<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(DataModel<Atividade> atividades) {
        this.atividades = atividades;
    }
    
    public String view(Padrao p) {
        current = p;
        return "View";
    }
    
    public String edit(Padrao p){
        current = p;
        return "Edit";
    }
    
    public String dateToString(Date date) {
        String dateInString = date.toString();
        int length = dateInString.length();
        String year = dateInString.substring(length - 4, length);
        String month = getMonth(dateInString.substring(4, 7));
        String day = "" + dateInString.charAt(8) + dateInString.charAt(9);
        String time = dateInString.substring(11, 19);
        String finalDate = day + "/" + month + "/" + year + " " + time;
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

    
    public DataModel getItemsNotAssociated() {
        items = new ListDataModel(getFacade().getNotAssociated(atividade));
        return items;
    }
    
    public DataModel getItemsNotAssociatedemp() {
        items = new ListDataModel(getFacade().getNotAssociatedemp(atividade,utilizadorFacade.find(Integer.parseInt(getUserId())).getEmpresaid()));
        return items;
    }
    
    @EJB
    UtilizadorFacade utilizadorFacade;

}
