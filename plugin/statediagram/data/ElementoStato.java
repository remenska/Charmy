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
    
package plugin.statediagram.data;

import java.awt.Point;
import java.io.Serializable;


import plugin.statediagram.graph.GraficoTipoBuild;
import plugin.statediagram.graph.GraficoTipoStart;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaProcStatoNotify;
import plugin.topologydiagram.resource.graph.ElementoBox;
import plugin.topologydiagram.resource.graph.ElementoBoxTesto;





/** La classe serve per rappresentare gli stati di un processo (nello "State Diagram")_
    Uno stato pu? essere di tipo 'START', 'BUILD' ed 'END'. */

public class ElementoStato
	extends ElementoProcessoStato
	implements Serializable {

	/** Costante che identifica uno stato iniziale nello "State Diagram". */
	public static final int START = 0;

	/** Costante che identifica un generico stato nello "State Diagram". */
	public static final int BUILD = 1;

	/** Costante che identifica uno stato finale nello "State Diagram". */
	//    public static final int END = 2; 

	private String onEntryCode="";
    
	private String onExitCode="";
    

	/** Costruttore che prende in ingresso il punto in cui verr? disegnato lo stato. */
	public ElementoStato(
		Point p,
		String label,
		IListaProcStatoNotify  updateEP) {

		this(p, BUILD, label, updateEP);

	}
	
	

	/** Costruttore che prende in ingresso il punto in cui verr? disegnato 
	    lo stato ed il tipo di stato. */
	public ElementoStato(
		Point p,
		int tipo,
		String label,
		IListaProcStatoNotify  updateEP) {
		//super(updateEP);
		this(p, tipo, label, false, 0, updateEP);
	}

	/**
	 * Costruttore per clonazione,
	 * si usa esclusivamente per clonare esattamente l'elemento compreso di id
	 * @param point punto dove costruire lo stato
	 * @param nome dello stato
	 * @param boolean forClone, true se debbo clonare esattamente il processo (compreso idElemento), 
	 *               false per costruttore normale, con un nuovo identificatore di elemento
	 * @param idElemento da associare al processo nel caso di forClose = true, altrimenti viene ignorato
	 * @param listener per informare il sistema delle eventuali modifiche
	 */
	public ElementoStato(
		Point p,
		int tipo,
		String label,
		boolean forClone,
		long idElemento,
		IListaProcStatoNotify  updateE) {
			
		super(forClone, idElemento, updateE);
		disable();
		setName(label);
		if (tipo == START) {
			setTipo(START);
			//grafprocstato = new GraficoTipoStart(p.x, p.y, label, this);
			grafprocstato = new GraficoTipoStart(p.x, p.y, this);
		}

		else {
			setTipo(BUILD);
			grafprocstato = new GraficoTipoBuild(p.x, p.y, this);
		}
		enabled();
	}




	public void setOnEntryCode(String code)
	{
		boolean bo = this.testAndSet();
		onEntryCode=code;
		this.testAndReset(bo);
	}

	public String getOnEntryCode()
	{
		return onEntryCode;
	}
    
	public void setOnExitCode(String code)
	{
		boolean bo = this.testAndSet();
		onExitCode=code;
		this.testAndReset(bo);
	}
    
	public String getOnExitCode()
	{
		return onExitCode;
	}



	/** Metodo utilizzato per impostare tutte le propriet? grafiche dello stato
		uguali a quelle dell'oggetto grafico passato come parametro_ 
		Risulta particolarmente utile nelle operazione di "paste".*/
	public void adjustGraph(ElementoBox graph) {
		boolean bo = testAndSet(); //controllo messaggi
		ElementoBoxTesto gph = (ElementoBoxTesto) graph;
		grafprocstato.setSelected(true);
		((ElementoBoxTesto) grafprocstato).setAllParameters(
			gph.getWidth(),
			gph.getHeight(),
			gph.getX(),
			gph.getY(),
			gph.getLineColor(),
			gph.getLineWeight(),
			gph.getBackgroundColor(),
//			getName(),
			gph.getTextColor(),
			gph.getTextFont(),
			gph.getFontSize(),
			gph.getFontStyle());
			testAndReset(bo);
	}



	/** Imposta il tipo di stato_ I valori consentiti in ingresso corrispondono
	    soltanto alle costanti intere START, BUILD ed END. */
	public boolean setType(int t) {
		int posX = grafprocstato.getX();
		int posY = grafprocstato.getY();
		String tmpnome = getName();

		if (getTipo() != t) {
			boolean bo = testAndSet(); //controllo messaggi
			switch (t) {
				case START :
					//elementType = START;
					setTipo(START);
					grafprocstato =
						//new GraficoTipoStart(posX, posY, tmpnome, this);
						new GraficoTipoStart(posX, posY, this);
						
					testAndReset(bo);
					return true;
				case BUILD :
					//elementType = BUILD;
					setTipo(BUILD);
					grafprocstato =
						new GraficoTipoBuild(posX, posY, this);
					testAndReset(bo);
					return true;
					/*	        	case END:                    
						            	elementType = END;
					                    grafprocstato = new GraficoTipoEnd(posX,posY,tmpnome);                    
							    		testAndReset(bo);
							    		return true;*/
				default :
					testAndReset(bo);
					return false;
			}
		}
		return true;
	}


	/**
	 * setta i valori dell'elemento prelevandoli dall'elemento passato
	 * compreso il nome
	 * @param elemento stato da cui prendere i valori
	 * @return
	 */
	public void setValue(ElementoStato stato){
		boolean bo = testAndSet(); //controllo messaggi


		this.adjustGraph(stato.getGrafico());
		this.setName(stato.getName());
		this.setType(stato.getTipo());
		this.setOnEntryCode(stato.getOnEntryCode());
		this.setOnExitCode(stato.getOnExitCode());
		testAndReset(bo);	
	}

	
	/**
	 * ridefinizione di setName per un problema di 
	 * visulizzazione del grafico (in questo modo si sincronizza il grafico al nome)
	 * 
	 */
	public void setName(String nome){
		boolean bo = this.testAndSet();
		super.setName(nome);
		if(grafprocstato!=null){
			grafprocstato.refresh();
    	}
	}
	

	/** Restituisce l'ordinata dell'estremo in alto a sinistra. */    
	public int getTopY()
	{
		return grafprocstato.getY();
	}
	
	
	
	/** Clonazione dell'oggetto.
	 * non ? necessario fare il testAndSet poiche si lavora sul clone con update = null
	 *  */
	public ElementoStato cloneStato() {
		ElementoBoxTesto gph = (ElementoBoxTesto) grafprocstato;

		ElementoStato cloned =
			new ElementoStato(
				new Point(grafprocstato.getX(), grafprocstato.getY()),
				getTipo(),
				getName(),
				true,
				getId(),
				null);

		cloned.setValue(this);

		return cloned;
	}

	/* (non-Javadoc)
	 * @see data.interfacce.IUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
		if(sendMsg){
			if(updateEp != null){
	  	  		updateEp.informaPreUpdate(this.cloneStato());
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.interfacce.IUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(sendMsg){
		   if(updateEp != null){
		 	   updateEp.informaPostUpdate(this.cloneStato());
			}
		}
	}

}