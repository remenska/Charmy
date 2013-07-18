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
    
package core.internal.ui.action.menu.help;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**	La classe risponde alla pressione dell'item "Help"
	nel menu "Help". */
	
public class CharmItemAction implements ActionListener
{
    
    public void actionPerformed(ActionEvent p1) 
    {
        String titolo=core.internal.datistatici.CharmyFile.DESCRIZIONE;
		String messaggio="Charmy (CHecking ARchitectural Model consistencY) \n"+
						 "is a framework that aims at assisting the software architect\n"+
						 "in designing Software Architectures and in validating them \n"+
						 "against functional requirements.\n"+
						 "State machines and scenarios are the source notations \n"+
						 "for specifying Software Architectures and their behavioral properties.\n"+
						 "Model checking techniques are used to check the consistency \n"+
						 "between the software architecture and the functional requirements. \n"+
						 "The model checker SPIN is the verification engine in Charmy; \n"+
						 "a Promela specification and Buchi Automata, modelling the software architecture \n"+
						 "and the requirements respectively, are both derived from the source notations.\n"+
						 "SPIN takes in input such specifications and performs model checking. \n"+
						 "A software process is associated to the framework to help identifying and\n"+
						 "refining architectural models.";
						 
		JOptionPane.showMessageDialog(null, messaggio,titolo,JOptionPane.INFORMATION_MESSAGE);
    }

}