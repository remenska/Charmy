/*
 * Created on 24-ott-2004
 */
package plugin.TeStor.interfaccia;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.event.ListDataEvent;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.azioni.AzioneAvviaTeStor;

/**
 * Il tab in cui vengono visualizzati i SD selezionati e i casi di test generati dal TeStor.
 * Questo tab è concepito in funzione di {@link plugin.TeStor.interfaccia.OutputTab}.
 * Per un quadro completo vedi {@link plugin.TeStor.interfaccia.TeStorTab}
 * 
 * @author Fabrizio Facchini
 */
public class OutputTab extends TeStorTab {

	/**
	 * Costruisce un nuovo oggetto OutputTab sulla base dei dati specificati.
	 * Aggiorna i pulsanti di spostamento alla funzione di deselezione di SD.
	 * Aggiorna il pulsante specifico del tab con la funzione di avviare il TeStor.
	 * Predispone l'etichetta della lista per indicare che i SD sono quelli selezionati.
	 * 
	 * @param listaSDModel il modello di lista gestito dal tab
	 * @param altraListaModel il modello di lista dell'altro tab
	 * @param tabPrincipale il tab principale del TeStor
	 * @see plugin.TeStor.condivisi.azioni.AzioneAvviaTeStor
	 */
	public OutputTab(DefaultListModel listaSDModel,DefaultListModel altraListaModel, Principale tabPrincipale, Container rifCont) {
		super(listaSDModel, altraListaModel, tabPrincipale);
		// aggiorna le etichette e i tooltip dei pulsanti 
		spostaEvidenziazione.setText("Deseleziona");
		spostaEvidenziazione.setToolTipText("Esclude i SD in evidenza da quelli selezionati per il TeStor");
		spostaTutti.setText("Desel. tutti");
		spostaTutti.setToolTipText("Esclude tutti i SD dal TeStor");
		specificoTab.setText("AVVIA TeStor");
		specificoTab.setToolTipText("Avvia la generazione dei casi di test sui SD selezionati");
		pannSn.add(new JLabel("SD selezionati"),BorderLayout.NORTH);
		// listener per il pulsante che avvia il TeStor ******************************
		specificoTab.addActionListener(new AzioneAvviaTeStor(this.listaSDModel,this.tabPrincipale));
		
		super.isActive = false;// è il secondo tab ad essere inserito
	}

	/**
	 * Aggiunge al messaggio per la barra di stato informazioni di carattere operativo
	 */
	protected void aggiornaInfoStato(){
		super.aggiornaInfoStato();	
		// segnala quanti SD sono disponibili
		infoStato += " selezionat" + (numSD<=1?"o":"i")
		// segnala altre informazioni
			+ (numSD>0?
				(". Per avviare il TeStor premere <AVVIA TeStor>."
					+ (tabPrincipale.isGenerazioneAvvenuta()?
						" Per azzerarlo portarsi sulla scheda [Selezione SD].":
						".")
				):
				". Per selezionare i SD d'interesse portarsi sulla scheda [Selezione SD]."
			);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
	 */
	/**
	 * Aggiorna l'abilitazione del pulsante che avvia il TeStor.
	 * Il pulsante viene:<br>
	 * - abilitato se c'è almeno un SD nella lista,<br>
	 * - disabilitato altrimenti.
	 * 
	 * @param e un evento
	 */
	public void intervalAdded(ListDataEvent e) {
		super.intervalAdded(e);
		this.specificoTab.setEnabled(this.numSD>0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
	 */
	/**
	 * Metodo identico a {@link OutputTab#intervalAdded(ListDataEvent)}.
	 * 
	 * @param e un evento
	 */
	public void intervalRemoved(ListDataEvent e) {
		this.intervalAdded(e);
	}

}