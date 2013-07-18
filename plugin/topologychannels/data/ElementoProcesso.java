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
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Vector;


import plugin.topologychannels.graph.GraficoTipoDummy;
import plugin.topologychannels.graph.GraficoTipoProcesso;
import plugin.topologychannels.graph.GraficoTipoStore;
import plugin.topologychannels.resource.data.ElementoProcessoStato;
import plugin.topologychannels.resource.data.interfacce.IListaProcStatoNotify;
import plugin.topologychannels.resource.graph.ElementoBox;
import plugin.topologychannels.resource.graph.ElementoBoxTesto;


/** La classe serve per rappresentare i processi dell'architettura (nella S_A_ Topology)_
 *   Un processo pu� essere di tipo 'PROCESS' o 'STORE' e pu� assumere solo la modalit�
 *   'ACTIVE' o 'PASSIVE'_ 
 *   In realt�, esiste un terzo tipo: il processo 'DUMMY'_ Esso � soltanto fittizio ed ha 
 *   l'unico scopo di modellare una sorgente esterna che manda o riceve un flusso di dati
 *   da un processo dell'architettura.
 */

public class ElementoProcesso extends ElementoProcessoStato implements Serializable {

	/** Tale variabile booleana assume valore "false" se il processo appartiene effettivamente 
	    all'architettura, "true" se invece e' associato ad una sorgente esterna. */
	private boolean dummy;

	/** Costante che identifica un generico processo. */
	public static final int PROCESS = 0;

	/** Costante che identifica un processo di smistamento o deposito dati. */
	public static final int STORE = 1;

	/** Costante che identifica un processo da implementare come oggetto attivo. */
	public static final int ACTIVE = 0;

	/** Costante che identifica un processo da implementare come oggetto passivo. */
	public static final int PASSIVE = 1;

	/** Memorizza la modalit� del processo_ Essa pu� assumere i seguenti 
	    valori costanti interi: ACTIVE, PASSIVE. */
	private int processModality;


	/** Costante che identifica l'inserimento di un processo dal thread editor. */
	public static final int USER = 1;
	
	/** Costante che identifica l'inserimento di un processo globale. */
	public static final int GLOBALE = 0;

	/**
	 * il processo � un processo inserito dall'utente nel thread editoe
	 * oppure nel topologyEditor (USER/GLOBALE)
	 */
	private int appartenenza ; 

	private Vector connectedProcesses;

	/**
	 * costruttore: la notifica � null
	 * @param label nome elemento
	 * @param valore indicante l'appartenenza dell'elemento
	 */
	public ElementoProcesso(String label, int appar){
		this(label,appar, null);
	}
	
	/** Costruttore. */
	public ElementoProcesso(String label, int appar, IListaProcStatoNotify  updateEp) {
		this(new Point(0, 0),appar, label, updateEp);
	}
	/**
	 * costruttore 
	 * @param p punto indicante la posizione
	 * @param valore indicante l'appartenenza dell'elemento
	 * @param tipo di elemento
	 * @param isDummy � un elemento dummy? true/false
	 * @param label nome dell'elemento 
	 */
	public ElementoProcesso(Point p, int appar, int tipo, boolean isDummy, String label) {
		this(p, appar,tipo, isDummy, label, null);
	
	}
	
	/** Costruttore che prende in ingresso il punto in cui verr� disegnato il processo. 
	 * @param IUpdateEp viene informato delle modifiche dell'elemento
	 * */
	public ElementoProcesso(Point p,int appar, String label, IListaProcStatoNotify  updateEp) {
		this(p,appar, PROCESS,false, label, false, 0, updateEp);
	}



	/** Costruttore che prende in ingresso il punto in cui verr� disegnato 
		il processo ed il tipo di processo. */
	public ElementoProcesso(Point p,int appar, int tipo, boolean isDummy, String label, IListaProcStatoNotify  updateEp) {
		this(p,appar, tipo, isDummy, label, false, 0, updateEp);
	}

	/**
	 * Costruttore per clonazione processi,
	 * si usa esclusivamente per clonare esattamente l'elemento compreso di id
	 * @param boolean forClone, true se debbo clonare esattamente il processo (compreso idElemento), 
	 *               false per costruttore normale, con un nuovo identificatore di elemento
	 * @param idClone da associare al processo nel caso di forClose = true, altrimenti viene ignorato
	 * 
	 */

