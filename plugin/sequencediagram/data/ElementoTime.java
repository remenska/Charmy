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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.io.Serializable;

import plugin.sequencediagram.data.interfacce.IListaTimeNotify;
import plugin.topologydiagram.resource.data.ElementoBase;


/**	Questa classe modella gli istanti di tempo nel Sequence Diagram_ 
	Ogni istante di tempo e' un intero non negativo rappresentato 
	dall'attributo time_ Ogni istante di tempo � rappresentato
   	graficamente nel sequence diagram con una fascia orizzontale_
	Per identificarla usiamo due attributi, minY e maxY, che 
	rappresentano rispettivamente l'ordinata min e max della fascia 
	orizzontale_ All'estremo sinistro della fascia � stampato il
	valore dell'istante di tempo (campo time)_
	Per rendere graficamente chiaro l'invio e la ricezione  di pi� 
	messaggi nello stesso istante di tempo, la fascia viene suddivisa
	in settori orizzontali (minimo 2, massimo 11) separati da linee_
	Il numero di linee, ovviamente, va da 1 a 10_ Le intersezioni di 
	queste linee con le linee di vita delle classi determinano i punti
	di attacco (invio e ricezione) dei messaggi_ L'altezza di ogni
	settore orizzontale � pari a hfascia. */

