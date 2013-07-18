/*
 * Created on 2-ott-2004
 */
package plugin.TeStor.condivisi.azioni;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

import plugin.TeStor.Principale;

/**
 * Gestisce l'abilitazione del pulsante di spostamento
 * di tutti i SD della lista specificata.
 * 
 * @author Fabrizio Facchini
 */
public class AzioneVariazioneModello implements ListDataListener{
	/**
	 * La lista dei SD che si sta monitorizzando
	 */
	private DefaultListModel listaModel;
	/**
	 * Il pulsante per spostare tutti i SD della lista
	 */
	private JButton spostaTutti;
	/**
	 * Il tab principale del plugin
	 */
	private Principale tabPrincipale;
	/**
	 * Costruisce un nuovo oggetto AzioneVariazioneModello
	 * sulla base della lista specificata, in relazione al pulsante specificato
	 * ed al tab principale del plugin (specificato).
	 * 
	 * @param listaModel la lista dei SD monitorizzata
	 * @param spostaTutti il pulsante per spostare tutti i SD della lista
	 * @param tabPrincipale il tab principale del plugin
	 */
	public AzioneVariazioneModello(DefaultListModel listaModel, JButton spostaTutti, Principale tabPrincipale){
		this.listaModel = listaModel;
		this.spostaTutti = spostaTutti;
		this.tabPrincipale = tabPrincipale;
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
	 */
	public void contentsChanged(ListDataEvent e) {
		
	}
	/**	
	 * Aggiusta l'abilitazione del pulsante di spostamento di tutti i SD della lista.
	 * Se non è avvenuta la generazione dei casi di test,
	 * il pulsante viene:<br>
	 * - abilitato se c'è almeno un SD nella lista,<br>
	 * - disabilitato altrimenti.
	 * 
	 * @param e un evento
	 */
	public void intervalAdded(ListDataEvent e) {
		spostaTutti.setEnabled(!tabPrincipale.isGenerazioneAvvenuta() && listaModel.getSize() > 0);
	}
	/**
	 * Aggiusta l'abilitazione del pulsante di spostamento di tutti i SD della lista.
	 * <br>Vedi {@link #intervalAdded(ListDataEvent)}
	 * 
	 * @param e un evento
	 */
	public void intervalRemoved(ListDataEvent e) {
		intervalAdded(e);
	}
}
