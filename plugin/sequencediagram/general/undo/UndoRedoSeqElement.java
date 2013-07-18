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
    

package plugin.sequencediagram.general.undo;

import java.awt.Point;

import plugin.sequencediagram.data.ElementoClasse;
import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaClasse;
import plugin.sequencediagram.data.ListaConstraint;
import plugin.sequencediagram.data.ListaLoop;
import plugin.sequencediagram.data.ListaParallel;
import plugin.sequencediagram.data.ListaSeqLink;
import plugin.sequencediagram.data.ListaSim;
import plugin.sequencediagram.data.ListaTime;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.eventi.sequence.AddSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.AddSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.RemoveSequenceTimeEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceClasseEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceConstraintEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLinkEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceLoopEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceParallelEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceSimEvento;
import plugin.sequencediagram.eventi.sequence.UpdateSequenceTimeEvento;
import plugin.sequencediagram.pluglistener.ISequenceElementListener;
import plugin.statediagram.data.ListaThread;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;
import core.resources.general.undo.UndoRedoInfo;
import core.resources.general.undo.UndoRedoManager;
import core.resources.general.undo.VectorUndoRedoInfo;

/**
 * @author Flamel
 * La classe implementa i metodi per fare l'Undo e il Redo del sistema,
 * la classe ? necessaria poiche si ? implementato il sistema di messaggistica
 * E' un listener globale (mettendosi in ascolto di tutte le modifiche effettuate ai dati)
 * Fa l'undo e redo dei sequenceElement
 * si pu? utilizzare nel SequenceEditor
 */

/* non utilizzata poiche si ? preferito elaborare come il vecchio metodo 
 * di undo redo(ogni thread Editor con una propria classe di undo/redo)
 */

