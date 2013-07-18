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
    


package plugin.topologychannels.resource.graph;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;


import plugin.topologychannels.resource.data.ElementoBase;




/** 
 * Classe astratta da cui discendono gli oggetti grafici usati nell'applicazione_
 * E' caratterizzata da un unico attributo 'name' con i relativi metodi e da tre
 * metodi astratti: 'getPointMiddle()', 'isIn()' e 'paintElementoGrafico()'. 
 */

public abstract class ElementoGrafico implements Serializable,IGraphElement {

	/** Memorizza il nome dell'elemento grafico. */
	//private String name;

	protected ElementoBase update = null;
	
	/**
	 * Costruttore dell'elementoBox
	 * @param IUpdate gestore dell'evento di update
	 */
	public ElementoGrafico(ElementoBase upd) {
		update = upd;
	}

	/** Restituisce il nome dell'oggetto. */
//	public String getName() {
//		return name;
//	}

	/** Imposta il nome dell'oggetto. */
//	public void setName(String s) {
//		informaPreUpdate();
//		name = s;
//		informaPostUpdate();
//	}


	/**
	 * informa il gestore di update che sto per cambiare qualcosa
	 *
	 */
	protected boolean informaPreUpdate(){
		if (update != null) {
					return update.testAndSet();
			
					//update.informPreUpdate();
				}
		return true;
	}

	/**
	 * informa il gestore di update che il cambiamento � avvenuto
	 *
	 */
	protected void informaPostUpdate(boolean info){
		if (update != null) {
					update.testAndReset(info);
					//update.informPostUpdate();
				}
	}



	/** Tale metodo astratto restituisce il punto centrale dell'elemento grafico. */
	public abstract Point getPointMiddle();

	/** Tale metodo astratto controlla se il punto in ingresso e' all'interno o all'esterno 
	    dell'elemento grafico_ In particolare tale metodo restituisce il valore 'true' se il 
	    punto 'p' si trova all'interno dell'elemento grafico, 'false' altrimenti. */
	public abstract boolean isIn(Point p);

	/** Tale metodo astratto serve per disegnare l'elemento grafico. */
	public abstract void paintElementoGrafico(Graphics2D g2D);

}