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
    
package plugin.ba.utility;

import java.util.LinkedList;

import javax.swing.JTextArea;

/** Classe per gestire la visualizzazione delle LTL formule_
	Estensione della JTextArea di Java. */

public class JTextAreaForBA extends JTextArea
{
	
	/** Costruttore. */
	public JTextAreaForBA()
	{
		super();
	}
	
	
	/** Visualizza nell'area di testo la lista di stringhe in ingresso. */
	public void setTextArea(LinkedList ListaDiStringhe)
	{
		String currentRow;
		
		if (ListaDiStringhe != null)
		{
			setText("");
			for (int i=0; i<ListaDiStringhe.size(); i++)
			{
				currentRow = (String)(ListaDiStringhe.get(i))+"\n";
				append(currentRow);
			}
		}
                this.repaint();
	}
	
}	