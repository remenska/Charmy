/*   Charmy (CHecking Architectural Model consistencY)
 *   Copyright (C) 2004 Patrizio Pelliccilink <pellicci@di.univaq.it>,
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

import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.statediagram.data.ListaDP;
import plugin.topologydiagram.resource.data.ImpElementoId;

/*
 *
 *@author FLAMEL 2005
 */
/**Questa classe contiene le funzioni per calcolare il buchi automata**/
public class PSC2BA extends ImpElementoId
{   
        
    private Buchi Buchi_new;
    
    private ListaDP lDP;
    
    /**analizza tutti i messaggi del PSC e per ognuno chiama la funzione appropriata**/
    public PSC2BA(LinkedList message,ListaDP ldp)
     {
        Buchi_new = new Buchi(); 
        this.lDP=ldp;
        int i=0;
        while(i < message.size())
        {
            ElementoSeqLink link = (ElementoSeqLink) message.get(i);
            if(link.isParallel())
            {
                LinkedList listpar =new LinkedList();
                    listpar.add(link);
                    for(int j=i+1;j<message.size();j++)
                    {
                        ElementoSeqLink link_par = (ElementoSeqLink) message.get(j);
                        if(link_par.isParallel())
                            if(link_par.getId_par() == link.getId_par())
                                listpar.add(link_par);
                    }
                
                    Buchi b = new Buchi();
                    b = parallel2BA(listpar);
                    if(i!=0)
                            Buchi_new = compose(Buchi_new,b);
                        else
                            Buchi_new = b;
                   
                    i = i+listpar.size();
            }
            else
                if(link.isLoop_op())
                {
                    LinkedList listloop =new LinkedList();
                    listloop.add(link);
                    for(int j=i+1;j<message.size();j++)
                    {
                        ElementoSeqLink link_loop = (ElementoSeqLink) message.get(j);
                        if(link_loop.isLoop_op())
                            if(link_loop.getId_loop() == link.getId_loop())
                                listloop.add(link_loop);
                    }
                    Buchi b = new Buchi();
                    int min = ((ElementoSeqLink) listloop.get(0)).getMinLoop();
                    int max = ((ElementoSeqLink) listloop.get(0)).getMaxLoop();
                    b = loop2BA(listloop,min,max);
                    if(i!=0)
                            Buchi_new = compose(Buchi_new,b);
                        else
                            Buchi_new = b;
                   
                    i = i+listloop.size();                    
                }
                else
                    if(link.isSimultaneous())
                    {
                        i++;
                    }
                    else
                    {
                        Buchi b = new Buchi();
                        b = basicAutomata(link);
                        if(i!=0)
                            Buchi_new = compose(Buchi_new,b);
                        else
                            Buchi_new = b;
                        i++; 
                    }              
        }
     }
     
