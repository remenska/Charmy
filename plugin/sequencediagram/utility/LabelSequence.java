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
    

package plugin.sequencediagram.utility;

import plugin.statediagram.graph.ElementoMessaggio;

/**
 *
 * @author  Patrizio
 */
public final class LabelSequence {
    
    /** Creates a new instance of LabelSequence */
    public LabelSequence() {
    }
    
    public String label(ElementoMessaggio canale)
    {
        String guardia=canale.getGuard();
        String operazioni=canale.getOperations();
        String testo=canale.getName();
        String label="";
        if(guardia.length()!=0){
            if(!guardia.startsWith("["))                            
               guardia="["+guardia; 
            if(!guardia.endsWith("]"))                               
               guardia+="]";
            label=guardia;
        }
        if(testo.length()!=0){                          
            if(canale.getSendReceive()== ElementoMessaggio.SEND){
                if(label.length()!=0)
                    label+="/!"+testo;
                else
                    label="!"+testo;
            }
            else
                if(canale.getSendReceive()==ElementoMessaggio.RECEIVE){
                    if(label.length()!=0)
                        label+="/?"+testo;
                    else
                        label="?"+testo;
                }
                else{
                    if(label.length()!=0)
                        label+="/"+testo;
                    else
                        label=testo;
                }        
                String parametri="";
                if(canale.getParameters()!=null)                
	                for(int j=0;j<canale.getParameters().size();j++)
	                    if(j==0)
	                        parametri=(String)canale.getParameters().get(j);
	                    else
	                        parametri+=","+(String)canale.getParameters().get(j);
	                    
	                    if(!parametri.equals("")){
	                        String[] par=parametri.split(",");
	                        label+="(";
	                        for(int j=0;j<par.length;j++)
	                            if(j==0)
	                                label+=par[j];
	                            else
	                                label+=","+par[j];
	                        label+=")";
	                    }                    
        }
                        
        if(operazioni.length()!=0){
            if(!operazioni.endsWith(";")) 
                operazioni+=";";
            if(label.length()!=0)
                label+="/"+operazioni;
            else
                label=operazioni;
        }
        if(label.length()!=0){
            return label;
        }
        else{            
            String str = "Necessario modificare il nome del messaggio. \n" + 
                            "Il nome di un messaggio non pu? essere vuoto";	
            //JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
            return "";
        }   
    }
    
}
