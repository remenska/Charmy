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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.topologychannels.resource.data.ElementoBase;
import plugin.topologychannels.resource.data.ElementoProcessoStato;



/** La classe serve per disegnare un collegamento a forma di loop.
	Istanze della classe sono utilizzate negli State Diagram. */

public class GraficoLoop extends GraficoLink implements Serializable
{   
    
    /** La prima parte del loop ? rappresentata con un arco. */
    transient private Arc2D.Double primoarco;
    
    /** La seconda parte del loop ? rappresentate con una linea. */
    transient private Line2D.Double primalinea;
    
    /** La terza parte del loop ? rappresentata con un arco. */
    transient private Arc2D.Double secondoarco;
    
    /** La quarta parte del loop ? rappresentate con una linea. */    
    transient private Line2D.Double secondalinea;
    
    /** La quinta parte del loop ? rappresentata con un arco. */
    transient private Arc2D.Double terzoarco;
    
    /** Semilarghezza dell'oggetto a cui si riferisce il loop. */
    private int semiw;

    /** Semialtezza dell'oggetto a cui si riferisce il loop. */    
    private int semih;
    
    /** Variabile che determina la posizione del loop. */
	private boolean rotazione;
	
	private ElementoProcessoStato elementoBox; //box di partenza
	
	// private Polygon ppoly;

	    
    /** Costruttore_ 
        x1      : ascissa del punto iniziale/finale del loop_
        y1      : ordinata del punto iniziale/finale del loop_
        wmezzi  : semilarghezza del punto iniziale/finale del loop_
        hmezzi  : semialtezza del processo a cui ? associato il loop_
        i       : determina il tipo di loop (il 'raggio' del loop)_
        txt     : testo associato al loop_
        TipoMessaggio: determina la forma della freccia del loop. */
  //  public GraficoLoop(int x1, int y1, int wmezzi, int hmezzi, int i, String txt, int TipoMessaggio, boolean flusso, IUpdate update)


  /** Costruttore_ 
   * @param x1      : ascissa del punto iniziale/finale del loop_
   * @param y1      : ordinata del punto iniziale/finale del loop_
   * @param  wmezzi  : semilarghezza del punto iniziale/finale del loop_
   * @param  hmezzi  : semialtezza del processo a cui ? associato il loop_
   * @param i       : determina il tipo di loop (il 'raggio' del loop)_
   * @param int regReqFail
   * @param ElementoBase: fornisce
	        		txt     : testo associato al loop_
	    			TipoMessaggio: determina la forma della freccia del loop. 
    */
/*
	public GraficoLoop(int x1, int y1, int wmezzi, int hmezzi, int i,int regReqFail, boolean flusso, ElementoBase update)
    {
        super(update);
        update.disable();
        int deltaangle = 0;
       // String testo;
        xstart = x1;
        ystart = y1;
        semiw = wmezzi;
        semih = hmezzi;
        posCollegamento = i;
        
        /* sostituito da ElementoBase.getViewName
        switch (regReqFail) 
        {
        	case 3:  
        		testo = "e: "+update.getName();
        		break;
        	case 4:
        		testo = "r: "+update.getName();
        		break;
        	case 5:
        		testo = "f: "+update.getName();
        		break;
        	default: 
        		testo = "e: "+update.getName();
        }
        */	
 /*       InitVariables();
        rotazione = false;
        primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
        		semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
        primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
        secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
        		ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
        secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
        		ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
        terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
        		ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
        flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
        		Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),update.getTipo(),((flusso)? 2:3));                
        testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
        		DEFAULT_FONTRENDERCONTEXT);
        testoX = (float)(xstart+semiw/2-4);
        testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
        rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
        rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);          	            
        update.enabled();
    }
 */   

