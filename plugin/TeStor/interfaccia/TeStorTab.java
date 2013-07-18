/*
 * Created on 23-ott-2004
 */
package plugin.TeStor.interfaccia;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.interfaccia.ListaIndiciInteri;
import plugin.TeStor.condivisi.interfaccia.ListaSD;
import plugin.TeStor.condivisi.interfaccia.PulsanteSpostamento;
import plugin.TeStor.condivisi.interfaccia.ScrollSequence;
import plugin.TeStor.condivisi.interfaccia.ScrollTestCase;

/**
 * La classe rappresenta il tab generico dentro al tab principale del TeStor.
 * Viene ereditata dai tab di input e output del TeStor
 * ({@link plugin.TeStor.interfaccia.InputTab} e {@link plugin.TeStor.interfaccia.OutputTab}),
 * ed è concepita unicamente a questo scopo.
 * <br>Contiene una lista di SD sincronizzata con i SD presenti nel Sequence Editor di Charmy,
 * dei pulsanti per la loro selezione (deselezione),
 * un pannello che visualizza il SD (più recentemente) evidenziato nella lista,
 * ed infine il codice per la gestione di una barra di zoom e della barra di stato di Charmy.
 * I pulsanti di selezione (deselezione) dei SD per il TeStor sono di due tipi:<br>
 * - uno seleziona (deseleziona) i SD <i>evidenziati</i> nella lista;<br>
 * - l'altro li seleziona (deseleziona) <i>tutti</i>.<br>
 * La funzione dei pulsanti di selezione (deselezione) di entrambi i tipi è la seguente:<br>
 * - nel tab di input i pulsanti <i>selezionano</i> i SD per il TeStor;<br>
 * - nel tab di output i pulsanti <i>deselezionano</i> i SD tra quelli selezionati per il TeStor.<br>
 * Di fatto, con queste accezioni selezionare dei SD significa <i>spostarli</i> dalla lista
 * di quelli disponibili (tab di input) alla lista di quelli selezionati (tab di output).
 * Deselezionarli significa effettuare lo spostamento inverso.
 * In questa documentazione i termini "selezione" e "deselezione", riferiti a SD, saranno usati 
 * unicamente con questo significato.<br>
 * L'accezione comune di selezione/deselezione di elementi di una lista, cioè
 * l'atto di cambiare lo stato degli elementi cliccando col mouse su di essi (con o senza
 * i tasti [Ctrl] o [Shift] premuti) al fine di effettuare su di essi delle operazioni,
 * viene sostiutita dal termine <i>"evidenziazione"</i> e i connessi verbo e aggettivo.
 * 
 * @author Fabrizio Facchini
 * @see plugin.TeStor.condivisi.interfaccia.ListaSD
 * @see plugin.TeStor.condivisi.interfaccia.PulsanteSpostamento
 * @see plugin.TeStor.interfaccia.BarraZoom
 */
