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

import plugin.sequencediagram.data.PlugData;
import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;

/** Classe utilizzata per verificare che, modificando il nome
	di un sequence, non venga violato il seguente vincolo:
	due sequence distinti non possono avere lo stesso nome. */

public class ControlloNomeSequence extends RifToControl
{

	private PlugData plugData;
	
	/** Costruttore. */
	public ControlloNomeSequence(PlugDataManager pm)
	{
		plugDataManager=pm;
		plugData=(plugin.sequencediagram.data.PlugData)plugDataManager.getPlugData("charmy.plugin.sequence");
	}

	
	/** Il metodo restituisce true se il nome del sequence (nomeSequence) non
		viola il vincolo relativo all'unicit? dei nomi di sequence_
		Metodo da usarsi con le operazioni di aggiunta di un nuovo sequence. */
	public boolean checkOkforNew(String nomeSequence)
	{
		
		if (nomeSequence.startsWith("cp_"))
		{	// Non ? consentito immettere nomi che cominciano con "cp_".
			// Questo vincolo consente di evitare i controlli durante
			// le operazioni di copia (clone) su un sequence.
			return false;
		}
		
		//return !(rifSequence.nomeSequenceGiaPresente(nomeSequence));
		PlugData pd=(PlugData)plugDataManager.getPlugData("charmy.plugin.sequence");
		if(pd!=null)
			return !(pd.getListaDS().giaPresente(nomeSequence));
		else		
			return false;			
	}


	/** Il metodo restituisce true se il nome del sequence (nomeSequence) non
		viola il vincolo relativo all'unicit? dei nomi di sequence_
		Metodo da usarsi con le operazioni di modifica propriet? di un sequence. */
	public boolean checkOkforProperties(String oldSequence, String newSequence)
	{

		if (oldSequence.equals(newSequence))
		{	// Se il nome non ? stato cambiato, sicuramente andr? bene!!
			return true;
		}
		
		if (newSequence.startsWith("cp_"))
		{	// Non ? consentito immettere nomi che cominciano con "cp_".
			// Questo vincolo consente di evitare i controlli durante
			// le operazioni di copia (clone) su un sequence.
			return false;
		}
		else
		{
			//return !(rifSequence.nomeSequenceGiaPresente(newSequence));
			return !(plugData.getListaDS().giaPresente(newSequence));
//	return false;
		}
	}
		
} 