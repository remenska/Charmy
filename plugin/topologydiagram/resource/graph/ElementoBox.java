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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.topologydiagram.resource.data.ElementoBase;



/** Questa classe specializza la classe 'ElementoGrafico' nel caso che  
    l'oggetto abbia una forma rettangolare o ellittica_ Si suppone che 
    tale forma abbia un bordo di spessore e colore variabile_
    E' inoltre possibile variare il colore di sfondo dell'oggetto_ 
    La classe non pu� essere utilizzata direttamente perch� non tutti
    i metodi astratti della classe 'ElementoGrafico' sono stati 
    implementati: la classe � astratta. */

public abstract class ElementoBox
	extends ElementoGrafico 
	implements Serializable
{

    /** Bordo del rettangolo: valore minimo. */
    public static final int minspessorelinea = 1;
    
    /** Bordo del rettangolo: valore massimo. */
    public static final int maxspessorelinea = 10;
    
    /** Bordo del rettangolo: risoluzione. */
    public static final int stepspessorelinea = 1;    
    
    /** Memorizza l'ascissa dell'estremo in alto a sinistra. */
    private int rettangoloX;
    
    /** Memorizza la larghezza della forma rettangolare. */
    private int larghezza;
    
    /** Memorizza l'altezza della forma rettangolare. */
    private int altezza;
    
    
    /** Memorizza lo spessore del bordo. */
    private int lineaspessore;
    
    /** Memorizza il colore di sfondo della forma rettangolare. */
    private Color sfondocolore;
    
    
    /** Memorizza il colore del bordo. */
    private Color lineacolore;
    
    
    
    transient private Rectangle2D rectForSelection = null;
    
    private boolean isSelected;
    
    transient private Rectangle2D rectSelTopLeft;
    
    transient private Rectangle2D rectSelBottomLeft;
    
    transient private Rectangle2D rectSelTopRight;
    
    transient private Rectangle2D rectSelBottomRight;    

    
    /** Costruttore. 
     * @param posizione angolo in alto a sinistra
     * @param posizione angolo in alto a sinistra
     * @param larghezza
     * @param altezza
     * @param ElementoBase relativo ai dati da disegnare
     * */
    protected ElementoBox(int x0, int y0, int plarghezza, int paltezza, ElementoBase update)        
    {
    	super(update);
    	rettangoloX = x0;
    	rettangoloY = y0;
    	larghezza = plarghezza;
    	altezza = paltezza;
    	isSelected = false;
    	rectForSelection = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
    	rectSelTopLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY-3,6,6);
    	rectSelBottomLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY+altezza-3,6,6);
    	rectSelTopRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY-3,6,6);
    	rectSelBottomRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);                            
    }
 
    
    /** Metodo per impostare la posizione dell'oggetto. 
     * @param nuovo punto x,y
     * */
    public abstract void setPoint(Point p);
    
    
    /** Imposta l'ascissa dell'estremo in alto a sinistra. 
     *  @param nuova posizione
     * */
    public void setX(int nuovo_rettangoloX)
    {
    	boolean bo = informaPreUpdate();
        rettangoloX = nuovo_rettangoloX;
		informaPostUpdate(bo);
    }
    
    
    /** Restituisce l'ascissa dell'estremo in alto a sinistra. 
     * @return posizione X attuale
     * */
    public int getX()
    {
        return rettangoloX;     
    }
    
    
    /** Restituisce l'ascissa dell'estremo in alto a sinistra come stringa. */
    public String getXAsString()
    {
        return String.valueOf(rettangoloX);
    }    
    

    /** Memorizza l'ordinata dell'estremo in alto a sinistra. */
    private int rettangoloY;
    
    
    /** Imposta l'ordinata dell'estremo in alto a sinistra. */
    public void setY(int nuovo_rettangoloY)
    {
    	boolean bo = informaPreUpdate();
        rettangoloY = nuovo_rettangoloY;
		informaPostUpdate(bo);
    }
    
    
    /**
     * settaggio rettangoli, genere un unico evento di update
     * @param rettangolo_X
     * @param rettangolo_Y
     */
    public void setXY(int rettangolo_X, int rettangolo_Y){
    	boolean bo = informaPreUpdate();
		rettangoloY = rettangolo_Y;
		rettangoloX = rettangolo_X;
		informaPostUpdate(bo);
    }
    
    
    
    /** Restituisce l'ordinata dell'estremo in alto a sinistra. */
    public int getY()
    {
        return rettangoloY; 
    }
    
    
    /** Restituisce l'ordinata dell'estremo in alto a sinistra come stringa. */
    public String getYAsString()
    {
        return String.valueOf(rettangoloY);
    }   
    
    
    /** Seleziona/deseleziona l'oggetto. */
    public void setSelected(boolean ctrlIfSelected)
    {
		//informaPreUpdate();
        isSelected = ctrlIfSelected;
		//informaPostUpdate();
    }

    
    /** Verifica se l'oggetto � stato selezionato. */
    public boolean isSelected()
    {
        return isSelected;
    } 
    
    
    /** Inverte lo stato selezionato/deselezionato. */
    public void invSelected()
    {
		//informaPreUpdate();
    	isSelected = !isSelected;
		//informaPostUpdate();
    }
    
    
    /** Se l'oggetto � stato selezionato, il metodo aggiorna la 
    	posizione dei rettangoli che evidenziano la selezione. */
    public void updateIfSelected()
    {
        if (isSelected)
        {
        	rectForSelection.setRect(rettangoloX,rettangoloY,larghezza,altezza);
        	rectSelTopLeft.setRect(rettangoloX-3,rettangoloY-3,6,6);
        	rectSelBottomLeft.setRect(rettangoloX-3,rettangoloY+altezza-3,6,6);
        	rectSelTopRight.setRect(rettangoloX+larghezza-3,rettangoloY-3,6,6);
        	rectSelBottomRight.setRect(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);        }
    }
    

    /** Il metodo aggiorna la posizione dei rettangoli che evidenziano la selezione. */
    public void updateSelection()
    {
    	rectForSelection.setRect(rettangoloX,rettangoloY,larghezza,altezza);
    	rectSelTopLeft.setRect(rettangoloX-3,rettangoloY-3,6,6);
    	rectSelBottomLeft.setRect(rettangoloX-3,rettangoloY+altezza-3,6,6);
    	rectSelTopRight.setRect(rettangoloX+larghezza-3,rettangoloY-3,6,6);
    	rectSelBottomRight.setRect(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);
    }
    
        
    /** "Stampa" i rettangoli che evidenziano la selezione. */
    public void paintSelected(Graphics2D g2d)
    {
        if (isSelected)
        {
        	g2d.setColor(Color.black);
        	g2d.draw(rectForSelection);
        	g2d.setColor(Color.white);
        	g2d.fill(rectSelTopLeft);
        	g2d.fill(rectSelBottomLeft);
        	g2d.fill(rectSelTopRight);
        	g2d.fill(rectSelBottomRight);
        	g2d.setColor(Color.black);
        	g2d.draw(rectSelTopLeft);
        	g2d.draw(rectSelBottomLeft);
        	g2d.draw(rectSelTopRight);
        	g2d.draw(rectSelBottomRight);
        	
        }
    }
    
    

    /** Restituisce la larghezza della forma rettangolare. */
    public int getWidth()
    {
        return larghezza;
    }
    
    
    /** Imposta la larghezza della forma rettangolare. */
    public void setWidth(int nuova_larghezza)
    {
    	boolean bo = informaPreUpdate();
        larghezza = nuova_larghezza;
		informaPostUpdate(bo);
    }
    


    
    /** Restituisce l'altezza della forma rettangolare. */
    public int getHeight()
    {
        return altezza;
    }
    
    
    /** Imposta l'altezza della forma rettangolare. */
    public void setHeight(int nuova_altezza)
    {
    	boolean bo = informaPreUpdate();
        altezza = nuova_altezza;
		informaPostUpdate(bo);
    }



    /** Restituisce il colore di sfondo della forma rettangolare. */
    public Color getBackgroundColor()
    {
        return sfondocolore;
    }
    
    
    /** Imposta il colore di sfondo della forma rettangolare. */
    public void setBackgroundColor(Color nuovo_sfondocolore)
    {
    	boolean bo = informaPreUpdate();
        sfondocolore = nuovo_sfondocolore;
		informaPostUpdate(bo);
    }


    /** Restituisce il colore del bordo. */
    public Color getLineColor()
    {
        return lineacolore;
    }
    
    
    /** Imposta il colore del bordo. */
    public void setLineColor(Color nuovo_lineacolore)
    {
    	boolean bo = informaPreUpdate();
        lineacolore = new Color(nuovo_lineacolore.getRed(),
        		nuovo_lineacolore.getGreen(),
        		nuovo_lineacolore.getBlue(),
        		nuovo_lineacolore.getAlpha()) ;
		informaPostUpdate(bo);
    }


    
    /** Restituisce lo spessore del bordo. */
    public int getLineWeight()
    {
        return lineaspessore;
    }
    
    
    /** Imposta lo spessore del bordo. */
    public void setLineWeight(int nuovo_lineaspessore)
    {
    	boolean bo = informaPreUpdate();
        lineaspessore = nuovo_lineaspessore;
		informaPostUpdate(bo);
    } 
    

    /** Restituisce il punto centrale della forma rettangolare. */
    public Point getPointMiddle()
    {
        Point temp = new Point();
        temp.x = Math.round((float)(rettangoloX + larghezza/2));
        temp.y = Math.round((float)(rettangoloY + altezza/2));       
        return temp;
    }


    /** Restituisce 'true' se il punto p si trova all'interno della forma
     *  rettangolare, 'false' altrimenti.
     * @param punto da controllare 
     */
    public boolean isIn(Point p)
    {
        
    	return isIn(p.x, p.y);
    	
    //	boolean cond1 = false;
     //   boolean cond2 = false; 
     //   cond1 = (p.x>=rettangoloX)&&(p.x<=(rettangoloX+larghezza));
     //   cond2 = (p.y>=rettangoloY)&&(p.y<=(rettangoloY+altezza));
     //   return (cond1&&cond2);
    }

    
    /** Restituisce 'true' se il punto di coordinate (x,y) si trova all'interno
        della forma rettangolare, 'false' altrimenti. */
    public boolean isIn(int x, int y)
    {
        boolean cond1 = false;
        boolean cond2 = false;
        cond1 = (x>=rettangoloX)&&(x<=(rettangoloX+larghezza));
        cond2 = (y>=rettangoloY)&&(y<=(rettangoloY+altezza));
        return (cond1&&cond2); 
    }
    
    
    /** Verifica se l'oggetto � contenuto nel rettangolo
    	passato come parametro d'ingresso. */
    public boolean isInRect(Rectangle2D rettangolo)
    {
    	Rectangle2D rectmp = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
		return rettangolo.contains(rectmp);    	
    }
    

	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */    
	public void restoreFromFile()
	{
        rectForSelection = new Rectangle2D.Double(rettangoloX,rettangoloY,larghezza,altezza);
        rectSelTopLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY-3,6,6);
        rectSelBottomLeft = new Rectangle2D.Double(rettangoloX-3,rettangoloY+altezza-3,6,6);
        rectSelTopRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY-3,6,6);
        rectSelBottomRight = new Rectangle2D.Double(rettangoloX+larghezza-3,rettangoloY+altezza-3,6,6);		
	}     
    
    
    /**
     * informa l'eleemnto grafico di ridisegnare il riquadro
     * e riattivare il layout grafico
     *
     */
    public abstract void refresh();
    
    
    
}