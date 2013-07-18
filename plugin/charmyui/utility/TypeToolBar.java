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
    
package plugin.charmyui.utility;

import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import core.internal.extensionpoint.IExtensionPoint;
import core.internal.extensionpoint.event.EventService;
import core.internal.plugin.editoralgo.IMainTabPanel;

import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;
import plugin.charmyui.extensionpoint.toolbar.IHostToolbar;




/** La classe estende JToolBar introducendo un metodo per 
	la creazione dei pulsanti della toolbar ed uno per 
	l'abilitazione/disabilitazione degli stessi_
	La costante PathGif consente di definire il path per
	le icone utilizzate dai pulsanti della toolbar. */
	 
public class TypeToolBar extends JToolBar implements IHostToolbar{
 
    
    /** Per la costruzione del file jar, nell'ipotesi d'inserire 
    	tutte le icone in una directory icon che si trova nella 
    	stessa directory del file jar. 
    	Funziona sotto Win2000, ma non ? stato verificato
    	con altri sistemi operativi!! */
    public static String PathGif=""; //= "F:\\documenti\\Dottorato\\Ricerca\\CHARMY\\CodiceCHARMY\\icon\\";    
    /** Tavola Hash per i pulsanti. */
    protected Hashtable TavolaPulsanti;  
    
    private Border DefaultBorder;
    
    private EventService eventService;
    
    public ExtensionPointToolbar extensionPointToolbar;
    
    public IMainTabPanel pluginOwner=null;
    
   
    
    public static URL createURLPath(String fileName)
    {
        if(System.getProperty("os.name").startsWith("Wi")){
            PathGif="jar:file:"+System.getProperty("user.dir").substring(2).replace('\\','/')+"/Charmy.jar!/icon/";
        }
        else
            PathGif="jar:file:"+System.getProperty("user.dir")+"/Charmy.jar!/icon/";
        try{
            return new URL(PathGif+fileName);
        }
        catch(Exception e){
            System.out.println("TypeToolBar.createURLPath ERROR: "+e);
            return null;
        }
    }


