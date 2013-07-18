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
import plugin.statediagram.data.ListaStato;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;

/** Classe utilizzata per verificare che, modificando il nome
	di uno stato, non venga violato il seguente vincolo:
	due stati distinti, all'interno dello stesso State Diagram 
	non possono avere lo stesso nome. */

public class ControlloNomeStato extends RifToControl
{

	/** Costruttore. */
	public ControlloNomeStato(PlugDataManager pm){
		super(pm);
	}


	/** Il metodo restituisce true se il nuovo nome (newName) non
	 *	viola il vincolo relativo all'unicit? dei nomi di uno stato
	 *	all'interno di uno State Diagram. 
	 * @param listaStato contente il nome da verificare
	 * @param vecchio nome
	 * @param nuovo nome	
	 ***/
	public boolean checkOK(ListaStato listaStato, String oldName, String newName)
	{
		boolean check_one;
	
		if (oldName.equals(newName)){	
			return true;
		}
		else{
			if (newName.startsWith("cp_")){	
				// Non e' consentito immettere nomi che cominciano con "cp_".
				// Questo vincolo consente di evitare i controlli durante
				// le operazioni di copia (cut&paste) su uno stato.
				return false;
			}
			if (newName.startsWith("state")){	
				int lunghezza = newName.length();
				if (lunghezza > 5){
					String numero = newName.substring(5,lunghezza);
					try{
						long intero = Long.parseLong(numero);
						if (intero > ElementoProcessoStato.getNumIstanze()){	
							// Non ? consentito immettere nomi del tipo stateX,
							// dove X ? un intero maggiore del numero di istanze
							// gi? create della classe ElementoProcessoStato.
							// Questo vincolo consente di evitare i controlli durante
							// le operazioni di inserimento di un nuovo stato.
							return false;
						}
					}
					catch (Exception ex){
						System.out.println("Controllo.ControlloNomeStato.checkOk: "+ex);
					}
				}
			}
			
			// Rimane da verificare che il nuovo nome non sia gi? utilizzato.			
			check_one = !(listaStato.giaPresente(newName));
			return check_one;
		}
	}
	
} 