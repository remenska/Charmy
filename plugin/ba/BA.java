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
    


package plugin.ba;

import java.io.Serializable;
import java.util.LinkedList;

/** Classe per la memorizzazione delle LTL formule relative
	ad un Sequence Diagram_ Ciascuna LTL formula ? implementata
	con una LinkedList per ottenere una lista di stringhe. */

public class BA implements Serializable
{
	
	/** Memorizza una LTL formula di tipo "A-Exist". */
	private LinkedList typeBA_text;

	/** Memorizza una LTL formula di tipo "A-Any". */	
	private LinkedList typeBA_graph;
	
	/** Costruttore. */
	public BA()
	{
		typeBA_text = new LinkedList();
		typeBA_graph = new LinkedList();
	}
	
	public BA(LinkedList typeBA_text,LinkedList typeBA_graph)
	{
		this.typeBA_text = typeBA_text;
		this.typeBA_graph = typeBA_graph;
	}
        
	/** Metodo per impostare la LTL formula di tipo "A-Exist". */
	public void setTypeBA_text(LinkedList listBA_text)
	{
		typeBA_text = listBA_text;
	}
	

	/** Metodo per impostare la LTL formula di tipo "A-Any". */	
	public void setTypeBA_graph(LinkedList listBA_graph)
	{
		typeBA_text = listBA_graph;
	}	
			
	/** Restituisce la LTL formula di tipo "A-Exist". */
	public LinkedList getTypeBA_text()
	{
		return typeBA_text;
	}
	

	/** Restituisce la LTL formula di tipo "A-Any". */		
	public LinkedList getTypeBA_graph()
	{
		return typeBA_graph;
	}
	
}