    protected AbstractButton createToolbarButtonJar(String fileNameIcon, String strToolTipText)
    {
        URL myurl = createURLPath(fileNameIcon);
        String mypathName = myurl.getFile();
        mypathName = mypathName.substring(0, mypathName.indexOf('!'));
        mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
        mypathName = System.getProperty("user.dir") + File.separator + mypathName;
        
        File myfile = new File(mypathName);
        if(myfile.exists()){
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
    }

    
    /** Abilita/disabilita un pulsante selezionandolo tramite
    	nameButton (nome associato al pulsante). */
    public void setButtonEnabled(String nameButton, boolean ctrlIfEnabled)
    {
    	JButton LocalJButton;
    	if (TavolaPulsanti != null){
    		LocalJButton = (JButton)TavolaPulsanti.get(nameButton);
    		LocalJButton.setEnabled(ctrlIfEnabled); 
    	}
    }
    
    
    /** Costruttore. */
    public TypeToolBar()
    {
    	super();
    	DefaultBorder = new EmptyBorder(3,3,3,3);
    }
    
    /** Costruttore. */
    public TypeToolBar(String nomeToolbar)
    {
    	super();
    	DefaultBorder = new EmptyBorder(3,3,3,3);
    	this.setName(nomeToolbar);
    	
    }
  
    
	public JButton createToolbarButton(String fileNameIcon, String strToolTipText, int intMnemonic)
	{
		// URL myurl = createURLPathGif(fileNameIcon);
		// String mypathName = myurl.getFile();
		// mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		// mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		// mypathName = System.getProperty("user.dir") + File.separator + mypathName;
		String mypathName = fileNameIcon;

		 File myfile = new File(mypathName);
		 if(myfile.exists())
		 {
			 ImageIcon myicon = new ImageIcon(mypathName); //myurl);
			 JButton LocalJButton = new JButton(myicon);
			 LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			 LocalJButton.setToolTipText(strToolTipText);
			 LocalJButton.setMnemonic(intMnemonic);
			 return LocalJButton;
		 }
		 else
		 {
			 JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.:\n "+ mypathName,"Information",JOptionPane.WARNING_MESSAGE);
			 return null;
		 }
	}

	public JButton createToolbarButton(String fileNameIcon, String strToolTipText)
	{
		// URL myurl = createURLPathGif(fileNameIcon);
		// String mypathName = myurl.getFile();
		// mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		// mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		// mypathName = System.getProperty("user.dir") + File.separator + mypathName;
		String mypathName = fileNameIcon;

		 File myfile = new File(mypathName);
		 if(myfile.exists())
		 {
			 ImageIcon myicon = new ImageIcon(mypathName); //myurl);
			 JButton LocalJButton = new JButton(myicon);
			 LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			 LocalJButton.setToolTipText(strToolTipText);
			 return LocalJButton;
		 }
		 else
		 {
			 JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.:\n "+ mypathName,"Information",JOptionPane.WARNING_MESSAGE);
			 return null;
		 }
	}
	 
	/** Riporta nella posizione di riposo tutti i pulsanti della toolbar. */    
	public void setNoPressed(){
	    
	   // if (extensionPoint==null){
	        for(int i=0;i<getComponentCount();i++){
	            AbstractButton button = (AbstractButton)this.getComponent(i);
	            button.setBorder(DefaultBorder);
	        }
	   // }
	  //  else
	   //     extensionPoint.setAllButtonNoPressed(this);
	}


	//???Patrizio???
	/*    
	    protected AbstractButton createToolbarToogleButton(String fileNameIcon, String strToolTipText)
	    {
			AbstractButton LocalJButton = new JToggleButton(new ImageIcon(PathGif + fileNameIcon));                
			LocalJButton.setMargin(new Insets(0, 0, 0, 0));
	                LocalJButton.setBorder(new BevelBorder(0));
			LocalJButton.setToolTipText(strToolTipText);
			return LocalJButton;
	    }*/
	    
	  
	    /**
	     * Crea e aggiunge un bottone alla toolbar
	     * @param abstractButton
	     */
	    public AbstractButton addButton(String fileNameIcon, String strToolTipText ){
	    	
	    	AbstractButton button = createToolbarButton(fileNameIcon,strToolTipText);
	    	add(button);
	    	return button;
	    }
	    
	    /**
	     * Crea e aggiunge un bottone alla toolbar con set del campo intMnemonic del bottone.
	     * @param abstractButton
	     */
	    public AbstractButton addButton(String fileNameIcon, String strToolTipText, int intMnemonic ){
	    	
	    	AbstractButton button = createToolbarButton(fileNameIcon,strToolTipText,intMnemonic);
	    	add(button);
	    	return button;
	    }
    
	/**
	 * @return Returns the eventService.
	 */
	public EventService getEventService() {
		return eventService;
	}
	/* (non-Javadoc)
	 * @see core.extensionpoint.IGenericHost#getIdHost()
	 */
	public String getIdHost() {
		// TODO Auto-generated method stub
		return null;
	}


	public IExtensionPoint getExtensionPointOwner() {
		// TODO Auto-generated method stub
		return this.extensionPointToolbar;
	}


	public IMainTabPanel getPluginOwner() {
		// TODO Auto-generated method stub
		return this.pluginOwner;
	}


	public void setExtensionPointOwner(IExtensionPoint extensionpoint) {
		this.extensionPointToolbar= (ExtensionPointToolbar)extensionpoint;
	}


	public void setPluginOwner(IMainTabPanel plugin) {
		this.pluginOwner=plugin;
	}


	public void setEventService(EventService eventService) {
		this.eventService=eventService;
	}


	public void notifyMessage(Object callerObject, int status, String message) {
		// TODO Auto-generated method stub
		
	}

}