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

//import plugin.statediagram.*;
//import plugin.statediagram.data.*;
import plugin.statediagram.data.delegate.DelegateListaStatoListener;
import plugin.statediagram.graph.*;
import plugin.statediagram.pluglistener.IListaStatoListener;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.ListaProcessoStato;


/** Questa classe e' utilizzata per memorizzare la lista 
	degli stati definiti in uno State Diagram. */

public class ListaStato extends ListaProcessoStato implements Serializable {

	/**
	 * delega per la gestione degli eventi
	 */
	private DelegateListaStatoListener delegateListener = null;

	/** Costruttore. */
	public ListaStato(IPlugData plugData) {
		super(null, plugData);
		setDelegateListener(new DelegateListaStatoListener(plugData,this));
	}

	/** Inserisce nella lista una copia dello stato in ingresso_ 
		Restituisce 'true' se l'inserimento ha successo. */
	public boolean addPasteElement(ElementoProcessoStato eps) {
		ElementoStato proc;
		ElementoStato es = (ElementoStato) eps;

		if (lista == null)
			return false;
		if (es == null)
			return false;
		proc = new ElementoStato(
		           es.getPointMiddle(), es.getTipo(),
  		           adjustNameElement("*" + es.getName()), null);
		proc.adjustGraph(es.getGrafico());
		return lista.add(proc);
	}


	/** Rimuove tutti gli stati selezionati dalla lista
	 * 
	 *  */
	public synchronized void removeAllSelected() {
		boolean ctrl;
		
		ElementoStato tmpStato;
		//ElementoCanale tmpCanale;
		//ListaCanale listChannel = new ListaCanale();

		if (this.size() > 0) {
			
			boolean inCorso = plugData.getPlugDataManager().startSessione();

			Iterator ite = iterator();
			while (ite.hasNext()) {
				tmpStato = (ElementoStato) ite.next();
				if (tmpStato.isSelected()) {
					this.removeElement(tmpStato);
					ite = iterator();
				}

			}
			//if (!inCorso) {
				plugData.getPlugDataManager().stopSessione(inCorso);
			//}
		}
	}

	public void setUnselected(){
		Iterator ite = iterator();
		while (ite.hasNext()) {
			((ElementoStato) ite.next()).setSelected(false);
		}
	}


	/** Rimuove tutti gli stati appartenenti alla lista in ingresso 
		(primo parametro)_ Restituisce la lista di tutti i messaggi 
		collegati ad almeno uno degli stati rimossi (messaggi scelti 
		tra quelli presenti nella lista data come secondo parametro). */
	public ListaMessaggio removeListSelected(LinkedList delStati, ListaMessaggio lcn) {
		boolean ctrl;
		ElementoStato tmpProcesso;
		ElementoMessaggio tmpCanale;
		ListaMessaggio listChannel = new ListaMessaggio(plugData);

		if ((delStati != null) && (!delStati.isEmpty())) {
			for (int i = 0; i < delStati.size(); i++) {
				tmpProcesso = (ElementoStato) delStati.get(i);

				if ((lcn != null) && (!lcn.isEmpty())) {
					for (int j = 0; j < lcn.size(); j++) {
						tmpCanale = (ElementoMessaggio) lcn.getElement(j);
						if ((tmpProcesso.equals(tmpCanale.getElement_one()))
							|| (tmpProcesso.equals(tmpCanale.getElement_two()))) {
							if (!listChannel.giaPresente(tmpCanale.getName())) {
								listChannel.add(tmpCanale);
							}
						}
					}
				}
				ctrl = removeElement(tmpProcesso);
			}
			return listChannel;
		} else {
			return null;
		}
	}

