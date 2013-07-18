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
    


package core.internal.ui.action.toolbar.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import core.internal.ui.simpleinterface.ZoomGraphInterface;


/** Superclasse per la gestione di "actions" utilizzabili in diverse
	situazioni_ Esempio: l'operazione "ZoomInX" è utilizzabile nei
	disegni "S_A_ Topology", "State Diagram" e "Sequence Diagram". */

public abstract class ZoomGraphButtonAction implements ActionListener 
{
    
    /**	Memorizza il riferimento alla finestra attiva:
    	TopologyWindow, StateWindow, SequenceWindow, PromelaWindow. */     
    protected ZoomGraphInterface Finestra;
    
    
    public ZoomGraphButtonAction(ZoomGraphInterface rifGraphWindow)
    {
        Finestra = rifGraphWindow;
    }
    
	/**
	 * costruttore semplice
	 * aggiunto per plug-in
	 */
	public ZoomGraphButtonAction()
	{
		Finestra = null;
	}
    
    
    
    
    public abstract void actionPerformed(ActionEvent p1);
     

    /** Aggiornamento del riferimento alla finestra attiva. */         
    public void updateRifGraphWindow(ZoomGraphInterface rifGraphWindow)
    {
    	Finestra = rifGraphWindow;
    }

} 