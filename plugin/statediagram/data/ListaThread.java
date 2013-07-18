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

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import core.internal.runtime.data.IPlugData;

import plugin.statediagram.data.delegate.DelegateListaThreadListener;
import plugin.statediagram.graph.*;
import plugin.statediagram.pluglistener.IListaThreadListener;
import plugin.statediagram.pluglistener.IThreadElementListener;

import plugin.statediagram.eventi.listathread.AddThreadEvento;
import plugin.statediagram.eventi.listathread.RemoveThreadEvento;
import plugin.statediagram.eventi.thread.AddThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.AddThreadStatoEvento;
import plugin.statediagram.eventi.thread.RemoveThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.RemoveThreadStatoEvento;
import plugin.statediagram.eventi.thread.UpdateThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.UpdateThreadStatoEvento;

/** Classe per l'implementazione delle liste di thread 
	usate nello "State Diagram"_ Ad ogni processo rimane
	associata una lista di thread. */
/*
 * si ? trasformata in un aggregato di linked list per convenienza, in quanto 
 * cosi ? possibile nascondere funzioni non necessarie
 * @author michele
 * Charmy plug-in
 *
 */
public class ListaThread implements IThreadElementListener, Serializable //extends LinkedList
{

	/**
	 * gestione eventi
	 */
	private DelegateListaThreadListener listener;

	/** Memorizza il nome del processo a cui appartengono 
		i thread della lista. */
	private String nameProcesso;

	/** Memorizza l'id del processo a cui appartengono 
		i thread della lista. */
	private long idProcesso;

	/**
	 * lista 
	 */
	private LinkedList listaThread;

	/**
	 * riferimento al contenitore dei dati del prg
	 */
	private IPlugData plugData; 
	
	
	/**
	 * Costruttore
	 * @param nProcesso identificativo del processo legato alla lista
	 * @param stringProcesso nome del processo legato alla lista(? inutile)
	 * @param plugData contenitore dei dati del programma
	 */


	/** Costruttore. */
	public ListaThread(long nProcesso, String stringProcesso, IPlugData plugData) {
		listaThread = new LinkedList(); //super();
		idProcesso = nProcesso;
		nameProcesso = stringProcesso;
		this.plugData = plugData;
		listener = new DelegateListaThreadListener(plugData,this);
	}




	/** Restituisce il nome del processo. */
	public String getNameProcesso() {
		return nameProcesso;
	}

	/** Restituisce l'id del processo. */
	public long getIdProcesso() {
		return idProcesso;
	}

	/** Imposta il nome del processo. */
	public void setNameProcesso(String nProcesso) {
		nameProcesso = nProcesso;
	}

	/** Imposta l'id del processo. */
	public void setIdProcesso(long j) {
		idProcesso = j;
	}


	/** Restituisce true se nella lista c'? un elemento con
		nome uguale alla stringa in ingresso (nomeThread). */
	public boolean nomeGiaPresente(String nomeThread) {
		for (int i = 0; i < listaThread.size(); i++) {
			if (get(i).getNomeThread().equals(nomeThread)) {
				return true;
			}
		}
		return false;
	}

	
	/** Restituisce la lista di tutti i messaggi 
	 usati nella lista di thread. */
	public LinkedList getAllMessages()
	{
		ThreadElement thread;
		LinkedList risposta = new LinkedList();

		if (isEmpty()) return risposta;
		
		// Ottengo tutti i nomi di link per ogni thread memorizzato.
		for (int i=0;i<size();i++)
		{
			thread =get(i);
			LinkedList listaMessaggi = thread.getListaMessaggio().getAllMessages();
			for (int j=0;j<listaMessaggi.size();j++) 
			{
				ElementoMessaggio canale = (ElementoMessaggio)listaMessaggi.get(j);
				if (!(risposta.contains(canale.getName())))
				{
					risposta.add(canale);
				}
			}
		}
		return risposta;
	}

