/*
 * Created on 24-ott-2004
 */

package plugin.TeStor;

import javax.swing.DefaultListModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import plugin.TeStor.azioni.AzioneHelp;
import plugin.TeStor.azioni.AzionePlug;
import plugin.TeStor.condivisi.AlgoritmoTeStor;
import plugin.TeStor.condivisi.azioni.AzioneSeqDiag;
import plugin.TeStor.data.PlugData;
import plugin.TeStor.dialog.TeStorDialog;
import plugin.TeStor.interfaccia.BarraZoom;
import plugin.TeStor.interfaccia.InputTab;
import plugin.TeStor.interfaccia.OutputTab;
import plugin.TeStor.interfaccia.TeStorTab;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;

/**
 * Il tab principale del TeStor.
 * Contiene due modelli di lista di SD (uno con tutti quelli <i>disponibili</i>,
 * l'altro con quelli <i>selezionati</i> per il TeStor); una <i>barra di zoom</i> per questi SD;
 * i due <i>sotto-tab</i> {@link plugin.TeStor.interfaccia.InputTab}
 * e {@link plugin.TeStor.interfaccia.OutputTab} costruiti sulla base dei SD disponibili
 * e selezionati, rispettivamente; infine costruisce le <i>voci di menù</i> del TeStor per i menù
 * di Charmy Plug ed Help: il primo di essi permette di visualizzare il <i>dialog di selezione
 * e avvio rapido del TeStor</i> ({@link plugin.TeStor.dialog.TeStorDialog}).
 * 
 * @author Fabrizio Facchini
 * @see plugin.TeStor.interfaccia.BarraZoom
 * @see plugin.TeStor.interfaccia.TeStorTab
 * @see plugin.TeStor.azioni.AzionePlug
 * @see plugin.TeStor.azioni.AzioneHelp
 */
public class Principale extends JTabbedPane implements IMainTabPanel{
	/**
	 * L'aspetto grafico di Charmy  
	 */	
	private PlugDataWin plugDataWin;
	/**
	 * L'aspetto dei dati di Charmy
	 */
	private plugin.TeStor.data.PlugData plugData;
	/** 
	 * Lista dei SD disponibili (modello)
	 */
	private DefaultListModel ListaDispModel = new DefaultListModel();
	/** 
	 * Lista dei SD selezionati (modello)
	 */
	private DefaultListModel ListaSelModel = new DefaultListModel();
	/**
	 * Il sotto-tab in cui si selezionano i SD per il TeStor
	 */
	private InputTab inputTab;
	/**
	 * Il sotto-tab in cui si visualizzano i casi di test generati
	 */
	private OutputTab outputTab;
	/**
	 * La barra di zoom per i SD
	 */
	private BarraZoom barraZoom;
	/**
	 * Il TeStorDialog
	 */
	private TeStorDialog dialog;
	/**
	 * Il sotto-tab del TeStor che è correntemente selezionato
	 */
	private TeStorTab tabAttivo;
	
	/**
	 * Indica se questo oggetto è disponibile per accettare input dell'utente.
	 * NON lo è:<br>
	 * - se il TeStorDialog è correntemente visualizzato,<br>
	 * - altrimenti, se questo oggetto non è selezionato fra i tab di Charmy.<br>
	 * Negli altri casi lo è.<br>
	 * Il flag risponde all'esigenza di gestire consistentemente la barra di zoom del TeStor
	 * e la barra di stato di Charmy.
	 */
	private boolean statoAttivo;
	/**
	 * Indica se questo oggetto è selezionato tra i tab di tutti i plugin di Charmy.
	 * É utilizzato nella determinazione del flag {@link Principale#statoAttivo}.
	 * Viene impostato da {@link Principale#stateActive()} e
	 * {@link Principale#stateInactive()}.
	 */
	private boolean isActive = false;// non è il primo tab visualizzato
	/**
	 * Indica se il TeStorDialog è correntemente visualizzato.
	 * É utilizzato nella determinazione del flag {@link Principale#statoAttivo}.
	 * Viene impostato da {@link plugin.TeStor.azioni.AzionePlug#actionPerformed(ActionEvent)}
	 * e {@link plugin.TeStor.dialog.AzioneChiudiDialog#actionPerformed(ActionEvent)}.
	 */
	private boolean dialogVisualizzato = false;// all'avvio non è visualizzato
	/**
	 * Indica che è avvenuta una generazione di casi di test.
	 * Questo flag influenza l'abilitazione dei tasti di
	 * selezione/deselezione dei SD nelle interfacce del plugin.
	 * Se questo flag è vero, i tasti vengono tutti disabilitati.
	 */
	private boolean isGenerazioneAvvenuta = false;// all'avvio non c'è stata
	/**
	 * Il cuore del TeStor: l'algoritmo TeStor
	 */
	public AlgoritmoTeStor algoritmo;

