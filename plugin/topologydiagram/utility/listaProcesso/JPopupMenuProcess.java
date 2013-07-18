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
    


package plugin.topologydiagram.utility.listaProcesso;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

//import plugin.topologydiagram.action.popupprocess.AddProcessItemAction;
import plugin.topologydiagram.action.popupprocess.ProProcessItemAction;


/** La classe estende JPopupMenu introducendo l'inizializzazione con i
	JMenuItem per gestire l'aggiunta, la modifica delle proprietà e
	l'eliminazione di un processo inserito direttamente dall'utente nella
	finestra dello State Diagram (senza passare per S_A_ Topology Diagram). */

public class JPopupMenuProcess 
	extends JPopupMenu
{

    /** JMenuItem per aggiungere un processo. */
    private JMenuItem addProcess;

    /** JMenuItem per impostare le proprietà di un processo. */
    private JMenuItem proProcess;

    /** JMenuItem per eliminare un processo. */
    private JMenuItem delProcess;


    /** Costruttore. */
    /*public JPopupMenuProcess(StateWindow sw)
    {
        super();

		addProcess = new JMenuItem("Add Process");
		addProcess.addActionListener(new AddProcessItemAction(sw));

		proProcess = new JMenuItem("Process Properties");
		proProcess.addActionListener(new ProProcessItemAction(sw));

		delProcess = new JMenuItem("Delete Process");
		delProcess.addActionListener(new DelProcessItemAction(sw));

		add(addProcess);
		add(proProcess);
		add(delProcess);
    }
    */

		public JPopupMenuProcess(JListaProcesso sw)
		{
			super();

//			addProcess = new JMenuItem("Add Process");
//			addProcess.addActionListener(new AddProcessItemAction(sw));
		
			proProcess = new JMenuItem("Process Properties");
			proProcess.addActionListener(new ProProcessItemAction(sw));

//			delProcess = new JMenuItem("Delete Process");
//			delProcess.addActionListener(new DelProcessItemAction(sw));
		
//			add(addProcess);
			add(proProcess);
//			add(delProcess);
		}

    
	/** Metodo per abilitare soltanto il JMenuItem
		relativo all'aggiunta di un processo. */
	public void setOnlyAddProcessEnabled()
	{
//		addProcess.setEnabled(true);
		proProcess.setEnabled(false);
//		delProcess.setEnabled(false);
	}


	/** Abilita tutti i JMenuItem. */
	public void setAllEnabled()
	{
//		addProcess.setEnabled(true);
		proProcess.setEnabled(true);
//		delProcess.setEnabled(true);
	}

}
