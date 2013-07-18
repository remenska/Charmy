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
    

package plugin.statediagram.action;
import java.awt.event.ActionEvent;

import core.internal.ui.action.toolbar.edit.GraphButtonAction;

import plugin.statediagram.StateWindow;
import plugin.statediagram.ThreadEditor;
import plugin.statediagram.toolbar.StateToolBar;

/** Permette di tornare nella condizione di Ready. */

public class ReadyButtonAction extends GraphButtonAction 
{
 /** Riferimento alla state toolBar */
    private StateToolBar stateTB;
    
 /** Riferimetno alla Finestra dello state*/
    private StateWindow finestra;
    
    public ReadyButtonAction(StateWindow clwnd,StateToolBar stb)
    {
        super(clwnd);
        stateTB=stb;
        finestra=clwnd;
    }
    
    
    public void actionPerformed(ActionEvent p1) 
    {
        stateTB.setNoPressed();
		finestra.getCurrentThreadEditor().setDeselectedAll();
        finestra.setWindowStatus(ThreadEditor.ATTESA,0,false);
    }

}