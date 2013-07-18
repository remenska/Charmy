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
    


package plugin.ba;

import java.util.LinkedList;

import plugin.topologydiagram.resource.data.ImpElementoId;

/**
 *
 * @author  FLAMEL 2005
 */

/**
 *
 * questa classe implementa il concetto di nodo 
 */
public class Node extends ImpElementoId {
    
    private int stateIndex = 0;
    private boolean acceptance = false;
    private boolean Final = false;
    private String label;
    private LinkedList listaArchi;
    private long instanceIde=0;
    
        
    //**Memorizza le istanze**//
    private static int NdInstanceNumber = 0;

    public Node(int stateIndex, boolean acceptance,boolean finale){
            this.stateIndex=stateIndex;
            this.acceptance=acceptance;
            if (this.acceptance){
                    if(this.stateIndex==0)
                            this.label="accept_init";
                    else
                            this.label="accept_S"+stateIndex;
            }
            else{
                    if(this.stateIndex==0)
                            this.label="T0_init";
                    else
                            this.label="T0_S"+stateIndex;
            }
             this.Final = finale;
            if(this.Final)                
                this.label = label.concat("_Final");      
            listaArchi = new LinkedList();
            instanceIde = incNdInstanceNumber();
    }
    
    /**restituisce l'id del nodo**/
    public long getId()
    {
        return this.instanceIde;
    }
    
    /**verifica se è finale**/
    public boolean isFinal()
    {
        return this.Final;
    }
    
    public void setFinal(boolean flag)
    {
        this.Final=flag;
    }
    
    public void setAcc(boolean flag)
    {
        this.acceptance=flag;
    }
    
    public boolean isInitial()
    {
        if(this.stateIndex==0)
            return true;
        else
            return false;
    }
    
    public boolean isAcceptance()
    {
        return this.acceptance;
    }
    
    public int getStateIndex()
    {
        return this.stateIndex;
    }
    
    public String getLabel()
    {
        return this.label;
    } 
    
    public void setStateIndex(int i)
    {
        this.stateIndex=i;
        changeStateLabel();
    }
    
    public void changeStateLabel()
    {
        if (this.acceptance){
                    if(this.stateIndex==0)
                            this.label="accept_init";
                    else
                            this.label="accept_S"+stateIndex;
            }
            else{
                    if(this.stateIndex==0)
                            this.label="T0_init";
                    else
                            this.label="T0_S"+stateIndex;
            }
        if(this.Final)                
            this.label = label.concat("_Final");
        
    }
    
    
    public void changeStateName()
    {                       
        if (this.acceptance){
            this.label="T0"+this.label.substring(6);
        }
        else{
            this.label="accept"+this.label.substring(2);
        }
        if(this.Final)                
            this.label = label.concat("_Final");
    }
    
    public void changeFinalStateName()
    {                       
        if (this.Final)
            Final=false;
        else
            Final= true;
        
    }
    
    public static int incNdInstanceNumber()
    {
            return NdInstanceNumber++;
    }

    
    
/*     
         public void changeNameTransition(int stateIndex,String label_target,String old_label,String new_label){
            String []trans;
            String newTransition;
            for(int i=0;i<transitions.size();i++){                   
                    if(label_target.equals(((String)(transitions.elementAt(i))).split("goto ")[1])){  
                                    trans=((String)(transitions.elementAt(i))).split("-> ");
                                    if(trans[0].equals("(1) ")){
                                            newTransition=label+ "-> "+trans[1];
                                    }

                                    else 
                                     {                                         
                                        if(trans[0].equals("("+old_label.substring(8)+")") && trans.length>1){
                                                    newTransition = "("+new_label+")"+ "-> goto "+trans[1];
                                            }
                                            else{
                                                    newTransition="";
                                            }                                             
                                      }
                                    }

                                    transitions.remove(i);
                                    transitions.add(i,newTransition);        			      
                    }
            }

    }

 */


}
    