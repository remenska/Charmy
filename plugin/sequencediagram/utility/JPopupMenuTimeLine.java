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
    

package plugin.sequencediagram.utility;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import plugin.sequencediagram.SequenceEditor;
import plugin.sequencediagram.action.popuptimeline.AddTimeLineItemAction;
import plugin.sequencediagram.action.popuptimeline.DelTimeLineItemAction;
import plugin.sequencediagram.action.popuptimeline.ProTimeLineItemAction;

/** La classe estende JPopupMenu introducendo l'inizializzazione con
	i JMenuItem per gestire l'aggiunta, la modifica delle proprietà e
	e l'eliminazione di una Time's Line. */

public class JPopupMenuTimeLine extends JPopupMenu
{

    /** JMenuItem per aggiungere una Time's Line. */
    private JMenuItem addTimeLine;
    
    /** JMenuItem per impostare le proprietà di una Time's Line. */
    private JMenuItem proTimeLine;
    
    /** JMenuItem per eliminare una Time's Line. */
    private JMenuItem delTimeLine;


    /** Costruttore. */
    public JPopupMenuTimeLine(SequenceEditor sw)
    {
        super();

		addTimeLine = new JMenuItem("Add Time's Line First");
		addTimeLine.addActionListener(new AddTimeLineItemAction(sw));

		proTimeLine = new JMenuItem("Time's Line Properties");
		proTimeLine.addActionListener(new ProTimeLineItemAction(sw));

		delTimeLine = new JMenuItem("Delete Time's Line");
		delTimeLine.addActionListener(new DelTimeLineItemAction(sw));
		
		add(addTimeLine);
		proTimeLine.setEnabled(false);
		add(proTimeLine);
		add(delTimeLine);
    }


	/** Abilita tutti i JMenuItem. */
	public void setAllEnabled()
	{
		addTimeLine.setEnabled(true);
		proTimeLine.setEnabled(true);
		delTimeLine.setEnabled(true);
	}

}
