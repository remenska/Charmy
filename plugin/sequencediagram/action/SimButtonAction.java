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
    

package plugin.sequencediagram.action;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import plugin.sequencediagram.SequenceEditor;
import plugin.sequencediagram.SequenceWindow;
import plugin.sequencediagram.toolbar.SequenceToolBar;

/** Gestione dell'azione di inserimento dell'operatore simultaneo*/

public class SimButtonAction extends MouseAdapter implements MouseListener 
{
    
    private SequenceWindow Finestra;
    private SequenceToolBar rifToolBar;
    private AbstractButton pulsante;

    
    public SimButtonAction(SequenceWindow clwnd, SequenceToolBar tbar, AbstractButton pls)
    {
        Finestra = clwnd;
        rifToolBar = tbar;
        pulsante = pls;
    }
    
    
    public void mouseReleased(MouseEvent evt) 
    {
        int numClick = evt.getClickCount();
        
        // Reset della "SequenceToolBar".
        rifToolBar.setNoPressed();
        if (SwingUtilities.isLeftMouseButton(evt))
        {           
            if (numClick == 1)
            {
                pulsante.setBorder(BorderFactory.createBevelBorder(1));
                Finestra.setWindowStatus(SequenceEditor.SIM,0,0,false);            
            }
            else
            {
            	// Doppio click => Blocco del pulsante.
                pulsante.setBorder(BorderFactory.createBevelBorder(1,Color.LIGHT_GRAY,Color.GRAY));
                Finestra.setWindowStatus(SequenceEditor.SIM,0,0,true);            
            }
        }
        else
        {
        	// Pressione del tasto destro del mouse => Sblocco pulsante.
        	Finestra.setWindowStatus(SequenceEditor.ATTESA,0,0,false);                
        }
        rifToolBar.revalidate();      
    }
        
}

