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
    

package core.internal.runtime;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.internal.plugin.PlugManager;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.FileManager;
import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;

public class Interfaccia extends JFrame
{

	/**
	 * dati relativi alle finestre
	 */
	private PlugDataWin plugDataWin;

	/**
	 * riferimenti contenenti le strutture dati
	 */
	private PlugDataManager plugDataManager;

	 	
    private FileManager istanzaFileManager;
    
    private PlugManager pm;

	private RifToControl istControllo;

    private int tabindex = 0;
        
    public Interfaccia()
    {
        setupFrame();
        setupFrameBehaviour();
		
//		//Per gestire la presenza dei plugin nel core
//		String dirCore = CharmyFile.dirCore();
//		Collection sm=getListaOrder(dirCore);
//		if(sm==null)return;
//		if(sm.size()<3)return;


		setupComponents();
		buildGui();
		attivaPluginsManager();
		
		addWindowListener(new Terminator());
		for(int i=0;i<plugDataManager.getPluginsList().size();i++){
			((IPlugData)(plugDataManager.getPluginsList().get(i))).activateListeners();
		}
		//attivaPlugManager();

    }

    public int focusIndex()
    {
        return plugDataWin.getMainPanel().getSelectedIndex();
    }
    

    private void setupFrame()
    {
		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = SystemKit.getScreenSize();
		int ScreenWidth = ScreenSize.width;
		int ScreenHeight = ScreenSize.height;
		double Rapporto = 0.74;
		setTitle(core.internal.datistatici.CharmyFile.DESCRIZIONE);
		
		Image img = SystemKit.getImage(core.internal.datistatici.CharmyFile.createURLPath("core/internal/icon/"+core.internal.datistatici.CharmyFile.LOGO,"Charmy.jar"));
        setIconImage(img);
		setSize((int)Math.round(ScreenWidth*Rapporto),(int)Math.round(ScreenHeight*Rapporto));
        setLocation((int)Math.round(ScreenWidth*(1-Rapporto)/2),(int)Math.round(ScreenHeight*(1-Rapporto)/2));
    }


    private void setupFrameBehaviour()
    {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
    }

//	/**
//	 * Inner class per implementare un filtro di file
//	 * per cercare sole le directory
//	 * @author michele
//	 * Charmy plug-in
//	 *
//	 */
//	public class FiltraDir implements FilenameFilter {
//
//		/* (non-Javadoc)
//		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
//		 */
//		public boolean accept(File dir, String name) {
//			String direct = dir.getAbsolutePath();
//			File f = new File(CharmyFile.addFileSeparator(direct).concat(name));
//			return f.isDirectory();
//		}
//	}
//
//	/**
//	 * crea un'array di plug-in ordinato per nome
//	 * @return ritorna una Collection Ordinata  di nomi di file 
//	 * 				oppure null se la Collection ? vuota
//	 */
//	public Collection getListaOrder( String directory){
//
//		File fLista = new File(directory);
//		TreeMap sm = null;
//		if(!fLista.exists()){
//			return null;
//		}
//		String lista[] = fLista.list(new FiltraDir());
//		if(lista.length>0){
//			sm = new TreeMap();
//			for(int i = 0; i<lista.length; i++){
//				sm.put(lista[i], lista[i]);
//			}
//		}
//		if(sm == null) 
//			return null;
//		return sm.values();
//
//	}

    private void setupComponents()
    {
		plugDataWin = new PlugDataWin();
		plugDataManager = new PlugDataManager();    
		pm = new PlugManager(plugDataWin,plugDataManager);
		istanzaFileManager =  new FileManager(this,pm);
		
		plugDataWin.setPlugManager(pm);  		
		plugDataWin.setFileManager(istanzaFileManager);
		
		plugDataManager.setPlugManager(pm);
		//plugData.setPlugManager(pm);
		
		
		istanzaFileManager.setPlugData(plugDataManager);
		
		
		//istControllo = new RifToControl(plugData);		
    }


