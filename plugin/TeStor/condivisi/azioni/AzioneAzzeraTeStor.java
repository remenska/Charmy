/*
 * Created on 18-apr-2005
 */
package plugin.TeStor.condivisi.azioni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import plugin.sequencediagram.data.ListaDS;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.interfaccia.ScrollSequence;

/**
 * Realizza l'azione di azzeramento del TeStor.
 * Riporta tutto allo stato iniziale.
 * 
 * @author Fabrizio Facchini
 */
public class AzioneAzzeraTeStor implements ActionListener {
	/**
	 * Il tab principale del plugin
	 */
	private Principale tabPrincipale;
	/**
	 * Costruisce un nuovo oggetto AzioneAzzeraTeStor.
	 * Si specifica il tab principale del plugin.
	 * 
	 * @param tabPrincipale il tab principale del plugin
	 */
	public AzioneAzzeraTeStor(Principale tabPrincipale) {
		super();
		this.tabPrincipale = tabPrincipale;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		tabPrincipale.setGenerazioneAvvenuta(false);
		tabPrincipale.getListaDispModel().removeAllElements();
		// la lista dei SD nel suo editor
		ListaDS lista = ((plugin.sequencediagram.data.PlugData)tabPrincipale.getPlugData().getPlugDataManager().getPlugData("charmy.plugin.sequence")).getListaDS();
		for(int i = 0; i < lista.size(); i++){
			tabPrincipale.getListaDispModel().addElement(new ScrollSequence(lista.get(i),tabPrincipale.getPD(),tabPrincipale.getPlugDataWin()));
		}
		tabPrincipale.getListaSelModel().removeAllElements();
	}

}
