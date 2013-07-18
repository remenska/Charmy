package plugin.statediagram.file;

import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.Utility;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;
import core.internal.runtime.data.IPlugData;


import plugin.statediagram.StateWindow;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.data.PlugData;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.xml.sax.Attributes;

public class MenagementFileSemantica implements SerializableCharmyFile{

	
	private PlugData plugData;
	private StateWindow plug;
	
	private Utility util = new Utility();
	private boolean bListaDP = false;
	private boolean bThreadElement = false;
	private boolean bListaStato = false;
	private boolean bElementoStato = false;
	private boolean bListaMessaggio = false;
	private boolean bElementoMessaggio = false;
	private long idProcessoDP;
	private long idStatoFrom;
	private long idStatoTo;
	private String onEntryCode;
	private String onExitCode;
	private ThreadElement threadElement;
	private ListaThread listaThread;
	private int modality;

	private int tipo;

	private boolean flussodiretto;

	private String nome;

	private long id;

	private LinkedList parameters=new LinkedList();;

	private boolean bOperations = false;

	private boolean bParameters = false;



	private String guard;

	private String operations;

	private boolean bGuard = false;

	private boolean bElementoParam = false;

	private int rff;

	private boolean bOnEntryCode = false;
	private boolean bOnExitCode;

	public MenagementFileSemantica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setPlugin(IMainTabPanel plugin) {
		this.plug = (StateWindow)plugin;
		this.plugData = (PlugData)plugin.getPlugData();
	}