public class TeStorTab extends JSplitPane implements ComponentListener, ListSelectionListener, ListDataListener{
	/**
	 * Indica se è la prima richiesta di un uso della barra di zoom
	 */
	private static boolean primaVolta = true;
	/**
	 * Il modello dei dati della lista dei SD gestita da questo oggetto
	 */
	protected DefaultListModel listaSDModel;
	/**
	 * Il tab principale del TeStor
	 */
	protected Principale tabPrincipale;
	/**
	 * La lista dei SD visualizzati da questo oggetto
	 */
	private ListaSD listaSD;
	/**
	 * Il TabbedPane che visualizza i SD
	 */
	private JTabbedPane pannDx = new JTabbedPane();
	/**
	 * Il pannello contenente lista e pannello dei pulsanti
	 */
	protected JPanel pannSn = new JPanel(new BorderLayout());
	/**
	 * Il pannello contenente i pulsanti
	 */
	private JPanel pannPulsanti = new JPanel(new BorderLayout());
	/**
	 * Il messaggio da visualizzare nella barra di stato di Charmy
	 */
	protected String infoStato;
	/**
	 * Il pulsante per spostare i SD evidenziati nella lista
	 */
	protected PulsanteSpostamento spostaEvidenziazione;
	/**
	 * Il pulsante per spostare tutti i SD nella lista
	 */
	protected PulsanteSpostamento spostaTutti;
	/**
	 * Il pulsante specifico della sottoclasse del tab.
	 * Nota: l'etichetta è provvisoria!
	 */
	protected JButton specificoTab = new JButton("specificoTab");
	/**
	 * Il numero di SD presenti nella lista
	 */
	protected int numSD = 0;
	/**
	 * Il numero di SD evidenziati nella lista
	 */
	private int numSDEvidenziati = 0;
	/**
	 * Array contenente gli indici dei SD evidenziati nella lista
	 */
	private int[] indiciEvidenziati;
	/**
	 * Il SD evidenziato più di recente.
	 */
	private ScrollSequence SDEvidenziato = null;
	/**
	 * La sequenza temporale degli indici dei SD evidenziati nella lista.
	 * <br>É stata adottata per gestire contemporaneamente
	 * l'esigenza di visualizzare un solo SD, ma di evidenziarne a piacere nella lista.
	 * Si è scelto di visualizzare l'ultimo SD aggiunto all'insieme di quelli già evidenziati.
	 * Se questo viene de-evidenziato, si visualizza quello precedentemente evidenziato,
	 * e così via.
	 */	
	private ListaIndiciInteri indiciCronologici = new ListaIndiciInteri();
	/**
	 * Indica se questo oggetto è disponibile per accettare input dell'utente.
	 * NON lo è:<br>
	 * - se il tab principale del TeStor non è correntemente disponibile per accettare input dell'utente,<br>
	 * - altrimenti, se questo oggetto non è selezionato fra i sotto-tab del TeStor.<br>
	 * Negli altri casi lo è.<br>
	 * Il flag risponde all'esigenza di gestire consistentemente la barra di zoom del TeStor
	 * e la barra di stato di Charmy.
	 * 
	 * @see plugin.TeStor.Principale#statoAttivo
	 */
	private boolean statoAttivo;
	/**
	 * Indica se questo oggetto è selezionato fra i sotto-tab del TeStor.
	 * Viene impostato da {@link TeStorTab#componentShown(ComponentEvent)} e
	 * {@link TeStorTab#componentHidden(ComponentEvent)}.
	 * É utilizzato nella determinazione del flag {@link TeStorTab#statoAttivo}.
	 */
	protected boolean isActive;
	
