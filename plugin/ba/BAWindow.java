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
    
package plugin.ba;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import plugin.ba.data.PlugData;
import plugin.ba.utility.JTextAreaForBA;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.eventi.listaDS.AddListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.AddListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSeqEleEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.RemoveListaDSTimeEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSClasseEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSConstraintEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLinkEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSLoopEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSParallelEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSSimEvento;
import plugin.sequencediagram.eventi.listaDS.UpdateListaDSTimeEvento;
import plugin.sequencediagram.pluglistener.IListaDSListener;
import plugin.sequencediagram.utility.listaSequence.JListaSeqElement;
import plugin.sequencediagram.utility.listaSequence.JListaSeqElementEvent;
import plugin.sequencediagram.utility.listaSequence.JListaSeqElementInfo;
import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.internal.ui.statusbar.StatusBar;
import core.resources.ui.WithGraphWindow;

/** Classe per creare e gestire il pannello per la presentazione
	delle LTL formule derivate dal corrispondente Sequence Diagram. */
	
public class BAWindow    extends WithGraphWindow   implements IMainTabPanel, IListaDSListener 
{
    
    /** Indica quale tipo di LTL formula ? visualizzata. */
    private int LTLTabIndex = 0;
    
    /** Riferimento alla barra di stato. */
    private StatusBar LTLStatus;
    
    /** Finestra per la LTL formula di tipo A-Exist. */
    private JTextAreaForBA BAOutput_text;
    
    /** Finestra per la LTL formula di tipo A-Any. */    
    private JTextAreaForBA BAOutput_graph;
        
	/** Scrollbar per la finestra LTLOutput_A_EXIST. */ 
    private JScrollPane BAScroll_text;
    
	/** Scrollbar per la finestra LTLOutput_A_ANY. */     
    private JScrollPane BAScroll_graph;
    
	/** Pannello contenente le "TextArea" per le LTL formule. */    
    private JTabbedPane BAOutputTabPanel;
    
    private JTextAreaForBA attualeBA;

	/**
	 * vettore delle liste di editing
	 */
	private Vector listaEdit; 


	/** Pannello contenente la lista dei nomi dei Sequence Diagram. */
	private JScrollPane leftPanel;   

	/**
	 * riferimento all'editore ltl attualmente selezionato
	 */
	private BAEdit baEdit = null;

	/**
	 * dati del plugRelativi alla finestra
	 */
    private PlugDataWin plugDataWin;
    
   
	protected IPlugData plugData = null ;

	/** Lista dei nomi dei Sequence Diagram_ 
		Visualizzata sul lato sinistro del programma. */

	private JListaSeqElement sequenceList;
    
	/** Pannello contenente LeftPanel e RightPanel. */
	private JSplitPane splitPanel;
        
    
	/** Pannello che aggiunge le scrollbar all'editor 
		del ltlEdit. */
	private JScrollPane scroller;
         
	/**
	 * gestore degli eventi
	 */
	private AlgoManagerBA algoBA;