	public SimpleValue getAttributeValue(SchemaEntry schemaEntry, String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		Object instanceElement = schemaEntry.getInstanceElement();
		Object parentInstanceElement =null;
		if (schemaEntry.getParent()!=null)
		 parentInstanceElement = schemaEntry.getParent().getInstanceElement();
		
		if ((elementName.compareTo(Tag.listaThread)==0)&& (attributeName.compareTo(Tag.att_LT_processo)==0))
			 return new SimpleValue((Tag.att_EPR_pre_id+ ((ListaThread)instanceElement).getIdProcesso()));
		
		if ((elementName.compareTo(Tag.threadElement)==0)&& (attributeName.compareTo(Tag.att_TE_nome)==0))
			 return new SimpleValue((((ThreadElement)instanceElement).getNomeThread()));
		
		if ((elementName.compareTo(Tag.threadElement)==0)&& (attributeName.compareTo(Tag.att_TE_numStato)==0))
			 return new SimpleValue((new Long(((ThreadElement)instanceElement).getNumStato()).toString()));
		
		if ((elementName.compareTo(Tag.elementoStato)==0)&& (attributeName.compareTo(Tag.att_EST_id)==0))
			 return new SimpleValue((Tag.att_EST_pre_id+ ((ElementoStato)instanceElement).getId()));
		
		if ((elementName.compareTo(Tag.elementoStato)==0)&& (attributeName.compareTo(Tag.att_EST_tipo)==0))
			 return new SimpleValue((((ElementoStato)instanceElement).getTipo()));
		
		if ((elementName.compareTo(Tag.elementoStato)==0)&& (attributeName.compareTo(Tag.att_EST_nome)==0))
			 return new SimpleValue((((ElementoStato)instanceElement).getName()));
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0)&& (attributeName.compareTo(Tag.att_EME_tipo)==0))
			 return new SimpleValue((((ElementoMessaggio)instanceElement).getTipo()));
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0)&& (attributeName.compareTo(Tag.att_EME_nome)==0))
			 return new SimpleValue((((ElementoMessaggio)instanceElement).getName()));
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0)&& (attributeName.compareTo(Tag.att_EME_flussodiretto)==0))
			 return new SimpleValue((((ElementoMessaggio)instanceElement).getFlussoDiretto()));
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0)&& (attributeName.compareTo(Tag.att_EME_sendReceive)==0))
			 return new SimpleValue((((ElementoMessaggio)instanceElement).getSendReceive()));
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0)&& (attributeName.compareTo(Tag.att_EME_rff)==0))
			 return new SimpleValue((((ElementoMessaggio)instanceElement).getMsgRRF()));
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0)&& (attributeName.compareTo(Tag.att_EME_id)==0))
			 return new SimpleValue((Tag.att_EME_pre_id+((ElementoMessaggio)instanceElement).getId()));
		
		if ((elementName.compareTo(Tag.elementoStatoFrom)==0)&& (attributeName.compareTo(Tag.att_ESFrom_idref)==0))
			 return new SimpleValue((Tag.att_EST_pre_id+((ElementoMessaggio)parentInstanceElement).getElementFrom().getId()));
		
		if ((elementName.compareTo(Tag.elementoStatoTo)==0)&& (attributeName.compareTo(Tag.att_ESTo_idref)==0))
			 return new SimpleValue((Tag.att_EST_pre_id+((ElementoMessaggio)parentInstanceElement).getElementTo().getId()));
		
		
		return null;
	}

	public Object[] getList(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName=schemaEntry.getElementParentName();
		
		Object parentInstanceElement =null;
		if (schemaEntry.getParent()!=null)
		 parentInstanceElement = schemaEntry.getParent().getInstanceElement();
		
		
		
		if ((elementName.compareTo(Tag.listaThread)==0) && (elementParentName.compareTo(Tag.listaDP)==0)){			
						
			ArrayList result = new ArrayList();
			ListaDP listaDp = ((ListaDP)parentInstanceElement);
			
			for (int i = 0; i < listaDp.size();i++){
				result.add(listaDp.get(i));
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		} 
		
		
		if ((elementName.compareTo(Tag.threadElement)==0) && (elementParentName.compareTo(Tag.listaThread)==0)){			
			
			ArrayList result = new ArrayList();
			ListaThread listaThread = ((ListaThread)parentInstanceElement);
			
			Iterator ite = listaThread.iterator();
			while(ite.hasNext()){
				ThreadElement te =(ThreadElement)ite.next();
				result.add(te);
			}									
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		 
		if ((elementName.compareTo(Tag.elementoStato)==0) && (elementParentName.compareTo(Tag.listaStato)==0)){			
			
			ArrayList result = new ArrayList();
			
			ListaStato listaStato = ((ListaStato)parentInstanceElement);
			Iterator ite = listaStato.iterator();
			while(ite.hasNext()){
				ElementoStato es =(ElementoStato)ite.next();
				result.add(es);
			}									
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoMessaggio)==0) && (elementParentName.compareTo(Tag.listaMessaggio)==0)){			
			
			ArrayList result = new ArrayList();
			
			ListaMessaggio listaMessaggio = ((ListaMessaggio)parentInstanceElement);
			Iterator ite = listaMessaggio.iterator();
			while(ite.hasNext()){
				ElementoMessaggio em =(ElementoMessaggio)ite.next();
				result.add(em);
			}									
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoParam)==0) && (elementParentName.compareTo(Tag.parametri)==0)){			
			
			LinkedList listaParametri =  ((ElementoMessaggio)(schemaEntry.getParent().getParent().getInstanceElement())).getParameters();
			
			ArrayList result = new ArrayList();
			Iterator ite = listaParametri.iterator();
			while(ite.hasNext()){				
				result.add(ite.next());
			}	
			
			return (Object[]) result.toArray(
					new Object[result.size()]);	
			
		}
		
		return null;
	}

	public Object getObject(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName=schemaEntry.getElementParentName();
		
		Object parentInstanceElement =null;
		if (schemaEntry.getParent()!=null)
		 parentInstanceElement = schemaEntry.getParent().getInstanceElement();
		
		
		if (elementName.compareTo(Tag.listaDP)==0)
			return (this.plugData).getListaDP();
		//if ((elementName.compareTo(Tag.listaThread)==0)&& (elementParentName.compareTo(Tag.listaDP)==0))
			//  return (this.plugData).getListaDP();
		
		if ((elementName.compareTo(Tag.listaStato)==0)&& (elementParentName.compareTo(Tag.threadElement)==0))
			 return ((ThreadElement)parentInstanceElement).getListaStato();
		
		if ((elementName.compareTo(Tag.listaMessaggio)==0)&& (elementParentName.compareTo(Tag.threadElement)==0))
			 return ((ThreadElement)parentInstanceElement).getListaMessaggio();
		
	//	if ((elementName.compareTo(Tag.threadElement)==0)&& (elementParentName.compareTo(Tag.listaThread)==0))
		//	 return ((ListaThread)parentInstance).getThreadElement();
		
		return null;
	}

	public Object getElement(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementName.compareTo(Tag.elementoParam)==0){
			
			return (String)schemaEntry.getInstanceElement();
		}
			
		if (elementName.compareTo(Tag.guard)==0){
			
			String guard = ((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getGuard();
			if (guard.equals(""))
				return null;
			return guard;
		}	
		
		if (elementName.compareTo(Tag.operations)==0){
			String operation =  ((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getOperations();
			
			if (operation.equals(""))
				return null;
			return operation;
		}
		
		if (elementName.compareTo(Tag.onEntryCode)==0){
			
			return ((ElementoStato)schemaEntry.getParent().getInstanceElement()).getOnEntryCode();
		}
		
		if (elementName.compareTo(Tag.onExitCode)==0){
			
			return ((ElementoStato)schemaEntry.getParent().getInstanceElement()).getOnExitCode();
		}

		return null;
	}

	public void characters(char[] buf, int offset, int len) {
		
		if(bOnEntryCode){
			onEntryCode = new String(buf,offset,len);
			bOnEntryCode=false;
		}
		if(bOnExitCode){
			onExitCode = new String(buf,offset,len);
			bOnExitCode=false;
		}
		
		if(bGuard){
			guard=new String(buf,offset,len);
			bGuard=false;
		}
		if(bOperations){
			operations=new String(buf,offset,len);
			bOperations=false;
		}
		if(bElementoParam){
			
			parameters.add(new String(buf,offset,len));
			bElementoParam=false;
		}
		
	}

	public void endElement(String elementName) {
		
		if (elementName.equals(Tag.listaDP)) {
			bListaDP = false;
			return;
		}
		
		
		if (elementName.equals(Tag.threadElement)) {
			bThreadElement = false;
			return;
		}		
		
		if (elementName.equals(Tag.listaStato)) {
			bListaStato = false;
			return;
		}			
		
		
		if (elementName.equals(Tag.elementoStato)) {
			bElementoStato = false;
			
			ElementoStato es = new
			     ElementoStato(
					new Point(0,0),
					tipo,
					nome,
					true,
				    id,
					null);
			es.setOnEntryCode(onEntryCode);
			es.setOnExitCode(onExitCode);
			
			threadElement.getListaStato().addElement(es);
			return;
		}			
		
		//lista Messaggio
		if (elementName.equals(Tag.listaMessaggio)) {
			bListaMessaggio = false;
			return;
		}		

		if (elementName.equals(Tag.elementoMessaggio)) {
			ElementoStato from = 
				(ElementoStato)threadElement.getListaStato().getElementoById(idStatoFrom);
			
			ElementoStato to =
				(ElementoStato)threadElement.getListaStato().getElementoById(idStatoTo);
			
			bElementoMessaggio = false;
			ElementoMessaggio em = new
		          ElementoMessaggio(
		          	threadElement,
					from,
					to,
					tipo,
					nome,
					rff,
					true,
					id,
					null);		
			em.setGuard(guard);
			em.setOperations(operations);
			em.setParameters(parameters);		
			em.setSendReceive(modality);
			
			threadElement.getListaMessaggio().addElement(em);
			guard="";
			operations="";
			parameters=new LinkedList();			
			return;
		}			
		//fine ListaMessaggio
		
		
	}

	public void startElement(String elementName, Attributes attributes) {
		/*
		 * inizio listaDP
		 */
		if (elementName.equals(Tag.listaDP)) {
			//controllo gli attributi
			bListaDP = true;
			return;
		}

		//bListaDP
		if(bListaDP){
			createThreadElement(elementName,  attributes);			
			return;
		}
		
	}

	/**
	 * estrai i dati relativi a thread element
	 * @param eName  tag da controllare
	 * @param attrs Attributi
	 */
	private void createListaDP(String eName, Attributes attrs){
	
		createThreadElement(eName, attrs);
	}

	/**
	 * controlla ThreadElement 
	 * @param eName  tag da controllare
	 * @param attrs Attributi
	 */
	private void createThreadElement(String eName, Attributes attrs){
		//thread element
		if(bThreadElement){
			
			if(bListaStato){
				if(!bElementoStato){
					tipo 	= Integer.parseInt(attrs.getValue(Tag.att_EST_tipo));
					id	= util.strToId(attrs.getValue(Tag.att_EST_id));
					nome = attrs.getValue(Tag.att_EST_nome);
					bElementoStato =true;
					return;
				}
				if(eName.equals(Tag.onEntryCode)){
	
					bOnEntryCode = true;
					return;
				}
				if(eName.equals(Tag.onExitCode)){
	
					bOnExitCode = true;
					return;
				}
				
			}
			
			//sto nei messaggi
			if(bListaMessaggio){
				if(!bElementoMessaggio){
				    //primo elemento messaggio incontrato	
					tipo 	= Integer.parseInt(attrs.getValue(Tag.att_EME_tipo));
					id	= util.strToId(attrs.getValue(Tag.att_EME_id));
					nome = attrs.getValue(Tag.att_EME_nome);		
					modality = Integer.parseInt(attrs.getValue(Tag.att_EME_sendReceive));
					flussodiretto =util.strToBool(attrs.getValue(Tag.att_EME_flussodiretto));
					rff = Integer.parseInt(attrs.getValue(Tag.att_EME_rff));
				   bElementoMessaggio = true;
				   return;
				}
				
				if(eName.equals(Tag.elementoStatoFrom)){
					idStatoFrom	= util.strToId(attrs.getValue(Tag.att_ESFrom_idref));
					return;
				}
				
				if(eName.equals(Tag.elementoStatoTo)){
					idStatoTo	= util.strToId(attrs.getValue(Tag.att_ESTo_idref));
					return;
				}
				
				if(eName.equals(Tag.guard)){					
					bGuard = true;
					return;
				}
				
				if(eName.equals(Tag.parametri)){
					parameters=new LinkedList();
					bParameters=true;
					return;
				}
				
				if(eName.equals(Tag.elementoParam)){
					bElementoParam=true;
					return;
				}
				
				if(eName.equals(Tag.operations)){
					bOperations=true;
					return;
				}
			}
			
	
			if(eName.equals(Tag.listaStato)){
				bListaStato = true;
				return;
			}
	
			if(eName.equals(Tag.listaMessaggio)){
				bListaMessaggio = true;
				return;
			}
			
		} //fine thread Element
		
	
		//preleva gli attributi di threadElement e lo costruisce
		if(eName.equals(Tag.threadElement)){
			bThreadElement = true;
			if(plugData!=null){
				listaThread = plugData.getListaDP().getListaThread(idProcessoDP);
				
				threadElement =new ThreadElement(
						attrs.getValue(Tag.att_TE_nome),
						listaThread,
						plugData);
				threadElement.setNumStato(new Long(attrs.getValue(Tag.att_TE_numStato)).longValue());		
				listaThread.add(threadElement);
			}
			return;
		}
		
		//preleva il riferimento al processo
		if(eName.equals(Tag.listaThread)){
			idProcessoDP = util.strToId(attrs.getValue(Tag.att_LT_processo));
			return;
		}
		
	}

	public void resetForNewFile() {
		this.plug.resetForNewFile();
	}

}
