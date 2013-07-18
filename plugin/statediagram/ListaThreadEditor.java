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
    

package plugin.statediagram;


import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import core.internal.ui.PlugDataWin;

import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.PlugData;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.toolbar.StateToolBar;

//import core.internal.runtime.data.IPlugData;

/**
 * Lista per al gestione dei Thread Editor,
 * questa lista gestisce l'editor, separandone la parte dati dalla relativa parte
 * UI, ? relativa solo al plug in
 * Inoltre ? implementata mediante un Vettore (metodo poco efficente)
 * mi riservo il caso di implementarlo mediante un 
 * Albero R&B "Cormen, Leiserson, and Rivest's Introduction to Algorithms". 
 * @author michele
 * Charmy plug-in
 **/
public class ListaThreadEditor {

	/**
	 * albero R&B  
	 */
	private Vector vettore;
	private PlugData plugData;
	private PlugDataWin plugDataWin;
	/** Riferimento alla StateToolBar. */
	private StateToolBar rifStateToolBar;
	
	/**
	 * Utilizzato per tenere il riferimento 
	 * del tab attivo nell'editor
	 */
	private TreeMap mappa;
	
	/**
	 * Costruttore
	 * Inizializza l'albero
	 *
	 */
	public ListaThreadEditor(PlugDataWin pdw, PlugData pd,StateToolBar rifToolBar ) {
		vettore = new Vector();
		plugData = pd;
		plugDataWin = pdw;
		rifStateToolBar = rifToolBar;
		mappa = new TreeMap();
	}




	/**
	 * aggiunge un thread editor alla lista
	 * @param te thread editor da aggiungere
	 * @return true se l'editor ? stato inserito false altrimenti
	 */
	public synchronized boolean add(ThreadEditor te) {
		boolean bo = vettore.add(te);
		return bo;
	}

	/**
	 * rimuove  un thread editor dalla lista
	 * @param te thread editor da rimuovere
	 */
	public synchronized boolean remove(ThreadEditor te) {
		boolean bo = vettore.remove(te);
		if (bo) { //rimozione elemento
			te.getThreadElement().getListaThread().remove(te.getThreadElement());
		}
		return bo;
		}

	/**
	 * ritorna un array di editor collegati all'idProcesso
	 * @param te thread editor da aggiungere
	 * @return null se non esistono editor, ThreadEditor[] altrimenti
	 */
	public synchronized ThreadEditor[] get(long idProcesso) {
		ThreadEditor[] te = null;
		int indice = 0;
		ListaThread lt = plugData.getListaDP().getListaThread(idProcesso);
		genEditor(lt);
		if (lt.size() > 0) {
			te = new ThreadEditor[lt.size()];
			Iterator iteratorLt = lt.iterator();
			while (iteratorLt.hasNext()) {
				ThreadElement threadEle = (ThreadElement) iteratorLt.next();
				Iterator iteVettore = vettore.iterator();
				while (iteVettore.hasNext()) {
					ThreadEditor threadEditor = (ThreadEditor) iteVettore.next();
					if (threadEditor.getThreadElement() == threadEle) {
						te[indice] = threadEditor;
						indice++;
					}
				}
			}
		}
		return te;
	}


	/**
	 *controlla se manca qualche editor e lo ricrea in caso di mancanza
	 *
	 */
	private void genEditor(ListaThread lt){
		boolean trovato = false;
		if (lt.size() > 0) {
			if(contaVettori(lt) != lt.size()){			
				Iterator iteratorLt = lt.iterator();
				while (iteratorLt.hasNext()) {
					trovato = false;
					ThreadElement threadEle = (ThreadElement) iteratorLt.next();
					Iterator iteVettore = vettore.iterator();
					while (iteVettore.hasNext()) {
						ThreadEditor threadEditor = (ThreadEditor) iteVettore.next();
						if (threadEditor.getThreadElement() == threadEle) {
							trovato = true;
							break;
						}
					}
				if(!trovato){
					ThreadEditor tedit =
					  new 	 ThreadEditor(
							plugDataWin.getStatusBar(),
							lt,
							plugData, threadEle);
					tedit.setToolBar(
							plugDataWin.getEditToolBar(),
							rifStateToolBar);
					add(tedit);
							iteratorLt = lt.iterator();		
				}
			}
		}		
	}	
}

	/**
	 * conta i threadEditor associati alla listaThread
	 * @param lt
	 * @return numero di threadeditor associati alla listaThread
	 */
	private int contaVettori(ListaThread lt){
		int indice =  0;
	    if(vettore.size() == 0) return 0;	
		Iterator iteratorLt = lt.iterator();
		while (iteratorLt.hasNext()) {
			ThreadElement threadEle = (ThreadElement) iteratorLt.next();
			Iterator iteVettore = vettore.iterator();
			while (iteVettore.hasNext()) {
				ThreadEditor threadEditor = (ThreadEditor) iteVettore.next();
				if (threadEditor.getThreadElement() == threadEle) {
					indice++;
				}
			}
		}
	 return indice;
	}	
	
	
	/**
	 * pulisce la mappa dei thread attivi 
	 */
	public void clear() {
		mappa.clear();
	}

	/**
	 * controlla se nella mappa c'? l'oggetto passato in argomento
	 * @param value
	 * @return true se l'oggetto ? contenuto nella mappa
	 */
	public boolean containsValue(Integer value) {
		return mappa.containsValue(value);
	}

	/**
	 * ritorna l'indice attivo relativo alla ListaThread
	 * @param key ListaThread
	 * @return -1 se non esiste una entry con la chiave ListaThread
	 *                       altrimenti torna il tab attivo
	 */
	public int get(Long key) {
		Integer valore = (Integer)mappa.get(key); 
		if(valore !=null){
		return valore.intValue();
		}
	   return -1;
	}

	/**
	 * Inserisce nella lista un intero rappresentante il tab attivo
	 * e con chiave ListaThread
	 * @param key ListaThread che realizza la chiave
	 * @param value Intero indicante il tab attivo
	 * @return Integer contenete il vecchio valore oppure null se non esisteva un valore in precedenza
	 */
	public Integer put(Long key,Integer value) {
		return (Integer)mappa.put((Object)key,(Object) value);
	}

}
