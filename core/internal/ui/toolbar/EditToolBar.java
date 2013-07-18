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
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import core.internal.ui.action.toolbar.edit.CopyGraphButtonAction;
import core.internal.ui.action.toolbar.edit.CutGraphButtonAction;
import core.internal.ui.action.toolbar.edit.DelGraphButtonAction;
import core.internal.ui.action.toolbar.edit.PasteGraphButtonAction;
import core.internal.ui.action.toolbar.edit.RedoGraphButtonAction;
import core.internal.ui.action.toolbar.edit.UndoGraphButtonAction;
import core.internal.ui.resources.TypeToolBar;
import core.internal.ui.simpleinterface.EditGraphInterface;


/** La classe crea e gestisce la "EditToolBar", ovvero la toolbar
	contenente i pulsanti per le operazioni di cut, copy, paste,
	undo, redo e cancellazione. */
	
public class EditToolBar extends TypeToolBar
{
	
    private AbstractButton CutButton;
    private CutGraphButtonAction CutButtonListener;
    
    private AbstractButton CopyButton;
    private CopyGraphButtonAction CopyButtonListener;
    
    private AbstractButton PasteButton;
    private PasteGraphButtonAction PasteButtonListener;
    
    private AbstractButton UndoButton;
    private UndoGraphButtonAction UndoButtonListener;
    
    private AbstractButton RedoButton;
    private RedoGraphButtonAction RedoButtonListener;
    
    private AbstractButton DelButton;
    private DelGraphButtonAction DelButtonListener;
    
     
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
	
	
	/** Costruttore_ Prende in ingresso il riferimento all'oggetto 
    	sul quale devono agire i pulsanti della toolbar. */
    public EditToolBar(EditGraphInterface rifGraphWindow)
    {    	
        super("Edit Toolbar",null);
        
        CutButton = createToolbarButtonJarCharmy("./core/internal/ui/icon/cut.gif","Cut");
        CutButtonListener = new CutGraphButtonAction(null);
        CutButton.addActionListener(CutButtonListener);
        
        CopyButton = createToolbarButtonJarCharmy("./core/internal/ui/icon/copy.gif","Copy");
        CopyButtonListener = new CopyGraphButtonAction(null);
        CopyButton.addActionListener(CopyButtonListener);
                
		PasteButton = createToolbarButtonJarCharmy("./core/internal/ui/icon/paste.gif","Paste");
        PasteButtonListener = new PasteGraphButtonAction(null);
        PasteButton.addActionListener(PasteButtonListener); 	
		
		UndoButton = createToolbarButtonJarCharmy("./core/internal/ui/icon/undo.gif","Undo");
        UndoButtonListener = new UndoGraphButtonAction(null);
    	UndoButton.addActionListener(UndoButtonListener); 		
		
		RedoButton = createToolbarButtonJarCharmy("./core/internal/ui/icon/redo.gif","Redo");
        RedoButtonListener = new RedoGraphButtonAction(null);
    	RedoButton.addActionListener(RedoButtonListener); 	
		
		DelButton = createToolbarButtonJarCharmy("./core/internal/ui/icon/del.gif","Delete");
		DelButtonListener = new DelGraphButtonAction(null);
		DelButton.addActionListener(DelButtonListener);
        
		updateRifGraphWindow(rifGraphWindow);
        
        add(CutButton);
		add(CopyButton);
		add(PasteButton);
		add(DelButton);        
        add(UndoButton);
        add(RedoButton);
		
		
		
		TavolaPulsanti = new Hashtable();
		TavolaPulsanti.put("Cut",CutButton);
		TavolaPulsanti.put("Copy",CopyButton); 
		TavolaPulsanti.put("Paste",PasteButton); 
		TavolaPulsanti.put("Undo",UndoButton); 
		TavolaPulsanti.put("Redo",RedoButton); 
		TavolaPulsanti.put("Del",DelButton);									             
    }
    
	/**
	 * abilita  disabilita i pulsanti
	 * @param enable
	 */
	private void enableAll(boolean enable){
		CutButton.setEnabled(enable);
		CopyButton.setEnabled(enable);
		PasteButton.setEnabled(enable);
		UndoButton.setEnabled(enable);
		RedoButton.setEnabled(enable);
		DelButton.setEnabled(enable);
	}

	/** Permette di modificare l'oggetto sul quale agiscono
		i pulsanti della toolbar. */
    public void updateRifGraphWindow(EditGraphInterface rifGraphWindow)
    {
    	
    	
    	CutButtonListener.updateRifGraphWindow(rifGraphWindow);
    	CopyButtonListener.updateRifGraphWindow(rifGraphWindow);
    	PasteButtonListener.updateRifGraphWindow(rifGraphWindow);
    	UndoButtonListener.updateRifGraphWindow(rifGraphWindow);
    	RedoButtonListener.updateRifGraphWindow(rifGraphWindow);  
    	DelButtonListener.updateRifGraphWindow(rifGraphWindow);
    	if(rifGraphWindow == null){
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
		String newName = "file:"+pathFileNameIcon;
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
			JButton LocalJButton = new JButton(myicon);
		LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			
				this.TavolaPulsanti.put(strToolTipText, LocalJButton);
			
			
			return LocalJButton;
		}
		else{
			JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ urlLibrary.getPath(),"Information",JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

}