
package plugin.TeStor.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import plugin.TeStor.Principale;

/**
 * Gestisce la chiusura del dialog del TeStor
 * 
 * @author Fabrizio Facchini
 */
public class AzioneChiudiDialog implements ActionListener{
	/**
	 * Il dialog da chiudere
	 */
	private JDialog dialog;
	/**
	 * Il tab principale del TeStor
	 */
	private Principale tabPrincipale;
	/**
	 * Costruisce un nuovo oggetto AzioneChiudiDialog sul dialog specificato,
	 * in relazione al tab principale del TeStor
	 * 
	 * @param dialog il dialog da chiudere
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public AzioneChiudiDialog(JDialog dialog, Principale tabPrincipale){
		this.dialog = dialog;
		this.tabPrincipale = tabPrincipale;
	}

	/**
	 * Nasconde il dialog e imposta la barra di stato di Charmy
	 * 
	 * @param e un evento
	 */
	public void actionPerformed(ActionEvent e) {
		// nasconde il dialog
		dialog.hide();
		// imposta il flag dialogVisualizzato
		tabPrincipale.setDialogVisualizzato(false);
		// ripristina il messaggio nella barra di stato
		if(tabPrincipale.isStatoAttivo()){
			tabPrincipale.setStatusBar(tabPrincipale.getTabAttivo().getInfoStato());
		}
	}

}