	/**
	 * Costruisce un nuovo oggetto TeStorTab sulla lista specificata
	 * 
	 * @param listaSDModel la lista (modello) che questo oggetto gestirà
	 * @param altraListaModel la lista inserita nel tab "gemello" (ha senso nelle due sotto-classi)
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public TeStorTab(DefaultListModel listaSDModel, DefaultListModel altraListaModel, Principale tabPrincipale) {
		super();
		this.listaSDModel = listaSDModel;
		listaSD = new ListaSD(this.listaSDModel);
		this.tabPrincipale = tabPrincipale;
		
		// crea i pulsanti di spostamento...
		// nota: le etichette sono provvisiorie!
		spostaEvidenziazione = new PulsanteSpostamento("spostaEvidenziazione", listaSD, altraListaModel, false);
		spostaTutti = new PulsanteSpostamento("spostaTutti", listaSD, altraListaModel, true);
		// disabilita i pulsanti...
		spostaEvidenziazione.setEnabled(false);
		spostaTutti.setEnabled(false);
		specificoTab.setEnabled(false);
		
		// posiziona le componenti nel tab...
		this.setLeftComponent(pannSn);
		this.setRightComponent(pannDx);
		this.setDividerLocation(200);// 200 per uniformità con altri plugin
		pannSn.add(new JScrollPane(this.listaSD),BorderLayout.CENTER);
		pannSn.add(pannPulsanti,BorderLayout.SOUTH);
		pannPulsanti.add(spostaEvidenziazione,BorderLayout.NORTH);
		pannPulsanti.add(spostaTutti,BorderLayout.CENTER);
		pannPulsanti.add(specificoTab,BorderLayout.SOUTH);
		
		// si mette in ascolto...
		listaSD.addListSelectionListener(this);// evidenziazione elementi
		this.listaSDModel.addListDataListener(this);// variazione numero elementi
		this.addComponentListener(this);// selezione/deselezione tab (in particolare)
		
		// aggiorna il testo per la barra di stato (sarà visualizzato solo finchè non si aggiunge un SD)
		infoStato = "TEst Sequence generaTOR - Per informazioni: Help > TEst Sequence generaTOR.";
			
	}
	/**
	 * Gestisce lo stato di questo oggetto di non essere visibile.
	 * Aggiorna il flag {@link TeStorTab#isActive}.
	 * Aggiorna la propria condizione di diponibilità a ricevere input dall'utente.
	 * 
	 * @param e un evento
	 * @see TeStorTab#aggiornaStatoAttivo()
	 */
	public void componentHidden(ComponentEvent e) {
		isActive = false;
		aggiornaStatoAttivo();
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
	}
	/**
	 * Gestisce lo stato di questo oggetto di essere visibile.
	 * Segnala al tab principale del TeStor di essere il sotto-tab correntemente visualizzato.
	 * Aggiorna il flag {@link TeStorTab#isActive}.
	 * Inoltre aggiorna la propria condizione di diponibilità a ricevere input dall'utente.
	 * Infine aggiorna la barra di stato e quella di zoom.
	 * 
	 * @param e un evento
	 * @see TeStorTab#aggiornaStatoAttivo()
	 * @see TeStorTab#aggiornaBarraZoom()
	 */
	public void componentShown(ComponentEvent e) {
		tabPrincipale.setTabAttivo(this);
		
		isActive = true;
		// aggiorna statoAttivo
		aggiornaStatoAttivo();
		// aggiorna la barra di stato
		if(statoAttivo){
			tabPrincipale.setStatusBar(infoStato);
		}
		// aggiorna la barra di zoom
		aggiornaBarraZoom();

	}
	/** 
	 * Gestisce il cambiamento nell'evidenziazione dei SD nella propria lista.
	 * Visualizza l'ultimo SD evidenziato in ordine di tempo;
	 * aggiorna l'abilitazione del pulsante che sposta i SD evidenziati
	 * (se non è avvenuta la generazione dei casi di test);
	 * aggiorna il SD gestito dalla barra di zoom;
	 * segnala delle informazioni.
	 * 
	 * @see TeStorTab#aggiornaStatoAttivo()
	 * @see TeStorTab#aggiornaBarraZoom()
	 * @see TeStorTab#aggiornaInfoStato()
	 */
	public void aggiornaVisualizzazione(){
		// l'indice del SD da visualizzare
		int indice;
		
		// AGGIORNAMENTI...
		// aggiorna la lista degli indici dei SD evidenziati
		indiciEvidenziati = listaSD.getSelectedIndices();
		// aggiorna il numero di SD evidenziati nella lista
		numSDEvidenziati = indiciEvidenziati.length;

		// aggiorna l'abilitazione del pulsante spostaEvidenziazione
		spostaEvidenziazione.setEnabled(!tabPrincipale.isGenerazioneAvvenuta() && numSDEvidenziati > 0);

		// aggiornamento statoAttivo
		aggiornaStatoAttivo();
		
		// ripulisce il pannello
		if(pannDx.getTabCount()>0){// è visualizzato un SD
			pannDx.removeAll();
		}
		// sincronizza gli indiciCronologici con gli indici effettivamente evidenziati.
		// Vedi ListaIndiciInteri.sincr
		indiciCronologici.sincr(indiciEvidenziati);
		// visualizza il SD (cioè lo ScrollSequence)
		if(numSDEvidenziati > 0){// c'è almeno un SD evidenziato
			// assegna l'indice del SD da visualizzare
			indice = indiciCronologici.lastIntElement();
			// assegna il relativo ScrollSequence
			SDEvidenziato = (ScrollSequence)listaSDModel.getElementAt(indice);
			// visualizza lo ScrollSequence
			pannDx.add(SDEvidenziato.getSequence().getNomeSequence(),SDEvidenziato);
			// codice per casi di test - PROVVISORIO ********
			if(SDEvidenziato instanceof ScrollTestCase){
				ScrollTestCase stc = (ScrollTestCase)SDEvidenziato;
				pannDx.add(
					"Casi di test generati dal SD: "+SDEvidenziato.getSequence().getNomeSequence()+" ("+stc.getCasiDiTest().size()+")",
					stc.getTabCasiDiTest()
				);
			}
			// **********************************************
			// barra di zoom...
			if(statoAttivo){
				// aggiornamento barra di zoom
				if(primaVolta){// la barra deve essere creata
					tabPrincipale.setBarraZoom(new BarraZoom(SDEvidenziato.getSeqEd(),tabPrincipale));
					tabPrincipale.getToolBar().add(tabPrincipale.getBarraZoom());
					tabPrincipale.getToolBar().validate();
					primaVolta = false;
				}
			}
		}
		
		// aggiorna la barra di zoom
		aggiornaBarraZoom();
		// aggiorna il testo per la barra di stato
		aggiornaInfoStato();
		// aggiorna la barra di stato
		if(statoAttivo){
			tabPrincipale.setStatusBar(infoStato);
		}
	}
	/**
	 * Gestisce il cambiamento nell'evidenziazione dei SD nella propria lista.
	 * Chiama il metodo {@link TeStorTab#aggiornaVisualizzazione()}.
	 * 
	 * @param e un evento
	 */
	public void valueChanged(ListSelectionEvent e) {
		
		if(!e.getValueIsAdjusting()){// la evidenziazione è stabile
			aggiornaVisualizzazione();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
	 */
	public void contentsChanged(ListDataEvent e) {
	}
	
	/**
	 * Gestisce l'aggiunta di SD alla lista.
	 * Aggiorna le informazioni segnalate e l'abilitazione del pulsante
	 * per spostare tutti i SD (se la generazione dei casi di test
	 * non è avvenuta).
	 * 
	 * @param e un evento
	 * @see TeStorTab#aggiornaStatoAttivo()
	 * @see TeStorTab#aggiornaInfoStato()
	 */
	public void intervalAdded(ListDataEvent e) {
		
		// aggiorna il numero di SD nella lista
		numSD = listaSDModel.getSize();
		// aggiorna l'abilitazione del pulsante spostaTutti
		spostaTutti.setEnabled(!tabPrincipale.isGenerazioneAvvenuta() && numSD > 0);

		
		
		// aggiorna il testo per la barra di stato
		aggiornaInfoStato();
		/* aggiorna il flag di statoAttivo perché, se necessario, i sotto-tab
		 * (che sono sottoclassi di questa), avendo quest'informazione, visualizzeranno
		 * correttamente il messaggio nella barra di stato
		 */
		aggiornaStatoAttivo();
		// aggiorna la barra di stato
		if(statoAttivo){
			tabPrincipale.setStatusBar(infoStato);
		}
	}
	
	/** 
	 * Gestisce la rimozione di SD dalla lista.
	 * Metodo identico a {@link TeStorTab#intervalAdded(ListDataEvent)}
	 * 
	 * @param e un evento
	 */
	public void intervalRemoved(ListDataEvent e) {
		intervalAdded(e);
		
	}

	/**
	 * Ritorna il testo per la barra di stato di Charmy
	 * 
	 * @return il testo contenuto in infoStato
	 */
	public String getInfoStato() {
		return infoStato;
	}
	
	/**
	 * Aggiorna la condizione di diponibilità di questo oggetto a ricevere input dall'utente.
	 * Aggiorna il flag {@link TeStorTab#statoAttivo}.
	 */
	protected void aggiornaStatoAttivo(){
		statoAttivo = tabPrincipale.isStatoAttivo() && isActive;
	}

	/**
	 * @return il campo isActive.
	 */
	public boolean isActive() {
		return isActive;
	}
	/**
	 * Imposta il testo del messaggio informativo infoStato col testo specificato
	 * 
	 * @param string il nuovo testo
	 */
	public void setInfoStato(String string) {
		infoStato = string;
	}
	/**
	 * Aggiorna il testo per la barra di stato.
	 * Il metodo visualizza informazioni complete sul numero di SD evidenziati e
	 * presenti nella lista gestita da questo oggetto.
	 */
	protected void aggiornaInfoStato(){
		if(numSD > 0){
			infoStato = (numSDEvidenziati==0?"Nessun ":numSDEvidenziati+" ") + "SD evidenziat" + (numSDEvidenziati<=1?"o":"i") + ". ";
		}else{
			infoStato = "";
		}
		infoStato += (numSD<=0?"Nessun ":numSD+" ") + "SD";
	}
	/**
	 * Aggiorna il SD associato alla barra di zoom e la propria abilitazione
	 */
	protected void aggiornaBarraZoom(){
		if(tabPrincipale.getBarraZoom()!=null){
			tabPrincipale.getBarraZoom().setEnabledButtons(false);
		}
		if(numSDEvidenziati>0 && tabPrincipale.getBarraZoom()!=null){
			// aggiorna il SD riferito
			tabPrincipale.getBarraZoom().aggiornaRifSeqEd(SDEvidenziato.getSeqEd());
			if(statoAttivo){
				// abilita la barra di zoom
				tabPrincipale.getBarraZoom().setEnabledButtons(true);
			}		
		}
	}
	/**
	 * Abilita/disabilita i due pulsanti di spostamento in quest'oggetto.
	 * 
	 * @param abilita valore di verità
	 */
	public void abilitaPulsSpost(boolean abilita){
		spostaEvidenziazione.setEnabled(abilita);
		spostaTutti.setEnabled(abilita);
	}
}
