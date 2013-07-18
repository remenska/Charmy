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
    

package plugin.topologydiagram.resource.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;


import plugin.topologydiagram.resource.data.ElementoBase;





/** Superclasse astratta di GraficoCollegamento e GraficoLoop. */

public abstract class GraficoLink implements Serializable
{
    
    /** Distanza tra un link e quello sottostante. */
    public static final int DEFAULT_LINK_DISTANCE = 30;

    /** Colore del testo. */
    protected static Color DEFAULT_TEXT_COLOR = Color.black;

    /** Memorizza il colore del testo. */
    protected Color testocolore;

    /** Font del testo. */
    protected static String DEFAULT_TEXT_FONT = "Times New Roman";

    /** Memorizza il font del testo. */
    protected String testofont;

    /** Stile del font. */
    protected static int DEFAULT_FONT_STYLE = Font.TRUETYPE_FONT;

    /** Memorizza lo stile del font. */
    protected int fontstile;

    /** Dimensione del font. */
    protected static int DEFAULT_FONT_SIZE = 12;

    /** Memorizza la dimensione del font. */
    protected int fontdimensione;

    /** Dimensione del font: valore minimo. */
    public static final int minlinkfontdimensione = 8;

    /** Dimensione del font: valore massimo. */
    public static final int maxlinkfontdimensione = 16;

    /** Dimensione del font: risoluzione. */
    public static final int steplinkfontdimensione = 2;

    /** Tratteggio della linea: DEFAULT. */
   public static final float[] SIMPLE_LINK_DASH = {10.0f, 0.0f, 10.0f, 0.0f};

    /** Costante associata al tratteggio 'DEFAULT'. */
    public static final int SIMPLE_PATTERN = 0;


    /** Tratteggio della linea: TYPE1. */
    public static final float[] TYPE1_LINK_DASH = {5.0f, 5.0f, 5.0f, 5.0f};

    /** Costante associata al tratteggio 'TYPE1'. */
    public static final int TYPE1_PATTERN = 1; 

    /** Tratteggio della linea: TYPE2. */
    public static final float[] TYPE2_LINK_DASH = {10.0f, 4.0f, 2.0f, 4.0f};

    /** Costante associata al tratteggio 'TYPE2'. */
    public static final int TYPE2_PATTERN = 2;
    
    /** Memorizza il tipo del tratteggio. */
    protected float[] collegamentotratteggio;
    
    /** Pattern del collegamento. */
    protected static int DEFAULT_LINK_PATTERN = SIMPLE_PATTERN;
//    protected static int DEFAULT_LINK_PATTERN = TYPE1_PATTERN;

    /** Memorizza la costante associata al tipo del tratteggio. */
    protected int tratteggiotipo;

    /** Colore del collegamento. */
    protected static Color DEFAULT_LINK_COLOR = Color.black;

	/** Memorizza il colore del collegamento. */
    protected Color collegamentocolore;

	/** Spessore del collegamento. */
    protected static int DEFAULT_LINK_WEIGHT = 1;

	/** Memorizza lo spessore del collegamento. */
    protected int collegamentospessore;

    /** Dimensione dello spessore del link: valore minimo. */
    public static final int mincollegamentospessore = 1;

	/** Dimensione dello spessore del link: valore massimo. */
    public static final int maxcollegamentospessore = 3;

	/** Dimensione dello spessore del link: risoluzione. */
    public static final int stepcollegamentospessore = 1;

    /** Necessario per l'implementazione. */
    public static final FontRenderContext DEFAULT_FONTRENDERCONTEXT =
        new FontRenderContext(new AffineTransform(), true, true);

    /** Ascissa del punto iniziale del link. */
    protected double xstart;

    /** Ordinata del punto iniziale del link. */
    protected double ystart;
    
    /** Ascissa del punto finale del link. */
    protected double x_end;

    /** Ordinata del punto finale del link. */
    protected double y_end;

//    /** Memorizza il testo del link. */
 //   protected String testo;
    
    /** Memorizza la guardia del link. */
    protected String guardia;
    
    /** Memorizza le operazioni del link. */
    protected String operazioni;
    

    /** Variabile per disegnare il testo. */
    transient protected TextLayout testolayout;

    /** Memorizza il tipo di link. */
    protected int posCollegamento;

