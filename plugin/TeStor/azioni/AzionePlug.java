/*
 * Created on 25-set-2004
 */
package plugin.TeStor.azioni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;

import plugin.TeStor.Principale;
import plugin.TeStor.dialog.TeStorDialog;
/**
 * Visualizza il dialog principale del TeStor
 * 
 * @author Fabrizio Facchini
 */
public class AzionePlug implements ActionListener {

	/**
	 * Il dialog principale del TeStor
	 */
	private TeStorDialog dialog;
	/**
	 * La finestra di riferimento (quella di Charmy)
	 */
	private Container c;
	/**
	 * Il tab principale del TeStor
	 */
	private Principale tabPrincipale;
	/**
	 * Costruisce un nuovo oggetto TeStorDialog con riferimento alla finestra specificata
	 * (quella di Charmy) e al tab principale del TeStor
	 * 
	 * @param c la finestra che possiede il dialog
	 * @param tabPrincipale il tab principale del TeStor
	 */
	public AzionePlug(Container c, Principale tabPrincipale){
		this.c = c;
		this.dialog = /*new TeStorDialog(c, tabPrincipale)*/tabPrincipale.getDialog();
		this.tabPrincipale = tabPrincipale;
	}

	/**
	 * Visualizza il dialog al centro della finestra di Charmy
	 */
	public void actionPerformed(ActionEvent e) {
		// posiziona il dialog
		dialog.setLocationRelativeTo(c);
		// mostra delle informazioni solo se è selezionato il tab del TeStor
		if(tabPrincipale.isStatoAttivo()){
			tabPrincipale.setStatusBar("TEst Sequence generaTOR - Selezionare i SD dialog'interesse ed avviare il TeStor");
		}
		// imposta il flag dialogVisualizzato
		tabPrincipale.setDialogVisualizzato(true);
		// mostra il dialog
		dialog.show();
	}

}
