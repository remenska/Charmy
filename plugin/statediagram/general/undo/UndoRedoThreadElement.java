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

import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.eventi.thread.AddThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.AddThreadStatoEvento;
import plugin.statediagram.eventi.thread.RemoveThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.RemoveThreadStatoEvento;
import plugin.statediagram.eventi.thread.UpdateThreadMessaggioEvento;
import plugin.statediagram.eventi.thread.UpdateThreadStatoEvento;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.pluglistener.IThreadElementListener;
import core.resources.general.undo.UndoRedoInfo;
import core.resources.general.undo.UndoRedoManager;
import core.resources.general.undo.VectorUndoRedoInfo;

/**
 * @author Michele Stoduto
 * La classe implementa i metodi per fare l'Undo e il Redo del sistema,
 * la classe ? necessaria poiche si ? implementato il sistema di messaggistica
 * E' un listener globale (mettendosi in ascolto di tutte le modifiche effettuate ai dati)
 * 
 */

public class UndoRedoThreadElement
	extends UndoRedoManager
	implements IThreadElementListener{

	private boolean registra = true;

	/* (non-Javadoc)
	 * @see general.ImpMainTabPanel#init()
	 */
	protected void init() {
	}

	/**
	 * esegue l'undo passato come parametro
	 * @param undoInfo undo da eseguire (operazione inversa di tipo)
	 */
	protected void eseguiUndo(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		eseguiUndoStato(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoMessaggio(vectorUndoInfo, scaleX, scaleY);
	}

	/**
	 * esegue l'undo di uno stato (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoStato(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaStato listaStato;
		UndoRedoInfo undoInfo;
		ThreadElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_STATO :
					//lo debbo rimuovere
					//AddThreadStatoEvento
					AddThreadStatoEvento aep =
						(AddThreadStatoEvento) undoInfo.getObject();
					te = aep.getSorgente();
					listaStato = aep.getListaStato();
					listaStato.removeElementById(
						aep.getElementoStato().getId());
					break;

				case UndoRedoTipo.REMOVE_STATO :
					//aggiungo lo stato
					RemoveThreadStatoEvento rep =
						(RemoveThreadStatoEvento) undoInfo.getObject();
					listaStato = rep.getListaStato();
					ElementoStato es = rep.getElementoStato().cloneStato();
					
					es.setPoint(
						updateGetPoint(
							new Point(es.getTopX(), es.getTopY()),
							scaleX,
							scaleY));
					
					listaStato.addElement(es);
					break;

				case UndoRedoTipo.UPDATE_STATO :
					/**
					 * nuovo e vecchio sono cloni dell'elemento originale
					 */
					UpdateThreadStatoEvento uep =
						(UpdateThreadStatoEvento) undoInfo.getObject();
					listaStato = uep.getListaStato();
					te = uep.getSorgente();
					ElementoStato nuovo = uep.getNuovoElementoStato();
					ElementoStato vecchio = uep.getVecchioElementoStato();
					ElementoStato vero =
						(ElementoStato) listaStato.getElementoById(
							vecchio.getId());
					if (vero != null) {
						
						boolean bo = vero.testAndSet();
						vero.setValue(vecchio);
						vero.setPoint(
								updateGetPoint(
								new Point(vecchio.getTopX(), vecchio.getTopY()),
								scaleX,
								scaleY));
						vero.testAndReset(bo);
						
						
						te.getListaMessaggio().updateListaCanalePosizione(vero);
					}

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
	 * esegue l'undo di un Messaggio , presuppone che sia stato
	 * eseguito UndoStato contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiUndoMessaggio(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaMessaggio listaMessaggio;
		UndoRedoInfo undoInfo;
		ThreadElement te;
		ListaThread lt;
		ElementoMessaggio elementoMessaggio;

		/**
		 * sistemazione messaggi
		 */

		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_MESSAGGIO :
					/*
					 * rimuovo il Messaggio
					 */
					AddThreadMessaggioEvento ace =
						(AddThreadMessaggioEvento) undoInfo.getObject();
					listaMessaggio = ace.getListaMessaggio();

					elementoMessaggio = ace.getElementoMessaggio();
					listaMessaggio.removeById(elementoMessaggio.getId());
					break;

				case UndoRedoTipo.REMOVE_MESSAGGIO :
					/*
					 *aggiungo di nuovo il Messaggio 
					 */
					//RemoveCanaleEvento
					RemoveThreadMessaggioEvento rce =
						(RemoveThreadMessaggioEvento) undoInfo.getObject();
					te=rce.getThreadElement();
					listaMessaggio = rce.getListaMessaggio();
					elementoMessaggio = rce.getElementoMessaggio();
					ListaStato listaStato =
						rce.getThreadElement().getListaStato();
					/*
					 * ricerca degli stati originali
					 */
					ElementoStato epStart =
						(ElementoStato) listaStato.getElementoById(
							elementoMessaggio.getElement_one().getId());

					ElementoStato epEnd =
						(ElementoStato) listaStato.getElementoById(
							elementoMessaggio.getElement_two().getId());

					ElementoMessaggio nuovoEc =
						new ElementoMessaggio(te,epStart, epEnd);
					nuovoEc.setValue(elementoMessaggio, true);

					listaMessaggio.addElement(nuovoEc);

					break;

				case UndoRedoTipo.UPDATE_MESSAGGIO :
					/*
					 * riporto il canale alla precedente versione
					 */
					UpdateThreadMessaggioEvento uce =
						(UpdateThreadMessaggioEvento) undoInfo.getObject();

					listaMessaggio = uce.getListaMessaggio();

					ElementoMessaggio ecOld = uce.getVecchioElementoMessaggio();
					elementoMessaggio =
						(ElementoMessaggio) listaMessaggio.getElementById(
							ecOld.getId());
					if (elementoMessaggio != null) {
						elementoMessaggio.setValue(ecOld, false);
					}
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

		eseguiRedoStato(vectorUndoInfo, scaleX, scaleY);
		eseguiRedoMessaggio(vectorUndoInfo, scaleX, scaleY);
	}

	/**
	 * esegue il redo di uno stato (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoStato(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaStato listaStato;
		UndoRedoInfo undoInfo;
		ThreadElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_STATO :
					//lo debbo aggiungere di nuovo
					//AddThreadStatoEvento
					AddThreadStatoEvento aep =
						(AddThreadStatoEvento) undoInfo.getObject();
					te = aep.getSorgente();

					listaStato = aep.getListaStato();
					listaStato.addElement(aep.getElementoStato().cloneStato());

					break;

				case UndoRedoTipo.REMOVE_STATO :
					//debbo rimuovere lo stato
					RemoveThreadStatoEvento rep =
						(RemoveThreadStatoEvento) undoInfo.getObject();

					listaStato = rep.getListaStato();
					listaStato.removeElementById(
						rep.getElementoStato().getId());

					break;

				case UndoRedoTipo.UPDATE_STATO :
					/**
					 * nuovo e vecchio sono cloni dell'elemento originale
					 */
					//debbo rimettere a posto la precedente modifica
					UpdateThreadStatoEvento uep =
						(UpdateThreadStatoEvento) undoInfo.getObject();
					listaStato = uep.getListaStato();
					te = uep.getSorgente();
					ElementoStato nuovo = uep.getNuovoElementoStato();
					ElementoStato vecchio = uep.getVecchioElementoStato();
					ElementoStato vero =
						(ElementoStato) listaStato.getElementoById(
							nuovo.getId());
					if (vero != null) {
						
						boolean bo = vero.testAndSet();
						vero.setValue(nuovo);
						vero.setPoint(
								updateGetPoint(
								new Point(nuovo.getTopX(), nuovo.getTopY()),
								scaleX,
								scaleY));
						vero.testAndReset(bo);
						
						te.getListaMessaggio().updateListaCanalePosizione(vero);
					}

					break;

				default :
					break;
			}
		}

	}

	/**
	 * esegue la redo di un Messaggio , presuppone che sia stato
	 * eseguito UndoStato contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiRedoMessaggio(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaMessaggio listaMessaggio;
		UndoRedoInfo undoInfo;
		ThreadElement te;
		ListaThread lt;
		ElementoMessaggio elementoMessaggio;

		/**
		 * sistemazione messaggi
		 */

		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_MESSAGGIO :
					/*
					 * raggiungo di nuovo il messaggio
					 */
					AddThreadMessaggioEvento ace =
						(AddThreadMessaggioEvento) undoInfo.getObject();
					te=ace.getThreadElement();
					
					listaMessaggio = ace.getListaMessaggio();
					elementoMessaggio = ace.getElementoMessaggio();
					ListaStato listaStato =
						ace.getThreadElement().getListaStato();
					/*
					 * ricerca degli stati originali
					 */
					ElementoStato epStart =
						(ElementoStato) listaStato.getElementoById(
							elementoMessaggio.getElement_one().getId());

					ElementoStato epEnd =
						(ElementoStato) listaStato.getElementoById(
							elementoMessaggio.getElement_two().getId());

					ElementoMessaggio nuovoEc =
						new ElementoMessaggio(te,epStart, epEnd);
					nuovoEc.setValue(elementoMessaggio, true);

					
					
					
					listaMessaggio.addElement(nuovoEc);

					break;

				case UndoRedoTipo.REMOVE_MESSAGGIO :
					/*
					 *aggiungo di nuovo il Messaggio 
					 */
					//RemoveCanaleEvento
					RemoveThreadMessaggioEvento rce =
						(RemoveThreadMessaggioEvento) undoInfo.getObject();
					listaMessaggio = rce.getListaMessaggio();
					elementoMessaggio = rce.getElementoMessaggio();
					listaMessaggio.removeById(elementoMessaggio.getId());

					break;

				case UndoRedoTipo.UPDATE_MESSAGGIO :
					/*
					 * riporto il canale alla nuova versione
					 */
					UpdateThreadMessaggioEvento uce =
						(UpdateThreadMessaggioEvento) undoInfo.getObject();

					listaMessaggio = uce.getListaMessaggio();

					ElementoMessaggio ecNuovo = uce.getNuovoElementoMessaggio();
					//ec = uce.getNuovoElementoCanale();
					elementoMessaggio =
						(ElementoMessaggio) listaMessaggio.getElementById(
							ecNuovo.getId());
					if (elementoMessaggio != null) {
						elementoMessaggio.setValue(ecNuovo, false);
					}
					break;

				default :
					break;
			}
		}
	}


	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoAdded(eventi.thread.AddThreadStatoEvento)
	 */
	public void statoAdded(AddThreadStatoEvento event) {
		add(UndoRedoTipo.ADD_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoRemoved(eventi.thread.RemoveThreadStatoEvento)
	 */
	public void statoRemoved(RemoveThreadStatoEvento event) {
		add(UndoRedoTipo.REMOVE_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#statoUpdate(eventi.thread.UpdateThreadStatoEvento)
	 */
	public void statoUpdate(UpdateThreadStatoEvento event) {
		add(UndoRedoTipo.UPDATE_STATO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioAdded(eventi.thread.AddThreadMessaggioEvento)
	 */
	public void messaggioAdded(AddThreadMessaggioEvento event) {
		add(UndoRedoTipo.ADD_MESSAGGIO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioRemoved(eventi.thread.RemoveThreadMessaggioEvento)
	 */
	public void messaggioRemoved(RemoveThreadMessaggioEvento event) {
		add(UndoRedoTipo.REMOVE_MESSAGGIO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#messaggioUpdate(eventi.thread.UpdateThreadMessaggioEvento)
	 */
	public void messaggioUpdate(UpdateThreadMessaggioEvento event) {
		add(UndoRedoTipo.UPDATE_MESSAGGIO, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.IThreadElementListener#threadRefresh()
	 */
	public void threadRefresh() {
	}

}
