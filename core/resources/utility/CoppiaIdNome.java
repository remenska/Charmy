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

/** Classe per creare una coppia (intero,stringa)_
	L'intero è di tipo long_
	E' usata dalla classe 'TabellaIdNome'. */

public class CoppiaIdNome implements Serializable
{

	/** Intero, primo elemento della coppia. */
	private long id;

	/** Stringa, secondo elemento della coppia. */
	private String nome;


	/** Costruttore. */
    public CoppiaIdNome(long i, String name)
    {        
		id = i;
		nome = name;
    }

	
	/** Restituisce la stringa, ovvero
		il secondo elemento della coppia. */
    public String getName()
    {
       return nome;
    }
    
    
	/** Restituisce l'intero, ovvero
		il primo elemento della coppia. */
    public long getId()
    {
    	return id;
    }
    
    
    /** Imposta il secondo elemento della coppia. */
    public void setName(String name)
    {
    	nome = name;
    }

	
	/** Imposta il primo elemento della coppia. */
	public void setId(int i)
	{
		id = i;
	}

}