package plugin.sequencediagram.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.DefaultListModel;

import org.xml.sax.Attributes;

import plugin.sequencediagram.SequenceWindow;
import plugin.sequencediagram.data.ElementoClasse;
import plugin.sequencediagram.data.ElementoConstraint;
import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ElementoParallelo;
import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ElementoSim;
import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaClasse;
import plugin.sequencediagram.data.ListaConstraint;
import plugin.sequencediagram.data.ListaDS;
import plugin.sequencediagram.data.ListaLoop;
import plugin.sequencediagram.data.ListaParallel;
import plugin.sequencediagram.data.ListaSeqLink;
import plugin.sequencediagram.data.ListaSim;
import plugin.sequencediagram.data.ListaTime;
import plugin.sequencediagram.data.PlugData;
import plugin.sequencediagram.data.SequenceElement;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.Utility;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;
import core.internal.runtime.data.IPlugData;

public class MenagementFileSemantica implements SerializableCharmyFile{
	
	private PlugData plugData; 
	private SequenceWindow plug;
	
	private Utility util = new Utility();
	private boolean bListaDS = false;
	private boolean bSequenceElement = false;
	private boolean bListaClasse = false;
	private boolean bListaTime = false;
	private boolean bListaConstraint = false;
	private boolean bListaParallel = false;
	private boolean bListaSim = false;
	private boolean bListaLoop = false;
	private boolean bListaSeqLink = false;
	private boolean bElementoSeqLink = false;
	private boolean bElementoConstraint = false;
	private boolean bListaLinkConstraint = false;
	private boolean bElementoParallel = false;
	private boolean bElementoLoop = false;
	private String min;
	private String max;
	
	private boolean bListaLinkParallel = false;
	private boolean bElementoSim = false;
	private boolean bListaLinkSim = false;
	private boolean bListaLinkLoop = false;
	private DefaultListModel linksConstraint;
	private LinkedList linksParallel;
	//private DefaultListModel linksSim;
	private LinkedList linksSim;
	private LinkedList linksLoop;
	private SequenceElement sequenceElement = null;
	private int fromPos;
	private int toPos;
	private long idLink;
	
	//fine listaDS
	
	private long idTimeTo;
	private long idTimeFrom;
	private boolean isStrict = false;
	
	private int modality;
	
	private int tipo;
	private int type;
	private boolean flussodiretto;
	
	private String nome;
	
	private long id;
	
	private int rff;	
	
	private long idLinkFrom;
	
	private long idLinkTo;
	
	private long idProcessoFrom;
	
	private long idProcessoTo;
	
	private String constraintExpression;
	
	//private long idConstRef;
	
	private long idLinktRef;
	
	private long fullIdTime;
	private long fullIdSequence;
	
	private int fullIdConstraint;
	private int fullIdSim;
	private int fullIdLoop;
	private int fullIdParallel;
	
	private int bound_med;
	
	public MenagementFileSemantica() {
		
		
	}
	
	public void setPlugin(IMainTabPanel plugin) {
		this.plug = (SequenceWindow)plugin;
		this.plugData = (PlugData)plugin.getPlugData();
	}
	