public class UndoRedoSeqElement
	extends UndoRedoManager
	implements ISequenceElementListener {

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

		eseguiUndoClasse(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoLink(vectorUndoInfo, scaleX, scaleY);
		eseguiUndoTime(vectorUndoInfo, scaleX, scaleY);
                eseguiUndoParallel(vectorUndoInfo, scaleX, scaleY);
                eseguiUndoConstraint(vectorUndoInfo, scaleX, scaleY);
                eseguiUndoSim(vectorUndoInfo, scaleX, scaleY);
                eseguiUndoLoop(vectorUndoInfo, scaleX, scaleY);

	}

	/**
	 * esegue l'undo di uno stato (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoClasse(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaClasse listaClasse;
		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_CLASSE :
					//lo debbo rimuovere
					AddSequenceClasseEvento aep =
						(AddSequenceClasseEvento) undoInfo.getObject();
					te = aep.getSorgente();
					listaClasse = aep.getListaClasse();
					listaClasse.removeElementById(
						aep.getElementoClasse().getId());
					break;

				case UndoRedoTipo.REMOVE_CLASSE :
					//aggiungo lo stato
					RemoveSequenceClasseEvento rep =
						(RemoveSequenceClasseEvento) undoInfo.getObject();
					listaClasse = rep.getListaClasse();
					listaClasse.addElement(
						rep.getElementoClasse().cloneClasse());
					break;

				case UndoRedoTipo.UPDATE_CLASSE :
					/**
					 * nuovo e vecchio sono cloni dell'elemento originale
					 */
					UpdateSequenceClasseEvento uep =
						(UpdateSequenceClasseEvento) undoInfo.getObject();
					listaClasse = uep.getListaClasse();
					te = uep.getSorgente();
					ElementoClasse nuovo = uep.getNuovoElementoClasse();
					ElementoClasse vecchio = uep.getVecchioElementoClasse();
					ElementoClasse vero =
						(ElementoClasse) listaClasse.getElementoById(
							vecchio.getId());

					if (vero != null) {
						vero.setValue(vecchio, true);
						te.getListaSeqLink().updateListaCanalePosizione(vero);
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
	 * esegue l'undo di un Link , presuppone che sia stato
	 * eseguito UndoClasse contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiUndoLink(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaSeqLink listaSeqLink;
		UndoRedoInfo undoInfo;
		ElementoSeqLink elementoSeqLink;

		/**
		 * sistemazione messaggi
		 */

		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_LINK :
					/*
					 * rimuovo il Messaggio
					 */
					AddSequenceLinkEvento ace =
						(AddSequenceLinkEvento) undoInfo.getObject();
					listaSeqLink = ace.getListaSeqLink();

					elementoSeqLink = ace.getElementoSeqLink();
					listaSeqLink.removeById(elementoSeqLink.getId());

					break;

				case UndoRedoTipo.REMOVE_LINK :
					/*
					 *aggiungo di nuovo il Messaggio 
					 */
					//RemoveCanaleEvento
					RemoveSequenceLinkEvento rce =
						(RemoveSequenceLinkEvento) undoInfo.getObject();

					listaSeqLink = rce.getListaSeqLink();
					elementoSeqLink = rce.getElementoSeqLink();
					ListaClasse listaClasse =
						rce.getSorgente().getListaClasse();

					/*
					 * ricerca delle classi originali
					 */
					ElementoClasse epStart =
						(ElementoClasse) listaClasse.getElementoById(
							elementoSeqLink.getElement_one().getId());

					ElementoClasse epEnd =
						(ElementoClasse) listaClasse.getElementoById(
							elementoSeqLink.getElement_two().getId());

					/*
					 * ricerca dei time originali
					 */

					ListaTime listaTime = rce.getSorgente().getListaTime();
					ElementoTime timeStart =
						(ElementoTime) listaTime.getElement(
							elementoSeqLink.getTime_one().getTime());

					ElementoTime timeStop =
						(ElementoTime) listaTime.getElement(
							elementoSeqLink.getTime_two().getTime());

					ElementoSeqLink nuovoSl =
						new ElementoSeqLink(
							"",
							epStart,
							epEnd,
							timeStart,
							timeStop,
							elementoSeqLink.getName(),
							null);

					listaSeqLink.addElement(nuovoSl);

					break;

				case UndoRedoTipo.UPDATE_LINK :
					/*
					 * riporto il canale alla precedente versione
					 */
					UpdateSequenceLinkEvento uce =
						(UpdateSequenceLinkEvento) undoInfo.getObject();

					listaSeqLink = uce.getListaSeqLink();

					ElementoSeqLink ecOld = uce.getVecchioElementoSeqLink();
					elementoSeqLink =
						(ElementoSeqLink) listaSeqLink.getElementById(
							ecOld.getId());
					if (elementoSeqLink != null) {
						elementoSeqLink.setValue(ecOld, false);
					}
					break;

				default :
					break;
			}
		}
	}

	/**
	 * esegue l'undo di unTime , presuppone che sia stato
	 * eseguito UndoClasse/UndoSeq contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiUndoTime(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaTime listaTime;
		UndoRedoInfo undoInfo;
		ElementoTime elementoTime;

		/**
		 * sistemazione messaggi
		 */

		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_TIME :
					/*
					 * rimuovo il Tempo
					 */
					AddSequenceTimeEvento ace =
						(AddSequenceTimeEvento) undoInfo.getObject();
					listaTime = ace.getListaTime();

					elementoTime = ace.getElementoTime();
					listaTime.removeElement(elementoTime.getTime());
					break;

				case UndoRedoTipo.REMOVE_TIME :
					/*
					 *aggiungo di nuovo il Messaggio 
					 */
					//RemoveCanaleEvento
					RemoveSequenceTimeEvento rce =
						(RemoveSequenceTimeEvento) undoInfo.getObject();

					listaTime = rce.getListaTime();
					elementoTime = rce.getElementoTime();
					listaTime.addLast(elementoTime.cloneTime());

					break;

				case UndoRedoTipo.UPDATE_LINK :
					break;

				default :
					break;
			}
		}
	}

	/**
	 * esegue l'Undo  di una Constraint (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoConstraint(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_CONSTRAINT:
					AddSequenceConstraintEvento event =
					                         (AddSequenceConstraintEvento) undoInfo.getObject();
					
					ListaConstraint listaConstraint = event.getListaConstraint(); 
					listaConstraint.removeElement(event.getElementoConstraint());
					
					
					break;

				case UndoRedoTipo.REMOVE_CONSTRAINT :
					break;

				case UndoRedoTipo.UPDATE_CONSTRAINT :
					break;

				default :
					break;
			}
		}

	}
        
        /**
	 * esegue l'Undo  di un operatore parallelo (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoParallel(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_PARALLEL:
					AddSequenceParallelEvento event =
					                         (AddSequenceParallelEvento) undoInfo.getObject();
					
					ListaParallel listaParallel = event.getListaParallel(); 
					listaParallel.removeElement(event.getElementoParallel());
					
					
					break;

				case UndoRedoTipo.REMOVE_PARALLEL :
					break;

				case UndoRedoTipo.UPDATE_PARALLEL :
					break;

				default :
					break;
			}
		}

	}
        
        /**
	 * esegue l'Undo  di un operatore parallelo (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoSim(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_SIM:
					AddSequenceSimEvento event =
					                         (AddSequenceSimEvento) undoInfo.getObject();
					
					ListaSim listaSim = event.getListaSim(); 
					listaSim.removeElement(event.getElementoSim());
					
					
					break;

				case UndoRedoTipo.REMOVE_SIM :
					break;

				case UndoRedoTipo.UPDATE_SIM :
					break;

				default :
					break;
			}
		}

	}
        
        /**
	 * esegue l'Undo  di un operatore parallelo (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiUndoLoop(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_LOOP:
					AddSequenceLoopEvento event =
					                         (AddSequenceLoopEvento) undoInfo.getObject();
					
					ListaLoop listaLoop = event.getListaLoop(); 
					listaLoop.removeElement(event.getElementoLoop());
					
					
					break;

				case UndoRedoTipo.REMOVE_LOOP :
					break;

				case UndoRedoTipo.UPDATE_LOOP :
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

		eseguiRedoClasse(vectorUndoInfo, scaleX, scaleY);
		eseguiRedoLink(vectorUndoInfo, scaleX, scaleY);
		eseguiRedoTime(vectorUndoInfo, scaleX, scaleY);
		eseguiRedoConstraint(vectorUndoInfo, scaleX, scaleY);
                eseguiRedoParallel(vectorUndoInfo, scaleX, scaleY);
                eseguiRedoSim(vectorUndoInfo, scaleX, scaleY);
                eseguiRedoLoop(vectorUndoInfo, scaleX, scaleY);

	}

	/**
	 * esegue il Redo  di una Calsse (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoClasse(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaClasse listaClasse;
		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_CLASSE :
					//lo debbo rimuovere
					AddSequenceClasseEvento aep =
						(AddSequenceClasseEvento) undoInfo.getObject();
					te = aep.getSorgente();
					listaClasse = aep.getListaClasse();

					listaClasse.addElement(
						aep.getElementoClasse().cloneClasse());
					break;

				case UndoRedoTipo.REMOVE_CLASSE :
					//aggiungo lo stato
					RemoveSequenceClasseEvento rep =
						(RemoveSequenceClasseEvento) undoInfo.getObject();
					listaClasse = rep.getListaClasse();
					listaClasse.removeElementById(
						rep.getElementoClasse().getId());

					break;

				case UndoRedoTipo.UPDATE_CLASSE :
					/**
					 * nuovo e vecchio sono cloni dell'elemento originale
					 */
					UpdateSequenceClasseEvento uep =
						(UpdateSequenceClasseEvento) undoInfo.getObject();
					listaClasse = uep.getListaClasse();
					te = uep.getSorgente();
					ElementoClasse nuovo = uep.getNuovoElementoClasse();
					ElementoClasse vecchio = uep.getVecchioElementoClasse();
					ElementoClasse vero =
						(ElementoClasse) listaClasse.getElementoById(
							nuovo.getId());

					if (vero != null) {
						vero.setValue(nuovo, true);
						te.getListaSeqLink().updateListaCanalePosizione(vero);
					}
					break;

				default :
					break;
			}
		}

	}

	/**
	 * esegue la Redo di un Link , presuppone che sia stato
	 * eseguito UndoClasse contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiRedoLink(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaSeqLink listaSeqLink;
		UndoRedoInfo undoInfo;
		ElementoSeqLink elementoSeqLink;

		/**
		 * sistemazione messaggi
		 */

		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_LINK :
					/*
					 * rimuovo il Messaggio
					 */
					AddSequenceLinkEvento ace =
						(AddSequenceLinkEvento) undoInfo.getObject();
					listaSeqLink = ace.getListaSeqLink();

					elementoSeqLink = ace.getElementoSeqLink();

					ListaClasse listaClasse =
						ace.getSorgente().getListaClasse();

					/*
					 * ricerca delle classi originali
					 */
					ElementoClasse epStart =
						(ElementoClasse) listaClasse.getElementoById(
							elementoSeqLink.getElement_one().getId());

					ElementoClasse epEnd =
						(ElementoClasse) listaClasse.getElementoById(
							elementoSeqLink.getElement_two().getId());

					/*
					 * ricerca dei time originali
					 */
					ListaTime listaTime = ace.getSorgente().getListaTime();
					ElementoTime timeStart =
						(ElementoTime) listaTime.getElement(
							elementoSeqLink.getTime_one().getTime());

					ElementoTime timeStop =
						(ElementoTime) listaTime.getElement(
							elementoSeqLink.getTime_two().getTime());

					ElementoSeqLink nuovoSl =
						new ElementoSeqLink(
							"",
							epStart,
							epEnd,
							timeStart,
							timeStop,
							elementoSeqLink.getName(),
							null);

					listaSeqLink.addElement(nuovoSl);
					break;

				case UndoRedoTipo.REMOVE_LINK :
					/*
					 *aggiungo di nuovo il Messaggio 
					 */
					//RemoveCanaleEvento
					RemoveSequenceLinkEvento rce =
						(RemoveSequenceLinkEvento) undoInfo.getObject();

					listaSeqLink = rce.getListaSeqLink();
					elementoSeqLink = rce.getElementoSeqLink();
					listaSeqLink.removeById(elementoSeqLink.getId());

					break;

				case UndoRedoTipo.UPDATE_LINK :
					/*
					 * riporto il canale alla precedente versione
					 */
					UpdateSequenceLinkEvento uce =
						(UpdateSequenceLinkEvento) undoInfo.getObject();

					listaSeqLink = uce.getListaSeqLink();

					ElementoSeqLink ecNuovo = uce.getNuovoElementoSeqLink();
					elementoSeqLink =
						(ElementoSeqLink) listaSeqLink.getElementById(
							ecNuovo.getId());
					if (elementoSeqLink != null) {
						elementoSeqLink.setValue(ecNuovo, false);
					}
					break;

				default :
					break;
			}
		}
	}

	
	
	
	
	
	
	/**
	 * esegue la redo di unTime , presuppone che sia stato
	 * eseguito UndoClasse/UndoSeq contenuti nella lista
	 * @param vectorUndoInfo lista degli undo
	 * @param scaleX scalaX 
	 * @param scaleY scalaY
	 */
	private void eseguiRedoTime(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY) {

		ListaTime listaTime;
		UndoRedoInfo undoInfo;
		ElementoTime elementoTime;

		/**
		 * sistemazione messaggi
		 */

		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {

				case UndoRedoTipo.ADD_TIME :
					/*
					 * rimuovo il Tempo
					 */
					AddSequenceTimeEvento ace =
						(AddSequenceTimeEvento) undoInfo.getObject();
					listaTime = ace.getListaTime();

					elementoTime = ace.getElementoTime();
			    	listaTime.addLast(elementoTime.cloneTime());
					break;

				case UndoRedoTipo.REMOVE_TIME :
					/*
					 *aggiungo di nuovo il Messaggio 
					 */
					RemoveSequenceTimeEvento rce =
						(RemoveSequenceTimeEvento) undoInfo.getObject();

					listaTime = rce.getListaTime();
					elementoTime = rce.getElementoTime();
					
				    listaTime.removeElement(elementoTime.getTime());
					break;

				case UndoRedoTipo.UPDATE_LINK :
					break;

				default :
					break;
			}
		}
	}

	
	/**
	 * esegue il Redo  di una Constraint (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoConstraint(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		ListaClasse listaClasse;
		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_CONSTRAINT :
					break;

				case UndoRedoTipo.REMOVE_CONSTRAINT :
					break;

				case UndoRedoTipo.UPDATE_CONSTRAINT :
					break;

				default :
					break;
			}
		}

	}
        
        /**
	 * esegue il Redo  di un Parallel (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoParallel(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		ListaClasse listaClasse;
		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_PARALLEL :
					break;

				case UndoRedoTipo.REMOVE_PARALLEL :
					break;

				case UndoRedoTipo.UPDATE_PARALLEL :
					break;

				default :
					break;
			}
		}

	}
        
        /**
	 * esegue il Redo  di un Sim (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoSim(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		ListaClasse listaClasse;
		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_SIM :
					break;

				case UndoRedoTipo.REMOVE_SIM :
					break;

				case UndoRedoTipo.UPDATE_SIM :
					break;

				default :
					break;
			}
		}

	}
        
        /**
	 * esegue il Redo  di un Sim (gestisce la rimozione, aggiunta etc)
	 * @param vectorUndoInfo
	 * @param scaleX
	 * @param scaleY
	 */
	private void eseguiRedoLoop(
			VectorUndoRedoInfo vectorUndoInfo,
			double scaleX,
			double scaleY) {

		ListaClasse listaClasse;
		UndoRedoInfo undoInfo;
		SequenceElement te;
		ListaThread lt;

		/**
		 * sistemazione processi
		 */
		for (int i = 0; i < vectorUndoInfo.size(); i++) {
			undoInfo = (UndoRedoInfo) vectorUndoInfo.get(i);
			switch (undoInfo.getTipo()) {
				case UndoRedoTipo.ADD_LOOP :
					break;

				case UndoRedoTipo.REMOVE_LOOP :
					break;

				case UndoRedoTipo.UPDATE_LOOP :
					break;

				default :
					break;
			}
		}

	}
	
	
	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getEditMenu()
	 */
	public EditGraphInterface getEditMenu() {
		return null;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#getZoomAction()
	 */
	public ZoomGraphInterface getZoomAction() {
		return null;
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#classeAdded(eventi.sequence.AddSequenceClasseEvento)
	 */
	public void classeAdded(AddSequenceClasseEvento event) {
		add(UndoRedoTipo.ADD_CLASSE, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#classeRemoved(eventi.sequence.RemoveSequenceClasseEvento)
	 */
	public void classeRemoved(RemoveSequenceClasseEvento event) {
		add(UndoRedoTipo.REMOVE_CLASSE, event, event.getIdSessione());

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#classeUpdate(eventi.sequence.UpdateSequenceClasseEvento)
	 */
	public void classeUpdate(UpdateSequenceClasseEvento event) {
		add(UndoRedoTipo.UPDATE_CLASSE, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#seqLinkAdded(eventi.sequence.AddSequenceLinkEvento)
	 */
	public void seqLinkAdded(AddSequenceLinkEvento event) {
		add(UndoRedoTipo.ADD_LINK, event, event.getIdSessione());

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#seqLinkRemoved(eventi.sequence.RemoveSequenceLinkEvento)
	 */
	public void seqLinkRemoved(RemoveSequenceLinkEvento event) {
		add(UndoRedoTipo.REMOVE_LINK, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#seqLinkUpdate(eventi.sequence.UpdateSequenceLinkEvento)
	 */
	public void seqLinkUpdate(UpdateSequenceLinkEvento event) {
		add(UndoRedoTipo.UPDATE_LINK, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#timeAdded(eventi.sequence.AddSequenceTimeEvento)
	 */
	public void timeAdded(AddSequenceTimeEvento event) {
		add(UndoRedoTipo.ADD_TIME, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#timeRemoved(eventi.sequence.RemoveSequenceTimeEvento)
	 */
	public void timeRemoved(RemoveSequenceTimeEvento event) {
		add(UndoRedoTipo.REMOVE_TIME, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#timeUpdate(eventi.sequence.UpdateSequenceTimeEvento)
	 */
	public void timeUpdate(UpdateSequenceTimeEvento event) {
		add(UndoRedoTipo.UPDATE_TIME, event, event.getIdSessione());
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#sequenceRefresh()
	 */
	public void sequenceRefresh() {

	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#constraintAdded(eventi.sequence.AddSequenceConstraintEvento)
	 */
	public void constraintAdded(AddSequenceConstraintEvento event) {
		  add(UndoRedoTipo.ADD_CONSTRAINT, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#constraintRemoved(eventi.sequence.RemoveSequenceConstraintEvento)
	 */
	public void constraintRemoved(RemoveSequenceConstraintEvento event) {
		add(UndoRedoTipo.REMOVE_CONSTRAINT, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#constraintUpdate(eventi.sequence.UpdateSequenceConstraintEvento)
	 */
	public void constraintUpdate(UpdateSequenceConstraintEvento event) {
		add(UndoRedoTipo.UPDATE_CONSTRAINT, event, event.getIdSessione());
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#parallelAdded(eventi.sequence.AddSequenceParallelEvento)
	 */
	public void parallelAdded(AddSequenceParallelEvento event) {
		  add(UndoRedoTipo.ADD_PARALLEL, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#parallelRemoved(eventi.sequence.RemoveSequenceParallelEvento)
	 */
	public void parallelRemoved(RemoveSequenceParallelEvento event) {
		add(UndoRedoTipo.REMOVE_PARALLEL, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#parallelUpdate(eventi.sequence.UpdateSequenceParallelEvento)
	 */
	public void parallelUpdate(UpdateSequenceParallelEvento event) {
		add(UndoRedoTipo.UPDATE_PARALLEL, event, event.getIdSessione());
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#SimAdded(eventi.sequence.AddSequenceSimEvento)
	 */
	public void SimAdded(AddSequenceSimEvento event) {
		  add(UndoRedoTipo.ADD_SIM, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#SimRemoved(eventi.sequence.RemoveSequenceSimEvento)
	 */
	public void SimRemoved(RemoveSequenceSimEvento event) {
		add(UndoRedoTipo.REMOVE_SIM, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#SimUpdate(eventi.sequence.UpdateSequenceSimEvento)
	 */
	public void SimUpdate(UpdateSequenceSimEvento event) {
		add(UndoRedoTipo.UPDATE_SIM, event, event.getIdSessione());
	}
        
        /* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#LoopAdded(eventi.sequence.AddSequenceLoopEvento)
	 */
	public void LoopAdded(AddSequenceLoopEvento event) {
		  add(UndoRedoTipo.ADD_LOOP, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#LoopRemoved(eventi.sequence.RemoveSequenceLoopEvento)
	 */
	public void LoopRemoved(RemoveSequenceLoopEvento event) {
		add(UndoRedoTipo.REMOVE_LOOP, event, event.getIdSessione());
		
	}

	/* (non-Javadoc)
	 * @see plugin.pluglistener.ISequenceElementListener#LoopUpdate(eventi.sequence.UpdateSequenceLoopEvento)
	 */
	public void LoopUpdate(UpdateSequenceLoopEvento event) {
		add(UndoRedoTipo.UPDATE_LOOP, event, event.getIdSessione());
	}


}
