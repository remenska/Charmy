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
    

package plugin.sequencediagram.data;

import plugin.sequencediagram.data.delegate.DelegateSequenceElementListener;
import plugin.sequencediagram.pluglistener.ISequenceElementListener;
import plugin.topologydiagram.resource.data.ImpElementoId;
import core.internal.plugin.file.FileManager;
import core.internal.runtime.data.IPlugData;

/**
 * @author Stoduto Michele
 * Charmy plug-in
 * Questa classe opera la separazione del SequenceEditor dalla lista dei suoi
 * elementi componenti, ListaSeqLink e ListaStato, ListaTime
 * inoltre si registra come listener delle classi suddette e gestisce la 
 * spedizione di un messaggio a classi che implementano L'interfacccia
 * ISequenceElementListener
 **/
public class SequenceElement extends ImpElementoId { 

	private DelegateSequenceElementListener listener;
	private IPlugData plugData;
	/** Contatore del numero di istanze della classe. */
	private static long numIstanza = 0;
	
	
	public static long getNumIstanze()
	{
		return numIstanza;
	}
	
	
	public static void setNumIstanze(long n)
	{
		numIstanza = n;
	}



	/**
	 * ListaClasse
	 */
	private ListaClasse listaClasse;

	/**
	 * ListaSeqLink 
	 */
	private ListaSeqLink listaSeqLink;

	/**
	 * ListaTime 
	 */
	private ListaTime listaTime;

	/**
	 * lista constraint
	 */
	private ListaConstraint listaConstraint;
        
        /**
	 * lista op.paralleli
	 */
	private ListaParallel listaParallel;
        
        /**
	 * lista op.simultaneo
	 */
	private ListaSim listaSim;
        
        /**
	 * lista op.loop
	 */
	private ListaLoop listaLoop;
	
        
	/**
	 * Costruisce creando in modo automatico la ListaStato e ListaMessaggi
	 */
	public SequenceElement(String nomeSequence,IPlugData plugData) {

		this(nomeSequence, 
				new ListaClasse(plugData), 
				new ListaSeqLink(plugData), 
				new ListaTime(plugData), 
				new ListaConstraint(plugData),
                                new ListaParallel(plugData),
                                new ListaSim(plugData),
                                new ListaLoop(plugData),
				plugData);
	}

	/**
	 * Costruisce inserendo ListaStato e ListaMessaggi
	 */
	public SequenceElement(String nomeSequence, ListaClasse lc, ListaSeqLink ls, ListaTime lt,
                                ListaConstraint lcons,ListaParallel lpar,ListaSim lsim,ListaLoop lloop, IPlugData plugData) {

		//this.nomeSequence = nomeSequence;
		setName(nomeSequence);
		
		numIstanza++;
		setId(numIstanza);

		
		
		listener = new DelegateSequenceElementListener(plugData, this);
		
		setListaClasse(lc);
		setListaSeqLink(ls);
		setListaTime(lt);
		setListaConstraint(lcons);
                setListaParallel(lpar);
                setListaSim(lsim);
                setListaLoop(lloop);
		this.plugData = plugData;
		
	}

	
	
	
	/**
	 * Ritorna la ListaMessaggio
	 * @return listaMessaggio
	 */
	public ListaSeqLink getListaSeqLink() {
		return listaSeqLink;
	}

	/**
	 * Ritorna la listaClasse
	 * @return
	 */
	public ListaClasse getListaClasse() {
		return listaClasse;
	}

	/**
	 * Ritorna la listaTime
	 * @return
	 */
	public ListaTime getListaTime() {
		return listaTime;
	}
	
	/**
	 * ritorna la lista constraint
	 * @return Returns the listaConstraint.
	 */
	public ListaConstraint getListaConstraint() {
		return listaConstraint;
	}
        
        /**
	 * Ritorna la ListaParallel
	 * @return listaParallel
	 */
	public ListaParallel getListaParallel() {
		return listaParallel;
	}
        
        /**
	 * Ritorna la ListaSim
	 * @return listaSim
	 */
	public ListaSim getListaSim() {
		return listaSim;
	}
        
        /**
	 * Ritorna la ListaLoop
	 * @return listaLoop
	 */
	public ListaLoop getListaLoop() {
		return listaLoop;
	}
        
        
	/**
	 * setta la ListaSeqLink
	 * @param ListaSeqLink
	 */
	public synchronized void setListaSeqLink(ListaSeqLink eleSeqLink) {

		/* evito duplicazione e generazione dati inutili */
		if (eleSeqLink != null) {
			eleSeqLink.removeListener(listener);
		}

		//listaSeqLink = eleSeqLink;
		if (listaSeqLink != null) {
			listaSeqLink.removeListener(listener);
		}
		
		listaSeqLink = eleSeqLink;
		if (listaSeqLink != null) {
			listaSeqLink.addListener(listener);
		}
		
	}

	/**
	 * setta la ListaClasse
	 * @param stato
	 */
	public synchronized void setListaClasse(ListaClasse classe) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (classe != null) {
			classe.removeListener(listener);
		}
		else return;
		
