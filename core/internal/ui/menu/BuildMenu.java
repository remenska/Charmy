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

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import core.internal.plugin.algoritmo.AlgoManager;
import core.internal.ui.resources.TypeJMenu;


/** La classe crea il menu Build. */
	
public class BuildMenu extends TypeJMenu{
	
    private JMenu PromelaMenuBuild;
    /** Item per il calcolo delle LTL formule. */
    private JMenuItem LTLMenuBuild;
    
    private JMenuItem SavePromelaMenuBuild;
    private JMenuItem SaveLTLMenuBuild;
    private JMenuItem Algo1,Algo2;
    
    /** Costruttore. */
    public BuildMenu(AlgoManager istanzaAM)
    {
	super("Plug");
  
    }
    
}