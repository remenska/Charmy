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
    
package plugin.sequencediagram.graph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.sequencediagram.SequenceEditor;
import plugin.topologydiagram.resource.data.ElementoBase;
import plugin.topologydiagram.resource.graph.GraficoTipo;


/** La classe specializza la classe 'GraficoTipo' realizzando un rettangolo con
    una linea come bordo e testo al centro; le caratteristiche grafiche dell'oggetto 
    sono quelle usate nella rappresentazione delle classi nel Sequence Diagram_
    Al rettangolo si aggiunge una linea verticale che parte a met? del lato inferiore. */

public class GraficoTipoSequence extends GraficoTipo implements Serializable
{
    /** Larghezza del rettangolo. */
    private static int DEFAULT_WIDTH = 60;
    
    /** Altezza del rettangolo. */
    private static int DEFAULT_HEIGHT = 40;

    /** Colore di riempimento del rettangolo. */
    private static Color DEFAULT_BACKGROUND_COLOR = Color.white;

    /** Colore del testo. */
    private static Color DEFAULT_TEXT_COLOR = Color.black;
    
    /** Font del testo. */
    private static String DEFAULT_TEXT_FONT = "Times New Roman";

    /** Dimensione del font. */
    private static int DEFAULT_FONT_SIZE = 14;
    
    /** Stile del font. */
    private static int DEFAULT_FONT_STYLE = Font.PLAIN;

    /** Colore del bordo. */
    private static Color DEFAULT_LINE_COLOR = Color.black;
    
    /** Spessore del bordo. */
    private static int DEFAULT_LINE_WEIGHT =2;

    /** Spessore del bordo. */
    private static int LINE_WEIGHT = 1;
    
    
    /** Tipo di tratteggio standard */
    public static final float[] DASHED = {5.0f, 5.0f, 5.0f, 5.0f};
    
    /** Linea continua */
    public static final float[] NOT_DASHED = {10.0f, 0.0f, 10.0f, 0.0f};
    
    /** Memorizza il rettangolo. */
    transient private Rectangle2D rettangolo;
    
    /** Linea della vita. */
    transient private Line2D linea;


	/** Costruttore_
	 *   @param x0 : ascissa dell'estremo in alto a sinistra_
	 *   @param y0 : ordinata dell'estremo in alto a sinistra_
	 *   @param ElementoBase contenente l'id, il tipo e il nome 
	 */
    public GraficoTipoSequence(int x0, int y0,ElementoBase update)
    {
        super(x0,y0,DEFAULT_WIDTH,DEFAULT_HEIGHT, update);
        initVariables();
        testolayout = new TextLayout(update.getViewName(), new Font(getTextFont(),DEFAULT_FONT_STYLE,
        		getFontSize()),DEFAULT_FONTRENDERCONTEXT);
        testolimiti = testolayout.getBounds();
        larghezzatesto = testolimiti.getWidth();
        altezzatesto = testolimiti.getHeight();
        ctrlWidthAndHeight();
        testoX = (float)(getX() + (tmplarghezza - larghezzatesto)/2);
        testoY = (float)(getY() + (tmpaltezza + altezzatesto)/2);
        rettangolo = new Rectangle2D.Double(x0,y0,Math.round((float)tmplarghezza),
        		Math.round((float)tmpaltezza));
        setWidth(Math.round((float)tmplarghezza));
        setHeight(Math.round((float)tmpaltezza));
        linea = new Line2D.Double(x0+tmplarghezza/2,y0+tmpaltezza,x0+tmplarghezza/2,980);
        updateSelection();
    }
    
 
    /** Metodo privato per l'inizializzazione di alcune variabili. */
    private void initVariables()
    {
    	tmplarghezza = DEFAULT_WIDTH;
    	tmpaltezza = DEFAULT_HEIGHT;
    	setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
    	setTextColor(DEFAULT_TEXT_COLOR);
    	setTextFont(DEFAULT_TEXT_FONT);
    	setFontSize(DEFAULT_FONT_SIZE);
    	setFontStyle(DEFAULT_FONT_STYLE);
    	setLineColor(DEFAULT_LINE_COLOR);
    	setLineWeight(DEFAULT_LINE_WEIGHT);
    }

    
    /** Verifica che larghezza ed altezza rispettino i vincoli imposti 
        dalle dimensioni del testo. */
    protected void ctrlWidthAndHeight()
    {
    	if ((tmplarghezza - 2*(WIDTH_MARGIN+getLineWeight())) < larghezzatesto){
    		tmplarghezza = larghezzatesto + 2*(WIDTH_MARGIN+getLineWeight());
    		if ((Math.round((float)tmplarghezza))%WIDTH_STEP != 0){
    			tmplarghezza = (Math.round(tmplarghezza/WIDTH_STEP + 0.5)) * WIDTH_STEP;
    		}
    	}
    	if ((tmpaltezza - 2*(HEIGHT_MARGIN+getLineWeight())) < altezzatesto){
    		tmpaltezza = altezzatesto + 2*(HEIGHT_MARGIN+getLineWeight());
    		if ((Math.round((float)tmpaltezza))%HEIGHT_STEP != 0){
    			tmpaltezza = (Math.round(tmpaltezza/HEIGHT_STEP + 0.5)) * HEIGHT_STEP;
    		} 
    	}                
    }


