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
    

package plugin.sequencediagram.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.data.delegate.DelegateListaDSListener;
import plugin.sequencediagram.pluglistener.IListaDSListener;
import core.internal.runtime.data.IPlugData;



/**
 * listaRappresentante la dinamica del sistema
 * aggrega linked list invece della precedente versione che la ereditava
 *  */

public class ListaDS implements Serializable 
{

	/**
	 * registrazioni listener
	 */
	private DelegateListaDSListener listener;
	private IPlugData plugData;

	/**
	 * linked lista da gestire, serve per la gesione degli eventi
	 */
	private LinkedList linkedList;
	

	/** Costruttore. 
	 * @param Contenitore dei dati
	 * */
	public  ListaDS(IPlugData plugData) 
	{
   		super();
		listener = new  DelegateListaDSListener(plugData, this);
		this.plugData = plugData;
		linkedList = new LinkedList();
	}


	/**
	 * controlla che un nome di SequenceElemnt si gi? presente
	 * @param nome da controllare
	 *  @return true se il nome ? presente, false altrimenti
	 */
	public boolean giaPresente(String nomeSeq){
		Iterator ite = linkedList.iterator();
		while (ite.hasNext()){
			SequenceElement se = (SequenceElement) ite.next();
			if(se.getName().equals(nomeSeq)) {
				return true;
			}
		}
		return false;
	}




	/** Restituisce tutti i flussi definiti in tutte le dinamiche di sistema_
		Tale metodo richiama il metodo getAllMessageName() di ogni oggetto della dinamica 
		di sistema, il quale recupera tutti i messaggi, senza ripetizioni, che si scambiano 
		le componenti_ La lista dei nomi restituiti non ha duplicati. */
	public LinkedList getAllMessageName()
	{
		SequenceElement ds;
		
		String nome;
		LinkedList risultato = new LinkedList();
		if (linkedList.isEmpty()) return risultato;
		try {
     		for (int i=0; i<linkedList.size(); i++){
				ds = (SequenceElement)(linkedList.get(i));
	      		
				LinkedList listaMess = ds.getListaSeqLink().getAllMessageName();
	      		for (int j=0;j<listaMess.size();j++){
			   		nome = (String) listaMess.get(j);
			   		if ((!risultato.contains(nome))){
			   	  		risultato.add(nome);
			   	  	} 
		       	} 
	      	} 	
     	} 
     	catch (NullPointerException e){
	    	String s = "Uso di un puntatore nullo \n dentro la classe ListaDS$getAllMessageName.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return risultato;
		}
		return risultato;
	}
	
	/**
	 * ritorna una linkedList contenente tutti i nomi delle classi
	 * @return
	 */
	public LinkedList getAllClassName()
	{
		SequenceElement ds;
		String nome;
		LinkedList risultato = new LinkedList();

		if (linkedList.isEmpty()) return risultato;
		try {
     		for (int i=0; i<linkedList.size(); i++){
	      		ds = (SequenceElement)(linkedList.get(i));
	      		LinkedList listaClassi = ds.getListaClasse().getListaAllClassName();
	      		for (int j=0;j<listaClassi.size();j++){
			   		nome = (String) listaClassi.get(j);
			   		if ((!risultato.contains(nome))){
			   	  		risultato.add(nome);
			   	  	} 
		       	} 
	      	} 	
     	} 
     	catch (NullPointerException e){
	    	String s = "Uso di un puntatore nullo \n dentro la classe ListaDS$getAllMessageName.\n" + e.toString();
            JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
            return risultato;
		}		
		return risultato;
	}
	

	/**
	 * aggiunge un elemento alla lista
	 * @param o SequenceElement da inserire
	 * @return true se l'iserimento ? andato a buon fine
	 */
	public boolean add(SequenceElement o) {
		boolean bo = linkedList.add(o);
		if(bo){
			listener.sequenceAdded(o);
			o.addListener(listener);	
		}
		return bo;
	}



	/**
	 * ritorna l'elemento all'indice index
	 * @param index dell'elemento
	 * @return elemento
	 */
	public SequenceElement get(int index) {
		return (SequenceElement)linkedList.get(index);
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return linkedList.isEmpty();
	}

	/**
	 * rimuove l'elemento con indice 
	 * @param index
	 * @return l'elemento rimosso
	 */
	public SequenceElement  remove(int index) {
		SequenceElement  tmp = (SequenceElement)linkedList.remove(index);
		if(tmp != null){
			tmp.removeListener(listener);
			listener.sequenceRemoved(tmp);
		}
		return tmp;
	}

	/**
	 * @param o
	 * @return true se l'elemento ? stato rimosso
	 */
	public boolean remove(SequenceElement o) {
		boolean bo = linkedList.remove(o);
		if(bo){
			o.removeListener(listener);	
			listener.sequenceRemoved(o);
		}
		return bo;
	}

	/**
	 * @return
	 */
	public int size() {
		return linkedList.size();
	}

	/**
	 * aggiunge un listener
	 * @param listener
	 */
	public void addListener(IListaDSListener listener) {
		this.listener.add(listener);
	}

	/**
	 * rimuove un listener
	 * @param listener
	 */
	public void removeListener(IListaDSListener listener) {
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
	 * ritorna l'iterator della lista
	 * @return
	 */
	public Iterator iterator() {
		return linkedList.iterator();
	}

	
	/**
	 * ritorna il SequenceElement identificato da id
	 * @return SequenceElement oppure null se non esiste
	 */
	public SequenceElement getElementById(long id) {
		
		for(int i = 0 ; i < linkedList.size(); i++){
			SequenceElement se = (SequenceElement)linkedList.get(i);
			if(se.getId()==id){
				return se;
			}
		}
		return null;
	}


	/**
	 * rimuove tutti gli elementi contenuti nella lista
	 * la rimozione viene vista come un'unica sessione
	 */
	public void removeAll() {
		boolean bo = plugData.getPlugDataManager().startSessione();
		Iterator ite = linkedList.iterator();
		while (ite.hasNext()){
			SequenceElement se = (SequenceElement) ite.next();
			se.removeAll();
			this.remove(se);
			ite = linkedList.iterator();
		}
		plugData.getPlugDataManager().stopSessione(bo);
		
	}
}

