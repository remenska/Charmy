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

import javax.swing.JComboBox;

import plugin.topologychannels.resource.graph.GraficoLink;


/** La classe specializza JComboBox introducendo l'inizializzazione con i pattern
    disponibili nella classe 'GraficoLink' per il tracciamento di una linea. */

public class JComboBoxPattern extends JComboBox
{

    /** Vettore dei pattern disponibili per il tracciamento di una linea. */
    static final int[] patterndisponibili = {GraficoLink.SIMPLE_PATTERN,
        GraficoLink.TYPE1_PATTERN, GraficoLink.TYPE2_PATTERN};


    /* Costruttore_ Prende in ingresso l'elemento da selezionare nella 'ComboBox'. */
    public JComboBoxPattern(int pattern)
    {
        super();

        int index = 0;

        addItem("Simple");
        addItem("Type 1");
        addItem("Type 2");
        for (int i=0; i < patterndisponibili.length; i++)
        {
            if (pattern == patterndisponibili[i])
            {
                index = i;
            }
        }
        setSelectedIndex(index);
    }


    /** Restituisce il pattern selezionato. */
    public int getSelectedPattern()
    {
       return(patterndisponibili[getSelectedIndex()]);
    }

}