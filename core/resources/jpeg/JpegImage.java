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
    

package core.resources.jpeg;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**	Classe che gestisce le operazioni di creazione di un'immagine
	e salvataggio su file della medesima in formato jpeg. */
	
public class JpegImage
{

	public static final int maxImagePixels = 2000000;
	private BufferedImage Immagine = null;
    private Graphics2D imgGp2D = null;
    
	private RenderingHints qualityHints;    
    
    

    public JpegImage(int x, int y, int imgWidth, int imgHeight, double iScaleX, 
		double iScaleY, Color backgroundColor)
	{
		Rectangle2D imgRettangolo;
		Graphics imgGp;
		double imgScaleX;
		double imgScaleY;
		imgScaleX = iScaleX;
		imgScaleY = iScaleY;
		
		// Il while è stato introdotto per evitare l'errore di
		// OutOfMemory provocato da immagini molto grandi.
		while (imgScaleX*imgScaleY*imgWidth*imgHeight > maxImagePixels)
		{
			imgScaleX = imgScaleX*0.99;
			imgScaleY = imgScaleY*0.99;
		}
		
		Immagine = new BufferedImage(Math.round((float)(imgWidth*imgScaleX)),
			Math.round((float)(imgHeight*imgScaleY)),BufferedImage.TYPE_INT_RGB);
                //imgRettangolo = new Rectangle2D.Double(x,y,imgWidth,imgHeight);
		imgGp = Immagine.getGraphics();
                //Raster r;

                //r = Immagine.getData(imgRettangolo);
                imgGp2D = (Graphics2D)imgGp;
		//imgGp2D = (Graphics2D)r;
                
		qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		qualityHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		qualityHints.put(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		qualityHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_ENABLE);
		qualityHints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		qualityHints.put(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
		qualityHints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		imgGp2D.setRenderingHints(qualityHints);
		
		//imgRettangolo = new Rectangle2D.Double(x,y,imgWidth,imgHeight);
                imgRettangolo = new Rectangle2D.Double(0,0,imgWidth,imgHeight);
		imgGp2D.scale(imgScaleX,imgScaleY);
		imgGp2D.setColor(backgroundColor);
		imgGp2D.fill(imgRettangolo);
                imgGp2D.setClip(0,0,imgWidth,imgHeight);
	}

	/** Restituisce l'oggetto Graphics2D per "disegnare"
		elementi grafici sull'immagine. */ 
	public Graphics2D getImageGraphics2D()
	{
		return imgGp2D;
	}       		


	/** Apre una finestra di dialogo per la scelta del file
		dove salvare l'immagine, quindi esegue l'operazione. */
	public boolean saveImageFile(Frame topLevelAncestor)
	{
		File outFile;
    	int iPos;
		JpegEncoder jpg;
		String imgFile;
    	FileDialog fd;    	
    	
    	FileOutputStream dataOut = null;    			
		
		try
		{
			fd = new FileDialog(topLevelAncestor, "Save image in:", 1);
        	fd.show();
        	imgFile = fd.getFile();
        	if(imgFile != null)        	
			{			
        		iPos = imgFile.indexOf('.', 0);
        		if (iPos < 0)
        		{
        			imgFile = imgFile + ".jpg";	
        		}
        		else
        		{ 
                	imgFile = imgFile.substring(0,iPos) + ".jpg";       				
        		}
        		outFile = new File(fd.getDirectory()+imgFile);
				dataOut = new FileOutputStream(outFile);
				jpg = new JpegEncoder(Immagine, 100, dataOut);
				jpg.Compress();
				dataOut.close();
				imgFile = null;
				imgGp2D = null;
				Immagine = null;
				fd.dispose();
				return true;				
			}
			fd.dispose();
			return false;			
		}
		catch (Exception ex)
		{
			System.out.println("Non salvata!!");
			return false;
		}	
	}    
    
}