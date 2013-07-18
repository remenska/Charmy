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
    


package plugin.topologydiagram.graph;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import plugin.topologydiagram.resource.data.ElementoBase;
import plugin.topologydiagram.resource.graph.ElementoBox;



/** La classe specializza 'ElementoBox' realizzando un rettangolo con una linea
    come bordo. Questo grafico rappresenta un processo fittizio ('DUMMY'). */

public class GraficoTipoDummy
 	extends ElementoBox
	implements Serializable
{
    
    /** Larghezza del rettangolo. */
    private static final int DEFAULT_WIDTH = 12;
    
    /** Altezza del rettangolo. */
    private static final int DEFAULT_HEIGHT = 12;

    /** Colore di riempimento del rettangolo. */
    private static Color DEFAULT_BACKGROUND_COLOR = Color.gray;

    /** Colore del bordo. */
    private static Color DEFAULT_LINE_COLOR = Color.black;
    
    /** Spessore del bordo. */
    private static int DEFAULT_LINE_WEIGHT = 2;

    /** Memorizza le caratteristiche del rettangolo. */
    transient private Rectangle2D rettangolo;
    
    
	/** Costruttore_
	 *   @param x0 : ascissa dell'estremo in alto a sinistra_
	 *   @param y0 : ordinata dell'estremo in alto a sinistra_
	 *   @param ElementoBase contenente l'id, il tipo e il nome 
	 */
    public GraficoTipoDummy(int x0, int y0, ElementoBase update)
    {
        super(x0,y0,DEFAULT_WIDTH,DEFAULT_HEIGHT, update);
       	update.disable();
        //setName(txt);
        initVariables();
        rettangolo = new Rectangle2D.Double(x0,y0,DEFAULT_WIDTH,DEFAULT_HEIGHT);
        update.enabled();
    }


    /** Metodo privato per inizializzare alcune variabili. */
    private void initVariables()
    {
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        setLineColor(DEFAULT_LINE_COLOR);
        setLineWeight(DEFAULT_LINE_WEIGHT);
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
        g2D.setStroke(tmpstroke);
        paintSelected(g2D);
    }    


	/** Metodo per modificare la posizione dell'oggetto. */
    public void setPoint(Point p)
    {     
    	update.informPreUpdate();
    	update.disable();
        setX(p.x);
        setY(p.y);
        rettangolo.setRect(p.x,p.y,getWidth(),getHeight());
        updateIfSelected();
		
		update.enabled();
		update.informPostUpdate();
    }


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile()
	{
        rettangolo = new Rectangle2D.Double(getX(),getY(),DEFAULT_WIDTH,DEFAULT_HEIGHT);		
		super.restoreFromFile();
	}


	/* (non-Javadoc)
	 * @see graph.ElementoBox#refresh()
	 */
	public void refresh() {
		restoreFromFile();
	}     

}