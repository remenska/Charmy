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
    

package plugin.ba.action;
import java.awt.event.ActionEvent;

import plugin.ba.AlgoManagerBA;

/** Classe per gestire l'avvio del calcolo delle formule LTL_
	La classe risponde alla pressione dell'item "LTL Formule"
	nel menu "Build". */
	
public class BAItemAction extends BasicBAAction
{

    public BAItemAction(AlgoManagerBA iAM)
    {
    	super(iAM);
    }
    
         
    public void actionPerformed(ActionEvent p1) 
    {
        gestioneAlgo.BA();
    }

}