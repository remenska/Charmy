
package plugin.TeStor.condivisi.azioni;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JList;
import javax.swing.JButton;

import plugin.TeStor.Principale;

/**
 * Gestisce l'abilitazione del pulsante di spostamento dei SD evidenziati
 * 
 * @author Fabrizio Facchini
 */
public class AzioneEvidenzia implements ListSelectionListener{	
	/**
	 * La lista dei SD (contenente quelli evidenziati)
	 */
	private JList lista;
	/**
	 * Il pulsante di spostamento dei SD evidenziati
	 */
	private JButton seleziona;
	/**
	 * Il tab principale del plugin
	 */
	private Principale tabPrincipale;

	/**
	 * Costruisce un nuovo oggetto AzioneEvidenzia in riferimento 
	 * alla lista, al pulsante ed al tab del plugin specificati.
	 * 
	 * @param lista la lista dei SD (contenente i SD evidenziati)
	 * @param seleziona il pulsante di spostamento dei SD evidenziati
	 * @param tabPrincipale il tab principale del plugin
	 */
	public AzioneEvidenzia(JList lista, JButton seleziona, Principale tabPrincipale){
		this.lista = lista;
		this.seleziona = seleziona;
		this.tabPrincipale = tabPrincipale;
	}

	/**
	 * Aggiusta l'abilitazione del pulsante di spostamento
	 * degli elementi evidenziati nella lista.
	 * Se la generazione dei casi di test non è avvenuta,
	 * il pulsante viene:<br>
	 * - abilitato se c'è almeno un SD evidenziato,<br>
	 * - disabilitato altrimenti.
	 */
	public void valueChanged(ListSelectionEvent e) {
		seleziona.setEnabled(!tabPrincipale.isGenerazioneAvvenuta() && lista.getSelectedIndex()!=-1);
	}
}
