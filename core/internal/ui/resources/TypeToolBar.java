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
    
package core.internal.ui.resources;

import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.editoralgo.IMainTabPanel;


/** La classe estende JToolBar introducendo un metodo per 
	la creazione dei pulsanti della toolbar ed uno per 
	l'abilitazione/disabilitazione degli stessi_
	La costante PathGif consente di definire il path per
	le icone utilizzate dai pulsanti della toolbar. */
	 
public class TypeToolBar extends JToolBar
{
 
    /** Per la costruzione del file jar, nell'ipotesi d'inserire 
    	tutte le icone in una directory icon che si trova nella 
    	stessa directory del file jar. 
    	Funziona sotto Win2000, ma non ? stato verificato
    	con altri sistemi operativi!! */
    public static String PathGif="/home/daniela/sw/charmy2.0Beta_source/core/internal/ui/icon/"; //= "F:\\documenti\\Dottorato\\Ricerca\\CHARMY\\CodiceCHARMY\\icon\\";    
    /** Tavola Hash per i pulsanti. */
    protected Hashtable TavolaPulsanti;    
    
    PlugInDescriptor pluginDescriptor=null;

   /* public static URL createURLPath(String fileName)
    {
        if(System.getProperty("os.name").startsWith("Wi")){
        	
        	String a = System.getProperty("user.dir");
        	String b = System.getProperty("user.dir").substring(2).replace('\\','/');
            PathGif="jar:file:"+System.getProperty("user.dir").substring(2).replace('\\','/')+"/Charmy.jar!/icon/";
            int y=0;
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
    }*/


    protected AbstractButton createToolbarButtonJar(String pathFileNameIcon, String strToolTipText, int intMnemonic)
    {
        
    	
    	URL internalLibrary = this.pluginDescriptor.getLibrary().getLibraryPlugEditor();
    	
    	
		//URL myurl =core.internal.datistatici.CharmyFile.createURLPath(pathFileNameIcon,pathFileNameJar);
		URL temp = null;
		File f = null;
		String newName = "file:"+pathFileNameIcon;
		try {
			temp = new URL(newName);
			//f = new File (temp.toURI()); 
		}
		catch (Exception e){
			//errore : la libreria non � un jar o non esiste il file
			System.out.println("Errore nel path dell'icona del bottone "+e);
			return null; 
		}
		
		
		/*String mypathName = internalLibrary.getFile();
		 mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		 mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		 mypathName = System.getProperty("user.dir") + File.separator + mypathName;*/
		
		// File myfile = new File(mypathName);
		
		if(/*f.exists()*/true){
			ImageIcon myicon = new ImageIcon(temp);
			JButton LocalJButton = new JButton(myicon);
		LocalJButton.setMargin(null);
			LocalJButton.setToolTipText(strToolTipText);
			 LocalJButton.setMnemonic(intMnemonic);
			
				this.TavolaPulsanti.put(strToolTipText, LocalJButton);
			
			
			return LocalJButton;
		}
		else{
			JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ internalLibrary.getPath(),"Information",JOptionPane.WARNING_MESSAGE);
			return null;
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
    
    
    protected AbstractButton createToolbarButtonJar(
			String pathFileNameIcon, String strToolTipText) {
		
		
    	URL internalLibrary = this.pluginDescriptor.getLibrary().getLibraryPlugEditor();
    	
    	String dir = this.pluginDescriptor.getDirectory();
		//URL myurl =core.internal.datistatici.CharmyFile.createURLPath(pathFileNameIcon,pathFileNameJar);
		URL temp = null;
		File f = null;
		String newName = "file:"+pathFileNameIcon;
		try {
			temp = new URL(newName);
			//f = new File (temp.toURI()); 
		}
		catch (Exception e){
			//errore : la libreria non � un jar o non esiste il file
			System.out.println("Errore nel path dell'icona del bottone "+e);
			return null;
		}
		
		
		/*String mypathName = internalLibrary.getFile();
		 mypathName = mypathName.substring(0, mypathName.indexOf('!'));
		 mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
		 mypathName = System.getProperty("user.dir") + File.separator + mypathName;*/
		
		// File myfile = new File(mypathName);
		
		if(/*f.exists()*/true){
			ImageIcon myicon = new ImageIcon(temp);
			JButton LocalJButton = new JButton(myicon);
		LocalJButton.setMargin(new Insets(0, 0, 0, 0));
			LocalJButton.setToolTipText(strToolTipText);
			// LocalJButton.setMnemonic(intMnemonic);
			
				this.TavolaPulsanti.put(strToolTipText, LocalJButton);
			
			
			return LocalJButton;
		}
		else{
			JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ internalLibrary.getPath(),"Information",JOptionPane.WARNING_MESSAGE);
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
    public TypeToolBar(String titolo,IMainTabPanel plugin)
    {
    	super(titolo);
    	
    	if (plugin!=null)
    	 pluginDescriptor = plugin.getPlugDataWin().getPlugManager().getPlugEditDescriptor(plugin);
    	 TavolaPulsanti = new Hashtable();
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

}