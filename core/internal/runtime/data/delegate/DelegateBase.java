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
    

package core.internal.runtime.data.delegate;

import java.util.Vector;

import core.internal.runtime.data.IPlugData;



/**
 * @author michele
 * fornisce funzioni comuni alle classi di delega.
 * ed � la classe base delle deleghe, contiene il riferimento al plug data
 * e le regole per sincronizzare la sessione
 */
public class DelegateBase extends Vector {

	/**
	 * contenitore dei dati
	 */
	protected IPlugData plugData;

	/**
	 * Costruttore
	 * @param plugData, contenitore dei dati
	 */
	public DelegateBase(IPlugData plugData) {
		this.plugData = plugData;
	}

	/**
	 * controlla se la sessione ha una transazione attiva, se la transizione non era attiva 
	 * la attiva, e restituisce false, altrimenti true, il numero di sessione va recuperato mediante 
	 * <code>plugData.getIdSessione()</code>
	 * @param plugData
	 * @return true se la transazione era attiva, false attiva una transazione e ritorna
	 */
	protected synchronized boolean startSessione() {	
		return plugData.getPlugDataManager().startSessione();
	}

	/**
	 * ferma la transazione corrente, prende in ingresso il risultato di <code>checkSessione()</code>
	 * @param bo false, ferma la transazione, true non fermarla
	 */
	protected synchronized void stopSessione(boolean bo) {
		//if (!bo) {
			plugData.getPlugDataManager().stopSessione(bo);
		//}
	}

}
