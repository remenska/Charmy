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
    

package plugin.topologydiagram.resource.data;

import plugin.topologydiagram.resource.data.interfacce.IUpdate;

/**
 * @author michele
 * Classe per implementare la gestione delle modifiche relative
 * ai vari Elementi, implementa una parte di IUpdate, e lascia implementare le
 * funzioni specifiche alla classe che eredita ImpUpdate, inoltre � una estensione di 
 * ElementoId, questa metodologia � necessaria poich� java non permette eredit� multiple
 * 
 */
public abstract class ImpUpdate implements IUpdate {

	protected boolean sendMsg = true;
	

	/* (non-Javadoc)
	 * @see data.interfacce.IUpdate#informPreUpdate()
	 */
	public abstract void informPreUpdate();

	/* (non-Javadoc)
	 * @see data.interfacce.IUpdate#informPostUpdate()
	 */
	public abstract void informPostUpdate();


	/* (non-Javadoc)
	 * @see data.IUpdate#disable()
	 */
	public void disable() {
		sendMsg = false;
	}

	/* (non-Javadoc)
	 * @see data.IUpdate#enabled()
	 */
	public void enabled() {
		sendMsg = true;
	}

	/**
	 * ritorna lo stato del sender
	 */
	public boolean getStato() {
		return sendMsg;
	}
	
	
	/**
	 * 
	 * @author michele
	 * disabilita o meno l'invio dell'informazione controllandone 
	 * lo stato attuale, e ritorna il lo stato precedente, e attiva una precedure di preUpdate
	 * se lo stato precedente � true, debbo inoltre provvedere a riabilitare il sistema di messaggistica 
	 * all'uscita della funzione
	 */
	public synchronized boolean testAndSet(){
		if(sendMsg){
			informPreUpdate();
			disable();
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @author michele
	 * riporta lo stato a come era prima di entrare nella funzione 
	 * praticamente prende in ingresso il valore booleano restituito da testAndSet()
	 * se lo stato precedente � true, debbo provvedere a riabilitare il sistema di messaggistica 
	 * all'uscita della funzione, e ad inviare un postUpdate()
	 */
	public synchronized void testAndReset(boolean oldStato){
		if(oldStato){
			enabled();
			informPostUpdate();
		}
	}
	
	
}
