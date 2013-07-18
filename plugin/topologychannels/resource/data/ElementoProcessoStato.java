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

import plugin.topologychannels.resource.data.interfacce.IListaProcStatoNotify;
import plugin.topologychannels.resource.graph.ElementoBox;

import core.resources.simpleinterface.CoordinatesInterface;


/**	Classe utilizzata per generalizzare metodi e campi degli elementi
	di tipo ElementoProcesso ed ElementoStato. */
	
public abstract class ElementoProcessoStato 
             extends ElementoBase 
             implements CoordinatesInterface,Serializable  
{
    
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

  
    /** Memorizza il grafico associato con il processo. */
    protected ElementoBox grafprocstato = null;


	/**
	 * gestore eventi update
	 */
	protected IListaProcStatoNotify  updateEp;

    /** Costruttore. */
    public ElementoProcessoStato(IListaProcStatoNotify  updateE)
    {
    	
        this(false,0, updateE);
    }


 	/**
	 * Costruttore per clonazione processi,
	 * si usa esclusivamente per clonare esattamente l'elemento compreso di id
	 * @param boolean forClone, true se debbo clonare esattamente il processo (compreso idElemento), 
	 *               false per costruttore normale, con un nuovo identificatore di elemento
	 * @param idElemento da associare al processo nel caso di forClose = true, altrimenti viene ignorato
	 * 
	 */
	public ElementoProcessoStato(boolean forClone, long idElemento,IListaProcStatoNotify  updateE )
	{
		super();
		if(forClone){
			setId(idElemento);
			this.updateEp = updateE;
		}
		else{
			numIstanze++;

			setId(numIstanze);
			
			//setId(newNumIstanze());
			updateEp = updateE;
		}
	}

	/** Metodo utilizzato per impostare tutte le propriet� grafiche dell'elemento
		uguali a quelle dell'oggetto grafico passato come parametro_ 
		Risulta particolarmente utile nelle operazione di "paste".*/
	public abstract void adjustGraph(ElementoBox graph);

    

    
    /** Restituisce il punto centrale del grafico associato all'elemento. */
    public Point getPointMiddle()
    {
        return grafprocstato.getPointMiddle();
    }

    
    /** Verifica se il punto di coordinate (x,y) � interno al grafico dell'elemento. */
    public boolean isIn(int x, int y)
    {
        return grafprocstato.isIn(x,y);
    }

    
    /** Disegna l'elemento. */
    public void paintElemento(Graphics2D g2D)
    {
        grafprocstato.paintElementoGrafico(g2D);
    }
        



	/** Memorizza il tipo di Elemento_ Pu� assumere il valore delle costanti intere PROCESS
		e STORE nel caso di un processo, START, BUILD ed END nel caso di uno stato. */

    /** Imposta il tipo dell'elemento_
    	Metodo astratto perch� l'implementazione dipende dalla
    	specializzazione dell'oggetto nelle sottoclassi. */
    public abstract boolean setType(int t);	  
   
   	/** da implementare obbligatoriamente, ricordarsi di settare setTipo della classe super 
	*/
//	public abstract boolean setTipo(int t);	  
    
    /**	Restituisce il riferimento all'oggetto che implementa il grafico dell'elemento. */
    public ElementoBox getGrafico()
    {
        return grafprocstato;
    }
    
    
    /** Imposta la posizione del grafico dell'elemento. */
    public void setPoint(Point p)
    {
        grafprocstato.setPoint(p);
    }
    
    
    /** Selezione/deselezione dell'elemento. */
    public void setSelected(boolean ctrlSelection)
    {
        grafprocstato.setSelected(ctrlSelection);
    }
    
    
    /** Verifica se l'elemento � stato selezionato. */
    public boolean isSelected()
    {
        return grafprocstato.isSelected();
    } 
    
    
    /** Inverte lo stato di selezione/deselezione dell'elemento. */
    public void invSelected()
    {
    	grafprocstato.invSelected();
    }
    
    
    /** Verifica che il grafico dell'elemento sia contenuto nel
    	rettangolo passato come parametro. */
    public boolean isInRect(Rectangle2D rettesterno)
    {
    	return grafprocstato.isInRect(rettesterno);
    }
    
    
    /** Restituisce l'ascissa dell'estremo in alto a sinistra. */
    public int getTopX()
    {
    	return grafprocstato.getX();
    }
    

    /** Restituisce l'ordinata dell'estremo in alto a sinistra. */    
    public int getTopY()
    {
    	return grafprocstato.getY();
    }
    

    /** Restituisce la larghezza dell'elemento. */    
    public int getWidth()
    {
    	return grafprocstato.getWidth();
    }
    

    /** Restituisce l'altezza dell'elemento. */      
    public int getHeight()
    {
    	return grafprocstato.getHeight();
    }



	/**
	 * ritorna la classe di ascolto update
	 * @return
	 */
	public IListaProcStatoNotify getUpdateEp() {
		return updateEp;
	}

	/**
	 * setta la classe di ascolto update
	 * @param updateEP
	 */
	public void setUpdateEp(IListaProcStatoNotify  updateEP) {
		updateEp = updateEP;
	}
	
}