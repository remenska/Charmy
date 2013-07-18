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
    
package plugin.topologydiagram.resource.general.edcp;

import java.util.Iterator;
import java.util.LinkedList;

import plugin.topologydiagram.resource.data.IElementoId;



/**
 * @author michele
 * linked list con ricerca di elementi mediante identificatore
 * dii ElementID
 */
public class LinkedListWithID {

	/**
	 * lista interna
	 */
	private LinkedList lista;
	
	public LinkedListWithID(){
		lista = new LinkedList();
	} 
	

	/**
	 * @param Identificativo dell'elemento
	 * @return true se l'eleemnto è stato inserito
	 */
	public boolean add(IElementoId o) {
		return lista.add(o);
	}

	/**
	 * svuota il contenuto della lista
	 */
	public void clear() {
		lista.clear();
	}

	/**
	 * prelevo l'oggetto di indice index
	 * @param index
	 * @return
	 */
	public Object get(int index) {
		return lista.get(index);
	}

	/**
	 * @return l'iteratore della lista
	 */
	public Iterator iterator() {
		return lista.iterator();
	}




	/**
	 * @return true se la lista è vuota
	 */
	public boolean isEmpty() {
		return lista.isEmpty();
	}

	/**
	 * @return ritorna la dimenzione della lista
	 */
	public int size() {
		return lista.size();
	}
	
	/**
	 * ritorna l'elemento con identificatore uguale ad id
	 * @param id
	 * @return
	 */
	public Object getElementById(long id){
	    Iterator ite = lista.iterator();
	    while(ite.hasNext()){
	    	IElementoId ed = (IElementoId)ite.next();
	    	if(id == ed.getId()){
	    		return ed;
	    	}
	    }
	    return null;
	}
	
	
	/**
	 * controlla se esiste un elemento con un dato id
	 * @param id
	 * @return true se l'elemento esiste, false altrimenti
	 */
	public boolean containing(long id){
		Iterator ite = lista.iterator();
		while(ite.hasNext()){
			IElementoId ed = (IElementoId)ite.next();
			if(id == ed.getId()){
				return true;
			}
		}
		return false;
	}
	
	
}