	public ElementoProcesso(
		Point p,
		int appar,
		int tipo,
		boolean isDummy,
		String label,
		boolean forClone,
		long idClone,
		IListaProcStatoNotify  updateEp) {
		super(forClone, idClone, updateEp);
		
		connectedProcesses=new Vector();
		
		//this.updateEp = updateEp;
		dummy = isDummy;
		//setName(label + getId());
		setName(label);
		appartenenza = appar;
		if (isDummy) {
			setTipo(PROCESS);
			grafprocstato = new GraficoTipoDummy(p.x, p.y, this);

		} else {
			dummy = false;
			if (tipo == STORE) {
				setTipo(STORE);
				grafprocstato = new GraficoTipoStore(p.x, p.y, this);
				
			} else {
				setTipo(PROCESS);
				grafprocstato = new GraficoTipoProcesso(p.x, p.y,  this);

			}
		}
		
		processModality = PASSIVE;
	}
	

	/** Costruttore da usarsi per creare un processo 'dummy'_ 
		Il parametro 'ctrldummy' � inutilizzato, serve solo per la 'firma' del metodo. */
	public ElementoProcesso(Point p, boolean ctrldummy, String label, IListaProcStatoNotify  updateEp) {
		super(updateEp);
		this.updateEp = updateEp;
		processModality = PASSIVE;
		dummy = true;
		setType(PROCESS);
	}

	/** Metodo utilizzato per impostare tutte le propriet� grafiche del processo
		uguali a quelle dell'oggetto grafico passato come parametro_ 
		Risulta particolarmente utile nelle operazione di "paste".*/
	public void adjustGraph(ElementoBox graph) {
		if (dummy) {
			Point pnt = new Point(graph.getX() + 100, graph.getY() + 100);

			grafprocstato.setSelected(true);
			grafprocstato.setPoint(pnt);
		} else {
			ElementoBoxTesto gph = (ElementoBoxTesto) graph;
			grafprocstato.setSelected(true);
			((ElementoBoxTesto) grafprocstato).setAllParameters(
				gph.getWidth(),
				gph.getHeight(),
				gph.getX() + 100,
				gph.getY() + 100,
				gph.getLineColor(),
				gph.getLineWeight(),
				gph.getBackgroundColor(),

				gph.getTextColor(),
				gph.getTextFont(),
				gph.getFontSize(),
				gph.getFontStyle());
		}
	}

	/** Imposta il nome del processo.
	 * fa un override della funzione della super classe
	 *  */
	public void setName(String str) {
		super.setName(str);
		try{
			grafprocstato.refresh();
		}
		catch(Exception e){
			
		}

	}
	

	public void setConnectedProcess(String process, String channels){
		LinkedList ll=new LinkedList();
		ll.add(process);
		ll.add(channels);
		connectedProcesses.add(ll);
	}

	public String getConnectedProcess(String process){
		for(int i=0;i<connectedProcesses.size();i++){
			if(((LinkedList)connectedProcesses.get(i)).get(0).equals(process)){
				return (String)((LinkedList)connectedProcesses.get(i)).get(1);
			}
		}
		return null;
	}
	
	/** Imposta il tipo di processo
	 * I valori consentiti in ingresso corrispondono
	 *		soltanto alle costanti intere PROCESS o STORE. 
	 **/
	public boolean setType(int t) {
		int posX = grafprocstato.getX();
		int posY = grafprocstato.getY();
		String tmpnome = getName(); //grafprocstato.getName();

		if ((getTipo() != t) || (dummy)) {
			switch (t) {
				case PROCESS :
					if (dummy) {
						dummy = false;
					}
					setTipo(PROCESS);
					grafprocstato = new GraficoTipoProcesso(posX, posY, this);
					return true;
				case STORE :
					if (dummy) {
						dummy = false;
					}
					dummy = false;
					setTipo(STORE);
					grafprocstato = new GraficoTipoStore(posX, posY, this);
					return true;
				default :
					return false;
			}
		}
		return true;
	}

	

	/** Restituisce la modalita di implementazione del processo_ I valori restituiti
	    corrispondono ad una delle seguenti costanti: ACTIVE, PASSIVE. */
	public int getModality() {
		return processModality;
	}

	/** Imposta la modalit� d'implementazione del processo
	 *  I valori consentiti sono 
	 *  solo i seguenti valori costanti: ACTIVE, PASSIVE. 
	 *  @param tipo di modalit� puo assumere solo i valori
	 *                   ACTIVE, PASSIVE
	 * */
	public boolean setModality(int m) {
		boolean bo = this.testAndSet();
		boolean uscita = false;
		switch (m) {
			case ACTIVE :
				processModality = ACTIVE;
				uscita =  true;
				break;
			case PASSIVE :
				processModality = PASSIVE;
				uscita =  true;
				break;
			default :
				uscita = false;
				break;
		}
		testAndReset(bo);
		return uscita;
		
	}