	public SimpleValue getAttributeValue(SchemaEntry schemaEntry, String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName=schemaEntry.getElementParentName();
		Object instanceElement = schemaEntry.getInstanceElement();
		
		Object parentInstanceElement =null;
		if (schemaEntry.getParent()!=null)
			parentInstanceElement = schemaEntry.getParent().getInstanceElement();
		
		
		//identificatori		
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idSequence)==0)){
			SimpleValue id =null;
			try {
				id = new SimpleValue(plugin.sequencediagram.data.SequenceElement.getNumIstanze());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}	  
		
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idTime)==0)){
			SimpleValue id =null;
			try {
				id = new SimpleValue(plugin.sequencediagram.data.ElementoTime.getNumIstanze());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}	  
		
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idConstraint)==0)){
			SimpleValue id =null;
			try {
				id = new SimpleValue(plugin.sequencediagram.data.ElementoConstraint.getConstraintInstanceNumber());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}
		
		
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idSim)==0)){
			SimpleValue id =null;
			try {
				id = new SimpleValue(plugin.sequencediagram.data.ElementoSim.getSimInstanceNumber());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}	 
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idParallel)==0)){
			SimpleValue id =null;
			try {
				id = new SimpleValue(plugin.sequencediagram.data.ElementoParallelo.getParallelInstanceNumber());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}	 
		if ((elementName.compareTo(Tag.identificatori)==0)&& (attributeName.compareTo(Tag.idLoop)==0)){
			SimpleValue id =null;
			try {
				id = new SimpleValue(plugin.sequencediagram.data.ElementoLoop.getLoopInstanceNumber());
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return id;
		}	 
		
		
		///semantica
		if ((elementName.compareTo(Tag.sequenceElement)==0)&& (attributeName.compareTo(Tag.att_SEE_nome)==0))
			return new SimpleValue((((SequenceElement)instanceElement).getName()));
		
		if ((elementName.compareTo(Tag.sequenceElement)==0)&& (attributeName.compareTo(Tag.att_SEE_id)==0))
			return new SimpleValue((Tag.att_SEE_pre_id+((SequenceElement)instanceElement).getId()));
		
		if ((elementName.compareTo(Tag.elementoClasse)==0)&& (attributeName.compareTo(Tag.att_ECL_id)==0))
			return new SimpleValue((Tag.att_ECL_pre_id+((ElementoClasse)instanceElement).getId()));
		
		if ((elementName.compareTo(Tag.elementoClasse)==0)&& (attributeName.compareTo(Tag.att_ECL_nome)==0))
			return new SimpleValue((((ElementoClasse)instanceElement).getName()));
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&& (attributeName.compareTo(Tag.att_ETI_time)==0))
			return new SimpleValue((((ElementoTime)instanceElement).getTime()));
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&& (attributeName.compareTo(Tag.att_ETI_id)==0))
			return new SimpleValue((Tag.att_ETI_pre_id+((ElementoTime)instanceElement).getId()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_tipo)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).getTipo()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_fromPos)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).getPosFrom()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_toPos)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).getPosTo()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_loop)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).ctrlIfLoop()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_rrf)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).getRegReqFail()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_strict)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).isStrict()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_flussodiretto)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).getFlussoDiretto()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_id)==0))
			return new SimpleValue((Tag.att_ESL_pre_id+((ElementoSeqLink)instanceElement).getId()));
		
		if ((elementName.compareTo(Tag.elementoSeqLink)==0)&& (attributeName.compareTo(Tag.att_ESL_nome)==0))
			return new SimpleValue((((ElementoSeqLink)instanceElement).getName()));
		
		if ((elementName.compareTo(Tag.elementoSeqLinkPrec)==0)&& (attributeName.compareTo(Tag.att_ESL_slprec)==0)){
			
			ElementoSeqLink elSeqLink = (ElementoSeqLink)parentInstanceElement;			
			if(elSeqLink.getPrec()!=null){
				return new SimpleValue(Tag.att_ESL_pre_id + elSeqLink.getPrec().getId());
			}
		}
		
		
		if ((elementName.compareTo(Tag.elementoConstraint)==0)){
			
			ElementoConstraint constraint = (ElementoConstraint)schemaEntry.getInstanceElement();
			return this.getElementoConstraintAttribute(constraint,attributeName);
			
		}
		
		
		if ((elementName.compareTo(Tag.elementoLinkConstraint)==0)&& (elementParentName.compareTo(Tag.listaLinkConstraint)==0)&&(attributeName.compareTo(Tag.att_ELC_id)==0))
			return new SimpleValue(((String)instanceElement));
		
		
		if ((elementName.compareTo(Tag.elementoSim)==0)){
			
			ElementoSim sim = (ElementoSim)schemaEntry.getInstanceElement();
			return this.getElementoSimAttribute(sim,attributeName);
			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkSim)==0)&&  (elementParentName.compareTo(Tag.listaLinkSim)==0)&&(attributeName.compareTo(Tag.att_ELS_id)==0))
			return new SimpleValue(Tag.att_ESL_pre_id+(((Long)instanceElement).longValue()));
		
		
		if ((elementName.compareTo(Tag.elementoParallel)==0)){
			
			ElementoParallelo parallelo = (ElementoParallelo)schemaEntry.getInstanceElement();
			return this.getElementoParallelAttribute(parallelo,attributeName);
			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkParallel)==0)&& (elementParentName.compareTo(Tag.listaLinkParallel)==0)&&(attributeName.compareTo(Tag.att_ELP_id)==0))
			return new SimpleValue(Tag.att_ESL_pre_id+(((Long)instanceElement).longValue()));
		
		
		if ((elementName.compareTo(Tag.elementoLoop)==0)){
			
			ElementoLoop loop = (ElementoLoop)schemaEntry.getInstanceElement();
			return this.getElementoLoopAttribute(loop,attributeName);
			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkLoop)==0)&&  (elementParentName.compareTo(Tag.listaLinkLoop)==0)&&(attributeName.compareTo(Tag.att_ELL_id)==0))
			return new SimpleValue(Tag.att_ESL_pre_id+(((Long)instanceElement).longValue()));
		
		
		if ((elementName.compareTo(Tag.elementoClasseFrom)==0)&& (attributeName.compareTo(Tag.att_ESL_ecFrom)==0))
			return new SimpleValue((Tag.att_ECA_pre_id +((ElementoSeqLink)parentInstanceElement).getElementFrom().getId()));
		
		if ((elementName.compareTo(Tag.elementoClasseTo)==0)&& (attributeName.compareTo(Tag.att_ESL_ecTo)==0))
			return new SimpleValue((Tag.att_ECA_pre_id +((ElementoSeqLink)parentInstanceElement).getElementTo().getId()));
		
		if ((elementName.compareTo(Tag.elementoTimeFrom)==0)&& (attributeName.compareTo(Tag.att_ESL_etFrom)==0))
			return new SimpleValue((Tag.att_ETI_pre_id +((ElementoSeqLink)parentInstanceElement).getTimeFrom().getId()));
		
		if ((elementName.compareTo(Tag.elementoTimeTo)==0)&& (attributeName.compareTo(Tag.att_ESL_etTo)==0))
			return new SimpleValue((Tag.att_ETI_pre_id +((ElementoSeqLink)parentInstanceElement).getTimeTo().getId()));
		
		
		return null;
	}
	
	
	private SimpleValue getElementoConstraintAttribute(ElementoConstraint constraint, String attribute){
		
		
		if ((attribute.compareTo(Tag.att_ECO_id)==0))
			return new SimpleValue(Tag.att_ECO_pre_id + constraint.getId());
		
		if ((attribute.compareTo(Tag.att_ECO_nome)==0))
			return new SimpleValue(constraint.getLabel());
		
		if ((attribute.compareTo(Tag.att_ECO_eslFrom)==0))
			return new SimpleValue(Tag.att_ESL_pre_id + constraint.getLink().getId());
		
		if ((attribute.compareTo(Tag.constraintExpression)==0))
			return new SimpleValue(constraint.getConstraintExpression());
		
		
		return null;
	}
	
	private SimpleValue getElementoSimAttribute(ElementoSim sim, String attribute){
		
		
		if ((attribute.compareTo(Tag.att_ESI_id)==0))
			return new SimpleValue(Tag.att_ESI_pre_id + sim.getId());
		
		if ((attribute.compareTo(Tag.att_ESI_nome)==0))
			return new SimpleValue(sim.getLabel());
		
		
		return null;
	}
	
	private SimpleValue getElementoParallelAttribute(ElementoParallelo parallelo, String attribute){
		
		
		if ((attribute.compareTo(Tag.att_EPA_id)==0))
			return new SimpleValue(Tag.att_EPA_pre_id + parallelo.getId());
		
		if ((attribute.compareTo(Tag.att_EPA_nome)==0))
			return new SimpleValue(parallelo.getLabel());
		
		if ((attribute.compareTo(Tag.att_EPA_point_middle)==0))
			return new SimpleValue(parallelo.getBound_med());
		
		return null;
	}
	
	private SimpleValue getElementoLoopAttribute(ElementoLoop loop, String attribute){
		
		
		if ((attribute.compareTo(Tag.att_ELO_id)==0))
			return new SimpleValue(Tag.att_ELO_pre_id + loop.getId());
		
		if ((attribute.compareTo(Tag.att_ELO_min)==0))
			return new SimpleValue( loop.getMin());
		
		if ((attribute.compareTo(Tag.att_ELO_max)==0))
			return new SimpleValue( loop.getMax());
		
		
		return null;
	}
	
	
	public Object[] getList(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName=schemaEntry.getElementParentName();
		
		Object parentInstanceElement =null;
		if (schemaEntry.getParent()!=null)
			parentInstanceElement = schemaEntry.getParent().getInstanceElement();
		
		
		if ((elementName.compareTo(Tag.sequenceElement)==0) && (elementParentName.compareTo(Tag.listaDS)==0)){			
			
			ArrayList result = new ArrayList();
			ListaDS listaDs = ((ListaDS)parentInstanceElement);
			
			for (int i = 0; i < listaDs.size();i++){
				result.add(listaDs.get(i));
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoClasse)==0) && (elementParentName.compareTo(Tag.listaClasse)==0)){			
			
			ArrayList result = new ArrayList();
			ListaClasse listaClasse = ((ListaClasse)parentInstanceElement);
			
			Iterator ite = listaClasse.iterator();
			while(ite.hasNext()){
				ElementoClasse ec =(ElementoClasse)ite.next();
				result.add(ec);
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoTime)==0) && (elementParentName.compareTo(Tag.listaTime)==0)){			
			
			ArrayList result = new ArrayList();
			ListaTime listaTime = ((ListaTime)parentInstanceElement);
			
			Iterator ite = listaTime.iterator();
			while(ite.hasNext()){
				
				result.add(ite.next());
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		if ((elementName.compareTo(Tag.elementoSeqLink)==0) && (elementParentName.compareTo(Tag.listaSeqLink)==0)){			
			
			ArrayList result = new ArrayList();
			ListaSeqLink listaSeqLink = ((ListaSeqLink)parentInstanceElement);
			
			Iterator ite = listaSeqLink.iterator();
			while(ite.hasNext()){
				
				result.add(ite.next());
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoConstraint)==0) && (elementParentName.compareTo(Tag.listaConstraint)==0)){			
			
			ArrayList result = new ArrayList();
			ListaConstraint listaConstraint = ((ListaConstraint)parentInstanceElement);
			
			Iterator ite = listaConstraint.iterator();
			while(ite.hasNext()){
				
				result.add(ite.next());
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkConstraint)==0) && (elementParentName.compareTo(Tag.listaLinkConstraint)==0)){			
			
			ArrayList result = new ArrayList();
			ElementoConstraint constraint = ((ElementoConstraint)schemaEntry.getParent().getParent().getInstanceElement());
			LinkedList listaLinkConstraint =  constraint.getMsg();
			if (listaLinkConstraint!=null){
				for(int i=0;i<listaLinkConstraint.size();i++){
					result.add(listaLinkConstraint.get(i));
					
				}
				
				return (Object[]) result.toArray(
						new Object[result.size()]);
			}
			
		}
		
		if ((elementName.compareTo(Tag.elementoParallel)==0) && (elementParentName.compareTo(Tag.listaParallel)==0)){			
			
			ArrayList result = new ArrayList();
			ListaParallel listaParallel = ((ListaParallel)parentInstanceElement);
			
			Iterator ite = listaParallel.iterator();
			while(ite.hasNext()){
				
				result.add(ite.next());
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkParallel)==0) && (elementParentName.compareTo(Tag.listaLinkParallel)==0)){			
			
			ArrayList result = new ArrayList();
			ElementoParallelo parallel = ((ElementoParallelo)schemaEntry.getParent().getParent().getInstanceElement());
			LinkedList listaLinkParallel =  parallel.getList_mess();
			if (listaLinkParallel!=null){
				for(int i=0;i<listaLinkParallel.size();i++){
					result.add(new Long(((ElementoSeqLink)listaLinkParallel.get(i)).getId()));
					
				}
				
				return (Object[]) result.toArray(
						new Object[result.size()]);
			}
			
		}
		
		
		if ((elementName.compareTo(Tag.elementoSim)==0) && (elementParentName.compareTo(Tag.listaSim)==0)){			
			
			ArrayList result = new ArrayList();
			ListaSim listaSim = ((ListaSim)parentInstanceElement);
			
			Iterator ite = listaSim.iterator();
			while(ite.hasNext()){
				
				result.add(ite.next());
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkSim)==0) && (elementParentName.compareTo(Tag.listaLinkSim)==0)){			
			
			ArrayList result = new ArrayList();
			ElementoSim sim = ((ElementoSim)schemaEntry.getParent().getParent().getInstanceElement());
			LinkedList listaLinkSim =  sim.getList_mess();
			if (listaLinkSim!=null){
				for(int i=0;i<listaLinkSim.size();i++){
					result.add(new Long(((ElementoSeqLink)listaLinkSim.get(i)).getId()));
					
				}
				
				return (Object[]) result.toArray(
						new Object[result.size()]);
			}
			
		}
		
		if ((elementName.compareTo(Tag.elementoLoop)==0) && (elementParentName.compareTo(Tag.listaLoop)==0)){			
			
			ArrayList result = new ArrayList();
			ListaLoop listaLoop = ((ListaLoop)parentInstanceElement);
			
			Iterator ite = listaLoop.iterator();
			while(ite.hasNext()){
				
				result.add(ite.next());
			}						
			return (Object[]) result.toArray(
					new Object[result.size()]);			
		}
		
		if ((elementName.compareTo(Tag.elementoLinkLoop)==0) && (elementParentName.compareTo(Tag.listaLinkLoop)==0)){			
			
			ArrayList result = new ArrayList();
			ElementoLoop loop = ((ElementoLoop)schemaEntry.getParent().getParent().getInstanceElement());
			LinkedList listaLinkLoop =  loop.getList_mess();
			if (listaLinkLoop!=null){
				for(int i=0;i<listaLinkLoop.size();i++){
					result.add(new Long(((ElementoSeqLink)listaLinkLoop.get(i)).getId()));
					
				}
				
				return (Object[]) result.toArray(
						new Object[result.size()]);
			}
			
		}
		
		
		return null;
	}
	
	public Object getObject(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName=schemaEntry.getElementParentName();
		
		Object parentInstanceElement =null;
		if (schemaEntry.getParent()!=null)
			parentInstanceElement = schemaEntry.getParent().getInstanceElement();
		
		
		if (elementName.compareTo(Tag.listaDS)==0)
			return (this.plugData).getListaDS();
		
		if ((elementName.compareTo(Tag.listaClasse)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaClasse();
		
		if ((elementName.compareTo(Tag.listaTime)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaTime();
		
		if ((elementName.compareTo(Tag.listaSeqLink)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaSeqLink();
		
		if ((elementName.compareTo(Tag.listaConstraint)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaConstraint();
		
		if ((elementName.compareTo(Tag.listaParallel)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaParallel();
		
		if ((elementName.compareTo(Tag.listaSim)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaSim();
		
		if ((elementName.compareTo(Tag.listaLoop)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaLoop();
		
		return null;
	}
	
	public Object getElement(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void characters(char[] buf, int offset, int len) {
		// TODO Auto-generated method stub
		
	}
	
	public void endElement(String elementName) {
		
		if (elementName.equals(plugin.charmyfile.Tag.charmy)){
			
			ElementoTime.setNumIstanze(this.fullIdTime);		
			SequenceElement.setNumIstanze(this.fullIdSequence);		
			
			ElementoConstraint.setInstanceNumber(this.fullIdConstraint);
			ElementoSim.setInstanceNumber(this.fullIdSim);
			ElementoParallelo.setInstanceNumber(this.fullIdParallel);
			ElementoLoop.setInstanceNumber(this.fullIdLoop);
			
		}
		
		/*
		 *inizio eleborazione listaDS 
		 */
		if (elementName.equals(Tag.listaDS)) {
			bListaDS = false;
			return;
		}
		
		if (elementName.equals(Tag.sequenceElement)) {
			bSequenceElement = false;
			///plugin.sequencediagram.data.PlugData pd=(plugin.sequencediagram.data.PlugData)plugData.getPlugData("charmy.plugin.sequence");
			if(plugData!=null)
				plugData.getListaDS().add(sequenceElement);
			
			return;
		}
		
		
		if(elementName.equals(Tag.listaTime)){
			bListaTime = false;
			return;
		}
		
		if(elementName.equals(Tag.listaConstraint)){
			bListaConstraint = false;
			return;
		}
		
		if(elementName.equals(Tag.listaSim)){
			bListaSim = false;
			return;
		}
		
		if(elementName.equals(Tag.listaParallel)){
			bListaParallel = false;
			return;
		}
		
		if(elementName.equals(Tag.listaLoop)){
			bListaLoop = false;
			return;
		}
		
		if(elementName.equals(Tag.listaLinkConstraint)){
			bListaLinkConstraint = false;
			return;
		}
		
		if(elementName.equals(Tag.listaLinkSim)){
			bListaLinkSim = false;
			return;
		}
		if(elementName.equals(Tag.listaLinkParallel)){
			bListaLinkParallel = false;
			return;
		}
		if(elementName.equals(Tag.listaLinkLoop)){
			bListaLinkLoop = false;
			return;
		}		
		
		if(elementName.equals(Tag.listaClasse)){
			bListaClasse = false;
			return;
		}
		
		if(elementName.equals(Tag.listaSeqLink)){
			bListaSeqLink = false;
			return;
		}
		
		
		if(elementName.equals(Tag.elementoSeqLink)){
			bElementoSeqLink = false;
			ElementoSeqLink esl = new 
			ElementoSeqLink(
					"",
					sequenceElement.getListaClasse().getElementoById(idProcessoFrom),
					sequenceElement.getListaClasse().getElementoById(idProcessoTo),
					sequenceElement.getListaTime().getElementoById(idTimeFrom),
					sequenceElement.getListaTime().getElementoById(idTimeTo),
					fromPos,
					toPos,
					tipo,
					rff,
					nome,
					true,
					id,
					null);
			esl.setStrict(isStrict);
			if(sequenceElement.getListaSeqLink().size()>0)
				esl.setPrec((ElementoSeqLink)sequenceElement.getListaSeqLink().getLastElement());
			else
				esl.setPrec(null);
			sequenceElement.getListaSeqLink().addElement(esl);
			return;
		}
		
		//crea ElementoConstraint
		
		///NOTA: per tutti gli elementi : vengono ridisegnati alla fine, su restoreFromFile invocato sul fine tag del sequenceElement
		if(elementName.equals(Tag.elementoConstraint)){
			bElementoConstraint = false;
			
			ElementoSeqLink elSeqLink = (ElementoSeqLink)sequenceElement.getListaSeqLink().getElementById(idLink);
			ElementoConstraint ec = new ElementoConstraint(
					elSeqLink,
					nome,
					constraintExpression,
					true,                                       
					id, 
					type,null);//
			ec.setMsg(linksConstraint);		 
			if(sequenceElement!=null){
				
				if(sequenceElement.getListaConstraint().addElement(ec)){
					elSeqLink.setSelected(false);
					elSeqLink.addConstraint(ec);
					fullIdConstraint++; ///per compatibilità vecchi file
				}
				
			}
			return;
		}
		
		//crea ElementoSim
		if(elementName.equals(Tag.elementoSim)){
			bElementoSim = false;
			if(sequenceElement!=null){
				
				ElementoSim sim = new ElementoSim(linksSim,sequenceElement,id,null,true);///mettere tipo e non type?????
				if(sequenceElement.getListaSim().addElement(sim)){
					
					for (int i = 0; i < linksSim.size();i++){
						((ElementoSeqLink)linksSim.get(i)).setSimultaneous(true,id);
						
					}
					
					
				}
			}		
			
			return;
		}
		
//		crea ElementoParallelo
		if(elementName.equals(Tag.elementoParallel)){
			bElementoParallel = false;
			if(sequenceElement!=null){
				
				ElementoParallelo parallelo = new ElementoParallelo(linksParallel,sequenceElement,id,null,1,linksParallel.size()-1,true);
				if(sequenceElement.getListaParallel().addElement(parallelo)){
					
					for (int i = 0; i < linksParallel.size();i++){
						((ElementoSeqLink)linksParallel.get(i)).setParallel(true,id);
						
					}
					
				}
				parallelo.setMed(bound_med);
			}		
			
			return;
		}
		
		
//		crea ElementoLoop
		if(elementName.equals(Tag.elementoLoop)){
			bElementoLoop = false;
			if(sequenceElement!=null){
				ElementoLoop loop = new ElementoLoop(linksLoop,sequenceElement,id,(new Integer(min)).intValue(),(new Integer(max)).intValue(),null,true);///mettere tipo e non type?????
				if (sequenceElement.getListaLoop().addElement(loop)){
					
					for (int i = 0; i < linksLoop.size();i++){
						((ElementoSeqLink)linksLoop.get(i)).setLoop(true,(new Integer(min)).intValue(),(new Integer(max)).intValue(),id);
						
					}
					
				}
			}		
			
			return;
		}
		
	}
	
	public void startElement(String elementName, Attributes attributes) {
		
		if (elementName.equals(Tag.identificatori)) {
			
			this.fullIdTime =
				Long.parseLong(attributes.getValue(Tag.idTime));
			
			this.fullIdSequence =
				Long.parseLong(attributes.getValue(Tag.idSequence));
			
			if(attributes.getValue(Tag.idConstraint)!=null)
			this.fullIdConstraint =
				Integer.parseInt(attributes.getValue(Tag.idConstraint));
			else 
				this.fullIdConstraint =0;
			
			if(attributes.getValue(Tag.idSim)!=null)
			this.fullIdSim =
				Integer.parseInt(attributes.getValue(Tag.idSim));
			else 
				this.fullIdSim =0;
			
			if(attributes.getValue(Tag.idLoop)!=null)
			this.fullIdLoop =
				Integer.parseInt(attributes.getValue(Tag.idLoop));
			else 
				this.fullIdLoop =0;
			
			if(attributes.getValue(Tag.idParallel)!=null)
			this.fullIdParallel =
				Integer.parseInt(attributes.getValue(Tag.idParallel));
			else 
				this.fullIdParallel =0;
			
			return; 
		}
		
		/*
		 *Inizio eleborazione listaDS 
		 */
		if (elementName.equals(Tag.listaDS)) {
			//controllo gli attributi
			bListaDS = true;
			return;
		}
		
		//bListaDS
		if(bListaDS){
			createSequenceElement(elementName,  attributes);
			return;
		}
		
	}
	
	/*
	 * inizio eleborazione listaDS **************************************************************
	 * 
	 */
	
	/**
	 * Crea SequenceElement
	 * @param name
	 * @param attrs
	 */
	private void createSequenceElement(String name, Attributes attrs) {
		
		if(name.equals(Tag.listaLinkConstraint)){
			
			bListaLinkConstraint = true;				
			return;
		}
		
		if(name.equals(Tag.listaLinkSim)){
			//linksSim=new DefaultListModel();
			
			bListaLinkSim = true;				
			return;
		}
		
		if(name.equals(Tag.listaLinkParallel)){
			
			bListaLinkParallel = true;				
			return;
		}
		
		if(name.equals(Tag.listaLinkLoop)){
			
			bListaLinkLoop = true;				
			return;
		}
		
		if(bSequenceElement){
			/*
			 * crea i dati della lista time
			 */
			if(bListaTime){
				createListaTime(name, attrs);
				return;
			}
			
			/*
			 * crea i dati della listaConstraint
			 */
			if(bListaConstraint){				
				createListaConstraint(name, attrs);
				return;
			}
			if(bListaSim){				
				createListaSim(name, attrs);
				return;
			}
			if(bListaParallel){				
				createListaParallelo(name, attrs);
				return;
			}
			if(bListaLoop){				
				createListaLoop(name, attrs);
				return;
			}
			/*
			 * crea i dati della listaClasse
			 */
			if(bListaClasse){
				createListaClasse(name, attrs);
				return;
			}
			
			/*
			 * crea i dati della listaSeqLink
			 */
			if(bListaSeqLink){
				createElementoSeqLink(name, attrs);
				return;
			}
			
			if(bListaLinkConstraint){
				createListaLinkConstraint(name, attrs);
				return;
			}
			
			if(bListaLinkSim){
				createListaLinkSim(name, attrs);
				return;
			}
			
			if(bListaLinkParallel){
				createListaLinkParallelo(name, attrs);
				return;
			}
			
			if(bListaLinkLoop){
				createListaLinkLoop(name, attrs);
				return;
			}
			
			if(name.equals(Tag.listaTime)){
				bListaTime = true;
				return;
			}
			
			if(name.equals(Tag.listaConstraint)){
				bListaConstraint = true;
				return;
			}
			
			if(name.equals(Tag.listaSim)){
				bListaSim = true;
				return;
			}
			
			if(name.equals(Tag.listaParallel)){
				bListaParallel = true;
				return;
			}
			
			if(name.equals(Tag.listaLoop)){
				bListaLoop = true;
				return;
			}
			
			if(name.equals(Tag.listaClasse)){
				bListaClasse = true;
				return;
			}
			
			if(name.equals(Tag.listaSeqLink)){
				bListaSeqLink = true;
				return;
			}
			return;
		}
		
		if(name.equals(Tag.sequenceElement)){
			bSequenceElement = true;
			//	plugin.sequencediagram.data.PlugData pd=(plugin.sequencediagram.data.PlugData)plugData.getPlugData("charmy.plugin.sequence");
			if(plugData!=null){
				sequenceElement =new 
				SequenceElement(attrs.getValue(Tag.att_SEE_nome), plugData);
				sequenceElement.setId(util.strToId(attrs.getValue(Tag.att_SEE_id)));
			}			
		}
	}
	
	/**
	 * carica la listaTime
	 * @param name
	 * @param attrs
	 */
	private void createListaTime(String name, Attributes attrs) {
		if (name.equals(Tag.elementoTime)){
			ElementoTime et = new 
			ElementoTime(Long.parseLong(attrs.getValue(Tag.att_ETI_time)), 10, 
					10, true, 
					true,null );
			et.setId(util.strToId(attrs.getValue(Tag.att_ETI_id)));
			sequenceElement.getListaTime().addLast(et); 
		}
	}
	
	/**
	 * carica la listaClasse
	 * @param name
	 * @param attrs
	 */
	private void createListaClasse(String name, Attributes attrs) {
		if (name.equals(Tag.elementoClasse)){
			ElementoClasse ec = new 
			
			ElementoClasse(
					0,
					0,
					attrs.getValue(Tag.att_ECL_nome),
					true,
					util.strToId(attrs.getValue(Tag.att_ECL_id)),
					null) ;
			sequenceElement.getListaClasse().addElement(ec);
		}
	}
	
	/**
	 * ElementoSeqLink
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void createElementoSeqLink(String eName, Attributes attrs){
		if(!bElementoSeqLink){
			id	= util.strToId(attrs.getValue(Tag.att_ESL_id));
			flussodiretto =util.strToBool(attrs.getValue(Tag.att_ESL_flussodiretto));
			nome = attrs.getValue(Tag.att_ESL_nome);
			rff = Integer.parseInt(attrs.getValue(Tag.att_ESL_rrf));
			tipo 	= Integer.parseInt(attrs.getValue(Tag.att_ESL_tipo));
			isStrict = util.strToBool(attrs.getValue(Tag.att_ESL_strict));
			fromPos = Integer.parseInt(attrs.getValue(Tag.att_ESL_fromPos));
			toPos = Integer.parseInt(attrs.getValue(Tag.att_ESL_toPos));
			
			bElementoSeqLink = true;
			return;
		}
		
		if(eName.equals(Tag.elementoClasseFrom)){
			idProcessoFrom = util.strToId(attrs.getValue(Tag.att_ESL_ecFrom));
			return;
		}
		
		if(eName.equals(Tag.elementoClasseTo)){
			idProcessoTo = util.strToId(attrs.getValue(Tag.att_ESL_ecTo));
			return;
		}
		
		if(eName.equals(Tag.elementoTimeFrom)){
			idTimeFrom = util.strToId(attrs.getValue(Tag.att_ESL_etFrom));
			return;
		}
		
		
		if(eName.equals(Tag.elementoTimeTo)){
			idTimeTo = util.strToId(attrs.getValue(Tag.att_ESL_etTo));
			return;
		}
		
		
		/*	if(eName.equals(Tag.elementoConstraintRef)){
		 idConstRef = util.strToId(attrs.getValue(Tag.att_ESL_ecref));
		 return;
		 }*/
		
		
		if(eName.equals(Tag.elementoSeqLinkPrec)){
			idLinktRef = util.strToId(attrs.getValue(Tag.att_ESL_slprec));
			return;
		}
	}
	
	/**
	 * ElementoConstraint
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void createListaConstraint(String eName, Attributes attrs){
		if(!bElementoConstraint){
			
			linksConstraint=new DefaultListModel();
			id	= util.strToId(attrs.getValue(Tag.att_ECO_id));
			nome = attrs.getValue(Tag.att_ECO_nome);
			constraintExpression = attrs.getValue(Tag.constraintExpression);
			idLink = util.strToId(attrs.getValue(Tag.att_ECO_eslFrom));
			bElementoConstraint = true;
			return;
		}
		
		if(eName.equals(Tag.elementoClasseFrom)){
			idProcessoFrom = util.strToId(attrs.getValue(Tag.att_ESL_ecFrom));
			return;
		}
		
		if(eName.equals(Tag.elementoClasseTo)){
			idProcessoTo = util.strToId(attrs.getValue(Tag.att_ESL_ecTo));
			return;
		}
		
		if(eName.equals(Tag.elementoTimeFrom)){
			idTimeFrom = util.strToId(attrs.getValue(Tag.att_ESL_etFrom));
			return;
		}
		
		
		if(eName.equals(Tag.elementoTimeTo)){
			idTimeTo = util.strToId(attrs.getValue(Tag.att_ESL_etTo));
			return;
		}
		
		
		/*if(eName.equals(Tag.elementoConstraintRef)){
		 idConstRef = util.strToId(attrs.getValue(Tag.att_ESL_ecref));
		 return;
		 }*/
		
		
		if(eName.equals(Tag.elementoSeqLinkPrec)){
			idLinktRef = util.strToId(attrs.getValue(Tag.att_ESL_slprec));
			return;
		}
		if(bListaLinkConstraint){
			createListaLinkConstraint(eName, attrs);
			return;
		}
	}
	
	private void createListaSim(String eName, Attributes attrs){
		if(!bElementoSim){
			linksSim=new LinkedList();
			id	= util.strToId(attrs.getValue(Tag.att_ESI_id));
			nome = attrs.getValue(Tag.att_ESI_nome);
			bElementoSim = true;
			return;
		}	
		
		if(bListaLinkSim){
			createListaLinkSim(eName, attrs);
			return;
		}
	}
	
	private void createListaParallelo(String eName, Attributes attrs){
		if(!bElementoParallel){
			linksParallel=new LinkedList();
			id	= util.strToId(attrs.getValue(Tag.att_EPA_id));
			nome = attrs.getValue(Tag.att_EPA_nome);
			bound_med = new Integer(attrs.getValue(Tag.att_EPA_point_middle)).intValue();
			bElementoParallel = true;
			return;
		}
		
		if(bListaLinkParallel){
			createListaLinkParallelo(eName, attrs);
			return;
		}
	}
	
	private void createListaLoop(String eName, Attributes attrs){
		if(!bElementoLoop){
			linksLoop=new LinkedList();
			id	= util.strToId(attrs.getValue(Tag.att_ELO_id));
			max = attrs.getValue(Tag.att_ELO_max);
			min = attrs.getValue(Tag.att_ELO_min);
			bElementoLoop = true;
			return;
		}
		
		if(bListaLinkLoop){
			createListaLinkLoop(eName, attrs);
			return;
		}
	}
	
	
	/**
	 * ElementoConstraint
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void createListaLinkConstraint(String eName, Attributes attrs){
		if(eName.equals(Tag.elementoLinkConstraint)){
			linksConstraint.addElement(attrs.getValue(Tag.att_ELC_id));
			return;
		}
		
	}
	
	private void createListaLinkSim(String eName, Attributes attrs){
		if(eName.equals(Tag.elementoLinkSim)){
			ElementoSeqLink esl=(ElementoSeqLink)sequenceElement.getListaSeqLink().getElementById(util.strToId(attrs.getValue(Tag.att_ELS_id)));
			linksSim.add(esl);
			return;
		}
		
	}
	
	private void createListaLinkParallelo(String eName, Attributes attrs){
		if(eName.equals(Tag.elementoLinkParallel)){
			ElementoSeqLink esl=(ElementoSeqLink)sequenceElement.getListaSeqLink().getElementById(util.strToId(attrs.getValue(Tag.att_ELP_id)));
			linksParallel.add(esl);
			return;
		}
		
	}
	
	private void createListaLinkLoop(String eName, Attributes attrs){
		if(eName.equals(Tag.elementoLinkLoop)){
			ElementoSeqLink esl=(ElementoSeqLink)sequenceElement.getListaSeqLink().getElementById(util.strToId(attrs.getValue(Tag.att_ELL_id)));
			linksLoop.add(esl);
			return;
		}
		
	}
	
	
	public void resetForNewFile() {
		plugin.sequencediagram.data.SequenceElement.setNumIstanze(0);
		plugin.sequencediagram.data.ElementoTime.setNumIstanze(0);
		plugin.sequencediagram.data.ElementoParallelo.setInstanceNumber(0);
		plugin.sequencediagram.data.ElementoSim.setInstanceNumber(0);
		plugin.sequencediagram.data.ElementoLoop.setInstanceNumber(0);
		plugin.sequencediagram.data.ElementoConstraint.setInstanceNumber(0);
		
		this.plug.resetForNewFile();
		
	}
	
}