 /**Restituisce il buchi**/   
 public Buchi getBuchi()
 {
     return Buchi_new;
 }
 
/** deriva l'automa da un operatore parallelo **/     
public Buchi parallel2BA(LinkedList list)
{
    LinkedList listMess = new LinkedList();
    ListBuchi listBU =new ListBuchi();
    Buchi new_buchi = new Buchi();
    LinkedList listop1 = new LinkedList();
    LinkedList listop2 = new LinkedList();
    Buchi buchi = new Buchi();
    for(int i=0;i<list.size();i++)
    {
        ElementoSeqLink link = (ElementoSeqLink) list.get(i);
        if(link.getOperatorPar())
            listop1.add(link);  //mi creo la lista dei messaggi del primo operatore
        else
            listop2.add(link);  // e quella del secondo operatore     
    }
    
   for(int i=0;i<listop1.size();i++) 
   {
       listMess.add(listop1.get(i));
   }
   for(int i=0;i<listop2.size();i++) 
   {
       listMess.add(listop2.get(i));
   }

    
    int k=1;

    for(int j=listop1.size()-1;j>=0;j--)                              //
    {
        for(int i=j;i<listMess.size()-k;i++)
        {
            listMess.remove(i);
            if(listMess.size()-1 == i)                                               //INTERLAVING
                listMess.addLast(listop1.get(j));
            else
                listMess.add(i+1,listop1.get(j));
            buchi = subl2Buchi(listMess);               
            listBU.addElement(buchi);
        }
        k++;
    }                                                                 //

    ///ezio 2006 - bug indexOutBoundsExeption - da corregere
    listMess.remove(listop2.size()-1);
    
    listMess.addLast(listop2.getLast());
    buchi = subl2Buchi(listMess);
    listBU.addElement(buchi);

    for(int j=listop2.size()-2;j>0;j--)
    {
        listMess.remove(j);
        listMess.add(j+listop1.size(), listop2.get(j));
        buchi = subl2Buchi(listMess);
        listBU.addElement(buchi);
    }                                                                 //
      
    new_buchi = collapseIFA(listBU);    
    return new_buchi;
}
 
 
/** deriva l'automa da un operatore Loop **/      
public Buchi loop2BA(LinkedList list,int min,int max)
{
    LinkedList listMess = new LinkedList();
    Buchi buchi = new Buchi();
    Buchi new_buchi = new Buchi();
    ListBuchi listBU =new ListBuchi();
    for(int i=min ;i<=max;i++)
    {
        for(int k=0;k<i;k++)
        {
            for(int j=0;j<list.size();j++)
            {
                listMess.add(list.get(j));
            }
            
        }
        
        ///ezio 2006
        if (listMess.size()>0) {
        	 buchi = subl2Buchi(listMess);
             listBU.addElement(buchi);
        }    
        ///////
        listMess = new LinkedList();
       
    }
    // ci vorrebbe una funzione per ottimizzare l'automa
    
    new_buchi = collapseIFA(listBU);
    return new_buchi; 
}
     
     
/**Prende una lista di messaggi e costruisce l'automa corrispondente**/     
public Buchi subl2Buchi(LinkedList list)
{
    Buchi b_new = new Buchi();
    
    ///ezio 2006 - bug da isolvere: indexoutBoundExeption
    b_new = basicAutomata((ElementoSeqLink)list.get(0));
    /////
    Buchi bapp = new Buchi();
    for(int i=1;i<list.size();i++)
    {
        bapp = basicAutomata((ElementoSeqLink)list.get(i));
        b_new = compose(b_new,bapp);
    }
    return b_new;
}

/**Prende un messaggio e costruisce l'automa corrispondente**/
public Buchi basicAutomata(ElementoSeqLink link)
    {        
        String ide;
        int countNode = 0;
        int precforfail=0;
        LinkedList listChain = new LinkedList();
        Buchi buchi = new Buchi();
        if(link.isComplement())
            ide="!"+link.getName();
        else
            ide=link.getName();

        /*Regular messages management*/        
        if (link.getRegReqFail()==ElementoSeqLink.REGULAR){
            
            if(link.isStrict()){
                buchi.addNode(countNode,false,false);	                        
                countNode++;
                buchi.addNode(countNode,false,true); 
                if(link.isConstraintPre())
                    buchi.addTransition(countNode-1,countNode,ide+"&&"+link.getExpConPre());
                else
                    buchi.addTransition(countNode-1,countNode,ide);
                buchi.addTransition(countNode,countNode,"1");                                 
                }                                                                                          
                      /*loose case*/
            else 
                if (link.hasConstraint()){                     
                     if(link.hasConBoll())
                     {
                         buchi.addNode(countNode,false,false);
                         buchi.addTransition(countNode,countNode,"1");
                         countNode++;
                         buchi.addNode(countNode,false,true);
                         buchi.addTransition(countNode-1, countNode,ide);
                         buchi.addTransition(countNode,countNode,"1");
                         if(link.isConstraintPastClo())
                         {
                             buchi.updateSelfTransition(countNode-1,link.getExpConPastClo());
                         }
                         if(link.isConstraintPastOp())
                         {
                             buchi.updateSelfTransition(countNode-1,link.getExpConPastOp());
                             buchi.updateTransition(countNode-1,countNode,link.getExpConPastOp(),"&&");
                         }
                         if(link.isConstraintPre())
                         {

                             buchi.updateTransition(countNode-1,countNode,link.getExpConPre(),"&&");
                         }
                         if(link.isConstraintFutOp())
                         {
                             buchi.updateTransition(countNode-1,countNode,link.getExpConFutOp(),"&&");
                             buchi.updateSelfTransition(countNode, link.getExpConFutOp());
                         }
                         if(link.isConstraintFutClo())
                         { 
                            buchi.updateSelfTransition(countNode, link.getExpConFutClo());
                         }
                     }
                     else
                     {
                         if(link.isConstraintChCloPast())
                        {
                            precforfail = countNode;
                            buchi.addNode(countNode,false,false);                                                
                            for(int i=0;i<link.getExpConChPastClo().size()-1;i++)
                            {    
                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastClo().get(i));
                                countNode++;
                                buchi.addNode(countNode,false,false);
                                buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastClo().get(i));
                            }
                            buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                            countNode++;
                            buchi.addNode(countNode,false,false);                        
                            buchi.addTransition(countNode, countNode,"1");
                            buchi.addTransition(countNode-1, countNode,"!"+ide+" && "+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                            countNode++;
                            buchi.addNode(countNode,false,true);
                            buchi.addTransition(countNode, countNode,"1"); 
                            buchi.addTransition(countNode-1, countNode,ide);
                            buchi.addTransition(countNode-2, countNode,ide+" && "+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));

                        }
                       else
                           if(link.isConstraintChOpPast())
                            {
                                precforfail = countNode;
                                buchi.addNode(countNode,false,false);                                                
                                for(int i=0;i<link.getExpConChPastOp().size()-1;i++)
                                {    
                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastOp().get(i));
                                    countNode++;
                                    buchi.addNode(countNode,false,false);
                                    buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastOp().get(i));
                                }
                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                                countNode++;
                                buchi.addNode(countNode,false,false);                        
                                buchi.addTransition(countNode, countNode,"1");
                                buchi.addTransition(countNode-1, countNode,"!"+ide+" && "+(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                                countNode++;
                                buchi.addNode(countNode,false,true);
                                buchi.addTransition(countNode, countNode,"1");   
                                buchi.addTransition(countNode-1, countNode,ide);
                            }
                           else
                               if(link.isConstraintChOpFut())
                                {
                                    precforfail = countNode;
                                    buchi.addNode(countNode,false,false);
                                    buchi.addTransition(countNode, countNode,"1");
                                    for(int i=0;i<link.getExpConChFutOp().size();i++)
                                    {  
                                        countNode++;
                                        buchi.addNode(countNode,false,false);
                                        buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChFutOp().get(i));                                                                                
                                    }
                                    buchi.addTransition(precforfail, precforfail+1,ide+" && !"+(String)link.getExpConChFutOp().get(0));
                                    countNode++;
                                    buchi.addNode(countNode,false,true); 
                                    buchi.addTransition(countNode, countNode,"1");
                                    for(int i=0;i<link.getExpConChFutOp().size();i++)
                                    {
                                        buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConChFutOp().get(i));
                                    }
                                }
                               else
                                  if(link.isConstraintChCloFut())
                                    {
                                        precforfail = countNode;
                                        buchi.addNode(countNode,false,false);
                                        buchi.addTransition(countNode, countNode,"1");
                                        for(int i=0;i<link.getExpConChFutClo().size();i++)
                                        {  
                                            countNode++;
                                            buchi.addNode(countNode,false,false);
                                            buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChFutClo().get(i));                                                                                
                                        }
                                        buchi.addTransition(precforfail, precforfail+1,ide+" && !"+(String)link.getExpConChFutClo().get(0));
                                        if(countNode >= precforfail+2)
                                            buchi.addTransition(precforfail, precforfail+2,ide);
                                        countNode++;
                                        buchi.addNode(countNode,false,true);
                                        buchi.addTransition(countNode, countNode,"1");
                                        for(int i=0;i<link.getExpConChFutClo().size();i++)
                                        {
                                            buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConChFutClo().get(i));
                                        }
                                    }
                                  else
                                      if(link.isConstraintUnCloFut())
                                        {
                                            precforfail = countNode;
                                            buchi.addNode(countNode,false,false);
                                            buchi.addTransition(countNode, countNode,"1");
                                            for(int i=0;i<link.getExpConUnFutClo().size();i++)
                                            {  
                                                countNode++;
                                                buchi.addNode(countNode,false,true);
                                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnFutClo().get(i));                                                                                
                                            }
                                            buchi.addTransition(precforfail, precforfail+1,ide+" && !"+(String)link.getExpConUnFutClo().get(0));
                                            if(countNode >= precforfail+2)
                                                buchi.addTransition(precforfail, precforfail+2,ide);                                           
                                            for(int i=0;i<link.getExpConUnFutClo().size()-1;i++)
                                            {
                                                buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConUnFutClo().get(i));
                                            }
                                        }
                                      else
                                        if(link.isConstraintUnOpFut())
                                        {
                                            precforfail = countNode;
                                            buchi.addNode(countNode,false,false);
                                            buchi.addTransition(countNode, countNode,"1");
                                            for(int i=0;i<link.getExpConUnFutOp().size();i++)
                                            {  
                                                countNode++;
                                                buchi.addNode(countNode,false,true);
                                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnFutOp().get(i));                                                                                
                                            }
                                            buchi.addTransition(precforfail, precforfail+1,ide+ " && !"+(String)link.getExpConUnFutOp().get(0));
                                            for(int i=0;i<link.getExpConUnFutOp().size()-1;i++)
                                            {
                                                buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConUnFutOp().get(i));
                                            }
                                        }
                                        else
                                            if(link.isConstraintUnCloPast())
                                            {
                                                precforfail = countNode;
                                                buchi.addNode(countNode,false,false);                                                
                                                for(int i=0;i<link.getExpConUnPastClo().size()-1;i++)
                                                {    
                                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastClo().get(i));
                                                    countNode++;
                                                    buchi.addNode(countNode,false,false);
                                                    buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastClo().get(i));
                                                }
                                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1));
                                                countNode++;
                                                buchi.addNode(countNode,false,true);                        
                                                buchi.addTransition(countNode, countNode,"1");
                                                buchi.addTransition(countNode-1, countNode,ide);
                                                for(int i=0;i<link.getExpConUnPastClo().size()-1;i++)
                                                {
                                                   buchi.addTransition(precforfail+i, countNode,ide); 
                                                }

                                            }
                                            else
                                               if(link.isConstraintUnOpPast())
                                                {
                                                    precforfail = countNode;
                                                    buchi.addNode(countNode,false,false);                                                
                                                    for(int i=0;i<link.getExpConUnPastOp().size()-1;i++)
                                                    {    
                                                        buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastOp().get(i));
                                                        countNode++;
                                                        buchi.addNode(countNode,false,false);
                                                        buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastOp().get(i));
                                                    }
                                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                                    countNode++;
                                                    buchi.addNode(countNode,false,true);                        
                                                    buchi.addTransition(countNode, countNode,"1");
                                                    buchi.addTransition(countNode-1, countNode,ide+" && !"+(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                                    for(int i=0;i<link.getExpConUnPastOp().size()-1;i++)
                                                    {
                                                       buchi.addTransition(precforfail+i, countNode,ide); 
                                                    }

                                                } 
                     }
                }
              else{
                    buchi.addNode(countNode,false,false);
                    buchi.addTransition(countNode,countNode,"1");					  	    
                    countNode++;
                    buchi.addNode(countNode,false,true);
                    buchi.addTransition(countNode-1, countNode,ide);
                    buchi.addTransition(countNode,countNode,"1");             					  	
                }
       }
        
        /**REQUIRED MESSAGE**/
        else if(link.getRegReqFail()==ElementoSeqLink.REQUIRED){
                if(link.isStrict()){
                    buchi.addNode(countNode,true,false);					  	    
                    countNode++;
                    buchi.addNode(countNode,true,false);
                    if(link.isConstraintPre())
                       buchi.addTransition(countNode-1,countNode,"!"+ide+"||!"+link.getExpConPre()); 
                    else
                        buchi.addTransition(countNode-1,countNode,"!"+ide);
                    buchi.addTransition(countNode,countNode,"1");
                    countNode++;
                    buchi.addNode(countNode,false,true);
                    if(link.isConstraintPre())
                        buchi.addTransition(countNode-2, countNode,ide+"&&"+link.getExpConPre());
                    else
                        buchi.addTransition(countNode-2, countNode,ide);
                    buchi.addTransition(countNode,countNode,"1");
                }				                            
                else     /*loose case*/		 
                   if (link.hasConstraint())
                   {                
                    if(link.hasConBoll())
                     {
                          buchi.addNode(countNode,true,false);
                          precforfail=countNode;
                          buchi.addTransition(countNode,countNode,"!"+ide);
                          countNode++;
                          buchi.addNode(countNode,false,true);
                          buchi.addTransition(countNode,countNode,"1");
                          buchi.addTransition(countNode-1,countNode,ide);
                          if(link.isConstraintPre())
                          {
                              buchi.updateTransition(countNode-1,countNode-1,link.getExpConPre(), "||");
                              buchi.updateTransition(countNode-1,countNode,link.getExpConPre(),"&&");
                          }    
                          if(link.isConstraintPastClo() || link.isConstraintPastOp()
                            || link.isConstraintFutClo() || link.isConstraintFutOp())
                          {
                              countNode++;                              
                              buchi.addNode(countNode,true,false);
                              buchi.addTransition(countNode,countNode,"1"); 
                              
                              if(link.isConstraintFutOp())
                              {   
                                  buchi.updateSelfTransition(countNode-1, link.getExpConFutOp());
                                  buchi.updateTransition(precforfail,countNode-1,link.getExpConFutOp(),"&&");
                                  buchi.updateTransition(countNode-1,countNode,"!"+link.getExpConFutOp(),"&&");  
                              }
                              if(link.isConstraintFutClo())
                              {
                                  if(link.isConstraintFutOp())
                                  { 
                                        countNode++;                              
                                        buchi.addNode(countNode,false,true);
                                        buchi.addTransition(countNode,countNode,link.getExpConFutClo());
                                        buchi.addTransition(precforfail,countNode,ide+" && "+link.getExpConFutClo());
                                        countNode++;                              
                                        buchi.addNode(countNode,true,false);
                                        buchi.addTransition(countNode,countNode,"1");
                                        buchi.addTransition(countNode-1,countNode,"!"+link.getExpConFutClo());
                                  }
                                  else
                                  {
                                        buchi.updateSelfTransition(countNode-1, link.getExpConFutClo());
                                        buchi.updateTransition(countNode-1,countNode,"!"+link.getExpConFutClo(),"&&"); 
                                  }
                                   

                              }
                              if(link.isConstraintPastOp())
                              {
                                  if(link.isConstraintFutOp() || link.isConstraintFutClo() )
                                  {
                                        countNode++;                              
                                        buchi.addNode(countNode,true,false);
                                        buchi.addTransition(countNode,countNode,"1");
                                        buchi.addTransition(precforfail,countNode,"!"+ link.getExpConPastOp());
                                        buchi.updateTransition(precforfail,precforfail+1,link.getExpConPastOp()," && ");
                                        if(link.isConstraintFutOp() && link.isConstraintFutClo() )
                                            buchi.updateTransition(precforfail,countNode-2,link.getExpConPastOp()," && ");
                                  }
                                  else
                                  {
                                     buchi.updateTransition(precforfail,countNode-1,link.getExpConPastOp(),"&&");
                                     buchi.updateTransition(precforfail,countNode,"!"+link.getExpConPastOp(),"&&");

                                  }
                              }
                              if(link.isConstraintPastClo())
                              {
                                   if(link.isConstraintFutOp() || link.isConstraintFutClo() )
                                   {
                                        countNode++;                              
                                        buchi.addNode(countNode,true,false);
                                        buchi.addTransition(countNode,countNode,"1");
                                        buchi.addTransition(precforfail,countNode,"!"+ide+" && !"+ link.getExpConPastClo());
                                   }
                                   else
                                   {
                                        buchi.updateTransition(precforfail,precforfail+2,"!"+ide+"&& !"+link.getExpConPastClo(),"&&");
                                   }
                                   
                                       
                              }
                              
                          }
                      }
                    else
                    {
                       if(link.isConstraintChCloPast())
                        {
                            precforfail = countNode;
                            buchi.addNode(countNode,true,false);                                                
                            for(int i=0;i<link.getExpConChPastClo().size()-1;i++)
                            {    
                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastClo().get(i));
                                countNode++;
                                buchi.addNode(countNode,true,false);
                                buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastClo().get(i));
                            }
                            buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                            countNode++;
                            buchi.addNode(countNode,true,false);                        
                            buchi.addTransition(countNode, countNode,"!"+ide);
                            buchi.addTransition(countNode-1, countNode,"!"+ide+" && "+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                            countNode++;
                            buchi.addNode(countNode,true,false);
                            buchi.addTransition(countNode, countNode,"1");   
                            buchi.addTransition(countNode-2, countNode,ide);
                            countNode++;
                            buchi.addNode(countNode,false,true);                        
                            buchi.addTransition(countNode, countNode,"1");
                            buchi.addTransition(countNode-2, countNode,ide);
                            for(int i=0;i<link.getExpConChPastClo().size()-1;i++)
                            {
                               buchi.addTransition(precforfail+i, countNode,ide); 
                            }

                        }
                       else
                           if(link.isConstraintChOpPast())
                            {
                                precforfail = countNode;
                                buchi.addNode(countNode,true,false);                                                
                                for(int i=0;i<link.getExpConChPastOp().size()-1;i++)
                                {    
                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastOp().get(i));
                                    countNode++;
                                    buchi.addNode(countNode,true,false);
                                    buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastOp().get(i));
                                }
                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                                countNode++;
                                buchi.addNode(countNode,true,false);                        
                                buchi.addTransition(countNode, countNode,"!"+ide);
                                buchi.addTransition(countNode-1, countNode,"!"+ide+" && "+(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                                countNode++;
                                buchi.addNode(countNode,true,false);
                                buchi.addTransition(countNode, countNode,"1");   
                                buchi.addTransition(countNode-2, countNode,ide+" && !"+ (String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                                countNode++;
                                buchi.addNode(countNode,false,true);                        
                                buchi.addTransition(countNode, countNode,"1");
                                buchi.addTransition(countNode-2, countNode,ide);
                                buchi.addTransition(countNode-3, countNode,ide+" && "+(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                                for(int i=0;i<link.getExpConChPastOp().size()-1;i++)
                                {
                                   buchi.addTransition(precforfail+i, countNode,ide); 
                                }

                            }
                           else
                               if(link.isConstraintChOpFut())
                                {
                                    precforfail = countNode;
                                    buchi.addNode(countNode,false,false);
                                    buchi.addTransition(countNode, countNode,"1");
                                    for(int i=0;i<link.getExpConChFutOp().size();i++)
                                    {  
                                        countNode++;
                                        buchi.addNode(countNode,true,false);
                                        buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChFutOp().get(i));                                                                                
                                    }
                                    buchi.addTransition(precforfail, precforfail+1,ide);
                                    countNode++;
                                    buchi.addNode(countNode,false,true);
                                    for(int i=0;i<link.getExpConChFutOp().size();i++)
                                    {
                                        buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConChFutOp().get(i));
                                    }
                                }
                               else
                                  if(link.isConstraintChCloFut())
                                    {
                                        precforfail = countNode;
                                        buchi.addNode(countNode,false,false);
                                        buchi.addTransition(countNode, countNode,"1");
                                        for(int i=0;i<link.getExpConChFutClo().size();i++)
                                        {  
                                            countNode++;
                                            buchi.addNode(countNode,true,false);
                                            buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChFutClo().get(i));                                                                                
                                        }
                                        buchi.addTransition(precforfail, precforfail+1,ide+" && !"+(String)link.getExpConChFutClo().get(0));
                                        if(countNode >= precforfail+2)
                                            buchi.addTransition(precforfail, precforfail+2,ide);
                                        countNode++;
                                        buchi.addNode(countNode,false,true);
                                        for(int i=0;i<link.getExpConChFutClo().size();i++)
                                        {
                                            buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConChFutClo().get(i));
                                        }
                                    }
                                  else
                                      if(link.isConstraintUnCloFut())
                                        {
                                            precforfail = countNode;
                                            buchi.addNode(countNode,false,false);
                                            buchi.addTransition(countNode, countNode,"1");
                                            for(int i=0;i<link.getExpConUnFutClo().size();i++)
                                            {  
                                                countNode++;
                                                buchi.addNode(countNode,false,true);
                                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnFutClo().get(i));                                                                                
                                            }
                                            buchi.addTransition(precforfail, precforfail+1,ide+" && !"+(String)link.getExpConUnFutClo().get(0));
                                            if(countNode >= precforfail+2)
                                                buchi.addTransition(precforfail, precforfail+2,ide);
                                            countNode++;
                                            buchi.addNode(countNode,true,false);
                                            buchi.addTransition(countNode, countNode,"1");
                                            for(int i=0;i<link.getExpConUnFutClo().size();i++)
                                            {
                                                buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConUnFutClo().get(i));
                                            }
                                        }
                                      else
                                        if(link.isConstraintUnOpFut())
                                        {
                                            precforfail = countNode;
                                            buchi.addNode(countNode,false,false);
                                            buchi.addTransition(countNode, countNode,"1");
                                            for(int i=0;i<link.getExpConUnFutOp().size();i++)
                                            {  
                                                countNode++;
                                                buchi.addNode(countNode,false,true);
                                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnFutOp().get(i));                                                                                
                                            }
                                            buchi.addTransition(precforfail, precforfail+1,ide);
                                            countNode++;
                                            buchi.addNode(countNode,true,false);
                                            buchi.addTransition(countNode, countNode,"1");
                                            for(int i=0;i<link.getExpConUnFutOp().size();i++)
                                            {
                                                buchi.addTransition(precforfail+1+i, precforfail+2+i,(String)link.getExpConUnFutOp().get(i));
                                            }
                                        }
                                        else
                                            if(link.isConstraintUnCloPast())
                                            {
                                                precforfail = countNode;
                                                buchi.addNode(countNode,true,false);                                                
                                                for(int i=0;i<link.getExpConUnPastClo().size()-1;i++)
                                                {    
                                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastClo().get(i));
                                                    countNode++;
                                                    buchi.addNode(countNode,true,false);
                                                    buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastClo().get(i));
                                                }
                                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1));
                                                countNode++;
                                                buchi.addNode(countNode,true,false);                        
                                                buchi.addTransition(countNode, countNode,"1");
                                                buchi.addTransition(countNode-1, countNode,"!"+ide+" && "+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1));
                                                countNode++;
                                                buchi.addNode(countNode,false,true);
                                                buchi.addTransition(countNode, countNode,"1");   
                                                buchi.addTransition(countNode-2, countNode,ide);
                                                for(int i=0;i<link.getExpConUnPastClo().size()-1;i++)
                                                {
                                                   buchi.addTransition(precforfail+i, countNode,ide); 
                                                }

                                            }
                                            else
                                               if(link.isConstraintUnOpPast())
                                                {
                                                    precforfail = countNode;
                                                    buchi.addNode(countNode,true,false);                                                
                                                    for(int i=0;i<link.getExpConUnPastOp().size()-1;i++)
                                                    {    
                                                        buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastOp().get(i));
                                                        countNode++;
                                                        buchi.addNode(countNode,true,false);
                                                        buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastOp().get(i));
                                                    }
                                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                                    countNode++;
                                                    buchi.addNode(countNode,true,false);                        
                                                    buchi.addTransition(countNode, countNode,"1");
                                                    buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                                    countNode++;
                                                    buchi.addNode(countNode,false,true);
                                                    buchi.addTransition(countNode, countNode,"1");   
                                                    buchi.addTransition(countNode-2, countNode,ide+" && !"+(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                                    for(int i=0;i<link.getExpConUnPastOp().size()-1;i++)
                                                    {
                                                       buchi.addTransition(precforfail+i, countNode,ide); 
                                                    }

                                                } 
                    }
                                                        
                  }
                  else{
                        buchi.addNode(countNode,true,false);
                        buchi.addTransition(countNode,countNode,"!"+ide);					  	    
                        countNode++;
                        buchi.addNode(countNode,false,true);
                        buchi.addTransition(countNode-1, countNode,ide);
                        buchi.addTransition(countNode,countNode,"1");            					  	
                      }
                    
        }
        
        /**FAIL MESSAGE**/
        else 
            if(link.getRegReqFail()==ElementoSeqLink.FAIL)
            {                	
                if(link.isStrict()){	
                    buchi.addNode(countNode,false,false);					  	    
                    countNode++;
                    buchi.addNode(countNode,false,true);
                    if(link.isConstraintPre())
                        buchi.addTransition(countNode-1,countNode,"!"+ide+"||!"+link.getExpConPre());
                    else
                        buchi.addTransition(countNode-1,countNode,"!"+ide);
                    buchi.addTransition(countNode,countNode,"1");
                    countNode++;
                    buchi.addNode(countNode,true,false);
                    if(link.isConstraintPre())
                        buchi.addTransition(countNode-2, countNode,ide+"&"+link.getExpConPre());
                    else
                        buchi.addTransition(countNode-2, countNode,ide);
                    buchi.addTransition(countNode,countNode,"1");
                }                    
            else                                        /*loose case*/
              if (link.hasConstraint())
              {
                if(link.hasConBoll())
                 {
                      buchi.addNode(countNode,false,false);
                      precforfail = countNode;
                      buchi.addTransition(countNode, countNode, "1");
                      countNode++;
                      buchi.addNode(countNode,true,true);
                      buchi.addTransition(precforfail, countNode, ide);
                      buchi.addTransition(countNode, countNode, "1");
                      if(link.isConstraintPre())
                      {
                          buchi.updateTransition(precforfail, countNode, link.getExpConPre(), "&&");                     
                      }                      
                      if(link.isConstraintPastClo() || link.isConstraintPastOp())
                      {
                          countNode++;
                          buchi.addNode(countNode,false,false);
                          buchi.addTransition(countNode, countNode, "1");                                                  
                          if(link.isConstraintPastClo())
                          {  
                              buchi.updateSelfTransition(precforfail, "!"+link.getExpConPastClo()); 
                              if(link.isConstraintPre())
                              {     
                                    buchi.changeStateName(countNode);                               
                                    buchi.updateTransition(precforfail,countNode, ide+" && " + link.getExpConPastClo(), "&&");
                                    countNode++;
                                    buchi.addNode(countNode,false,true);
                                    buchi.addTransition(countNode, countNode, "1");
                                    buchi.updateTransition(precforfail, countNode, "!"+link.getExpConPastClo(),"&&");
                              }
                              else
                              {
                                  buchi.updateTransition(precforfail,countNode-1, link.getExpConPastClo(), "&&");
                                  buchi.updateTransition(precforfail, countNode, "!"+link.getExpConPastClo(),"&&");
                              }
                          }
                          if(link.isConstraintPastOp())
                          {
                              buchi.updateSelfTransition(precforfail, "!"+link.getExpConPastOp()); 
                              if(link.isConstraintPastClo())
                              {
                                  countNode++;
                                  buchi.addNode(countNode,false,true);
                                  buchi.addTransition(countNode, countNode, "1");
                                  buchi.updateTransition(precforfail, countNode, "!"+link.getExpConPastOp()+"&& !"+ide,"&&");
                                 
                              }
                              else                                  
                                buchi.updateTransition(precforfail, countNode, "!"+link.getExpConPastOp()+"&& !"+ide,"&&");
                          }
                      }
                  
                }
                else
                {
                    if(link.isConstraintChCloPast())
                    {
                        precforfail = countNode;
                        buchi.addNode(countNode,false,false);                                                
                        for(int i=0;i<link.getExpConChPastClo().size()-1;i++)
                        {    
                            buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastClo().get(i));
                            countNode++;
                            buchi.addNode(countNode,false,false);
                            buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastClo().get(i));
                        }
                        buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                        countNode++;
                        buchi.addNode(countNode,false,true);                        
                        buchi.addTransition(countNode, countNode,"1");
                        buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                        countNode++;
                        buchi.addNode(countNode,true,false);                        
                        buchi.addTransition(countNode, countNode,"1");
                        buchi.addTransition(countNode-2, countNode,ide+" && !"+(String)link.getExpConChPastClo().get(link.getExpConChPastClo().size()-1));
                        for(int i=0;i<link.getExpConChPastClo().size()-1;i++)
                        {
                           buchi.addTransition(precforfail+i, countNode,ide); 
                        }
                        
                    }
                    else 
                        if(link.isConstraintChOpPast())
                        {
                            precforfail = countNode;
                            buchi.addNode(countNode,false,false);                                                
                            for(int i=0;i<link.getExpConChPastOp().size()-1;i++)
                            {    
                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastOp().get(i));
                                countNode++;
                                buchi.addNode(countNode,false,false);
                                buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastOp().get(i));
                            }
                            buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1));
                            countNode++;
                            buchi.addNode(countNode,false,true);                        
                            buchi.addTransition(countNode, countNode,"1");
                            buchi.addTransition(countNode-1, countNode,(String)link.getExpConChPastOp().get(link.getExpConChPastOp().size()-1)+" && !"+ide);
                            countNode++;
                            buchi.addNode(countNode,true,false);                        
                            buchi.addTransition(countNode, countNode,"1");
                            buchi.addTransition(countNode-2, countNode,ide);
                            for(int i=0;i<link.getExpConChPastOp().size()-1;i++)
                            {
                               buchi.addTransition(precforfail+i, countNode,ide); 
                            }
                        }
                        else
                            if(link.isConstraintUnCloPast())
                            {
                                precforfail = countNode;
                                buchi.addNode(countNode,false,true);                                                
                                for(int i=0;i<link.getExpConUnPastClo().size()-1;i++)
                                {    
                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastClo().get(i));
                                    countNode++;
                                    buchi.addNode(countNode,false,true);
                                    buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastClo().get(i));
                                }
                                buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1));
                                countNode++;
                                buchi.addNode(countNode,false,true);                        
                                buchi.addTransition(countNode, countNode,"1");
                                buchi.addTransition(countNode-1, countNode,"!"+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1)+" && "+ide);
                                countNode++;
                                buchi.addNode(countNode,false,true);                        
                                buchi.addTransition(countNode, countNode,"!"+ide);
                                buchi.addTransition(countNode-2, countNode,"!"+ide+" && "+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1));
                                countNode++;
                                buchi.addNode(countNode,true,false);
                                buchi.addTransition(countNode, countNode,"1");
                                buchi.addTransition(countNode-3, countNode,ide+" && "+(String)link.getExpConUnPastClo().get(link.getExpConUnPastClo().size()-1));
                                
                                for(int i=0;i<link.getExpConUnPastClo().size()-1;i++)
                                {
                                   buchi.addTransition(precforfail+i, countNode-2,ide); 
                                }
                            }
                            else
                                if(link.isConstraintUnOpPast())
                                {
                                    precforfail = countNode;
                                    buchi.addNode(countNode,false,true);                                                
                                    for(int i=0;i<link.getExpConUnPastOp().size()-1;i++)
                                    {    
                                        buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastOp().get(i));
                                        countNode++;
                                        buchi.addNode(countNode,false,true);
                                        buchi.addTransition(countNode-1, countNode,(String)link.getExpConUnPastOp().get(i));
                                    }
                                    buchi.addTransition(countNode, countNode,"!"+(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                    countNode++;
                                    buchi.addNode(countNode,false,true);                        
                                    buchi.addTransition(countNode, countNode,"1");
                                    buchi.addTransition(countNode-1, countNode,ide);
                                    countNode++;
                                    buchi.addNode(countNode,false,true);                        
                                    buchi.addTransition(countNode, countNode,"!"+ide);
                                    buchi.addTransition(countNode-2, countNode,"!"+ide+" && "+(String)link.getExpConUnPastOp().get(link.getExpConUnPastOp().size()-1));
                                    countNode++;
                                    buchi.addNode(countNode,true,false);
                                    buchi.addTransition(countNode, countNode,"1");

                                    for(int i=0;i<link.getExpConUnPastOp().size()-1;i++)
                                    {
                                       buchi.addTransition(precforfail+i, countNode-2,ide); 
                                    }
                                }
                   
                }
                                               
              }
              else{
                buchi.addNode(countNode,false,true);                     
                countNode++;
                buchi.addNode(countNode,true,false);
                buchi.addTransition(countNode-1, countNode,ide);
                buchi.addTransition(countNode-1,countNode-1,"1");
                buchi.addTransition(countNode,countNode,"1");                                					  	
              }
            }
            
        return buchi;
    }
   
