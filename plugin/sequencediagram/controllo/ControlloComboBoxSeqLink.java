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
    


package plugin.sequencediagram.controllo;
import java.util.LinkedList;
import java.util.Vector;

import plugin.statediagram.data.PlugData;
import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;

/** Classe per gestire i nomi utilizzati per i link dei sequence diagram. */ 

public class ControlloComboBoxSeqLink extends RifToControl
{

	PlugData plugData;
	
	/** Costruttore. */
	public ControlloComboBoxSeqLink(PlugDataManager pd){
		//modifica ezio 2006
		super(pd);
		plugData=(PlugData)pd.getPlugData("charmy.plugin.state");
	}


	/** ... */
	public Vector getComboBoxList()
	{		
  		// Recupero tutti i messaggi definiti nei vari diagrammi di stato.
  		LinkedList listaNomi = null; 
		plugData=(plugin.statediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.state");  		
  		listaNomi =(plugData.getListaDP()).getAllMessageName(); //  .getMessageNameNoSym();
  		
  		// Aggiungo a tali messaggi i canali definiti nell'architettura.
  		//LinkedList listaNomiTopology = null;
  		//listaNomiTopology = (plugData.getListaCanale()).getAllMessageName();

  		// Elimino i nomi duplicati.
  		//for (int i=0; i<listaNomiTopology.size(); i++)
      	//{
	   //		String nome = (String)(listaNomiTopology.get(i));
	   	//	if (!(listaNomi.contains(nome))) 
	   	//	{
	   	//		listaNomi.add(nome);
	   	//	}
      	//}
      	
      	if (listaNomi != null)
      	{
			return (new Vector(listaNomi));
		}
		else
		{
			return (new Vector());
		}      			
	}

	public Vector getComboBoxList(String processFrom,String processTo)
	{		
		// Recupero tutti i messaggi definiti nei vari diagrammi di stato.
		LinkedList listaNomi = null;
		listaNomi =(plugData.getListaDP()).getAllMessageNameForProcess(processFrom,processTo);
		if (listaNomi != null){						
			return (new Vector(listaNomi));
		}
		else{
			return (new Vector());
		}      			
	}

	public Vector getComboBoxListTau(String process)
	{		
		// Recupero tutti i messaggi definiti nei vari diagrammi di stato.
		LinkedList listaNomi = null;
		listaNomi =(plugData.getListaDP()).getAllTauMessageNameForProcess(process);
		if (listaNomi != null){						
			return (new Vector(listaNomi));
		}
		else{
			return (new Vector());
		}      			
	}


} 