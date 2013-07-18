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
    


package core.internal.ui.toolbar;

import java.awt.Insets;
import java.io.File;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import core.internal.plugin.file.FileManager;
import core.internal.ui.action.menu.file.OpenFileItemAction;
import core.internal.ui.action.menu.file.SaveFileItemAction;
import core.internal.ui.resources.TypeToolBar;



/** La classe crea e gestisce la "GeneralToolBar", ovvero la toolbar
	contenente i pulsanti per le operazioni di:
	- iniziare il disegno di una nuova architettura;
	- aprire il disegno di un'architettura precedentemente salvata;
	- salvare l'architettura corrente. */
	
public class GeneralToolBar extends TypeToolBar
{
	
   // private AbstractButton NewButton;
    
    private AbstractButton OpenButton;
    
    private AbstractButton SaveButton;
	
    //private NewFileItemAction newFileItemAction;
    
	private OpenFileItemAction openFileItemAction;
	private SaveFileItemAction saveFileItemAction;
	/** Costruttore. */
    public GeneralToolBar(FileManager istanzaFM)
    {
        super("General Toolbar",null); 
		
		//newFileItemAction = new NewFileItemAction(istanzaFM);
		///NewButton = createToolbarButtonJar("new.gif","New Architecture");
        //NewButton.addActionListener(newFileItemAction);        
        
		openFileItemAction = new OpenFileItemAction(istanzaFM);
        OpenButton = createToolbarButtonJarCharmy("/home/daniela/sw/charmy2.0Beta_source/output/core/internal/ui/icon/open.gif","Open Architecture");
        OpenButton.addActionListener(openFileItemAction);        
        
		saveFileItemAction = new SaveFileItemAction(istanzaFM);
        SaveButton = createToolbarButtonJarCharmy("/home/daniela/sw/charmy2.0Beta_source/output/core/internal/ui/icon/save.gif","Save Architecture");   	
        SaveButton.addActionListener(saveFileItemAction);        
        enableAll(false);
        
        //add(NewButton);
		add(OpenButton);
		add(SaveButton);      
    }

	
	/**
	 * abilita / disabilita tutti i menu di edit
	 * @param enable true abilita tutti, false disabilita
	 */
	private void enableAll(boolean enable){
		SaveButton.setEnabled(enable);
		OpenButton.setEnabled(enable);
		//NewButton.setEnabled(enable);
		
		
	
	}

	/**
	 * installa un nuovo FileMAnager
	 * @param FileManager di riferimento
	 */
	public void updateRifGraphWindow(FileManager fm)

	{
    	
	//	newFileItemAction.updateFileManagerWindow(fm); 

		openFileItemAction.updateFileManagerWindow(fm);
		
//		quitItemAction.updateFileManagerWindow(fm);
	//	printItemAction.updateFileManagerWindow(fm);
//		saveAsFileItemAction.updateFileManagerWindow(fm);
		saveFileItemAction.updateFileManagerWindow(fm);
	//	printAreaItemAction.updateFileManagerWindow(fm); 

		
		if(fm == null){
			enableAll(false);
		}
		else{
			enableAll(true);
		}    	  	    	    	    	
	}


	/*protected AbstractButton createToolbarButtonJar(String pathFileNameIcon,String pathFileNameJar, String strToolTipText)
	{
	    
		
		URL myurl =core.internal.datistatici.CharmyFile.createURLPath(pathFileNameIcon,pathFileNameJar);
		//URL myurl = createURLPath(fileNameIcon);
		
		
	    String mypathName = myurl.getFile();
	    mypathName = mypathName.substring(0, mypathName.indexOf('!'));
	    mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
	    mypathName = System.getProperty("user.dir") + File.separator + mypathName;
	    
	    File myfile = new File(mypathName);
	    
	    if(true){
	        ImageIcon myicon = new ImageIcon(myurl);
	        JButton LocalJButton = new JButton(myicon);
	        LocalJButton.setMargin(new Insets(0, 0, 0, 0));
	        LocalJButton.setToolTipText(strToolTipText);
	        return LocalJButton;
	    }
	    else{
	        JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ mypathName,"Information",JOptionPane.WARNING_MESSAGE);
	        return null;
	    }
	}*/
	
	
	private AbstractButton createToolbarButtonJarCharmy(
			String pathFileNameIcon, String strToolTipText) {
		
		
		URL urlLibrary = null;
		try {
	
			urlLibrary = new File("Charmy.jar").toURL();
		} catch (Exception e) {
	
			System.out.println("ERROR - Charmy.jar not present: "+e);
			
	
		}
		URL temp = null;
		File f = null;
		System.out.println("URL LIBRARY: " + urlLibrary);
		String newName = "file:"+pathFileNameIcon;
		System.out.println("NEW NAME: "+newName);
		try {
			temp = new URL(newName);
		}
		catch (Exception e){
			//errore : la libreria non � un jar o non esiste il file
			System.out.println("Errore nel path dell'icona del bottone "+e);
			return null;
		}
		
		
	
		if(/*f.exists()*/true){
			ImageIcon myicon = new ImageIcon(temp);
			System.out.println("TEMP " + temp);
			JButton LocalJButton = new JButton(myicon);
		LocalJButton.setMargin(null);
			LocalJButton.setToolTipText(strToolTipText);
			LocalJButton.setSize(100, 100);
				this.TavolaPulsanti.put(strToolTipText, LocalJButton);
			
			
			return LocalJButton;
		}
		else{
			JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ urlLibrary.getPath(),"Information",JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	
	



}