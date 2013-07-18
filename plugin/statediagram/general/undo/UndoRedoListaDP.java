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
    

package plugin.statediagram.general.undo;


import java.awt.Point;

import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.PlugData;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.eventi.listaDP.AddDPLTEvento;
import plugin.statediagram.eventi.listaDP.AddDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.AddDPStatoEvento;
import plugin.statediagram.eventi.listaDP.AddDPThreadEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPLTEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPStatoEvento;
import plugin.statediagram.eventi.listaDP.RemoveDPThreadEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPLTEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPMessaggioEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPStatoEvento;
import plugin.statediagram.eventi.listaDP.UpdateDPThreadEvento;
import plugin.statediagram.pluglistener.IListaDPListener;
import plugin.topologydiagram.data.ElementoCanale;
import plugin.topologydiagram.data.ElementoProcesso;
import plugin.topologydiagram.data.ListaCanale;
import plugin.topologydiagram.data.ListaProcesso;
import plugin.topologydiagram.eventi.listacanali.AddCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.RemoveCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.UpdateCanaleEvento;
import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import core.resources.general.undo.UndoRedoInfo;
import core.resources.general.undo.UndoRedoManager;
import core.resources.general.undo.VectorUndoRedoInfo;

/**
 * @author Michele Stoduto
 * La classe implementa i metodi per fare l'Undo e il Redo del sistema,
 * la classe � necessaria poiche si � implementato il sistema di messaggistica
 * E' un listener globale (mettendosi in ascolto di tutte le modifiche effettuate ai dati)
 * 
 */


/* non utilizzata poiche si � preferito elaborare come il vecchio metodo 
 * di undo redo(ogni thread Editor con una propria classe di undo/redo)
 */ 

