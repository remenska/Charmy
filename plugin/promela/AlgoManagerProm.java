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
    

package plugin.promela;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

import core.internal.runtime.data.IPlugData;


/** Classe per la gestione degli algoritmi di conversione
	per calcolare le formule LTL e la specifica Promela_
	Non ancora implementata!! */

public class AlgoManagerProm
{
		
	/** Memorizza il riferimento alla finestra "Output: Promela". */
	private PromelaWindow rifPromela; 
	
	private PromelaSpecified risPromela;
	
	//private ControlloIntegrita check;
	
	private IPlugData plugData;
	
	
	/** Costruttore. */
	public AlgoManagerProm (IPlugData plugDt, PromelaWindow rifPrW){
		plugData = plugDt;
		rifPromela = rifPrW;
//		check = new ControlloIntegrita();
	}
	
	
	/** Metodo per attivare il calcolo della specifica Promela. */
	public void Promela(int tipoAlgo)
	{
        //check.resetError();
        //non serve piï¿½ la sincronizzazione
        
        //if (check.checkIntegrity()){		
			risPromela = new PromelaSpecified(plugData,tipoAlgo);			
            rifPromela.setPromelaSpecified1(risPromela.getPromela1());
		//}
		//else{
		//	showError();
		//}	
	}
	
	
	
	public void SavePromela()
	{
		FileDialog fd;
		String strFile;
		FileOutputStream fos;
		DataOutputStream dos;
		LinkedList llPromela1;
                LinkedList llPromela2;
		String pathFile;
		String row;
		
		try
		{
//			TODO Charmy v2.0
			fd = new FileDialog((Frame)(rifPromela.getTopLevelAncestor()), "Save Promela:", 1);
       		fd.show();
       		strFile = fd.getFile();
       		if (rifPromela != null){
       			llPromela1 = rifPromela.getPromelaSpecifiedAsLinkedList1();
       		}
       		else{
       			llPromela1 = new LinkedList();
       		}
       		if(strFile != null){
       			if(strFile.indexOf('.')==-1){
       				strFile+=".pro";			
       			}
           		pathFile = fd.getDirectory() + strFile;
            	fos = new FileOutputStream(pathFile);            	
            	dos = new DataOutputStream(fos);     	
    			for (int i=0; i<llPromela1.size(); i++){
	   				row = (String)(llPromela1.get(i)) + "\n";
					dos.writeBytes(row);
				}
            	dos.flush();
            	fos.close();
            	dos.close();            			
			}
			fd.dispose();
//			TODO Charmy v2.0		
		}
		catch (Exception ex){
			System.out.println(ex);
			System.out.println("File not saved!");
		}					        		
	}


	public PromelaWindow getRifPromela() {
		return rifPromela;
	}   	

	
//	private void showError()
//	{
//		String str;
//        
//        str =  	"Errore d'integrità \n\n";
//        if (check.getErrorMessage() != 0)
//        {
//        	str = str + "Negli scenari sono presenti " + check.getErrorMessage() + " canali non definiti \n" +
//        				"nel Topology Diagram nè in alcun State Diagram. \n";
//        } 
//		if (check.getErrorClass() != 0)
//		{
//        	str = str + "Negli scenari sono presenti " + check.getErrorClass() + " processi non definiti \n" +
//        				"nel Topology Diagram nè nel pannello degli State Diagram. \n";			
//		}	
//        JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.WARNING_MESSAGE);		
//	}  	
				
}