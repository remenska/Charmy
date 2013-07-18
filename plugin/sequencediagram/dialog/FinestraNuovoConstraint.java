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
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import plugin.sequencediagram.SequenceWindow;
import plugin.sequencediagram.controllo.ControlloComboBoxSeqLink;
import plugin.sequencediagram.data.ElementoConstraint;
import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ListaConstraint;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.graph.GraficoConstraint;
import plugin.topologydiagram.TopologyWindow;
import core.internal.runtime.data.PlugDataManager;

/**
 *
 * @author  FLAMEL 2005
 */


/** La classe realizza un pannello di dialogo tramite il quale l'utente
	puï¿½ impostare le proprietï¿½ di un nuovo Constraint. */

public class FinestraNuovoConstraint 	extends JDialog 
	implements ListSelectionListener
{
	
	public PlugDataManager pdm;//ezio 2006
    /** Modello per la lista dei canali disponibili all'uso. */
    private DefaultListModel listModelDisponibili;

    /** Lista dei canali disponibili all'uso. */
    private JList canaliDisponibili;
    
    /**è una finestra che mi permette di scegliere la classe sender e receiver di un messaggio**/
    private FinestraSequenceConstClass finClass;

    /** Barre di scorrimento per la lista dei canali
            disponibili all'uso. */
    private JScrollPane pannelloCanaliDisponibili;

    /** Modello per la lista dei canali giï¿½ inseriti nel constraint. */
    private DefaultListModel listModelScelti;
    
    /** Modello per la lista dei canali giï¿½ inseriti nel constraint. */
    private DefaultListModel listModelScelti_print;

    /** Lista dei canali giï¿½ in uso nel corrente constraint. */
    private JList canaliScelti;

    /** Barre di scorrimento per la lista dei canali
            giï¿½ in uso nel corrente constraint. */
    private JScrollPane pannelloCanaliScelti;


    /** Pulsante per aggiungere un processo al corrente constraint. */
    private JButton plsAggiungi;

    /** Pulsante per rimuovere tutti i processi dal corrente constraint. */
    private JButton plsRimuoviTutti;

    /** Pannello contenente i pulsanti per l'inserimento o la rimozione
            di uno o piï¿½ canali dal corrente constraint. */
    private JPanel comandiPanel;

    /** Pannello contenente i tipi possibili di constraints. */
    private JPanel typePanel;

    /** Permette di selezionare se il constraints è past. */
    private JCheckBox pastclos_con;

    /** Permette di selezionare se il constraints è past. */
    private JCheckBox pastopen_con;
    
    /** Permette di selezionare se il constraints è present. */
    private JCheckBox pre_con;
    
    /** Permette di selezionare se il constraints è future. */
    private JCheckBox futclos_con;

    /** Permette di selezionare se il constraints è future. */
    private JCheckBox futopen_con;
    
    /** Permette di selezionare se il chain è non ordinato. */
    private JCheckBox unwanted;
    
    /** Permette di selezionare se il chain è non ordinato. */
    private JCheckBox bol_sel;
    
    /** Permette di selezionare se il chain è non ordinato. */
    private JCheckBox cha_sel;
    
    /** Scrollpane per l'editor booeano. */
    private JScrollPane scroll_bool;
    
    /** Scrollpane per l'editor di chain. */
    private JScrollPane scroll_ch;

    /** Pannello contenente le liste dei processi scelti e 
            disponibili e i pulsanti per la gestione. */	
    private JPanel listepannello;

   /** Gestione dei pulsanti per aggiungere o rimuovere 
            uno o piï¿½ processi dal corrente Sequence Diagram. */
    private GestioneComandi plsGestore;

    /** Lista di tutti i canali utilizzabili in un constraint. */
    private LinkedList listaTuttiCanali;

    /** Lista di tutti i canali utilizzabili in un constraint. */
    private ListaConstraint list_con;
    
    /** Elenco dei nomi dei canali giï¿½ utilizzati nel corrente constraint. */
    private LinkedList listaNomiCanaliScelti;

    /** Elenco dei nomi dei canali non ancora utilizzati nel corrente
            conatrint, ma disponibili all'utilizzo. */
    private LinkedList listaNomiCanaliDisponibili;

    /** Uguale a 1 quando viene selezionata la lista dei canali disponibili,
            uguale a 2 nel caso di selezione della lista dei canali giï¿½ in uso. */
    private int listaSelezionata;

    /** Memorizza l'indice del canale selezionato. */
    private int indiceSelezionato;

    /** Memorizza il nome del canale selezionato. */
    private String elementoSelezionato;

    /** Variabile usata per gestire due diversi tipi di operazione_
            ctrlOP = true: aggiunta di un nuovo constraint_
            ctrlOP = false: modifica proprietï¿½ del constraint corrente. */
    private boolean ctrlOP; 	

    /** Pannello contenente listepannello e opzionipannello. */
    private JPanel pannellocentrale;

    /** Riferimento alla classe che gestisce 
            le operazioni sui Sequence Diagram. */
    private SequenceWindow rifSequenceWindow;
    
    /** Riferimento al sequence di cui fa parte. */
    private SequenceElement rifSeq;
    
    /** Campo per visualizzare/impostare il nome del constraint. */
    private JTextArea nuovoConstraint_boolean;
    
    /** Campo per visualizzare/impostare il nome del constraint. */
    private JTextArea nuovoConstraint_chain;

    /** Pannello contenente la formula booleana. */
    private JPanel Pannelloboolean;

    /** Pannello contenente la formula booleana. */
    private JPanel Pannellochain;
    
    
    private JTabbedPane pannelloformule;
    
    /** Pulsante and. */
    private JButton andpulsante;
    
    /** Pulsante or. */
    private JButton orpulsante;
    
    /** Pulsante not. */
    private JButton notpulsante;
    
    private String link_constraintbol="";
    private String link_constraintch="";
    
    /** Pannello che contiene le 'CheckBox' per la selezione 
        dell'estremo superiore ed inferiore. */
    private JPanel estremi;

    /** Pannello contenente i pannelli 'Pannelloboolean' (a Nord)
        e 'estremi' (a Sud). */
    private JPanel constraintPannello;

    // Pulsanti di interazione con l'utente.
    /** Pulsante per inserire il constraint con le proprieta' specificate. */
    private JButton okpulsante;

    /** Pulsante per uscire senza inserire il constraint. */
    private JButton annullapulsante;
    // Fine pulsanti di interazione con l'utente.

    /** Pannello contenente i pulsanti 'okpulsante' e 'annullapulsante'. */
    private JPanel sudpannello;

    /** Pannello principale. */
    private JSplitPane principalepannello;

    /** Riferimento al constraint di cui si vogliono modificare le caratteristiche. */
    private ElementoConstraint constraint;
	
    /** Riferimento al grafico del processo. */	
    private GraficoConstraint constraintGrafico;
    
    /** Riferimento al Link a cui è applicato il constraint */
    private ElementoSeqLink Link;
    
    /** Inserimento nuovo Link */
    private boolean newConstraint;
        
    private Graphics2D g2;
    
    private Frame frame;
    private String txt;
	
    /**
    Costruttore_
        con        : riferimento all'oggetto di cui si vogliono modificare le caratteristiche_
        lframe    : riferimento al frame "proprietario" della finestra di dialogo_
        titolo    : titolo della finestra di dialogo.
    */
    public FinestraNuovoConstraint(ElementoConstraint con,
                                SequenceElement seq,
                                ElementoSeqLink link, 
                                Frame lframe, 
                                String titolo,
                                boolean ctrlOperation,
                                Graphics2D g2,
                                PlugDataManager pd)
    {
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        this.pdm=pd;//ezio 2006
        frame = lframe;
        rifSeq = seq;
        plsGestore = new GestioneComandi();
        ctrlOP = ctrlOperation;
        constraint = con;
        Link = link;
        {
            listModelScelti_print = new DefaultListModel();
            Vector vectorTuttiCanali = (new ControlloComboBoxSeqLink(pd)).getComboBoxList();
            listaTuttiCanali=new LinkedList();
        for(int i=0;i<vectorTuttiCanali.size();i++)                
            listaTuttiCanali.add(vectorTuttiCanali.elementAt(i));
        }
        if (ctrlOP)
        {  // Aggiunta nuovo constraint.				
            listaNomiCanaliScelti = new LinkedList();
            listaNomiCanaliDisponibili = listaTuttiCanali;
        }
        else
        {// Modifica proprietï¿½ constraint corrente.
            listaNomiCanaliScelti=con.getMsg();
            listaNomiCanaliDisponibili = disponibili(listaTuttiCanali,listaNomiCanaliScelti);
        }

        listModelDisponibili = new DefaultListModel();
        for (int i=0;i<listaNomiCanaliDisponibili.size();i++){
            listModelDisponibili.addElement(listaNomiCanaliDisponibili.get(i));
        }
        
        typePanel = new JPanel();
        typePanel.setLayout(new GridLayout(1,5));
        
        typePanel.add(new JLabel("Closed Past"));
        pastclos_con = new JCheckBox("");
        typePanel.add(pastclos_con);
        
        typePanel.add(new JLabel("Open Past"));
        pastopen_con = new JCheckBox("");        
        typePanel.add(pastopen_con);
        
        typePanel.add(new JLabel("Present"));
        pre_con = new JCheckBox("");        
        typePanel.add(pre_con);
        
        typePanel.add(new JLabel("Closed Future"));
        futclos_con = new JCheckBox("");
        typePanel.add(futclos_con);
        
        typePanel.add(new JLabel("Open Future"));
        futopen_con = new JCheckBox("");
        typePanel.add(futopen_con);
        
        pastopen_con.addActionListener(plsGestore);
        pastclos_con.addActionListener(plsGestore);
        pre_con.addActionListener(plsGestore);
        futopen_con.addActionListener(plsGestore);
        futclos_con.addActionListener(plsGestore);
        
        
        
        canaliDisponibili = new JList(listModelDisponibili);
        canaliDisponibili.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        canaliDisponibili.addListSelectionListener(this);
        pannelloCanaliDisponibili = new JScrollPane(canaliDisponibili);
        pannelloCanaliDisponibili.setBorder(BorderFactory.createTitledBorder("Available Channels:"));  
        pannelloCanaliDisponibili.setPreferredSize(new Dimension(150,200));

        listModelScelti = new DefaultListModel();
        for (int i=0;i<listaNomiCanaliScelti.size();i++){
            listModelScelti.addElement(listaNomiCanaliScelti.get(i));
        }

        canaliScelti = new JList(listModelScelti);
        canaliScelti.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        canaliScelti.addListSelectionListener(this);
        pannelloCanaliScelti = new JScrollPane(canaliScelti);
        pannelloCanaliScelti.setBorder(BorderFactory.createTitledBorder("Picked Channels:"));
        pannelloCanaliScelti.setPreferredSize(new Dimension(150,200));        
        JPanel comandiPanel = new JPanel();	
        comandiPanel.setLayout(new GridLayout(7,1));
        comandiPanel.add(new JLabel(""));
        comandiPanel.add(new JLabel(""));		
        plsAggiungi = new JButton("Add");
        plsAggiungi.addActionListener(plsGestore);
        comandiPanel.add(plsAggiungi);
        plsRimuoviTutti = new JButton("Remove All");
        plsRimuoviTutti.addActionListener(plsGestore);
        comandiPanel.add(plsRimuoviTutti);
        comandiPanel.add(new JLabel(""));
        						
        
        listepannello = new JPanel();
        listepannello.setLayout(new BorderLayout());
        listepannello.add(typePanel,BorderLayout.NORTH); 
        listepannello.add(pannelloCanaliDisponibili,BorderLayout.WEST);
        listepannello.add(comandiPanel,BorderLayout.CENTER);
        listepannello.add(pannelloCanaliScelti,BorderLayout.EAST);
        listepannello.setBorder(BorderFactory.createTitledBorder(""));       
       
        this.g2=g2;
        
        constraintGrafico = (GraficoConstraint)(con.getGrafico());
        
        // Costruzione del pannello relativo alle caratteristiche generali del processo.
        Pannelloboolean = new JPanel();
        Pannelloboolean.setLayout(new FlowLayout());
        Pannelloboolean.add(new JLabel("Boolean formula constraints:    "));
        nuovoConstraint_boolean =new JTextArea(constraint.getConstraintExpression(),1,1);
        nuovoConstraint_boolean.setEditable(false);
        scroll_bool = new JScrollPane(nuovoConstraint_boolean);
        scroll_bool.setPreferredSize(new Dimension(400,40));
        Pannelloboolean.add(scroll_bool);
        andpulsante = new JButton("&");
        orpulsante = new JButton("||");
        notpulsante = new JButton("!");
        andpulsante.addActionListener(plsGestore);
        orpulsante.addActionListener(plsGestore);
        notpulsante.addActionListener(plsGestore);
        
        Pannelloboolean.add(andpulsante);
        Pannelloboolean.add(orpulsante);
        Pannelloboolean.add(notpulsante);
        Pannelloboolean.setVisible(true); 
        Pannelloboolean.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent evt) {
                    DefTast(evt);
                }
            });
        
        Pannellochain = new JPanel();
        Pannellochain.setLayout(new FlowLayout());
        Pannellochain.add(new JLabel("Chain constraints:    "));
        nuovoConstraint_chain =new JTextArea(constraint.getConstraintExpression(),1,1);
        nuovoConstraint_chain.setEditable(false);
        scroll_ch = new JScrollPane(nuovoConstraint_chain);
        scroll_ch.setPreferredSize(new Dimension(400,40));
        Pannellochain.add(scroll_ch);
        Pannellochain.add(new JLabel("Unwanted chain constraints"));
        unwanted = new JCheckBox("");
        Pannellochain.add(unwanted);
        Pannellochain.setVisible(true);
        Pannellochain.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent evt) {
                    DefTast(evt);
                }
            });
      
        pannelloformule = new JTabbedPane();
        pannelloformule.addTab("Boolean formula constraints ",Pannelloboolean);
        pannelloformule.addTab("Chain constraints",Pannellochain);

        
        constraintPannello = new JPanel();
        constraintPannello.setLayout(new BorderLayout());
        constraintPannello.add(listepannello,BorderLayout.NORTH);
        constraintPannello.add(pannelloformule,BorderLayout.CENTER);
       	constraintPannello.setBorder(BorderFactory.createTitledBorder("Constraint Insert"));

        okpulsante = new JButton("OK");
        okpulsante.addActionListener(new pulsantiListener());
        

        annullapulsante = new JButton("Cancel");
        annullapulsante.addActionListener(new pulsantiListener());
        sudpannello = new JPanel();
        sudpannello.setLayout(new FlowLayout());
        sudpannello.add(annullapulsante);
        sudpannello.add(okpulsante);

        getRootPane().setDefaultButton(okpulsante);
        
        Init();
        
        principalepannello = new JSplitPane();
        principalepannello.setOrientation(JSplitPane.VERTICAL_SPLIT);

        principalepannello.setTopComponent(constraintPannello);
        principalepannello.setBottomComponent(sudpannello);
        getContentPane().add (principalepannello);
        pack();
        
        Toolkit SystemKit = Toolkit.getDefaultToolkit();
        Dimension ScreenSize = SystemKit.getScreenSize();
        Dimension DialogSize = getSize();
        setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
        setVisible(true);
        
        
    }

    private LinkedList disponibili(LinkedList all,LinkedList parte)
    {
        
        LinkedList disp=new LinkedList();
        if(parte!=null)
        {
            for(int i=0;i<all.size();i++)
                if(parte.indexOf(all.get(i))==-1)
                    disp.add(all.get(i));
            return disp;
        }
        else 
            return all;
    }

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     *
     */
    public void valueChanged(ListSelectionEvent e) 
    {
		if (!e.getValueIsAdjusting()){ 
			JList theList = (JList) e.getSource();
			if (theList == canaliDisponibili){
                            if(Pannelloboolean.isShowing())
                            {
                                if(canaliDisponibili.getSelectedIndex()!=-1 
                                        && !andpulsante.isEnabled()
                                        && !orpulsante.isEnabled())                        
                                    plsAggiungi.setEnabled(true);                        
                                else
                                    plsAggiungi.setEnabled(false);
                            }
                            else
                                plsAggiungi.setEnabled(true); 
                            if (canaliDisponibili.isSelectionEmpty()){
                            }
                            else{
                                indiceSelezionato = canaliDisponibili.getSelectedIndex();
                                elementoSelezionato = (String)(canaliDisponibili.getSelectedValue());
                                canaliDisponibili.setSelectedIndex(indiceSelezionato);
                                canaliScelti.clearSelection();
                                listaSelezionata = 1;
                            }
	   		} 
	   		else 
                            if (theList == canaliScelti)
                            {
                                if (canaliScelti.isSelectionEmpty()){
					}
                                else{                               
                                    indiceSelezionato = canaliScelti.getSelectedIndex();
                                    elementoSelezionato = (String)(canaliScelti.getSelectedValue());
                                    canaliScelti.setSelectedIndex(indiceSelezionato);
                                    canaliDisponibili.clearSelection(); 
                                    listaSelezionata = 2;
                                }
                            }
		}
		else{
		}			                  
    }        

    /** Implementa le azioni relative alla pressione dei pulsanti OK
    	oppure CANCEL, con la chiusura della finestra di dialogo. */
    private final class pulsantiListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try{
       	        Object target = e.getSource();                
                if (target.equals(annullapulsante)){
                    constraint.setInserisciConstraint(false);
                    dispose();
	        	}
                else{
                    if((nuovoConstraint_boolean.getText().length()==0) && (nuovoConstraint_chain.getText().length()==0) 
                        && (listModelScelti.size()==0))
                    {                       
                        if(ctrlOP){
                            constraint.setInserisciConstraint(false);                       
                            dispose();
                        }
                        else{
                            Link.removeConstraint(constraint);
                            rifSeq.getListaConstraint().removeElement(constraint);
                            constraint=null;
                            constraintGrafico=null;
                            dispose();
                        }
                    } 
                    else{   
                        
                        
                        if(pre_con.isSelected() && Pannelloboolean.isShowing())
                            constraint.setType(0);
                        else if(pastclos_con.isSelected()&& Pannelloboolean.isShowing() )
                            constraint.setType(-1);
                        else if(pastopen_con.isSelected()&& Pannelloboolean.isShowing())
                            constraint.setType(-2);
                        else if(futclos_con.isSelected()&& Pannelloboolean.isShowing())
                            constraint.setType(1);
                        else if(futopen_con.isSelected()&& Pannelloboolean.isShowing())
                            constraint.setType(2);
                        else if(pastclos_con.isSelected() && Pannellochain.isShowing() && unwanted.isSelected())
                                constraint.setType(7);
                        else if(pastopen_con.isSelected() && Pannellochain.isShowing()&& unwanted.isSelected())
                                constraint.setType(8);
                        else if(futclos_con.isSelected() && Pannellochain.isShowing()&& unwanted.isSelected())
                                constraint.setType(9);
                        else if(futopen_con.isSelected() && Pannellochain.isShowing()&& unwanted.isSelected())
                                constraint.setType(10);
                        else if(pastclos_con.isSelected() && Pannellochain.isShowing())
                                constraint.setType(3);
                        else if(pastopen_con.isSelected() && Pannellochain.isShowing())
                                constraint.setType(4);
                        else if(futclos_con.isSelected() && Pannellochain.isShowing())
                                constraint.setType(5);
                        else if(futopen_con.isSelected() && Pannellochain.isShowing())
                                constraint.setType(6);
                        
                        constraint.setMsg(listModelScelti); 
                        constraint.setInserisciConstraint(true);
                        
                        if((nuovoConstraint_chain.getText().length()==0))
                        {        
                            constraint.setConstraintExpression(nuovoConstraint_boolean.getText());  
                            constraint.setLabel(nuovoConstraint_boolean.getText());
                            
                        }
                        else 
                        {
                            constraint.setMsgForChain(listModelScelti_print);
                            constraint.setConstraintExpression(nuovoConstraint_chain.getText());
                            constraint.setLabel(nuovoConstraint_chain.getText());
                        }
                        

                                             
                        if(ctrlOP){
                            rifSeq.getListaConstraint().addElement(constraint);
                            Link.addConstraint(constraint);
                          //ezio 2006  core.internal.plugin.file.FileManager.setModificata(true);
                            
                           pdm.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);//ezio 2006
                         //ezio 2006   SequenceEditor.incConstraintInstanceNumber();
                        }
                        constraint.updateConstraintPosizione();
                        constraint.paintConstraint(g2); 
                        dispose();
                    }
                }
                dispose();				
            }
            catch (Exception ex){
            	ex.printStackTrace();
	    	}
        }
    }
    
    
    
	/** Classe interna per la gestione dei pulsanti di aggiunta ed 
		eliminazione dei processi dal corrente State Diagram. */
	private final class GestioneComandi extends AbstractAction{

		GestioneComandi(){ 
			super();
		}
	
		public void actionPerformed(ActionEvent e){
                    int indice;
                    int size;
                    String item;
                    Object target = e.getSource();    
        
	 		if (target.equals(plsAggiungi)){
                            // azioni per l'aggiunta dell'elemento  
                               txt = elementoSelezionato;
                               
                               if(Pannelloboolean.isShowing())
                               {
                                   String str =nuovoConstraint_boolean.getText();
                                   if(str.length()!=0)
                                       if(str.substring(str.length()-1).equals("!"))
                                       {
                                        txt = "!"+txt;
                                        str = nuovoConstraint_boolean.getText().substring(0, str.length()-1);
                                        nuovoConstraint_boolean.setText(str);
                                       }
                                   finClass = new FinestraSequenceConstClass(constraint,rifSeq,"Constraint properties",frame,txt,nuovoConstraint_boolean,plsAggiungi,pdm.getFileManager());//ezio 2006 
                                   
                               }
                                else
                                {
                                   String str =nuovoConstraint_chain.getText(); 
                                   finClass = new FinestraSequenceConstClass(constraint,rifSeq,"Constraint properties",frame,txt,nuovoConstraint_chain,plsAggiungi,pdm.getFileManager()); 
                                   if(!nuovoConstraint_chain.getText().equals(str)) 
                                   {
                                        AggStr();                                        
                                        AggMsg();
                                   }
                                   
                                }
                               CntrlInt();
                               
                                if (listaSelezionata==1 && !plsAggiungi.isEnabled()){
                                    listModelDisponibili.remove(indiceSelezionato);
                                    listModelScelti.addElement(elementoSelezionato);
                                    if(Pannelloboolean.isShowing())                                   
                                    {
                                        plsAggiungi.setEnabled(false);
                                        plsRimuoviTutti.setEnabled(true);
                                        notpulsante.setEnabled(false);
                                        if(listModelDisponibili.isEmpty())
                                        {
                                            andpulsante.setEnabled(false);
                                            orpulsante.setEnabled(false);
                                        }
                                        else
                                        {
                                            orpulsante.setEnabled(true);
                                            andpulsante.setEnabled(true);
                                        }
                                        
                                    }                                        
                                    else
                                    {
                                        plsAggiungi.setEnabled(true);
                                        plsRimuoviTutti.setEnabled(true);
                                    
                                    }                                    
                                    size = listModelDisponibili.getSize();
                                    if (size == 0){
                                        // disabilita il pulsante plsAggiungi e plsAggiungiTutti
                                        plsAggiungi.setEnabled(false);
                                    } 
                                    else
                                    {//Aggiusta la selezione.
                                        if (indiceSelezionato == listModelDisponibili.getSize()){
                                        // e' stato rimosso l'elemento in ultima posizione
                                            indiceSelezionato--;
                                        }
                                    canaliDisponibili.setSelectedIndex(indiceSelezionato);   	
                                    }
                                    // abilito plsRimuovi e plsRimuoviTutti	
                                
                                
                               }
			}	 
                            else 
                                if (target.equals(plsRimuoviTutti)){
                                    // azioni per la rimozione di tutti gli elementi scelti
                                    for (int j=0;j<listModelScelti.getSize();j++)
                                    {
                                        item = (String)(listModelScelti.getElementAt(j));
                                        listModelDisponibili.addElement(item);
                                    }
                                    // disabilito plsRimuovi e plsRimuoviTutti
                                  
                                    plsRimuoviTutti.setEnabled(false);
                                    // abilito plsAggiungi e plsAggiungiTutti
                                    plsAggiungi.setEnabled(true);
                                    canaliScelti.clearSelection();
                                    // rimuovo tutti gli elementi dalla seconda lista
                                    listModelScelti.removeAllElements();				 	 
                                    indiceSelezionato = 0;
                                    canaliDisponibili.setSelectedIndex(indiceSelezionato);
                                    elementoSelezionato = (String)(canaliDisponibili.getSelectedValue());
                                    listaSelezionata = 1; 
                                    setText();
                                    nuovoConstraint_boolean.setText(null);
                                    okpulsante.setEnabled(true);
                                    orpulsante.setEnabled(false);
                                    andpulsante.setEnabled(false);
                                    notpulsante.setEnabled(true);
                                }
                            else                                
                                if(target.equals(pre_con)){
                                    if(pre_con.isSelected()){
                                        pastclos_con.setEnabled(false);                                        
                                        pastopen_con.setEnabled(false);
                                        futopen_con.setEnabled(false);
                                        futclos_con.setEnabled(false);
                                        pannelloformule.setEnabledAt(1, false);
                                    }
                                    else{
                                        pastclos_con.setEnabled(true);                                        
                                        pastopen_con.setEnabled(true);
                                        futopen_con.setEnabled(true);
                                        futclos_con.setEnabled(true);
                                        if(!Link.isSimultaneous() && 
                                            !(Link.isConstraintFutClo()
                                                || Link.isConstraintFutOp()
                                                || Link.isConstraintPastClo()
                                                || Link.isConstraintPastOp()))
                                            pannelloformule.setEnabledAt(1, true);
                                    }
                                    CntrlInt();
                                }
                            
                            else 
                                if(target.equals(futopen_con)){
                                    if(futopen_con.isSelected()){
                                        pre_con.setEnabled(false);
                                        futclos_con.setEnabled(false);
                                        pastclos_con.setEnabled(false);
                                        pastopen_con.setEnabled(false);
                                    }
                                    else{  
                                        if(nuovoConstraint_chain.isShowing())
                                            pre_con.setEnabled(false);
                                        else
                                            pre_con.setEnabled(true);
                                        futclos_con.setEnabled(true);
                                        pastclos_con.setEnabled(true);
                                        pastopen_con.setEnabled(true);
                                    }
                                    CntrlInt();
                                }
                            else 
                                if(target.equals(futclos_con)){
                                    if(futclos_con.isSelected()){
                                        pre_con.setEnabled(false);
                                        futopen_con.setEnabled(false);
                                        pastclos_con.setEnabled(false);
                                        pastopen_con.setEnabled(false);
                                    }
                                    else{
                                        if(nuovoConstraint_chain.isShowing())
                                            pre_con.setEnabled(false);
                                        else
                                            pre_con.setEnabled(true);
                                        futopen_con.setEnabled(true);
                                        pastclos_con.setEnabled(true);
                                        pastopen_con.setEnabled(true);
                                    }
                                    CntrlInt();
                                }
                            else 
                                if(target.equals(pastclos_con)){
                                    if(pastclos_con.isSelected()){
                                        pre_con.setEnabled(false);
                                        futopen_con.setEnabled(false);
                                        futclos_con.setEnabled(false);
                                        pastopen_con.setEnabled(false);
                                    }
                                    else{
                                        if(nuovoConstraint_chain.isShowing())
                                            pre_con.setEnabled(false);
                                        else
                                            pre_con.setEnabled(true);
                                        futopen_con.setEnabled(true);
                                        futclos_con.setEnabled(true);
                                        pastopen_con.setEnabled(true);
                                    }
                                    CntrlInt();
                                }  
                           else 
                                if(target.equals(pastopen_con)){
                                    if(pastopen_con.isSelected()){
                                        pre_con.setEnabled(false);
                                        futopen_con.setEnabled(false);
                                        futclos_con.setEnabled(false);
                                        pastclos_con.setEnabled(false);
                                    }
                                    else{
                                        if(nuovoConstraint_chain.isShowing())
                                            pre_con.setEnabled(false);
                                        else
                                            pre_con.setEnabled(true);
                                        futopen_con.setEnabled(true);
                                        futclos_con.setEnabled(true);
                                        pastclos_con.setEnabled(true);
                                    }
                                    CntrlInt();
                                }
                          else 
                                if(target.equals(andpulsante)){
                                    nuovoConstraint_boolean.append(" && ");
                                    orpulsante.setEnabled(false);
                                    andpulsante.setEnabled(false);
                                    notpulsante.setEnabled(true);
                                    okpulsante.setEnabled(false);
                                    plsAggiungi.setEnabled(true);
                                } 
                          else 
                                if(target.equals(orpulsante)){
                                    nuovoConstraint_boolean.append(" || ");
                                    andpulsante.setEnabled(false);
                                    orpulsante.setEnabled(false);
                                    notpulsante.setEnabled(true);
                                    okpulsante.setEnabled(false);
                                    plsAggiungi.setEnabled(true);
                                    
                                }   
                          else 
                                if(target.equals(notpulsante)){
                                    nuovoConstraint_boolean.append("!");
                                    orpulsante.setEnabled(false);
                                    andpulsante.setEnabled(false);
                                    notpulsante.setEnabled(false);
                                    okpulsante.setEnabled(false);
                                }

                    if(Pannelloboolean.isShowing() && listModelScelti.size()==1)
                        okpulsante.setEnabled(false);

                    
                    canaliDisponibili.repaint();
                    canaliScelti.repaint();
                        
                }
	}
        
        /**Aggiorna la stringa in maniera opportuna aggiungendoci virgole e parentesi**/
        public void AggStr()
        {
            String str = nuovoConstraint_chain.getText();
            String app = str;
            listModelScelti_print = new DefaultListModel();
            if(str.length()==0)
                return;            
            int punti=0;
            for(int i=0;i<str.length();i++)
            {
                if(str.charAt(i)=='.')
                    punti++;
            }
            if(punti==2)
            {
                str = "("+str+")";
                app = str;
            }
            else
            {
               str = str.replace(')',',');
               str = str+")";
               app = str;
               punti=0;
            }
                     
            nuovoConstraint_chain.setText(str);
        }
        
        /**fà un parsing delle formule applicate alle catene **/
        public void AggMsg()
        {
            String app = nuovoConstraint_chain.getText();  
            String elemento = "" ;
            int k=1; 
            for(int i =0;i<app.length();i++)
            {                
               if(app.charAt(i) == ',')
                {
                  elemento = app.substring(k,i); 
                  listModelScelti_print.addElement("("+elemento+")");
                  k = i+1;
                }
               else
                   if(app.charAt(i) == ')')
                   {
                      elemento = app.substring(k,i); 
                      listModelScelti_print.addElement("("+elemento+")"); 
                      return;
                   }
            }
           
        }
        
        
        /** Forma una stringa concatenando i nomi presenti nella listModelScelti**/
        public void setText()
        {
            link_constraintch = "";
            for(int i=0; i<listModelScelti.size();i++){
                String app;
                app = (String) listModelScelti.get(i);
                link_constraintch = link_constraintch.concat(app);
              } 
             nuovoConstraint_chain.setText(link_constraintch);
        }
        
        /**Funzione di controllo integrità**/
        public void Init()
        {
            if(Link.hasConstraint())
            {
                if(Link.isConstraintChCloFut()
                    || Link.isConstraintChCloPast()
                    || Link.isConstraintChOpFut()
                    || Link.isConstraintChOpPast()
                    || Link.isConstraintUnCloFut()
                    || Link.isConstraintUnCloPast()
                    || Link.isConstraintUnOpFut()
                    || Link.isConstraintUnOpPast())
                {
                    pannelloformule.setSelectedIndex(1);
                    pannelloformule.setEnabledAt(0, false); 
                }
                else
                {
                    pannelloformule.setEnabledAt(1, false);
                }
                
                if(Link.isConstraintFutClo()|| Link.isConstraintChCloFut() || Link.isConstraintUnCloFut())
                    futclos_con.setVisible(false);                  
                if(Link.isConstraintPastOp() || Link.isConstraintChOpPast() || Link.isConstraintUnOpPast())
                    pastopen_con.setVisible(false);                    
                if(Link.isConstraintFutOp() || Link.isConstraintChOpFut()|| Link.isConstraintUnOpFut())
                    futopen_con.setVisible(false);                 
                if(Link.isConstraintPastClo() || Link.isConstraintChCloPast() || Link.isConstraintUnCloPast())
                    pastclos_con.setVisible(false);
                if(Link.isConstraintPre())
                    pre_con.setVisible(false);
            }     
                    
            if(Link.isLoop_op())
            {
                ElementoLoop loop =(ElementoLoop) rifSeq.getListaLoop().getElementId(Link.getId_loop());
                if(Link.equals(loop.getfirst_link()))
                {
                    pastclos_con.setVisible(false);
                    pastopen_con.setVisible(false);
                }
                else
                    if(Link.equals(loop.getlast_link()))
                    {
                        futclos_con.setVisible(false);
                        futopen_con.setVisible(false); 
                    }

            }
            if(Link.isParallel())
            {
                pastclos_con.setVisible(false);
                pastopen_con.setVisible(false);
                futclos_con.setVisible(false);
                futopen_con.setVisible(false);
                pannelloformule.setEnabledAt(1, false); 
                
            }
            if(Link.isSimultaneous())
            {
               pannelloformule.setEnabledAt(1, false);
            }
            
            if(Link.getPrec()!=null)
                if(Link.getPrec().hasConstraint())
                    if(Link.getPrec().isConstraintChCloFut()
                       ||Link.getPrec().isConstraintChOpFut()
                       ||Link.getPrec().isConstraintUnCloFut()
                       ||Link.getPrec().isConstraintUnOpFut()
                       ||Link.getPrec().isConstraintFutClo()
                       ||Link.getPrec().isConstraintFutOp())
                    {
                        pastopen_con.setVisible(false);
                        pastclos_con.setVisible(false);
                    }
            
            for(int i=0;i<rifSeq.getListaSeqLink().size();i++)
            {
                ElementoSeqLink link =(ElementoSeqLink) rifSeq.getListaSeqLink().getListLinkSequence().get(i);
                if(link.hasConstraint())
                    if(link.isConstraintPastClo() || link.isConstraintPastOp() || link.isConstraintChCloPast() 
                        || link.isConstraintChOpPast() || link.isConstraintUnCloPast() || link.isConstraintUnOpPast())
                    {
                        if(link.getPrec()!=null && !link.equals(Link))
                            if(Link.equals(link.getPrec()))
                            {
                                futopen_con.setVisible(false);
                                futclos_con.setVisible(false);
                            }
                    }  
            }
            
            if (listaNomiCanaliScelti.size() == 0){
                plsRimuoviTutti.setEnabled(false);
            }
            if (listaNomiCanaliDisponibili.size() == 0){
                    plsAggiungi.setEnabled(false);
            }
             if(listModelScelti.size()!=0)
                okpulsante.setEnabled(true);
            else
                okpulsante.setEnabled(false);

            if(nuovoConstraint_boolean.getText().equals(""))
            {
               andpulsante.setEnabled(false); 
               orpulsante.setEnabled(false);          
            }
            else
            {
               andpulsante.setEnabled(true); 
               orpulsante.setEnabled(true);
               notpulsante.setEnabled(false);
            }
            
             
             if(constraint.isChCloFut() || constraint.isChOpFut() || constraint.isChCloPast() || constraint.isChOpPast()
                || constraint.isUnCloFut() || constraint.isUnOpFut() || constraint.isUnCloPast() || constraint.isUnOpPast())
             {
                pannelloformule.setSelectedIndex(1);        
             }
            else
             if(constraint.isPastOp()||constraint.isFutOp()||constraint.isPastClo()||constraint.isFutClo())                
                pannelloformule.setSelectedIndex(0); 
            
            if(constraint.isUnCloFut()|| constraint.isUnOpFut() || constraint.isUnCloPast() || constraint.isUnOpPast())
                unwanted.setSelected(true);
            if(constraint.isFutClo() || constraint.isChCloFut() || constraint.isUnCloFut())
            {
                futclos_con.setVisible(true);
                futclos_con.setSelected(true); 
                futopen_con.setEnabled(false);
                pastclos_con.setEnabled(false);
                pastopen_con.setEnabled(false);
                pre_con.setEnabled(false);
                
            }
            else           
                if(constraint.isPastOp() || constraint.isChOpPast() || constraint.isUnOpPast())
                {
                    pastopen_con.setVisible(true);
                    pastopen_con.setSelected(true);
                    futopen_con.setEnabled(false);
                    pastclos_con.setEnabled(false);
                    futclos_con.setEnabled(false);
                    pre_con.setEnabled(false);
                }
            else          
                if(constraint.isFutOp() || constraint.isChOpFut() || constraint.isUnOpFut())
                {
                    futopen_con.setVisible(true);
                    futopen_con.setSelected(true); 
                    futclos_con.setEnabled(false);
                    pastclos_con.setEnabled(false);
                    pastopen_con.setEnabled(false);
                    pre_con.setEnabled(false);
                }
            else           
                if(constraint.isPastClo() || constraint.isChCloPast() || constraint.isUnCloPast())
                {
                    pastclos_con.setVisible(true);
                    pastclos_con.setSelected(true);
                    futopen_con.setEnabled(false);
                    futclos_con.setEnabled(false);
                    pastopen_con.setEnabled(false);
                    pre_con.setEnabled(false);
                }
            else            
                if(constraint.isPre()){
                	///ezio 2006
                     pre_con.setVisible(true);
                     ///
                     pre_con.setSelected(true);
                     pastopen_con.setEnabled(false);
                     futopen_con.setEnabled(false);
                     pastclos_con.setEnabled(false);
                     futclos_con.setEnabled(false);
                     unwanted.setSelected(false);
                     pannelloformule.setEnabledAt(1, false);
                 }
            if(Link.isStrict())
            {
                pannelloformule.setEnabledAt(1, false);
                pastopen_con.setVisible(false);
                futopen_con.setVisible(false);
                pastclos_con.setVisible(false);
                futclos_con.setVisible(false);
            }
            plsAggiungi.setEnabled(false);
            if(Link.getRegReqFail()==5)
            {
                futopen_con.setVisible(false);
                futclos_con.setVisible(false);
            }

        }
        
        /**funzione che aggiorna la visibilità del pulsante ok**/
        public void CntrlInt(){
            
            if(pastopen_con.isSelected() || pastclos_con.isSelected() ||futopen_con.isSelected()
                || futclos_con.isSelected() || pre_con.isSelected())
            {
                if(nuovoConstraint_boolean.getText() != null || nuovoConstraint_chain.getText() != null )
                    if(nuovoConstraint_boolean.getText()!="!")
                        okpulsante.setEnabled(true);
                    else
                        okpulsante.setEnabled(false); 
                else
                    okpulsante.setEnabled(false);                
            }
            else
                okpulsante.setEnabled(false);
            
        }
        
         /**setta la tastiera in maniera opportuna dopo la scelta dell'inserimento 
          o di una fomula booleana o di una catena**/
         private void DefTast(java.awt.event.ComponentEvent evt) {  
             String item;
             Object target =evt.getSource();    
             if(target.equals(Pannelloboolean))
             {                 
                 nuovoConstraint_chain.setText("");
                 orpulsante.setEnabled(false);
                 andpulsante.setEnabled(false);
                 notpulsante.setEnabled(true);
                 if(!(constraint.isFutClo()
                  || constraint.isPastClo()
                  || constraint.isFutOp()
                  || constraint.isPastOp()))
                    pre_con.setEnabled(true);
                 /*
                 // azioni per la rimozione di tutti gli elementi scelti
                for (int j=0;j<listModelScelti.getSize();j++){
                        item = (String)(listModelScelti.getElementAt(j));
                        listModelDisponibili.addElement(item);
                }
                // disabilito plsRimuovi e plsRimuoviTutti
                plsRimuovi.setEnabled(false);
                plsRimuoviTutti.setEnabled(false);
                // abilito plsAggiungi e plsAggiungiTutti
                plsAggiungi.setEnabled(true);      		 		 
                canaliScelti.clearSelection();
                // rimuovo tutti gli elementi dalla seconda lista
                listModelScelti.removeAllElements();				 	 
                indiceSelezionato = 0;
                canaliDisponibili.setSelectedIndex(indiceSelezionato);
                elementoSelezionato = (String)(canaliDisponibili.getSelectedValue());
                listaSelezionata = 1; 
                  */
                 
             }
             else
                 if(target.equals(Pannellochain))          //per pannellochain
                 {
                      // azioni per la rimozione di tutti gli elementi scelti
                  /*  for (int j=0;j<listModelScelti.getSize();j++){
                            item = (String)(listModelScelti.getElementAt(j));
                            listModelDisponibili.addElement(item);
                    }
                    // disabilito plsRimuovi e plsRimuoviTutti
                    plsRimuovi.setEnabled(false);
                    plsRimuoviTutti.setEnabled(false);
                    // abilito plsAggiungi e plsAggiungiTutti
                    plsAggiungi.setEnabled(true);
                    plsAggiungiTutti.setEnabled(true);	      		 		 
                    canaliScelti.clearSelection();
                    // rimuovo tutti gli elementi dalla seconda lista
                    listModelScelti.removeAllElements();				 	 
                    indiceSelezionato = 0;
                    canaliDisponibili.setSelectedIndex(indiceSelezionato);
                    elementoSelezionato = (String)(canaliDisponibili.getSelectedValue());
                    listaSelezionata = 1; 
                   */
                    nuovoConstraint_boolean.setText("");
                    pre_con.setEnabled(false);
                 
                 }
             
   
        }
		  
}