	/**
	 * Costruisce un nuovo oggetto Principale.
	 * Vengono creati i due sotto-tab del TeStor e impostati alcuni flag.
	 */
	public Principale(){
		super();
	}
	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#setDati(data.PlugDataWin, data.PlugData)
	 */
	/**
	 * Aggiunge questo oggetto ai tab dei plugin del TeStor grazie ai parametri specificati.
	 * Crea i menù e aggiunge i relativi listener.
	 * Si mette in ascolto per le modifiche sui SD.
	 * 
	 * @param plugDtW l'interfaccia grafica di Charmy
	 * @param pd i dati di Charmy
	 * @see plugin.TeStor.condivisi.azioni.AzioneSeqDiag
	 */
	public void setDati(PlugDataWin plugDtW) {
		
		// crea i due sotto-tab
		inputTab = new InputTab(ListaDispModel,ListaSelModel,this);
		outputTab = new OutputTab(ListaSelModel,ListaDispModel,this, plugDtW.getMainPanel().getTopLevelAncestor());
		
		// imposta quale sotto-tab è visibile
		tabAttivo = inputTab.isActive()? (TeStorTab)inputTab: outputTab;// in quanto è il primo inserito
		
		// aggiorna statoAttivo
		aggiornaStatoAttivo();		
		// aggiunge il tab del TeStor a Charmy
		plugDtW.getMainPanel().addTab("TEst Sequence generaTOR", this);
		
		// inserisce i due sotto-tab...
		this.add("Selezione SD",inputTab);
		this.add("Generazione casi di test",outputTab);
		
		// inizializza i campi...
		plugDataWin = plugDtW;
		//plugData = pd;
		this.algoritmo = new AlgoritmoTeStor(plugData);
		this.dialog =  new TeStorDialog(plugDtW.getMainPanel().getTopLevelAncestor(), this);
		
		// MENU ********************		
		// crea e inserisce gli elementi di menu nel menu del plug			
		JMenuItem plugMenuItem = new JMenuItem("TEst Sequence generaTOR...");
		// crea il sottomenu TeStor, gli aggiunge gli elem. precedenti e lo colloca nel menu plug di Charmy
		JMenu plugMenu = new JMenu("TeStor");
		plugMenu.add(plugMenuItem);
		plugDtW.getPlugMenu().add(plugMenu);
		// si pone all'ascolto
		plugMenuItem.addActionListener(new AzionePlug(plugDtW.getMainPanel().getTopLevelAncestor(),this));
		
		// crea e inserisce l'elemento di menu nel menu help
		JMenuItem helpMenuItem = new JMenuItem("TEst Sequence generaTOR");
		plugDtW.getHelpMenu().add(helpMenuItem,(plugDtW.getHelpMenu().getMenuComponentCount()-2));
		// si pone all'ascolto
		helpMenuItem.addActionListener(new AzioneHelp());
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugDataWin()
	 */
	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugData()
	 */
	public IPlugData getPlugData() {
		return plugData;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getEditMenu()
	 */
	public EditGraphInterface getEditMenu() {
		return null;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getZoomAction()
	 */
	public ZoomGraphInterface getZoomAction() {
		return null;
	}

	/**
	 * Gestisce l'evento che indica che Charmy ha creato una nuova architettura (vuota).
	 * Aggiorna la barra di stato.
	 */
	public void resetForNewFile() {
		inputTab.setInfoStato("TEst Sequence generaTOR - Per informazioni: Help > TEst Sequence generaTOR.");
		outputTab.setInfoStato(inputTab.getInfoStato());
		if(statoAttivo){
			setStatusBar(tabAttivo.getInfoStato());
		}
	}

	/**
	 * Gestisce lo stato di essere il tab di Charmy correntemente selezionato.
	 * Aggiorna il flag {@link Principale#isActive}.
	 * Aggiorna lo stato di disponibilità di questo oggetto ad accettare input dell'utente,
	 * la barra di stato e quella di zoom (quest'ultima viene aggiunta alla toolbar di Charmy).
	 * 
	 * @see Principale#aggiornaStatoAttivo()
	 */
	public void stateActive() {
		isActive = true;
		aggiornaStatoAttivo();
		if(statoAttivo){
			setStatusBar(tabAttivo.getInfoStato());
		}
		if(barraZoom!=null){
			getToolBar().add(barraZoom);
		}
	}

	/**
	 * Gestisce lo stato di NON essere il tab di Charmy correntemente selezionato.
	 * Aggiorna il flag {@link Principale#isActive}.
	 * Aggiorna lo stato di disponibilità di questo oggetto ad accettare input dell'utente,
	 * la barra di stato e quella di zoom (quest'ultima viene rimossa dalla toolbar di Charmy).
	 * 
	 * @see Principale#aggiornaStatoAttivo()
	 */
	public void stateInactive() {
		isActive = false;
		aggiornaStatoAttivo();
		setStatusBar("");
		if(barraZoom!=null){
			getToolBar().remove(barraZoom);
		}
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getDati()
	 */
	public Object[] getDati() {
		return null;
	}
	
	/**
	 * Restituisce il sotto-tab attualmente selezionato
	 * 
	 * @return il sotto-tab attualmente selezionato
	 */
	public TeStorTab getTabAttivo(){
		return tabAttivo;
	}

	/**
	 * Restituisce lo stato di disponibilità di questo oggetto ad accettare input dell'utente.
	 * 
	 * @return il valore di {@link Principale#statoAttivo}
	 */
	public boolean isStatoAttivo() {
		return statoAttivo;
	}

	/**
	 * Imposta l'informazione del sotto-tab correntemente selezionato con quello specificato.
	 * 
	 * @param tab il sotto-tab del tab principale del TeStor correntemente attivo
	 */
	public void setTabAttivo(TeStorTab tab) {
		tabAttivo = tab;
	}

	/**
	 * Aggiorna la propria condizione di diponibilità a ricevere input dall'utente.
	 * Aggiorna il flag {@link Principale#statoAttivo}.
	 */
	public void aggiornaStatoAttivo() {
		statoAttivo = this.isActive && !this.dialogVisualizzato;
	}

	/**
	 * Imposta l'informazione sullo stato di visibilità del TeStorDialog con il valore specificato. 
	 * Aggiorna il flag {@link Principale#dialogVisualizzato}, 
	 * la propria disponibilità a ricevere input dell'utente e la barra di stato.
	 * 
	 * @param b un valore di verità
	 * @see Principale#aggiornaStatoAttivo()
	 */
	public void setDialogVisualizzato(boolean b){
		dialogVisualizzato = b;
		aggiornaStatoAttivo();
		if(statoAttivo){
			setStatusBar(getTabAttivo().getInfoStato());
		}
	}
	
	/**
	 * Aggiorna il testo visualizzato nella barra di stato di Charmy col messaggio specificato
	 * 
	 * @param messaggio il testo per la barra di stato
	 */
	public void setStatusBar(String messaggio){
		plugDataWin.getStatusBar().setText(messaggio);
	}
	
	/**
	 * Restituisce il modello della lista dei SD disponibili
	 * 
	 * @return il modello della lista dei SD disponibili
	 */
	public DefaultListModel getListaDispModel() {
		return ListaDispModel;
	}

	/**
	 * Restituisce il modello della lista dei SD selezionati
	 * 
	 * @return il modello della lista dei SD selezionati
	 */
	public DefaultListModel getListaSelModel() {
		return ListaSelModel;
	}

	/**
	 * Restituisce la barra di zoom del TeStor
	 * 
	 * @return la barra di zoom del TeStor
	 */
	public BarraZoom getBarraZoom() {
		return barraZoom;
	}

	/**
	 * Imposta la barra di zoom del TeStor con quella specificata
	 * 
	 * @param zoom una barra di zoom
	 */
	public void setBarraZoom(BarraZoom zoom) {
		barraZoom = zoom;
	}
	/**
	 * @return il campo dialog.
	 */
	public TeStorDialog getDialog() {
		return dialog;
	}
	/**
	 * @return il campo isGenerazioneAvvenuta.
	 */
	public boolean isGenerazioneAvvenuta() {
		return isGenerazioneAvvenuta;
	}
	/**
	 * Imposta il valore del campo specificato.
	 * 
	 * @param isGenerazioneAvvenuta il valore per isGenerazioneAvvenuta da assegnare.
	 */
	public void setGenerazioneAvvenuta(boolean isGenerazioneAvvenuta) {
		this.isGenerazioneAvvenuta = isGenerazioneAvvenuta;
		inputTab.aggiornaVisualizzazione();
		outputTab.aggiornaVisualizzazione();
	}
	/**
	 * Abilita/disabilita i pulsanti di spostamento delle interfacce.
	 * 
	 * @param abilita
	 */
	public void abilitaPulsSpost(boolean abilita){
		inputTab.abilitaPulsSpost(abilita);
		outputTab.abilitaPulsSpost(abilita);
		dialog.abilitaPulsSpost(abilita);
	}
	/**
	 * Restituisce la toolBar di Charmy
	 * 
	 * @return la toolBar di Charmy
	 */
	public JPanel getToolBar(){
		return plugDataWin.getToolBarPanel();
	}
	/**
	 * @return il campo PlugData
	 */
	public PlugData getPD(){
		return plugData;
	}
	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {
		plugData=new PlugData(pm);
		return plugData;
	}
	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#init()
	 */
	public void init() {
		// aggiunge il listener per gli eventi sui SD **********************
		((plugin.sequencediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.sequence"))
		.getListaDS().addListener(new AzioneSeqDiag(ListaDispModel, ListaSelModel, this.plugDataWin, this.plugData));
	}
	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}

}
