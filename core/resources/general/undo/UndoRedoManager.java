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
    

package core.resources.general.undo;


import java.awt.Point;

import core.internal.plugin.editoralgo.ImpMainTabPanel;

/**
 * @author Michele Stoduto
 * La classe implementa i metodi per fare l'Undo e il Redo del sistema,
 * la classe è necessaria poiche si è implementato il sistema di messaggistica
 * E' un listener globale (mettendosi in ascolto di tutte le modifiche effettuate ai dati)
 * derivato dalla precedente versione di ArrayManager
 * 
 */
public abstract class UndoRedoManager extends ImpMainTabPanel {

	/** Costante che determina il massimo livello di
		profondità per le operazioni di undo e redo. */
	public static final int maxlivello = 20;

	private VectorUndoRedoInfo[] vectorURInfo;

	/**
	 * rappresenta l'ultimo id di sessione memorizzaro
	 */
	//private long idSessione = -1;

	/** Memorizza il numero di operazioni di Undo che
		si possono applicare allo stato corrente. */
	private int opundo;

	/** Memorizza il numero di operazioni di Redo che
		si possono applicare allo stato corrente. */
	private int opredo;

	/** Memorizza l'indice dell'elemento dell'array
		che rappresenta lo stato attuale. */
	private int indice;

	private boolean registra = true;

	public UndoRedoManager() {
		vectorURInfo = new VectorUndoRedoInfo[maxlivello + 1];

		for (int i = 0; i < maxlivello + 1; i++) {

			//undoRedoInfo[i] = new UndoRedoInfo(0, null);
			vectorURInfo[i] = new VectorUndoRedoInfo();
		}

		opundo = 0;
		opredo = 0;
		indice = 0;
	}

	/** (non-Javadoc)
	 * @see general.ImpMainTabPanel#init()
	 * se la funzione viene subclassata, ricordarsi di chiamarla
	 */
	//	protected void init() {

	//		plugData.setUndoRedoManager(this);
	/*
	 * registrazione listener
	 */
	//		plugData.getListaProcesso().addListener(this);
	//		plugData.getListaCanale().addListener(this);
	//		plugData.getListaDP().addListener(this);

	//	}

	/** Aggiunge un nuovo elemento "StateMemory" al vettore_ 
		Operazione di Add. */
	protected void add(int tipo, Object oggetto, long idSessione) {
		//	Calcolo il nuovo indice.

		if (!registra) {
			return;
		}

		if (vectorURInfo[indice].getIdSessione() == idSessione) {
			//stessa sessione in corso
			vectorURInfo[indice].add(new UndoRedoInfo(tipo, oggetto));
			return;
		};

		if (indice == maxlivello) {
			indice = 0;
		} else {
			indice++;
		}

		//pulisco tutto
		vectorURInfo[indice].removeAllElements();
		vectorURInfo[indice].setIdSessione(idSessione);
		vectorURInfo[indice].add(new UndoRedoInfo(tipo, oggetto));
		//		Memorizzazione dei riferimenti alle liste clonate.
		//undoRedoInfo[indice].setTipo(tipo);
		//undoRedoInfo[indice].setObject(oggetto);

		if (opundo < maxlivello) {
			opundo++;
		}
		//	Commentare o meno la riga seguente dipende dalla
		//	politica di Undo/Redo che si vuole applicare.
		//	opredo = 0;
	}

	/** Restituisce un punto le cui coordinate sono quelle del punto in ingresso
		aggiornate tenendo conto dei fattori di scala per l'asse X e per l'asse Y. */
	private Point updateGetPoint(Point pnt, double scaleX, double scaleY) {
		Point pp = new Point((int) (pnt.x / scaleX), (int) (pnt.y / scaleY));
		return pp;
	}

	/**	Operazione di Undo.
	 * 	Richiesta di un undo
	 * 	@param scalaX per eventuali scale per disegno
	 * 	@param scalaY per eventuale disegno 
	 *  */
	public synchronized UndoRedoInfo undo(double scaleX, double scaleY) {
		if (opundo > 0) {
			opundo--;
			if (opredo < maxlivello) {
				opredo++;
			}

			registra = false;

			eseguiUndo(vectorURInfo[indice], scaleX, scaleY);
			registra = true;

			if (indice == 0) {
				indice = maxlivello;
			} else {
				indice--;
			}

			//	Per gestire correttamente le operazioni di Undo/Redo
			//	intercalate con quelle di Add, è necessario restituire
			//	una copia dell'oggetto.			
			//return undoRedoInfo[indice];

			//			registra = false;

			//			eseguiUndo(vectorURInfo[indice], scaleX, scaleY);
			//			registra = true;


			return null;

		} else {
			return null;
		}
	}

	/**
	 * esegue l'undo passato come parametro
	 * @param undoInfo undo da eseguire (operazione inversa di tipo)
	 */
	protected abstract void eseguiUndo(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY);

	/**
	 * esegue il redo passato come parametro
	 * @param undoInfo undo da eseguire (operazione inversa di tipo)
	 */
	protected abstract void eseguiRedo(
		VectorUndoRedoInfo vectorUndoInfo,
		double scaleX,
		double scaleY);

	/** Operazione di Redo. */
	public UndoRedoInfo redo(double scaleX, double scaleY) {
		int localIndex = 0;

		if (opredo > 0) {
			opredo--;

			if (opundo < maxlivello) {
				opundo++;
			}
			if (indice == maxlivello) {
				indice = 0;
			} else {
				indice++;
			}
			//	Per gestire correttamente le operazioni di Undo/Redo
			//	intercalate con quelle di Add, è necessario restituire
			//	una copia dell'oggetto.				

			registra = false;
			eseguiRedo(vectorURInfo[indice], scaleX, scaleY);
			//	eseguiRedo(undoRedoInfo[indice], scaleX, scaleY);
			registra = true;
		} else {
			return null;
		}
		return null;
	}

	/** Restituisce l'indice dello stato corrente. */
	public int getIndex() {
		return indice;
	}

	/* (non-Javadoc)
	 * @see simpleinterface.IMainTabPanel#stateActive()
	 */
	public void stateActive() {
	}


}
