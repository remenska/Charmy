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
    
package core.resources.utility;

import java.awt.Font;

import javax.swing.JComboBox;

/** La classe estende JComboBox introducendo l'inizializzazione con gli stili
	di testo disponibili in Java e selezionando quello passato come parametro. */

public class JComboBoxStyle extends JComboBox
{

    /** Vettore degli stili di testo disponibili. */
    static final int[] stilidisponibili = {Font.PLAIN, Font.ITALIC, Font.BOLD, Font.ITALIC+Font.BOLD};


    /* Costruttore_ Prende in ingresso l'elemento da selezionare nella 'ComboBox'. */
    public JComboBoxStyle(int stile)
    {
        super();

        int index = 0;

        addItem("Plain");
        addItem("Italic");
        addItem("Bold");
        addItem("Bold Italic");
        for (int i=0; i < stilidisponibili.length; i++)
        {
            if (stile == stilidisponibili[i])
            {
                index = i;
            }
        }
        setSelectedIndex(index);
    }


    /** Restituisce lo stile selezionato. */
    public int getSelectedStile()
    {
       return(stilidisponibili[getSelectedIndex()]);
    }

}