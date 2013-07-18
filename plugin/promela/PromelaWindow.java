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
    

package plugin.promela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import core.internal.extensionpoint.DeclaredHost;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.resources.ui.WithGraphWindow;

import plugin.promela.data.CodicePromelaSpecified;
import plugin.promela.data.PlugData;
import plugin.promela.toolbar.PromelaToolBar;

/** Classe per creare e gestire il pannello per la presentazione del
    codice Promela relativo all'architettura software corrente. */
	
public class PromelaWindow extends WithGraphWindow implements IMainTabPanel
{
    private IPlugData plugData;
	private PlugDataWin plugDataWin;
	private CodicePromelaSpecified istanzaCodicePromelaSpecified1;
    
    private JTextArea PromelaOutput1;

        /** Pannello contenente l'editor (PromelaScroller) */
    private JScrollPane PromelaAlgo1Panel;

    /** Pannello contenente PromelaLeftPanel e PromelaRightPanel. */
    private JTabbedPane PromelaTabbedPanel;
	private PromelaToolBar promelaToolBar;
	
	private AlgoManagerProm algoProm;

    /** Costruttore. */
    public PromelaWindow()
    {
        super();
   }


	/**
	 * inizializzazione del sistema
	 *
	 */
	public void init(){
	   PromelaOutput1 = new JTextArea();
	   PromelaTabbedPanel = new JTabbedPane();
		PromelaAlgo1Panel = new JScrollPane(PromelaOutput1);
		PromelaTabbedPanel.add("Promela Code",PromelaAlgo1Panel);
	   setBorder(BorderFactory.createEtchedBorder(Color.blue,Color.yellow));
	   setLayout(new BorderLayout());
	   add("Center",PromelaTabbedPanel);
	   istanzaCodicePromelaSpecified1 = new CodicePromelaSpecified();
	   
	   plugDataWin.getMainPanel().addTab("Promela Window", this);
	   algoProm = new AlgoManagerProm(plugData, this);
	   plugDataWin.getPlugMenu().add(new MenuPromela(algoProm));
	   setToolBar(new PromelaToolBar(algoProm));
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#initHost()
	 */
	public DeclaredHost[] initHost() {
		// TODO Auto-generated method stub
		return null;
	}
//    protected void BuildPromelaTabbedPanel2()
//    {
//        PromelaTabbedPanel = new JTabbedPane();
//        PromelaAlgo1Panel = new JScrollPane(PromelaOutput1);
//        PromelaTabbedPanel.add("Promela Code",PromelaAlgo1Panel);
//    }


	public CodicePromelaSpecified getPromelaSpecified1()
	{
		return istanzaCodicePromelaSpecified1;
	}
        
	public LinkedList getPromelaSpecifiedAsLinkedList1()
	{
		return (LinkedList)(istanzaCodicePromelaSpecified1);
	}

	public void setPromelaSpecified1(LinkedList specPromela)
	{
        if(specPromela==null)return;
        String row;
		istanzaCodicePromelaSpecified1 = new CodicePromelaSpecified();
		for(int i=0; i<specPromela.size(); i++){
			row = (String)(specPromela.get(i));
			istanzaCodicePromelaSpecified1.addLast(row);
		}
		Visualizza1();
	}

//	public void setPromelaSpecified1(CodicePromelaSpecified specPromela)
//	{
//		istanzaCodicePromelaSpecified1 = specPromela;
//		Visualizza1();
//	}
	
	/** Visualizza sullo schermo la specifica Promela. */
	private void Visualizza1()
	{
    	String row;
    	PromelaOutput1.setText("");
    	for (int i=0; i<istanzaCodicePromelaSpecified1.size(); i++)
	   	{
	   		row = (String)(istanzaCodicePromelaSpecified1.get(i));
        	PromelaOutput1.append(row);
			PromelaOutput1.append("\n");
		}		
        
	}

	public void setToolBar(PromelaToolBar pToolBar)
	{
		promelaToolBar = pToolBar;
	}
	
	
	public void updatePromela1()
	{
    	String testo = PromelaOutput1.getText();
    	int lunghezza = testo.length();
    	CodicePromelaSpecified testoFormula = new CodicePromelaSpecified();
    	String tmp;
    	int indiceACapo;
    	int i=0;
    	
    	while (i<lunghezza)
    	{
    		indiceACapo = testo.indexOf(i,'\n');
    		if (indiceACapo >= 0)
    		{
    			tmp = testo.substring(i,indiceACapo-1);
    			testoFormula.add(tmp);
    			i=indiceACapo+1;
    		}
    		else
    		{
    			tmp = testo.substring(i,lunghezza-1);
    			testoFormula.add(tmp);
    			i=lunghezza;
    		}
    	}
    	
    	istanzaCodicePromelaSpecified1 = testoFormula;
    	Visualizza1();  			
	}

	
    public void setButtonEnabled()
    {
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
    	if(PromelaTabbedPanel.getSelectedIndex()==0)
            PromelaOutput1.copy();
        else
          //  PromelaOutput2.copy();
    	plugDataWin.getStatusBar().setText("<Copy> ok!!  Ready.");     	
    }
    
    
    public void opPaste()
    {
    	if(PromelaTabbedPanel.getSelectedIndex()==0)
            PromelaOutput1.paste();
        else
            //PromelaOutput2.paste();
    	plugDataWin.getStatusBar().setText("<Paste> ok!!  Ready.");     	
    }
    
    
    public void opCut()
    {
    	if(PromelaTabbedPanel.getSelectedIndex()==0)
            PromelaOutput1.cut();
        else
            //PromelaOutput2.cut();
    	plugDataWin.getStatusBar().setText("<Cut> ok!!  Ready.");    	
    }
    
    
    public void opDel()
    {
    	plugDataWin.getStatusBar().setText("<Delete> not available!!  Ready.");    
    }
    
    
    public void opUndo()
    {
    	plugDataWin.getStatusBar().setText("<Undo> not available!!  Ready.");
    }
    
    
    public void opRedo() 
    {
    	plugDataWin.getStatusBar().setText("<Redo> not available!!  Ready.");    	
    }
    
    
	public void opImg()
	{
    }


	///ezio 2006 - bug : questo metodo non viene mai chiamato
	public void resetForNewFile()
	{
        while (istanzaCodicePromelaSpecified1.size()>0)
        {
        	istanzaCodicePromelaSpecified1.removeFirst();
        }
        PromelaOutput1.setText("");
        plugDataWin.getStatusBar().setText("Promela Specified.");        		
	}
	    	

	public void stateActive() {	
	    	plugDataWin.getToolBarPanel().add(promelaToolBar);
		    plugDataWin.getStatusBar().setText("Promela Specified.");    
	}

	public void stateInactive() {
		plugDataWin.getToolBarPanel().remove(promelaToolBar);
	}

	public void setDati(PlugDataWin plugDtW) {
		plugDataWin = plugDtW;
		//init();
	}

	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}


	public IPlugData getPlugData() {
		return this.plugData;
	}


	public EditGraphInterface getEditMenu() {
		return null;
	}


	public ZoomGraphInterface getZoomAction() {
		return null;
	}


	public Object[] getDati() {
		updatePromela1();
        Object[] no = new Object[1];
		no[0] = PromelaOutput1.getText();
		return no;
	}


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#newPlugData(data.PlugDataManager)
	 */
	public IPlugData newPlugData(PlugDataManager pm) {
		plugData=new PlugData(pm);
		return plugData;
	}


//	public void restoreFromFile(Object lp) {
//	}
		
}