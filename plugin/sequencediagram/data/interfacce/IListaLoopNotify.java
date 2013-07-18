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



package plugin.sequencediagram.data.interfacce;

import plugin.sequencediagram.data.ElementoLoop;
/**
 * interfaccia per implementare un gestore di notifica di aggiunta elementi
 * @author flamel
 * Charmy plug-in
 **/
 public interface IListaLoopNotify {
    
    /** 
	 * Notifica l'inserimento di un nuovo Elemento 
	 */
	public  void notifyAdd(ElementoLoop eps); 
    
    
	/** 
	 * Notifica la cancellazione di un nuovo Elemento 
	 */
	public  void notifyRemove(ElementoLoop eps); 
    
  
	/** 
	 * Notifica la Modifica di un nuovo Elemento 
	 */
	public  void notifyUpdate(ElementoLoop oldeps, ElementoLoop neweps); 
    
	/** 
	 * Notifica il refresh della lista 
	 */
	public  void refreshNotify(); 
    
    
	/**
	 * funzione richiamata prima di un update
	 * @Elemento Processo che andr� modificato
	 */
	public void informaPreUpdate(ElementoLoop ep);
	
	/**
	 * funzione richiamata successivamente all'update
	 * @param elemento processo modificato
	 */
	public void informaPostUpdate(ElementoLoop ep);
    
}