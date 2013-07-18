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
    

package plugin.topologychannels.data;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JOptionPane;

import core.internal.runtime.data.IPlugData;

import plugin.topologychannels.data.delegate.DelegateListaProcessoListener;
import plugin.topologychannels.graph.GraficoTipoDummy;
import plugin.topologychannels.graph.GraficoTipoProcesso;
import plugin.topologychannels.graph.GraficoTipoStore;
import plugin.topologychannels.pluglistener.IListaProcessoListener;
import plugin.topologychannels.resource.data.ElementoProcessoStato;
import plugin.topologychannels.resource.data.ListaProcessoStato;
//import core.internal.runtime.data.IPlugData;


/** Questa classe e' utilizzata per memorizzare la lista 
    dei processi definiti nel S_A_ Topology Diagram. */

public class ListaProcesso
	extends ListaProcessoStato
	implements Serializable //, SendListener
{

	/**
	 * delega per la gestione degli eventi
	 */
	private DelegateListaProcessoListener delegateListener = null;

	private PlugData plugData;

	/** Costruttore. 
	 * @param contenitore dei dati
	 * */
	public ListaProcesso(IPlugData pl) {
		super(null, pl);
		plugData = (PlugData)pl;
		setDelegateListener(new DelegateListaProcessoListener(this, pl));
	}

	/** Restituisce il numero dei processi attualmente inseriti nella lista
	    non considerando quelli fittizi ('dummmy'). */
	public int sizeNoDummy() {
		int risultato = 0;

		if (lista.isEmpty()) {
			return 0;
		} else {
			for (int j = 0; j < lista.size(); j++) {
				if (!((ElementoProcesso) lista.get(j)).isDummy()) {
					risultato++;
				}
			}
			return risultato;
		}
	}

	/** Inserisce nella lista una copia del processo in ingresso_ 
		Restituisce 'true' se l'inserimento ha successo. */
	public boolean addPasteElement(ElementoProcessoStato eps) {
		ElementoProcesso proc;
		ElementoProcesso ep = (ElementoProcesso) eps;

		if (lista == null)
			return false;
		if (ep == null)
			return false;
		proc =
			new ElementoProcesso(
				ep.getPointMiddle(),
				ElementoProcesso.GLOBALE,
				ep.getTipo(),
				ep.isDummy(),
				this.adjustNameElement("*" + ep.getName()));
		proc.adjustGraph(ep.getGrafico());
		return this.addElement(proc);
	}

	/**
	 * @author  Michele Stoduto
	 * rimuove tutti i processi selezionati (attributo selected = true)
	 * 
	 */
	public synchronized void removeAllSelected() {
		boolean ctrl;
		ElementoProcesso tmpProcesso;

		if (this.size() > 0) {
			
			boolean inCorso = plugData.getPlugDataManager().startSessione();
			Iterator ite = iterator();

			while (ite.hasNext()) {
				tmpProcesso = (ElementoProcesso) ite.next();
				if (tmpProcesso.isSelected()) {
					this.removeElement(tmpProcesso);
					ite = iterator();
				}

			}

			plugData.getPlugDataManager().stopSessione(inCorso);

		}
	}


	/** 
	 *  Restituisce il vettore contenente i nomi di tutti i processi definiti_  
	 *  
	 * Nel vettore non sono inclusi i nomi dei processi 'dummy'.
	 *  
	 */
	
	public Vector getAllProcessName() {
		ElementoProcesso ep;
		String nome;
		Vector vettore;

		vettore = new Vector();
		if (lista == null)
			return vettore;
		if (lista.isEmpty() == true)
			return vettore;
		try {
			for (int i = 0; i < lista.size(); i++) {
				ep = (ElementoProcesso) lista.get(i);
				if (!ep.isDummy()) {
					nome = ep.getName();
					vettore.addElement(nome);
				}
			}
		} catch (NullPointerException e) {
			String s =
				"Puntatore nullo dentro \n la classe ListaProcesso$getAllProcessName().\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return vettore;
	}

	/** Restituisce una linkedList contenente i nomi di tutti i processi definiti_  
	 *  come GLOBALE
	 *  Nel vettore non sono inclusi i nomi dei processi 'dummy'.
	 *   
	 */
	public LinkedList getListaAllGlobalName() {
		ElementoProcesso ep;
		String nome;
		LinkedList vettore;

		vettore = new LinkedList();
		if (lista == null)
			return vettore;
		if (lista.isEmpty() == true)
			return vettore;
		try {
			for (int i = 0; i < lista.size(); i++) {
				ep = (ElementoProcesso) lista.get(i);
				if ((!ep.isDummy()) && (ep.getAppartenenza()==ElementoProcesso.GLOBALE)) {
					nome = ep.getName();
					vettore.add(nome);
				}
			}
		} catch (NullPointerException e) {
			String s =
				"Puntatore nullo dentro \n la classe ListaProcesso$getAllProcessName().\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return vettore;
	}

	/** Restituisce una linkedList contenente i nomi di tutti i processi definiti_  
	 *  come USER
	 *  Nel vettore non sono inclusi i nomi dei processi 'dummy'.
	 *   
	 */
	public LinkedList getListaAllUserName() {
		ElementoProcesso ep;
		String nome;
		LinkedList vettore;

		vettore = new LinkedList();
		if (lista == null)
			return vettore;
		if (lista.isEmpty() == true)
			return vettore;
		try {
			for (int i = 0; i < lista.size(); i++) {
				ep = (ElementoProcesso) lista.get(i);
				if ((!ep.isDummy()) 
				      && (ep.getAppartenenza()==ElementoProcesso.USER)) {
					nome = ep.getName();
					vettore.add(nome);
				}
			}
		} catch (NullPointerException e) {
			String s =
				"Puntatore nullo dentro \n la classe ListaProcesso$getAllProcessName().\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return vettore;
	}





	/** Prende in ingresso una lista di nomi di processo e restituisce la lista 
		di tutti i nomi di processi attualmente definiti nell'architettura che
		per? non appartengono alla lista data in ingresso_ Non vengono inclusi 
		i nomi dei processi 'dummy'. */
	public LinkedList getAllRemain(LinkedList tmplistname) {
		ElementoProcesso ep;
		String nome;
		LinkedList risultato;

		risultato = new LinkedList();
		if (lista == null)
			return risultato;
		if (lista.isEmpty() == true)
			return risultato;
		try {
			for (int i = 0; i < lista.size(); i++) {
				ep = (ElementoProcesso) lista.get(i);
				if (!ep.isDummy()) {
					nome = ep.getName();
					if (!giaPresente(nome, tmplistname))
						risultato.add(nome);
				}
			}
		} catch (NullPointerException e) {
			String s =
				"Puntatore nullo dentro \n la classe ListaProcesso$getAllRemain().\n"
					+ e.toString();
			JOptionPane.showMessageDialog(
				null,
				s,
				"Condizione di errore!",
				JOptionPane.WARNING_MESSAGE);
			return null;
		}
		return risultato;
	}

	/** Restituisce la lista processo (come LinkedList)
		dopo aver eliminato i processi dummy. */
	public LinkedList getListProcessNoDummy() {
		LinkedList listaNoDummy = new LinkedList();
		ElementoProcesso tmpProcesso;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				tmpProcesso = (ElementoProcesso) lista.get(i);
				if (!tmpProcesso.isDummy()) {
					listaNoDummy.add(tmpProcesso);
				}
			}
		}
		return listaNoDummy;
	}

	/** Restituisce la lista processo dopo 
		aver eliminato i processi dummy. */
	public ListaProcesso getListaProcessoSenzaDummy() {
		ListaProcesso listaNoDummy = new ListaProcesso(plugData);
		ElementoProcesso tmpProcesso;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				tmpProcesso = (ElementoProcesso) lista.get(i);
				if (!tmpProcesso.isDummy()) {
					listaNoDummy.addElement(tmpProcesso);
				}
			}
		}
		return listaNoDummy;
	}

	/** Concatena la lista processo con quella passata in ingresso_ 
		Se (dummy=true) la lista restituita contiene i processi
		dummy, altrimenti vengono esclusi. */
	public ListaProcesso concatenaListaProcesso(
		ListaProcesso lp_one,
		boolean dummy) {
		ListaProcesso lp_risultato = new ListaProcesso(plugData);
		ElementoProcesso tmpProcesso;
		
		if (lista!=null)
		{
			for (int i=0; i<lista.size(); i++)
			{
				tmpProcesso = (ElementoProcesso)lista.get(i);
				if ((dummy)||(!tmpProcesso.isDummy()))
				{
					lp_risultato.addElement(tmpProcesso);
				}
			}
		}
		if (lp_one!=null)
		{
			for (int i=0; i<lp_one.size(); i++)
			{
				tmpProcesso = (ElementoProcesso)lp_one.getElement(i);
				if ((dummy)||(!tmpProcesso.isDummy()))
				{    			
					lp_risultato.addElement(tmpProcesso);
				}
			}    		
		}
		return lp_risultato;    	
	}

	
	/** Clonazione dell'oggetto. 
	 * */
	public ListaProcesso cloneListaProcesso()
	{	
		ElementoProcesso tmpElementoProcesso = null;
		ElementoProcesso clonedElementoProcesso = null;
		ListaProcesso cloned = new ListaProcesso(plugData);
		int j=0;
		
		while (j<lista.size())
		{
			tmpElementoProcesso = (ElementoProcesso)(lista.get(j));
			clonedElementoProcesso = tmpElementoProcesso.cloneProcesso();
			(cloned.lista).add(clonedElementoProcesso);
			j++;	
		}

		return cloned;     	
	}
	
	
	
	
	/**
	 *  (non-Javadoc)
	 */
	public void restoreFromFile() {
		GraficoTipoProcesso grafico1;
		GraficoTipoStore grafico2;
		GraficoTipoDummy grafico3;
		ElementoProcesso proc;
		int tipoProcesso;
		boolean isdummy;

		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {
				proc = (ElementoProcesso) (lista.get(i));
				isdummy = proc.isDummy();
				if (isdummy) {
					grafico3 = (GraficoTipoDummy) (proc.getGrafico());
					grafico3.restoreFromFile();
				} else {
					tipoProcesso = proc.getTipo();
					if (tipoProcesso == ElementoProcesso.PROCESS) {
						grafico1 = (GraficoTipoProcesso) (proc.getGrafico());
						grafico1.restoreFromFile();
					} else {
						grafico2 = (GraficoTipoStore) (proc.getGrafico());
						grafico2.restoreFromFile();
					}
				}
			}
		}
	}

	/**
	 * aggiunta di un listener per la lista
	 * @param ilpl
	 */
	public void addListener(IListaProcessoListener ilpl) {
		this.delegateListener.add(ilpl);
	}

	/**
	 * rimozione di   un listener per la lista
	 * @param ilpl
	 */
	public void removeListener(IListaProcessoListener ilpl) {
		this.delegateListener.removeElement(ilpl);
	}

	/**
	 * rimuove tutti i listener registrati
	 *
	 */
	public void removeAllListener() {
		this.delegateListener.removeAllElements();
	}

	/**
	 * preleva il listener delegato
	 * @return
	 */
	public DelegateListaProcessoListener getDelegateListener() {
		return delegateListener;
	}

	/**
	 * setta la classe delega per il listener
	 * @param listener
	 */
	public void setDelegateListener(DelegateListaProcessoListener listener) {
		delegateListener = listener;
		delegateListener.setListaProcesso(this);
		setListaNotify(delegateListener);
	}

	/* (non-Javadoc)
	 * @see data.ListaProcessoStato#getElementoById(long)
	 */
	public ElementoProcesso getProcessoById(long id) {
		return (ElementoProcesso) super.getElementoById(id);
	}

	/**
	 * aggiunge un elemento generico alla lista
	 * sostituisce StateEditor.addUserProcess()
	 * @return true se l'elemento ? stato aggiunto
	 */
	public boolean addGenerico(){
		return addElement( new ElementoProcesso("Proc", ElementoProcesso.USER));
	}

	/**
	 * ridefinizione di paint list per disegnare solo processi
	 * globali
	 * @param Classe Graphics2D dove disegnare
	 */
	public void paintLista(Graphics2D g2D) {
		if (lista != null) {
			for (int i = 0; i < lista.size(); i++) {

				ElementoProcesso ep = (ElementoProcesso) lista.get(i);
				if(ep.getAppartenenza() == ElementoProcesso.GLOBALE){
					ep.paintElemento(g2D);				
				}
			}
		}
	}
	
	public void setUnselected(){
		Iterator ite = iterator();
		while (ite.hasNext()) {
			((ElementoProcesso) ite.next()).setSelected(false);
		}
	}

	
}