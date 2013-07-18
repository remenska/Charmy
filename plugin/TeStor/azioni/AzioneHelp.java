
package plugin.TeStor.azioni;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * Visualizza un messaggio con informazioni sul TeStor
 * 
 * @author Fabrizio Facchini
 */
public class AzioneHelp implements ActionListener{

	/**
	 * Visualizza un messaggio con informazioni sul TeStor
	 */
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null,
			"TEst Sequence generaTOR - Versione 1.0\n" +
			"Fabrizio Facchini - Università degli Studi dell'Aquila\n\n" +
			"L'algoritmo TeStor, a partire dai Sequence Diagram (SD) selezionati,\n" +
			"genera tutti i possibili casi di test che ognuno di questi implica.\n" +
			"Dato un SD, i casi di test generati sono tutti e soli i SD necessari\n" +
			"a coprire le State Machine delle componenti della Software Architecture,\n" +
			"e tra i possibili sono quelli rappresentanti i percorsi più brevi.",
			"Informazioni TeStor",JOptionPane.INFORMATION_MESSAGE);
	}

}
