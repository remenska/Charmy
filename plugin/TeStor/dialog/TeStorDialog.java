
package plugin.TeStor.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;

import plugin.TeStor.Principale;
import plugin.TeStor.condivisi.azioni.AzioneAvviaTeStor;
import plugin.TeStor.condivisi.azioni.AzioneEvidenzia;
import plugin.TeStor.condivisi.azioni.AzioneVariazioneModello;
import plugin.TeStor.condivisi.interfaccia.ListaSD;
import plugin.TeStor.condivisi.interfaccia.PulsanteSpostamento;

/**
 * La finestra di dialogo del TeStor.
 * Questo dialog è un'alternativa al tab principale del TeStor per la selezione dei SD
 * e l'avvio dell'algoritmo TeStor. Può essere utilizzato da chi sa già quali SD selezionare
 * senza bisogno di visualizzarli, ed ha fretta di avviare il TeStor; bastano pochi click:
 * si selezionano i SD e si avvia il TeStor, senza bisogno di cambiare il tab di Charmy
 * sul quale si sta lavorando.
 * 
 * @author Fabrizio Facchini
 */
public class TeStorDialog extends JDialog{
	
	/**
	 * Il pulsante che seleziona i SD evidenziati
	 */
	private PulsanteSpostamento Seleziona;
	/** 
	 * Il pulsante che deseleziona i SD evidenziati (in Sel)
	 */
	private PulsanteSpostamento Deseleziona;
	/**
	 *  Il pulsante per selezionare tutti i SD disponibili 
	 */
	private PulsanteSpostamento SelTutti;
	/**
	 * Il pulsante per deselezionare tutti i SD selezionati
	 */
	private PulsanteSpostamento DeselTutti;
	/**
	 * Costruisce un nuovo oggetto TeStorDialog rispetto alla finestra specificata
	 * 
	 * @param owner la finestra di riferimento (quella di Charmy)
	 * @param tabPrincipale il tab principale del TeStor
	 * @throws HeadlessException
	 */	
	public TeStorDialog(Container owner, Principale tabPrincipale) throws HeadlessException {
	
		// crea il dialog con owner indicato, con titolo indicato, e modale (il valore true)
		super((Frame) owner,"Algoritmo TeStor", true);
	
		// DICHIARAZIONI *********
		// pannelli...
		JPanel Primario = new JPanel(new BorderLayout());
		JSplitPane Superiore = new JSplitPane();
		JPanel Inferiore = new JPanel(new BorderLayout());
		JPanel Selezioni = new JPanel(new BorderLayout());
		JPanel PulsSelez = new JPanel(new BorderLayout());
		JPanel PulsDesel = new JPanel(new BorderLayout());
		JPanel PannelloDisp = new JPanel(new BorderLayout());
		JPanel PannelloSelezioniSel = new JPanel(new BorderLayout());
		//JPanel PulsanteCentrale = new JPanel(new BorderLayout());
	
		//lista dei SD disponibili
		ListaSD Disp = new ListaSD((ListModel)tabPrincipale.getListaDispModel());// nota: senza casting è un DefaultListModel
		Disp.setToolTipText("SD disponibili");
		JScrollPane ScrollDisp = new JScrollPane(Disp);
		
		// lista dei SD selezionati
		ListaSD Sel = new ListaSD((ListModel)tabPrincipale.getListaSelModel());// nota: senza casting è un DefaultListModel
		Sel.setToolTipText("SD selezionati");
		JScrollPane ScrollSel = new JScrollPane(Sel);
		
		
		// il pulsante che avvia il TeStor sui SD nella lista Sel
		JButton Avvia = new JButton("AVVIA TeStor");
	
		//private JButton Annulla = new JButton("Annulla");
		
		//il pulsante di chiusura del dialog
		JButton Chiudi = new JButton("Chiudi");
		
		// CODICE *************			
		// nasconde il dialog come operazione di chiusura
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		// pone il dialog al centro della finestra che lo possiede
		this.setLocationRelativeTo(owner);
		
		// pone Primario come pannello principale del dialog
		this.setContentPane(Primario);
		// fissa un bordo vuoto di 10 pixel
		Primario.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


		// PULSANTI**************************************************************
		// listener per il pulsante di chiusura
		Chiudi.addActionListener(new AzioneChiudiDialog(this,tabPrincipale));
		Chiudi.setToolTipText("Chiude questa finestra di dialogo");
		// setta il tooltip per il pulsante di avvio
		Avvia.setToolTipText("Avvia la generazione dei casi di test sui SD nella lista di destra");
		Avvia.setEnabled(false);
		
		// aggiorna i pulsanti di spostamento per le rispettive liste di origine e destinazione
		Seleziona = new PulsanteSpostamento("Seleziona >>",Disp,(DefaultListModel)Sel.getModel(),false);
		Seleziona.setToolTipText("Seleziona per il TeStor i SD in evidenza nella lista a sinistra");
		Seleziona.setEnabled(false);
		Deseleziona = new PulsanteSpostamento("<< Deseleziona",Sel,(DefaultListModel)Disp.getModel(),false);
		Deseleziona.setToolTipText("Esclude i SD in evidenza nella lista a destra da quelli selezionati per il TeStor");
		Deseleziona.setEnabled(false);
		SelTutti = new PulsanteSpostamento("Sel. tutti >>",Disp,(DefaultListModel)Sel.getModel(),true);
		SelTutti.setToolTipText("Seleziona per il TeStor tutti i SD nella lista a sinistra");
		SelTutti.setEnabled(false);
		DeselTutti = new PulsanteSpostamento("<< Desel. tutti",Sel,(DefaultListModel)Disp.getModel(),true);
		DeselTutti.setToolTipText("Esclude dal TeStor tutti i SD nella lista a destra");
		DeselTutti.setEnabled(false);

		// listener per i pulsanti di selezione/deselezione
		Disp.addListSelectionListener(new AzioneEvidenzia(Disp,Seleziona, tabPrincipale));
		Disp.getModel().addListDataListener(new AzioneVariazioneModello(tabPrincipale.getListaDispModel(),SelTutti, tabPrincipale));
		Sel.addListSelectionListener(new AzioneEvidenzia(Sel,Deseleziona, tabPrincipale));
		Sel.getModel().addListDataListener(new AzioneVariazioneModello(tabPrincipale.getListaSelModel(),DeselTutti, tabPrincipale));
		
		// listener per il pulsante che avvia il TeStor
		Sel.getModel().addListDataListener(new AzioneVariazioneModello(tabPrincipale.getListaSelModel(),Avvia, tabPrincipale));
		Avvia.addActionListener(new AzioneAvviaTeStor(tabPrincipale.getListaSelModel(),tabPrincipale));//**********************************
	
		
		// PANNELLI**************************************************************
		// aggiunge due sotto-pannelli superiore e inferiore
		Primario.add(Superiore,BorderLayout.CENTER);
		Primario.add(Inferiore,BorderLayout.SOUTH);
		
		Selezioni.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		Selezioni.add(PulsSelez,BorderLayout.NORTH);
		Selezioni.add(PulsDesel,BorderLayout.SOUTH);
		
		PulsSelez.add(Seleziona,BorderLayout.NORTH);
		PulsSelez.add(SelTutti,BorderLayout.SOUTH);
		
		PulsDesel.add(Deseleziona,BorderLayout.NORTH);
		PulsDesel.add(DeselTutti,BorderLayout.SOUTH);
	
		Superiore.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Selezionare i Sequence Diagram per il TeStor"),BorderFactory.createEmptyBorder(10,10,10,10)));

		PannelloSelezioniSel.add(Selezioni,BorderLayout.WEST);
		PannelloSelezioniSel.add(ScrollSel,BorderLayout.CENTER);

		Superiore.setLeftComponent(ScrollDisp);
		Superiore.setRightComponent(PannelloSelezioniSel);

		//PulsanteCentrale.setBorder(BorderFactory.createEmptyBorder(0,40,0,40));
		//PulsanteCentrale.add(Annulla);
		Inferiore.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		Inferiore.add(Avvia,BorderLayout.WEST);
		//Inferiore.add(PulsanteCentrale,BorderLayout.CENTER);		
		Inferiore.add(Chiudi,BorderLayout.EAST);
			
		this.pack();

	}
	/**
	 * Abilita/disabilita tutti i pulsanti di spostamento
	 * 
	 * @param abilita valore di verità
	 */
	public void abilitaPulsSpost(boolean abilita){
		Seleziona.setEnabled(abilita);
		SelTutti.setEnabled(abilita);
		Deseleziona.setEnabled(abilita);
		DeselTutti.setEnabled(abilita);
	}

}
