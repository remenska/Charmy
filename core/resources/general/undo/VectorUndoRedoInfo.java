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

import java.util.Vector;

/**
 * @author michele
 * Vettore per contenere l'insieme di messaggi con lo stesso id di Sessione
 */
public class VectorUndoRedoInfo extends Vector {

	private long idSessione = 0;

	
	/**
	 * ritorna l'id di sessione relativo agli eventi contenuti nella classe
	 * @return long rappresentante l'id di sessione
	 */
	public long getIdSessione() {
		return idSessione;
	}

	/**
	 * setta l'id di sessione della classe
	 * @param idSessione
	 */
	public void setIdSessione(long idSessione) {
		this.idSessione = idSessione;
	}


}
