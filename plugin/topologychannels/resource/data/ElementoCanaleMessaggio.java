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
    

package plugin.topologychannels.resource.data;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.topologychannels.resource.data.interfacce.IListaCanMessNotify;
import plugin.topologychannels.resource.graph.GraficoLink;



/** Superclasse astratta di ElementoCanale e di ElementoMessaggio_
	La prima implementa il canale di collegamento tra due processi 
	nell'editor architetturale, la seconda il link per lo scambio 
    di messaggi tra due stati di uno State Diagram. */

public abstract class ElementoCanaleMessaggio extends  ElementoBase
	implements Serializable {

	/** Variabile di classe che memorizza il numero di istanze di classe
    che sono state create. */
protected static long numIstanze = 0;

/**
 * ritorna un nuovo numero di istanza
 * @return nuovo numero di istanza
 */
public static long newNumIstanze(){
	numIstanze++;
	return numIstanze;
}


public static long getNumIstanze()
{
	return numIstanze;
}


public static void setNumIstanze(long n)
{
	numIstanze = n;
}

	
	/** Numero dei canali creati. */
	protected static long numCanale;

	
	public static long getNumCanale() {
		return numCanale;
	}

	public static void incNumCanale() {
		numCanale = numCanale + 1;
	}


	protected boolean sendMsg = true;

	/**
	 * gestore eventi update
	 */
	protected IListaCanMessNotify updateEp;

	/** Memorizza il primo elemento del link. */
	protected ElementoProcessoStato element_one;

	/** Memorizza il secondo elemento del link. */
	protected ElementoProcessoStato element_two;

	/** Assume valore 'true' se il flusso � diretto da 'element_one'
		verso 'element_two', 'false' altrimenti. */
	protected boolean flussodiretto;

	/** Memorizza l'oggetto che gestisce la rappresentazione grafica del link. */
	protected GraficoLink grafico;

	/**  
	 * Definisce la posizione da utilizzare per disegnare 
	 * il grafico
	 * vecchia "graficotipo" che non aveva significato!!! 
	 * */
	protected int posizioneCanMess;


	/** Costruttore. */
		public ElementoCanaleMessaggio(IListaCanMessNotify updateE) {
			this(false, 0, updateE);
		}
	

	/**
	 * Costruttore per clonazione processi,
	 * si usa esclusivamente per clonare esattamente l'elemento compreso di id
	 * @param boolean forClone, true se debbo clonare esattamente il processo (compreso idElemento), 
	 *               false per costruttore normale, con un nuovo identificatore di elemento
	 * @param idElemento da associare al processo nel caso di forClose = true, altrimenti viene ignorato
	 * 
	 */
		public ElementoCanaleMessaggio(boolean forClone, long idElemento, IListaCanMessNotify updateE )
		{
			if(forClone){
				setId(idElemento);
				updateEp = null;
			}
			else{
				setId(newNumIstanze());
				updateEp = updateE;
			}
		}
	



	/** Restituisce l'elemento da cui parte il link. */
	public ElementoProcessoStato getElementFrom() {
		if (flussodiretto) {
			return element_one;
		} 
		else {
			return element_two;
		}
	}

	/** Restituisce l'elemento a cui arriva il link. */
	public ElementoProcessoStato getElementTo() {
		if (flussodiretto) {
			return element_two;
		} 
		else {
			return element_one;
		}
	}

	/** Restituisce il primo elemento del link. */
	public ElementoProcessoStato getElement_one() {
		return element_one;
	}

	/** Restituisce il secondo elemento del link. */
	public ElementoProcessoStato getElement_two() {
		return element_two;
	}

	/** Restituisce il numero di posizione associata al link 
	 * 
	 * */
	public int getPosizione() {
		return posizioneCanMess;
	}

	/** Analogo al metodo isIn() di 'ElementoProcessoStato'_ 
		Serve per verificare se il punto 'p' � sufficientemente 
	    vicino al link (vicino dal punto di vista grafico). */
	public boolean isSelezionato(Point p) {
		return grafico.isSelezionato(p);
	}

	/** Disegna il grafico associato al link. */
	public void paintCanale(Graphics2D g2D) {
		if (grafico != null)
			grafico.paintGraficoLink(g2D);
	}

	/** Crea l'oggetto grafico associato al link.
	 * crea il grafico del canale
	 * old creaGrafico Canale
	 *  */
	public abstract void setPosizione(int graphicsPos);

	/** Aggiorna la posizione del link in funzione di quella
		dell'elemento di arrivo e di partenza. */
	public abstract void updateCanalePosizione();

	/** Restituisce un riferimento al grafico del link. */
	public GraficoLink getGrafico() {
		return grafico;
	}

	/** Restituisce 'true' se il flusso di dati � da 'element_one' a 'element_two',
	    'false' in caso contrario. */
	public boolean getFlussoDiretto() {
		return flussodiretto;
	}

	/** */
	public void setFlussoDiretto(boolean ctrlFlusso) {
		informPreUpdate();
		flussodiretto = ctrlFlusso;
	    informPostUpdate();
	}

	/** Inverte la direzione del flusso di dati nel link. */
	public void invFlussoDiretto() {
		flussodiretto = !flussodiretto;
	}

	/** Seleziona/deseleziona il link. */
	public void setSelected(boolean ctrlSelection) {
		grafico.setSelected(ctrlSelection);
	}

	/** Verifica se il link � selezionato. */
	public boolean isSelected() {
		return grafico.isSelected();
	}

	/** Inverte lo stato selezionato/deselezionato del link. */
	public void invSelected() {
		grafico.invSelected();
	}

	/** Aggiorna le propriet� grafiche del link. */
	public synchronized void adjustGraph(GraficoLink graph) {
		boolean bo = sendMsg;
		if(bo){
		    informPreUpdate();
		    disable();
		}
		
		grafico.updateLineaProprieta(
			graph.getLineColor(),
			graph.getLineWeight(),
			graph.getLineTheme());
		grafico.updateTestoProprieta(
			graph.getTextColor(),
			graph.getTextFont(),
			graph.getFontStyle(),
			graph.getFontSize());
		if(bo){
	    	enabled();
	    	informPostUpdate();
		}
			
	}

	/** Verifica se il link � interno al rettangolo passato come parametro_ 
		Questo metodo, probabilmente, pu� essere eliminato!! */
	public boolean isInRect(Rectangle2D rettesterno) {
		return grafico.isInRect(rettesterno);
	}

	/**
	 * ritorna la classe di ascolto update
	 * @return
	 */
	public IListaCanMessNotify getUpdateEp() {
		return updateEp;
	}

	/**
	 * setta la classe di ascolto update
	 * @param updateEP
	 */
	public void setUpdateEp(IListaCanMessNotify updateEP) {
		updateEp = updateEP;
	}
}