	/** Costruttore_ 
	 * @param x1      : ascissa del punto iniziale/finale del loop_
	 * @param y1      : ordinata del punto iniziale/finale del loop_
	 * @param  wmezzi  : semilarghezza del punto iniziale/finale del loop_
	 * @param  hmezzi  : semialtezza del processo a cui ? associato il loop_
	 * @param i       : determina il tipo di loop (il 'raggio' del loop)_
	 * @param int regReqFail
	 * @param ElementoBase: fornisce
	 txt     : testo associato al loop_
	 TipoMessaggio: determina la forma della freccia del loop. 
	 */
/*	public GraficoLoop(int x1, int y1, int wmezzi, int hmezzi, int i, boolean flusso, ElementoBase update)
	{
		super(update);
		int deltaangle = 0;
		
		xstart = x1;
		ystart = y1;
		semiw = wmezzi;
		semih = hmezzi;
		posCollegamento = i;
		//testo=txt;
		InitVariables();
		
		rotazione = false;
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),update.getTipo(),((flusso)? 2:3));                
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
		rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);          	            
	}    

*/	
	/**
	 * Costruttore
	 * @param box1 ElementoBox di partenza
	 * @param i
	 * @param flusso
	 * @param update
	 */
	public GraficoLoop(ElementoProcessoStato box1, int i, boolean flusso, ElementoBase update)
	{
		super(update);
		int deltaangle = 0;
		elementoBox = box1;
		calcolaStartEnd(i);
	/*	
		xstart = x1;
		ystart = y1;
		semiw = wmezzi;
		semih = hmezzi;
		posCollegamento = i;
		//testo=txt;
	*/
		InitVariables();
		
		rotazione = false;
		calcolaValoriGrafici(flusso);
		/*
		
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),update.getTipo(),((flusso)? 2:3));                
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
		rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
		*/          	            
	}    
	
	
	/**
	 * calcola i punti di partenza e di arrivo
	 * per il canale
	 * @param posizione da riempire con l'arco
	 */
	private void calcolaStartEnd(int posizione){
		posCollegamento = posizione;
		xstart = elementoBox.getPointMiddle().x;
		ystart = elementoBox.getPointMiddle().y;;
		semiw = elementoBox.getWidth() / 2;
		semih = elementoBox.getHeight() / 2;
	}
	
	
	
	/**
	 * funzione per il calcolo dei valori 
	 * relativi al disegno
	 *
	 */
	private void calcolaValoriGrafici(boolean flusso){

		
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),update.getTipo(),((flusso)? 2:3));                
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		if (rotazione)
		{	        
			rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);          		
		}
		else
		{
			rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);      			
		}
	}
	
	
	
	
