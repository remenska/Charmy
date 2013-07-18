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

import java.awt.Point;
import java.io.Serializable;

import plugin.topologychannels.resource.data.ElementoCanaleMessaggio;
import plugin.topologychannels.resource.data.ElementoProcessoStato;
import plugin.topologychannels.resource.data.ListaProcessoStato;
import plugin.topologychannels.resource.data.interfacce.IListaCanMessNotify;
import plugin.topologychannels.resource.graph.GraficoCollegamento;



/** Questa classe implementa il canale di collegamento
    tra due processi nell'editor architetturale. */

public class ElementoCanale
	extends ElementoCanaleMessaggio
	implements Serializable {

	/** Denota un canale standard. */
	public static final int CHANNEL = 0;

	/** Denota un canale esterno all'architettura che manda o riceve un flusso. */
	public static final int INPUT_OUTPUT = 1;

	/** Memorizza il tipo del canale. */
	private int modalita;



	/**
	 * Costruttore
	 * @param FromProcess processo iniziale
	 * @param ToProcess processo finale
	 */
	public ElementoCanale(
		ElementoProcessoStato FromProcess,
		ElementoProcessoStato ToProcess) {
		this(FromProcess, ToProcess, null);
	}

	/**
	 * Costruttore
	 * @param updateE listener per le modifiche
	 */
	public ElementoCanale(IListaCanMessNotify updateE) {
		super(updateE);
	}

	/**
	 * Costruttore
	 * @param FromProcess
	 * @param ToProcess
	 * @param updateE listener modifiche
	 */
	public ElementoCanale(
		ElementoProcessoStato FromProcess,
		ElementoProcessoStato ToProcess,
		IListaCanMessNotify updateE) {

		this(FromProcess, ToProcess, false, 0, updateE);

	}

	/**
	 * Costruttore per clonazione processi,
	 * si usa esclusivamente per clonare esattamente l'elemento compreso di id
	 * @param boolean forClone, true se debbo clonare esattamente il processo (compreso idElemento), 
	 *               false per costruttore normale, con un nuovo identificatore di elemento
	 * @param idClone da associare al processo nel caso di forClose = true, altrimenti viene ignorato
	 * 
	 */

	public ElementoCanale(
		ElementoProcessoStato FromProcess,
		ElementoProcessoStato ToProcess,
		boolean forClone,
		long idCanale,
		IListaCanMessNotify updateE) {
		//super(updateE);
		super(forClone, idCanale, updateE);

		setName( "Channel_" + numIstanze);
		setTipo(0);
		
		
		// La variabile 'process_one' conterr� un riferimento
		// al processo con 'idProcesso' pi� piccolo.
		if (FromProcess.getId() < ToProcess.getId()) {
			element_one = FromProcess;
			element_two = ToProcess;
			flussodiretto = true;
		} else {
			element_one = ToProcess;
			element_two = FromProcess;
			flussodiretto = false;
		}
		grafico = null;
		modalita = CHANNEL;		
	}

	/** Crea l'oggetto grafico associato al canale. 
	 * old creaGraficoCanale
	 * */
	public void setPosizione(int graphicsPos) {
		Point p1 = element_one.getPointMiddle();
		Point p2 = element_two.getPointMiddle();
		posizioneCanMess = graphicsPos;
		
		grafico =
			new GraficoCollegamento(
				element_one,
				element_two,
				graphicsPos,
				flussodiretto,
				this);

	}

	/** Aggiorna la posizione del canale in funzione di
		quella dell'elemento di arrivo e di partenza. */
	public void updateCanalePosizione() {		
		((GraficoCollegamento) grafico).updateCollegamentoPosizione(
			getPosizione(),
			flussodiretto);
	}

	/** Imposta la modalit� del canale. */
	public synchronized boolean setModality(int m) {
		boolean bo = getStato();
		boolean uscita;
		if (bo) {
			informPreUpdate();
			disable();
		}

		switch (m) {
			case CHANNEL :
				modalita = CHANNEL;
				uscita = true;
			case INPUT_OUTPUT :
				modalita = INPUT_OUTPUT;
				uscita = true;
			default :
				uscita = false;
		}

		if (bo) {
			enabled();
			informPostUpdate();
		}
		return uscita;
	}

	/** Restituisce la modalit� del canale. */
	public int getModality() {
		return modalita;
	}





	/** Clonazione dell'oggetto. */
	public ElementoCanale cloneCanale(ListaProcessoStato lprc) {
		ElementoProcessoStato ProcessoDa = getElement_one();
		ElementoProcessoStato ProcessoA = getElement_two();
		String NomeProcessoDa = ProcessoDa.getName();
		String NomeProcessoA = ProcessoA.getName();
		ElementoCanale cloned;

		ProcessoDa = lprc.getElement(NomeProcessoDa);
		ProcessoA = lprc.getElement(NomeProcessoA);
		cloned =
			new ElementoCanale(ProcessoDa, ProcessoA, true, getId(), null);
		cloned.setModality(modalita);
		cloned.setFlussoDiretto(flussodiretto);
		cloned.setPosizione(getTipo());
		cloned.setName(grafico.getText());
		(cloned.grafico).updateLineaProprieta(
			grafico.getLineColor(),
			grafico.getLineWeight(),
			grafico.getLineTheme());
		(cloned.grafico).updateTestoProprieta(
			grafico.getTextColor(),
			grafico.getTextFont(),
			grafico.getFontStyle(),
			grafico.getFontSize());

		return cloned;
	}

	/** 
	 * Nuova clonazione 
	 * Clonazione dell'oggetto. */
	public ElementoCanale cloneCanale() {

		ElementoProcesso ProcessoDa = (ElementoProcesso) getElement_one();
		ElementoProcesso ProcessoA = (ElementoProcesso) getElement_two();
		String NomeProcessoDa = ProcessoDa.getName();
		String NomeProcessoA = ProcessoA.getName();
		ElementoCanale cloned;
		cloned =
			new ElementoCanale(
				ProcessoDa.cloneProcesso(),
				ProcessoA.cloneProcesso(),
				true,
				getId(),
				null);
		cloned.setModality(modalita);
		cloned.setFlussoDiretto(flussodiretto);
		cloned.setPosizione(getTipo());
		cloned.setName(grafico.getText());
		(cloned.grafico).updateLineaProprieta(
			grafico.getLineColor(),
			grafico.getLineWeight(),
			grafico.getLineTheme());
		(cloned.grafico).updateTestoProprieta(
			grafico.getTextColor(),
			grafico.getTextFont(),
			grafico.getFontStyle(),
			grafico.getFontSize());

		return cloned;
	}

	/**
	 * copia i dati identificante il elementoCanale, 
	 * tranne l'identificatore  creandone un simil-clone
	 * @author Michele Stoduto
	 * @param elementoCanale da cui prelevare i dati
	 * @param copiaId, true copia completamente il canale, false l'id non viene copiato
	 * 
	 */
	public void setValue(ElementoCanale elementoCanale, boolean copiaId) {
		boolean bo = sendMsg;
		
		if(bo){
			informPreUpdate();
			disable();
		}
		
		if(copiaId){
			setId(elementoCanale.getId());
		}
		setModality(elementoCanale.getModality());
		
		setPosizione(elementoCanale.getPosizione());
		setName(elementoCanale.getGrafico().getText());
		grafico.updateLineaProprieta(
			elementoCanale.getGrafico().getLineColor(),
			elementoCanale.getGrafico().getLineWeight(),
			elementoCanale.getGrafico().getLineTheme());
		grafico.updateTestoProprieta(
		/*	elementoCanale.getGrafico().getText(), */
			elementoCanale.getGrafico().getTextColor(),
			elementoCanale.getGrafico().getTextFont(),
			elementoCanale.getGrafico().getFontStyle(),
			elementoCanale.getGrafico().getFontSize());
		setFlussoDiretto(elementoCanale.getFlussoDiretto());
		this.updateCanalePosizione();
		if(bo){
			enabled();
			informPostUpdate();
		}
	}
	

	/* (non-Javadoc)
	 * @see data.IUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPreUpdate(this.cloneCanale());
			}
		}

	}

	/* (non-Javadoc)
	 * @see data.IUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPostUpdate(this.cloneCanale());
			}
		}

	}

}