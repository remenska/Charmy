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

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import plugin.ba.action.BAItemAction;
import plugin.ba.action.SaveBAItemAction;
import core.internal.ui.resources.TypeJMenu;


/** La classe crea il menu Build. */
	
public class MenuBA extends TypeJMenu
{

    /** Item per il calcolo delle LTL formule. */
    private JMenuItem BAMenuBuild;
    
    private JMenuItem SaveBAMenuBuild;
    //riferimento agli item del menu per cambiare la finestra d'azione
    private BAItemAction baItem;
    private SaveBAItemAction saveBA;
	
    /** Costruttore. */
    public MenuBA(AlgoManagerBA istanzaAM)
    {
	super("Buchi Automata Menu");
		BAMenuBuild = createMenuItem("Buchi Automata Generation");
		BAMenuBuild.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12,InputEvent.CTRL_MASK));
		baItem = new BAItemAction(istanzaAM);
		BAMenuBuild.addActionListener(baItem);
		SaveBAMenuBuild = createMenuItem("Save Buchi Automata");
		saveBA = new SaveBAItemAction(istanzaAM);
		SaveBAMenuBuild.addActionListener(saveBA);        		   	
		add(BAMenuBuild);
		addSeparator();
		add(SaveBAMenuBuild);
		
    }
    
    
    /** Aggiornamento del riferimento alla finestra attiva. */    
    public void updateRifAlgo(AlgoManagerBA rifAlgo)
    
    {
    	saveBA.updateRifAlgo(rifAlgo);
    	baItem.updateRifAlgo(rifAlgo);
    	
    }
}