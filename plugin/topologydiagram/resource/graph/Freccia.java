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

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

// import java.io.Serializable;

/** La classe serve per costruire il grafico di una freccia_ 
    E' possibile realizzare tre tipi di freccia: una per i messaggi
    semplici, una per i sincroni, l'ultima per gli asincroni. */
  
public class Freccia implements IGraphElement
{
    /** Costante usata per i messaggi di tipo 'SIMPLE'. */
//    public static final int ForSimpleMessage = 0;
    
    /** Costante usata per i messaggi di tipo 'SYNCHRONOUS'. */    
    public static final int ForSynchronousMessage = 1;
    
    /** Costante usata per i messaggi di tipo 'ASYNCHRONOUS'. */     
    public static final int ForAsynchronousMessage = 2;

    /** Determina la dimensione della freccia lungo l'asse 'X'. */
    private static int semilarghezza = 8;
    
    /** Determina la dimensione della freccia lungo l'asse 'Y'. */    
    private static int semialtezza = 8;
    
    /** La freccia è implementata usando la classe 'GeneralPath'. */
    private GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);
    
    /** Memorizza l'ascissa del centro della freccia. */
    private int xf;
    
    /** Memorizza l'ordinata del centro della freccia. */
    private int yf;
    
    /** Memorizza il tipo di freccia. */
    private int tipofreccia;

    
    /** Costruttore_ Le coordinate 'x' e 'y' vengono usate come centro della freccia,
        il parametro 'tipo' per sceglierne la forma e l'argomento 'ctrldirezione' per
        stabilirne l'orientamento ('true' significa da sinistra verso destra)_ 
        Se il parametro 'tipo' contiene un valore non valido, si costruisce una freccia
        per messaggi di tipo 'SIMPLE'. */
    public Freccia(int x, int y, int tipo, int ctrldirezione)
    {
        int semiwidth;
        if (ctrldirezione < 2){
        	if (ctrldirezione == 0){
            	semiwidth = semilarghezza;
        	}
        	else{
            	semiwidth = - semilarghezza;
        	}
        	switch(tipo){
            	case ForSynchronousMessage:
                	tipofreccia = tipo;
                	gp.moveTo(x+semiwidth,y);
                	gp.lineTo(x-semiwidth,y-semialtezza);
                	gp.lineTo(x-semiwidth,y+semialtezza);
                	gp.closePath();
                	break;                
            	case ForAsynchronousMessage:
                	tipofreccia = tipo;
                	gp.moveTo(x+semiwidth,y);
                	gp.lineTo(x-semiwidth,y-semialtezza);
                	gp.lineTo(x-semiwidth,y);
                	gp.closePath();
                	break;
            	default:
                    tipofreccia = 1;
                	gp.moveTo(x+semiwidth,y);
                	gp.lineTo(x-semiwidth,y-semialtezza);
                	gp.lineTo(x-semiwidth,y+semialtezza);
                	gp.closePath();
                	break; 
        	}
    	}
    	else{
    		if (ctrldirezione == 2){
            	semiwidth = semilarghezza;    			
    		}
    		else{
            	semiwidth = - semilarghezza;    			
    		}
        	switch(tipo){
            	case ForSynchronousMessage:
         			tipofreccia = tipo;
                	gp.moveTo(x,y+semiwidth);
                	gp.lineTo(x-semialtezza,y-semiwidth);
                	gp.lineTo(x+semialtezza,y-semiwidth);
                	gp.closePath();
                	break;
            	case ForAsynchronousMessage:
        			tipofreccia = tipo;
                	gp.moveTo(x,y+semiwidth);
                	gp.lineTo(x-semialtezza,y-semiwidth);
                	gp.lineTo(x,y-semiwidth);
                	gp.closePath();
                	break;
            	default:
           			tipofreccia = 1;
                	gp.moveTo(x,y+semiwidth);
                	gp.lineTo(x-semialtezza,y-semiwidth);
                	gp.lineTo(x+semialtezza,y-semiwidth);
                	gp.closePath();
                	break;
        	}    		
    	}
        xf = x;
        yf = y;
    }
    

    /** Costruttore_ Le coordinate 'x' e 'y' vengono usate come centro della freccia,
        il parametro 'tipo' per sceglierne la forma_ Se il parametro 'tipo' contiene
        un valore non valido, si costruisce una freccia per messaggi di tipo 'SIMPLE'_
        Questo costruttore è utile per i messaggi con grafico tipo loop. */
    public Freccia(int x, int y, int tipo)
    {
        switch(tipo){
            case ForSynchronousMessage:
         		tipofreccia = tipo;
                gp.moveTo(x,y+semilarghezza);
                gp.lineTo(x-semialtezza,y-semilarghezza);
                gp.lineTo(x+semialtezza,y-semilarghezza);
                gp.closePath();
                break;
            case ForAsynchronousMessage:
        		tipofreccia = tipo;
                gp.moveTo(x,y+semilarghezza);
                gp.lineTo(x-semialtezza,y-semilarghezza);
                gp.lineTo(x,y-semilarghezza);
                gp.closePath();
                break;
            default:
                tipofreccia = ForSynchronousMessage;
                gp.moveTo(x,y+semilarghezza);
                gp.lineTo(x-semialtezza,y-semilarghezza);
                gp.lineTo(x,y+semilarghezza);
                gp.lineTo(x+semialtezza,y-semilarghezza);
                gp.closePath();
                break;
        }
        xf = x;
        yf = y;
    }
    
    
    /** Metodo per disegnare la freccia. */
    public void paintFreccia(Graphics2D g2D)
    {
        switch(tipofreccia){
            case ForSynchronousMessage:
                g2D.fill(gp);
                break;                
            case ForAsynchronousMessage:
                g2D.fill(gp);
                break;
            default:
                g2D.fill(gp);
                break;
        }        
    }
	    
}  