	/** Metodo per impostare un processo come fittizio_ E' fittizio un 
	    processo associato ad una sorgente esterna all'architettura_ Tale sorgente 
	    pu� spedire o ricevere un flusso da un qualche processo dell'architettura_
	    Dunque, un processo fittizio non appartiene all'insieme dei processi definiti 
	    nell'architettura, ma esiste solo nel canale associato alla sorgente esterna_ 
	    Cfr. nota del metodo 'isDummy()'. */
	public void setDummy(boolean b) {
		
		int posX = grafprocstato.getX();
		int posY = grafprocstato.getY();
		
		String tmpnome = getName(); //grafprocstato.getName();

		if (dummy != b) {
			boolean bo = this.testAndSet();
			dummy = b;
			//grafprocstato = new GraficoTipoDummy(posX, posY, tmpnome, this);
			grafprocstato = new GraficoTipoDummy(posX, posY,  this);
			testAndReset(bo);
		}
		
	}

	/** Restituisce vero se il processo e' fittizio, ossia associato ad una sorgente esterna 
	    all'architettura_ Un processo fittizio non viene memorizzato nella lista dei processi, 
	    ma e' associato solo al canale rappresentante la sorgente esterna_ */
	/** Nota di Luned� 22/10/01_
	    Forse sarebbe meglio modificare questa impostazione (dovuta a L.Quaglia) e memorizzare 
	    anche i processi fittizi nella lista dei processi_ Di questo scelta bisogner�, in ogni
	    caso, tenerne conto nell'implementazione dell'algoritmo di traduzione. */
	public boolean isDummy() {
		return dummy;
	}

	/** Clonazione dell'oggetto.
	 *  Clona un elemento, rimuovendo il gestore di update
	 *  */
	public ElementoProcesso cloneProcesso() {
		ElementoProcesso cloned = null;
		
//		if(appartenenza == GLOBALE){
		cloned =
			new ElementoProcesso(
				new Point(grafprocstato.getX(), grafprocstato.getY()),
				appartenenza,
				getTipo(),
				dummy,
				getName(),
				true,
				getId(),
				null);
		
		cloned.setValue(this,true);
		return cloned;

	}


	/**
	 * copia i valori dell'ElementoProcesso
	 * @param ep elemento processo da cui prelevare i dati
	 * @param forClone setta anche l'identificativo di default è falso
	 */
	public void setValue(ElementoProcesso ep, boolean forClone ){
		
		boolean bo = this.testAndSet();
		
		if(forClone){
			setId(ep.getId());
		}
		
		this.setAppartenenza(ep.getAppartenenza());
		this.setDummy(ep.isDummy());
		this.setModality(ep.getModality());
		this.setName(ep.getName());
		this.setType(ep.getTipo());

		if(ep.isDummy()){
			return;
		}
		
	   ElementoBoxTesto gph = (ElementoBoxTesto) ep.getGrafico();

		
		((ElementoBoxTesto) (this.grafprocstato)).setAllParameters(
				gph.getWidth(),
				gph.getHeight(),
				gph.getX(),
				gph.getY(),
				gph.getLineColor(),
				gph.getLineWeight(),
				gph.getBackgroundColor(),
				//			getName(),  //getName(),
				gph.getTextColor(),
				gph.getTextFont(),
				gph.getFontSize(),
				gph.getFontStyle());
		
		testAndReset(bo);
		
	}
	
	

	/* (non-Javadoc)
	 * @see data.IUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
			if(sendMsg){  //posso inviare il messaggio
				if(updateEp != null){
					updateEp.informaPreUpdate(this.cloneProcesso());
				}
			}
	}

	/* (non-Javadoc)
	 * @see data.IUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(sendMsg){  //posso inviare il messaggio
			if(updateEp != null){
				updateEp.informaPostUpdate(this.cloneProcesso());
			}
		}
	}
	/**
	 * USER/GLOBALE
	 * @return l'identificativo di appartenenza per l'elemento
	 */
	public int getAppartenenza() {
		return appartenenza;
	}

	/**
	 * setta l'identificativo  di appartenenza (GLOBAL / USER)
	 * @param appartenenza
	 */
	public void setAppartenenza(int appartenenza) {
		informPreUpdate();
		this.appartenenza = appartenenza;
		informPostUpdate();
	}

	/** 
	 * redefinisce isIn di ElementoLista Processo
	 * Verifica se il punto di coordinate (x,y) � interno al grafico dell'elemento. */
	public boolean isIn(int x, int y)
	{
		if(appartenenza == USER) return false;
		return super.isIn(x,y);
	}

	/** Verifica che il grafico dell'elemento sia contenuto nel
		rettangolo passato come parametro. */
	public boolean isInRect(Rectangle2D rettesterno)
	{
		if(appartenenza == USER) return false;
		return super.isInRect(rettesterno);
	}
    

}