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
    


package plugin.topologychannels.resource.utility;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import plugin.topologychannels.resource.data.ElementoProcessoStato;
import plugin.topologychannels.resource.data.ListaCanaleMessaggio;



/** Classe per gestire la posizione di un insieme di oggetti 
	di tipo ElementoProcessoStato appartenenti ad una LinkedList_
	Risulta particolarmente utile per lo spostamento 
	contemporaneo di tutti gli oggetti della lista. */

public class ListMovedObjects extends LinkedList
{

	/**	Restituisce il pi? piccolo rettangolo contenente
		tutti gli oggetti appartenenti alla LinkedList. */
	public Rectangle2D getExternalBounds()    
	{
    	ElementoProcessoStato tmpProcesso = null;
    	int topLeftX;
    	int topLeftY;
    	int bottomRightX;
    	int bottomRightY;
     	int tmptopLeftX;
    	int tmptopLeftY;
    	int tmpbottomRightX;
    	int tmpbottomRightY;
    	   	
    	if ((this != null) && (!this.isEmpty()))
    	{	
    		tmpProcesso = (ElementoProcessoStato)this.get(0);	
    		topLeftX = tmpProcesso.getTopX();
    		topLeftY = tmpProcesso.getTopY();
    		bottomRightX = topLeftX + tmpProcesso.getWidth();
    		bottomRightY = topLeftY + tmpProcesso.getHeight();
    		for (int j=1; j < this.size(); j++)
    		{
   				tmpProcesso = (ElementoProcessoStato)this.get(j);    			
    			tmptopLeftX = tmpProcesso.getTopX();
    			if (tmptopLeftX < topLeftX)
    			{
    				topLeftX = tmptopLeftX;
    			}
    			tmptopLeftY = tmpProcesso.getTopY();
    			if (tmptopLeftY < topLeftY)
    			{
    				topLeftY = tmptopLeftY;
    			}    			
    			tmpbottomRightX = tmptopLeftX + tmpProcesso.getWidth();
    			if (tmpbottomRightX > bottomRightX)
    			{
    				bottomRightX = tmpbottomRightX;
    			}
    			tmpbottomRightY = tmptopLeftY + tmpProcesso.getHeight();
    			if (tmpbottomRightY > bottomRightY)
    			{
    				bottomRightY = tmpbottomRightY;
    			}
    		}    			    			   				
    		return new Rectangle2D.Double(topLeftX,topLeftY,
    			bottomRightX-topLeftX,bottomRightY-topLeftY);    	
    	}
    	else
    	{
    		return null;
    	}
    }

    
	/** Restituisce come punto di riferimento della posizione degli oggetti
		l'estremo in alto a sinistra del primo elemento della lista_ 
		Questo permette di calcolare di quanto spostare gli oggetti della
		lista usando la posizione del mouse come secondo riferimento. */     
    public Point getRifPoint()
    {
    	ElementoProcessoStato tmpProcesso = null;
    	int topLeftX;
    	int topLeftY;
    	    	    	
   		if ((this != null) && (!this.isEmpty()))
    	{
    		tmpProcesso = (ElementoProcessoStato)this.get(0);    		
    		topLeftX = tmpProcesso.getTopX();
    		topLeftY = tmpProcesso.getTopY();    		
    		return new Point(topLeftX,topLeftY);
    	}
    	else
    	{
    		return null;
    	}
    } 
    
    
    /** Aggiorna di 'dX' e 'dY' la posizione di tutti gli elementi della
    	lista e degli oggetti di tipo CanaleMessaggio ad essi collegati 
    	(presi dalla lista passata come parametro d'ingresso). */
    public void updatePosition(Point rif, Point track, ListaCanaleMessaggio listCNL)
    {
    	ElementoProcessoStato tmpProcesso;
    	int topLeftX;
    	int topLeftY;
        int dX = track.x - rif.x;
        int dY = track.y - rif.y;
        
   		if ((this != null) && (!this.isEmpty()))
    	{
    		for (int j=0; j < this.size(); j++)
    		{    		    	    	
    			tmpProcesso = (ElementoProcessoStato)this.get(j);
    			topLeftX = tmpProcesso.getTopX() + dX;
    			topLeftY = tmpProcesso.getTopY() + dY;     			
    			tmpProcesso.setPoint(new Point(topLeftX,topLeftY));
    			listCNL.updateListaCanalePosizione(tmpProcesso);
    		}
    	}
    	else
    	{
    	}    	
    }       		
    
}