/**Prende in input una lista di automi e unifica collassando tutti gli stati iniziali
 finali  e accettori, gli stati accettanti sono collassati solo se hann una sefl-transition etichettata con "1"**/     
public Buchi collapseIFA(ListBuchi list)
{
    Buchi b_new = new Buchi();
    ListNode Final = new ListNode();
    ListNode Accepting = new ListNode();
    ListNode Initial = new ListNode();
    Node init;
    Node accepting;
    Node finale;

    //ezio 2006 
    if (list.size()==0)
    	return  b_new;
    ////
    
    for(int i=0;i<list.size();i++)
    {
       Buchi buchi = list.get(i);
       for(int j=0;j<buchi.sizeNode();j++)
       {
           b_new.addNode(buchi.getNode(j));              //setto tutti gli stati del primo automa come non finali
           if(buchi.getNode(j).isInitial())
                Initial.addElement(buchi.getNode(j));
           if(buchi.getNode(j).isAcceptance() && CntSelf1(b_new,buchi.getNode(j)))
               Accepting.addElement(buchi.getNode(j));
           if(buchi.getNode(j).isFinal())
            {
               Final.addElement(buchi.getNode(j));  
               buchi.getNode(j).setFinal(false);
            }
       }
       for(int j=0;j<buchi.sizeTrans();j++)
       {
           b_new.addTransition(buchi.getTransition(j));  //setto tutti gli stati del primo automa come non finali
       }
    }
    
    //////////////
  
    
     ////////////////////////Init////////////////////////////
  
    init = new Node(b_new.sizeNode(),false,false);
    b_new.addInitNode(init);
    
    finale = new Node(b_new.sizeNode(),false,true);
    b_new.addNode(finale);
    
    accepting = new Node(b_new.sizeNode(),true,false);
    b_new.addNode(accepting);
    
    
    
  
    for(int j=0;j<Initial.size();j++)
    {
        Transition tr = new Transition(init,Initial.get(j),"EPS");
        b_new.addTransition(tr);
    } 
    
    if(Accepting.size()!=0)
    {
        for(int k=0;k<Accepting.size();k++)
        {
            Transition tr = new Transition(accepting,Accepting.get(k),"EPS");
            b_new.addTransition(tr);
        }
    }
    else
        b_new.removeNode(accepting);
    
    for(int j=0;j<Final.size();j++)
    {
        Transition tr = new Transition(finale,Final.get(j),"EPS");
        b_new.addTransition(tr);
    }
    
    
    changeIndex(b_new); 
    boolean ok = true;
    int i = 0;
    while(ok)
    {
        Transition tr = b_new.getTransition(i);
        if(tr.getLabel().equals("EPS"))
        {
            collapse(tr,b_new);       //collasso le esp-transizioni
            i = 0;
        }
        else
            i++;
        
        if(i>=b_new.sizeTrans())
            ok = false;
    }

    return b_new;
}
     
     
/**prende in input due automi b1 e b2 e construisce un nuovo automa b_new ottenuto
 dalla composizione di b1 con b2**/    
