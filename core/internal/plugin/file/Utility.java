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
    
package core.internal.plugin.file;

public class Utility {

	
	/**
	 * trasforma il valore stringa false / true nel corripondente valore booleano
	 * @param stringa
	 * @return boleano rappresentato dalla stringa
	 */
	public boolean strToBool(String stringa){
		if(stringa.equals("false")){
			return false;
		}
		else
			return true;		
	}
	
	/**
	 * trasforma il valore stringa XXX_numero nel corripondente valore long
	 * @param stringa
	 * @return long rappresentato dalla stringa
	 */
	public long strToId(String stringa){
		if(stringa.length()>3)
			return  Long.parseLong(stringa.substring(4));
		else
			return Long.parseLong(stringa);		
	}

	
	/**
	 * trasforma il valore stringa XXX_numero nella estenzione XXX
	 * @param stringa
	 * @return stringa contenente l'acronimo della classe della classe
	 */
	public String strToAcronimo(String stringa){
		if(stringa.length()>4)
			return stringa.substring(1,3);
		else
			return stringa;
	}
	
}
