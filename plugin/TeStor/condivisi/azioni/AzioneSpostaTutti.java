/*
 * Created on 25-set-2004
 */
package plugin.TeStor.condivisi.azioni;

import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.DefaultListModel;
/**
 * Sposta tutti gli elementi di una lista verso un'altra.
 * La classe realizza una possibile modalità di selezione di elementi
 * da un certo insieme: si preme un pulsante e tutti gli elementi dell'insieme (una lista)
 * vengono spostati nell'insieme degli elementi selezionati (un'altra lista).
 * 
 * @author Fabrizio Facchini
 */
public class AzioneSpostaTutti extends AzioneSpostaEvidenziazione{

	/**
	 * Costruisce un nuovo oggetto AzioneSpostaTutti
	 * sulla base delle liste (origine e destinazione) specificate
	 * 
	 * @param listaOrigine la lista con gli elementi evidenziati
	 * @param origine il modello di lista di origine
	 * @param destinazione il modello di lista di destinazione
	 */
	public AzioneSpostaTutti(JList listaOrigine, DefaultListModel origine, DefaultListModel destinazione) {
		super(listaOrigine, origine, destinazione);
	}

	/**
	 * Sposta tutti gli elementi di una lista verso un'altra.
	 * Dapprima evidenzia tutti gli elementi nella lista e poi li sposta
	 * ricorrendo al metodo di spostamento della superclasse
	 * 
	 * @param e un evento
	 */
	public void actionPerformed(ActionEvent e) {
		// evidenzia tutti gli elementi
		listaOrigine.setSelectionInterval(0,listaOrigine.getModel().getSize()-1);
		super.actionPerformed(e);
		/* 
		 * Nota: non si può incorrere in IndexOutOfBounds in quanto il pulsante di
		 * spostamente non è abilitato se la lista d'origine è vuota
		 */
		
	}

}
