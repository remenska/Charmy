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
    
package plugin.topologychannels.resource.data;

/**
 * @author michele
 * interfaccia per l'identificazione univoca dell'Elemento, viene utilizzata
 * come base per tutti gli elementi ed i link.
 */
public interface IElementoId {
	
	
		/** 
		 * ritorna l'identificativo dell'elemento
		 * @return long rappresentante l'id dell'elemento
		 */
		public long getId();

		/**
		 * setta l'identifictivo dell'elemento
		 * @param id long rappresentante l'identificativo
		 */
		public void setId(long id);
		
		/**
		 * Stringa rappresentante il nome dell'identificativo
		 * @return nome dell'elemento
		 */
		public String getName();
		
		/**
		 * setta il nome dell'elemento
		 * @param name nome dell'elemento
		 */
		public void setName(String name);
		
		/**
		 * ritorna il tipo di elemento
		 * @return intero rappresentante il tipo di elemento
		 */
		public int getTipo();
		
		/**
		 * setta il tipo dell'oggetto
		 * @param tipo
		 */
		public boolean setTipo(int tipo);
				
		

}
