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

import java.util.Vector;

import plugin.topologydiagram.resource.data.ImpElementoId;

/**
 *
 * @author  FLAMEL 2005
 */

/**Questa classe implementa il buchi automata 
 memorizza due liste una per gli archi ed una per i nodi
 non è stata necessaria l'implementazione di liste di adiacenza perché 
 non sono servite**/
public class Buchi extends ImpElementoId {
    
    /**memorizza la lista dei nodi associata al buchi**/
    private ListNode lista;
    
    /**memorizza la lista degli archi associata al buchi**/
    private ListTransition lsTr;

    private long instanceIde=0;
    //**Memorizza le istanze**//
        private static int BuchiInstanceNumber = 0;
        

    
    /** Creates a new instance of buchi */
    public Buchi() {
        lista = new ListNode();
        lsTr = new ListTransition();
        instanceIde = incBuchiInstanceNumber();

    }
    
    /**aggiunge un nodo**/
    public void addNode(int stateIndex, boolean acceptance,boolean finale){
        
        Node nd = new Node(stateIndex,acceptance,finale);
        lista.addElement(nd);
        
    }
    
    /**aggiunge un nodo**/
    public void addNode(Node nd,int stateIndex)
    {
        lista.addElement(nd);
        nd.setStateIndex(stateIndex);
    }
    
    /**aggiunge un nodo**/
    public void addNode(Node nd)
    {
        lista.addElement(nd);
    }
    
    /**aggiunge un nodo**/
    public void addNode(Node nd,boolean finale)
    {
        nd.setFinal(finale);
        lista.addElement(nd);
    }
    
    /**aggiunge un nodo al'inizio della lista**/
    public void addInitNode(Node nd)
    {
        lista.addInitElement(nd);
   
    }
    
    /**Restitusce un nodo dal suo index**/
    public Node getFromIndex (int index)
    {
        return lista.getFromIndex(index);
    }
    
    /**rimuove un nodo**/
    public boolean removeNode(Node nd)
    {
        return lista.removeElement(nd);
    }
    
    /**restituisce la tagia della lista dei nodi**/
    public int sizeNode()
    {
        return lista.size();
    }
    
    /**restituisce la tagia della lista degli archi**/
    public int sizeTrans()
    {
        return lsTr.size();
    }
    
    /**restituisce un nodo dalla sua posizione nella lista**/
    public Node getNode(int i)
    {
        return lista.get(i);
    }
    
    /**restituisce un'arco dalla sua posizione nella lista**/
    public Transition getTransition(int i)
    {
        return lsTr.get(i);
    }
    
    /**restituisce il nodo iniziale**/
    public Node getNodeInitial()
    {
        for(int i=0;i<lista.size();i++)
        {
            if(lista.get(i).isInitial())
                return (Node) lista.get(i);
        }
        return null;
    }
    
    /**aggiunge una transizione*/    
    public void addTransition(int stateIndex, int target, String label)
    {
            Transition tr = new Transition(lista.get(stateIndex),lista.get(target),label);
            lsTr.addElement(tr);  

    }
    
    /**aggiunge una transizione*/
    public void addTransition(Node stateIndex, Node target, String label)
    {
            Transition tr = new Transition(stateIndex,target,label);
            lsTr.addElement(tr);  

    }
    
    /**aggiunge una transizione*/
    public void addTransition(Transition tr){
            lsTr.addElement(tr);

    }
    
    /**rimuove una transizione */
    public void deleteTransitions(int stateIndex,int target,String label){
        Node Index = lista.getFromIndex(stateIndex);
        Node Target = lista.getFromIndex(target);
    	lsTr.deleteTransitions(Index,Target,label);
    }
    
    /**rimuove una transizione*/
    public void removeTransitions(Transition tr){
    	lsTr.removeElement(tr);
    }
    
   /**verifica se uno stato è accettante*/
    public boolean isAcceptanceState(int stateIndex){
    	return lista.get(stateIndex).isAcceptance();
    }
    
    /**verifica se uno stato è finale*/
    public boolean isFinalState(int stateIndex)
    {
        return lista.get(stateIndex).isFinal();
    }
    
    /**incrementa il contatore di buchi*/
    public static int incBuchiInstanceNumber()
    {
            return BuchiInstanceNumber++;
    }
    
    /**restituisce l'id del nodo iniziale*/
    public long getIdInit()
    {
        return lista.getIdInit();
    }
    
    /**restituisce l'id del buchi*/
    public long getId()
    {
        return instanceIde;
    }

