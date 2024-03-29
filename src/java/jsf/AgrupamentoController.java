package jsf;

import static com.journaldev.jsf.util.SessionUtils.getUserId;
import jpa.entities.Agrupamento;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.AgrupamentoFacade;

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
import jpa.entities.AgrupamentohasFrase;
import jpa.entities.Padrao;
import jpa.entities.Utilizador;
import jpa.entities.AgrupamentohasPadrao;
import jpa.entities.AgrupamentohasPadraoPK;
import jpa.entities.Frase;
import jpa.session.AgrupamentohasFraseFacade;
import jpa.session.AgrupamentohasPadraoFacade;

@Named("agrupamentoController")
@SessionScoped
public class AgrupamentoController implements Serializable {
    @EJB
    private AgrupamentohasPadraoFacade agrupamentohasPadraoFacade;
    private Agrupamento current;
    private DataModel items = null;
    @EJB
    private jpa.session.AgrupamentoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Padrao padrao;
    private Utilizador utilizador;
    private AgrupamentohasPadrao association;
    @EJB
    private AgrupamentohasFraseFacade agrupamentohasFraseFacade;
       private List<Agrupamento> agrupamentosOnList;
    private DataModel<Frase> frases = null;
    private DataModel<Padrao> padroes = null;
    
    private Map<Agrupamento, Boolean> selectedItems = new HashMap<Agrupamento, Boolean>();

    public AgrupamentoController() {
    }

