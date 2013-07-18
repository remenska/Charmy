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

/**
 * Interfaccia per gestire gli update degli elementi base,ElementoBox etc
 * @author michele
 * Charmy plug-in
 **/
public interface IUpdate {
	

	/**
	 * funzione richiamata prima di un update
	 *
	 */
	public void informPreUpdate();
	
	/**
	 * funzione richiamata successivamente all'update
	 *
	 */
	public void informPostUpdate();


	/**
	 * disabilita l'invio dei messaggi, utile quando ci sono numerosi parametri da cambiare
	 *
	 */
	public void disable();
	

	/**
	 * abilita la trasmissione dei messaggi, utile quando ci sono numerosi parametri da cambiare
	 * obbligatoria dopo un <code>disable()</code>
	 *
	 */
	public void enabled();
	
	/**
	 * restituisce lo stato di abilitato o disabilitato
	 *
	 */
	public boolean getStato();
	
	
	
	/**
	 * 
	 * @author michele
	 * disabilita o meno l'invio dell'informazione controllandone 
	 * lo stato attuale, e ritorna il lo stato precedente, 
	 * se lo stato precedente � true, debbo provvedere a riabilitare il sistema di messaggistica 
	 * all'uscita della funzione
	 */
	public boolean testAndSet();
	
	
	/**
	 * 
	 * @author michele
	 * riporta lo stato a come era prima di entrare nella funzione 
	 * praticamente prende in ingresso il valore booleano restituito da testAndSet()
	 * se lo stato precedente � true, debbo provvedere a riabilitare il sistema di messaggistica 
	 * all'uscita della funzione
	 */
	public void testAndReset(boolean oldStato);
	
	
		
	
	

}