	/**
	 * attiva il plug manager
	 * va chiamato alla fine di tutte le inizializzazioni
	 */
	private void attivaPluginsManager(){
		pm.start();
		pm.init();		
		
	
		istanzaFileManager.start();/// nuova feature (ezio)		
			
		plugDataWin.getMainPanel().addChangeListener(new TabChange());
		this.setMainTab(0);

	}



    private void buildGui()
    {
    	/* inserimento toolbar standard */
		plugDataWin.getToolBarPanel().setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		plugDataWin.getToolBarPanel().add(plugDataWin.getGeneralToolBar());
		plugDataWin.getToolBarPanel().add(plugDataWin.getEditToolBar());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH,plugDataWin.getToolBarPanel());
		getContentPane().add(BorderLayout.CENTER,plugDataWin.getMainPanel());
		getContentPane().add(BorderLayout.SOUTH, plugDataWin.getStatusBar());

	    setJMenuBar(plugDataWin.getMainMenuBar());
    }


    /** Classe per gestire l'evento TabChange tra "S.A. Topology",
		"State Diagram", "Sequence Diagram" e "Output: Promela". 
	  * La classe ? stata modificata per permettere una gestione uniforma tra
	  * le varie tipologie di finestre, in quanto si deve normalizzare la gestione per poter
	  * gestire inmodo pi? semplice la gestione dei plug-in	
	  */
    class TabChange implements ChangeListener
    {
       
        public void stateChanged(ChangeEvent e){			
			setMainTab(plugDataWin.getMainPanel().getSelectedIndex());
		}

		TabChange(){
		}
    }

	/**
	 * setta i dati relativi al tab numero "indice" 
	 * @param indice numero del tab attivo
	 */
	private void setMainTab(int indice){
		
		
	
			for( int numComp = plugDataWin.getMainPanel().getComponentCount() -1 ;  numComp >= 0; numComp--){
				
				Object candidatePlugin2 = plugDataWin.getMainPanel().getComponent(numComp);
				IMainTabPanel plugCorr;
				try{
					 plugCorr = (IMainTabPanel)candidatePlugin2;
					 if(plugCorr!=null)plugCorr.stateInactive();
					
				} catch (Exception ex) {
					//return;
					
				}
					
					
				}
			
			Object candidatePlugin = plugDataWin.getMainPanel().getComponent(indice);
			IMainTabPanel plug;
		try{
			 plug = (IMainTabPanel)candidatePlugin;
			
		} catch (Exception ex) {
			return;
			
		}
			
			if(plug!=null){
				plug = (IMainTabPanel)plugDataWin.getMainPanel().getComponent(indice);
				plug.stateActive();
				//bug - da riparare - classe pannello principale diverso da IMainTabPanel 
				this.istanzaFileManager.setPluginActivated(plug);//ezio
				
				plugDataWin.getEditMenu().updateRifGraphWindow(plug.getEditMenu());
				plugDataWin.getEditToolBar().updateRifGraphWindow(plug.getEditMenu());
				plugDataWin.getZoomToolBar().updateRifGraphWindow(plug.getZoomAction());
			}
			plugDataWin.getToolBarPanel().revalidate();
			plugDataWin.getToolBarPanel().repaint();
			
			
		
		
		
		
	}

				
	public void adjustTitle(String pFile)
	{
		if (pFile == null){
			setTitle(core.internal.datistatici.CharmyFile.DESCRIZIONE);
		}
		else{
			setTitle(core.internal.datistatici.CharmyFile.DESCRIZIONE+"  ["+pFile+"]");
		}
	}

	
	/** Classe per gestire l'uscita dal programma. */
	class Terminator extends WindowAdapter
	{
		public void windowClosing(WindowEvent we){
			plugDataWin.getFileManager().Quit();
		}
	}
        
}