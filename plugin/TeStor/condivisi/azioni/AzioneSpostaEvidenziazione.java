/*
 * Created on 25-set-2004
 */
package plugin.TeStor.condivisi.azioni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.DefaultListModel;
/**
 * Sposta da una lista all'altra gli elementi evidenziati nella prima lista.
 * La classe realizza una possibile modalità di selezione di elementi
 * da un certo insieme: si evidenziano gli elementi d'interesse nell'insieme (una lista),
 * si preme un pulsante e gli elementi evidenziati vengono spostati nell'insieme
 * di quelli selezionati (un'altra lista).
 * 
 * @author Fabrizio Facchini
 */
public class AzioneSpostaEvidenziazione implements ActionListener{
	
	/**
	 * La lista origine contenente l'informazione di quali elementi sono evidenziati
	 */
	protected JList listaOrigine;
	/**
	 * L'insieme a cui vanno sottratti gli elementi da spostare
	 */
	private DefaultListModel origine;
	/**
	 * L'insieme a cui vanno aggiunti gli elementi da spostare
	 */
	private DefaultListModel destinazione;
	
	/**
	 * Costruisce un nuovo oggetto AzioneSpostaEvidenziazione
	 * sulla base delle liste (origine e destinazione) specificate
	 * 
	 * @param listaOrigine la lista con gli elementi evidenziati
	 * @param origine il modello di lista di origine
	 * @param destinazione il modello di lista di destinazione
	 */
	public AzioneSpostaEvidenziazione(JList listaOrigine, DefaultListModel origine, DefaultListModel destinazione){
		this.listaOrigine = listaOrigine;
		this.origine = origine;
		this.destinazione = destinazione;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	/**
	 * Sposta gli elementi evidenziati nella lista origine: da questa alla lista destinazione.
	 * Con due cicli aggiunge gli elementi evidenziati alla destinazione e li rimuove dall'origine.
	 */
	public void actionPerformed(ActionEvent e) {
		// gli indici degli elementi nella evidenziazione
		int[] indiciEvidenziati = listaOrigine.getSelectedIndices();
		// il numero di elementi nella evidenziazione
		int n = indiciEvidenziati.length;
		
		for(int i=0; i<n; i++){ 
			// aggiunge in destinazione l'i-esimo elemento evidenziato in origine
			destinazione.addElement(origine.getElementAt(indiciEvidenziati[i]));
		}
		for(int i=n-1; i>=0; i--){
			// rimuove in origine l'i-esimo elemento (evidenziato in origine)
			origine.removeElementAt(indiciEvidenziati[i]);
			/*
			 * Nota: l'eliminazione avviene a ritroso per mantenere l'indice originale degli elementi
			 * da rimuovere. Diversamente gli indici cambierebbero, andando a rimuovere elementi sbagliati
			 */
		}
		
	}

}