    /**se lo stato è accettante diventa non accettante altrimenti il contrario*/
    public void changeStateName(int stateIndex){
        lista.get(stateIndex).changeStateName();        
    }
    
    /**se lo stato è finale diventa non finale altrimenti il contrario*/
    public void changeFinalStateName(int stateIndex){
        lista.get(stateIndex).changeFinalStateName();        
    }
 
    /**restituisce l'etichetta associata allo stato */
    public String getStateName(int stateIndex){
        return lista.get(stateIndex).getLabel();
    }
    
    /**aggiorna l'etichetta  della transizione , se non esiste ne crea una */
    public void updateTransition(int stateIndex,int target,String label,String op){

        for(int i=0;i<lsTr.size();i++)
        {                   
            Transition tr = lsTr.get(i);
            if(tr.getNodeIndex().getStateIndex()==stateIndex && tr.getNodeTarget().getStateIndex()==target)
            {
                tr.setLabel(tr.getLabel()+op+"("+label+")");
                return;
            }
        }
        Transition tra = new Transition(this.getFromIndex(stateIndex),this.getFromIndex(target),label);
        lsTr.addElement(tra);

    }
    
    /**aggiorna l'etichetta  di una self-transition , se non esiste ne crea una */
    public void updateSelfTransition(int stateIndex,String label)
    {
            for(int i=0;i<lsTr.size();i++)
            {                
                 Transition tr = lsTr.get(i);
                 if(tr.getNodeIndex().getStateIndex() == stateIndex && tr.getNodeTarget().getStateIndex() == stateIndex)
                 {
                     if(label.equals(tr.getLabel()))
                        return;
                     if(!label.equals("1"))
                     {
                         if(!tr.getLabel().equals("1"))
                         {
                            tr.setLabel(tr.getLabel()+" && ("+label+")");
                            return; 
                         }
                         else
                         {
                            tr.setLabel(label);
                            return;
                         }
                     }
                     else
                        return;                     
                 }                                            			      
            }
            Transition trans = new Transition (this.getFromIndex(stateIndex),this.getFromIndex(stateIndex),label);
            lsTr.addElement(trans);     
    }
    
    /**crea la stringa per la stampa finale*/
    public String stringForPrint(){
        final class defVar{
        	public String var;
        	public String def;
        	public defVar(String var,String def){
        		this.var=var;        		
        		this.def=def;
        	}
        }
        
        Vector defineVector=new Vector();        
        String define="";
        int defineCount=0;
        boolean contained=false;
        ListTransition listTran = new ListTransition();
        String stampa="\nnever{\n";
        for (int i=0;i<lista.size();i++){
            if(lsTr==null)
                stampa+=lista.get(i).getLabel()+":\n \t skip \n";
            else{
				int k;
                stampa+=lista.get(i).getLabel()+":\n \t if \n";
               listTran= lsTr.getTransNode(lista.get(i));
                for(int j=0;j<listTran.size();j++){
                   String label ="("+listTran.get(j).getLabel()+") -> goto "+listTran.get(j).getNodeTarget().getLabel();
                    if(label.split("-")[0].equals("(1) ")){
                    	stampa+="\t :: "+label+"\n";
                    }
                    else{
	                    define= label.split("-")[0];                    
	                    for(k=0;k<defineVector.size();k++){
	                    	if(((defVar)defineVector.elementAt(k)).def.substring(2).equals(define)){
	                    		contained=true;
	                    		break;
	                    	}
	                    }
	                    if(contained){                    
	                    	stampa+="\t :: "+((defVar)defineVector.elementAt(k)).var+" -"+label.split("-")[1]+"\n";
	                    }
	                    else{
	                         defineVector.add(new defVar("a_"+defineCount," -"+label.split("-")[0]));
               			 stampa+="\t :: "+"a_"+defineCount++ +" -"+label.split("-")[1]+"\n";
	                    }
	                    k=0;
	                    contained=false;
                    }
                }
                stampa+="\t fi;\n";
            }
        }
        
        stampa+="\n}";
        for(int i=0;i<defineVector.size();i++){
        	if(((defVar)defineVector.elementAt(i)).def.substring(2).startsWith("("))
        		stampa="#define "+((defVar)defineVector.elementAt(i)).var+" "+
        				((defVar)defineVector.elementAt(i)).def.substring(2)+"\n" +stampa;
        	else	
				stampa="#define "+((defVar)defineVector.elementAt(i)).var+" "
						+"("+((defVar)defineVector.elementAt(i)).def.substring(2)+")"+"\n" +stampa;
        }
        return stampa;
    }
    
   
}
