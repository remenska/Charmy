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
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plugin.ba.utility.JTextAreaForBA;
import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.resources.ui.WithGraphWindow;

/** Classe per creare e gestire il pannello per la presentazione
	delle LTL formule derivate dal corrispondente Sequence Diagram. */
	
public class BAEdit //extends WithGraphEditor implements Serializable

 extends WithGraphWindow  
	implements IMainTabPanel
{
    

    
	/** Indica quale tipo di LTL formula � visualizzata. */
	private int BATabIndex = 0;
    
	/** Riferimento alla barra di stato. */
   // private StatusBar LTLStatus;
    
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
    
	/** Memorizza le LTL formule relative al Sequence Diagram. */
	private BA rifBA;

	
	/**
	 * identificatore del sequenceElement legato all'editor
	 */
	private long idSequence; 

	/**
	 * dati del plugRelativi alla finestra
	 */
	private PlugDataWin plugDataWin;
    
   
	protected IPlugData plugData = null ;

   
    
	/** Costruttore.
	 * @param identificatore del sequenceElement legato all'editor
	 *
	 *  */
	public BAEdit(long idSequence)
	{
		super();
		this.idSequence = idSequence;
		//init();
	}

    
	/**
	 * funzione di inizializzazione del plug
	 *
	 */
	public void init(){
		BAOutput_text = new JTextAreaForBA();
		BAOutputTabPanel = new JTabbedPane(1);
		BAScroll_text = new JScrollPane(BAOutput_text);
		BAOutputTabPanel.addTab("Buchi Automata, textual notation",BAScroll_text);
		BAOutputTabPanel.setBackgroundAt(0, Color.lightGray);
    	
		BAOutputTabPanel.setBackgroundAt(BATabIndex, Color.white);
		setBorder(BorderFactory.createEtchedBorder(Color.black,Color.white));
		setLayout(new BorderLayout());
		add ("Center",BAOutputTabPanel);
		attualeBA = BAOutput_text;
		
		rifBA = new BA();
//		validate();
		repaint();
	}
    
    
	/** Classe nidificata per gestire gli eventi di "TabChange" relativi
		al JTabbedPane inserito nella sezione "Center" del pannello. */
	class BATabChange implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			int i = BAOutputTabPanel.getSelectedIndex();
			if(i == BATabIndex || i < 0)
			{
				return;
			}
			else
			{
				switch (i)
				{
					case 0:
						attualeBA = BAOutput_text;
						BAScroll_text.repaint();
						BAOutput_text.repaint();
						BAOutputTabPanel.repaint();
						repaint();
						break;
					case 1:
						attualeBA = BAOutput_graph;
						break;
					default:
						break;	    					    					    				
				}
				BAOutputTabPanel.setBackgroundAt(i, Color.white);
				BAOutputTabPanel.setBackgroundAt(BATabIndex, Color.lightGray);
				BATabIndex = i;
				plugDataWin.getStatusBar().setText("Buchi Automata: " + BAOutputTabPanel.getTitleAt(i) + ".");
				repaint();
				return;
			}
		}		
		
		/** Costruttore. */
		BATabChange()
		{
			repaint();
		}
	}


	/** Permette di impostare la visibilit� della barra di stato
		inserita nella sezione "South" del pannello. */
	public void StatusVisible()
	{
		plugDataWin.getStatusBar().setVisible(true);
	}
    
    
	/** Imposta la visualizzazione delle LTL formule
		nelle corrispondenti finestre. */
	public void setTextLTLFormule(BA ba)
	{
		rifBA = ba;
		if(ba!=null)
		{
			if(BAOutput_text!=null)
				BAOutput_text.setTextArea(ba.getTypeBA_text());
		}
		repaint();        
		validate();   
	}
    
    
	/** Pulisce le finestre usate per la visualizzazione delle LTL formule. */
	public void resetText()
	{
		BAOutput_text.setText("");
		BAOutput_graph.setText("");
		repaint();
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
		plugDataWin.getStatusBar().setText("<Copy> ok!!  Ready.");
	}
    
    
	public void opPaste()
	{
		attualeBA.paste();
		plugDataWin.getStatusBar().setText("<Paste> ok!!  Ready.");    	
	}
    
    
	public void opCut()
	{
		attualeBA.cut();
		plugDataWin.getStatusBar().setText("<Cut> ok!!  Ready.");    	
	}
    
    
	public void opDel()
	{
		plugDataWin.getStatusBar().setText("<Delete> not available!!");
	}
    
    
	public void opUndo()
	{
		plugDataWin.getStatusBar().setText("<Undo> not available!!");    	
	}
    
    
	public void opRedo() 
	{
		plugDataWin.getStatusBar().setText("<Redo> not available!!");     	
	}
    
    
	public void opImg()
	{
	}
    
    
	public void updateLTLFormule()
	{	
		rifBA.setTypeBA_text(processingBA(BAOutput_text.getText()));
		rifBA.setTypeBA_graph(processingBA(BAOutput_graph.getText()));
		repaint();
	}
    
 
	/**
	 * aggiorna la nuova formula 
	 * @param listAExist
	 */
	public void setTypeBA_text(LinkedList listBA_text){
		rifBA.setTypeBA_text(listBA_text);
		setBA();
	}
    
	public void setBA()
	{
		if(BAOutput_text!=null)
			BAOutput_text.setTextArea(rifBA.getTypeBA_text());
	}   
    
	private LinkedList processingBA(String testo)
	{
		int lunghezza = testo.length();
		LinkedList testoBA = new LinkedList();
		String tmp;
		int indiceACapo;
		int i=0;
    	
		while (i<lunghezza)
		{
			indiceACapo = testo.indexOf(i,'\n');
			if (indiceACapo >= 0)
			{
				tmp = testo.substring(i,indiceACapo-1);
				testoBA.add(tmp);
				i=indiceACapo+1;
			}
			else
			{
				tmp = testo.substring(i,lunghezza-1);
				testoBA.add(tmp);
				i=lunghezza;
			}
		}
		repaint();
		return testoBA;
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
		init();
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


	/**
	 * ritorna l'identificativo del sequence
	 * legato all'editor
	 * @return long idSequence
	 */
	public long getIdSequence() {
		return idSequence;
	}

	/**
	 * setta l'id del sequences legato all'editor
	 * @param idSequence
	 */
	public void setIdSequence(long idSequence) {
		this.idSequence = idSequence;
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getDati()
	 */
	public Object[] getDati() {
		Object[] n = new Object[1]; 
		n[0] = BAOutput_text.getText();
		return n;
	}





	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {		
		return plugData;
	}


	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}


	public JTextAreaForBA getAttualeBA() {
		return attualeBA;
	}



}