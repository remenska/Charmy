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

import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;

/** Classe per gestire i nomi utilizzati per i link dei sequence diagram. */ 

public class ControlloNomeSeqLink extends RifToControl
{
	private plugin.statediagram.data.PlugData plugDataThread;

	private plugin.topologydiagram.data.PlugData plugDataTopology;

	/** Costruttore. */
	public ControlloNomeSeqLink(PlugDataManager pm)
	{
		super(pm);
		plugDataThread=(plugin.statediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.state");
		plugDataTopology=(plugin.topologydiagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.topology");
	}


	/** Restituisce il nome di un canale se ne esiste almeno uno, altrimenti null. */
	public String getNameOK()
	{
		String nomeCanale = plugDataTopology.getListaCanale().getAnyNameChannel();
		
		if (nomeCanale != null)
		{
			return nomeCanale;
		}
		else
		{
			return plugDataThread.getListaDP().getAnyNameChannel();
		}
	}

} 