    /** Costruttore. */
    public BAWindow()
    {
        super();
        listaEdit = new Vector();
    }

    
    /**
     * funzione di inizializzazione del plug
     *
     */
    public void init(){
		BuildSequenceSplitPanel();
		LTLStatus = plugDataWin.getStatusBar();

		setBorder(BorderFactory.createEtchedBorder(Color.DARK_GRAY,Color.GRAY));
		setLayout(new BorderLayout());

		add("Center",splitPanel);

		plugDataWin.getMainPanel().addTab("Buchi Automata Window", this);
		((plugin.sequencediagram.data.PlugData)(plugData.getPlugDataManager().getPlugData("charmy.plugin.sequence"))).getListaDS().addListener(this);
		
		algoBA = new AlgoManagerBA(plugData,plugDataWin, this);
		
		plugDataWin.getPlugMenu().add(new MenuBA(algoBA));
    }
    


	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}


	/** Metodo richiamato dal costruttore per la
	 * realizzazione dell'oggetto. 
	 * dei relativi pannelli sinistro e destro
	 * */
	protected void BuildSequenceSplitPanel()
	{
		splitPanel = new JSplitPane();	
		
		sequenceList = new JListaSeqElement(plugData);
        
		sequenceList.setSelectionMode(0);
	    sequenceList.setJListaProcessoEvent(new SequenceListAction());
        
       
		leftPanel = new JScrollPane(sequenceList, 20, 30);		

		splitPanel.setDividerLocation(200);
		splitPanel.setLeftComponent(leftPanel);
		splitPanel.setRightComponent(new JPanel());
	}
    
	/** Classe nidificata per gestire gli eventi di "selezione" dalla
		JList posta nella zona sinistra della SequenceWindow. */
	class SequenceListAction implements JListaSeqElementEvent
	{
    	
		public void change(JListaSeqElementInfo jlpi) {
			int SequenceScelto = sequenceList.getSelectedIndex();
			int j = 0;
			boolean ctrl = true;
  
			if (SequenceScelto < 0 )
			{
				return;
			}
			else
			{
				JListaSeqElementInfo selectedItem = (JListaSeqElementInfo)sequenceList.getSelectedValue();
				plugDataWin.getStatusBar().setText("Sequence: " + selectedItem.getSequenceElement().getNomeSequence() + ".");
				settaEditorFor(selectedItem.getSequenceElement());
				repaint();
			}
			
		}
		/** Costruttore 
		 * implementa il listener per cambiare l'editing dell'ltl
		 * */
		SequenceListAction()
		{
		}
	}    

	/**
	 * imposta l'editor svuotando la finestra destra e mettendovi l'editor 
	 * relativa ad se
	 * @param se SequenceElement da editare
	 */
	private void settaEditorFor(SequenceElement se){
		int divider = splitPanel.getDividerLocation();
		baEdit = getEdit(se);
		scroller = new JScrollPane(baEdit);
		splitPanel.setRightComponent(scroller);
		splitPanel.setDividerLocation(divider);

	}
	
	/**
	 * ritorna l'editor relativo al SequenceElement
	 * @param se
	 * @return
	 */
	public BAEdit getOldEdit(SequenceElement se){
		Iterator le = listaEdit.iterator();
		while (le.hasNext()){
			BAEdit ltlE =(BAEdit) le.next();
			 if(ltlE.getIdSequence()==se.getId()){
				return ltlE;
			 }
		}
		return null;
	}

	/**
	 * ritorna l'editor relativo se non esiste
	 * lo crea e lo associa al sequences passato come parametro
	 * @param se
	 * @return Editor associato al sequences
	 */
	public BAEdit getEdit(SequenceElement se){
		BAEdit le = getOldEdit(se);
		if(le ==null){
			le = new BAEdit(se.getId());
			le.setDati(plugDataWin);
			listaEdit.add(le);
		}
		return le;
	}

	/** Permette di impostare la visibilit? della barra di stato
		inserita nella sezione "South" del pannello. */
    public void StatusVisible()
    {
        LTLStatus.setVisible(true);
    }
    	
    public void resetScale()
    {
    }
    
    
    public void incScaleX()
    {
    }
    
    
    public void decScaleX()
    {
    }
    
    
    public void incScaleY()
    {
    }
    
    
    public void decScaleY() 
    {
    }
    
    
    public void opCopy()
    {
    	attualeBA.copy();
    	LTLStatus.setText("<Copy> ok!!  Ready.");
    }
    
    
    public void opPaste()
    {
    	attualeBA.paste();
    	LTLStatus.setText("<Paste> ok!!  Ready.");    	
    }
    
    
    public void opCut()
    {
    	attualeBA.cut();
    	LTLStatus.setText("<Cut> ok!!  Ready.");    	
    }
    
    
    public void opDel()
    {
    	LTLStatus.setText("<Delete> not available!!");
    }
    
    
    public void opUndo()
    {
    	LTLStatus.setText("<Undo> not available!!");    	
    }
    
    
    public void opRedo() 
    {
    	LTLStatus.setText("<Redo> not available!!");     	
    }
    
    
	public void opImg()
	{
    }
    
	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#removeToolbar()
	 */
	public void stateInactive() {
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
		return null;
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getZoomAction()
	 */
	public ZoomGraphInterface getZoomAction() {
		return null;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateActive()
	 */
	public void stateActive() {

	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#classeAdded(eventi.listaDS.AddListaDSClasseEvento)
	 */
	public void classeAdded(AddListaDSClasseEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#classeRemoved(eventi.listaDS.RemoveListaDSClasseEvento)
	 */
	public void classeRemoved(RemoveListaDSClasseEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#classeUpdate(eventi.listaDS.UpdateListaDSClasseEvento)
	 */
	public void classeUpdate(UpdateListaDSClasseEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#seqLinkAdded(eventi.listaDS.AddListaDSLinkEvento)
	 */
	public void seqLinkAdded(AddListaDSLinkEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#seqLinkRemoved(eventi.listaDS.RemoveListaDSLinkEvento)
	 */
	public void seqLinkRemoved(RemoveListaDSLinkEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#seqLinkUpdate(eventi.listaDS.UpdateListaDSLinkEvento)
	 */
	public void seqLinkUpdate(UpdateListaDSLinkEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#timeAdded(eventi.listaDS.AddListaDSTimeEvento)
	 */
	public void timeAdded(AddListaDSTimeEvento event) {	
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#timeRemoved(eventi.listaDS.RemoveListaDSTimeEvento)
	 */
	public void timeRemoved(RemoveListaDSTimeEvento event) {	
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#timeUpdate(eventi.listaDS.UpdateListaDSTimeEvento)
	 */
	public void timeUpdate(UpdateListaDSTimeEvento event) {	
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#sequenceAdded(eventi.listaDS.AddListaDSSeqEleEvento)
	 */
	public void sequenceAdded(AddListaDSSeqEleEvento event) {
		if(!checkSeq(event.getSequenceElement().getId())){
			BAEdit lt = new BAEdit(event.getSequenceElement().getId());
			lt.setDati(plugDataWin);
		    listaEdit.add(lt);	
		}	
			
	}
	
	/**
	 * controlla se ad un elemento ? gia associato un editor
	 * @param identificativo SequenceElement
	 * @return true se l'editor ha un elemento associato
	 */
	private boolean checkSeq(long id){
		Iterator le = listaEdit.iterator();
			while (le.hasNext()){
				BAEdit baE =(BAEdit) le.next();
				 if(baE.getIdSequence()==id){
					return true;
				 }
			}
			return false;
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#sequenceRemoved(eventi.listaDS.RemoveListaDSSeqEleEvento)
	 */
	public void sequenceRemoved(RemoveListaDSSeqEleEvento event) {
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#listaDSRefresh()
	 */
	public void listaDSRefresh() {

	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#constraintAdded(eventi.listaDS.AddListaDSConstraintEvento)
	 */
	public void constraintAdded(AddListaDSConstraintEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#constraintRemoved(eventi.listaDS.RemoveListaDSConstraintEvento)
	 */
	public void constraintRemoved(RemoveListaDSConstraintEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#constraintUpdate(eventi.listaDS.UpdateListaDSConstraintEvento)
	 */
	public void constraintUpdate(UpdateListaDSConstraintEvento event) {
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#parallelAdded(eventi.listaDS.AddListaDSParallelEvento)
	 */
	public void parallelAdded(AddListaDSParallelEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#parallelRemoved(eventi.listaDS.RemoveListaDSParallelEvento)
	 */
	public void parallelRemoved(RemoveListaDSParallelEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#parallelUpdate(eventi.listaDS.UpdateListaDSParallelEvento)
	 */
	public void parallelUpdate(UpdateListaDSParallelEvento event) {
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#SimAdded(eventi.listaDS.AddListaDSSimEvento)
	 */
	public void SimAdded(AddListaDSSimEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#SimRemoved(eventi.listaDS.RemoveListaDSSimEvento)
	 */
	public void SimRemoved(RemoveListaDSSimEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#SimUpdate(eventi.listaDS.UpdateListaDSSimEvento)
	 */
	public void SimUpdate(UpdateListaDSSimEvento event) {
		
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#LoopAdded(eventi.listaDS.AddListaDSLoopEvento)
	 */
	public void LoopAdded(AddListaDSLoopEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#LoopRemoved(eventi.listaDS.RemoveListaDSLoopEvento)
	 */
	public void LoopRemoved(RemoveListaDSLoopEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see plugin.pluglistener.IListaDSListener#LoopUpdate(eventi.listaDS.UpdateListaDSLoopEvento)
	 */
	public void LoopUpdate(UpdateListaDSLoopEvento event) {
		
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getDati()
	 */
	public Object[] getDati() {
		//procedo alla generazione di una formula LTL
		algoBA.BA();
		Object[] no = new Object[listaEdit.size()];
		for (int i = 0; i < listaEdit.size(); i++){
			BAEdit le = (BAEdit) listaEdit.get(i);
			le.getDati();
			no[i] = le.getDati()[0];
		}
		return no;
	}





	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {
		plugData=new PlugData(pm);
		return plugData;	
	}

	public void resetForNewFile()
	{
		for (int i=0; i<listaEdit.size(); i++)
	   	{
			BAEdit editor = (BAEdit)listaEdit.get(i);
			editor.getAttualeBA().setText("");
	   	}		
	
		sequenceList.removeAll();
		this.listaEdit.removeAllElements();
		
	}


}