	/** Metodo per aggiornare posizione e dimensioni del rettangolo e della linea di vita. */
    protected void setRectShape(int x, int y, int cwidth, int cheight)
    {
    	rettangolo.setRect(x,y,cwidth,cheight);
    	linea.setLine(x+cwidth/2.0,y+cheight,x+cwidth/2.0,y+cheight+4000);
    }

    
    /** Metodo per disegnare l'oggetto. */
    public void paintElementoGrafico(Graphics2D g2D)
    {
    	Stroke tmpstroke;

    	tmpstroke = g2D.getStroke();
    	g2D.setColor(getBackgroundColor());
    	g2D.fill(rettangolo);
    	g2D.setColor(getLineColor());
    	g2D.setStroke(new BasicStroke(getLineWeight()));
    	g2D.draw(rettangolo);
    	g2D.setStroke(new BasicStroke(LINE_WEIGHT,BasicStroke.CAP_ROUND,
    			BasicStroke.JOIN_ROUND,10.0f,DASHED,0));
    	g2D.draw(linea);
    	g2D.setColor(getTextColor());
    	g2D.setFont(new Font(getTextFont(),getFontStyle(),getFontSize()));
    	testolayout.draw(g2D,testoX,testoY);
    	g2D.setStroke(tmpstroke);
    	paintSelected(g2D);
    }


    /** Restituisce la larghezza di default dell'oggetto. */
    public static int getDEFAULT_WIDTH()
    {
    	return DEFAULT_WIDTH;
    }

    
    /** Restituisce l'altezza di default dell'oggetto. */
    public static int getDEFAULT_HEIGHT()
    {
    	return DEFAULT_HEIGHT;
    }


    /** Restituisce il colore di default di riempimento dell'oggetto. */
    public static Color getDEFAULT_BACKGROUND_COLOR()
    {
    	return DEFAULT_BACKGROUND_COLOR;
    }

    
    /** Restituisce il colore di default del testo. */
    public static Color getDEFAULT_TEXT_COLOR()
    {
    	return DEFAULT_TEXT_COLOR;
    }
    
    
    /** Restituisce il font di default del testo. */
    public static String getDEFAULT_TEXT_FONT()
    {
    	return DEFAULT_TEXT_FONT;
    }

    
    /** Restituisce la dimensione di default del font. */
    public static int getDEFAULT_FONT_SIZE()
    {
    	return DEFAULT_FONT_SIZE;
    }
    
    
    /** Restituisce lo stile di default del font. */
    public static int getDEFAULT_FONT_STYLE()
    {
    	return DEFAULT_FONT_STYLE;
    }

    
    /** Restituisce il colore di default del bordo. */
    public static Color getDEFAULT_LINE_COLOR()
    {
    	return DEFAULT_LINE_COLOR;
    }
    
    
    /** Restituisce lo spessore di default del bordo. */
    public static int getDEFAULT_LINE_WEIGHT()
    {
    	return DEFAULT_LINE_WEIGHT;
    }
    
    
    /** Imposta le propriet? di default. */
	public static void setDEFAULT_PROPERTIES(int newWidth, int newHeight, 
		Color newBackColor, String newTextFont, Color newTextColor, int newFontSize, 
		int newFontStyle, int newLineWeight, Color newLineColor)
	{
		DEFAULT_WIDTH = newWidth;
		DEFAULT_HEIGHT = newHeight;
		DEFAULT_BACKGROUND_COLOR = newBackColor;
		DEFAULT_TEXT_FONT = newTextFont;
		DEFAULT_TEXT_COLOR = newTextColor;
		DEFAULT_FONT_SIZE = newFontSize;
		DEFAULT_FONT_STYLE = newFontStyle;
		DEFAULT_LINE_WEIGHT = newLineWeight;
		DEFAULT_LINE_COLOR = newLineColor;
	}
	
	
    /** Verifica se il punto di coordinate (x,y) ? interno al grafico dell'elemento. */
    public boolean isInLine(int x, int y)
    {
    	int xMiddle = (this.getPointMiddle()).x;
    	return ((y>SequenceEditor.MARGINE_SUPERIORE)&&(x>=(xMiddle-5))&&(x<=(xMiddle+5)));
    }	    


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile()
	{
		rettangolo = new Rectangle2D.Double(getX(),getY(),getWidth(),getHeight());
		linea = new Line2D.Double(getX()+getWidth()/2,getY()+getHeight(),getX()+getWidth()/2,getY()+getHeight()+4000);        		
		super.restoreFromFile();
	}    
	
	
	public void setLine(int altezza)
	{
		linea.setLine(this.getPointMiddle().x,this.getY()+this.getHeight(),this.getPointMiddle().x,altezza);
	}

	/* (non-Javadoc)
	 * @see graph.ElementoBox#refresh()
	 */
	public void refresh() {
		restoreFromFile();
	}     
	    
}