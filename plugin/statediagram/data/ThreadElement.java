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
    
package plugin.statediagram.data;


import java.util.LinkedList;

import core.internal.runtime.data.IPlugData;

import plugin.statediagram.data.delegate.DelegateThreadElementListener;
import plugin.statediagram.pluglistener.IThreadElementListener;


/**
 * @author Stoduto Michele
 * Charmy plug-in
 * Questa classe opera la separazione del ThreadEditor dalla lista dei suoi
 * elementi componenti, ListaStato e ListaMessaggio
 * inoltre si registra come listener delle classi suddette e gestisce la 
 * spedizione di un messaggio a classi che implementano L'interfacccia
 * IThreadElementListener
 **/
public class ThreadElement implements Cloneable{  

	private DelegateThreadElementListener listener;

	/**
	 * nome Thread
	 */
	private String nomeThread;

	/**
	 * listaThread dove � inserito l'elemento
	 */
	private ListaThread listaThread;

	/**
	 * ListaStato 
	 */
	private ListaStato listaStato;

	/**
	 * ListaStato 
	 */
	private ListaMessaggio listaMessaggio;

	private IPlugData plugData;

	

	/**
	 * Costruisce creando in modo automatico la ListaStato e ListaMessaggi
	 */
	public ThreadElement(String nomeTh, ListaThread lt, IPlugData plugData) {

		this(nomeTh, new ListaStato(plugData), new ListaMessaggio(plugData), lt, plugData);
	}

	/**
	 * Costruisce inserendo ListaStato e ListaMessaggi
	 */
	public ThreadElement(String nomeTh, ListaStato ls, ListaMessaggio lm, ListaThread lt, IPlugData plugData) {

		nomeThread = nomeTh;
		listener = new DelegateThreadElementListener(plugData, this);
		setListaStato(ls);
		setListaMessaggio(lm);

		listaThread = lt;

		this.addListener(lm);
		this.plugData = plugData;
	}

	/** Numero Stati*/
	private long numStato;

	public long getNumStato() {
		return numStato;
	}
	
	public void setNumStato(long numStato){
		this.numStato=numStato;
	}

	public void incNumStato() {
		numStato = numStato + 1;
	}

	public void decNumStato() {
		numStato = numStato - 1;
	}

	/**
	 * Ritorna la ListaMessaggio
	 * @return listaMessaggio
	 */
	public ListaMessaggio getListaMessaggio() {
		return listaMessaggio;
	}
	
	public LinkedList getListaSendMessaggio(){
		return listaMessaggio.getListaNomeSendMessaggio();
	}
	
	public LinkedList getListaReceiveMessaggio(){
		return listaMessaggio.getListaNomeReceiveMessaggio();
	}
	
	public LinkedList getListaTauMessages(){
		return listaMessaggio.getTauMessages();
	}

	/**
	 * Ritorna la listaStato
	 * @return
	 */
	public ListaStato getListaStato() {
		return listaStato;
	}

	/**
	 * setta la ListaMessaggio
	 * @param ListaMessaggio
	 */
	public synchronized void setListaMessaggio(ListaMessaggio messaggio) {

		/* evito duplicazione e generazione dati inutili */
		if (listaMessaggio != null) {
			listaMessaggio.removeListener(listener);
		}

		listaMessaggio = messaggio;
		if (listaMessaggio != null) {
			listaMessaggio.removeListener(listener);
			listaMessaggio.addListener(listener);
		}
	}

	/**
	 * setta la ListaStato
	 * @param stato
	 */
	public synchronized void setListaStato(ListaStato stato) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (listaStato != null) {
			listaStato.removeListener(listener);
		}
		
		
		listaStato = stato;
		if (listaStato != null) {
			listaStato.removeListener(listener);
			listaStato.addListener(listener);
		}
	}

	/**
	 * ritorna la lista thread che contiene l'elemento
	 * @return
	 */
	public ListaThread getListaThread() {
		return listaThread;
	}

	/**
	 * setta la lista thread che contiene l'elemento
	 * @param thread
	 */
	public void setListaThread(ListaThread thread) {
		listaThread = thread;
	}

	/**
	 * ritorna la stringa del nome del trhread
	 * @return
	 */
	public String getNomeThread() {
		return nomeThread;
	}

	
	/**
	 * setta la stringa del nomethread
	 * @param string
	 */
	public void setNomeThread(String string) {
		nomeThread = string;
	}

	/**
	 * aggiunge un listener
	 * @param listener
	 */
	public void addListener(IThreadElementListener listener) {
		this.listener.add(listener);
	}

	/**
	 * rimuove un listener
	 * @param listener
	 */
	public void removeListener(IThreadElementListener listener) {
		this.listener.remove(listener);
	}

	/**
	 * rimuove tutti i  listener registrati
	 * @param listener
	 */
	public void removeAllListener() {
		this.listener.removeAllElements();
	}

	/**
	 * rimuove tutti gli elementi contenuti nella lista
	 */
	public void removeAll() {
		boolean bo = plugData.getPlugDataManager().startSessione();
		
		this.listaMessaggio.removeAll();
		this.listaStato.removeAll();
		
		plugData.getPlugDataManager().stopSessione(bo);
	}

	public ThreadElement cloneThread(){
		ThreadElement te = new ThreadElement(this.getNomeThread(), this.getListaThread(),this.plugData);
		te.setListaStato(this.getListaStato().cloneListaStato());
		te.setListaMessaggio(this.getListaMessaggio().cloneListaMessaggio(this.getListaStato()));
		
		//te.getListaStato().setStartState(this.getListaStato().getStartState());
		return te;		
	}

}
