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
    

package plugin.sequencediagram;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.eventi.listaDP.AddDPLTEvento;
import plugin.statediagram.eventi.listaDP.AddDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.AddDPStatoEvento;
import plugin.statediagram.eventi.listaDP.AddDPThreadEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPLTEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPStatoEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPThreadEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPStatoEvento;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.pluglistener.IListaDPListener;
import plugin.topologydiagram.data.ListaProcesso;
import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import plugin.topologydiagram.pluglistener.IListaProcessoListener;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.sequencediagram.data.ListaClasse;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.ListaSeqLink;
import plugin.sequencediagram.data.PlugData;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.dialog.FinestraSequence;
import plugin.sequencediagram.toolbar.SequenceToolBar;
import plugin.sequencediagram.utility.listaSequence.JListaSeqElement;
import plugin.sequencediagram.utility.listaSequence.JListaSeqElementEvent;
import plugin.sequencediagram.utility.listaSequence.JListaSeqElementInfo;
import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.menu.EditMenu;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.resources.ui.WithGraphWindow;

/** Classe per creare e gestire i Sequence Diagram. */
	
public class SequenceWindow 	extends WithGraphWindow implements IMainTabPanel,
			IListaProcessoListener,
			IListaDPListener
{
    
    private PlugDataWin plugDataWin;

	/** Lista dei nomi dei Sequence Diagram_ 
    	Visualizzata sul lato sinistro del programma. */
	private JListaSeqElement sequenceList;
    
    
    
    /** Indice dell'elemento della SequenceList
    	corrispondente al Sequence Diagram visualizzato. */
    private int SequenceIndex = -1;    
	    
    /** Pannello contenente la lista dei nomi dei Sequence Diagram. */
    private JScrollPane SequenceLeftPanel;   
    
    /** Riferimento all'editor corrente. */
    private SequenceEditor seqEditor = null;
    
    /** Pannello che aggiunge le scrollbar all'editor 
    	del Sequence Diagram (seqEditor). */
    private JScrollPane SequenceScroller;
    

    /** Indice per la gestione degli eventi di "TabChange". */
    private int SequenceTabIndex = 0;
        
    /** Pannello contenente l'editor (SequenceScroller) */
    private JTabbedPane SequenceRightPanel;

    /** Pannello contenente SequenceLeftPanel e SequenceRightPanel. */
    private JSplitPane SequenceSplitPanel;
            

    /** Lista di tutti i processi utilizzabili in un Sequence Diagram. */
    private ListaProcesso allProcessList;     
    
   
    /** Riferimento alla toolbar del Sequence Diagram. */
    private SequenceToolBar rifSequenceToolBar;
    
    private SequenceWindow rifSequenceWindow;
    
    private EditMenu rifEditMenu;
   
   /** 
    * riferimento ai dati derivati dal plugIn
    */
   private PlugData plugData;
   
   private plugin.topologydiagram.data.PlugData pdTopology;
   
   private plugin.statediagram.data.PlugData pdThread;
   
	        
	/**
	 * lista degli editor
	 */
	private ListaSequenceEditor listaSequenceEditor;        
	        
	        
    /** Costruttore. */
    public SequenceWindow()
    {
        super();        
	 }

	/**
	 * attiva la classe
	 *
	 */
	public void init(){		
		BuildSequenceSplitPanel();
		setBorder(BorderFactory.createEtchedBorder(Color.blue,Color.yellow));
		setLayout(new BorderLayout());
		add("Center",SequenceSplitPanel);

		rifSequenceWindow = this;
		//costruzione tool bar particolare
		setToolBar(new SequenceToolBar(this));
	    listaSequenceEditor = new ListaSequenceEditor(
	    		plugDataWin, 
				plugData, 
				rifSequenceToolBar);
		pdTopology=(plugin.topologydiagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.topology");
		pdThread=(plugin.statediagram.data.PlugData )plugData.getPlugDataManager().getPlugData("charmy.plugin.state");				
		pdTopology.getListaProcesso().addListener(this);
		pdThread.getListaDP().addListener(this);
				
	    plugDataWin.getMainPanel().addTab("Sequence Editor", this);
	    rifSequenceToolBar.setEnabledMessageButtons(false);
	    rifSequenceToolBar.setEnabledSequenceButtons(false);
	    //this.sequenceList.activateListeners();
	}

	/**
	 *
	 */
	//public static void focusIn(int i)
	//{
//		SequenceTabIndex=i;
	//	SequenceRightPanel.setSelectedIndex(i);
	//}   
	
	
	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}

	/** Metodo richiamato dal costruttore per la realizzazione dell'oggetto. */
    protected void BuildSequenceSplitPanel()
    {
        SequenceSplitPanel = new JSplitPane();
        sequenceList = new JListaSeqElement(plugData);
        
        sequenceList.setSelectionMode(0);
       	//nuovo listener
	     sequenceList.setJListaProcessoEvent(new SequenceListAction());
        
       
        SequenceLeftPanel = new JScrollPane(sequenceList, 20, 30);
        SequenceRightPanel = new JTabbedPane();
        SequenceSplitPanel.setLeftComponent(SequenceLeftPanel);
        SequenceSplitPanel.setRightComponent(SequenceRightPanel);
        SequenceSplitPanel.setDividerLocation(200);
        SequenceSplitPanel.setLeftComponent(SequenceLeftPanel);
        SequenceSplitPanel.setRightComponent(SequenceRightPanel);
    }


    public void setEditMenu(EditMenu em)
    {
    	rifEditMenu = em;
    }
    
    
    /** Metodo per impostare i riferimenti alla
    	EditToolBar ed alla SequenceToolBar. */
    public void setToolBar(SequenceToolBar stbar)
    {
    	abilitaBottoni(true);
    	rifSequenceToolBar = stbar;     
    }
    
    
    /**
     * ho cercato di capirla questa funzione, ma fa la stessa cosa in ogni caso
     *
     */
    public void setButtonEnabled(){
    	if (SequenceTabIndex == 0){
    		abilitaBottoni(true);
        }
        else{
        	abilitaBottoni(true);
        }        
    }
    
    /**	
     * abilita i bottoni delle edittoolbar
     * @param abilita true, disabilita false
     */
    private void abilitaBottoni(boolean abilita){
    	plugDataWin.getEditToolBar().setButtonEnabled("Copy",abilita);
    	plugDataWin.getEditToolBar().setButtonEnabled("Paste",abilita);        
    	plugDataWin.getEditToolBar().setButtonEnabled("Cut",abilita);
    	plugDataWin.getEditToolBar().setButtonEnabled("Del",abilita);         
    	plugDataWin.getEditToolBar().setButtonEnabled("Undo",abilita);
    	plugDataWin.getEditToolBar().setButtonEnabled("Redo",abilita);
    }
        
	
    /** Classe nidificata per gestire gli eventi di "selezione" dalla
    	JList posta nella zona sinistra della SequenceWindow. */
    class SequenceListAction implements JListaSeqElementEvent
    {
    	
		public void change(JListaSeqElementInfo jlpi) {
			int SequenceScelto = sequenceList.getSelectedIndex();
			int j = 0;
			boolean ctrl = true;
			if (SequenceScelto < 0 || SequenceScelto == SequenceIndex){
				return;
			}
			else{
				JListaSeqElementInfo selectedItem = (JListaSeqElementInfo)sequenceList.getSelectedValue();
				plugDataWin.getStatusBar().setText("Sequence: " + selectedItem.getSequenceElement().getNomeSequence() + ".");
				//sitemare per sequence Editor
				settaEditorFor(selectedItem.getSequenceElement());
				rifSequenceToolBar.setEnabledMessageButtons(true);
				rifSequenceToolBar.setEnabledSequenceButtons(true);
			}
			
		}
	}    


  

	/**
	 * imposta l'editor svuotando la finestra destra e mettendovi l'editor 
	 * relativa ad se
	 * @param se SequenceElement da editare
	 */
	private void settaEditorFor(SequenceElement se){
		SequenceRightPanel.removeAll();
		seqEditor = listaSequenceEditor.getSeqEditor(se);
		SequenceScroller = new JScrollPane(seqEditor);
		SequenceRightPanel.add(/*se.getName()*/"Sequence Editor",SequenceScroller);			
		repaint();
	}
    
    
    /** Operazione di copy. */
    public void opCopy()
    {
		if ((seqEditor!=null)&&(sequenceList.getSelectedIndex() >= 0))
		{
			plugDataWin.getStatusBar().setText("<Copy> not available!!");
		}
    	else
    	{
    		plugDataWin.getStatusBar().setText("<Copy> not succeeded!!  Sequence Diagram not selected!!");
    	} 		   	
    }
    
    
    /** Operazione di paste. */
    public void opPaste()
    {
    	if ((seqEditor!=null)&&(sequenceList.getSelectedIndex() >= 0)){
			plugDataWin.getStatusBar().setText("<Paste> not available!!");
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Paste> not succeeded!!  Sequence Diagram not selected!!");
    	} 
    }
    
    
    /** Operazione di cut. */
    public void opCut()
    {
    	if ((seqEditor!=null)&&(sequenceList.getSelectedIndex() >= 0)){
			plugDataWin.getStatusBar().setText("<Cut> not available!!");
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Cut> not succeeded!!  Sequence Diagram not selected!!");
    	}     	
    }
    
    
    /** Operazione di cancellazione. */
    public void opDel()
    {
    	if ((seqEditor!=null)&&(sequenceList.getSelectedIndex() >= 0)){
    		seqEditor.opDel();
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Delete> not succeeded!!  Sequence Diagram not selected!!");
    	}    	
    }
    
    
    /** Operazione di undo. */
    public void opUndo()
    {
    	if ((seqEditor!=null)&&(sequenceList.getSelectedIndex() >= 0)){
    		seqEditor.opUndo(); 		
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Undo> not succeeded!!  Sequence Diagram not selected!!");
    	} 
    }
    
    
    /** Operazione di redo. */
    public void opRedo()
    {
    	if ((seqEditor!=null)&&(sequenceList.getSelectedIndex() >= 0)){	
    		seqEditor.opRedo();    		 		
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Redo> not succeeded!!  Sequence Diagram not selected!!");
    	}   
    } 
    
    
    /** Attiva le operazioni per salvare, come immagine jpeg,
    	il Sequence Diagram corrente su file. */ 
	public void opImg()
	{
		if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){
			if (SequenceTabIndex == 0){
				seqEditor.opImg();
			}
			else{
				plugDataWin.getStatusBar().setText("<Save JPEG Image> not available!!");	
			}
		}
    	else{
    		plugDataWin.getStatusBar().setText("<Save JPEG Image> not succeeded!!  Sequence Diagram not selected!!");
    	}			
	} 

	public SequenceToolBar getSequenceToolBar(){
		return rifSequenceToolBar;
	}
	
	/** Metodo per impostare lo stato dell'editor. */
	public void setWindowStatus(int j, int tipoprc, int msgType, boolean ctrl)
    {
		if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){
			if (SequenceTabIndex == 0){     
				seqEditor.setEditorStatus(j,tipoprc,msgType,ctrl);
			}
			else{
				rifSequenceToolBar.setNoPressed();
				plugDataWin.getStatusBar().setText("<Insertion> not available!!");              		
			}     	
		}
		else{        	
			rifSequenceToolBar.setNoPressed();
			plugDataWin.getStatusBar().setText("<Insertion> not available!!  Sequence Diagram not selected!!");        	
		}
    }
    
    
    /** Metodo per impostare le propriet? del corrente Sequence Diagram. */
    public void opRenameSequence()
    {
    	if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){
    		FinestraSequence finSequence = new FinestraSequence(null,this,"Sequence's Properties",false);    		
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Sequence Properties> not succeeded!!  Sequence not selected!!");
    	}    	
    }
	    
	    
	/** Metodo per creare una copia del corrente Sequence Diagram. */
	public void opCopySequence(){
				
        SequenceElement tmpElementoSequence = null;
        SequenceEditor tmpSequenceEditor;
        String nomeSequence;
        String tmpnomeSequence;
        int num = 1;
        if (seqEditor.getSequenceElement() != null){
            nomeSequence = "cp_" + seqEditor.getSequenceElement().getNomeSequence();
            tmpnomeSequence = nomeSequence;
			while (plugData.getListaDS().giaPresente(tmpnomeSequence)){
                num++;
                tmpnomeSequence = nomeSequence + "_" + Integer.toString(num);
            }
            nomeSequence = tmpnomeSequence;
            tmpElementoSequence = new SequenceElement(nomeSequence,plugData);
            
            tmpElementoSequence.setListaClasse(seqEditor.getListaClasse().cloneListaClasse());
            tmpElementoSequence.setListaTime(seqEditor.getListaDeiTempi().cloneListaTime());			
            tmpElementoSequence.setListaSeqLink(seqEditor.getListaLink().cloneListaLink(tmpElementoSequence.getListaClasse(),
							tmpElementoSequence.getListaTime()));  
            tmpElementoSequence.setListaConstraint(seqEditor.getListaConstraint().cloneListaConstraint());
            tmpElementoSequence.setListaParallel(seqEditor.getListaParallel().cloneListaParallel());
            tmpElementoSequence.setListaLoop(seqEditor.getListaLoop().cloneListaLoop());
            tmpElementoSequence.setListaSim(seqEditor.getListaSim().cloneListaSim());
			plugData.getListaDS().add(tmpElementoSequence);
        	
			tmpSequenceEditor = 
					new SequenceEditor(
						nomeSequence,
						new DefaultListModel(),
						seqEditor.getListaDeiTempi().isStringTimeVisible(),
						seqEditor.getListaDeiTempi().isLineTimeVisible(),
						plugData,
						plugDataWin);

			tmpSequenceEditor.setToolBar(plugDataWin.getEditToolBar(),rifSequenceToolBar);
	        tmpSequenceEditor.setSequenceElement(tmpElementoSequence);
			listaSequenceEditor.add(tmpSequenceEditor);
			tmpSequenceEditor.repaint();
        }
        else{
            plugDataWin.getStatusBar().setText("<Copy Sequence> not succeeded!!  Sequence not selected!!");
        }		            
	}    


	/** Metodo per aprire la finestra di dialogo relativa 
		alla creazione di un nuovo Sequence Diagram. */
	public void opNewSequence()
	{
		if(getAllProcessList().size()>1){
			FinestraSequence finSequence = new FinestraSequence(null,this,"New Sequence",true);
		}
		else{
    		plugDataWin.getStatusBar().setText("<New Sequence> not succeeded!!");            
            String str;
            str =  	"You must create at least two processes and one message\n" +
            	    "before the creation of a Sequence";	
            JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);   
        }		
	}
	
	
	
	/** Metodo per aggiungere una fascia temporale al corrente Sequence Diagram. */
	
	public void addTimeLine()
	{
        if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){ 			
			if (SequenceTabIndex == 0){     
        		seqEditor.addTimeLine();
        	}
        	else{
    			plugDataWin.getStatusBar().setText("<Time's Line> not available!!");              		
        	}
		}
		else{
			plugDataWin.getStatusBar().setText("<Time's Line> not succeeded!!  Sequence Diagram not selected!!");
		}
	}


    /** Zoom sull'asse X. */
    public void incScaleX()
    {
        if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){    	
        	if (SequenceTabIndex == 0){	
        		seqEditor.incScaleX();
        	}
        	else{
    			plugDataWin.getStatusBar().setText("<Stretch Horizontal> not available!!");              		
        	}
        }
    	else{
    		plugDataWin.getStatusBar().setText("<Stretch Horizontal> not succeeded!!  Sequence Diagram not selected!!");
    	}         
    }
    
    
    /** Zoom negativo sull'asse X. */
    public void decScaleX()
    {
        if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){
        	if (SequenceTabIndex == 0){     
        		seqEditor.decScaleX();
        	}
        	else{
    			plugDataWin.getStatusBar().setText("<Compress Horizontal> not available!!");              		
        	}
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Compress Horizontal> not succeeded!!  Sequence Diagram not selected!!");
    	}    	
    }
    
    
    /** Zoom sull'asse Y. */
    public void incScaleY()
    {
        if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){   
        	if (SequenceTabIndex == 0){     
        		seqEditor.incScaleY();
        	}
        	else{
    			plugDataWin.getStatusBar().setText("<Stretch Vertical> not available!!");              		
        	}
        }
        else{
    		plugDataWin.getStatusBar().setText("<Stretch Vertical> not succeeded!!  Sequence Diagram not selected!!");
    	} 
    }
    
    
    /** Zoom negativo sull'asse Y. */
    public void decScaleY()
    {
        if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){    	
        	if (SequenceTabIndex == 0){     
        		seqEditor.decScaleY();
        	}
        	else{
    			plugDataWin.getStatusBar().setText("<Compress Vertical> not available!!");              		
        	}
    	}
    	else{
    		plugDataWin.getStatusBar().setText("<Compress Vertical> not succeeded!!  Sequence Diagram not selected!!");
    	}     	
    } 
    
    
    /** Ripristina la scala di visualizzazione
    	del Sequence Diagram al 100%. */
    public void resetScale()
    {
        if ((seqEditor != null)&&(sequenceList.getSelectedIndex() >= 0)){     	
        	if (SequenceTabIndex == 0){     
        		seqEditor.resetScale();
        	}
        	else{
    			plugDataWin.getStatusBar().setText("<Zoom Reset> not available!!");              		
        	}
        }
    	else{
    		plugDataWin.getStatusBar().setText("<Zoom Reset> not succeeded!!  Sequence Diagram not selected!!");
    	}        
    }
    
    
    /** Imposta la lista di tutti i processi
    	utilizzabili in un Sequence Diagram. */
    public void setAllProcessList(ListaProcesso lp)
    {
    	allProcessList = lp;
    }
    
    
    /** Restituisce l'editor del corrente Sequence Diagram. */
    public SequenceEditor getCurrentSequenceEditor()
    {
    	return seqEditor;
    }
    
    
    /** Restituisce la lista di tutti i processi 
    	utilizzabili in un Sequence Diagram. */
    public ListaProcesso getAllProcessList()
    {
    	return pdTopology.getListaProcesso();//. allProcessList;
    }
    

	/** Metodo per creare un nuovo Sequence Diagram. */    
    public void addSequence(String nomeSequence, DefaultListModel listaClassi, boolean ctrlStringTime, boolean ctrlLineTime)
    {
    	SequenceEditor localSequenceEditor = seqEditor;
    	//aggiunge il sequence
    	seqEditor = new SequenceEditor(nomeSequence,listaClassi,ctrlStringTime,ctrlLineTime, plugData, plugDataWin);
	    seqEditor.setToolBar(plugDataWin.getEditToolBar(),rifSequenceToolBar);
		listaSequenceEditor.add(seqEditor);
		
		settaEditorFor(seqEditor.getSequenceElement());
    	if (sequenceList.number()==2)
    	{
    		sequenceList.setSelectedIndex((sequenceList.number())-1);
    	}
    	sequenceList.setSelectedIndex(0);
    	sequenceList.setSelectedIndex((sequenceList.number())-1);	
    }


	/** Metodo per eliminare il corrente Sequence Diagram. */
	public void delSequence()
	{
        SequenceElement tmpElementoSequence;
        String SelectedItem;
        boolean ctrl = true;
        int j = 0;
        
        if (sequenceList.getSelectedIndex() >= 0)
        {
        	if(sequenceList.number()==1){
        		rifSequenceToolBar.setEnabledMessageButtons(false);
        		rifSequenceToolBar.setEnabledSequenceButtons(false);
			}
			JListaSeqElementInfo selectedItem = (JListaSeqElementInfo)sequenceList.getSelectedValue();
        	tmpElementoSequence = selectedItem.getSequenceElement(); 
        	plugData.getListaDS().remove(tmpElementoSequence);
			//	Selezione del primo processo dell'elenco.
			sequenceList.clearSelection();
			if (sequenceList.number() > 0)
			{
				SequenceIndex = -1;
				sequenceList.setSelectedIndex(0);
			}
			else
			{								
				SequenceRightPanel.removeAll();
			}
		}
		else
		{
    		plugDataWin.getStatusBar().setText("<Delete Sequence> not succeeded!!  Sequence not selected!!");			
		}			
	}


    /** Aggiorna le propriet? del Sequence. 
     * non dovrebbe servire pi?
     * */
    public void updateSequence(String nomeSequence, DefaultListModel listaClassi, boolean ctrlStringTime, boolean ctrlLineTime)
    {
		SequenceElement tmpSequence;
    	boolean ctrl = true;
    	int j = 0;
    	int k = sequenceList.getSelectedIndex();
		JListaSeqElementInfo SelectedItem;
    	SelectedItem = (JListaSeqElementInfo)sequenceList.getSelectedValue(); 
        while ((ctrl) && (j<plugData.getListaDS().size()))
        {
        	tmpSequence = (SequenceElement)(plugData.getListaDS().get(j));
            if ((tmpSequence.getNomeSequence()).equals(SelectedItem.getSequenceElement().getName()))
            {
            	ctrl = false;
            	tmpSequence.setName(nomeSequence);
            }
            else
            {
            	j++;
            }
        }
      
    	seqEditor.updateSequence(nomeSequence,listaClassi,ctrlStringTime,ctrlLineTime);    	
    	if (k>=0)
    	{
    		sequenceList.setSelectedIndex(k);
    	}      	
    	
    	SequenceLeftPanel.repaint();
    	this.seqEditor.setName(nomeSequence);
    }


	/** Restituisce la lista di tutti i Sequence. */
	public ListaDS getListaSequence()
	{
		return plugData.getListaDS();
	}
	
	
	/** Restituisce true se il nome di sequence in ingresso ? gia utilizzato. */
	public boolean nomeSequenceGiaPresente(String strSequence)
	{
		
		return plugData.getListaDS().giaPresente( strSequence);  //strSequence
	}


	

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */	
	public void restoreFromFile(ListaProcesso lp)
	{
		SequenceElement istanzaSequence;
		allProcessList = lp;
		SequenceIndex = -1;
		SequenceTabIndex = 0;		
		for (int i=0; i<plugData.getListaDS().size(); i++)
		{
			istanzaSequence = (SequenceElement)(plugData.getListaDS().get(i));
		}
		if (plugData.getListaDS().size()>0)
		{
			SequenceScroller = new JScrollPane(seqEditor);
    		SequenceRightPanel.add(SequenceScroller,SequenceTabIndex);
			sequenceList.setSelectedIndex(0);
		}
		rifSequenceToolBar.setNoPressed();
        plugDataWin.getStatusBar().setText("Sequence ready.");			
	}
	

	public void resetForNewFile()
	{
		sequenceList.removeList();
		while (SequenceRightPanel.getTabCount() > 0) {
			SequenceRightPanel.remove(0);
		}
		
		if (plugData.getListaDS() != null) {
			while (plugData.getListaDS().size() > 0) {
				plugData.getListaDS().remove(0);
			}
		}
		
		seqEditor = null;
		SequenceTabIndex = 0;
		plugData.getListaDS().removeAll();
		rifSequenceToolBar.setNoPressed();
		plugDataWin.getStatusBar().setText("Sequence ready.");
		SequenceIndex = -1;
		rifSequenceWindow = this;

		//costruzione tool bar particolare
		listaSequenceEditor = new ListaSequenceEditor(
				plugDataWin, 
				plugData, 
				rifSequenceToolBar);
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateChanged()
	 */
	public void stateActive() {
		setButtonEnabled();
		plugDataWin.getEditToolBar().updateRifGraphWindow(this);          	
		plugDataWin.getZoomToolBar().updateRifGraphWindow(this); 
		plugDataWin.getToolBarPanel().add(rifSequenceToolBar);
		plugDataWin.getToolBarPanel().add(plugDataWin.getZoomToolBar());  
		plugDataWin.getStatusBar().setText("Sequence ready.");		

	}



	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#removeToolbar()
	 */
	public void stateInactive() {
		plugDataWin.getToolBarPanel().remove(rifSequenceToolBar);
		plugDataWin.getToolBarPanel().remove(plugDataWin.getZoomToolBar()); 
	}
		
	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#setPlugDataWin(plugin.PlugDataWin)
	 */
	public void setDati(PlugDataWin plugDtW) {
		plugDataWin = plugDtW;
		//init();
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugDataWin()
	 */
	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugData()
	 */
	public IPlugData getPlugData() {
		return plugData;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getEditMenu()
	 */
	public EditGraphInterface getEditMenu() {
		return this;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getZoomAction()
	 */
	public ZoomGraphInterface getZoomAction() {
		return this;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getDati()
	 */
	public Object[] getDati() {
		return null;
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaProcessoListener#processoAdded(core.a01topology.eventi.listaprocesso.AddEPEvento)
	 */
	public void processoAdded(AddEPEvento event) {
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaProcessoListener#processoRemoved(core.a01topology.eventi.listaprocesso.RemoveEPEvento)
	 */
	public void processoRemoved(RemoveEPEvento event) {
		ListaDS seqs=getListaSequence();
		ElementoProcessoStato eps;
		for(int i=0;i<seqs.size();i++){
			SequenceElement se=(SequenceElement)seqs.get(i);
			ListaClasse lc=se.getListaClasse();
			for(int j=0;j<lc.size();j++){
				eps=(ElementoProcessoStato)lc.getElement(j);
				if(eps.getName().equals(event.getElementoProcesso().getName())){
					lc.removeElement(eps);
				}
				if (lc.size()==0){
					seqs.remove(se);
					i--;
					SequenceRightPanel.removeAll();
				}				
				
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaProcessoListener#processoUpdate(core.a01topology.eventi.listaprocesso.UpdateEPEvento)
	 */
	public void processoUpdate(UpdateEPEvento event) {
		ListaDS seqs=getListaSequence();
		ElementoProcessoStato eps;
		for(int i=0;i<seqs.size();i++){
			SequenceElement se=(SequenceElement)seqs.get(i);
			ListaClasse lc=se.getListaClasse();
			for(int j=0;j<lc.size();j++){
				eps=(ElementoProcessoStato)lc.getElement(j);
				if(eps.getName().equals(event.getOldElementoProcesso().getName())){					
					//ElementoClasse ec=new ElementoClasse(event.getOldElementoProcesso().getTopX(),event.getOldElementoProcesso().getTopY(),event.getOldElementoProcesso().getName(),null);
					eps.setName(event.getNewElementoProcesso().getName());					
					eps.adjustGraph(eps.getGrafico());					
				}				
			}
		}
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaProcessoListener#processoRefresh()
	 */
	public void processoRefresh() {
		
	}


	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaMessaggioListener#messaggioRefresh()
	 */
	public void messaggioRefresh() {
		
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#statoAdded(core.a02thread.eventi.listaDP.AddDPStatoEvento)
	 */
	public void statoAdded(AddDPStatoEvento event) {		
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#statoRemoved(core.a02thread.eventi.listaDP.RemoveDPStatoEvento)
	 */
	public void statoRemoved(RemoveDPStatoEvento event) {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#statoUpdate(core.a02thread.eventi.listaDP.UpdateDPStatoEvento)
	 */
	public void statoUpdate(UpdateDPStatoEvento event) {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#messaggioAdded(core.a02thread.eventi.listaDP.AddDPMessaggioEvento)
	 */
	public void messaggioAdded(AddDPMessaggioEvento event) {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#messaggioRemoved(core.a02thread.eventi.listaDP.RemoveDPMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveDPMessaggioEvento event) {
		ListaDS seqs=getListaSequence();
		ElementoCanaleMessaggio em;
		for(int i=0;i<seqs.size();i++){
			SequenceElement se=(SequenceElement)seqs.get(i);
			ListaSeqLink lsl=se.getListaSeqLink();
			for(int j=0;j<lsl.size();j++){
				em=(ElementoCanaleMessaggio)lsl.getElement(j);
				if(em.getName().equals(event.getElementoMessaggio().getName())){
					ListaMessaggio ll=event.getThreadElement().getListaMessaggio();
					boolean found=false;
					for(int k=0;k<ll.size();k++){
						ElementoMessaggio tmpMsg=(ElementoMessaggio)ll.getElement(k);
						if(tmpMsg.getName().equals(event.getElementoMessaggio().getName())){
							k=ll.size();
							found=true;					
						}
					}
					if(!found)
						lsl.removeAllElements(em.getName());		
				}				
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#messaggioUpdate(core.a02thread.eventi.listaDP.UpdateDPMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateDPMessaggioEvento event) {
		ListaDS seqs=getListaSequence();
		ElementoCanaleMessaggio em;
		for(int i=0;i<seqs.size();i++){
			SequenceElement se=(SequenceElement)seqs.get(i);
			ListaSeqLink lsl=se.getListaSeqLink();
			for(int j=0;j<lsl.size();j++){
				em=(ElementoCanaleMessaggio)lsl.getElement(j);
				if(em.getName().equals(event.getVecchioElementoMessaggio().getName())){					
					ListaMessaggio ll=event.getThreadElement().getListaMessaggio();
					boolean found=false;
					for(int k=0;k<ll.size();k++){
						ElementoMessaggio tmpMsg=(ElementoMessaggio)ll.getElement(k);
						if(tmpMsg.getName().equals(event.getVecchioElementoMessaggio().getName())){
							k=ll.size();
							found=true;					
						}
					}
					if(!found)
						lsl.removeAllElements(em.getName());	
//					em.setName(event.getNuovoElementoMessaggio().getName());
//					em.adjustGraph(em.getGrafico());
				}				
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#listaDPRefresh()
	 */
	public void listaDPRefresh() {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#threadAdded(core.a02thread.eventi.listaDP.AddDPThreadEvento)
	 */
	public void threadAdded(AddDPThreadEvento event) {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#threadRemoved(core.a02thread.eventi.listaDP.RemoveDPThreadEvento)
	 */
	public void threadRemoved(RemoveDPThreadEvento event) {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#listaThreadAdded(core.a02thread.eventi.listaDP.AddDPLTEvento)
	 */
	public void listaThreadAdded(AddDPLTEvento event) {
	}

	/* (non-Javadoc)
	 * @see core.a02thread.pluglistener.IListaDPListener#listaThreadRemoved(core.a02thread.eventi.listaDP.RemoveDPLTEvento)
	 */
	public void listaThreadRemoved(RemoveDPLTEvento event) {
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {		
		plugData=new PlugData(pm);
		return plugData;
	}
		
}