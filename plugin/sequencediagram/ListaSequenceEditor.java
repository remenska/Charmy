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
    

package plugin.sequencediagram; 

import java.util.Iterator;
import java.util.Vector;

import plugin.sequencediagram.data.PlugData;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.toolbar.SequenceToolBar;
import core.internal.runtime.data.IPlugData;
import core.internal.ui.PlugDataWin;

/**
 * Lista per al gestione dei SequenceEditor,
 * questa lista gestisce l'editor, separandone la parte dati dalla relativa parte
 * UI, ? relativa solo al plug in
 * Inoltre ? implementata mediante un Vettore (metodo poco efficente)
 * mi riservo il caso di implementarlo mediante un 
 * Albero R&B "Cormen, Leiserson, and Rivest's Introduction to Algorithms". 
 * @author michele
 * Charmy plug-in
 **/
public class ListaSequenceEditor {

	/**
	 * albero R&B  
	 */
	private Vector vettore;
	private PlugData plugData;
	private PlugDataWin plugDataWin;
	// toolbar di riferimento
	private SequenceToolBar seqToolBar;
	
	/**
	 * Costruttore
	 * Inizializza l'albero
	 * @param finestre
	 * @param dati
	 * @param SequenceToolBar
	 */
	public ListaSequenceEditor(PlugDataWin pdw, IPlugData pd,SequenceToolBar stb ) {
		vettore = new Vector();
		plugData = (PlugData)pd;
		plugDataWin = pdw;
		seqToolBar = stb;
	}

	public int size(){
		return vettore.size();
	}
	
	/**
	 * aggiunge un SequenceEditor editor alla lista
	 * @param te thread editor da aggiungere
	 * @return true se l'editor ? stato inserito false altrimenti
	 */
	public synchronized boolean add(SequenceEditor se) {
		boolean bo = vettore.add(se);
		//if(bo){
		//	te.getThreadElement().getListaThread().add(te.getThreadElement());
		//}
		return bo;
	}

	/**
	 * rimuove  un SequenceEditor dalla lista e il relativo Elemento
	 * @param te SequenceEditor da rimuovere
	 */
	public synchronized boolean remove(SequenceEditor te) {
		boolean bo = vettore.remove(te);
		if (bo) { //rimozione elemento
			plugData.getListaDS().remove(te.getSequenceElement());
			//te.getL .getListaThread().remove(te.getThreadElement());
		}
		return bo;
	}

	/**
	 * ritorna l'editor associato al SequenceElement
	 * o null se nessun SequenceElement ? collegato all'editor
	 * @param SequenceElement 
	 *  * @return
	 */
	public synchronized SequenceEditor getSeqEditor(SequenceElement se) {
		return getSeqEditor(se.getId());
	}

	/**
	 * pulisce il contenuto della lista 
	 */
	public void clearAll() {
		vettore.removeAllElements();
	}
	
	/**
	 * 
	 * @param identificativo SequenceElement
	 * @return editor legato all'id del SequenceElement
	 */
	public synchronized SequenceEditor getSeqEditor(long idSe) {
		SequenceEditor seqEditor = null;
		
		//seqEditor = new SequenceEditor
		SequenceElement se = plugData.getListaDS().getElementById(idSe);
		if(se == null){ //non esiste il sequence con quell'id
			return null;
		}
		
		
		
		//ListaDS lds = plugData.getListaDS();
		if (vettore.size() > 0) {
			Iterator iteratorLt = vettore.iterator();
			while (iteratorLt.hasNext()) {
				seqEditor = (SequenceEditor) iteratorLt.next();
				if (seqEditor.getSequenceElement().getId() == idSe) {
					return seqEditor;
				}
			}
		}

//altrimenti lo crea
		seqEditor = new
		       SequenceEditor(
		       		se,
					plugData, plugDataWin);
		seqEditor.setToolBar(plugDataWin.getEditToolBar(),seqToolBar);
		this.add(seqEditor);
		return seqEditor;
	}

	
	
	
	
	/*
	 * ritorna un array di SequenceEditor collegati all'idProcesso
	 * @param te thread editor da aggiungere
	 * @return null se non esistono editor, ThreadEditor[] altrimenti
	 */
	/*
	public synchronized ThreadEditor[] get(long idProcesso) {
		ThreadEditor[] te = null;
		int indice = 0;
		ListaThread lt = plugData.getListaDP().getListaThread(idProcesso);
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
	*/
	/**
	 * Classe di confronto per l'albero R&B
	 * @author michele
	 * Charmy plug-in
	 *
	 */
	//	public class MyComparator implements Comparator{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	//		public int compare(Object o1, Object o2) {
	//           ThreadElement t1 = (ThreadElement) o1;
	//			ThreadElement t2 = (ThreadElement) o2;
	//           String str1 = "" + t1.getListaThread().getIdProcesso();
	//           str1 =str1 + t1.getNomeThread();

	//			String str2 = "" + t2.getListaThread().getIdProcesso();
	//			str1 =str2 + t2.getNomeThread();
	//           return str1.compareTo(str2);
	//return 0;
	//		}
	//	}

}
