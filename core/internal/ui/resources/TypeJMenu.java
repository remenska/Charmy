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
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;



/** Superclasse per la creazione e gestione dei vari menu. */
	
public class TypeJMenu extends JMenu
{
 
    /** Memorizza il path per le icone. */
    protected static String PathGif = TypeToolBar.PathGif;
    
    /** Tavola Hash per i pulsanti. */
    protected Hashtable TavolaItems; 
        
    /** Costruttore. */
    public TypeJMenu(String titolo)
    {
		super(titolo);   
    }

	
	/** Crea un JMenuItem usando i parametri name e icon. */
    protected JMenuItem createMenuItem(String name,Icon icon)
    {
		return new JMenuItem(name,icon);
    }

	
	/** Crea un JMenuItem usando il parametro icon. */
    protected JMenuItem createMenuItem(String name)
    {
		return new JMenuItem(name);
    }

    /** Abilita/disabilita un pulsante selezionandolo tramite
    	nameButton (nome associato al pulsante). */
    public void setItemEnabled(String nameItem, boolean ctrlIfEnabled)
    {
    	JMenuItem LocalJMenuItem;
    	if (TavolaItems != null)
    	{
    		LocalJMenuItem = (JMenuItem)TavolaItems.get(nameItem);
    		LocalJMenuItem.setEnabled(ctrlIfEnabled); 
    	}
    }
        
}