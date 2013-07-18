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
    

package plugin.charmyui.utility.interfacce;

/**	Interfaccia_ Tutti i metodi che necessitano
	delle operazioni di "zoom" devono implementarla. */

public interface ZoomGraphInterface
{
    
    /**	Costante che determina il fattore di scala
    	per tutte le operazioni di "zoom". */  
    final double zoomFactor = 1.1;
    
    
    /**	Ripristina la visualizzazione dell'oggetto in scala 1:1. */
    void resetScale();
    
    
    /** Zoom dell'oggetto lungo l'asse X. */
    void incScaleX();


    /** Zoom negativo dell'oggetto lungo l'asse X. */    
    void decScaleX();
    

    /** Zoom dell'oggetto lungo l'asse Y. */    
    void incScaleY();
    
    
    /** Zoom negativo dell'oggetto lungo l'asse Y. */    
    void decScaleY(); 
    
}