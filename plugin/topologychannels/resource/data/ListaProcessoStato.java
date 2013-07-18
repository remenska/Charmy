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
    


package plugin.topologychannels.resource.data;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.topologychannels.resource.data.interfacce.IListaProcStatoNotify;
import plugin.topologychannels.resource.utility.ListMovedObjects;

import core.internal.runtime.data.IPlugData;


/** Superclasse astratta di ListaProcesso e ListaStato_
	La prima implementa la lista dei processi inseriti nel S_A_ Topology
	Diagram, la seconda quella degli stati di uno State Diagram. */

public abstract class ListaProcessoStato implements Serializable {

	/** Memorizza una lista di oggetti della classe ElementoProcessoStato. */
	protected LinkedList lista;

	/** delega per la gestione dei listener */
	private IListaProcStatoNotify listaNotify = null;

	/**
	 * variabile che indica il fatto che una serie di 
	 * modifiche appartengono ad una stessa transazione
	 * la variabile viene utilizzata prevalentemente in <code><i>removeListSelected()</i></code>
	 */
	protected boolean isTransaction = false;
	
	/**
	 * riferimento al contenitore dei dati
	 */
	protected IPlugData plugData;

	/** Costruttore. 
	  *  con la notifica degli eventi
	  * @param notifica degli eventi
	  * @param contenitore dei dati
	  */
	public ListaProcessoStato(IListaProcStatoNotify notify, IPlugData plugData) {
		lista = new LinkedList();
		listaNotify = notify;
		this.plugData = plugData;
	}

	/** Restituisce il numero degli elementi attualmente inseriti nella lista. */
	public int size() {
		if (lista.isEmpty())
			return 0;
		else
			return lista.size();
	}

	/** Inserisce nella lista una copia dell'elemento in ingresso. */
	public abstract boolean addPasteElement(ElementoProcessoStato eps);

	/** 
	 * Notifica l'inserimento di un nuovo Elemento 
	 */
	protected void addNotify(ElementoProcessoStato eps) {
		if (listaNotify != null) {
			listaNotify.addNotify(eps);
		}

	}

	/** 
	 * Notifica la cancellazione di un nuovo Elemento 
	 */
	protected void removeNotify(ElementoProcessoStato eps) {
		if (listaNotify != null) {
			listaNotify.removeNotify(eps);
		}

	}

	/** 
	 * Notifica la Modifica di un nuovo Elemento 
	 */
	public void updateNotify(
		ElementoProcessoStato oldeps,
		ElementoProcessoStato neweps) {
			//System.out.println(oldeps.getClass()+", "+neweps.getClass());
			if (listaNotify != null) {
				listaNotify.updateNotify(oldeps, neweps);
		}
	}

	/** 
	 * Notifica la Modifica di un nuovo Elemento 
	 */
	public void refreshNotify() {
		if (listaNotify != null) {
			listaNotify.refreshNotify();
		}
	}

	/** Inserisce un nuovo elemento nella lista_ Restituisce 'true' se l'elemento e' 
	    stato inserito nella lista, 'false' altrimenti. */
	public boolean addElement(ElementoProcessoStato eps) {
		ElementoProcessoStato proc;
		
		
		
		
		
		boolean bool;
		if (lista == null)
			return false;
		if (eps == null)
			return false;
		if(lista.contains(eps)) return true; //l'elemento è già nella lista
		
		
		eps.setUpdateEp(listaNotify);
		// Cerca, se esiste, un processo con lo stesso nome di quello che si vuole inserire.
		// e ne notifica l'inserimento
		for (int i = 0; i < lista.size(); i++) {
			proc = (ElementoProcessoStato) lista.get(i);
			if (proc.getName().equalsIgnoreCase(eps.getName())) {
			
				lista.set(i, eps);

				addNotify(eps);

				return true;
			}
		}
		bool = lista.add(eps); //dal codice sembra che non diventa mai false
		if (bool != false) {
			addNotify(eps);
		}
		return bool;
	}

	/** Restituisce l'indice nella lista dell'elemento in ingresso, 
	 * '-1' se non e' presente.
	 * @param ElementoProcessoStato da trovare
	 * @throws IndexOutOfBoundsException
	 *  */
	public int getIndex(ElementoProcessoStato eps) 
	    throws IndexOutOfBoundsException{
		int temp = -1;

		if (lista == null)
			return -1;
		if (eps == null)
			return -1;
		if (lista.isEmpty())
			return -1;

			temp = lista.indexOf(eps);

		return temp;
	}

