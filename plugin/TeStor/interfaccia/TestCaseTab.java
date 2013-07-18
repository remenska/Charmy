/*
 * Created on 26-feb-2005
 */
package plugin.TeStor.interfaccia;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.interfaccia.ListaIndiciInteri;
import plugin.TeStor.condivisi.interfaccia.ListaSD;
import plugin.TeStor.condivisi.interfaccia.ScrollSequence;

/**
 * Tab per la visualizzazione dei casi di test generati dal TeStor.
 * Il tab è inserito nel tab di output del plugin; associato ad un SD selezionato per il TeStor
 * visualizza i casi di test da esso implicati.
 * 
 * @author Fabrizio Facchini
 */
public class TestCaseTab extends JSplitPane
		implements
			ComponentListener,
			ListSelectionListener {

	/**
	 * Indica se è la prima richiesta di un uso della barra di zoom
	 */
	//private static boolean primaVolta = true;
	/**
	 * Il modello dei dati della lista dei SD gestita da questo oggetto
	 */
	DefaultListModel listaSDModel;
	/**
	 * Il tab principale del TeStor
	 */
	Principale tabPrincipale;
	/**
	 * La lista dei SD visualizzati da questo oggetto
	 */
	ListaSD listaSD;
	/**
	 * Il TabbedPane che visualizza i SD
	 */
	JTabbedPane pannDx = new JTabbedPane();
	/**
	 * Il pannello contenente lista e pannello dei pulsanti
	 */
	JPanel pannSn = new JPanel(new BorderLayout());
	/**
	 * Il numero di SD presenti nella lista
	 */
	int numSD = 0;
	/**
	 * Il numero di SD evidenziati nella lista
	 */
	int numSDEvidenziati = 0;
	/**
	 * Array contenente gli indici dei SD evidenziati nella lista
	 */
	int[] indiciEvidenziati;
	/**
	 * Il SD evidenziato più di recente.
	 */
	ScrollSequence SDEvidenziato = null;
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
	//boolean statoAttivo;
	/**
	 * Indica se questo oggetto è selezionato fra i sotto-tab del TeStor.
	 * Viene impostato da {@link TeStorTab#componentShown(ComponentEvent)} e
	 * {@link TeStorTab#componentHidden(ComponentEvent)}.
	 * É utilizzato nella determinazione del flag {@link TeStorTab#statoAttivo}.
	 */
	//boolean isActive;
	/**
	 * Costruisce un nuovo oggetto TestCaseTab sulla lista specificata
	 * 
	 * @param listaSDModel la lista (modello) che questo oggetto gestirà
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public TestCaseTab(DefaultListModel listaSDModel, Principale tabPrincipale) {
		super();
		this.listaSDModel = listaSDModel;
		listaSD = new ListaSD(this.listaSDModel);
		this.tabPrincipale = tabPrincipale;
			
		// posiziona le componenti nel tab...
		this.setLeftComponent(pannSn);
		this.setRightComponent(pannDx);
		this.setDividerLocation(200);// 200 per uniformità con altri plugin
		pannSn.add(new JScrollPane(this.listaSD),BorderLayout.CENTER);
		pannSn.add(new JLabel("Casi di test"),BorderLayout.NORTH);
		
		// si mette in ascolto...
		listaSD.addListSelectionListener(this);// evidenziazione elementi
		/*this.listaSDModel.addListDataListener(this);// variazione numero elementi
		this.addComponentListener(this);// selezione/deselezione tab (in particolare)
		
		// aggiorna il testo per la barra di stato (sarà visualizzato solo finchè non si aggiunge un SD)
		infoStato = "TEst Sequence generaTOR - Per informazioni: Help > TEst Sequence generaTOR.";*/
			
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		
		if(!e.getValueIsAdjusting()){// la evidenziazione è stabile
			aggiorna();
		}
	}
	/** 
	 * Gestisce il cambiamento nell'evidenziazione dei SD nella propria lista.
	 * Visualizza l'ultimo SD evidenziato in ordine di tempo;
	 * aggiorna l'abilitazione del pulsante che sposta i SD evidenziati;
	 * aggiorna il SD gestito dalla barra di zoom;
	 * segnala delle informazioni.
	 * 
	 * @see TeStorTab#aggiornaStatoAttivo()
	 * @see TeStorTab#aggiornaBarraZoom()
	 * @see TeStorTab#aggiornaInfoStato()
	 */
	public void aggiorna(){
		// l'indice del SD da visualizzare
		int indice;
		
		// AGGIORNAMENTI...
		// aggiorna la lista degli indici dei SD evidenziati
		indiciEvidenziati = listaSD.getSelectedIndices();
		// aggiorna il numero di SD evidenziati nella lista
		numSDEvidenziati = indiciEvidenziati.length;
		// aggiorna l'abilitazione del pulsante spostaEvidenziazione
		//spostaEvidenziazione.setEnabled(numSDEvidenziati > 0);
		// aggiornamento statoAttivo
		//aggiornaStatoAttivo();
		
		// ripulisce il pannello
		if(pannDx.getTabCount()>0){// è visualizzato un SD
			//pannDx.remove(0);// l'unico ha indice 0
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
			/*if(SDEvidenziato.getClass().getName().equals(ScrollTestCase.class.getName())){
				//pannDx.add(((ScrollSequence)((ScrollTestCase)SDEvidenziato).getCasiDiTest().get(0)).getSequence().getNomeSequence(),(ScrollSequence)((ScrollTestCase)SDEvidenziato).getCasiDiTest().get(0));
				pannDx.add("Casi di test generati il SD: "+SDEvidenziato.getSequence().getNomeSequence(),new TestCaseTab(((ScrollTestCase)SDEvidenziato).getCasiDiTest(),tabPrincipale));
			}
			// **********************************************
			// barra di zoom...
			if(statoAttivo){
				// aggiornamento barra di zoom
				if(primaVolta){// la barra deve essere creata
					tabPrincipale.setBarraZoom(new BarraZoom(SDEvidenziato.getSeqEd()));
					tabPrincipale.getToolBar().add(tabPrincipale.getBarraZoom());
					//tabPrincipale.getBarraZoom().validate();
					tabPrincipale.getToolBar().validate();
					primaVolta = false;
				}
			}*/
		}
		
		// aggiorna la barra di zoom
		/*aggiornaBarraZoom();
		// aggiorna il testo per la barra di stato
		aggiornaInfoStato();
		// aggiorna la barra di stato
		if(statoAttivo){
			tabPrincipale.setStatusBar(infoStato);
		}*/
	}

}
