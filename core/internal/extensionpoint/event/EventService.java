/*
 * Created on 8-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.extensionpoint.event;

import java.util.Vector;


/**
 * @author eziolotta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public  class EventService {
	
	
	
	/**
	 * listEventSubscribed[i] = Vector()
	 * A) se listEventSubscribed[i].size = 2 -> Vector[0]= Vector(): lista subscriber dell'evento - Vector[1]=String idEvento: evento sottoscritto
	 * B) se listEventSubscribed[i].size = 3 -> Vector[0]= Vector(): lista subscriber dell'evento - Vector[1]=String idEvento: evento sottoscritto - Verctor[2] = Integer localEventoID: id evento ridefinito da un host, filtro su se stesso 
	 */
	private Vector listEventSubscribed;
	
	
	/**
	 * 
	 */
	public EventService() {
		
		//risparmio memoria, settiamo a zero e ogni volta che aggiungiamo un elemento, il vettore aumenta il size di 1
				
		listEventSubscribed = new Vector(0);	
	}
	
	
	/**
	 * 
	 * @param event
	 */
	public   void pub(EventBase event) {		
		
		String eventID = event.getEventID();
		
		for(int i=0;i<listEventSubscribed.size();i++){	
			
			Vector dataSubscriber = (Vector)listEventSubscribed.get(i);
			
			if(((String)dataSubscriber.get(1)).compareTo(eventID)==0){
				
				if ((event.getLocalHostID()!="")&&
						(dataSubscriber.size()==3)){
					
					if(event.getLocalHostID() == ((String)dataSubscriber.get(2))){
						// NOTIFICA AI SUBSCRIBER : EVENTO LOCALE
						Vector listaSubscriber = (Vector)dataSubscriber.get(0);
						
						for(int j=0;j<listaSubscriber.size();j++){				
							((ISubscriberListener)listaSubscriber.get(j)).notify(event);
						}
					}					
				}
				else{
					//NOTIFICA AI SUBSCRIBER 
					Vector listaSubscriber = (Vector)dataSubscriber.get(0);
					
					for(int j=0;j<listaSubscriber.size();j++){				
						((ISubscriberListener)listaSubscriber.get(j)).notify(event);
					}					
				}					
			}
			
		}
	}
	
	
	
	/**
	 * 
	 * @param subscriber sottoscrittore dell'evento
	 * @param eventID - EventID classe che gestisce l'id complesso dell'evento
	 */
	public void sub(ISubscriberListener subscriber, String eventID) {
		
		boolean trovato=false;
		
		for(int i=0;i<listEventSubscribed.size();i++){			
			Vector dataSubscriber = (Vector)listEventSubscribed.get(i);
			
			if(((String)dataSubscriber.get(1)).compareTo(eventID)==0){
				trovato = true;
				
				Vector listaSubscriber = (Vector)dataSubscriber.get(0);		      	
				listaSubscriber.add(subscriber);		      	
				break;
			}			
		}
		
		if (!trovato){
			
			Vector sub = new Vector(0);								
			sub.add(subscriber);
			
			Vector dataEventSub = new Vector(2);
			dataEventSub.add(0,sub);
			dataEventSub.add(1,eventID);
			
			listEventSubscribed.add(dataEventSub);
		}
	}
	
		
	/**
	 * 
	 * @param subscriber sottoscrittore dell'evento
	 * @param eventID - EventID classe che gestisce l'id complesso dell'evento
	 */
	public void sub(ISubscriberListener subscriber, String eventID, String localHostID) {
		
		boolean trovato=false;
		
		for(int i=0;i<listEventSubscribed.size();i++){			
			Vector dataSubscriber = (Vector)listEventSubscribed.get(i);
			
			if(dataSubscriber.size()==3){
				
				if ((((String)dataSubscriber.get(1)).compareTo(eventID)==0)&&
						(((String)dataSubscriber.get(2)).compareTo(localHostID)==0)){				
							
							trovato = true;
							
							Vector listaSubscriber = (Vector)dataSubscriber.get(0);		      	
							listaSubscriber.add(subscriber);		      	
							break;
						}
				}						
		}
		
		if (!trovato){
			
			Vector sub = new Vector(0);								
			sub.add(subscriber);
			
			Vector dataEventSub = new Vector(3);
			dataEventSub.add(0,sub);
			dataEventSub.add(1,eventID);
			dataEventSub.add(2,localHostID);
			
			listEventSubscribed.add(dataEventSub);
		}
	}
	
	
	/**
	 * unSubscribe - al subscriber non arriverrano più notifiche relative all'evento con identificatore passato come argomento 
	 * @param subscriber
	 * @param eventID
	 */
	public  void unSub(ISubscriberListener subscriber, String eventID){
		
		
		for(int i=0;i<listEventSubscribed.size();i++){			
			Vector dataSubscriber = (Vector)listEventSubscribed.get(i);
			
			if (dataSubscriber.size()==2){
				
				if(((String)dataSubscriber.get(1)).compareTo(eventID)==0){
					
					Vector listaSubscriber= (Vector)dataSubscriber.get(1);
					
					for(int j=0;j<listaSubscriber.size();j++){
						
						if (listaSubscriber.get(j).equals(subscriber)){							
							listaSubscriber.remove(j);
						}
					}				
				}
			}			
			break;
		}		
	}		
	
	
	/**
	 * Rimuove il subscriber dalla lista
	 * @param subscriber
	 */
	public  void unSubAll(ISubscriberListener subscriber){
		
		for(int i=0;i<listEventSubscribed.size();i++){			
			Vector dataSubscriber = (Vector)listEventSubscribed.get(i);
			
			Vector listaSubscriber= (Vector)dataSubscriber.get(0);
			
			for(int j=0;j<listaSubscriber.size();j++){
				
				if (listaSubscriber.get(j).equals(subscriber)){
					
					listaSubscriber.remove(j);
				}
			}	
		}		
	}
	
}
