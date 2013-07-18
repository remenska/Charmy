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

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.topologychannels.resource.data.ElementoBase;




/** La classe specializza la classe 'ElementoBoxTesto'. */

public abstract class GraficoTipo extends ElementoBoxTesto implements Serializable
{

    /** Memorizza le caratteristiche della stringa. */
    transient protected TextLayout testolayout;
   
    /** Memorizza la larghezza del rettangolo.  */
    protected double tmplarghezza;
    
    /** Memorizza l'altezza del rettangolo. */
    protected double tmpaltezza;
    
    /** Memorizza la larghezza del testo. */
    protected double larghezzatesto;
    
    /** Memorizza l'altezza del testo. */
    protected double altezzatesto;

    /** Memorizza l'ascissa del punto in basso a sinistra del testo. */
    protected float testoX;
    
    /** Memorizza l'ordinata del punto in basso a sinistra del testo. */
    protected float testoY;

    /** Rettangolo che delimita il testo. */
    transient protected Rectangle2D testolimiti;

    
    /** Costruttore_
        @param x0 : ascissa dell'estremo in alto a sinistra_
        @param y0 : ordinata dell'estremo in alto a sinistra_
        @param txt: stringa d'inizializzazione del testo al centro del rettangolo.
        @param funzioni di inforamzioni per l'update
    */
    
    public GraficoTipo(int x0, int y0, int dWidth, int dHeight, ElementoBase update)
    {
        super(x0,y0,dWidth,dHeight, update);
    }


	/** Permette di modificare la posizione dell'oggetto. */
    public void setPoint(Point p)
    {

    	boolean bo = this.informaPreUpdate();
		int dX = p.x - getX();
		int dY = p.y - getY();
		
		setX(p.x);
		setY(p.y);
		setRectShape(p.x,p.y,getWidth(),getHeight());         
		testoX = testoX + dX;
		testoY = testoY + dY;       
		updateIfSelected();
		this.informaPostUpdate(bo);
		
    }
 
 
	/** Permette di modificare posizione e dimensioni dell'oggetto. */    
    protected abstract void setRectShape(int x, int y, int cwidth, int cheight);

    
    /** Verifica che larghezza ed altezza rispettino i vincoli imposti 
        dalle dimensioni del testo. */
    protected abstract void ctrlWidthAndHeight();

    
    /** Metodo per impostare tutte le propriet? dell'oggetto. */
    public void setAllParameters(int nuova_larghezza, int nuova_altezza, int nuovo_rettangoloX,
        int nuovo_rettangoloY, Color nuovo_lineacolore, int nuovo_lineaspessore,
        Color nuovo_sfondocolore /*, String nuovo_testo*/, Color nuovo_testocolore,
        String nuovo_testofont, int nuova_fontdimensione, int nuovo_fontstile)
    {
    	boolean bo = this.informaPreUpdate();
    	tmplarghezza = nuova_larghezza;
    	tmpaltezza = nuova_altezza;
    	setX(nuovo_rettangoloX);
    	setY(nuovo_rettangoloY);
    	setLineColor(nuovo_lineacolore);
    	setLineWeight(nuovo_lineaspessore);
    	setBackgroundColor(nuovo_sfondocolore);
    	setTextColor(nuovo_testocolore);
    	setTextFont(nuovo_testofont);
    	setFontSize(nuova_fontdimensione);
    	setFontStyle(nuovo_fontstile);
    	testolayout = new TextLayout(update.getViewName(), new Font(nuovo_testofont,nuovo_fontstile,
    			nuova_fontdimensione),DEFAULT_FONTRENDERCONTEXT);
    	testolimiti = testolayout.getBounds();
    	larghezzatesto = testolimiti.getWidth();
    	altezzatesto = testolimiti.getHeight();
    	ctrlWidthAndHeight();
    	testoX = (float)(nuovo_rettangoloX + (tmplarghezza - larghezzatesto)/2);
    	testoY = (float)(nuovo_rettangoloY + (tmpaltezza + altezzatesto)/2);
    	setRectShape(getX(),getY(),
    			Math.round((float)tmplarghezza),Math.round((float)tmpaltezza));
    	setWidth(Math.round((float)tmplarghezza));  
    	setHeight(Math.round((float)tmpaltezza));        	
    	updateIfSelected();
    	this.informaPostUpdate(bo);
        //update.testAndReset(bo);
    }


	/** Metodo per modificare il testo (ed il nome) dell'oggetto. */ 
 	public void setTextGraficoTipo(String nuovo_testo)
 	{
 		boolean bo = this.informaPreUpdate();
		testolayout = new TextLayout(nuovo_testo, new Font(getTextFont(),getFontStyle(),
				getFontSize()),DEFAULT_FONTRENDERCONTEXT);
		testolimiti = testolayout.getBounds();
		larghezzatesto = testolimiti.getWidth();
		altezzatesto = testolimiti.getHeight();
		ctrlWidthAndHeight();
		testoX = (float)(getX() + (tmplarghezza - larghezzatesto)/2);
		testoY = (float)(getY() + (tmpaltezza + altezzatesto)/2);
		setRectShape(getX(),getY(),
				Math.round((float)tmplarghezza),Math.round((float)tmpaltezza));
		setWidth(Math.round((float)tmplarghezza));
		setHeight(Math.round((float)tmpaltezza));        	
		updateIfSelected();        	                 		
		informaPostUpdate(bo);
 	}


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */    
	public void restoreFromFile()
	{
		testolayout = new TextLayout(update.getViewName(), new Font(getTextFont(),getFontStyle(),
				this.getFontSize()),DEFAULT_FONTRENDERCONTEXT);
		testolimiti = testolayout.getBounds();
		larghezzatesto = testolimiti.getWidth();
		altezzatesto = testolimiti.getHeight();
		ctrlWidthAndHeight();
		testoX = (float)(getX() + (getWidth() - larghezzatesto)/2);
		testoY = (float)(getY() + (getHeight() + altezzatesto)/2);        		
		super.restoreFromFile();
	}      
	
}