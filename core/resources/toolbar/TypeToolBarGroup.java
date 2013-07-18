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
    

package core.resources.toolbar;

import java.awt.Insets;
import java.io.File;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import core.internal.ui.resources.TypeToolBar;
//import javax.swing.border.BevelBorder;

/** La classe estende JToolBar introducendo un metodo per 
	la creazione dei pulsanti della toolbar ed uno per 
	l'abilitazione/disabilitazione degli stessi_
	Attraverso la gestione di un gruppo di pulsanti
	La costante PathGif consente di definire il path per
	le icone utilizzate dai pulsanti della toolbar. */
	 
public class TypeToolBarGroup extends TypeToolBar
{
 

    private ButtonGroup btGroup ;
    
	/** Crea un nuovo pulsante. 
	 * di tipo Toogle
	 * */
    //???Patrizio???
    protected AbstractButton createToolbarToogleButton(String pathFileNameIcon, String pathFileNameJar,String strToolTipText)
    {
    	
    	 URL myurl = core.internal.datistatici.CharmyFile.createURLPath(pathFileNameIcon,pathFileNameJar);
               // URL myurl = createURLPath(fileNameIcon);
                String mypathName = myurl.getFile();
                mypathName = mypathName.substring(0, mypathName.indexOf('!'));
                mypathName = mypathName.substring(mypathName.lastIndexOf('/')+1, mypathName.length());
                mypathName = System.getProperty("user.dir") + File.separator + mypathName;
                
                File myfile = new File(mypathName);
                if(myfile.exists())
                {
                    ImageIcon myicon = new ImageIcon(myurl);
                    AbstractButton LocalJButton = new JToggleButton(myicon);
                    LocalJButton.setMargin(new Insets(0, 0, 0, 0));
                    LocalJButton.setToolTipText(strToolTipText);
                    return LocalJButton;
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Unable to load the buttons icons.\nThe .jar file must be in the directory: "+ mypathName,"Information",JOptionPane.WARNING_MESSAGE);
                    return null;
                }
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
     * Aggiunge un bottone sia al gruppo che alla toolbar
     * @param abstractButton
     */
    public void addGroupButton(AbstractButton abstractButton ){
    	btGroup.add(abstractButton);
    	add(abstractButton);
    }
    
    /** Costruttore. */
    public TypeToolBarGroup(String titolo)
    {
    	super(titolo,null);
    	btGroup = new ButtonGroup();
    }
}