	/** Restituisce la lista dei nomi di tutti i messaggi 
		usati nella lista di thread. */
	public LinkedList getAllReceiveMessageName() {
		//ThreadEditor thread;
		ThreadElement thread;
		LinkedList risposta = new LinkedList();

		if (listaThread.isEmpty())
			return risposta;

		// Ottengo tutti i nomi di link per ogni thread memorizzato.
		for (int i = 0; i < listaThread.size(); i++) {
			thread = (ThreadElement) (listaThread.get(i));
			LinkedList listaNomi = thread.getListaReceiveMessaggio();
			for (int j = 0; j < listaNomi.size(); j++) {
				String nome = (String) listaNomi.get(j);
				if (!(risposta.contains(nome))) {
					risposta.add(nome);
				}
			}
		}
		return risposta;
	}	
	


	/** Restituisce la lista dei nomi di tutti i messaggi 
		usati nella lista di thread. */
	public LinkedList getAllSendMessageName() {
		//ThreadEditor thread;
		ThreadElement thread;
		LinkedList risposta = new LinkedList();

		if (listaThread.isEmpty())
			return risposta;

		// Ottengo tutti i nomi di link per ogni thread memorizzato.
		for (int i = 0; i < listaThread.size(); i++) {
			thread = (ThreadElement) (listaThread.get(i));
			LinkedList listaNomi = thread.getListaSendMessaggio();
			for (int j = 0; j < listaNomi.size(); j++) {
				String nome = (String) listaNomi.get(j);
				if (!(risposta.contains(nome))) {
					risposta.add(nome);
				}
			}
		}
		return risposta;
	}	

	public LinkedList getAllTauMessages(){
		ThreadElement thread;
		LinkedList risposta = new LinkedList();

		if (listaThread.isEmpty())
			return risposta;

		// Ottengo tutti i nomi di link per ogni thread memorizzato.
		for (int i = 0; i < listaThread.size(); i++) {
			thread = (ThreadElement) (listaThread.get(i));
			LinkedList listaNomi = thread.getListaTauMessages();
			for (int j = 0; j < listaNomi.size(); j++) {
				String nome = (String) listaNomi.get(j);
				if (!(risposta.contains(nome))) {
					risposta.add(nome);
				}
			}
		}
		return risposta;
		
	}
	
	/** Restituisce la lista dei nomi di tutti i messaggi 
		usati nella lista di thread. */
	public LinkedList getAllMessageName() {
		//ThreadEditor thread;
		ThreadElement thread;
		LinkedList risposta = new LinkedList();

		if (listaThread.isEmpty())
			return risposta;

		// Ottengo tutti i nomi di link per ogni thread memorizzato.
		for (int i = 0; i < listaThread.size(); i++) {
			thread = (ThreadElement) (listaThread.get(i));
			LinkedList listaNomi = thread.getListaMessaggio().getAllMessageName();
			for (int j = 0; j < listaNomi.size(); j++) {
				String nome = (String) listaNomi.get(j);
				if (!(risposta.contains(nome))) {
					risposta.add(nome);
				}
			}
		}
		return risposta;
	}


	/**
	 * dimensione della lista
	 * @return intero rappresentante il numero di elementi nella lista
	 */
	public int size() {
		return listaThread.size();
	}

	/**
	 * ritorna vero se la lista non contiene elementi
	 * @return
	 */
	public boolean isEmpty() {
		return listaThread.isEmpty();
	}

	/**
	 * rimuove la prima occorrenza dell'oggetto nella lista
	 * inoltre rimuove il listener della ListaThread relativa
	 * @param o
	 * @return
	 */
	public boolean remove(ThreadElement o) {
		boolean bo;
		bo = listaThread.remove(o);
		if(bo){ //invio evento rimozione listener
			o.removeListener(this);
			listener.threadRemoved(o,this);
		}
		return bo;
	}

	/**
	 * rimuove il primo elemento dellla lista
	 * @return ritorna l'elemento rimosso
	 */
	public ThreadElement removeFirst() {
		ThreadElement te = (ThreadElement) listaThread.removeFirst();
		if(te != null){
		  listener.threadRemoved(te,this);
		}
		return te;
	}