    public Agrupamento getSelected() {
        if (current == null) {
            current = new Agrupamento();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AgrupamentoFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(1000000) {

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


    public String prepareCreate() {
        current = new Agrupamento();
        current.setIdAgrupamento(0);
	Date date = new Date();
	
	utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
	    
        current.setDataCriacao(date);
        selectedItemIndex = -1;
        recreatePagination();
        recreateModel();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Agrupamento) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        recreatePagination();
        recreateModel();
        return "Edit";
    }

    public String update() {
        recreatePagination();
        recreateModel();
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Agrupamento) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentoDeleted"));
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

    public Agrupamento getAgrupamento(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Agrupamento.class)
    public static class AgrupamentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AgrupamentoController controller = (AgrupamentoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "agrupamentoController");
            return controller.getAgrupamento(getKey(value));
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
            if (object instanceof Agrupamento) {
                Agrupamento o = (Agrupamento) object;
                return getStringKey(o.getIdAgrupamento());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Agrupamento.class.getName());
            }
        }

    }
    
    //What was changed/added ---------------------------------------------------------------------------------------
    
    
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
    
    
    public Map<Agrupamento, Boolean> getSelectedItems(){
        return selectedItems;
    }

    private void prepareSelectedList(){
        agrupamentosOnList = new ArrayList<Agrupamento>();
        for(Agrupamento a : selectedItems.keySet()){
            if(selectedItems.get(a) == true){
                agrupamentosOnList.add(a);
            }
        }
    }
    
    public String associateAllAgrupamentos() {
        prepareSelectedList();
        int counter = 0;
        for(int i = 0; i < agrupamentosOnList.size(); i++) {
            associate(agrupamentosOnList.get(i));
            counter++;
        }
        selectedItems = new HashMap<>();
        agrupamentosOnList = new ArrayList<>();
        if(counter > 1){
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentosAssociated"));
        }
        else if(counter == 1){
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentoAssociated"));
        }
        return "associateAgrupamento";
    }
    
    public String prepareAssociate(int padr){
        current = new Agrupamento();
        selectedItemIndex= -1;
        padrao = new Padrao();
        padrao.setIdPadrao(padr);
        utilizador = new Utilizador();
        utilizador.setIdUtilizador(Integer.parseInt(getUserId()));
        current.setUtilizadoridUtilizador(utilizador);
        return "/padrao/associateAgrupamento";
    }
    
    public void associate(Agrupamento a) {
        current.setIdAgrupamento(a.getIdAgrupamento());
        current.setNome(a.getNome());
        current.setDescricao(a.getDescricao());
        current.setDataCriacao(a.getDataCriacao());
        
        association = new AgrupamentohasPadrao();
        
        AgrupamentohasPadraoPK pk = new AgrupamentohasPadraoPK(current.getIdAgrupamento(), padrao.getIdPadrao());
        Date date = new Date();
        association.setPadrao(padrao);
        association.setAgrupamento(current);
        association.setAgrupamentohasPadraoPK(pk);
        association.setDataCriacao(cvtToGMT(date));
        association.setUtilizadoridUtilizador(current.getUtilizadoridUtilizador());
        agrupamentohasPadraoFacade.create(association);
        recreateModel();
        recreatePagination();
    }
    
    public String desassociateAgrupamento(Padrao padr, Agrupamento agrup) {
        List<AgrupamentohasPadrao> ahp = getFacade().removeByIdPadraoIdAgrup(padr.getIdPadrao(), agrup.getIdAgrupamento());
        if (ahp.size() == 1) {
            AgrupamentohasPadrao ap = ahp.get(0);
            try {
                agrupamentohasPadraoFacade.remove(ap);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentoDesassociated"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            }
        }
        items = getPagination().createPageDataModel();
        recreatePagination();
        recreateModel();
        
        return "View";
    }
    
    //agrupamentos grupo 6
    
    public DataModel<Padrao> getPadroes() {
        return padroes;
    }

    public void setPadroes(DataModel<Padrao> padroes) {
        this.padroes = padroes;
    }

    public DataModel<Frase> getFrases() {
        return frases;
    }

    public void setFrases(DataModel<Frase> frases) {
        this.frases = frases;
    }
    
    public void setSelectedItems(Map<Agrupamento, Boolean> selectedItems) {
        this.selectedItems = selectedItems;
    }
    
       public String destroyAgrup(){
        
        destroyAgrupamento(current);
        return "List";
    }
       
   private void performDestroyFull() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
        }
    }
       
     public void destroyAgrupamento(Agrupamento a) {
        current = a;
        performDestroyFull();
        recreatePagination();
        recreateModel();
    }

    public String destroyAgrupamentos() {
        prepareSelectedList();
        for (int i = 0; i < agrupamentosOnList.size(); i++) {
            destroyAgrupamento(agrupamentosOnList.get(i));
        }
        selectedItems = new HashMap<>();
        agrupamentosOnList = new ArrayList<>();
        return "List";
    }
    
    public String prepareView() {
        
        current = (Agrupamento) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        prepareSelectedListFrases();
        prepareSelectedListPadroes();
        recreatePagination();
        recreateModel();
        return "View";
    }
    
     public void prepareSelectedListFrases(){
        List<AgrupamentohasFrase> result = new ArrayList<>();
        List<Frase> result2 = new ArrayList<>();
        result.addAll(getFacade().pesquisaFrase(current.getIdAgrupamento()));
        for(AgrupamentohasFrase af: result){
            result2.add(af.getFrase());
        }
        
        frases = new ListDataModel(result2);        
    }
     
     public void prepareSelectedListPadroes(){
        List<AgrupamentohasPadrao> result = new ArrayList<>();
        List<Padrao> result2 = new ArrayList<>();
        result.addAll(getFacade().pesquisaPadrao(current.getIdAgrupamento()));
        for(AgrupamentohasPadrao af: result){
            result2.add(af.getPadrao());
        }
        
        padroes = new ListDataModel(result2);        
    }
     
     public String desassociarFrase(int idFrase){
        List<AgrupamentohasFrase> ahf = getFacade().findByIdFraseIdAgrup(idFrase, current.getIdAgrupamento());
        if(ahf.size() == 1){
           AgrupamentohasFrase af =  ahf.get(0);
            try {
                 agrupamentohasFraseFacade.remove(af);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasFraseDeleted"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            }
            prepareSelectedListFrases();
        }
        recreatePagination();
        recreateModel();
        
        return "View";
    }
    
    public String desassociarPadrao(int idPadrao){
        List<AgrupamentohasPadrao> ahp = getFacade().findByIdPadraoIdAgrup(idPadrao, current.getIdAgrupamento());
        if(ahp.size() == 1){
           AgrupamentohasPadrao ap =  ahp.get(0);
            try {
                 agrupamentohasPadraoFacade.remove(ap);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/resources/Bundle").getString("AgrupamentohasPadraoDeleted"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/resources/Bundle").getString("PersistenceErrorOccured"));
            }
            prepareSelectedListPadroes();
        }
	recreatePagination();
        recreateModel();
        
        return "View";
    }

    
 
	
    public String viewAgrupamento(Agrupamento agrupamento) {
        current = agrupamento;
        return "/agrupamento/View";
    }
	public void viewAux() {
        current = (Agrupamento) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
    }
}
