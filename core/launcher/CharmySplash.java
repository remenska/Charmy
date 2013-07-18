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
<<<<<<< CharmySplash.java
 */  
/* 
* @(#)MyAppSplash.java  1.2  2003-06-01 
* 
* Copyright (c) 1999-2003 Werner Randelshofer 
* Staldenmattweg 2, Immensee, CH-6405, Switzerland 
* All rights reserved. 
* 
* This material is provided "as is", with absolutely no warranty expressed 
* or implied. Any use is at your own risk. 
* 
* Permission to use or copy this software is hereby granted without fee, 
* provided this copyright notice is retained on all copies. 
*  
* modified and adapted by Patrizio Pelliccione <pellicci@di.univaq.it> 
*/

package core.launcher;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
//import java.net.URL;


/**
 * Demonstrates how to displays a splash window during startup of an application. 
 * Adapt this class to your liking but keep it small. 
 *
 * @author Werner Randelshofer, Staldenmattweg 2, Immensee, CH-6405, Switzerland. 
 * @version 1.2  2003-06-01 Revised. 
 */
 public class CharmySplash extends Object {   
 	public static void main(String[] args) {
	        // Read the image data and open the splash screen
		// ----------------------------------------------
		Frame splashFrame = null;
		//URL imageURL = MyAppSplash.class.getResource("charmySplash.jpeg");        
		//if (imageURL != null) {		
		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		
		//INSERIRE QUI IL PARSE DELLE FEATURE? - EZIO - configurazione piattaforma: icone, ect.. 
		// o vengono letti da un file di proprietà, oppure già contenute nel file feature.xml - quale scelta?????
		
		Image img = SystemKit.getImage(core.internal.datistatici.CharmyFile.createURLPath("core/internal/icon/"+core.internal.datistatici.CharmyFile.ABOUT_IMAGE,"Charmy.jar"));
		
		//ezio- Image img = SystemKit.getImage(TypeToolBar.createURLPath(core.internal.datistatici.CharmyFile.ABOUT_IMAGE));
		
		splashFrame = SplashWindow.splash(img );            
		//Toolkit.getDefaultToolkit().createImage(imageURL)                
		                   
		//} else {        
		//    System.err.println("Splash image not found");        
		//}
		// Call the main method of the application using Reflection.        
		// ---------------------------------------------------------        
		try {          
			if (Class.forName("core.launcher.Start")==null){           	
				return;           
			}           
			else{		       
				Class.forName("core.launcher.Start").getMethod("start", new Class[] {}/*{String[].class}*/).invoke(null, new Object[] {});               
			}                       
		} catch (Throwable e) {            
			e.printStackTrace();            
			System.err.flush();            
			System.exit(10);        
		}        
		// Dispose the splash window by disposing its parent frame        
		// -------------------------------------------------------        
		if (splashFrame != null)         	
			splashFrame.dispose();    
	}
}
