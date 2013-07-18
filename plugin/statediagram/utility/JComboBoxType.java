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
    

package plugin.statediagram.utility;
import javax.swing.JComboBox;

import plugin.topologydiagram.data.ElementoProcesso;
import plugin.statediagram.data.ElementoStato;


/** La classe specializza JComboBox introducendo l'inizializzazione con i tipi
    di processo disponibili nella classe 'ElementoProcesso'. */

public class JComboBoxType extends JComboBox
{

    /** Vettore dei tipi di processo disponibili nella classe 'ElementoProcesso'. */    
    static final int[] tipiProcesso = {ElementoProcesso.PROCESS, 
        ElementoProcesso.STORE};
        
    static final int[] tipiStato = {ElementoStato.START, ElementoStato.BUILD/*,
    	ElementoStato.END*/};

    private int[] tipidisponibili;
    
    /* Costruttore_ Prende in ingresso l'elemento da selezionare nella 'ComboBox'. */    
    public JComboBoxType(int type, int ProcessState)
    {
        super();

        int index = 0;
		
		if (ProcessState == 0)
        {
        	addItem("Process");
        	addItem("Store");
        	tipidisponibili = tipiProcesso;
        }
        else
        {
        	addItem("Start");
        	addItem("Build");
//        	addItem("End");
        	tipidisponibili = tipiStato;        	
        }
        for (int i=0; i < tipidisponibili.length; i++)
        {
            if (type == tipidisponibili[i])
            {
                index = i;
            }
        }
        setSelectedIndex(index);
    }

    
    /** Restituisce il tipo selezionato. */
    public int getSelectedType()
    {
       return(tipidisponibili[getSelectedIndex()]);
    }

}