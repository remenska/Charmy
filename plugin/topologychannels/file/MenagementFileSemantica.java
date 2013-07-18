package plugin.topologychannels.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;

import org.xml.sax.Attributes;

import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.Utility;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;
import core.internal.runtime.data.IPlugData;

import plugin.topologychannels.TopologyWindow;
import plugin.topologychannels.data.ListaCanale;
import plugin.topologychannels.data.ListaProcesso;
import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.data.ElementoCanale;
import plugin.topologychannels.data.PlugData;

//import plugin.charmyfile.save.semantica.SalvaSemantica;

public class MenagementFileSemantica implements SerializableCharmyFile{

	private PlugData plugData;
	private TopologyWindow plug;
	
	private Utility util = new Utility();
	
    private boolean bTopology = false;
	private boolean bListaProcesso = false;
	private boolean bListaCanale = false;
	private boolean bElementoCanale = false;
	//elemento canale
    private int modality;
    private int tipo;
    private boolean flussodiretto;
    private String nome;
    private long id;
    private long idProcessoFrom;
    private long idProcessoTo;
    private long idProcessoDP;
    private long idStatoFrom;
    private long idStatoTo;
    private long fullIdProcessoStato;
	private long fullIdCanaleMessaggio;
	private int rff;
	
	public MenagementFileSemantica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SimpleValue getAttributeValue(SchemaEntry schemaEntry, String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		Object instanceElement = schemaEntry.getInstanceElement();
		
		///identificatori
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idProcessoStato)==0)){
			SimpleValue id =null;
			try {
				 id = new SimpleValue(plugin.topologychannels.resource.data.ElementoProcessoStato.getNumIstanze());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}
			
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idCanaleMessaggio)==0)){
			SimpleValue id =null;
			try {
				 id = new SimpleValue(plugin.topologychannels.resource.data.ElementoCanaleMessaggio.getNumIstanze());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}	  
			
		
		
		//semantica 
		if ((elementName.compareTo(Tag.elementoProcesso)==0)&& (attributeName.compareTo(Tag.att_EPR_appartenenza)==0))
		  return new SimpleValue(((ElementoProcesso)instanceElement).getAppartenenza());
		
		if ((elementName.compareTo(Tag.elementoProcesso)==0)&& (attributeName.compareTo(Tag.att_EPR_dummy)==0))
			  return new SimpleValue(((ElementoProcesso)instanceElement).isDummy());
		
		if ((elementName.compareTo(Tag.elementoProcesso)==0)&& (attributeName.compareTo(Tag.att_EPR_id)==0))
			  return new SimpleValue(Tag.att_EPR_pre_id+((ElementoProcesso)instanceElement).getId());
		
		if ((elementName.compareTo(Tag.elementoProcesso)==0)&& (attributeName.compareTo(Tag.att_EPR_modality)==0))
			  return new SimpleValue(((ElementoProcesso)instanceElement).getModality());
		
		if ((elementName.compareTo(Tag.elementoProcesso)==0)&& (attributeName.compareTo(Tag.att_EPR_nome)==0))
			  return new SimpleValue(((ElementoProcesso)instanceElement).getName());
		
		if ((elementName.compareTo(Tag.elementoProcesso)==0)&& (attributeName.compareTo(Tag.att_EPR_tipo)==0))
			  return new SimpleValue(((ElementoProcesso)instanceElement).getTipo());
		
		
		if ((elementName.compareTo(Tag.elementoCanale)==0)&& (attributeName.compareTo(Tag.att_ECA_flussodiretto)==0))
			  return new SimpleValue(((ElementoCanale)instanceElement).getFlussoDiretto());
		
		if ((elementName.compareTo(Tag.elementoCanale)==0)&& (attributeName.compareTo(Tag.att_ECA_id)==0))
			  return new SimpleValue(Tag.att_ECA_pre_id+((ElementoCanale)instanceElement).getId());
		
		if ((elementName.compareTo(Tag.elementoCanale)==0)&& (attributeName.compareTo(Tag.att_ECA_modality)==0))
			  return new SimpleValue(((ElementoCanale)instanceElement).getModality());
		
		if ((elementName.compareTo(Tag.elementoCanale)==0)&& (attributeName.compareTo(Tag.att_ECA_nome)==0))
			  return new SimpleValue(((ElementoCanale)instanceElement).getName());
		
		if ((elementName.compareTo(Tag.elementoCanale)==0)&& (attributeName.compareTo(Tag.att_ECA_tipo)==0))
			  return new SimpleValue(((ElementoCanale)instanceElement).getTipo());
		
		
		if ((elementName.compareTo(Tag.elementoProcessoFrom)==0)&& (attributeName.compareTo(Tag.att_EPFrom_idref)==0))
			  return new SimpleValue(Tag.att_EPR_pre_id+((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getElementFrom().getId());
		
		if ((elementName.compareTo(Tag.elementoProcessoTo)==0)&& (attributeName.compareTo(Tag.att_EPTo_idref)==0))
			  return new SimpleValue(Tag.att_EPR_pre_id+((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getElementTo().getId());
		
		
		return null;
	}

	public Object[] getList(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		Object parentInstance = schemaEntry.getParent().getInstanceElement();
		
		if (elementName.compareTo(Tag.elementoProcesso)==0){
			ArrayList result = new ArrayList();
			ListaProcesso listaProcesso = ((ListaProcesso)parentInstance);
			Iterator ite = listaProcesso.iterator();
			while(ite.hasNext()){
				ElementoProcesso ep =(ElementoProcesso)ite.next();
				result.add(ep);
			}
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if (elementName.compareTo(Tag.elementoCanale)==0){
			ArrayList result = new ArrayList();
			ListaCanale listaCanale = ((ListaCanale)parentInstance);
			Iterator ite = listaCanale.iterator();
			while(ite.hasNext()){
				ElementoCanale ec =(ElementoCanale)ite.next();
				result.add(ec);
			}
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		return null;
	}

	public Object getObject(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		if (elementName.compareTo(Tag.listaProcesso)==0)
			return (this.plugData).getListaProcesso();
			
			if (elementName.compareTo(Tag.listaCanale)==0)
				return (this.plugData).getListaCanale();
			
			return null;
	}

	public Object getElement(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPlugin(IMainTabPanel plugin) {
		this.plug = (TopologyWindow)plugin;
		this.plugData = (PlugData)plugin.getPlugData();
	}

	public void startElement(String elementName, Attributes attributes) {
	
		if (elementName.equals(Tag.identificatori)) {
			
			this.fullIdCanaleMessaggio = 
				Long.parseLong(attributes.getValue(Tag.idCanaleMessaggio));
			
			this.fullIdProcessoStato =
				Long.parseLong(attributes.getValue(Tag.idProcessoStato));
			
		}
		
		
		
		if (elementName.equals(Tag.topology)) {
			//controllo gli attributi
			bTopology = true;
			return;
		}
		
		//sto nella topologia
		if(bTopology){
			createTopology(elementName, attributes);
			return;
		}
		
		
	}
	
	public void characters(char[] buf, int offset, int len) {
		
		
	}

	public void endElement(String elementName) {
		
		
		if (elementName.equals(plugin.charmyfile.Tag.charmy)){
			
			ElementoProcesso.setNumIstanze(this.fullIdProcessoStato);		
			ElementoCanale.setNumIstanze(this.fullIdCanaleMessaggio);		
			
		}
		
		if (elementName.equals(Tag.topology)) {
			bTopology = false;
			return;
		}
			

		//end bTopology
		if(bTopology){
			if(elementName.equals(Tag.listaProcesso)){
				bListaProcesso = false;
			}
			if(elementName.equals(Tag.listaCanale)){
				bListaCanale = false;
			}
		}
		
		//end elementocanale
		if(bElementoCanale){
			if(elementName.equals(Tag.elementoCanale)){
				if(plugData!=null){

					ElementoCanale ec = 
						new ElementoCanale(
								plugData.getListaProcesso().getElementoById(idProcessoFrom), 
								plugData.getListaProcesso().getElementoById(idProcessoTo), 
							true, id, null); 
					ec.setName(nome);
					
					plugData.getListaCanale().addElement(ec);
					bElementoCanale = false;
				}
			}
			return;
		}

		
	}

	/**
	 * controlla la topologia
	 * @param eName  tag da controllare
	 * @param attrs Attributi
	 */
	private void createTopology(String eName, Attributes attrs){
		
		if(!bListaProcesso && eName.equals(Tag.listaProcesso)){
			bListaProcesso = true;
			return;
		}
		
		//debbo trovare l'elemento processo
		if(bListaProcesso){
			creaElementoProcesso(eName,attrs);
		}
		
		
		if(!bListaCanale && eName.equals(Tag.listaCanale)){
			bListaCanale = true;
			return;
		}
		
		//	devo trovare l'elemento processo
		if(bListaCanale){
			creaElementoCanale(eName,attrs);
		}
	}

	/**
	 * Elemento Processo
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void creaElementoProcesso(String eName, Attributes attrs){
		ElementoProcesso ep = new 
		ElementoProcesso(new Point(0,0),
				Integer.parseInt(attrs.getValue(Tag.att_EPR_appartenenza)),
				Integer.parseInt(attrs.getValue(Tag.att_EPR_tipo)),
				util.strToBool(attrs.getValue(Tag.att_EPR_dummy)), 
				attrs.getValue(Tag.att_EPR_nome),
				true,
				util.strToId(attrs.getValue(Tag.att_EPR_id)),
				null);		
		
		if(plugData!=null)
			plugData.getListaProcesso().addElement(ep);
		
	}

	/**
	 * ElementoCanale
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void creaElementoCanale(String eName, Attributes attrs){
		if(!bElementoCanale){
			modality = Integer.parseInt(attrs.getValue(Tag.att_ECA_modality));
			tipo 	= Integer.parseInt(attrs.getValue(Tag.att_ECA_tipo));
			id	= util.strToId(attrs.getValue(Tag.att_ECA_id));
			flussodiretto =util.strToBool(attrs.getValue(Tag.att_ECA_flussodiretto));
			nome = attrs.getValue(Tag.att_ECA_nome);
			bElementoCanale = true;
			return;
		}
		if(eName.equals(Tag.elementoProcessoFrom)){
			idProcessoFrom = util.strToId(attrs.getValue(Tag.att_EPFrom_idref));
			return;
		}
		
		if(eName.equals(Tag.elementoProcessoTo)){
			idProcessoTo = util.strToId(attrs.getValue(Tag.att_EPTo_idref));
			return;
		}
	}

	public void resetForNewFile() {
		plugin.topologychannels.resource.data.ElementoProcessoStato.setNumIstanze(0);
		plugin.topologychannels.resource.data.ElementoCanaleMessaggio.setNumIstanze(0);
		this.plug.resetForNewFile();
	}

}
