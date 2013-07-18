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
    

package plugin.statediagram.graph;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;


//import plugin.topologydiagram.data.ElementoProcesso;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ThreadElement;
import plugin.sequencediagram.utility.LabelSequence;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaCanMessNotify;
import plugin.topologydiagram.resource.graph.GraficoCollegamento;
import plugin.topologydiagram.resource.graph.GraficoLoop;


   /** Questa classe implementa il link per lo scambio di
    *  messaggi tra due stati nello State Diagram. 
    *
    */

public class ElementoMessaggio
	extends ElementoCanaleMessaggio
	implements Serializable {

	/** Costante che rappresenta un messaggio sincrono. */
	public static final int SYNCHRONOUS = 1;
	/** Costante che rappresenta un messaggio asincrono. */
	public static final int ASYNCHRONOUS = 2;
	public static final int SEND = 3;
	public static final int RECEIVE = 4;
	public static final int TAU = 5;
	/** Memorizza il tipo di messaggio: Regular, Required, Fail */
	private int msgRRF;
	private boolean isLoop;
	private String operations;
	private String guard;
	private int sendReceive;
	private LinkedList parameters;
	private ThreadElement te;

	/**
	 * costruttore standard
	 * @param FromProcess
	 * @param ToProcess
	 */
	public ElementoMessaggio(
		ThreadElement te,
		ElementoProcessoStato FromProcess,
		ElementoProcessoStato ToProcess) {
		this(te,FromProcess, ToProcess,SYNCHRONOUS,"link_",null);
	}


	/**
	 * Castruttore
	 * @param FromProcess
	 * @param ToProcess
	 * @param msgType
	 * @param label
	 * @param updateE
	 */
	public ElementoMessaggio(
		ThreadElement te,
		ElementoProcessoStato FromProcess,
		ElementoProcessoStato ToProcess,
		int msgType,
		String label,
		IListaCanMessNotify updateE) {
			this(te,FromProcess, ToProcess, msgType, label,ElementoMessaggio.TAU, false, 0, updateE);
	}

	/**
	 * Costruttore per clonazione processi,
	 * si usa esclusivamente per clonare esattamente l'elemento compreso di id
	 * @param boolean forClone, true se debbo clonare esattamente il processo (compreso idElemento), 
	 *               false per costruttore normale, con un nuovo identificatore di elemento
	 * @param idClone da associare al processo nel caso di forClose = true, altrimenti viene ignorato
	 * 
	 */
	public ElementoMessaggio(
		ThreadElement te,
		ElementoProcessoStato FromProcess,
		ElementoProcessoStato ToProcess,
		int msgType,
		String label,
		int msgRRF,
		boolean forClone,
		long idClone,
		IListaCanMessNotify updateE) {
			super(forClone, idClone, updateE);
			this.te=te;
			setName(label);
			setTipo(msgType);
			sendReceive=SEND;
			parameters=new LinkedList();
			if (FromProcess.equals(ToProcess)) {
				isLoop = true;
				element_one = FromProcess;
				element_two = ToProcess;
				flussodiretto = true;
			} 
			else {
				isLoop = false;
			// La variabile 'process_one' conterr? un riferimento
			// al processo con 'idProcesso' pi? piccolo.
				if (FromProcess.getId() < ToProcess.getId()) {
					element_one = FromProcess;
					element_two = ToProcess;
					flussodiretto = true;
				} 
				else {
					element_one = ToProcess;
					element_two = FromProcess;
					flussodiretto = false;
				}
			}
		this.msgRRF=msgRRF;
		grafico = null;
	}


	public String getThreadName(){
		return te.getNomeThread();
	}
	
	/**
	 * setta la stringa relativa all'operatore
	 * @param op
	 */
	public void setOperations(String op){
		boolean bo = this.testAndSet();
		operations=op;
		this.testAndReset(bo);
	}

	/**
	 * Stringa Opeazione
	 * @return la Stringa rappresentante l'operazione, 
	 *                  se la stringa non ? stata definita ritorna
	 * 				una stringa di lunghezza 0
	 */
	public String getOperations(){
		if(operations!=null)
			return operations;
		else
			return "";
	}
    
    /**
     * setta al stringa di guardia
     * @param stringa rappresentante la guardia
     */
	public void setGuard(String g){
		boolean bo = this.testAndSet();
		guard=g;
		this.testAndReset(bo);
	}
	
	/**
	 * Stringa di guardia
	 * @return la Stringa rappresentante la guardia, 
	 *                  se la stringa non ? stata definita ritorna
	 * 				una stringa di lunghezza 0
	 */   
	public String getGuard(){
		if(guard!=null)
			return guard;
		else
			return "";
	}
    
    /**
     * setta la modalit? dell'elemento
     * @param sr
     */
	public void setSendReceive(int sr){
		boolean bo = this.testAndSet();
		sendReceive=sr;
		this.testAndReset(bo);
	}
    
	public int getSendReceive(){
		return sendReceive;
	}
    
    
	public void setParameters(LinkedList par){
		boolean bo = this.testAndSet();
		parameters=par;
		this.testAndReset(bo);
	}
    
	public LinkedList getParameters(){
		return parameters;
	}
    
    public void setPosizioneCanMess(int pos){
    	posizioneCanMess=pos;
    }
    
    public int getPosizioneCanMess(){
    	return posizioneCanMess;
    }
    	
	/**
	 * Crea L'oggetto grafico associato al canale
	 * @param Posizione dell'oggetto nel canale (per il disegno di archi non sovrapposti)
	 */
	
	public void setPosizione(int graphicsPos) {
		Point p1 = element_one.getPointMiddle();
		Point p2 = element_two.getPointMiddle();
		posizioneCanMess = graphicsPos;
		if (isLoop) {
			grafico =
				new GraficoLoop(
					element_one,
					graphicsPos,
					flussodiretto, this);
		} 
		else {
			grafico =
				new GraficoCollegamento(
					element_one,
					element_two,
					graphicsPos,
					flussodiretto,
					this);
		}
	}
	
	/**	
	 * ridefinizione della stringa esplicativa del messaggio
	 * @return Stringa del messaggio 
	 */
	public String getViewName(){
		return new LabelSequence().label(this);
	}
	

	/** Aggiorna la posizione del link in funzione di
		quella dell'elemento di arrivo e di partenza. */
	public void updateCanalePosizione() {
		Point p1 = element_one.getPointMiddle();
		Point p2 = element_two.getPointMiddle();
		if (isLoop) { 
			((GraficoLoop) grafico).updateLoopPosizione(
				this.getPosizione(),
				flussodiretto);
		} 
		else {
			((GraficoCollegamento) grafico).updateCollegamentoPosizione(
				this.getPosizione(),
				flussodiretto);
		}
	}

	/** Imposta il tipo del messaggio. */
	public boolean setTipo(int j) {
		switch (j) {
			case SYNCHRONOUS :
				return super.setTipo( SYNCHRONOUS);  
				//messaggiotipo = SYNCHRONOUS;
				
			case ASYNCHRONOUS :
				return super.setTipo( ASYNCHRONOUS);
				//messaggiotipo = ASYNCHRONOUS;
				
			default :
				return super.setTipo( SYNCHRONOUS);
				//messaggiotipo = SYNCHRONOUS;
		}
	}



	/** Verifica se si tratta di un loop. */
	public boolean ctrlIfLoop() {
		return isLoop;
	}


	/** Clonazione dell'oggetto. 
	 * Clona il messaggio compreso un clone delle partenze e arrivi
	 * inoltre anche l'id ? lo stesso
	 * */
	public ElementoMessaggio cloneMessaggio() {

		ElementoStato ProcessoA = null;
		ElementoStato ProcessoDa =
			((ElementoStato) getElementFrom()).cloneStato();
		if(isLoop){  //creazione del loop
		    ProcessoA = ProcessoDa;
		}
		else{
			 ProcessoA = ((ElementoStato) getElementTo()).cloneStato();	
		}
		ElementoMessaggio cloned;
		cloned =
			new ElementoMessaggio(				
				te,
				ProcessoDa,
				ProcessoA,
				getTipo(),
				"link",
				this.msgRRF,
				true,
				this.getId(),
				null);
		cloned.setValue(this,true);
		return cloned;
	}

	/**
	 * setta i valori dell'elemento messaggio 
	 * @param em elemento messaggio da cui prendere i dati
	 * @param forClone true indica che deve essere copiato anche 
	 *               l'identificatore di em
	 */
	public void setValue(ElementoMessaggio em, boolean forClone) {
		boolean bo = this.testAndSet();
		if(forClone){
			setId( em.getId());
		}
		setTipo(em.getTipo());
		setGuard(em.getGuard());
		setOperations(em.getOperations());
	    setSendReceive(em.getSendReceive());
		setFlussoDiretto(em.getFlussoDiretto());
		setPosizione(em.getPosizione());
		setName(em.getName());

		(grafico).updateLineaProprieta(
			em.getGrafico().getLineColor(),
			em.getGrafico().getLineWeight(),
			em.getGrafico().getLineTheme());
		(grafico).updateTestoProprieta(
			em.getGrafico().getTextColor(),
			em.getGrafico().getTextFont(),
			em.getGrafico().getFontStyle(),
			em.getGrafico().getFontSize());
		isLoop = em.isLoop;	
			
		if (isLoop) {
			((GraficoLoop) (grafico)).setRotazione(
				((GraficoLoop) em.getGrafico()).getRotazione());
		}
	
		this.testAndReset(bo);

	}


	/* (non-Javadoc)
	 * @see data.IUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPreUpdate(this.cloneMessaggio());
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.IUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPostUpdate(this.cloneMessaggio());
			}
		}
	}

	/**
	 * @return Returns the msgRRF.
	 */
	public int getMsgRRF() {
		return msgRRF;
	}

}