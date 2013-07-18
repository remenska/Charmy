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
    

package plugin.promela;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import core.internal.ui.resources.TypeJMenu;

import plugin.promela.action.PromelaItemAction;
import plugin.promela.action.SavePromelaItemAction;

/** La classe crea il menu relativa al plug-in promela. */
	
public class MenuPromela extends TypeJMenu
{

    private JMenuItem SavePromelaMenuBuild;
    private JMenuItem Algo1;
    
    /** Costruttore. */
    public MenuPromela(AlgoManagerProm istanzaAM)
    {
	super("Promela Specified");
        Algo1 = new JMenuItem("Promela Code Generation");
        Algo1.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, InputEvent.CTRL_MASK));
        Algo1.addActionListener(new PromelaItemAction(istanzaAM));
        add(Algo1);
        SavePromelaMenuBuild = createMenuItem("Save Promela");
        SavePromelaMenuBuild.addActionListener(new SavePromelaItemAction(istanzaAM));        			
		addSeparator();
		add(SavePromelaMenuBuild);
    }
    
}