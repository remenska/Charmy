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

import javax.swing.JOptionPane;

import core.internal.runtime.data.IPlugData;

import plugin.statediagram.data.delegate.DelegateListaDPlistener;
import plugin.statediagram.graph.*;
import plugin.statediagram.pluglistener.IListaDPListener;

import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import plugin.topologydiagram.pluglistener.IListaProcessoListener;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;

/** 
 * */

public class ListaDP implements Serializable,  IListaProcessoListener {

	/**
	 * registrazioni listener
	 */
	private DelegateListaDPlistener listener;

	/**
	 * lista contente gli elementi della dinamica del processo
	 */
	private LinkedList linkedList;

	/**
	 * contenitore dei dati
	 */
	private IPlugData plugData;

	/** Costruttore. 
	 * @param contenitore dei dati
	 * */
	public ListaDP(IPlugData plugData) {
		linkedList = new LinkedList();
		listener = new  DelegateListaDPlistener(plugData, this);
		this.plugData = plugData;
	}


	public int getNumParametersMessage(String messageName){
		ListaThread lt=(ListaThread)linkedList.get(0);
		int value=-1;
		for(int i=0;i<getAllMessages().size();i++){	
			if(((ElementoCanaleMessaggio)getAllMessages().get(i)).getName().equals(messageName)){
				value=((ElementoMessaggio)getAllMessages().get(i)).getParameters().size();
				if(value==0)
					return 1;
				else
					return value;			
			}				
		}
		if(value==-1)
			System.out.println("Error: "+
					((ElementoCanaleMessaggio)getAllMessages().get(0)).getName());
		return -1;
	}

