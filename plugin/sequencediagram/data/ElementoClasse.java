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
    
package plugin.sequencediagram.data;
import java.awt.Point;
import java.io.Serializable;

import plugin.sequencediagram.graph.GraficoTipoSequence;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaProcStatoNotify;
import plugin.topologydiagram.resource.graph.ElementoBox;
import plugin.topologydiagram.resource.graph.ElementoBoxTesto;

/**	Classe per la gestione delle classi del Sequence Diagram. */

public class ElementoClasse
	extends ElementoProcessoStato
	implements Serializable {

	/**
	 * Costruttore
	 * @param xX posizione x dell'angolo in alto a sinistra
	 * @param yY posizione y dell'angolo in alto a sinistra
	 * @param strText Testo da visualizzare
	 * @param updateEP gestore eventi di modifica
	 */
	public ElementoClasse(
		int xX,
		int yY,
		String strText,
		IListaProcStatoNotify  updateEP) {
		this(xX, yY, strText, false, 0, updateEP);

	}

	/**
	 * Costruttore 
	 * @param xX posizione x dell'angolo in alto a sinistra
	 * @param yY posizione y dell'angolo in alto a sinistra
	 * @param  strText Testo da visualizzare
	 * @param forClone true l'elemento viene creato per una clonazione
	 * @param idElemento identificatore dell'elemento 
	 *                  da utilizzare nel caso di creazione di un clone.
	 * @param updateEP gestore eventi di modifica
	 */
	public ElementoClasse(
		int xX,
		int yY,
		String strText,
		boolean forClone,
		long idElemento,
		IListaProcStatoNotify  updateEP) {

		super(forClone, idElemento, updateEP);
	
		setName(strText);
		grafprocstato = new GraficoTipoSequence(xX, yY,this );
	}

	/** Metodo vuoto_ 
	 *   E' necessario implementarlo perch�
	 *	  definito come astratto nella superclasse 
	 *   <code>ElementoProcessoStato<code>. 
	 */
	public void adjustGraph(ElementoBox graph) {
		
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

			gph.getTextColor(),
			gph.getTextFont(),
			gph.getFontSize(),
			gph.getFontStyle());
	}


	/** Metodo vuoto_ E' necessario implementarlo perch�
		definito come astratto nella superclasse 'ElementoProcessoStato'. */
	public boolean setType(int t) {
		return true;
	}

	/** Imposta la posizione del grafico dell'elemento_
		Sostituisce il metodo nella superclasse 'ElementoProcessoStato'. */
	public void setPoint(Point p) {
		boolean bo = testAndSet(); //controllo messaggi
		Point pp = new Point();

		pp.x = p.x;
		pp.y = grafprocstato.getY();
		grafprocstato.setPoint(pp);
		testAndReset(bo);
	}

	/** Verifica se il punto di coordinate (x,y)
	 *   � interno al grafico dell'elemento.
	 * @param int x coordinata x
	 * @param int y coordinata y
	 * @return true se il punto (x,y) � interno al grafico dell'elemento
	 *                  false altrimenti
	 */
	public boolean isInLine(int x, int y) {
		return ((GraficoTipoSequence) grafprocstato).isInLine(x, y);
	}

	/** Clonazione dell'oggetto.
	 *   @return Clone dell'ElementoClasse
	 *  */
	public ElementoClasse cloneClasse() {
		ElementoBoxTesto gph = (ElementoBoxTesto) grafprocstato;
		ElementoClasse cloned =
			new ElementoClasse(
				grafprocstato.getX(),
				grafprocstato.getY(),
				getName(),
				true,
				getId(),
				null);
		cloned.setValue(this, true);
		
		((ElementoBoxTesto) (cloned.grafprocstato)).setAllParameters(
			gph.getWidth(),
			gph.getHeight(),
			gph.getX(),
			gph.getY(),
			gph.getLineColor(),
			gph.getLineWeight(),
			gph.getBackgroundColor(),
			gph.getTextColor(),
			gph.getTextFont(),
			gph.getFontSize(),
			gph.getFontStyle());
		return cloned;
	}


	/**
	 * setta i valori dell'elemento prelevandoli dall'elemento passato
	 * compreso il nome
	 * @param elemento stato da cui prendere i valori
	 * @param true indica 
	 */
	public void setValue(ElementoClasse classe , boolean forClone){
		boolean bo = testAndSet(); //controllo messaggi
		if(forClone){
			this.setId(classe.getId());
		}
		
		this.setName(classe.getName());
		
		ElementoBoxTesto gph = (ElementoBoxTesto)classe.grafprocstato;
		((ElementoBoxTesto) (this.grafprocstato)).setAllParameters(
				gph.getWidth(),
				gph.getHeight(),
				gph.getX(),
				gph.getY(),
				gph.getLineColor(),
				gph.getLineWeight(),
				gph.getBackgroundColor(),
				gph.getTextColor(),
				gph.getTextFont(),
				gph.getFontSize(),
				gph.getFontStyle());
		
		testAndReset(bo);	
	}



	/** 
	 * 
	 */
	public void setLine(int altezza) {
		boolean bo = testAndSet(); //controllo messaggi
		((GraficoTipoSequence) grafprocstato).setLine(altezza);
		testAndReset(bo);
	}

	public void informPreUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPreUpdate(this);
			}
		}
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPostUpdate(this);
			}
		}
	}

}