		if (listaClasse != null) {
			listaClasse.removeListener(listener);
		}
		listaClasse = classe;
		if (listaClasse != null) {
			listaClasse.addListener(listener);
		}		

	}

	/**
	 * setta la ListaTime
	 * @param stato
	 */
	public synchronized void setListaTime(ListaTime classe) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (classe != null) {
			classe.removeListener(listener);
		}
		else return;
		
		if (listaTime != null) {
			listaTime.removeListener(listener);
		}
		listaTime = classe;
		if (listaTime != null) {
			listaTime.addListener(listener);
		}		
	}


	/**
	 * setta la ListaConstraint
	 * @param stato
	 */
	public synchronized void setListaConstraint(ListaConstraint constraint) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (constraint != null) {
			constraint.removeListener(listener);
		}
		else return;
		
		if (listaConstraint != null) {
			listaConstraint.removeListener(listener);
		}
		listaConstraint = constraint;
		if (listaConstraint != null) {
			listaConstraint.addListener(listener);
		}		
	}
        
        /**
	 * setta la ListaParallel
	 * @param stato
	 */
	public synchronized void setListaParallel(ListaParallel parallel) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (parallel != null) {
			parallel.removeListener(listener);
		}
		else return;
		
		if (listaParallel != null) {
			listaParallel.removeListener(listener);
		}
		listaParallel = parallel;
		if (listaParallel != null) {
			listaParallel.addListener(listener);
		}		
	}
        
        /**
	 * setta la ListaSim
	 * @param stato
	 */
	public synchronized void setListaSim(ListaSim sim) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (sim != null) {
			sim.removeListener(listener);
		}
		else return;
		
		if (listaSim != null) {
			listaSim.removeListener(listener);
		}
		listaSim = sim;
		if (listaSim != null) {
			listaSim.addListener(listener);
		}		
	}
        
        /**
	 * setta la ListaLoop
	 * @param stato
	 */
	public synchronized void setListaLoop(ListaLoop loop) {

		/**
		 * serve per eliminare l'eventualit� che il listener sia registrato 2 volte
		 */
		if (loop != null) {
			loop.removeListener(listener);
		}
		else return;
		
		if (listaLoop != null) {
			listaLoop.removeListener(listener);
		}
		listaLoop = loop;
		if (listaLoop != null) {
			listaLoop.addListener(listener);
		}		
	}
	
	
	
	/**
	 * ritorna la stringa del nome del trhread
	 * @return
	 */
	public String getNomeSequence() {
		return getName();
	}

	/**
	 * setta la stringa del nomethread
	 * @param string
	 */
	public void setNomeSequence(String string) {
		setName(string);
	}

	/**
	 * aggiunge un listener
	 * @param listener
	 */
	public void addListener(ISequenceElementListener listener) {
		this.listener.add(listener);
	}

	/**
	 * rimuove un listener
	 * @param listener
	 */
	public void removeListener(ISequenceElementListener listener) {
		this.listener.remove(listener);
	}

	/**
	 * rimuove tutti i  listener registrati
	 * @param listener
	 */
	public void removeAllListener() {
		this.listener.removeAllElements();
	}


	/**
	 * rimuove tutti gli elementi contenuti nel sequence
	 */
	public void removeAll() {
		boolean bo = plugData.getPlugDataManager().startSessione();
		
		this.listaClasse.removeAll();
		this.listaConstraint.removeAll();
                this.listaParallel.removeAll();
		this.listaSeqLink.removeAll();
		this.listaTime.removeAll();
                this.listaSim.removeAll();
                this.listaLoop.removeAll();
              
		
		plugData.getPlugDataManager().stopSessione(bo);

	}

        /**Memorizza nelle liste di tutti gli operatori paralleli , il nuovo link **/
        public void setLinkgood_allpar(){
            for(int i=0;i<listaParallel.size();i++){
                ElementoParallelo par =  listaParallel.getElement(i);
                par.setlist_link(this.getListaSeqLink().getListLinkSequence());
            }
            
            
        }
        
        /**Memorizza nelle liste di tutti gli operatori sim , il nuovo link **/
        public void setLinkgood_allsim(){
            for(int i=0;i<listaSim.size();i++){
                ElementoSim sim =  listaSim.getElement(i);
                sim.setlist_link(this.getListaSeqLink().getListLinkSequence());
            }
            
            
        }
        
        /**Memorizza nelle liste di tutti gli operatori loop , il nuovo link **/
        public void setLinkgood_allloop(){
            for(int i=0;i<listaLoop.size();i++){
                ElementoLoop loop =  listaLoop.getElement(i);
                loop.setlist_link(this.getListaSeqLink().getListLinkSequence());
            }
            
            
        }
        
        /**Cancella i paralleli selezionati **/
        public void removeAllSelected_Par(){
            for(int i=0;i<listaParallel.size();i++){
                ElementoParallelo par =  listaParallel.getElement(i);
                if(par.isSelected()){
                    listaParallel.removeElement(par);
                }
            }
        }
        
        /**Cancella i paralleli selezionati **/
        public void removeAllSelected_Constr(){
            for(int i=0;i<listaConstraint.size();i++){
                ElementoConstraint con =  listaConstraint.getElement(i);
                if(con.isSelected()){
                    listaConstraint.removeElement(con);
                }
            }
        }
        
        
        /**Cancella i simultanei selezionati **/
        public void removeAllSelected_Sim(){
            for(int i=0;i<listaSim.size();i++){
                ElementoSim sim =  listaSim.getElement(i);
                if(sim.isSelected()){
                    listaSim.removeElement(sim);
                }
            }
        }
        
        /**Cancella i loop selezionati **/
        public void removeAllSelected_Loop(){
            for(int i=0;i<listaLoop.size();i++){
                ElementoLoop Loop =  listaLoop.getElement(i);
                if(Loop.isSelected()){
                    listaLoop.removeElement(Loop);
                }
            }
        }
        
        /**Cancella i loop selezionati **/
        public void remove_Loop(ElementoLoop loop){
            for(int i=0;i<loop.size();i++)
            {
               loop.get(i).setLoop(false,0,0,0);                
            }
            listaLoop.removeElement(loop); 
            
        }


		public IPlugData getPlugData() {
			return plugData;
		}


        
        
}
