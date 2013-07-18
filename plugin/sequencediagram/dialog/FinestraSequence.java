/*   Charmy (CHecking Architectural Model consistencY)
 *   Copyright (C) 2004 Patrizio Pelliccione <pellicci@di.univaq.it>,
 *   Henry Muccini <muccini@di.univaq.it>, Paola Inverardi <inverard@di.univaq.it>. 
 *   Computer Science Department, University of L'Aquila. SEA Group. 
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package plugin.sequencediagram.dialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import plugin.sequencediagram.SequenceEditor;
import plugin.sequencediagram.SequenceWindow;
import plugin.sequencediagram.controllo.ControlloNomeSequence;
import plugin.sequencediagram.data.ListaClasse;
import plugin.sequencediagram.data.SequenceElement;
import plugin.topologydiagram.TopologyWindow;
import plugin.topologydiagram.data.ListaProcesso;

/** Classe per creare un Sequence Diagram oppure per 
 gestirne le propriet� (aggiunta o rimozione di un
 processo e modifica del nome). */

public class FinestraSequence extends JDialog implements ListSelectionListener
{	
	
	/** Modello per la lista dei processi disponibili all'uso. */
	private DefaultListModel listModelDisponibili;
	
	/** Lista dei processi disponibili all'uso. */
	private JList processiDisponibili;
	
	/** Barre di scorrimento per la lista dei processi
	 disponibili all'uso. */
	private JScrollPane pannelloProcessiDisponibili;
	
	/** Modello per la lista dei processi gi� in uso nel corrente Sequence Diagram. */
	private DefaultListModel listModelScelti;
	
	/** Lista dei processi gi� in uso nel corrente Sequence Diagram. */
	private JList processiScelti;
	
	/** Barre di scorrimento per la lista dei processi
	 gi� in uso nel corrente Sequence Diagram. */
	private JScrollPane pannelloProcessiScelti;
	
	/** Pulsante per aggiungere un processo al corrente Sequence Diagram. */
	private JButton plsAggiungi;
	
	/** Pulsante per aggiungere tutti i processi disponibili al Sequence Diagram. */
	private JButton plsAggiungiTutti;
	
	/** Pulsante per rimuovere un processo dal corrente Sequence Diagram. */
	private JButton plsRimuovi;
	
	/** Pulsante per rimuovere tutti i processi dal corrente Sequence Diagram. */
	private JButton plsRimuoviTutti;
	
	/** Pannello contenente i pulsanti per l'inserimento o la rimozione
	 di uno o pi� processi dal corrente Sequence Diagram. */
	private JPanel comandiPanel;
	
	/** Pannello contenente le liste dei processi scelti e 
	 disponibili e i pulsanti per la gestione. */	
	private JPanel listepannello;
	
	/** Campo di testo per impostare il nome del Sequence Diagram. */
	private JTextField controllonome;
	
	/** Pannello contenente il campo per impostare il nome del Sequence Diagram. */
	private JPanel nomepannello;
	
	/** Pulsante per lasciare la finestra senza apportare modifiche. */
	private JButton cancel;
	
	/** Pulsante per lasciare la finestra e apportare le modifiche. */
	private JButton ok;
	
	/** Pannello contente i pulsanti "Cancel" e "OK". */
	private JPanel buttonPanel;
	
	/** Gestione dei pulsanti per aggiungere o rimuovere 
	 uno o pi� processi dal corrente Sequence Diagram. */
	private GestioneComandi plsGestore;
	
	/** Riferimento al corrente Sequence Diagram. */
	private SequenceEditor rifSequenceEditor;
	
	/** Riferimento alla classe che gestisce 
	 le operazioni sui Sequence Diagram. */
	private SequenceWindow rifSequenceWindow;
	
	/** Lista dei processi gi� utilizzati dal corrente Sequence Diagram_
	 E' vuota nel caso di creazione di un nuovo Sequence Diagram. */
	private ListaClasse listaSeqEditorProcessi;
	
	/** Lista di tutti i processi utilizzabili in un Sequence Diagram. */
	private ListaProcesso listaTuttiProcessi;
	