public Buchi compose(Buchi b1,Buchi b2)
{   
   Buchi b_new = new Buchi();
   LinkedList Final_1 = new LinkedList();
   LinkedList init = new LinkedList();
   
   for(int i=0;i<b1.sizeNode();i++)
   {
       b_new.addNode(b1.getNode(i));
        if(b1.getNode(i).isFinal())
           Final_1.add(b_new.getNode(b_new.sizeNode()-1));
       b_new.getNode(b_new.sizeNode()-1).setFinal(false);
                                             //setto tutti gli stati del primo automa come non finali
   }  
   for(int i=0;i<b1.sizeTrans();i++)
   {
       b_new.addTransition(b1.getTransition(i));  //setto tutti gli stati del primo automa come non finali
   }

   Node init2 = null;
   for(int i=0;i<b2.sizeNode();i++)
   {
      b_new.addNode(b2.getNode(i));
      if(b2.getNode(i).isInitial())
         init2 = b_new.getNode(b_new.sizeNode()-1);
   }
   for(int i=0;i<b2.sizeTrans();i++)
   {
       b_new.addTransition(b2.getTransition(i)); 
   }
   
   changeIndex(b_new);
   
   for(int j=0;j<Final_1.size();j++)
   {
      b_new.addTransition((Node)Final_1.get(j),init2, "EPS"); //aggiungo le EPS trans tra gli stati finali del primo automa e lo stato iniziale del secondo
   }
  
    boolean ok = true;
    int i = 0;
    while(ok)
    {
        Transition tr = b_new.getTransition(i);
        if(tr.getLabel().equals("EPS"))
        {
            collapse(tr,b_new);
            i = 0;
        }
        else
            i++;
        if(i>=b_new.sizeTrans())
            ok = false;
    }

   return b_new;
   
   
}