public class ElementoTime extends ElementoBase 
     implements Serializable
{	
	
	/** Variabile di classe che memorizza il numero di istanze di classe
	 che sono state create. */
	protected static long numIstanze = 0;
	
	
	/**	Altezza di un settore orizzontale. */
	public static final int hfascia = 20;
	
	/** Massimo numero di linee consentite. */
	public static final int maxlinee = 10;	
	
	/** Minima ascissa della linea rappresentante
		il lato inferiore della fascia. */
	private static final int minX = 20;

	/** Massima ascissa della linea rappresentante
		il lato inferiore della fascia. */	
	private static final int maxX = 1180;
		
	/** Tratteggio della linea: TYPE1. */
    transient public static final float[] TIME_LINK_TYPE = {2.0f, 8.0f};

	/** Tratteggio della linea: TYPE1. */
    transient public static final float[] SECTOR_LINK_TYPE = {1.0f, 6.0f};
    	
	transient public static Color TIME_LINK_COLOR = Color.lightGray;
	
	transient public static Color SECTOR_LINK_COLOR = Color.lightGray;
	
	/** Ordinata del lato superiore della fascia. */
	private int minY;
	
	/** Ordinata del lato inferiore della fascia. */
	private int maxY;
	
	/** Numero rappresentante l'istante di tempo. */
	private long time;
		
	/** Linea tracciata per visualizzare il lato
		inferiore della fascia. */
	transient private Line2D linea;
	
	/**	Vettore che memorizza il grafico delle linee
		orizzontali che suddividono la fascia in settori. */
	transient private Line2D[] vettore;
	
	/** Numero di linee orizzontali memorizzate in vettore. */
	private int numerolinee;
	
	/** Attributo per abilitare/disabilitare la visualizzazione
		del valore di time all'estremo sinistro della fascia. */
	private boolean stringIsVisible;

	/** Attributo per abilitare/disabilitare la visualizzazione
		delle linee che suddividono la fascia in settori. */	
	private boolean lineIsVisible;
 
	/**
	 * gestore eventi update
	 */
	protected IListaTimeNotify updateEp = null;
	
	
	
	/**
	 * setta il numero di istanza
	 * @param n
	 */
	public static void setNumIstanze(long n) {
		numIstanze = n;
	}
	/**
	 * preleva il numero di istanza globale
	 * @return
	 */
	public static long getNumIstanze() {
		return numIstanze;
	}

	
	
	
	
	/** Il costruttore prende in ingresso tre valori interi: rappresentano
     *	@param l'istante di tempo, 
     * @param l'ordinata massima (estremo Inf) 
     * @param l'ordinata minima (estremoSup) della fascia orizzontale. 
     * @param true/false abilitare/disabilitare la visualizzazzione del testo sulla linea 
     * @param true/false abilitare/disabilitare la visualizzazzione delle righe temporali 
     * @param gestore delle notifiche relative alla modifica
     */ 
	public ElementoTime(long tempo, int estremoInf, 
				int estremoSup, boolean ctrlString, 
				boolean ctrlLine,IListaTimeNotify update )
	{
		time = tempo;
    	minY = estremoSup;
		maxY = estremoInf;
		linea = new Line2D.Double(minX,maxY,maxX,maxY);
		vettore = new Line2D[maxlinee];		
		numerolinee = 1;
		vettore[0] = new Line2D.Double(minX+20,minY+hfascia,maxX-20,minY+hfascia);
		stringIsVisible = ctrlString;
		lineIsVisible = ctrlLine;
		updateEp = update;
		setId(numIstanze++);
	}	


	


	
	/** Il costruttore
	 *	@param l'istante di tempo, 
	 * @param l'ordinata minima (estremoSup) della fascia orizzontale.
	 *  				 l'ordinata massima (estremo Inf) viene calcolata con la
	 * 				seguente formula: estremoSup+2*hfascia  
	 * @param true/false abilitare/disabilitare la visualizzazzione del testo sulla linea 
	 * @param true/false abilitare/disabilitare la visualizzazzione delle righe temporali 
	 * @param gestore delle notifiche relative alla modifica
	 */ 	
	public ElementoTime(long tempo, int estremoSup, boolean ctrlString, boolean ctrlLine, IListaTimeNotify update)
	{
		
		this(tempo,estremoSup+2*hfascia, estremoSup, ctrlString, ctrlLine,update);

	}
	
	
	/** Restituisce l'istante di tempo. */
	public long getTime()
	{
 		return time;
 	}


	/** Imposta l'istante di tempo. */
	public void setTime(long nTime)
	{
		this.informPreUpdate();
		time = nTime;
		this.informPostUpdate();
		
	}


	/** Restituisce l'ordinata minima (estremo superiore) della fascia 
		orizzontale usata per rappresentare l'istante di tempo. */
	public int getMinY()
	{
 		return minY;
 	}
 	

	/** Imposta l'ordinata minima (estremo superiore) della fascia 
		orizzontale usata per rappresentare il tempo. */
	public void setMinY(int nMinY)
	{
		this.informPreUpdate();
 		minY = nMinY;
 		this.informPostUpdate();
 		
 	} 
 	
 	 	
	/** Restituisce l'ordinata massima (estremo inferiore) della fascia 
		orizzontale usata per rappresentare l'istante di tempo. */
	public int getMaxY()
	{
 		return maxY;
 	}


	/** Imposta l'ordinata massima (estremo inferiore) della fascia 
		orizzontale usata per rappresentare il tempo. */
	public void setMaxY(int nMaxY)
	{
		this.informPreUpdate();
 		maxY = nMaxY;
 		linea.setLine(minX,maxY,maxX,maxY);
 		this.informPostUpdate();
 	} 


	/** Restituisce il numero di linee comprese nella fascia temporale. */
	public int getLineNumber()
	{
		return numerolinee;
	}
	
	
	/** Imposta il numero di linee comprese nella fascia temporale. */
	public void setLineNumber(int nlin)
	{
		this.informPreUpdate();
		if ((nlin>0) && (nlin<=maxlinee))
		{
			numerolinee = nlin;
			for(int i=0; i<numerolinee; i++)
			{
				vettore[i] = new Line2D.Double(minX+20,minY+(hfascia*(i+1)),
					maxX-20,minY+(hfascia*(i+1)));				
			}
			maxY = minY+(numerolinee+1)*hfascia;
			linea.setLine(minX,maxY,maxX,maxY);
		}
		this.informPostUpdate();
	}
	
	
	/** Restituisce l'ordinata del centro della fascia. */
	public int getMiddleY()
	{
 		int risultato = (int)((minY+maxY)/2);
 		return risultato;
 	}


	/** Restituisce vero se il punto p si trova nella fascia 
		orizzontale rappresentante l'istante di tempo. */
	public boolean isInTime(Point p)
	{
		if ((p.y > minY) && (p.y <= maxY)) 
		{	
			return true;
		}
   		else 
   		{
   			return false;
   		}
	}
	
	
	/** Imposta la stringa del time visibile. */
	public void setStringVisible(boolean ctrl)
	{
		informPreUpdate();
		stringIsVisible = ctrl;
		informPostUpdate();
	}
	
	
	/** Imposta le linee visibili. */
	public void setLineVisible(boolean ctrl)
	{
		informPreUpdate();
		lineIsVisible = ctrl;
		informPostUpdate();
	}
	
		
    /** Metodo per disegnare l'oggetto. */
    public void paintElementoTime(Graphics2D g2D)
    {
		Stroke tmpStroke;
	   Color tmpColor;

	   tmpStroke = g2D.getStroke();
	   tmpColor = g2D.getColor();
   	
	   if (stringIsVisible)
	   {
		   g2D.drawString(Long.toString(time),minX,(int)((minY+maxY)/2));
	   }
    	
	   g2D.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,
		   BasicStroke.JOIN_ROUND,10.0f,TIME_LINK_TYPE,0));
	   g2D.setColor(TIME_LINK_COLOR);    	
	   g2D.draw(linea);
    	
	   if (lineIsVisible)
	   {
		   g2D.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,
			   BasicStroke.JOIN_ROUND,10.0f,SECTOR_LINK_TYPE,0));
		   g2D.setColor(SECTOR_LINK_COLOR);
		   for (int i=0; i<numerolinee; i++)
		   {
			   g2D.draw(vettore[i]);
		   }
	   }
        
	   g2D.setColor(tmpColor);
	   g2D.setStroke(tmpStroke);     		
    }
    
    
    /** Sposta in basso ('offset' positivo) oppure 
    	in alto ('offset negativo') la fascia che 
    	rappresenta l'istante di tempo. */
    public void addGraphOffset(int offset)
    {
    	informPreUpdate();
		minY = minY + offset;
		maxY = maxY + offset;
		linea.setLine(minX,maxY,maxX,maxY);
		for(int i=0; i<numerolinee; i++)
		{
			vettore[i].setLine(minX+20,minY+(hfascia*(i+1)),maxX-20,minY+(hfascia*(i+1)));				
		}    	
		informPostUpdate();
    }
    
    
    /** Clonazione dell'oggetto. */
    public ElementoTime cloneTime()
    {   	
    	ElementoTime cloned;

    	cloned = new ElementoTime(time,minY,stringIsVisible,lineIsVisible, null);
    	cloned.setLineNumber(numerolinee);
    	return cloned;
    }    


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile()
	{
		linea = new Line2D.Double(minX,maxY,maxX,maxY);
		vettore = new Line2D[maxlinee];		
		for(int i=0; i<numerolinee; i++)
		{
			vettore[i] = new Line2D.Double(minX+20,minY+(hfascia*(i+1)),
				maxX-20,minY+(hfascia*(i+1)));				
		}	
	} 

	
	/**
	 * setta la linea di mezzo
	 * @param larghezza larghezza della linea
	 */
	public void setLine(int larghezza)
	{
		informPreUpdate();
		linea.setLine(minX,maxY,larghezza,maxY);
		for(int i=0; i<numerolinee; i++)
		{
			vettore[i].setLine(minX+20,minY+(hfascia*(i+1)),
				larghezza-20,minY+(hfascia*(i+1)));				
		}		
		informPostUpdate();
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
		if(updateEp != null){
			updateEp.informaPreUpdate(this);
		}
		
	}

	/* (non-Javadoc)
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if(updateEp != null){
			updateEp.informaPostUpdate(this);
		}
	}
	
	/**
	 * ritorna il listener per l'elemento
	 * @return
	 */
	public IListaTimeNotify getUpdateEp() {
		return updateEp;
	}

	/**
	 * setta il listener per l'elemento
	 * @param updateEp
	 */
	public void setUpdateEp(IListaTimeNotify updateEp) {
		this.updateEp = updateEp;
	}

	/**
	 * @return Returns the stringIsVisible.
	 */
	public boolean isStringIsVisible() {
		return stringIsVisible;
	}

	/**
	 * @return Returns the lineIsVisible.
	 */
	public boolean isLineVisible() {
		return lineIsVisible;
	}

}