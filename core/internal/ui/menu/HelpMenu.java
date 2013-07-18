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
    
package core.internal.ui.menu;
import javax.swing.JMenuItem;

import core.internal.ui.action.menu.help.AboutItemAction;
import core.internal.ui.action.menu.help.CharmItemAction;
import core.internal.ui.resources.TypeJMenu;

//import plugin.topologydiagram.action.menuhelp.TopologyItemAction;
//import plugin.statediagram.action.menuhelp.StateItemAction;
//import plugin.sequencediagram.action.menuhelp.SequenceItemAction;


/** La classe crea il menu Help. */
	
public class HelpMenu extends TypeJMenu
{
    /** Item About. */
    private JMenuItem CharmMenuHelp;
    private JMenuItem TopologyMenuHelp;
    private JMenuItem StateMenuHelp;
    private JMenuItem SequenceMenuHelp;
    private JMenuItem AboutMenuHelp;
    
    
    /** Costruttore. */
    public HelpMenu()
    {
		super("Help");
		CharmMenuHelp = createMenuItem("CHARMY Program");
        CharmMenuHelp.addActionListener(new CharmItemAction());		
		
/*		TopologyMenuHelp = createMenuItem("Topology Panel");
        TopologyMenuHelp.addActionListener(new TopologyItemAction());		
		
		StateMenuHelp = createMenuItem("State Panel");
        StateMenuHelp.addActionListener(new StateItemAction());		
		
		SequenceMenuHelp = createMenuItem("Sequence Panel");
        SequenceMenuHelp.addActionListener(new SequenceItemAction());		*/
		
		AboutMenuHelp = createMenuItem("About...");
        AboutMenuHelp.addActionListener(new AboutItemAction());		   	
		
		add(CharmMenuHelp);
//		addSeparator();
//		add(TopologyMenuHelp);
//		add(StateMenuHelp);
//		add(SequenceMenuHelp);
//		addSeparator();
		add(AboutMenuHelp);    
    }
    
}