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
    
package core.resources.general.edcp;

import core.internal.runtime.data.PlugDataManager;

/**
 * @author michele
 * classe base per il sistema di cut-copy-paste
 **/
public abstract class CopyCutPaste {

	/**
	 * contenitore dei dati
	 */
	protected PlugDataManager plugData;

	public CopyCutPaste(PlugDataManager pl){
		plugData = pl;
	}

	/**
	 * metodo per la copia in memoria
	 *
	 */
	public abstract void copy();
	
	/**
	 * metodo per il paste
	 *
	 */
	public abstract void paste();
	
	/**
	 * metodo per fare il cut
	 */
	public abstract void cut();

}
