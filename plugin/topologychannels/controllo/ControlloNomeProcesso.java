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
    


package plugin.topologychannels.controllo;
import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;
import plugin.topologychannels.data.PlugData;
//import core.internal.runtime.data.ElementoProcessoStato;

/** Classe utilizzata per verificare che, modificando il nome
	di un processo, non venga violato il seguente vincolo:
	due processi distinti non possono avere lo stesso nome. */
public class ControlloNomeProcesso extends RifToControl
{
	
	private PlugData plugData;

	/** Costruttore. */
	public ControlloNomeProcesso(PlugDataManager pm){
		super(pm);
		plugDataManager=pm;
		plugData=(PlugData)pm.getPlugData("charmy.plugin.topology");
	}

	/** Il metodo restituisce true se il nuovo nome (newName) non
		viola il vincolo relativo all'unicit? dei nomi di processo. */
	public boolean checkOK(String oldName, String newName)
	{
		boolean check_one;
		boolean check_two;
		
		if (oldName.equals(newName)){	
			return true;
		}
		else{	
			//if (newName.startsWith("cp_")){	
				// Non e' consentito immettere nomi che cominciano con "cp_".
				// Questo vincolo consente di evitare i controlli durante
				// le operazioni di copia (cut&paste) su un processo.
			//	return false;
			//}		
//			if (newName.startsWith("process")){	
//				int lunghezza = newName.length();
//				if (lunghezza > 7){
//					String numero = newName.substring(7,lunghezza);
//					try{
//						long intero = Long.parseLong(numero);
//						if (intero > ElementoProcessoStato.getNumIstanze()){	
//							// Non e' consentito immettere nomi del tipo processX,
//							// dove X e' un intero maggiore del numero di istanze
//							// gia' create della classe ElementoProcessoStato.
//							// Questo vincolo consente di evitare i controlli durante
//							// le operazioni di inserimento di un nuovo processo.
//							return false;
//						}
//					}
//					catch (Exception ex){
//						System.out.println("Controllo.ControlloNomeProcesso.checkOk: "+ex);
//					}
//				}
//			}
			
			// Rimane da verificare che il nuovo nome non sia gi? utilizzato.			
			return !(plugData.getListaProcesso().giaPresente(newName));
		}
	}
} 