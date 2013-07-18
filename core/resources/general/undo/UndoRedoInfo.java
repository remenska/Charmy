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
    

package core.resources.general.undo;

/**
 * @author Michele Stoduto
 * Progetto Charmy
 * Informa sul tipo di messaggio undo/redo da eleborare, 
 * prende un identificativo del tipo di messaggio(aggiunta, rimozione , refresh etc)
 * ed un generico Object per poter inserire qualunque tipo di dato(legato al tipo di messaggio)
 */
public class UndoRedoInfo {



	/**
	 * indica il tipo di comando registrato
	 */
	private int tipo;
	
	
	/**
	 * oggetto dipendente dal tipo
	 */
	private Object object;
	
	
	public UndoRedoInfo(int tipo, Object object){
		this.tipo = tipo;
		this.object = object;
	}
			
	/**
	 * ritorna l'oggetto memorizzato(il formato dell'oggetto dipende da tipo)
	 * @return oggetto inserito
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * ritorna il tipo dell'oggetto
	 * @return int tipo oggetto
	 */
	public int getTipo() {
		return tipo;
	}
	

	/**
	 * setta l'oggetto
	 * @param object
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * setta il tipo di oggetto
	 * @param i
	 */
	public void setTipo(int i) {
		tipo = i;
	}


}
