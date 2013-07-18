/*
 * Created on 27-set-2004
 */
package plugin.TeStor.condivisi.interfaccia;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.DefaultListModel;

import plugin.TeStor.condivisi.azioni.AzioneSpostaEvidenziazione;
import plugin.TeStor.condivisi.azioni.AzioneSpostaTutti;


/**
 * Pulsante di spostamento di elementi fra liste.
 * Il pulsante, collegato a due liste, effettua lo spostamento degli elementi evidenziati
 * (o di tutti gli elementi presenti) nella lista di origine verso quella di destinazione.
 * 
 * @author Fabrizio Facchini
 * @see plugin.TeStor.condivisi.azioni.AzioneSpostaEvidenziazione
 * @see plugin.TeStor.condivisi.azioni.AzioneSpostaTutti
 */
public class PulsanteSpostamento extends JButton{
	/**
	 * Costruisce un nuovo oggetto PulsanteSpostamento col titolo specificato.
	 * L'oggetto sposte elementi dalla lista origine a quella destinazione.
	 * Il parametro <code>tutti</code> specializza il pulsante, indicando se
	 * gli spostamenti interesseranno tutti gli elementi nella lista di origine
	 * o solo quelli evidenziati nel momento di premere il pulsante.
	 * 
	 * @param titolo l'etichetta del pulsante
	 * @param origine la lista origine dello spostamento
	 * @param destinazioneModel la lista (modello) destinazione dello spostamento
	 * @param tutti se <code>true</code> il pulsante sposta tutti gli elementi nella lista origine,
	 * 				altrimenti solo quelli evidenziati
	 */
	public PulsanteSpostamento(String titolo, JList origine, DefaultListModel destinazioneModel, boolean tutti){
		// setta l'etichetta del pulsante
		super(titolo);
		// aggiunge il listener per il pulsante
		if(!tutti){// specializzazione pulsante
			this.addActionListener(new AzioneSpostaEvidenziazione(origine,(DefaultListModel) origine.getModel(),destinazioneModel));
		}else{
			this.addActionListener(new AzioneSpostaTutti(origine,(DefaultListModel) origine.getModel(),destinazioneModel));
		}
	}

}
