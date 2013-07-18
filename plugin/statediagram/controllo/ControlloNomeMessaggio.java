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
//import core.internal.runtime.data.ElementoMessaggio;

import java.util.LinkedList;

import core.internal.runtime.controllo.RifToControl;
import core.internal.runtime.data.PlugDataManager;
import plugin.statediagram.data.ListaMessaggio;

/** Classe utilizzata per verificare che, modificando il nome
	di un messaggio, non venga violato il seguente vincolo:
	il nome del messaggio deve iniziare con "?" o con "!". */ 

public class ControlloNomeMessaggio extends RifToControl
{

	/** Costruttore. */
	public ControlloNomeMessaggio(PlugDataManager pm){
		super(pm);
	}


        public boolean checkOK(String testo)
        {
            return true;
        }
	/** Il metodo restituisce true se il nuovo nome (newName) non
		viola i vincoli relativi al nome del messaggio. */
	public int checkOK(String testo,ListaMessaggio listaMessaggi)
	{
		return -1;
	}			

	public boolean checkSendReceivePairs(LinkedList listaSend,LinkedList listaReceive){
		
		return true;
	}

//    public int checkOKPromela(String testo,ListaMessaggio listaMessaggi)
//	{
//            String nome="";
//            int numPar=0;    
//            String testoOp[] = testo.split("\\(");
//            if(testoOp.length==1){
//                nome=testo;
//            }
//            else
//                if (testoOp.length==2){
//                    nome=testoOp[0];
//                    if (testoOp[1].endsWith(")"))
//                        testoOp[1]=testoOp[1].substring(0,testoOp[1].length()-1);
//                    
//                    String[] par=testoOp[1].split(",");
//                    numPar=par.length; 
//                }
//                else{
//                    String str =  	"Necessario modificare il nome del messaggio. \n" + 
//                            "La lista dei parametri deve essere contenuta tra le parentesi ()";	
//                    //JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
//                }
//            String nomeConfronto="";
//            String label="";
//            int numParConfronto=0;
//            for(int i=0;i<listaMessaggi.size();i++){
//                label=listaMessaggi.getElement(i).getName();
//                numParConfronto=((ElementoMessaggio)listaMessaggi.getElement(i)).getParameters().size();
//                if((label.equals(nome))&&(numPar==numParConfronto))
//                    return -1;
//                if (label.equals(nome))
//                    return numParConfronto;
//            }
//            return -1;
//        }	
} 