	/** Restituisce la lista dei nomi di tutti i messaggi 
		usati negli State Diagram. */
	public LinkedList getAllMessageName() {
		ListaThread dp;
		String nome;
		LinkedList risultato = new LinkedList();
		if (linkedList.isEmpty())
			return risultato;
		try { // Inserisco i messaggi definiti nei vari diagrammi di stato.
			for (int i = 0; i < linkedList.size(); i++) {
				dp = (ListaThread) (linkedList.get(i));
				LinkedList listaMess = dp.getAllMessageName();
				for (int j = 0; j < listaMess.size(); j++) {
					nome = (String) (listaMess.get(j));
					if (!(risultato.contains(nome))) {
						risultato.add(nome);
					}
				}
			}
		} catch (NullPointerException e) {
			//non dovrebbe verificarsi mai
			String s =
				"Null pointer in ListaDP$getAllMessageName class.\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Error!",
				JOptionPane.WARNING_MESSAGE);
			return risultato;
		}
		return risultato;
	}


	/** Restituisce la lista dei nomi di tutti i messaggi 
		del processo passato come parametro. */
	public LinkedList getAllTauMessageNameForProcess(String process_from) {
		ListaThread dp;
		String nome;
		LinkedList risultato = new LinkedList();
		LinkedList from = new LinkedList();
		if (linkedList.isEmpty())
			return risultato;
		try { 
			// Inserisco i messaggi definiti nei vari diagrammi di stato.
			for(int i=0;i<linkedList.size();i++){
				dp = (ListaThread) (linkedList.get(i));
				if(dp.getNameProcesso().equals(process_from)){
					from=dp.getAllTauMessages();
				}
			}
			for (int j = 0; j < from.size(); j++) {
				//nome = ((ElementoMessaggio)from.get(j)).getName();
				nome = (String)from.get(j);
					if (!(risultato.contains(nome))) {
						risultato.add(nome);
					}
			}
		} catch (NullPointerException e) {
			//non dovrebbe verificarsi mai
			String s =
				"Uso di un puntatore nullo \n dentro la classe ListaDP$getAllMessageName.\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return risultato;
		}
		return risultato;
	}

	
	

	/** Restituisce la lista dei nomi di tutti i messaggi 
		del processo passato come parametro. */
	public LinkedList getAllMessageNameForProcess(String process_from,String process_to) {
		ListaThread dp;
		String nome;
		LinkedList risultato = new LinkedList();
		LinkedList from = new LinkedList();
		LinkedList to = new LinkedList();
		if (linkedList.isEmpty())
			return risultato;
		try { 
			// Inserisco i messaggi definiti nei vari diagrammi di stato.
			for(int i=0;i<linkedList.size();i++){
				dp = (ListaThread) (linkedList.get(i));
				if(dp.getNameProcesso().equals(process_from)){
					from=dp.getAllSendMessageName();
				}
				if(dp.getNameProcesso().equals(process_to)){
					to=dp.getAllReceiveMessageName();
				}
			}
			for (int j = 0; j < from.size(); j++) {
				nome = (String) (from.get(j));
				if(to.contains(nome))
					if (!(risultato.contains(nome))) {
						risultato.add(nome);
					}
			}
		} catch (NullPointerException e) {
			//non dovrebbe verificarsi mai
			String s =
				"Uso di un puntatore nullo \n dentro la classe ListaDP$getAllMessageName.\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return risultato;
		}
		return risultato;
	}
	
	public ElementoMessaggio getMessage(String name){
		LinkedList listMessages=getAllMessages();
		for(int i=0;i<listMessages.size();i++)
			if(((ElementoMessaggio)listMessages.get(i)).getName().equals(name))
				return (ElementoMessaggio)listMessages.get(i);
		return null;		
	}
		
	/** Restituisce la lista di tutti i messaggi 
	 usati negli State Diagram. */ 
	public LinkedList getAllMessages()
	{
		ListaThread dp;
		ElementoMessaggio canale;
		LinkedList risultato = new LinkedList();

		if (linkedList.isEmpty()) return risultato;
		try 
		{	// Inserisco i messaggi definiti nei vari diagrammi di stato.
			for (int i=0;i<size();i++)
			{
				dp = (ListaThread)(get(i));
				LinkedList listaMess = dp.getAllMessages();
				for (int j=0;j<listaMess.size();j++)
				{
					canale = (ElementoMessaggio)(listaMess.get(j));
					if (!(risultato.contains(canale.getName()))){
						risultato.add(canale);  
					}
				} 
			} 	
		}
		catch (NullPointerException e)
		{
			String s = "Uso di un puntatore nullo \n dentro la classe ListaDP$getAllMessage.\n" + e.toString();
			JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
			return risultato;
		}
		return risultato;
	}  
	
	
	/** Restituisce la lista di tutti i messaggi 
	 usati negli State Diagram. */ 
	public LinkedList getAllMessagesDoppioni()
	{
		ListaThread dp;
		ElementoMessaggio canale;
		LinkedList risultato = new LinkedList();

		if (linkedList.isEmpty()) return risultato;
		try 
		{	// Inserisco i messaggi definiti nei vari diagrammi di stato.
			for (int i=0;i<size();i++)
			{
				dp = (ListaThread)(get(i));
				LinkedList listaMess = dp.getAllMessages();
				for (int j=0;j<listaMess.size();j++)
				{
					canale = (ElementoMessaggio)(listaMess.get(j));
					risultato.add(canale);  
				} 
			} 	
		}
		catch (NullPointerException e)
		{
			String s = "Uso di un puntatore nullo \n dentro la classe ListaDP$getAllMessage.\n" + e.toString();
			JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
			return risultato;
		}
		return risultato;
	}  
	
	
	
	public ListaThread getDinamicaProcesso(int i) {
		return (ListaThread) (linkedList.get(i));
	}


	/** Restituisce il nome di un canale se ne esiste almeno uno, altrimenti null. */
	public String getAnyNameChannel() {
		LinkedList ll = getAllMessageName();

		if (linkedList.isEmpty())
			return null;
		if (ll.isEmpty())
			return null;
		return (String) (ll.get(0));
	}

	
	
	/**
	 * ritorna la lista Thread associata all'id del processo 
	 * @author Stoduto Michele
	 */
	public ListaThread getListaThread(long id) {
		ListaThread tmpListaThread = null;
		for (int i = 0; i < linkedList.size(); i++) {
			tmpListaThread = getDinamicaProcesso(i);
			if (id == tmpListaThread.getIdProcesso()) {
				//	Trovata la lista associata con il processo selezionato.
				return tmpListaThread;
			}
		}
		return null;
	}

	/**
	 * ritorna la lista Thread associata all'nome del processo 
	 * @author Stoduto Michele
	 */
	public ListaThread getListaThread(String nome) {
		ListaThread tmpListaThread = null;
		for (int i = 0; i < linkedList.size(); i++) {
			tmpListaThread = getDinamicaProcesso(i);
			if (nome.equals(tmpListaThread.getNameProcesso())) {
				//	Trovata la lista associata con il processo selezionato.
				return tmpListaThread;
			}
		}
		return null;
	}

	/**
	 * muove l'elemento ThreadElement nella lista del processo con idProcesso
	 * @return true se lo spostamento ? riuscito, false altrimenti
	 */
	public synchronized boolean moveThreadTo(ThreadElement threadElement, long idProcesso) {
		//verifica se threadElement ? contenuto in una lista valida
		ListaThread lt = threadElement.getListaThread();
		if (linkedList.contains(lt)) {
			if (lt.getIdProcesso() == idProcesso) {
				return true; //gia ? nella lista

			}
			ListaThread ltnew = this.getListaThread(idProcesso);
			if (ltnew == null) { //id Di processo non valido
				return false;
			}
			if (lt.remove(threadElement)) {
				if (ltnew.add(threadElement)) {
					listener.listaThreadRefresh();
					return true;
				}
				return false;
			} else { //qualcosa non ha funzionato nella cancellazzione dell'elemento
				return false;
			}

		} else { //lista non contenuta
			ListaThread ltnew = this.getListaThread(idProcesso);
			if (ltnew == null) { //id Di processo non valido
				return false;
			}
			if (ltnew.add(threadElement)) {
				listener.listaThreadRefresh();
				return true;
			}
			return false;
		}
	}

	/**
	 * ritorna il numero di elementi contenuti nalla lista
	 * @return
	 */
	public int size() {
		return linkedList.size();
	}

	/**
	 * rimuove il primo elemento della lista e lo restituisce
	 * @return
	 */
	public ListaThread removeFirst() {
		ListaThread lt = (ListaThread) linkedList.removeFirst();
		if (lt != null) {
			lt.removeListener(listener);
			listener.informaRemoveDP(lt);
		}
		return lt;
	}

	/**
	 * 
	 * rimuove la ListaThread dalla lista
	 * @param lista da rimuovere
	 * @return true se la lista ? stata rimossa
	 */
	public boolean remove(ListaThread o) {
		boolean bo = linkedList.remove(o);
		if (bo) {
			o.removeListener(listener);
			listener.informaRemoveDP(o);
		}
		return bo;
	}

	/**
	 * 
	 * rimuove la ListaThread dalla lista
	 * @param lista da rimuovere
	 * @return la lista rimossa o null se non esiste
	 */
	public ListaThread remove(int indice) {
		ListaThread lt = (ListaThread) linkedList.remove(indice);

		if (lt != null) {
			lt.removeListener(listener);
			listener.informaRemoveDP(lt);
		}
		return lt;
	}

	/**
	 * 
	 * ritorna l'elemento in posizione indice
	 * @param indice da prelevare
	 * @return l'elemento in posizione indice 
	* @throws IndexOutOfBoundsException if the specified index is is out of
	 * range (<tt>index &lt; 0 || index &gt;= size()</tt>).
	 */
	public ListaThread get(int indice) {
		return (ListaThread) linkedList.get(indice);
	}

	/**
	 * 
	 *aggiunge la ListaThread dalla lista
	 * @param lista da aggiungere
	 * @return false se la lista non po? essere aggiunta
	 */
	public boolean add(ListaThread lt) {
		boolean bo = linkedList.add(lt);
		if (bo) {
			lt.addListener(listener);
			listener.informaAddDP(lt);
		}
		return bo;
	}

	/**
	 * ritorna l'iterator della lista
	 * @return
	 */
	public Iterator iterator() {
		return linkedList.iterator();
	}

	/*---------------------------------------------------------------------------------------------------------------*/
	/**
	 * getione eventi
	 */

	/**
	 * aggiunge un listener
	 * @param listener
	 */
	public void addListener(IListaDPListener listener) {
		this.listener.add(listener);
	}

	/**
	 * rimuove un listener
	 * @param listener
	 */
	public void removeListener(IListaDPListener listener) {
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
	 * gestione eventi provenienti dal processo
	 * @author michele
	 * Charmy plug-in
	 *
	 */

	/**
	 * listener degli eventi
	 */

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoAdded(eventi.AddEPEvento)
	 */
	public void processoAdded(AddEPEvento event) {
		ListaThread tmpListaThread;		
		tmpListaThread = getListaThread(event.getElementoProcesso().getId());
		if (tmpListaThread == null) {
			add(
				new ListaThread(
					event.getElementoProcesso().getId(),
					event.getElementoProcesso().getName(), plugData));
		}
	
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRemoved(eventi.RemoveEPEvento)
	 */
	public void processoRemoved(RemoveEPEvento event) {
			remove(getListaThread(event.getElementoProcesso().getId()));
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoUpdate(eventi.UpdateEPEvento)
	 */
	public void processoUpdate(UpdateEPEvento event) {
		for(int i=0;i<linkedList.size();i++){
			if(((ListaThread)linkedList.get(i)).getNameProcesso().equals(event.getOldElementoProcesso().getName())){
				((ListaThread)linkedList.get(i)).setNameProcesso(event.getNewElementoProcesso().getName());
			}
		}
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRefresh()
	 */
	public void processoRefresh() {
		// non gestiti
	}

	/**
	 * rimuove tutti gli elementi contenuti nella lista
	 */
	public void removeAll() {
		boolean bo = plugData.getPlugDataManager().startSessione();
		Iterator ite = linkedList.iterator();
		while (ite.hasNext()){
			ListaThread lt = (ListaThread)ite.next();
			
			//ThreadElement te = (ThreadElement) ite.next();
			lt.removeAll();
			this.remove(lt);
			ite = linkedList.iterator();
		}
		plugData.getPlugDataManager().stopSessione(bo);
	}

}