    /** Memorizza la freccia del link. */
    transient protected Freccia flex;

    /** Ascissa del punto in basso a sinistra del testo. */
    protected float testoX;

    /** Ordinata del punto in basso a sinistra del testo. */
    protected float testoY;
    
    /** True quando il link viene selezionato, false altrimenti. */ 
    protected boolean isSelected;
    
    /** Primo rettangolo per evidenziare la selezione del link. */
    transient protected Rectangle2D rectSelFirst;
    
    /** Secondo rettangolo per evidenziare la selezione del link. */
    transient protected Rectangle2D rectSelSecond;
    
    /**
     * eventhandler
     */
    protected ElementoBase update;
    
    /** Costruttore. */
    public GraficoLink(ElementoBase update)
    {
    	this.update = update;
    }


    /** Richiamato dal costruttore per inizializzare alcune variabili. */
    protected void InitVariables()
    {
    	testocolore = DEFAULT_TEXT_COLOR;
    	testofont = DEFAULT_TEXT_FONT;
    	fontdimensione = DEFAULT_FONT_SIZE;
    	fontstile = DEFAULT_FONT_STYLE;
    	collegamentocolore = DEFAULT_LINK_COLOR;
    	collegamentospessore = DEFAULT_LINK_WEIGHT;
    	tratteggiotipo = DEFAULT_LINK_PATTERN;
    	switch (tratteggiotipo)
    	{
    		case TYPE1_PATTERN:
    			collegamentotratteggio = TYPE1_LINK_DASH;
    			break;
    		case TYPE2_PATTERN:
    			collegamentotratteggio = TYPE2_LINK_DASH;
    			break;
    		default:
    			collegamentotratteggio = SIMPLE_LINK_DASH;
//          		collegamentotratteggio = TYPE1_LINK_DASH;
    			break;
    	}        
    }


    /** Permette di modificare il testo del collegamento. */
    //public abstract void updateTesto(String nuovo_testo);

    /** Permette di modificare il testo del collegamento. */
   //public abstract void updateTesto(String nuovo_testo,String nuova_guardia,String nuove_operazioni);
    
    /** Permette di aggiornare le propriet? grafiche del testo. */
    public abstract void updateTestoProprieta(Color nuovo_testocolore,
        String nuovo_testofont, int nuovo_fontstile, int nuova_fontdimensione);


    /** 
     * Permette di aggiornare le propriet? grafiche della linea. 
     * @param nuovo colore per la linea
     * @param spessore della linea
     * @param tipo di tratteggio
     */
    public synchronized void updateLineaProprieta(Color nuovo_collegamentocolore,
        int nuovo_collegamentospessore, int controllocollegamentotratteggio)
    {
    	
    	boolean bo = update.getStato();
    	if(bo){
    		update.informPreUpdate();
			update.disable();
    	}  
    	collegamentocolore = nuovo_collegamentocolore;
    	collegamentospessore = nuovo_collegamentospessore;
    	switch(controllocollegamentotratteggio)
    	{
    		case TYPE1_PATTERN:
    			tratteggiotipo = TYPE1_PATTERN;
    			collegamentotratteggio = TYPE1_LINK_DASH;
    			break;
    		case TYPE2_PATTERN:
    			tratteggiotipo = TYPE2_PATTERN;
    			collegamentotratteggio = TYPE2_LINK_DASH;
    			break;
    		default:
    			tratteggiotipo = SIMPLE_PATTERN;
    			collegamentotratteggio = SIMPLE_LINK_DASH;
//                  tratteggiotipo = TYPE1_PATTERN;
//                  collegamentotratteggio = TYPE1_LINK_DASH;
    			
    			break;
    	}
		if(bo){
			update.enabled();
			update.informPostUpdate();
		}  
    }


    /** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento.
        La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
        metodo restituisca 'true'. */
    public abstract boolean isSelezionato(Point pnt);


    /** Disegna il collegamento. */
    public abstract void paintGraficoLink(Graphics2D g2D);


    /** Restituisce il colore del link. */
    public Color getLineColor()
    {
        return collegamentocolore;
    }


    /** Restituisce lo spessore del link. */
    public int getLineWeight()
    {
        return collegamentospessore;
    }


    /** Restituisce il font del testo. */
    public String getTextFont()
    {
        return testofont;
    }