/*	public GraficoLoop(int x1, int y1, int wmezzi, int hmezzi, int i,boolean flusso, ElementoBase update)
	{
		super(update);
		update.disable();
		int deltaangle = 0;
		
		xstart = x1;
		ystart = y1;
		semiw = wmezzi;
		semih = hmezzi;
		posCollegamento = i;
		//testo = txt;
		InitVariables();
		rotazione = false;
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),update.getTipo(),((flusso)? 2:3));                
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
		rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);          	            
		update.enabled();
	}
*/	
	
	/**  
	 *  Metodo per aggiornare la dimensione del loop. 
	 * @param x1      : ascissa del punto iniziale/finale del loop_
	 * @param y1      : ordinata del punto iniziale/finale del loop_
	 * @param  wmezzi  : semilarghezza del punto iniziale/finale del loop_
	 * @param  hmezzi  : semialtezza del processo a cui ? associato il loop_
	 * @param i       : determina il tipo di loop (il 'raggio' del loop)_
	 *        """" @param ElementoBase: fornisce
	 */
 /*   public void updateLoopSize(int x1, int y1, int wmezzi, int hmezzi, int TipoMessaggio, boolean flusso)
    {   
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		xstart = x1;
		ystart = y1;
		semiw = wmezzi;
		semih = hmezzi;
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),TipoMessaggio,((flusso)? 2:3));                
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		if (rotazione)
		{	        	
			rectSelFirst.setRect(xstart+semiw/2-3,ystart-semih*2-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond.setRect(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);          		
		}
		else
		{
			rectSelFirst.setRect(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond.setRect(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);      			
		}
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}

    }
    
	*/
	/** Metodo per impostare la posizione del loop: in alto
		a destra con 'false', in basso a sinistra con 'true'. */
	public void setRotazione(boolean ctrlRotazione)
	{
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		rotazione = ctrlRotazione;
		if (rotazione)
		{	        	
			rectSelFirst.setRect(xstart+semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond.setRect(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);          		
		}
		else
		{
			rectSelFirst.setRect(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond.setRect(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);      			
		}		
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}
	
	}
	

	/** Restituisce la posizione del loop. */	
	public boolean getRotazione()
	{
		return rotazione;
	}
	

	/** Metodo per aggiornare la posizione del loop. */	
	public void updateLoopPosizione(int TipoMessaggio, boolean flusso)
	{
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		
		calcolaStartEnd(TipoMessaggio);
		calcolaValoriGrafici(flusso);
		/*
		
		xstart = x1;
		ystart = y1;
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);    
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),TipoMessaggio,((flusso)? 2:3));                
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		
		if (rotazione)
		{	        
			rectSelFirst.setRect(xstart+semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond.setRect(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);          		
		}
		else
		{
			rectSelFirst.setRect(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond.setRect(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);      			
		}	
		
		*/
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}

	}    
    
    
    /** Metodo per disegnare l'oggetto. */    
    public void paintGraficoLink(Graphics2D g2D)
    {      
    	Stroke tmpStroke;
    	Paint tmpPaint;
    	AffineTransform tmpTransform;
    	double angolo;

    	tmpStroke = g2D.getStroke();
    	tmpPaint = g2D.getPaint();
    	tmpTransform = g2D.getTransform();
    	if (rotazione)
    	{
    		g2D.rotate(Math.PI,xstart,ystart);
    	}
    	g2D.setStroke(new BasicStroke(collegamentospessore,BasicStroke.CAP_ROUND,
    			BasicStroke.JOIN_ROUND,10.0f,collegamentotratteggio,0));
    	g2D.setColor(collegamentocolore);        
    	g2D.draw(primoarco);
    	g2D.draw(primalinea);
    	g2D.draw(secondalinea);
    	g2D.draw(secondoarco);
    	g2D.draw(terzoarco);
    	/*
    	 if (ppoly != null)
    	 {
    	 g2D.draw(ppoly);
    	 }
    	 */
    	flex.paintFreccia(g2D);        
    	if (rotazione)
    	{
    		g2D.rotate(Math.PI,testoX+semiw/2+4,testoY-5);
    	}
    	g2D.setColor(testocolore);
    	g2D.setFont(new Font(testofont,fontstile,fontdimensione));
    	testolayout.draw(g2D,testoX,testoY);
    	g2D.setStroke(tmpStroke);
    	paintSelected(g2D);        
    	g2D.setPaint(tmpPaint);        
    	g2D.setTransform(tmpTransform);
    }
    
    
    /** Verifica se il punto 'pnt' ? sufficientemente vicino alla zona "attiva" del loop. */
    public boolean isSelezionato(Point pnt)
    {
    	final int delta = 5;
    	int[] ascisse = new int[4];
    	int[] ordinate = new int[4];
    	Polygon poly;
    	boolean risultato;

    	if (rotazione)
    	{
    		ascisse[0] = Math.round((float)(xstart-semiw/2));
    		ascisse[1] = Math.round((float)(xstart-semiw/2));
    		ascisse[3] = Math.round((float)(xstart-3*semiw/2));
    		ascisse[2] = Math.round((float)(xstart-3*semiw/2));
    		ordinate[0] = Math.round((float)(ystart+semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE-delta));
    		ordinate[1] = Math.round((float)(ystart+semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE+delta));
    		ordinate[3] = Math.round((float)(ystart+semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE-delta));
    		ordinate[2] = Math.round((float)(ystart+semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE+delta));        	
    	}
    	else
    	{
    		ascisse[0] = Math.round((float)(xstart+semiw/2));
    		ascisse[1] = Math.round((float)(xstart+semiw/2));
    		ascisse[3] = Math.round((float)(xstart+3*semiw/2));
    		ascisse[2] = Math.round((float)(xstart+3*semiw/2));
    		ordinate[0] = Math.round((float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-delta));
    		ordinate[1] = Math.round((float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE+delta));
    		ordinate[3] = Math.round((float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-delta));
    		ordinate[2] = Math.round((float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE+delta));
    	}
    	poly = new Polygon(ascisse,ordinate,4);
    	risultato = poly.contains(pnt);
    	// Nel prototipo ? scritto che le due istruzioni seguenti sono state aggiunte
    	// per consentire al 'Garbage Collection' di rimuovere l'oggetto temporaneo 'poly'.
    	ascisse = null;
    	ordinate = null;
    	// ppoly = poly;
    	return risultato;
    }
        
 
    /** Permette di aggiornare le propriet? grafiche del testo. */
    public void updateTestoProprieta(/*String nuovo_testo,*/ 
		Color nuovo_testocolore,
        String nuovo_testofont, int nuovo_fontstile, int nuova_fontdimensione)
	{
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		//testo = nuovo_testo;
		testocolore = nuovo_testocolore;
		testofont = nuovo_testofont;
		fontstile = nuovo_fontstile;
		fontdimensione = nuova_fontdimensione;
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);		
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}

	}
	
   /** Permette di aggiornare le propriet? grafiche del testo. */
    public void updateTestoProprieta(
    	/*String nuovo_testo,*/
    	String nuova_guardia,String nuove_operazioni, Color nuovo_testocolore,
        String nuovo_testofont, int nuovo_fontstile, int nuova_fontdimensione)
	{
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		String testo;
		if (nuova_guardia!="")
			if(nuove_operazioni!="")
				testo = nuova_guardia+"/"+update.getViewName() /*nuovo_testo*/+"/"+nuove_operazioni;
			else
				testo = nuova_guardia+"/"+update.getViewName() /*nuovo_testo*/;
		else
			if(nuove_operazioni!="")
				testo = update.getViewName() /*nuovo_testo*/ +"/"+nuove_operazioni;
			else
				testo = update.getViewName() /*nuovo_testo*/;
		testocolore = nuovo_testocolore;
		testofont = nuovo_testofont;
		fontstile = nuovo_fontstile;
		fontdimensione = nuova_fontdimensione;
		testolayout = new TextLayout(testo, new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);		
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}
		
	}
	
    /** Permette di modificare l'orientamento della freccia e
        di cambiarne la forma in funzione del tipo di messaggio. */
    public void updateFreccia(int TipoMessaggio, boolean flusso)
    {
		boolean bo = update.getStato();
		if(bo){
			update.informPreUpdate();
			update.disable();
		}  
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),TipoMessaggio,((flusso)? 2:3));
		
		if (bo) {
			update.enabled();
			update.informPostUpdate();
		}

    }	        
    /** Permette di modificare l'orientamento della freccia,
     di cambiarne la forma in funzione del tipo di messaggio e
     di cambiare il tipo di messaggio: Regular, Required, Fail. */
    //public void updateFreccia(int TipoMessaggio, String testo, boolean flusso)
 /*   
	public void updateFreccia(int TipoMessaggio, boolean flusso)
    {
    	flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
    			Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),TipoMessaggio,((flusso)? 2:3));
    	//updateTesto(testo);    
    }	        
   */ 

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile(int TipoMessaggio,boolean flusso)
	{		
		
		calcolaStartEnd(TipoMessaggio);
		calcolaValoriGrafici(flusso);
		
		/*
		testolayout = new TextLayout(update.getViewName(), new Font(testofont,fontstile,fontdimensione),
				DEFAULT_FONTRENDERCONTEXT);
		testoX = (float)(xstart+semiw/2-4);
		testoY = (float)(ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-5);
		if (rotazione)
		{	        
			rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1+2.0/3.0)*DEFAULT_LINK_DISTANCE-3,6,6);          		
		}
		else
		{
			rectSelFirst = new Rectangle2D.Double(xstart+semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);
			rectSelSecond = new Rectangle2D.Double(xstart+3*semiw/2-3,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE-3,6,6);      			
		}
		*/
		/*
		flex = new Freccia(Math.round((float)(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2)),
				Math.round((float)(ystart-semih-DEFAULT_LINK_DISTANCE/2)),TipoMessaggio,((flusso)? 2:3));
		primoarco = new Arc2D.Double(xstart,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				semiw,2*(semih+(posCollegamento+1)*DEFAULT_LINK_DISTANCE),90,90,0);
		primalinea = new Line2D.Double(xstart+semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,
				xstart+3*semiw/2,ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE);
		secondoarco = new Arc2D.Double(xstart+3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-semih-(posCollegamento+1)*DEFAULT_LINK_DISTANCE,posCollegamento*DEFAULT_LINK_DISTANCE,
				2*posCollegamento*DEFAULT_LINK_DISTANCE,0,90,0);
		secondalinea = new Line2D.Double(xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,
				ystart-semih,xstart+3*semiw/2+posCollegamento*DEFAULT_LINK_DISTANCE/2,ystart-semih-DEFAULT_LINK_DISTANCE);
		terzoarco = new Arc2D.Double(xstart-3*semiw/2-DEFAULT_LINK_DISTANCE*posCollegamento/2,
				ystart-2*semih,3*semiw+posCollegamento*DEFAULT_LINK_DISTANCE, 2*semih,270,90,0);
				*/             	
	} 
		
}  