	/** Elenco dei nomi dei processi gi� utilizzati nel corrente Sequence Diagram. */
	private LinkedList listaNomiProcessiScelti;
	
	/** Elenco dei nomi dei processi non ancora utilizzati nel corrente
	 Sequence Diagram, ma disponibili all'utilizzo. */
	private LinkedList listaNomiProcessiDisponibili;
	
	/** Uguale a 1 quando viene selezionata la lista dei processi disponibili,
	 uguale a 2 nel caso di selezione della lista dei processi gi� in uso. */
	private int listaSelezionata;
	
	/** Memorizza l'indice del processo selezionato. */
	private int indiceSelezionato;
	
	/** Memorizza il nome del processo selezionato. */
	private String elementoSelezionato;
	
	/** Variabile usata per gestire due diversi tipi di operazione_
	 ctrlOP = true: aggiunta di un nuovo sequence_
	 ctrlOP = false: modifica propriet� del sequence corrente. */
	private boolean ctrlOP; 	
	
	/** Pannello contenente listepannello e opzionipannello. */
	private JPanel pannellocentrale;
	
	/** Pannello contenente stringtimepannello e linetimepannello. */
	private JPanel opzionipannello;
	
	/** Pannello contenente la checkbox per la visualizzazione 
	 del 'time' nella fascia temporale. */	
	private JPanel stringtimepannello;
	
	/** Pannello contenente la checkbox per la visualizzazione 
	 delle linee nella fascia temporale. */
	private JPanel linetimepannello;
	
	/** CheckBox per abilitare/disabilitare la visualizzazione
	 del 'time' nella fascia temporale. */
	private JCheckBox stringTime;
	
