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

import core.internal.ui.action.toolbar.zoom.ZoomInXGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomInYGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomOutXGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomOutYGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomResetGraphButtonAction;
import core.internal.ui.resources.TypeToolBar;
import core.internal.ui.simpleinterface.ZoomGraphInterface;


/** La classe crea e gestisce la "ZoomToolBar", ovvero la toolbar
	contenente i pulsanti per tutte le operazioni di zoom. */
	
public class ZoomToolBar extends TypeToolBar
{
	
    private AbstractButton ZoomInXButton;
    private ZoomInXGraphButtonAction ZoomInXButtonListener;
    
    private AbstractButton ZoomOutXButton;
    private ZoomOutXGraphButtonAction ZoomOutXButtonListener;
    
    private AbstractButton ZoomInYButton;
    private ZoomInYGraphButtonAction ZoomInYButtonListener;
    
    private AbstractButton ZoomOutYButton;
    private ZoomOutYGraphButtonAction ZoomOutYButtonListener;
    
    private AbstractButton ZoomResetButton;
    private ZoomResetGraphButtonAction ZoomResetButtonListener;


    /** Costruttore_ Prende in ingresso il riferimento all'oggetto 
    	sul quale devono agire i pulsanti della toolbar. */
    public ZoomToolBar(ZoomGraphInterface rifGraphWindow)
    {
        super("Zoom Toolbar",null); 
    	
		ZoomInXButton = createToolbarButtonJarCharmy("core/internal/ui/icon/stretchHorizontal.gif","Stretch Horizontal");
        ZoomInXButtonListener = new ZoomInXGraphButtonAction(rifGraphWindow);
        ZoomInXButton.addActionListener(ZoomInXButtonListener);
        
        ZoomOutXButton = createToolbarButtonJarCharmy("core/internal/ui/icon/compressHorizontal.gif","Compress Horizontal");
        ZoomOutXButtonListener = new ZoomOutXGraphButtonAction(rifGraphWindow);
        ZoomOutXButton.addActionListener(ZoomOutXButtonListener);
        
		ZoomInYButton = createToolbarButtonJarCharmy("core/internal/ui/icon/stretchVertical.gif","Stretch Vertical");
        ZoomInYButtonListener = new ZoomInYGraphButtonAction(rifGraphWindow);
        ZoomInYButton.addActionListener(ZoomInYButtonListener);
        
		ZoomOutYButton = createToolbarButtonJarCharmy("core/internal/ui/icon/compressVertical.gif","Compress Vertical");
        ZoomOutYButtonListener = new ZoomOutYGraphButtonAction(rifGraphWindow);
        ZoomOutYButton.addActionListener(ZoomOutYButtonListener);
        
		ZoomResetButton = createToolbarButtonJarCharmy("core/internal/ui/icon/zoomreset.gif","Zoom Reset");
        ZoomResetButtonListener = new ZoomResetGraphButtonAction(rifGraphWindow);
        ZoomResetButton.addActionListener(ZoomResetButtonListener);
        
        add(ZoomInXButton);
		add(ZoomOutXButton);
		add(ZoomInYButton);
        add(ZoomOutYButton);
        add(ZoomResetButton);       
    }
    

	/** Permette di modificare l'oggetto sul quale agiscono
		i pulsanti della toolbar. */    
    public void updateRifGraphWindow(ZoomGraphInterface rifGraphWindow)
    {
    	ZoomInXButtonListener.updateRifGraphWindow(rifGraphWindow);
    	ZoomOutXButtonListener.updateRifGraphWindow(rifGraphWindow);
    	ZoomInYButtonListener.updateRifGraphWindow(rifGraphWindow);
    	ZoomOutYButtonListener.updateRifGraphWindow(rifGraphWindow);
    	ZoomResetButtonListener.updateRifGraphWindow(rifGraphWindow);    	    	    	    	
    }

	public void setEnabledButtons(boolean en){
		if(en){
			ZoomInXButton.setEnabled(true);
			ZoomOutXButton.setEnabled(true);
			ZoomInYButton.setEnabled(true);
			ZoomOutYButton.setEnabled(true);
			ZoomResetButton.setEnabled(true);       			
		}
		else{
			ZoomInXButton.setEnabled(false);
			ZoomOutXButton.setEnabled(false);
			ZoomInYButton.setEnabled(false);
			ZoomOutYButton.setEnabled(false);
			ZoomResetButton.setEnabled(false);       						
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