    /** Restituisce il colore del testo. */
    public Color getTextColor()
    {
        return testocolore;
    }

    /**
     * setta il colore del testo
     * @param color set color
     */
    public void setTextColor(Color color)
    {
    	boolean bo = update.testAndSet();
        this.testocolore = color;
        update.testAndReset(bo);
        
    }

    
    
    
    
    /** Restituisce la dimensione del font. */
    public int getFontSize()
    {
        return fontdimensione;
    }


    /** Restituisce lo stile del font. */
    public int getFontStyle()
    {
        return fontstile;
    }


    /** Restituisce il tipo di tratteggio. */
    public int getLineTheme()
    {
        return tratteggiotipo;
    }


    /** Restituisce la stringa di testo. */
    public String getText()
    {
       return update.getViewName(); //  testo;
   }

    /** Restituisce la stringa di testo. */
    public String getGuard()
    {
        return guardia;
    }
      
    /** Restituisce la stringa di testo. */
    public String getOperations()
    {
        return operazioni;
    }

    /** Imposta il link come selezionato/deselezionato. */
    public void setSelected(boolean ctrlIfSelected)
    {
        isSelected = ctrlIfSelected;
    }


	/** Restituisce true se il link ? stato selezionato, false altrimenti. */
    public boolean isSelected()
    {
        return isSelected;
    }

    
    /** Seleziona/deseleziona il link se deselezionato/selezionato. */
    public void invSelected()
    {
    	isSelected = !isSelected;
    }

    
    /** Visualizza i rettangoli rectSelFirst e rectSelSecond
    	quando il link ? stato selezionato. */
    public void paintSelected(Graphics2D g2d)
    {
        if (isSelected)
        {
        	g2d.setColor(Color.white);
        	g2d.fill(rectSelFirst);
        	g2d.fill(rectSelSecond);
        	g2d.setColor(Color.black);
        	g2d.draw(rectSelFirst);
        	g2d.draw(rectSelSecond);
        	
        }
    } 
    
    
    /** seleziona il link se è contenuto nel rettangolo */
    public boolean isInRect(Rectangle2D rettesterno)
    {
        if(rettesterno.contains(xstart,ystart) || rettesterno.contains(x_end,y_end) || rettesterno.intersectsLine(0, ystart, 100, ystart))
            return true;
        else
            return false;
    }    
    
    
	/** Restituisce il colore di default del link. */
	public static Color getDEFAULT_LINK_COLOR()
	{
		return DEFAULT_LINK_COLOR;
	}    


	/** Restituisce lo spessore di default del link. */
	public static int getDEFAULT_LINK_WEIGHT()
	{
		return DEFAULT_LINK_WEIGHT;
	} 

	
	/** Restituisce il pattern di default del link. */
	public static int getDEFAULT_LINK_PATTERN()
	{
		return DEFAULT_LINK_PATTERN;
	} 	    


	/** Restituisce il font di default del testo del link. */
	public static String getDEFAULT_TEXT_FONT()
	{
		return DEFAULT_TEXT_FONT;
	} 
	

	/** Restituisce il colore di default del testo. */
	public static Color getDEFAULT_TEXT_COLOR()
	{
		return DEFAULT_TEXT_COLOR;
	} 


	/** Restituisce la dimensione di default del testo del link. */
	public static int getDEFAULT_FONT_SIZE()
	{
		return DEFAULT_FONT_SIZE;
	} 


	/** Restituisce lo stile di default del font del testo. */
	public static int getDEFAULT_FONT_STYLE()
	{
		return DEFAULT_FONT_STYLE;
	}

	
	/* Imposta le propriet? di default. */
	public static void setDEFAULT_PROPERTIES(Color newLinkColor, int newLinkWeight, int newLinkPattern,
		String newTextFont, Color newTextColor, int newFontSize, int newFontStyle)
	{
		DEFAULT_LINK_COLOR = newLinkColor;
		DEFAULT_LINK_WEIGHT = newLinkWeight;
		DEFAULT_LINK_PATTERN = newLinkPattern;
		DEFAULT_TEXT_FONT = newTextFont;
		DEFAULT_TEXT_COLOR = newTextColor;
		DEFAULT_FONT_SIZE = newFontSize;
		DEFAULT_FONT_STYLE = newFontStyle;
	}  
		
}