/**collassa tutte le eps-trans unificando i due stati in un unico stato**/    
public void collapse (Transition tr ,Buchi buchi )
{
    boolean ok= true;
    int i=0;
    while(ok)
    {
       Transition trans = buchi.getTransition(i);
       if(trans.getId() != tr.getId())
       {
           if(trans.getIdIndex() == tr.getIdTarget()) 
           {
               if(trans.isSelf())
                {
                    buchi.updateSelfTransition(tr.getNodeIndex().getStateIndex(), trans.getLabel());
                    buchi.removeTransitions(trans);
                    i=0;
                }
                else
                {
                    trans.setIndex(tr.getNodeIndex());
                    i++;
                }
           }
                     
           else if(trans.getIdTarget() == tr.getIdTarget()) 
           {
               trans.setTarget(tr.getNodeIndex());
               i++;              
           }
           else
               i++;               
       }
       else
            i++;
       
       if(i>=buchi.sizeTrans())
           ok=false;
    }
    
    
    if(tr.getNodeTarget().isAcceptance() && !tr.getNodeIndex().isAcceptance())
         tr.getNodeIndex().setAcc(true); 
    
    buchi.removeNode(tr.getNodeTarget());
    buchi.removeTransitions(tr); 
    changeIndex(buchi);

}

public boolean CntSelf1(Buchi buchi,Node nd)
{
    String label="";
    for(int i=0;i<buchi.sizeTrans();i++)
    {
        Transition tr = buchi.getTransition(i);
        label = tr.getLabel();
        if(tr.getNodeIndex().equals(tr.getNodeTarget()) && label.equals("1") && tr.getNodeIndex().equals(nd))
            return true;
    }
    return false;
}


/**Ridenomina gli stati dell'atoma**/
public void changeIndex(Buchi buchi)
{
    for(int i=0;i<buchi.sizeNode();i++)
    {   
       buchi.getNode(i).setStateIndex(i);
       
    }
}



    
}