public class UndoRedoListaDP
	extends UndoRedoManager
	implements IListaDPListener {

	private boolean registra = true;
	
	private PlugData pdThread;
	private plugin.topologydiagram.data.PlugData pdTopology=(plugin.topologydiagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.topology");

	/* (non-Javadoc)
	 * @see general.ImpMainTabPanel#init()
	 */
	protected void init() {

		pdThread=((PlugData)plugData);
		pdThread.getListaDP().addListener(this);

	}


	/**
	 * esegue l'undo passato come parametro
	 * @param undoInfo undo da eseguire (operazione inversa di tipo)
	 */
	protected void eseguiUndo(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		eseguiUndoListaThread(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoThread(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoStato(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoMessaggio(vectorUndoInfo, scaleX, scaleY);

	}


	/**
	 * controlla l'undo della lista thread, in questo caso non fa nulla in quanto 
	 * la gestione � lasciata all'undo dei processi
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoListaThread(VectorUndoRedoInfo vectorUndoInfo,
	double scaleX,
	double scaleY){
	}
	

	/**
	 * esegue l'undo di un threadElement (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoThread(VectorUndoRedoInfo vectorUndoInfo,
	double scaleX,
	double scaleY){
		
		ListaDP ldp;
		UndoRedoInfo undoInfo;
		ThreadElement te;
		ListaThread lt;
		
		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_THREAD :

				AddDPThreadEvento aep = (AddDPThreadEvento) undoInfo.getObject();
					ldp = aep.getSorgente();
					lt = aep.getListaThread();
					lt.remove(aep.getThreadElement());

					break;

				case UndoRedoTipo.REMOVE_THREAD :
					//aggiungo il processo
 					RemoveDPThreadEvento rep = (RemoveDPThreadEvento) undoInfo.getObject();
					lt = rep.getListaThread();
					lt.add(rep.getThreadElement());
					break;

				case UndoRedoTipo.UPDATE_PROCESSO :
					//aggiorno il processo attuale con quello precedente
					//l'evento � generato a causo dello spostamento da una
					//lista ad un'altra
					break;

				default :
					break;
			}
		}
		
		
		
		
		
	}
	
	private void eseguiUndoStato(VectorUndoRedoInfo vectorUndoInfo,
	double scaleX,
	double scaleY){
		
	}
	
	private void eseguiUndoMessaggio(VectorUndoRedoInfo vectorUndoInfo,
	double scaleX,
	double scaleY){
	}
	
	

	/**
	 * esegue il redo di un threadElement (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoThread(VectorUndoRedoInfo vectorUndoInfo,
	double scaleX,
	double scaleY){
		
		ListaDP ldp;
		UndoRedoInfo undoInfo;
		ThreadElement te;
		ListaThread lt;
		
		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_THREAD :
				AddDPThreadEvento aep = (AddDPThreadEvento) undoInfo.getObject();
					ldp = aep.getSorgente();
					lt = aep.getListaThread();
					lt.add(aep.getThreadElement());
					break;

				case UndoRedoTipo.REMOVE_THREAD :
					RemoveDPThreadEvento rep = (RemoveDPThreadEvento) undoInfo.getObject();
					lt = rep.getListaThread();
					lt.remove(rep.getThreadElement());
					break;

				case UndoRedoTipo.UPDATE_PROCESSO :
					break;

				default :
					break;
			}
		}

	}
	
	
	
	/** Restituisce un punto le cui coordinate sono quelle del punto in ingresso
		aggiornate tenendo conto dei fattori di scala per l'asse X e per l'asse Y. */
	private Point updateGetPoint(Point pnt, double scaleX, double scaleY) {
		Point pp = new Point((int) (pnt.x / scaleX), (int) (pnt.y / scaleY));
		return pp;
	}







	/**
	 * esegue l'undo dei Canali, presuppone che sia stato
	 * eseguito UndoProcesso contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiUndoCanale(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaProcesso lp;
		UndoRedoInfo undoInfo;
		ListaCanale lc;
		ElementoCanale ec;
		ElementoProcesso epStart;
		ElementoProcesso epEnd;

		/**
		 * sistemazione canali
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_CANALE :
					/*
					 * rimuovo il canale
					 */
					AddCanaleEvento ace =
						(AddCanaleEvento) undoInfo.getObject();
					lc = ace.getSorgente();
					ec = ace.getElementoCanale();
					lc.removeById(ec.getId());

					break;

				case UndoRedoTipo.REMOVE_CANALE :
					/*
					 *aggiungo di nuovo il canale 
					 */
					//RemoveCanaleEvento
					RemoveCanaleEvento rce =
						(RemoveCanaleEvento) undoInfo.getObject();
					lc = rce.getSorgente();
					ec = rce.getElementoCanale();
					epStart =
						pdTopology.getListaProcesso().getProcessoById(
							((ElementoProcesso) ec.getElement_one())
								.getId());
					epEnd =
						pdTopology.getListaProcesso().getProcessoById(
							((ElementoProcesso) ec.getElement_two())
								.getId());

					ElementoCanale nuovoEc = new ElementoCanale(epStart, epEnd);
					nuovoEc.setValue(ec, true);
					lc.addElement(nuovoEc);

					break;

				case UndoRedoTipo.UPDATE_CANALE :
					/*
					 * riporto il canale alla precedente versione
					 */

					UpdateCanaleEvento uce =
						(UpdateCanaleEvento) undoInfo.getObject();
					lc = uce.getSorgente();
					ElementoCanale ecOld = uce.getVecchioElementoCanale();
					ec = lc.getCanaleById(ecOld.getId());
					if (ec != null) {
						ec.setValue(ecOld, false);

					}
					break;

				default :
					break;
			}
		}
	}

	/**
		 * esegue l'undo della ListaThread, presuppone che sia stato
		 * eseguito UndoProcesso  e Undo Canale  contenuti nella lista
		 * @param vectorUndoInfo lista degli undo
		 */
	private void eseguiUndoLT(VectorUndoRedoInfo vectorUndoInfo) {

		UndoRedoInfo undoInfo;
		ListaDP listaDP;
		/**
		 * sistemazione canali
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_LISTATHREAD :
					/*
					 * rimuovo la lista canale
					 */
					AddDPLTEvento add = (AddDPLTEvento) undoInfo.getObject();
					listaDP = add.getListaDP();
					listaDP.remove(add.getListaThread());

					break;
				case UndoRedoTipo.REMOVE_LISTATHREAD :
					/*
					 *aggiungo di nuovo il canale 
					 */
					RemoveDPLTEvento rem =
						(RemoveDPLTEvento) undoInfo.getObject();
					listaDP = rem.getListaDP();
					ListaThread lt =
						listaDP.getListaThread(
							rem.getListaThread().getIdProcesso());
					if (lt != null) {
						listaDP.remove(lt);
						//rimuovo eventuale nuova lista e aggiungo la vecchia
					}
					listaDP.add(rem.getListaThread());

					break;
				default :
					break;
			}
		}
	}


	/**
	 * esegue il redo dei processi contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiRedoProcesso(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaProcesso lp;
		UndoRedoInfo undoInfo;
		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_PROCESSO :
					AddEPEvento aep = (AddEPEvento) undoInfo.getObject();
					lp = aep.getSorgente();
					ElementoProcesso aen = aep.getElementoProcesso();
					lp.addElement(aen);
					break;

				case UndoRedoTipo.REMOVE_PROCESSO :
					//rimuovo il processo
					RemoveEPEvento rep = (RemoveEPEvento) undoInfo.getObject();
					lp = rep.getSorgente();
					ElementoProcesso ren = rep.getElementoProcesso();
					lp.removeElementById(ren.getId());
					break;

				case UndoRedoTipo.UPDATE_PROCESSO :
					//aggiorno il processo attuale con quello precedente
					/**
					 * nuovo e vecchio sono cloni dell'elemento originale
					 */
					UpdateEPEvento uep = (UpdateEPEvento) undoInfo.getObject();
					ElementoProcesso nuovo = uep.getNewElementoProcesso();
					ElementoProcesso vecchio = uep.getOldElementoProcesso();

					ElementoProcesso vero = getProcessoReale(nuovo);
					if (vero == null) { //qualcosa non quadra
						return;
					}

					vero.adjustGraph(nuovo.getGrafico());

					vero.setPoint(
						updateGetPoint(
							new Point(nuovo.getTopX(), nuovo.getTopY()),
							scaleX,
							scaleY));

					pdTopology.getListaCanale().updateListaCanalePosizione(vero);
					break;

				default :
					break;
			}
		}
	}

	/**
	 * esegue il Redo dei Canali, presuppone che sia stato
	 * eseguito RedoProcesso contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */

	private void eseguiRedoCanale(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {
		ListaProcesso lp;
		UndoRedoInfo undoInfo;
		ListaCanale lc;
		ElementoCanale ec;
		ElementoProcesso epStart;
		ElementoProcesso epEnd;

		/**
		 * sistemazione canali
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_CANALE :
					/*
					 * aggiungo il canale
					 */
					AddCanaleEvento rce =
						(AddCanaleEvento) undoInfo.getObject();
					lc = rce.getSorgente();
					ec = rce.getElementoCanale();
					epStart =
						pdTopology.getListaProcesso().getProcessoById(
							((ElementoProcesso) ec.getElement_one())
								.getId());
					epEnd =
						pdTopology.getListaProcesso().getProcessoById(
							((ElementoProcesso) ec.getElement_two())
								.getId());

					ElementoCanale nuovoEc = new ElementoCanale(epStart, epEnd);
					nuovoEc.setValue(ec, true);
					lc.addElement(nuovoEc);

					break;

				case UndoRedoTipo.REMOVE_CANALE :
					/*
					 *rimuovo di nuovo il canale 
					 */

					RemoveCanaleEvento ace =
						(RemoveCanaleEvento) undoInfo.getObject();
					lc = ace.getSorgente();
					ec = ace.getElementoCanale();
					lc.removeById(ec.getId());

					break;

				case UndoRedoTipo.UPDATE_CANALE :
					/*
					 * riporto il canale alla precedente versione
					 */

					UpdateCanaleEvento uce =
						(UpdateCanaleEvento) undoInfo.getObject();
					lc = uce.getSorgente();
					ElementoCanale ecNew = uce.getNuovoElementoCanale();
					//ec = uce.getNuovoElementoCanale();
					ec = lc.getCanaleById(ecNew.getId());
					if (ec != null) {
						ec.setValue(ecNew, false);
					}
					break;

				default :
					break;
			}
		}
	}

	/**
		 * esegue l'undo della ListaThread, presuppone che sia stato
		 * eseguito UndoProcesso  e Undo Canale  contenuti nella lista
		 * @param vectorUndoInfo lista degli undo
		 */
	private void eseguiRedoLT(VectorUndoRedoInfo vectorUndoInfo) {

		UndoRedoInfo undoInfo;
		ListaDP listaDP;
		/**
		 * sistemazione canali
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_LISTATHREAD :
					/*
					 * aggiungo la lista canale
					 */

				AddDPLTEvento rem =
						(AddDPLTEvento) undoInfo.getObject();
					listaDP = rem.getListaDP();
					ListaThread lt =
						listaDP.getListaThread(
							rem.getListaThread().getIdProcesso());
					if (lt != null) {
						listaDP.remove(lt);
						//rimuovo eventuale nuova lista e aggiungo la vecchia
					}
					listaDP.add(rem.getListaThread());

					break;
				case UndoRedoTipo.REMOVE_LISTATHREAD :
					/*
					 *rimuovo di nuovo il canale 
					 */
				RemoveDPLTEvento add = (RemoveDPLTEvento) undoInfo.getObject();
					listaDP = add.getListaDP();
					listaDP.remove(add.getListaThread());

					break;
				default :
					break;
			}
		}
	}

	/**
	 * esegue il redo passato come parametro
	 * @param undoInfo undo da eseguire (operazione inversa di tipo)
	 */
	protected void eseguiRedo(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		eseguiRedoThread(vectorUndoInfo, scaleX, scaleY);
	}

	/**
	 * prende dalla lista dei processi (user e globali) l'elemento il cui clone � ep
	 * @return l'elemento reale oppure null
	 */
	private ElementoProcesso getProcessoReale(ElementoProcesso ep) {

		ElementoProcesso vero =
		pdTopology.getListaProcesso().getProcessoById(ep.getId());

		return vero;
	}



	public void listaThreadAdded(AddDPLTEvento ate) {
//		add(UndoRedoTipo.ADD_LISTATHREAD, ate, ate.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#listaThreadRemoved(eventi.listaDP.RemoveDPLTEvento)
	 */
	public void listaThreadRemoved(RemoveDPLTEvento rte) {
//		add(UndoRedoTipo.REMOVE_LISTATHREAD, rte, rte.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#listaThreadUpdated(eventi.listaDP.UpdateDPLTEvento)
	 */
	public void listaThreadUpdated(UpdateDPLTEvento ute) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#statoAdded(eventi.listaDP.AddDPStatoEvento)
	 */
	public void statoAdded(AddDPStatoEvento event) {
		add(UndoRedoTipo.ADD_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#statoRemoved(eventi.listaDP.RemoveDPStatoEvento)
	 */
	public void statoRemoved(RemoveDPStatoEvento event) {
		add(UndoRedoTipo.REMOVE_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#statoUpdate(eventi.listaDP.UpdateDPStatoEvento)
	 */
	public void statoUpdate(UpdateDPStatoEvento event) {
		add(UndoRedoTipo.UPDATE_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#messaggioAdded(eventi.listaDP.AddDPMessaggioEvento)
	 */
	public void messaggioAdded(AddDPMessaggioEvento event) {
		add(UndoRedoTipo.ADD_MESSAGGIO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#messaggioRemoved(eventi.listaDP.RemoveDPMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveDPMessaggioEvento event) {
		add(UndoRedoTipo.REMOVE_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#messaggioUpdate(eventi.listaDP.UpdateDPMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateDPMessaggioEvento event) {
		add(UndoRedoTipo.UPDATE_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#listaDPRefresh()
	 */
	public void listaDPRefresh() {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#threadAdded(eventi.listaDP.AddDPThreadEvento)
	 */
	public void threadAdded(AddDPThreadEvento ate) {
		add(UndoRedoTipo.ADD_THREAD, ate, ate.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#threadRemoved(eventi.listaDP.RemoveDPThreadEvento)
	 */
	public void threadRemoved(RemoveDPThreadEvento rte) {
		add(UndoRedoTipo.REMOVE_THREAD, rte, rte.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#threadUpdated(eventi.listaDP.UpdateDPThreadEvento)
	 */
	public void threadUpdated(UpdateDPThreadEvento ute) {
		add(UndoRedoTipo.UPDATE_THREAD, ute, ute.getIdSessione());
	}


}
