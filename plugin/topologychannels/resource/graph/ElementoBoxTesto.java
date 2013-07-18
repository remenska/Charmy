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
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import plugin.topologychannels.resource.data.ElementoBase;






/** La classe specializza la classe 'ElementoBox inserendo una stringa
    al centro della forma rettangolare (ellittica)_
    Tale stringa verr� fatta coincidere con il 'nome' nella versione definitiva. */

public abstract class ElementoBoxTesto extends ElementoBox implements Serializable
{
    
    /** Larghezza della forma rettangolare: valore minimo. */
    public static final int minlarghezza = 40;
    
    /** Larghezza della forma rettangolare: valore massimo. */
    
    public static final int maxlarghezza = 200;
    
    /** Larghezza della forma rettangolare: risoluzione. */    
    public static final int steplarghezza = 20;

    /** Altezza della forma rettangolare: valore minimo. */    
    public static final int minaltezza = 20;
    
    /** Altezza della forma rettangolare: valore massimo. */    
    public static final int maxaltezza = 100;
    
    /** Altezza della forma rettangolare: risoluzione. */       
    public static final int stepaltezza = 10;
    
    /** Dimensione del font: valore minimo. */
    public static final int minfontdimensione = 8;

    /** Dimensione del font: valore massimo. */    
    public static final int maxfontdimensione = 20;

    /** Dimensione del font: risoluzione. */    
    public static final int stepfontdimensione = 2;
    
    /** Distanza minima tra il testo ed il bordo sinistro e destro. */
    public static final int WIDTH_MARGIN = 5;
    
    /** Distanza minima tra il testo ed il bordo superiore ed inferiore. */    
    public static final int HEIGHT_MARGIN = 5;

    /** La larghezza pu� essere aumentata con passo WIDTH_STEP. */ 
    public static final int WIDTH_STEP = 20;
    
    /** L'altezza pu� essere aumentata con passo HEIGHT_STEP. */     
    public static final int HEIGHT_STEP = 10;

    /** Necessario per l'implementazione!! */
    public static final FontRenderContext DEFAULT_FONTRENDERCONTEXT =
        new FontRenderContext(new AffineTransform(),true,true);
    
    /** Memorizza il colore del testo. */
    private Color testocolore;
    
    /** Memorizza il font usato per il testo. */
    private String testofont;

    /** Memorizza la dimensione del font. */
    private int fontdimensione;
    
    /** Memorizza lo stile del testo. */
    private int fontstile;
    
    
	/** Costruttore.
	 * @param x0,y0 coordinate x,y dell'elemento
	 * @param IUpdate funzione di informazione per gli update
	 *  */
    protected ElementoBoxTesto(int x0, int y0, int ilarghezza, int ialtezza, ElementoBase update)        
    {
        super(x0,y0,ilarghezza,ialtezza, update);
    }
    
    

    
    /** Restituisce il colore del testo. */
    public Color getTextColor()
    {
        return testocolore;
    }
    
    
    /** Permette di impostare il colore del testo. */
    public void setTextColor(Color nuovo_testocolore)
    {
    	boolean bo = informaPreUpdate();
        testocolore = nuovo_testocolore;
		informaPostUpdate(bo);
    }
    
    
    
    
    /** Restituisce il font usato per il testo. */
    public String getTextFont()
    {
        return testofont;
    }
    
    
    /** Permette di impostare il font del testo. */
    public void setTextFont(String nuovo_testofont)
    {
		boolean bo = informaPreUpdate();
        testofont = nuovo_testofont;
		informaPostUpdate(bo);
    }

    
    
    
    /** Restituisce la dimensione del font. */
    public int getFontSize()
    {
        return fontdimensione;
    }
    
    
    /** Imposta la dimensione del font. */
    public void setFontSize(int nuova_fontdimensione)
    {
		boolean bo = informaPreUpdate();
        fontdimensione = nuova_fontdimensione;
		informaPostUpdate(bo);
    } 
 
    

    /** Restituisce lo stile del font. */
    public int getFontStyle()
    {
        return fontstile;
    }
    
    
    /** Imposta lo stile del font. */
    public void setFontStyle(int nuovo_fontstile)
    {
		boolean bo = informaPreUpdate();
        fontstile = nuovo_fontstile;
		informaPostUpdate(bo);
    }

    
    /** Metodo astratto usato per aggiornare tutte le propriet� di un 'ElementoBoxTesto'. 
     * */
    public abstract void setAllParameters(int nuova_larghezza, int nuova_altezza, int nuovo_rettangoloX,
        int nuovo_rettangoloY, Color nuovo_lineacolore, int nuovo_lineaspessore,
        Color nuovo_sfondocolore,Color nuovo_testocolore,
        String nuovo_testofont, int nuova_fontdimensione, int nuovo_fontstile);    


	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */
	public void restoreFromFile()
	{
		super.restoreFromFile();
	}  

}