	/** CheckBox per abilitare/disabilitare la visualizzazione
	 delle linee nella fascia temporale. */
	private JCheckBox lineTime;
	
	
	/**	Costruttore_
	 Se ctrlOperation = true viene aggiunto un nuovo sequence, altrimenti
	 vengono modificate le propriet� del sequence corrente. */    
	public FinestraSequence(Frame lframe, SequenceWindow sw, String titolo, boolean ctrlOperation)
	{
		// Finestra di dialogo di tipo modale.
		super(lframe,titolo,true);
		
		plsGestore = new GestioneComandi();
		ctrlOP = ctrlOperation;
		rifSequenceWindow = sw;
		listaTuttiProcessi = sw.getAllProcessList();
		if (ctrlOP){// Aggiunta nuovo sequence.				
			listaNomiProcessiScelti = new LinkedList();
			listaNomiProcessiDisponibili = listaTuttiProcessi.getListaAllGlobalName(); 
		}
		else{// Modifica propriet� sequence corrente.
			rifSequenceEditor = rifSequenceWindow.getCurrentSequenceEditor();		
			listaSeqEditorProcessi = rifSequenceEditor.getListaClasse();
			listaNomiProcessiScelti = listaSeqEditorProcessi.getListaAllClassName();
			listaNomiProcessiDisponibili = listaTuttiProcessi.getAllRemain(listaNomiProcessiScelti);		
		}
		
		listModelDisponibili = new DefaultListModel();
		for (int i=0;i<listaNomiProcessiDisponibili.size();i++){
			listModelDisponibili.addElement(listaNomiProcessiDisponibili.get(i));
		}			   
		processiDisponibili = new JList(listModelDisponibili);
		processiDisponibili.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		processiDisponibili.addListSelectionListener(this);
		pannelloProcessiDisponibili = new JScrollPane(processiDisponibili);
		pannelloProcessiDisponibili.setBorder(BorderFactory.createTitledBorder("Available Process:"));  
		pannelloProcessiDisponibili.setPreferredSize(new Dimension(150,200));
		
		listModelScelti = new DefaultListModel();
		for (int i=0;i<listaNomiProcessiScelti.size();i++){
			listModelScelti.addElement(listaNomiProcessiScelti.get(i));
		}			
		processiScelti = new JList(listModelScelti);
		processiScelti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		processiScelti.addListSelectionListener(this);
		pannelloProcessiScelti = new JScrollPane(processiScelti);
		pannelloProcessiScelti.setBorder(BorderFactory.createTitledBorder("Picked Process:"));
		pannelloProcessiScelti.setPreferredSize(new Dimension(150,200));
		
		JPanel comandiPanel = new JPanel();	
		comandiPanel.setLayout(new GridLayout(7,1));
		comandiPanel.add(new JLabel(""));
		comandiPanel.add(new JLabel(""));		
		plsAggiungi = new JButton("Add");
		plsAggiungi.addActionListener(plsGestore);
		comandiPanel.add(plsAggiungi);
		plsAggiungiTutti = new JButton("Add All");
		plsAggiungiTutti.addActionListener(plsGestore);
		comandiPanel.add(plsAggiungiTutti);
		plsRimuovi = new JButton("Remove");
		plsRimuovi.addActionListener(plsGestore);
		comandiPanel.add(plsRimuovi);
		plsRimuoviTutti = new JButton("Remove All");
		plsRimuoviTutti.addActionListener(plsGestore);
		comandiPanel.add(plsRimuoviTutti);
		comandiPanel.add(new JLabel(""));
		if (listaNomiProcessiScelti.size() == 0){
			plsRimuovi.setEnabled(false);
			plsRimuoviTutti.setEnabled(false);
		}
		if (listaNomiProcessiDisponibili.size() == 0){
			plsAggiungi.setEnabled(false);
			plsAggiungiTutti.setEnabled(false);
		}						
		
		listepannello = new JPanel();
		listepannello.setLayout(new BorderLayout());
		listepannello.add(pannelloProcessiDisponibili,BorderLayout.WEST);
		listepannello.add(comandiPanel,BorderLayout.CENTER);
		listepannello.add(pannelloProcessiScelti,BorderLayout.EAST);
		listepannello.setBorder(BorderFactory.createTitledBorder(""));       
		
		opzionipannello = new JPanel();
		opzionipannello.setLayout(new GridLayout(1,2));
		stringtimepannello = new JPanel();
		stringtimepannello.setLayout(new FlowLayout(FlowLayout.LEFT));
		stringTime = new JCheckBox();
		stringtimepannello.add(stringTime);
		stringtimepannello.add(new JLabel("Show Time's String "));
		linetimepannello = new JPanel();
		linetimepannello.setLayout(new FlowLayout(FlowLayout.RIGHT));
		lineTime = new JCheckBox();
		linetimepannello.add(lineTime);
		linetimepannello.add(new JLabel("Show Time's Line "));
		if (ctrlOP){
			stringTime.setSelected(false);
			lineTime.setSelected(false);
		}
		else{
			stringTime.setSelected(rifSequenceEditor.isStringTimeVisible());
			lineTime.setSelected(rifSequenceEditor.isLineTimeVisible());
		}
		opzionipannello.add(stringtimepannello);
		opzionipannello.add(linetimepannello);
		opzionipannello.setBorder(BorderFactory.createTitledBorder(""));
		
		pannellocentrale = new JPanel();
		pannellocentrale.setLayout(new BorderLayout());
		pannellocentrale.add(listepannello,BorderLayout.CENTER);
		pannellocentrale.add(opzionipannello,BorderLayout.SOUTH);
		
		nomepannello = new JPanel();
		nomepannello.setLayout(new GridLayout(1,2));
		nomepannello.add(new JLabel(" Sequence Name: "));
		if (ctrlOP){
			controllonome = new JTextField("Sequence"+(SequenceElement.getNumIstanze()+1),15);
		}
		else{
			controllonome = new JTextField(rifSequenceEditor.getName(),15);
		}
		nomepannello.add(controllonome);
		nomepannello.setBorder(BorderFactory.createTitledBorder("")); 
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout (new FlowLayout(FlowLayout.RIGHT));
		cancel = new JButton("Cancel");
		cancel.addActionListener(new buttonListener());
		cancel.setMargin(new Insets(1,1,1,1));
		buttonPanel.add(cancel);
		ok = new JButton("OK");
		ok.addActionListener(new buttonListener());
		ok.setMargin(new Insets(1,14,1,14));			   
		buttonPanel.add(ok);
		getRootPane().setDefaultButton(ok);
		
		getContentPane().add("North",nomepannello);
		getContentPane().add("Center", pannellocentrale);		
		getContentPane().add("South", buttonPanel);
		
		pack();
		
		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = SystemKit.getScreenSize();
		Dimension DialogSize = getSize();
		setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
		setVisible(true);
	}
	
	
	/** Metodo che gestisce la selezione/deselezione degli elementi nelle due liste */ 
	public void valueChanged(ListSelectionEvent e) 
	{	
		if (!e.getValueIsAdjusting()){ 
			JList theList = (JList) e.getSource();
			if (theList == processiDisponibili){  
				if (processiDisponibili.isSelectionEmpty()){       				
				}
				else 
				{
					indiceSelezionato = processiDisponibili.getSelectedIndex();
					elementoSelezionato = (String)(processiDisponibili.getSelectedValue());
					processiDisponibili.setSelectedIndex(indiceSelezionato);
					processiScelti.clearSelection();
					listaSelezionata = 1;          			
				}
			} 
			else if (theList == processiScelti){
				if (processiScelti.isSelectionEmpty()) {
				}
				else{
					indiceSelezionato = processiScelti.getSelectedIndex();
					elementoSelezionato = (String)(processiScelti.getSelectedValue());
					processiScelti.setSelectedIndex(indiceSelezionato);
					processiDisponibili.clearSelection(); 
					listaSelezionata = 2;
				}
			}
		}
		else{
		}			  
	} 
	
	
	/** Implementa le azioni relative alla pressione dei pulsanti OK
	 oppure CANCEL, con la chiusura della finestra di dialogo. */
	private final class buttonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			try{
				Object target = e.getSource();
				if (target.equals(cancel)){
					dispose();
				}
				else{	
					String str;
					if (ctrlOP){
						if ((new ControlloNomeSequence(rifSequenceWindow.getPlugData().getPlugDataManager())).checkOkforNew(controllonome.getText())){ 
							if (listModelScelti.size()>0){
								rifSequenceWindow.addSequence(controllonome.getText(),listModelScelti, 
										stringTime.isSelected(), lineTime.isSelected());                    			
								// ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
								rifSequenceWindow.getPlugData().getPlugDataManager().getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
								
								rifSequenceWindow.getSequenceToolBar().setEnabledMessageButtons(true);
								rifSequenceWindow.getSequenceToolBar().setEnabledSequenceButtons(true);
								dispose();
							}
							else{
								str = "You must select at least one process.";
								JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else{
							str =  	"You must change the name of the sequence. \n\n" + 
							"1. Two different sequences cannot have the same name. \n" +
							"2. The inserted name cannot starts with the prefix 'cp_'. \n";	
							JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE); 	                			
						}	
					}
					else{
						if ((new ControlloNomeSequence(rifSequenceWindow.getPlugData().getPlugDataManager()).checkOkforProperties(rifSequenceEditor.getName(),controllonome.getText()))){
							if (listModelScelti.size()>0){
								rifSequenceWindow.updateSequence(controllonome.getText(),listModelScelti,
										stringTime.isSelected(), lineTime.isSelected());
								
//								ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
								rifSequenceWindow.getPlugData().getPlugDataManager().getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
								
								rifSequenceWindow.getSequenceToolBar().setEnabledMessageButtons(true);
								rifSequenceWindow.getSequenceToolBar().setEnabledSequenceButtons(true);
								dispose();
							}
							else{
								str = "You must select at least one process.";
								JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else{
							str =  	"You must change the name of the sequence. \n\n" + 
							"1. Two different sequences cannot have the same name. \n" +
							"2. The inserted name cannot starts with the prefix 'cp_'. \n";	
							JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);                 			
						}	
					}
					dispose();
				}
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	
	/** Classe interna per la gestione dei pulsanti di aggiunta ed 
	 eliminazione dei processi dal corrente State Diagram. */
	private final class GestioneComandi extends AbstractAction 
	{
		
		GestioneComandi(){ 
			super();
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			int indice;
			int size;
			String item;
			JButton sorgente = (JButton)(e.getSource());
			if (sorgente == plsAggiungi) {// azioni per l'aggiunta dell'elemento
				if (listaSelezionata==1)  {
					listModelDisponibili.remove(indiceSelezionato);
					listModelScelti.addElement(elementoSelezionato);
					size = listModelDisponibili.getSize();
					if (size == 0) {// disabilita il pulsante plsAggiungi e plsAggiungiTutti
						plsAggiungi.setEnabled(false);
						plsAggiungiTutti.setEnabled(false);
					} 
					else {//Aggiusta la selezione.
						if (indiceSelezionato == listModelDisponibili.getSize()){// e' stato rimosso l'elemento in ultima posizione
							indiceSelezionato--;
						}
						processiDisponibili.setSelectedIndex(indiceSelezionato);   	
					}
					// abilito plsRimuovi e plsRimuoviTutti	
					plsRimuovi.setEnabled(true);
					plsRimuoviTutti.setEnabled(true);
				};	   		
			}
			else if (sorgente == plsRimuovi){// azioni per la rimozione dell'elemento
				if (listaSelezionata==2)  {
					listModelScelti.remove(indiceSelezionato);
					listModelDisponibili.addElement(elementoSelezionato);
					size = listModelScelti.getSize();
					if (size == 0) {// disabilita il pulsante aggiungi e aggiungiAll
						plsRimuovi.setEnabled(false);
						plsRimuoviTutti.setEnabled(false);
					} 
					else {//Aggiusta la selezione.
						if (indiceSelezionato == listModelScelti.getSize()){// e' stato rimosso l'elemento in ultima posizione
							indiceSelezionato--;
						}
						processiScelti.setSelectedIndex(indiceSelezionato);   
					};
					// abilito plsAggiungi e plsAggiungiTutti 
					plsAggiungi.setEnabled(true);
					plsAggiungiTutti.setEnabled(true);
				}	        			    
			} 
			else if (sorgente == plsAggiungiTutti){// azioni per l'aggiunta di tutti gli elementi possibili
				for (int j=0;j<listModelDisponibili.getSize();j++){
					item = (String)(listModelDisponibili.getElementAt(j));
					listModelScelti.addElement(item);
				}
				// disabilito plsAggiungi e plsAggiungiTutti
				plsAggiungi.setEnabled(false);
				plsAggiungiTutti.setEnabled(false);
				// abilito plsRimuovi e plsRimuoviTutti
				plsRimuovi.setEnabled(true);
				plsRimuoviTutti.setEnabled(true);		 
				processiDisponibili.clearSelection();
				// rimuovo tutti gli elementi dalla prima lista
				listModelDisponibili.removeAllElements(); 	  
				indiceSelezionato = 0;
				processiScelti.setSelectedIndex(indiceSelezionato);
				elementoSelezionato = (String)(processiScelti.getSelectedValue());
				listaSelezionata = 2; 			
			} 
			else if (sorgente == plsRimuoviTutti){// azioni per la rimozione di tutti gli elementi scelti
				for (int j=0;j<listModelScelti.getSize();j++){
					item = (String)(listModelScelti.getElementAt(j));
					listModelDisponibili.addElement(item);
				}
				// disabilito plsRimuovi e plsRimuoviTutti
				plsRimuovi.setEnabled(false);
				plsRimuoviTutti.setEnabled(false);
				// abilito plsAggiungi e plsAggiungiTutti
				plsAggiungi.setEnabled(true);
				plsAggiungiTutti.setEnabled(true);	      		 		 
				processiScelti.clearSelection();
				// rimuovo tutti gli elementi dalla seconda lista
				listModelScelti.removeAllElements();				 	 
				indiceSelezionato = 0;
				processiDisponibili.setSelectedIndex(indiceSelezionato);
				elementoSelezionato = (String)(processiDisponibili.getSelectedValue());
				listaSelezionata = 1; 			
			}						   
			processiDisponibili.repaint();
			processiScelti.repaint();					
		}
	}	       
	
}