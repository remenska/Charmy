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
    

package plugin.topologydiagram.resource.data.interfacce;

import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;

/**
 * interfaccia per implementare un gestore di notifica di aggiunta/rimoz/update Canali
 * @author michele
 * Charmy plug-in
 **/
public interface IListaCanMessNotify {

	/** 
	 * Notifica l'inserimento di un nuovo Canale
	 */
	public void notifyAdd(ElementoCanaleMessaggio ecm);

	/** 
	 * Notifica la cancellazione di un nuovo Elemento 
	 */
	public void notifyRemove(ElementoCanaleMessaggio ecm);

	/** 
	 * Notifica la Modifica di un nuovo Elemento 
	 */
	public void notifyUpdate(
		ElementoCanaleMessaggio oldecm,
		ElementoCanaleMessaggio newecm);

	/** 
	 * Notifica la cancellazione di un nuovo Elemento 
	 */
	public void refreshCanale();
	
	
	/**
	 * funzione richiamata prima di un update
	 * @Elemento Processo che andr� modificato
	 */
	public void informaPreUpdate(ElementoCanaleMessaggio ep);
	
	/**
	 * funzione richiamata successivamente all'update
	 * @param elemento processo modificato
	 */
	public void informaPostUpdate(ElementoCanaleMessaggio ep);

}
