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
    

package plugin.topologydiagram.general.undo;


import java.awt.Point;

import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaThread;
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
import plugin.topologydiagram.data.PlugData;
import plugin.topologydiagram.eventi.listacanali.AddCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.RemoveCanaleEvento;
import plugin.topologydiagram.eventi.listacanali.UpdateCanaleEvento;
import plugin.topologydiagram.eventi.listaprocesso.AddEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.RemoveEPEvento;
import plugin.topologydiagram.eventi.listaprocesso.UpdateEPEvento;
import plugin.topologydiagram.pluglistener.IListaCanaleListener;
import plugin.topologydiagram.pluglistener.IListaProcessoListener;
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
public class UndoRedoTopology
	extends UndoRedoManager
	implements IListaProcessoListener, IListaCanaleListener, IListaDPListener {

	private boolean registra = true;
	
	private PlugData pdTopology;
	private plugin.statediagram.data.PlugData pdThread;

	/* (non-Javadoc)
	 * @see general.ImpMainTabPanel#init()
	 */
	protected void init() {

		//plugData.setUndoRedoManager(this);
		/*
		 * registrazione listener
		 */
		pdTopology=(PlugData)plugData; 
		pdThread=(plugin.statediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.state");
		pdTopology.getListaProcesso().addListener(this);
		pdTopology.getListaCanale().addListener(this);
		pdThread.getListaDP().addListener(this);


	}


	/** Restituisce un punto le cui coordinate sono quelle del punto in ingresso
		aggiornate tenendo conto dei fattori di scala per l'asse X e per l'asse Y. */
	private Point updateGetPoint(Point pnt, double scaleX, double scaleY) {
		Point pp = new Point((int) (pnt.x / scaleX), (int) (pnt.y / scaleY));
		return pp;
	}


	/**
	 * esegue l'undo dei processi contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiUndoProcesso(
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
					//lo debbo rimuovere
					AddEPEvento aep = (AddEPEvento) undoInfo.getObject();
					lp = aep.getSorgente();
					ElementoProcesso ren = aep.getElementoProcesso();
					lp.removeElementById(ren.getId());

					break;

				case UndoRedoTipo.REMOVE_PROCESSO :
					//aggiungo il processo
					RemoveEPEvento rep = (RemoveEPEvento) undoInfo.getObject();
					lp = rep.getSorgente();
					ElementoProcesso ep = rep.getElementoProcesso();
					lp.addElement(ep);
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
					
					
					boolean bo = vero.testAndSet();
					vero.setValue(vecchio, false);

					vero.setPoint(
						updateGetPoint(
							new Point(vecchio.getTopX(), vecchio.getTopY()),
							scaleX,
							scaleY));
					vero.testAndReset(bo);
					pdTopology.getListaCanale().updateListaCanalePosizione(vero);
					break;

				default :
					break;
			}
		}

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

					ElementoCanale nuovoEc =
					    new ElementoCanale(
										epStart,
										epEnd,
										true,
										ec.getId(),
										null);     
					    
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
	 * esegue l'undo passato come parametro
	 * @param undoInfo undo da eseguire (operazione inversa di tipo)
	 */
	protected void eseguiUndo(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		eseguiUndoProcesso(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoCanale(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoLT(vectorUndoInfo);

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

					
					boolean bo = vero.testAndSet();
					vero.setValue(nuovo, false);

					vero.setPoint(
							updateGetPoint(
							new Point(nuovo.getTopX(), nuovo.getTopY()),
							scaleX,
							scaleY));
					vero.testAndReset(bo);
					
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

					ElementoCanale nuovoEc =
							new ElementoCanale(
											epStart,
											epEnd,
											true,
											ec.getId(),
											null);   
					
					
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

		eseguiRedoProcesso(vectorUndoInfo, scaleX, scaleY);
		eseguiRedoCanale(vectorUndoInfo, scaleX, scaleY);
		eseguiRedoLT(vectorUndoInfo);
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



	//GESTIONE LISTENER

	//IListaProcessoListener

	/*
	 * 
	 */
	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoAdded(eventi.AddEPEvento)
	 */
	public void processoAdded(AddEPEvento event) {
		add(UndoRedoTipo.ADD_PROCESSO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRemoved(eventi.RemoveEPEvento)
	 */
	public void processoRemoved(RemoveEPEvento event) {
		add(UndoRedoTipo.REMOVE_PROCESSO, event, event.getIdSessione());

	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoUpdate(eventi.UpdateEPEvento)
	 */
	public void processoUpdate(UpdateEPEvento event) {
		add(UndoRedoTipo.UPDATE_PROCESSO, event, event.getIdSessione());

	}

	/* (non-Javadoc)
	 * @see plugin.IListaProcessoListener#processoRefresh()
	 */
	public void processoRefresh() {
	}

	/*
	 * Canale Listener
	 */
	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleAdded(eventi.listacanali.AddCanaleEvento)
	 */
	public void canaleAdded(AddCanaleEvento event) {
		add(UndoRedoTipo.ADD_CANALE, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleRemoved(eventi.listacanali.RemoveCanaleEvento)
	 */
	public void canaleRemoved(RemoveCanaleEvento event) {
		add(UndoRedoTipo.REMOVE_CANALE, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleUpdate(eventi.listacanali.UpdateCanaleEvento)
	 */
	public void canaleUpdate(UpdateCanaleEvento event) {
		add(UndoRedoTipo.UPDATE_CANALE, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaCanaleListener#canaleRefresh()
	 */
	public void canaleRefresh() {
	}

	/*
	 * listaDP
	 */

	public void listaThreadAdded(AddDPLTEvento ate) {
		add(UndoRedoTipo.ADD_LISTATHREAD, ate, ate.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#listaThreadRemoved(eventi.listaDP.RemoveDPLTEvento)
	 */
	public void listaThreadRemoved(RemoveDPLTEvento rte) {
		add(UndoRedoTipo.REMOVE_LISTATHREAD, rte, rte.getIdSessione());
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
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#statoRemoved(eventi.listaDP.RemoveDPStatoEvento)
	 */
	public void statoRemoved(RemoveDPStatoEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#statoUpdate(eventi.listaDP.UpdateDPStatoEvento)
	 */
	public void statoUpdate(UpdateDPStatoEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#messaggioAdded(eventi.listaDP.AddDPMessaggioEvento)
	 */
	public void messaggioAdded(AddDPMessaggioEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#messaggioRemoved(eventi.listaDP.RemoveDPMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveDPMessaggioEvento event) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#messaggioUpdate(eventi.listaDP.UpdateDPMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateDPMessaggioEvento event) {
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
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#threadRemoved(eventi.listaDP.RemoveDPThreadEvento)
	 */
	public void threadRemoved(RemoveDPThreadEvento rte) {
	}

	/* (non-Javadoc)
	 * @see plugin.IListaDPListener#threadUpdated(eventi.listaDP.UpdateDPThreadEvento)
	 */
	public void threadUpdated(UpdateDPThreadEvento ute) {
	}

}
