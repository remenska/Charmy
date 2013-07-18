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
    

package core.resources.utility;

import java.io.Serializable;
import java.util.LinkedList;

/**	Classe che memorizza la tabella delle corrispondenze
	tra oggetti di tipo stringa ed interi di tipo long_ 
	Si suppone una corrispondenza di tipo biunivoco. */

public class TabellaIdNome implements Serializable
{

	/** Memorizza la tabella delle coppie 
		(stringa,interi) come una lista. */
	private LinkedList lista;


    /** Costruttore. */
    public TabellaIdNome()
    {        
		lista = new LinkedList();
    }


    /** Dato l'intero, restituisce la stringa associata. */
    public String getName(long i)
    {
		CoppiaIdNome coppia = null;
		boolean ctrl = true;
		int j=0;
		
		while (ctrl && j<lista.size())
		{
			coppia = (CoppiaIdNome)lista.get(j);
			if (coppia.getId() == i)
			{
				ctrl = false;
			}
			j++;	
		}
		if (!ctrl)
		{
			return coppia.getName();
		}
		else
		{
			return null;
		}
    }
    
    
    /** Data la stringa, restituisce l'intero associato. */
    public long getId(String name)
    {
		CoppiaIdNome coppia = null;
		boolean ctrl = true;
		int j=0;
		
		while (ctrl && j<lista.size())
		{
			coppia = (CoppiaIdNome)lista.get(j);
			if ((coppia.getName()).equals(name))
			{
				ctrl = false;
			}
			j++;	
		}
		if (!ctrl)
		{
			return coppia.getId();
		}
		else
		{
			return -1;
		}
    }
    
    
    /** Inserisce una nuova coppia (long,stringa) nella tabella_ 
    	Manca la verifica che nella tabella non esista un'altra
    	coppia con l'intero e/o la stringa forniti in ingresso. */
    public void putCoppia(long i, String name)
    {
    	lista.add(new CoppiaIdNome(i,name));
    }

	
	/** Dato l'intero, rimuove la coppia associata. */
	public void removeCoppia(long i)
	{
		CoppiaIdNome coppia = null;
		boolean ctrl = true;
		int j=0;
		
		while (ctrl && j<lista.size())
		{
			coppia = (CoppiaIdNome)lista.get(j);
			if (coppia.getId() == i)
			{
				ctrl = false;
			}
			j++;	
		}
		if (!ctrl)
		{
			lista.remove(coppia);
		}
	}


	/** Data la stringa, rimuove la coppia associata. */
	public void removeCoppia(String name)
	{
		CoppiaIdNome coppia = null;
		boolean ctrl = true;
		int j=0;
		
		while (ctrl && j<lista.size())
		{
			coppia = (CoppiaIdNome)lista.get(j);
			if ((coppia.getName()).equals(name))
			{
				ctrl = false;
			}
			j++;	
		}
		if (!ctrl)
		{
			lista.remove(coppia);
		}
	}
	
	
	public void removeAll()
	{
		while (lista.size()>0)
		{
			lista.removeFirst();
		}
	}
	
}