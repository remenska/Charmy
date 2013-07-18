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
    
package plugin.statediagram.controllo;

import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;
import plugin.statediagram.data.PlugData;

/** Classe utilizzata per verificare che, modificando il nome
	di un thread, non venga violato il seguente vincolo:
	due thread distinti ma relativi allo stesso processo non 
	possono avere lo stesso nome. */

public class ControlloNomeThread extends RifToControl
{

	PlugData plugData;
	PlugDataManager plugDataManager;
	
	/** Costruttore. */
	public ControlloNomeThread(PlugDataManager plugDataManager){		
		super(plugDataManager);
		this.plugDataManager=plugDataManager;
	}


	/** Il metodo restituisce true se il nome del Thread (nomeThread) non
		viola il vincolo relativo all'unicità dei nomi di thread_
		Metodo da usarsi con le operazioni di aggiunta di un nuovo thread. */
	public boolean checkOkforNew(String nomeProcesso, String nomeThread)
	{
		//return true;
//		if (nomeThread.startsWith("cp_"))
//		{	// Non è consentito immettere nomi che cominciano con "cp_".
//			// Questo vincolo consente di evitare i controlli durante
//			// le operazioni di copia (clone) su un thread.
//			return false;
//		}
//		
//		//return !((rifState.getListaThread(nomeProcesso)).nomeGiaPresente(nomeThread));
		plugData=(plugin.statediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.state");
		if(plugData.getListaDP().getListaThread(nomeProcesso)==null){
			return true;
		}
		return !((plugData.getListaDP().getListaThread(nomeProcesso)).nomeGiaPresente(nomeThread));
						
	}


	/** Il metodo restituisce true se il nome del Thread (nomeThread) non
		viola il vincolo relativo all'unicità dei nomi di thread_
		Metodo da usarsi con le operazioni di modifica proprietà di un thread. */
	public boolean checkOkforProperties(String oldProcesso, String newProcesso, 
		String oldThread, String newThread)
	{
	//	return true;
	plugData=(plugin.statediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.state");
	if(plugData.getListaDP().getListaThread(newProcesso)==null){
		return true;
	}

		if (oldThread.equals(newThread))
		{
			if (oldProcesso.equals(newProcesso))
			{
				return true;
			}
			else
			{
				return !((plugData.getListaDP().getListaThread(newProcesso)).nomeGiaPresente(newThread));				
			}
		}
		
			if(plugData.getListaDP()!=null)
				if(plugData.getListaDP().getListaThread(newProcesso)!=null)
					return !((plugData.getListaDP().getListaThread(newProcesso)).nomeGiaPresente(newThread));
				else{
				}
					
			else{}
				
			return true;	
			//return !((rifState.getListaThread(newProcesso)).nomeGiaPresente(newThread));
	}
	
} 