	/**
	 * ritorna l'elemento alla specifica posizione
	 * @param index
	 * @return
	 */
	public ThreadElement get(int index) {
		return (ThreadElement) listaThread.get(index);
	}

	
	/**
	 * ritorna l'elemento con lo specifico nome
	 * @param Nome Thread Element
	 * @return ThreadElement Associato al nome, null altrimenti
	 */
	public ThreadElement getThreadElement(String nomeThread) {
		
		for(int i = 0; i<listaThread.size(); i++){
			ThreadElement te = get(i);
			if(te.getNomeThread().equals(nomeThread)){
				return te;
			}

		}
		return null;
	}
	
	
	
	
	
	
	/**
	 * aggiunge un oggetto nella lista e ne imposta la listathread
	 * di riferimento e i listener della relativa lista
	 *
	 * @param Elemento da aggiungere
	 * @return true se l'oggetto ? stato aggiunto false altrimenti
	 *
	 */
	public boolean add(ThreadElement o) {
		
		if(listaThread.contains(o)) return true; 
		
		boolean bo = listaThread.add(o);
		
		if(bo){
		   o.setListaThread(this);
		   o.removeListener(this);
		   o.addListener(this);
		   listener.threadAdded(o,this);
		}		
		
		return bo;
	}

	/**
	 * rimuove tutti gli elementi dalla lista
	 */
	public void clear() {
		listaThread.clear();
	}

	/**
	 * restituisce l'iterator della lista
	 * @return iterator
	 */
	public Iterator iterator(){
		return listaThread.iterator();
	}



	/**
	 * getione eventi
	 */

	/**
	 * aggiunge un listener
	 * @param listener
	 */
	public void addListener(IListaThreadListener  listener) {
		this.listener.add(listener);
	}

	/**
	 * rimuove un listener
	 * @param listener
	 */
	public void removeListener(IListaThreadListener  listener) {
		this.listener.remove(listener);
	}

	/**
	 * rimuove tutti i  listener registrati
	 * @param listener
	 */
	public void removeAllListener() {
		this.listener.removeAllElements();
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoAdded(eventi.thread.AddThreadStatoEvento)
	 */
	public void statoAdded(AddThreadStatoEvento event) {
			listener.statoAdded(event);	
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoRemoved(eventi.thread.RemoveThreadStatoEvento)
	 */
	public void statoRemoved(RemoveThreadStatoEvento event) {
		listener.statoRemoved(event);
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoUpdate(eventi.thread.UpdateThreadStatoEvento)
	 */
	public void statoUpdate(UpdateThreadStatoEvento event) {
		listener.statoUpdate(event);
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioAdded(eventi.thread.AddThreadMessaggioEvento)
	 */
	public void messaggioAdded(AddThreadMessaggioEvento event) {
		listener.messaggioAdded(event);
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioRemoved(eventi.thread.RemoveThreadMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveThreadMessaggioEvento event) {
		listener.messaggioRemoved(event);
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioUpdate(eventi.thread.UpdateThreadMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateThreadMessaggioEvento event) {
		listener.messaggioUpdate(event);
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#threadRefresh()
	 */
	public void threadRefresh() {
		listener.threadRefresh();
	}


	/**
	 * Chiamata quando un Elemento ? aggiunto alla lista
	 */
	public void threadAdded(AddThreadEvento ate){
		listener.threadAdded(ate);
	}
	
	/**
	 * chiamate quando un thread ? rimosso dalla lista
	 * @param rte
	 */
	
	public void threadRemoved(RemoveThreadEvento rte){
		listener.threadRemoved(rte);
	}




	/**
	 * rimuove gli elementi contenuti nella lista
	 */
	public void removeAll() {
		boolean bo = plugData.getPlugDataManager().startSessione();
		Iterator ite = this.listaThread.iterator();
		while (ite.hasNext()){
			ThreadElement te = (ThreadElement) ite.next();
			te.removeAll();
			this.remove(te);
			ite = listaThread.iterator();
		}
		plugData.getPlugDataManager().stopSessione(bo);
	}
	

}