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
    
package plugin.topologydiagram.data;

import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;

/**
 * Classe per lo scambio dei dati con il plug-in, contiene tutti i dati
 * che si possono scambiare con il plug-in
 *  **/
public class PlugData implements IPlugData{
	
	PlugDataManager pdm;

	/**
	 * variabile indicante una transazione, ovvero un'insieme di operazioni
	 * sui dati che riguardano la stessa sessione
	 */
	//private boolean inTransaction = false;
	
	/**
	 * identificativo di sessione della transazione, ogni movimento
	 * di transazione avr� il medesimo id
	 */
	//private long idSessione = 0;


	/**
	 * dati riguardanti la lista canali (S_A_Topology Diagram)
	 */
	private ListaCanale listaCanale = null;
	
	/** lista dei processi definiti nel S_A_ Topology Diagram. */
	private ListaProcesso listaProcesso = null;
	
	/**
	 * costruttore, per default costruisce tutte le liste vuote
	 */

	public PlugData(PlugDataManager pdm){
			this.pdm=pdm;
			pdm.addPlugin(this);
			listaProcesso = new ListaProcesso(this);
			listaCanale = new ListaCanale(this);
	}

	
	
	/**
	 * pulisce tutti i dati contenuti nelle liste
	 * la cancellazzione dei dati viene vista come 
	 * un'unica sessione
	 *
	 */
	public  synchronized void  clearAll(){
		
		this.listaProcesso.removeAll();
		this.listaCanale.removeAll();

	}
	
	/**
	 * Ritorna la lista Canali
	 * @return
	 */
	public ListaCanale getListaCanale() {
		return listaCanale;
	}
	



	/**
	 * ritorna la lista dei processi
	 * @return
	 */
	public ListaProcesso getListaProcesso() {
		return listaProcesso;
	}
	


	/* (non-Javadoc)
	 * @see data.IPlugData#setPlugDataManager(data.PlugDataManager)
	 */
	public PlugDataManager getPlugDataManager() {
		return pdm;		
	}



	/* (non-Javadoc)
	 * @see data.IPlugData#setPlugDataManager(data.PlugDataManager)
	 */
	public void setPlugDataManager(PlugDataManager pdm) {
		this.pdm=pdm;		
	}



	/* (non-Javadoc)
	 * @see data.IPlugData#activateListeners()
	 */
	public void activateListeners() {		
		plugin.statediagram.data.PlugData pdThread=(plugin.statediagram.data.PlugData)pdm.getPlugData("charmy.plugin.state");
		if(pdThread!=null)
			listaProcesso.addListener(pdThread.getListaDP());
		listaProcesso.addListener(listaCanale);		
	}

}