	/** Aggiorna l'elemento nella lista avente lo stesso nome di quello passato 
		come parametro_ Ritorna 'false' se l'aggiornamento non ? stato possibile. */
	public boolean updateElement(ElementoProcessoStato nuovo) {
		ElementoProcessoStato proc;
		String nomeP;

		if (lista == null)
			return false;
		if (nuovo == null)
			return false;
		for (int i = 0; i < lista.size(); i++) {
			proc = (ElementoProcessoStato) lista.get(i);
			nomeP = (String) proc.getName();
			if (nomeP.equalsIgnoreCase(nuovo.getName())) {

				lista.set(i, nuovo);
				updateNotify(proc, nuovo);
				return true;
			};
		}
		return false;
	}


	/** Restituisce il processo con il nome specificato, 'null' se non e' presente. */
	public ElementoProcessoStato getElement(String nome) {
		ElementoProcessoStato eps;

		if (lista == null)
			return null;
		if (nome == null)
			return null;
		for (int i = 0; i < lista.size(); i++) {
			eps = (ElementoProcessoStato) lista.get(i);
			if (nome.equalsIgnoreCase(eps.getName()))
				return (ElementoProcessoStato) eps;
		}
		return null;
	}

	/** Deseleziona tutti gli elementi della lista. */
	public void noSelected() {
		ElementoProcessoStato eps;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				eps = (ElementoProcessoStato) lista.get(i);
				eps.setSelected(false);
			}
		}
	}

	/** Restituisce l'elemento specificato dall'indice, 'null' se non ? presente. */
	public ElementoProcessoStato getElement(int i) {
		if (lista == null)
			return null;
		if (lista.isEmpty() == true)
			return null;
		try {
			return (ElementoProcessoStato) lista.get(i);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/** Rimuove dalla lista l'elemento specificato come parametro. */
	public boolean removeElement(ElementoProcessoStato eps) {
		ElementoProcessoStato proc;
		
		if (lista == null)
			return false;
		if (eps == null)
			return false;
		for (int i = 0; i < lista.size(); i++) {
			proc = (ElementoProcessoStato) lista.get(i);
			if (proc == eps) {

				lista.remove(i);
				eps.setUpdateEp(null); //stacco la vecchia classe
				removeNotify(eps);
				return true;
			}
		}
		return false;
	}

	/**
	 * @author  Michele Stoduto
	 * rimuove tutti i processi selezionati (attributo selected = true)
	 * 
	 */
	public void removeAllSelected() {
		boolean ctrl;
		ElementoProcessoStato tmpProcesso;

		if (this.size() > 0) {
			for (int i = 0; i < size(); i++) {
				tmpProcesso = (ElementoProcessoStato) getElement(i);
				if (tmpProcesso.isSelected()) {
					isTransaction = true;
					removeElement(tmpProcesso);
				}
			}
			isTransaction = false;
		}
	}




	/** Rimuove tutti gli elementi inseriti nella lista. */
	public void removeAll() {
		if (lista == null)
			return;
		Iterator ite = lista.iterator();
		while (ite.hasNext()){
			this.removeElement((ElementoProcessoStato)ite.next());
			ite = lista.iterator();
		}
		
		//		lista.removeAll(lista);
	//	refreshNotify();
	}

	/** Controlla se il punto specificato si trova all'interno di uno dei
		rettangoli rappresentanti gli elementi_ In caso affermativo viene 
		restituito in output l'elemento, altrimenti viene restituito null. */
	public ElementoProcessoStato getElement(Point p) {
		ElementoProcessoStato eps = null;

		if (lista == null)
			return null;
		if (p == null)
			return null;
		try {
			for (int i = 0; i < lista.size(); i++) {
				eps = (ElementoProcessoStato) lista.get(i);
				if (eps != null)
					if (eps.isIn(p.x, p.y))
						return eps;
			}
		} catch (IndexOutOfBoundsException e) {
			String s =
				"Index out of range: \n ListaProcesso$getProcess. \n"
					+ e.toString()
					+ "\n The program will closed!";
			JOptionPane.showMessageDialog(
				null,
				s,
				"Error condition!",
				JOptionPane.WARNING_MESSAGE);
			//System.exit(0);
		}
		return null;
	}

	/** Controlla che il nome (primo parametro) non sia gi? presente 
		nella lista dei nomi (secondo parametro). */
	public boolean giaPresente(String nome, LinkedList listname) {
		String n;

		if (listname == null)
			return false;
		for (int i = 0; i < listname.size(); i++) {
			n = (String) listname.get(i);
			if (n.equalsIgnoreCase(nome))
				return true;
		}
		return false;
	}

	/** Controlla che il nome preso come parametro non sia gi? presente 
		nella lista degli elementi attualmente definiti. */
	public boolean giaPresente(String nome) {
		ElementoProcessoStato proc;
		String n;
		if (lista == null)
			return false;
		for (int i = 0; i < lista.size(); i++) {
			proc = (ElementoProcessoStato) lista.get(i);
			n = (String) proc.getName();
			if (n.equalsIgnoreCase(nome))
				return true;
		}
		return false;
	}

	/** Stampa l'intera lista.
	 *  Lista Processo ne ridefinisce il disegno
	 *  */
	public void paintLista(Graphics2D g2D) {
		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				((ElementoProcessoStato) lista.get(i)).paintElemento(g2D);
			}
		}
	}

	/** Restituisce la lista degli elementi selezionati. */
	public ListMovedObjects listSelectedProcess() {
		ListMovedObjects plista = new ListMovedObjects();
		ElementoProcessoStato processo;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				processo = (ElementoProcessoStato) lista.get(i);
				if (processo.isSelected()) {
					plista.add(processo);
				}
			}
		}
		return plista;
	}

	/** Restituisce una linked list degli elementi selezionati.
	 * 
	 *  */
	public LinkedList getListaSelectedProcess() {
		LinkedList plista = new LinkedList();
		ElementoProcessoStato processo;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				processo = (ElementoProcessoStato) lista.get(i);
				if (processo.isSelected()) {
					plista.add(processo);
				}
			}
		}
		return plista;
	}




	/** Seleziona gli elementi della lista contenuti nel rettangolo
		passato come parametro d'ingresso. */
	public void setSelectedIfInRectangle(Rectangle2D rectExternal) {
		ElementoProcessoStato processo;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				processo = (ElementoProcessoStato) lista.get(i);
				if (processo.isInRect(rectExternal)) {
					processo.setSelected(true);
				}
			}
		}

	}

	/**
	 * ritorna un iteratore della lista
	 * @return iteratore della lista
	 */
	public Iterator iterator(){
		return lista.iterator();
	}

	/** Modifica il nome dell'elemento in ingresso se questo inizia con '*'_
		Molto utile per aggiustare il nome dopo un'operazione di paste. */
	public String adjustNameElement(String str) {
		String localStr;

		localStr = str;
		if (localStr.startsWith("*")) {
			localStr = localStr.substring(1, localStr.length());
			localStr = "cp_" + localStr;
			int j = 2;
			int i = localStr.length();
			while (this.giaPresente(localStr)) {
				localStr = localStr.substring(0, i);
				localStr = localStr + "_" + j;
				j++;
			}
		}
		return localStr;
	}

	public abstract void restoreFromFile();

	/**
	 * preleva la delega per la gestione degli eventi di manipolazione
	 * @return
	 */
	protected IListaProcStatoNotify getListaNotify() {
		return listaNotify;
	}

	/**
	 * setta la delega per la gestione degli eventi di manipolazione
	 * @param notify
	 */
	protected void setListaNotify(IListaProcStatoNotify notify) {
		listaNotify = notify;
	}

	/**
	 * Ritorna gli elementi contenuti nella lista in un array con un appropriato ordine
	 * @return Array di oggetti
	 */
	public Object[] toArray(){
		return lista.toArray();
	}

	/**
	 * preleva l'elemento processostato mediante identificatore
	 * @param id identificatore elemento
	 * @return ElementoProcessoStato null se non esiste nella lista
	 */
	public  ElementoProcessoStato getElementoById(long id){
		Iterator ite = lista.iterator();
		while(ite.hasNext()){
			ElementoProcessoStato eps = (ElementoProcessoStato) ite.next();
			if(eps.getId()==id){
				return eps;
			}
		}
		return null;
	}

	/**
	 * rimuove l'elemento mediante identificatore
	 * @param id dell'elemento da eliminare
	 * @return true se l'elemento ? stato tolto, false se l'elemento non ? stato eliminato
	 */
	public boolean removeElementById(long id){
		Iterator ite = lista.iterator();
				while(ite.hasNext()){
					ElementoProcessoStato eps = (ElementoProcessoStato) ite.next();
					if(eps.getId()==id){
						lista.remove(eps);
						removeNotify(eps);
						return true;
					}
				}
				return false;
	}


}