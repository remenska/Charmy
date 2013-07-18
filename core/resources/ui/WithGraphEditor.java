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
    

package core.resources.ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import javax.swing.JPanel;

import core.internal.plugin.file.FileManager;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;


/** Superclasse astratta di TopologyEditor, ThreadEditor e SequenceEditor_ 
	Queste sottoclassi dovranno implementare i metodi dell'interfaccia
	ZoomGraphInterface e EditGraphInterface. */

public abstract class WithGraphEditor extends JPanel implements ZoomGraphInterface,
	EditGraphInterface, Serializable
{

	protected FileManager fileManager;
    public static final int maxWidth = 4000;
    
    public static final int maxHeight = 4000;
    
    /** Definisce il tratteggio che delimita la zona di editing del pannello. */
    public static final float dash[] = {5.0f, 5.0f};
    
    /** Necessario per implementare il tratteggio. */
    transient protected BasicStroke bstroke = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash,0.0f);
    
    /** Rettangolo che delimita la zona di editing del pannello. */
    transient protected Rectangle2D rettangolo = null;
    
    /** Colore di background del pannello. */
    protected Color editorColor;     
    
    /** Fattore di scala per l'asse X. */
    protected double scaleX = 1;
    
    /** Fattore di scala per l'asse Y. */
    protected double scaleY = 1;
    
    /** Dimensione del pannello lungo l'asse X. */
    protected double sizeX = 1300;
    
    /** Dimensione del pannello lungo l'asse Y. */
    protected double sizeY = 1100;
    
    /** Dimensione della zona di editing lungo l'asse X. */
    protected int rWidth = 1200;
    
    /** Dimensione della zona di editing lungo l'asse Y. */
    protected int rHeight = 1000;

	
	/** Costruttore. */
	public WithGraphEditor(FileManager fileManager)
	{
		this.fileManager=fileManager;
        Dimension DimPanello = new Dimension(roundToInt(sizeX),roundToInt(sizeY));
        setPreferredSize(DimPanello);
        editorColor = Color.white;
        setBackground(editorColor);
        rettangolo = new Rectangle2D.Double(0,0,rWidth,rHeight);        		
	}
	

	/** Metodo privato per impostare le dimensioni del pannello. */
    private void updateSize()
    {   
        Dimension DimPanello = new Dimension(roundToInt(sizeX),roundToInt(sizeY));
        setPreferredSize(DimPanello);
        revalidate();
        repaint();
    } 
    
    
    /** Metodo per arrotondare un numero di tipo double ad un intero. */
    protected int roundToInt(double a)
    {
        return Math.round((float)a);
    }
    
        	        
    /** Ripristina la scala del pannello al 100%. */
    public void resetScale()
    {
        sizeX = rWidth + 100;
        sizeY = rHeight + 100;
        scaleX = 1;
        scaleY = 1;
        updateSize();
    }  

    
    /** Operazione di zoom sull'asse X. */
    public void incScaleX()
    {
        scaleX = scaleX * zoomFactor;
        sizeX = sizeX * zoomFactor;
        updateSize();        
    }


    /** Operazione di zoom negativo sull'asse X. */    
    public void decScaleX()
    {
        scaleX = scaleX / zoomFactor;
        sizeX = sizeX / zoomFactor;
        updateSize();
    }

    
    /** Operazione di zoom sull'asse Y. */    
    public void incScaleY()
    {
        scaleY = scaleY * zoomFactor;
        sizeY = sizeY * zoomFactor;
        updateSize();        
    }
    

    /** Operazione di zoom negativo sull'asse Y. */    
    public void decScaleY()
    {
        scaleY = scaleY / zoomFactor;
        sizeY = sizeY / zoomFactor;
        updateSize();
    }   

	
    /** Restituisce il colore di sfondo del pannello, */
    public Color getCEColor()
    {
        return editorColor;
    }
    
	
    /** Restituisce la larghezza del rettangolo all'interno
    	del quale si può realizzare il disegno. */
    public int getCEWidth()
    {
        return rWidth;
    }

 
    /** Restituisce l'altezza del rettangolo all'interno
    	del quale si può realizzare il disegno. */
    public int getCEHeight()
    {
        return rHeight;
    }
    
    
    /** Imposta le proprietà del pannello: altezza, larghezza e colore dello sfondo. */
    public void setCEProperties(int nWidth, int nHeight, Color nBGColor)
    {
        rWidth = nWidth;
        rHeight = nHeight;
        rettangolo.setRect(0,0,rWidth,rHeight);
        editorColor = nBGColor;
        setBackground(editorColor); 
        resetScale();
    } 
    
    
	/** Metodo per ricostruire la struttura delle classi a partire
		dalle informazioni memorizzate sul file. */    
	public void restoreFromFile()
	{
		bstroke = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash,0.0f);		
		rettangolo = new Rectangle2D.Double(0,0,rWidth,rHeight);
	}
	
        
    public abstract void opCopy();
    
    
    public abstract void opPaste();
    
    
    public abstract void opCut();
    
    
    public abstract void opDel();
    
    
    public abstract void opUndo();
    
    
    public abstract void opRedo(); 
    
    
	public abstract void opImg();


	public FileManager getFileManager() {
		return fileManager;
	}         
		    
}