	/** Prende in ingresso una lista di nomi di stato e restituisce la lista 
	    di tutti i nomi di stati attualmente definiti nello State Diagram che 
	    per? non appartengano alla lista data in ingresso. */
	public LinkedList getAllRemain(LinkedList tmplistname) {
		ElementoStato ep;
		String nome;
		LinkedList risultato;

		risultato = new LinkedList();
		if (lista == null)
			return risultato;
		if (lista.isEmpty() == true)
			return risultato;
		try {
			for (int i = 0; i < lista.size(); i++) {
				ep = (ElementoStato) lista.get(i);
				nome = ep.getName();
				if (!giaPresente(nome, tmplistname))
					risultato.add(nome);
			}
		} catch (NullPointerException e) {
			String s =
				"Puntatore nullo dentro \n la classe ListaStateo$getAllRemain().\n" + e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return risultato;
	}

	/** Clonazione dell'oggetto. */
	public ListaStato cloneListaStato() {
		ElementoStato tmpElementoStato = null;
		ElementoStato clonedElementoStato = null;
		ListaStato cloned = new ListaStato(plugData);
		int j = 0;

		while (j < lista.size()) {
			tmpElementoStato = (ElementoStato) (lista.get(j));
			clonedElementoStato = tmpElementoStato.cloneStato();
			(cloned.lista).add(clonedElementoStato);
			j++;
		}

		return cloned;
	}

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile() {
		GraficoTipoStart grafico1;
		GraficoTipoBuild grafico2;
		//    	GraficoTipoEnd grafico3;
		ElementoStato stato;
		int tipoStato;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				stato = (ElementoStato) (lista.get(i));
				tipoStato = stato.getTipo();
				switch (tipoStato) {
					case ElementoStato.START :
						grafico1 = (GraficoTipoStart) (stato.getGrafico());
						grafico1.restoreFromFile();
						break;
						/*					case ElementoStato.END:
												grafico3 = (GraficoTipoEnd)(stato.getGrafico());
												grafico3.restoreFromFile();
												break;*/
					default : // case ElementoStato.BUILD:
						grafico2 = (GraficoTipoBuild) (stato.getGrafico());
						grafico2.restoreFromFile();
						break;
				}
			}
		}
	}


	/**
	 * aggiunta di un listener per la lista
	 * @param ilpl
	 */
	public void addListener(IListaStatoListener ilpl) {
		this.delegateListener.add(ilpl);
	}


	/**
	 * rimozione di   un listener per la lista
	 * @param ilpl
	 */
	public void removeListener(IListaStatoListener ilpl){
		this.delegateListener.removeElement(ilpl);
	}


	/**
	 * rimuove tutti i listener registrati
	 *
	 */
	public void removeAllListener(){
		this.delegateListener.removeAllElements();
	}
	
	
	/**
	 * controlla che vi sia gia inserito la stato di partenza
	 * @return true se lo stato di partenza gi? esiste, false altrimenti 
	 * 
	 */
	public boolean startExist(){
		Iterator ite = lista.iterator();
		while(ite.hasNext()){
			ElementoStato es = (ElementoStato) ite.next();
			if(es.getTipo() == ElementoStato.START){
			  return true;	
			}
		}
		return false;
	}


	/**
	 * Ritorna l'elemento di partenza
	 * @return ElementoStato di partenza, null se non esiste 
	 * 
	 */
	public ElementoStato getStartState(){
		Iterator ite = lista.iterator();
		while(ite.hasNext()){
			ElementoStato es = (ElementoStato) ite.next();
			if(es.getTipo() == ElementoStato.START){
			  return es;	
			}
		}
		return null;
	}

	/**
	 * Ritorna l'elemento di partenza
	 * @return ElementoStato di partenza, null se non esiste 
	 * 
	 */
	public boolean setStartState(ElementoStato state){
		Iterator ite = lista.iterator();
		while(ite.hasNext()){
			ElementoStato es = (ElementoStato) ite.next();
			if(es.getName().equals(state.getName())){
			 	es.setType(ElementoStato.START);
			    return true;	
			}		
		}
		return false;
	}


	/**
	 * ritorna la classe di delega listener
	 * @return
	 */
	public DelegateListaStatoListener getDelegateListener() {
		return delegateListener;
	}

	/**
	 * setta la gestione dei listener per i cambiamenti dei dati
	 * attenzione, ? una operazione che pu? disabilitare 
	 * gli ascoltatori precedenti
	 * @param listener
	 */
	public void setDelegateListener(DelegateListaStatoListener listener) {
		delegateListener = listener;
		listener.setListaStato(this);
		setListaNotify(delegateListener);
	}

}