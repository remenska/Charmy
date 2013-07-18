/*
 * Created on 24-ott-2004
 */
package plugin.TeStor.interfaccia;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.azioni.AzioneAzzeraTeStor;

/**
 * Il tab in cui selezionare i SD in input al TeStor.
 * Questo tab è concepito in funzione di {@link plugin.TeStor.interfaccia.OutputTab}.
 * Per un quadro completo vedi {@link plugin.TeStor.interfaccia.TeStorTab}.
 * 
 * @author Fabrizio Facchini
 */
public class InputTab extends TeStorTab {

	/**
	 * Costruisce un nuovo oggetto InputTab sulla base dei dati specificati.
	 * Aggiorna i pulsanti di spostamento alla funzione di selezione di SD.
	 * Aggiorna il pulsante specifico del tab con la funzione di azzerare il TeStor.
	 * Predispone l'etichetta della lista per indicare che i SD sono quelli disponibili.
	 * 
	 * @param listaSDModel il modello di lista gestito dal tab
	 * @param altraListaModel il modello di lista dell'altro tab
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public InputTab(DefaultListModel listaSDModel,DefaultListModel altraListaModel, Principale tabPrincipale) {
		super(listaSDModel, altraListaModel, tabPrincipale);
		// aggiorna le etichette e i tooltip dei pulsanti 
		spostaEvidenziazione.setText("Seleziona");
		spostaEvidenziazione.setToolTipText("Seleziona per il TeStor i SD in evidenza");
		spostaTutti.setText("Sel. tutti");
		spostaTutti.setToolTipText("Seleziona per il TeStor tutti i SD disponibili");
		specificoTab.setText("AZZERA TeStor");
		specificoTab.setToolTipText("Reimposta il TeStor per generare nuovi casi di test");
		specificoTab.setEnabled(true);
		pannSn.add(new JLabel("SD disponibili"),BorderLayout.NORTH);
		// listener per il pulsante che azzera il TeStor ******************************
		specificoTab.addActionListener(new AzioneAzzeraTeStor(this.tabPrincipale));
		
		super.isActive = true;// è il primo tab ad essere inserito
	}

	/**
	 * Aggiunge al messaggio per la barra di stato informazioni di carattere operativo
	 */
	protected void aggiornaInfoStato(){
		super.aggiornaInfoStato();	
		// segnala quanti SD sono disponibili
		infoStato += " disponibil" + (numSD<=1?"e":"i")
		// segnala altre informazioni
			+ (numSD>0?
				(!tabPrincipale.isGenerazioneAvvenuta()?
					". Selezionare i SD d'interesse, portarsi sulla scheda [Generazione casi di test] ed avviare il TeStor.":
					". Riavviare il TeStor oppure azzerarlo premendo il tasto <AZZERA TeStor>"
				):
				(!tabPrincipale.isGenerazioneAvvenuta()?
					". Per avviare il TeStor portarsi sulla scheda [Generazione casi di test].":
					". Riavviare il TeStor oppure azzerarlo premendo il tasto <AZZERA TeStor>"
				)
			);
	}
	
}