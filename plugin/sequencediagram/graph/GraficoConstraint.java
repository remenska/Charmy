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
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import javax.swing.JPanel;

import plugin.sequencediagram.data.ElementoConstraint;

/**
 * @author flamel 2005
 * Charmy plug-in
 * classe che disegna il constraint
 **/

public class GraficoConstraint 	extends JPanel 
	implements Serializable
{
    

    /** Ascissa del punto iniziale del constraint. */
    protected int xstart;

    /** Ordinata del punto iniziale del constraint. */
    protected int ystart;
    
    /** Ascissa lunghezza dell'ellisse. */
    protected int xend;

    /** altezza dell'elisse. */
    protected int yend;

    /** Variabile booleana che indica se e' incluso il punto di partenza del constraint. */
    transient protected boolean startIncluded;

    /** Memorizza booleana che indica se e' incluso il punto finale del constraint. */
    transient protected boolean endIncluded;

    /** Ascissa del punto in basso a sinistra del testo. */
    protected float testoX;

    /** Ordinata del punto in basso a sinistra del testo. */
    protected float testoY;
    
    /** True quando il constraint viene selezionato, false altrimenti. */ 
    protected boolean isSelected;
    
    /** Rettangolo di selezione. */
    transient protected Rectangle2D rectSel;      
    
    /** per la rappresentazione del constarint . */
    transient private Line2D.Double linea1;
    transient private Line2D.Double linea2;
    transient private Line2D.Double linea3;
    transient private Line2D.Double linea4;
    transient private Line2D.Double linea5;
    transient private Polygon freccia ;
    
    /** La parte iniziale del link ? rappresentata con un cerchio. */
    transient private Ellipse2D.Double cerchio;

    
     /** Memorizza un valore pari ad 1/3 della distanza tra i punti
        di coordinate (xstart,ystart) e (xend,yend). */
    private double p;
    
    /** Variabile per disegnare il testo. */
    transient protected TextLayout testolayout;
    
    /** Necessario per l'implementazione. */
    public static final FontRenderContext DEFAULT_FONTRENDERCONTEXT =
        new FontRenderContext(new AffineTransform(), true, true);
    
    /** Colore del testo. */
    protected static Color DEFAULT_TEXT_COLOR = Color.black;

    /** Font del testo. */
    protected static String DEFAULT_TEXT_FONT = "Times New Roman";

    /** Stile del font. */
    protected static int DEFAULT_FONT_STYLE = Font.TRUETYPE_FONT;

    /** Dimensione del font. */
    protected static int DEFAULT_FONT_SIZE = 11;
    

    /**Memorizza il tipo di constraint
     -2 past open
     -1 past closed
     0 present
     1 future closed
     2 future open
     3 chain closed past
     4 chain open past
     5 chain closed fut
     6 chain open fut
     7 unwanted closed past
     8 unwanted open past
     9 unwanted closed fut
     10 unwanted open fut**/
    private int Type;
    
    /** Riferimento al constraint */
    private ElementoConstraint constraintCorrente;

      
    /** Costruttore_
    	x1		: ascissa del punto iniziale del collegamento_
    	y1		: ordinata del punto iniziale del collegamento_
    	x2		: ascissa del punto finale del collegamento_
    	y2		: ordinata del punto finale del collegamento_
    	txt		: testo associato al collegamento_
        ec              : l'elemento da disegnare
        type            : identifica il tipo di constraint
        flussodiretto   : identifica il flusso del link a cui è stato applicato il constr*/
    public GraficoConstraint(int x1,int y1,int x2, int y2, String txt, ElementoConstraint ec,int type,boolean flussodiretto)
    {
        constraintCorrente=ec;
        xstart = x1;
        ystart = y1;
        xend = x2;
        yend = y2;
        Type=type;
        if(flussodiretto){   
            if(Type==-1)
            {
                    cerchio = new Ellipse2D.Double(xstart+20,ystart-6,12,12); 
                    testoX = (float)xstart+5;
                    testoY = (float)ystart+30; 
                    rectSel = new Rectangle2D.Double(xstart+20,ystart-6,12,12);
            }
            else if(Type==-2)
            {
                    cerchio = new Ellipse2D.Double(xstart+40,ystart-6,12,12); 
                    testoX = (float)xstart+40;
                    testoY = (float)ystart+20; 
                    rectSel = new Rectangle2D.Double(xstart+40,ystart-6,12,12);
            }
            else if(Type==0)
            {
                    int c = 15+(x1+(x2-x1)/2);
                    linea1 = new Line2D.Double(c-10,y1-8,c+10,y1+8);
                    linea2 = new Line2D.Double(c+10,y1-8,c-10,y1+8);
                    rectSel = new Rectangle2D.Double(c-2,y1-2,6,6);
                    testoX = (float)c-15;
                    testoY = (float)ystart+20;
            }        
            else if(Type==1)
            {
                    cerchio = new Ellipse2D.Double(x2-32,y2-6,12,12);  
                    testoX = (float)x2-42;
                    testoY = (float)ystart+20;
                    rectSel = new Rectangle2D.Double(x2-32,y2-6,12,12);

            }
            else if(Type==2)
            {
                    cerchio = new Ellipse2D.Double(x2-52,y2-6,12,12);  
                    testoX = (float)x2-62;
                    testoY = (float)ystart+30;
                    rectSel = new Rectangle2D.Double(x2-52,y2-6,12,12);

            }
            else if(Type==3 ||Type==7)
            {
                int[] xpoint= {xstart+30,xstart+30,xstart+30,xstart+40};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart+20,y1+6,xstart+30,y1+6);
                linea2 = new Line2D.Double(xstart+20,y1+10,xstart+30,y1+10); 
                if(Type==7 )
                    linea3 = new Line2D.Double(xstart+20,y1+15,xstart+30,y1+2); 
                rectSel = new Rectangle2D.Double(xstart+20,y1+2,20,12);
                testoX = (float)xstart+20;
                testoY = (float)ystart+30;
            }
            else if(Type==4 || Type==8)
            {
                int[] xpoint= {xstart+50,xstart+50,xstart+50,xstart+60};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart+40,y1+6,xstart+50,y1+6);
                linea2 = new Line2D.Double(xstart+40,y1+10,xstart+50,y1+10); 
                if( Type==8)
                    linea3 = new Line2D.Double(xstart+40,y1+15,xstart+50,y1+2); 
                rectSel = new Rectangle2D.Double(xstart+40,y1+2,20,12);
                testoX = (float)xstart+40;
                testoY = (float)ystart+20;
            }
            else if(Type==5||Type==9)
            {
                int[] xpoint ={x2-22,x2-22,x2-22,x2-12,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2-32,y1+6,x2-22,y1+6);
                linea2 = new Line2D.Double(x2-32,y1+10,x2-22,y1+10);
                if(Type==9)
                    linea3 = new Line2D.Double(x2-32,y1+15,x2-22,y1+2);
                rectSel = new Rectangle2D.Double(x2-32,y1+2,20,12);
                testoX = (float)x2-40;
                testoY = (float)ystart+20;
            }
            else if(Type==6 || Type==10)
            {
                int[] xpoint ={x2-42,x2-42,x2-42,x2-32,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2-52,y1+6,x2-42,y1+6);
                linea2 = new Line2D.Double(x2-52,y1+10,x2-42,y1+10);
                if(Type==10)
                    linea3 = new Line2D.Double(x2-52,y1+15,x2-42,y1+2);
                rectSel = new Rectangle2D.Double(x2-52,y1+2,20,12);
                testoX = (float)x2-50;
                testoY = (float)ystart+40;
            }
        }
        else{             //flusso non diretto
            if(Type==-1)
            {
                    cerchio = new Ellipse2D.Double(xstart-32,ystart-6,12,12);
                    testoX = (float)xstart-40;
                    testoY = (float)ystart+30;
                    rectSel = new Rectangle2D.Double(xstart-32,ystart-6,12,12);
                    
            }
            else if(Type==-2)
            {
                    cerchio = new Ellipse2D.Double(xstart-52,ystart-6,12,12);
                    testoX = (float)xstart-62;
                    testoY = (float)ystart+20;
                    rectSel = new Rectangle2D.Double(xstart-52,ystart-6,12,12);
                    
            }
            else if(Type==0)
            {
                    int c = (x1+(x2-x1)/2)-20;
                    linea1 = new Line2D.Double(c+10,y1-8,c-10,y1+8);
                    linea2 = new Line2D.Double(c-10,y1-8,c+10,y1+8);
                    rectSel = new Rectangle2D.Double(c,y1-2,6,6);
                    testoX = (float)c+25;
                    testoY = (float)ystart+20;
            }        
            else if(Type==1)
            {
                    cerchio = new Ellipse2D.Double(x2+20,y2-6,12,12);   
                    testoX = (float)x2+20;
                    testoY = (float)ystart+20;
                    rectSel = new Rectangle2D.Double(x2+20,y2-6,12,12);

            }
            else if(Type==2)
            {
                    cerchio = new Ellipse2D.Double(x2+40,y2-6,12,12);   
                    testoX = (float)x2+40;
                    testoY = (float)ystart+30;
                    rectSel = new Rectangle2D.Double(x2+40,y2-6,12,12);

            }
            else if(Type==3 ||Type==7)
            {
                int[] xpoint= {xstart-30,xstart-30,xstart-30,xstart-40};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart-20,y1+6,xstart-30,y1+6);
                linea2 = new Line2D.Double(xstart-20,y1+10,xstart-30,y1+10);  
                if(Type==7)
                    linea3 = new Line2D.Double(xstart-20,y1+15,xstart-30,y1+2); 
                rectSel = new Rectangle2D.Double(xstart-40,y1+2,20,12);
                testoX = (float)xstart-30;
                testoY = (float)ystart+30;
                
            }
            else if(Type==4 || Type==8)
            {
                int[] xpoint= {xstart-50,xstart-50,xstart-50,xstart-60};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart-40,y1+6,xstart-50,y1+6);
                linea2 = new Line2D.Double(xstart-40,y1+10,xstart-50,y1+10);  
                if(Type==8)
                    linea3 = new Line2D.Double(xstart-40,y1+15,xstart-50,y1+2); 
                rectSel = new Rectangle2D.Double(xstart-60,y1+2,20,12);
                testoX = (float)xstart-60;
                testoY = (float)ystart+20;
                
            }
            else if(Type==5 ||Type==9 )
            {
                int[] xpoint ={x2+22,x2+22,x2+22,x2+12,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2+32,y1+6,x2+22,y1+6);
                linea2 = new Line2D.Double(x2+32,y1+10,x2+22,y1+10);
                if(Type==9)
                    linea3 = new Line2D.Double(x2+32,y1+15,x2+22,y1+2);
                rectSel = new Rectangle2D.Double(x2+12,y1+2,20,12);
                testoX = (float)x2+12;
                testoY = (float)ystart+20;
            }
            else if( Type==6 || Type==10)
            {
                int[] xpoint ={x2+42,x2+42,x2+42,x2+32,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2+52,y1+6,x2+42,y1+6);
                linea2 = new Line2D.Double(x2+52,y1+10,x2+42,y1+10);
                if(Type==10)
                    linea3 = new Line2D.Double(x2+52,y1+15,x2+42,y1+2);
                rectSel = new Rectangle2D.Double(x2+32,y1+2,20,12);
                testoX = (float)x2+20;
                testoY = (float)ystart+30;
            }
            
            
        }

      testolayout = new TextLayout(constraintCorrente.getLabel(), new Font(DEFAULT_TEXT_FONT,DEFAULT_FONT_STYLE,DEFAULT_FONT_SIZE),DEFAULT_FONTRENDERCONTEXT);
                  
    }

    /** Metodo per aggiornare la posizione del constraint*/
    public void updateConstraintPosizione(int x1,int y1,int x2, int y2,int type,boolean flussodiretto)
    {
        xstart = x1;
        ystart = y1;
        xend = x2;
        yend = y2;
        Type = type;        
        if(flussodiretto){
            if(Type==-1)
            {
                    cerchio = new Ellipse2D.Double(xstart+20,ystart-6,12,12); 
                    testoX = (float)xstart+5;
                    testoY = (float)ystart+30; 
                    rectSel = new Rectangle2D.Double(xstart+20,ystart-6,12,12);
            }
            else if(Type==-2)
            {
                    cerchio = new Ellipse2D.Double(xstart+40,ystart-6,12,12); 
                    testoX = (float)xstart+40;
                    testoY = (float)ystart+20; 
                    rectSel = new Rectangle2D.Double(xstart+40,ystart-6,12,12);
            }
            else if(Type==0)
            {
                    int c = 15+(x1+(x2-x1)/2);
                    linea1 = new Line2D.Double(c-10,y1-8,c+10,y1+8);
                    linea2 = new Line2D.Double(c+10,y1-8,c-10,y1+8);
                    rectSel = new Rectangle2D.Double(c-2,y1-2,6,6);
                    testoX = (float)c-15;
                    testoY = (float)ystart+20;
            }        
            else if(Type==1)
            {
                    cerchio = new Ellipse2D.Double(x2-32,y2-6,12,12);  
                    testoX = (float)x2-42;
                    testoY = (float)ystart+20;
                    rectSel = new Rectangle2D.Double(x2-32,y2-6,12,12);

            }
            else if(Type==2)
            {
                    cerchio = new Ellipse2D.Double(x2-52,y2-6,12,12);  
                    testoX = (float)x2-62;
                    testoY = (float)ystart+30;
                    rectSel = new Rectangle2D.Double(x2-52,y2-6,12,12);

            }
            else if(Type==3 ||Type==7)
            {
                int[] xpoint= {xstart+30,xstart+30,xstart+30,xstart+40};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart+20,y1+6,xstart+30,y1+6);
                linea2 = new Line2D.Double(xstart+20,y1+10,xstart+30,y1+10); 
                if(Type==7 )
                    linea3 = new Line2D.Double(xstart+20,y1+15,xstart+30,y1+2); 
                rectSel = new Rectangle2D.Double(xstart+20,y1+2,20,12);
                testoX = (float)xstart+20;
                testoY = (float)ystart+30;
            }
            else if(Type==4 || Type==8)
            {
                int[] xpoint= {xstart+50,xstart+50,xstart+50,xstart+60};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart+40,y1+6,xstart+50,y1+6);
                linea2 = new Line2D.Double(xstart+40,y1+10,xstart+50,y1+10); 
                if( Type==8)
                    linea3 = new Line2D.Double(xstart+40,y1+15,xstart+50,y1+2); 
                rectSel = new Rectangle2D.Double(xstart+40,y1+2,20,12);
                testoX = (float)xstart+40;
                testoY = (float)ystart+20;
            }
            else if(Type==5||Type==9)
            {
                int[] xpoint ={x2-22,x2-22,x2-22,x2-12,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2-32,y1+6,x2-22,y1+6);
                linea2 = new Line2D.Double(x2-32,y1+10,x2-22,y1+10);
                if(Type==9)
                    linea3 = new Line2D.Double(x2-32,y1+15,x2-22,y1+2);
                rectSel = new Rectangle2D.Double(x2-32,y1+2,20,12);
                testoX = (float)x2-40;
                testoY = (float)ystart+20;
            }
            else if(Type==6 || Type==10)
            {
                int[] xpoint ={x2-42,x2-42,x2-42,x2-32,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2-52,y1+6,x2-42,y1+6);
                linea2 = new Line2D.Double(x2-52,y1+10,x2-42,y1+10);
                if(Type==10)
                    linea3 = new Line2D.Double(x2-52,y1+15,x2-42,y1+2);
                rectSel = new Rectangle2D.Double(x2-52,y1+2,20,12);
                testoX = (float)x2-50;
                testoY = (float)ystart+40;
            }
        }
        else{
            if(Type==-1)
            {
                    cerchio = new Ellipse2D.Double(xstart-32,ystart-6,12,12);
                    testoX = (float)xstart-40;
                    testoY = (float)ystart+30;
                    rectSel = new Rectangle2D.Double(xstart-32,ystart-6,12,12);
                    
            }
            else if(Type==-2)
            {
                    cerchio = new Ellipse2D.Double(xstart-52,ystart-6,12,12);
                    testoX = (float)xstart-62;
                    testoY = (float)ystart+20;
                    rectSel = new Rectangle2D.Double(xstart-52,ystart-6,12,12);
                    
            }
            else if(Type==0)
            {
                    int c = (x1+(x2-x1)/2)-20;
                    linea1 = new Line2D.Double(c+10,y1-8,c-10,y1+8);
                    linea2 = new Line2D.Double(c-10,y1-8,c+10,y1+8);
                    rectSel = new Rectangle2D.Double(c,y1-2,6,6);
                    testoX = (float)c+25;
                    testoY = (float)ystart+20;
            }        
            else if(Type==1)
            {
                    cerchio = new Ellipse2D.Double(x2+20,y2-6,12,12);   
                    testoX = (float)x2+20;
                    testoY = (float)ystart+20;
                    rectSel = new Rectangle2D.Double(x2+20,y2-6,12,12);

            }
            else if(Type==2)
            {
                    cerchio = new Ellipse2D.Double(x2+40,y2-6,12,12);   
                    testoX = (float)x2+40;
                    testoY = (float)ystart+30;
                    rectSel = new Rectangle2D.Double(x2+40,y2-6,12,12);

            }
            else if(Type==3 ||Type==7)
            {
                int[] xpoint= {xstart-30,xstart-30,xstart-30,xstart-40};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart-20,y1+6,xstart-30,y1+6);
                linea2 = new Line2D.Double(xstart-20,y1+10,xstart-30,y1+10);  
                if(Type==7)
                    linea3 = new Line2D.Double(xstart-20,y1+15,xstart-30,y1+2); 
                rectSel = new Rectangle2D.Double(xstart-40,y1+2,20,12);
                testoX = (float)xstart-30;
                testoY = (float)ystart+30;
                
            }
            else if(Type==4 || Type==8)
            {
                int[] xpoint= {xstart-50,xstart-50,xstart-50,xstart-60};
                int[] ypoint= {y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(xstart-40,y1+6,xstart-50,y1+6);
                linea2 = new Line2D.Double(xstart-40,y1+10,xstart-50,y1+10);  
                if(Type==8)
                    linea3 = new Line2D.Double(xstart-40,y1+15,xstart-50,y1+2); 
                rectSel = new Rectangle2D.Double(xstart-60,y1+2,20,12);
                testoX = (float)xstart-60;
                testoY = (float)ystart+20;
                
            }
            else if(Type==5 ||Type==9 )
            {
                int[] xpoint ={x2+22,x2+22,x2+22,x2+12,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2+32,y1+6,x2+22,y1+6);
                linea2 = new Line2D.Double(x2+32,y1+10,x2+22,y1+10);
                if(Type==9)
                    linea3 = new Line2D.Double(x2+32,y1+15,x2+22,y1+2);
                rectSel = new Rectangle2D.Double(x2+12,y1+2,20,12);
                testoX = (float)x2+12;
                testoY = (float)ystart+20;
            }
            else if( Type==6 || Type==10)
            {
                int[] xpoint ={x2+42,x2+42,x2+42,x2+32,};
                int[] ypoint ={y1+2,y1+14,y1+14,y1+8};
                freccia = new Polygon(xpoint,ypoint,4);
                linea1 = new Line2D.Double(x2+52,y1+6,x2+42,y1+6);
                linea2 = new Line2D.Double(x2+52,y1+10,x2+42,y1+10);
                if(Type==10)
                    linea3 = new Line2D.Double(x2+52,y1+15,x2+42,y1+2);
                rectSel = new Rectangle2D.Double(x2+32,y1+2,20,12);
                testoX = (float)x2+20;
                testoY = (float)ystart+30;
            }
            
            
        }
        
        testolayout = new TextLayout(constraintCorrente.getLabel(), new Font(DEFAULT_TEXT_FONT,DEFAULT_FONT_STYLE,DEFAULT_FONT_SIZE),DEFAULT_FONTRENDERCONTEXT);

    }
    
    /** Visualizza il rettangolo di selezione
    	quando il link ? stato selezionato. */
    public void paintSelected(Graphics2D g2d)
    {
        if (isSelected)
        {          
            g2d.draw(rectSel);

        }
    } 


    /** Verifica se il punto 'pnt' ? sufficientemente vicino al 'centro' del collegamento.
        La costante 'delta' determina quanto il punto 'pnt' deve stare vicino perch? il
        metodo restituisca 'true'. */
    public boolean isSelezionato(Point pnt)
    {
        return rectSel.contains(pnt);
    }


    /** Disegna il constraint. */
    public void paintGraficoConstraint(Graphics2D g2D)
    {
        Stroke tmpStroke;
        Paint tmpPaint;
        AffineTransform tmpTransform;
        tmpStroke = g2D.getStroke();
        tmpPaint = g2D.getPaint();
        tmpTransform = g2D.getTransform();

        g2D.setColor(Color.BLACK);
        if(Type == 0)
        {
            g2D.draw(linea1);
            g2D.draw(linea2);
        }
        else if(Type ==-1 || Type==1)  //essendo closed lo riempio
        {   
            g2D.fill(cerchio);
            g2D.draw(cerchio);                  
        }
        else if(Type ==-2 || Type==2) //esendo open lo lascio vuoto
        {   
            g2D.draw(cerchio);                  
        }
        else if(Type ==4 || Type==6 || Type==8 || Type==10 ) //esendo open lo lascio vuoto
        {   
            g2D.draw(linea1);  
            g2D.draw(linea2); 
            if(Type==8 || Type==10) g2D.draw(linea3);
            g2D.drawPolygon(freccia);
            
        }
        else if(Type ==3 || Type==5 || Type==7 || Type==9) //esendo closed lo riempio
        {   
            g2D.draw(linea1);  
            g2D.draw(linea2); 
            if(Type==7 || Type==9) g2D.draw(linea3);
            g2D.fillPolygon(freccia);
            g2D.drawPolygon(freccia);
        }
        g2D.setColor(DEFAULT_TEXT_COLOR);
        g2D.setFont(new Font(DEFAULT_TEXT_FONT,DEFAULT_FONT_STYLE,DEFAULT_FONT_SIZE));
        if (testolayout!=null)testolayout.draw(g2D,testoX,testoY);        
        g2D.setStroke(tmpStroke);
        paintSelected(g2D);        
        g2D.setPaint(tmpPaint);
        g2D.setTransform(tmpTransform);
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
 
    
    /** A COSA SERVE?? */
    public boolean isInRect(Rectangle2D rettesterno)
    {
    	return true;
    }     
    
    
    /** Metodo per ricostruire la struttura delle classi a partire
            dalle informazioni memorizzate sul file. */    
    public void restoreFromFile()
    {		
               
        if(Type==-1)
        {
                cerchio = new Ellipse2D.Double(xstart+20,ystart-6,12,12); 
        }
        else if(Type==0)
        {
                int c = 15+(xstart+(xend-xstart)/2);
                linea1 = new Line2D.Double(c-10,ystart-8,c+10,ystart+8);
                linea2 = new Line2D.Double(c+10,ystart-8,c-10,ystart+8);
                rectSel = new Rectangle2D.Double(c-2,ystart-2,4,4);
        }        
        else if(Type==1)
        {
                cerchio = new Ellipse2D.Double(xend-32,yend-6,12,12);       
               
        } 

        constraintCorrente.updateConstraintPosizione();
        validate();
        repaint();
        }     		
    }