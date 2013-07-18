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
    

package core.internal.plugin.editoralgo;

import core.internal.runtime.data.IPlugData;
import core.internal.ui.PlugDataWin;

/**
 * @author Michele Stoduto
 * La classe implementa l'interfaccia IMainTabPanel, per velocizzare la scrittura di nuovi plugin
 * fornendo funzioni gia pronte per l'interfaccia con il plugin manager
 * l'unica funzione da definire ? la init(), dichiarata astratta
 * 
 */
public abstract class ImpMainTabPanel {

	
	/**
	 * riferimento ai dati delle finestre
	 */
	protected PlugDataWin plugDataWin;
	
	
	/**
	 * riferimento alla Struttura dati
	 */
	protected IPlugData plugData;
	

	

	/**
	 * Inizializzazione del Sistema
	 */
	protected abstract void init();


	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#setDati(plugin.PlugDataWin, plugin.PlugData)
	 */
	public void setDati(PlugDataWin plugDtW, IPlugData pd) {
		plugDataWin = plugDtW;
		plugData= pd;
		init();
	}

	public void startUndoRedo(){
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugDataWin()
	 */
	public PlugDataWin getPlugDataWin() {
		return plugDataWin;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateActive(javax.swing.event.ChangeEvent)
	 */
	public void stateActive() {}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#removeToolbar()
	 */
	public void stateInactive() {}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getPlugData()
	 */
	public IPlugData getPlugData() {
		return plugData;
	}
	
	
}
