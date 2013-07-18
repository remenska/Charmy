package plugin.sequencediagram.file;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

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
import plugin.sequencediagram.graph.GraficoTipoSequence;
import plugin.topologydiagram.resource.graph.ElementoBox;
import plugin.topologydiagram.resource.graph.ElementoBoxTesto;
import plugin.topologydiagram.resource.graph.GraficoLink;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.Utility;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;
import core.internal.runtime.data.IPlugData;

public class MenagementFileGrafica implements SerializableCharmyFile {
	
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
	
	private boolean bElementoConstraint = false;	 
	private boolean bElementoParallel = false;    
	private boolean bElementoSim = false;
	private boolean bElementoLoop = false;
		
	private SequenceElement sequenceElement = null;
	private ElementoBox ebox;
	private GraficoLink gLink;
	private String fontNome;
	private int fontSize;
	private int fontStyle;
	private Color fontColor;
	private boolean inFont = false;
	private boolean inLine = false;
	private int width;
	private int lineWeight;
	private int height;
	private int lineTheme;
	private boolean bDefault;
	private Color backGr;
	private Color lineColor;
	private boolean inLink;
	
	///attributi elementoBoxPSC
	
	private int topX;
	private int topY;
	private int width_Op;
	private int height_Op;
	
	//att 
	private long id;
	private int type_con;
	private int point_start_mid;
	
	public MenagementFileGrafica() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SimpleValue getAttributeValue(SchemaEntry schemaEntry, String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		String elementParentName = schemaEntry.getElementParentName();
		
		if ((elementName.compareTo(Tag.class_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Height)==0))
			return new SimpleValue(GraficoTipoSequence.getDEFAULT_HEIGHT()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.class_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Width)==0))
			return new SimpleValue(GraficoTipoSequence.getDEFAULT_WIDTH()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.eBoxBkColor)==0))
			return this.getBkColorValue(schemaEntry,attributeName);
		
		if ((elementName.compareTo(Tag.color)==0))
			return this.getColorValue(schemaEntry,attributeName);
		
		if ((elementName.compareTo(Tag.line)==0))
			return this.getLineValue(schemaEntry,attributeName);
		
		if ((elementName.compareTo(Tag.font)==0))
			return this.getFontValue(schemaEntry,attributeName);
		
		
		if (elementName.compareTo(Tag.elementoBox)==0)
			return this.getElementoBoxValue(schemaEntry,attributeName);
		////////////////
		//if ((elementName.compareTo(Tag.elementoBoxPSC)==0)&&(elementParentName.compareTo(Tag.elementoParallel)==0))
			//return this.getElementoBoxParallelValue(schemaEntry,attributeName);
		
		/*if ((elementName.compareTo(Tag.elementoBoxPSC)==0)&&(elementParentName.compareTo(Tag.elementoSim)==0))
			return this.getElementoBoxParallelValue(schemaEntry,attributeName);
		
		if ((elementName.compareTo(Tag.elementoBoxPSC)==0)&&(elementParentName.compareTo(Tag.elementoLoop)==0))
			return this.getElementoBoxParallelValue(schemaEntry,attributeName);
		*/
		////////////////////
		if ((elementName.compareTo(Tag.graficoLink)==0)&&(attributeName.compareTo(Tag.att_graficoLink_idref)==0))
			return new SimpleValue(Tag.att_ESL_pre_id+ ((ElementoSeqLink)schemaEntry.getInstanceElement()).getId()); 
		
		if ((elementName.compareTo(Tag.sequenceElement)==0)&&(attributeName.compareTo(Tag.att_SEE_nome)==0))
			return new SimpleValue(((SequenceElement)schemaEntry.getInstanceElement()).getName()); 
		
		if ((elementName.compareTo(Tag.sequenceElement)==0)&&(attributeName.compareTo(Tag.att_SEE_id)==0))
			return new SimpleValue(Tag.att_SEE_pre_id+ ((SequenceElement)schemaEntry.getInstanceElement()).getId()); 
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&&(attributeName.compareTo(Tag.att_ETI_id)==0))
			return new SimpleValue(Tag.att_ETI_pre_id+ ((ElementoTime)schemaEntry.getInstanceElement()).getId()); 
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&&(attributeName.compareTo(Tag.att_ETI_gr_lineNum)==0))
			return new SimpleValue(((ElementoTime)schemaEntry.getInstanceElement()).getLineNumber()); 
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&&(attributeName.compareTo(Tag.att_ETI_gr_lineVisible)==0))
			return new SimpleValue(((ElementoTime)schemaEntry.getInstanceElement()).isLineVisible()); 
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&&(attributeName.compareTo(Tag.att_ETI_gr_maxY)==0))
			return new SimpleValue(((ElementoTime)schemaEntry.getInstanceElement()).getMaxY()); 
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&&(attributeName.compareTo(Tag.att_ETI_gr_minY)==0))
			return new SimpleValue(((ElementoTime)schemaEntry.getInstanceElement()).getMinY()); 
		
		if ((elementName.compareTo(Tag.elementoTime)==0)&&(attributeName.compareTo(Tag.att_ETI_gr_stringVisible)==0))
			return new SimpleValue(((ElementoTime)schemaEntry.getInstanceElement()).isStringIsVisible()); 
		
		if ((elementName.compareTo(Tag.elementoConstraint)==0)&&(attributeName.compareTo(Tag.att_ECO_id)==0))
			return new SimpleValue(Tag.att_ECO_pre_id+((ElementoConstraint)schemaEntry.getInstanceElement()).getId());
		
		if ((elementName.compareTo(Tag.elementoConstraint)==0)&&(attributeName.compareTo(Tag.att_ECO_tipo)==0))
			return new SimpleValue(((ElementoConstraint)schemaEntry.getInstanceElement()).getType()); 
		
		if ((elementName.compareTo(Tag.elementoSim)==0)&&(attributeName.compareTo(Tag.att_ESI_id)==0))
			return new SimpleValue(Tag.att_ESI_pre_id+((ElementoSim)schemaEntry.getInstanceElement()).getId());
		
		if ((elementName.compareTo(Tag.elementoParallel)==0)&&(attributeName.compareTo(Tag.att_EPA_id)==0))
			return new SimpleValue(Tag.att_EPA_pre_id+((ElementoParallelo)schemaEntry.getInstanceElement()).getId());
		
		if ((elementName.compareTo(Tag.elementoParallel)==0)&&(attributeName.compareTo(Tag.att_EPA_PntStart)==0))
			return new SimpleValue(((ElementoParallelo)schemaEntry.getInstanceElement()).getParallel_point_start_lin_med());
		
		
		if ((elementName.compareTo(Tag.elementoLoop)==0)&&(attributeName.compareTo(Tag.att_ELO_id)==0))
			return new SimpleValue(Tag.att_ELO_pre_id+((ElementoLoop)schemaEntry.getInstanceElement()).getId());
		
		
		
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
		
		if ((elementName.compareTo(Tag.elementoBox)==0) && (elementParentName.compareTo(Tag.listaClasse)==0)){			
			
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
		if ((elementName.compareTo(Tag.graficoLink)==0) && (elementParentName.compareTo(Tag.listaSeqLink)==0)){			
			
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
		
		if ((elementName.compareTo(Tag.listaSim)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaSim();
		
		if ((elementName.compareTo(Tag.listaParallel)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaParallel();
		
		if ((elementName.compareTo(Tag.listaLoop)==0)&& (elementParentName.compareTo(Tag.sequenceElement)==0))
			return ((SequenceElement)parentInstanceElement).getListaLoop();
		
		
		
		return null;
	}
	
	public Object getElement(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public void setPlugin(IMainTabPanel plugin) {
		this.plug = (SequenceWindow)plugin;
		this.plugData = (PlugData)plugin.getPlugData();
	}

	private SimpleValue getColoreValue(Color color,String attributeName){
		
		if (attributeName.compareTo(Tag.att_red)==0)
			return new SimpleValue(color.getRed());		
		if (attributeName.compareTo(Tag.att_green)==0)
			return new SimpleValue(color.getGreen());
		if (attributeName.compareTo(Tag.att_blue)==0)
			return new SimpleValue(color.getBlue());
		
		return null;
	}
	
	private SimpleValue getColorValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.font)==0){
				SchemaEntry schemaEntryParent = schemaEntry.getParent();
				String elementParentParentName = null;
				if (schemaEntryParent!=null)
					elementParentParentName = schemaEntryParent.getElementParentName();
				if (elementParentParentName!=null){
					
					if (elementParentParentName.compareTo(Tag.class_def)==0)
						return this.getColoreValue(GraficoTipoSequence.getDEFAULT_TEXT_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.link_def)==0)
						return this.getColoreValue(GraficoLink.getDEFAULT_TEXT_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.elementoBox)==0){					
						if(((ElementoClasse)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico() instanceof  ElementoBoxTesto){
							
							return this.getColoreValue(((ElementoBoxTesto)((ElementoClasse)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico()).getTextColor(),attributeName);						
						}
					}
					
					if (elementParentParentName.compareTo(Tag.graficoLink)==0)
						return this.getColoreValue(((ElementoSeqLink)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico().getTextColor(),attributeName);						
					
				}
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.line)==0){
				SchemaEntry schemaEntryParent = schemaEntry.getParent();
				String elementParentParentName = null;
				if (schemaEntryParent!=null)
					elementParentParentName = schemaEntryParent.getElementParentName();
				if (elementParentParentName!=null){
					
					if (elementParentParentName.compareTo(Tag.class_def)==0)
						return this.getColoreValue(GraficoTipoSequence.getDEFAULT_LINE_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.link_def)==0)
						return this.getColoreValue(GraficoLink.getDEFAULT_LINK_COLOR(),attributeName);
					
					
					if (elementParentParentName.compareTo(Tag.elementoBox)==0)
						return this.getColoreValue(((ElementoBoxTesto)((ElementoClasse)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico()).getLineColor(),attributeName);						
					
					if (elementParentParentName.compareTo(Tag.graficoLink)==0)
						return this.getColoreValue(((ElementoSeqLink)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico().getLineColor(),attributeName);						
					
				}
				
			}
		
		return null;
	}
	
	private SimpleValue getFontValue(SchemaEntry schemaEntry, String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.class_def)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(GraficoTipoSequence.getDEFAULT_TEXT_FONT());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(GraficoTipoSequence.getDEFAULT_FONT_SIZE());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(GraficoTipoSequence.getDEFAULT_FONT_STYLE());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.link_def)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(GraficoLink.getDEFAULT_TEXT_FONT());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(GraficoLink.getDEFAULT_FONT_SIZE());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(GraficoLink.getDEFAULT_FONT_STYLE());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				ElementoBoxTesto ebt = null;
				if(((ElementoClasse)schemaEntry.getParent().getInstanceElement()).getGrafico() instanceof  ElementoBoxTesto){
					
					ebt = (ElementoBoxTesto)((ElementoClasse)schemaEntry.getParent().getInstanceElement()).getGrafico();
				}
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(ebt.getTextFont());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(ebt.getFontSize());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(ebt.getFontStyle());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.graficoLink)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(((ElementoSeqLink)schemaEntry.getParent().getInstanceElement()).getGrafico().getTextFont());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(((ElementoSeqLink)schemaEntry.getParent().getInstanceElement()).getGrafico().getFontSize());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(((ElementoSeqLink)schemaEntry.getParent().getInstanceElement()).getGrafico().getFontStyle());
				
				
			}
		
		return null;
	}
	
	private SimpleValue getElementoBoxValue(SchemaEntry schemaEntry,String attributeName){
		
		if  (attributeName.compareTo(Tag.att_EBO_Height)==0)
			return new SimpleValue(((ElementoClasse)schemaEntry.getInstanceElement()).getGrafico().getHeight());
		
		if  (attributeName.compareTo(Tag.att_EBO_Width)==0)
			return new SimpleValue(((ElementoClasse)schemaEntry.getInstanceElement()).getGrafico().getWidth());
		
		if  (attributeName.compareTo(Tag.att_EBO_TopX)==0)
			return new SimpleValue(((ElementoClasse)schemaEntry.getInstanceElement()).getGrafico().getX());
		
		if  (attributeName.compareTo(Tag.att_EBO_TopY)==0)
			return new SimpleValue(((ElementoClasse)schemaEntry.getInstanceElement()).getGrafico().getY());
		
		if  (attributeName.compareTo(Tag.att_EBO_idref)==0)
			return new SimpleValue(Tag.att_ECL_pre_id+((ElementoClasse)schemaEntry.getInstanceElement()).getId());
		
		return null;
	}
	
	

	
	
	
	private SimpleValue getBkColorValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.class_def)==0)
				return this.getColoreValue(GraficoTipoSequence.getDEFAULT_BACKGROUND_COLOR(),attributeName);
		
		//if (elementParentName!=null)
		//if (elementParentName.compareTo(Tag.link_def)==0)
		//	return this.getColoreValue(GraficoLink.getDEFAULT_BACKGROUND_COLOR(),attributeName);
		
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				return this.getColoreValue(((ElementoClasse)schemaEntry.getParent().getInstanceElement()).getGrafico().getBackgroundColor(),attributeName);
			}
		
		
		return null;
	}
	
	private SimpleValue getLineValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.class_def)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(GraficoTipoSequence.getDEFAULT_LINE_WEIGHT());
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.link_def)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(GraficoLink.getDEFAULT_LINK_PATTERN());
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(GraficoLink.getDEFAULT_LINK_WEIGHT());
				
			} 
		
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(((ElementoClasse)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineWeight());
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.graficoLink)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(((ElementoSeqLink)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineTheme());
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(((ElementoSeqLink)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineWeight());
				
			}
		
		return null;
	}
	
	public void characters(char[] buf, int offset, int len) {
		// TODO Auto-generated method stub
		
	}
	
	public void endElement(String elementName) {
		/* DEFAULT ++++++++++++++++++++++++++++++++++++++++++++++++ */		
		if (elementName.equals(Tag.defaultConf)) {
			//controllo gli attributi
			bDefault = false;
			return;
		}
		if(elementName.equals(Tag.link_def)){
			inLink = false;
			GraficoLink.setDEFAULT_PROPERTIES(
					lineColor,
					lineWeight,
					lineTheme,
					fontNome,
					fontColor,
					fontSize,
					fontStyle);
			return;
		}
		if(elementName.equals(Tag.class_def)){
			GraficoTipoSequence.setDEFAULT_PROPERTIES(width,
					height,
					this.backGr,
					fontNome,
					fontColor,
					fontSize,
					fontStyle,
					this.lineWeight,
					this.lineColor);
			return;
		}
		if(elementName.equals(Tag.elementoBox)){
		}
		
		
		if(elementName.equals(Tag.graficoLink)){
		}
		
		if(elementName.equals(Tag.font)){
			inFont = false;
			return;
		}
		
		if(elementName.equals(Tag.line)){
			inLine = false;
			return;
		}
		if (elementName.equals(Tag.sequenceElement)) {
			
			bSequenceElement = false;
			
					
			//sequenceElement.getListaSeqLink().updateListaSeqLinkPosizione();
		/*	for(int j=0;j<sequenceElement.getListaClasse().size();j++){
				sequenceElement.getListaSeqLink().updateListaCanalePosizione(sequenceElement.getListaClasse().getElement(j));
				
				}*/
			
			
			sequenceElement.getListaSeqLink().updateListaSeqLinkPosizione();
			for(int j=0;j<sequenceElement.getListaClasse().size();j++){
				sequenceElement.getListaSeqLink().updateListaCanalePosizione(sequenceElement.getListaClasse().getElement(j));
				
				}

	
	
			/*sequenceElement.getListaTime().restoreFromFile();
			sequenceElement.getListaClasse().restoreFromFile();
			sequenceElement.getListaSeqLink().restoreFromFile();
			

				for(int j=0;j<sequenceElement.getListaClasse().size();j++){
					sequenceElement.getListaSeqLink().updateListaCanalePosizione(sequenceElement.getListaClasse().getElement(j));
					
					}
		sequenceElement.getListaSeqLink().updateListaSeqLinkPosizione();
		
			sequenceElement.getListaConstraint().restoreFromFile();
			sequenceElement.getListaSim().restoreFromFile();
			sequenceElement.getListaParallel().restoreFromFile();
			sequenceElement.getListaLoop().restoreFromFile();
			
			*/
			
			for(int j=0;j<sequenceElement.getListaClasse().size();j++){
				
				
				for (int i=0;i<sequenceElement.getListaConstraint().size();i++)
					sequenceElement.getListaConstraint().getElement(i).updateConstraintPosizione();
				
				for (int i=0;i<sequenceElement.getListaSim().size();i++)
					sequenceElement.getListaSim().getElement(i).updateSimPosizione();
				
				for (int i=0;i<sequenceElement.getListaParallel().size();i++)
					sequenceElement.getListaParallel().getElement(i).updateParallelPosizione();
				
				for (int i=0;i<sequenceElement.getListaLoop().size();i++)
					sequenceElement.getListaLoop().getElement(i).updateLoopPosizione();
				
			}	
			return;
		}
		
		/*
		 *inizio eleborazione listaDS 
		 */
		if (elementName.equals(Tag.listaDS)) {
			bListaDS = false;
			return;
		}
		
		
		if(elementName.equals(Tag.listaTime)){
		sequenceElement.getListaTime().restoreFromFile();
			bListaTime = false;
			return;
		}
		
		if(elementName.equals(Tag.listaConstraint)){
			
			sequenceElement.getListaConstraint().restoreFromFile();
			bListaConstraint = false;
			return;
		}
		
		if(elementName.equals(Tag.listaSim)){
			sequenceElement.getListaSim().restoreFromFile();
			bListaSim = false;
			return;
		}
		if(elementName.equals(Tag.listaParallel)){
			this.sequenceElement.getListaParallel().restoreFromFile();
			bListaParallel = false;
			return;
		}
		if(elementName.equals(Tag.listaLoop)){
			sequenceElement.getListaLoop().restoreFromFile();
			bListaLoop = false;
			return;
		}
		
		
		if(elementName.equals(Tag.listaClasse)){
			sequenceElement.getListaClasse().restoreFromFile();
			bListaClasse = false;
			return;
		}
		
		if(elementName.equals(Tag.listaSeqLink)){
			sequenceElement.getListaSeqLink().restoreFromFile();
			
			
			bListaSeqLink = false;
			return;
		}
		
		//crea ElementoConstraint grafico
		if(elementName.equals(Tag.elementoConstraint)){
			
			ElementoConstraint ec = this.sequenceElement.getListaConstraint().getElementById(id);
			ec.setType(type_con);
			ec.updateConstraintPosizione();
			bElementoConstraint = false;
			return;
		}
//		crea ElementoSim grafico
		if(elementName.equals(Tag.elementoSim)){
			
			ElementoSim sim = this.sequenceElement.getListaSim().getElementById(id);
			sim.updateSimPosizione();
////			/settiamo eventuali attributi grafici dell'elemento
			bElementoSim = false;
			return;
		}
		
//		crea ElementoParallel grafico
		if(elementName.equals(Tag.elementoParallel)){
			
			ElementoParallelo parallelo = this.sequenceElement.getListaParallel().getElementById(id);
		
////			/settiamo eventuali attributi grafici dell'elemento
			/*parallelo.setXYRet();
			parallelo.setPar_Height();
			parallelo.setParallel_point_width();
			parallelo.setParallel_point_start_lin_med();*/
			
			bElementoParallel = false;
			return;
		}
//		crea ElementoLoop grafico
		if(elementName.equals(Tag.elementoLoop)){
			
			ElementoLoop loop = this.sequenceElement.getListaLoop().getElementById(id);
			/////settiamo eventuali attributi grafici dell'elemento
			
			loop.updateLoopPosizione();
			bElementoLoop = false;
			return;
		}
		
		
		
	}
	
	public void startElement(String elementName, Attributes attributes) {
		if(bDefault){
			createDefault(elementName, attributes);
			return;
		}
		
		
		if (elementName.equals(Tag.defaultConf)) {
			//controllo gli attributi
			bDefault = true;
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
	
	
	
	/**
	 * setta i dati relativi all'elemento box
	 * @param box ElementoBox da settare
	 * @param eName Tag 
	 * @param attrs Attributi
	 */
	private void settaElementoBox(ElementoBox box,String eName, Attributes attrs ){
		if(inFont){
			if(eName.equals(Tag.color)){
				ElementoBoxTesto ebt =(ElementoBoxTesto) ebox;
				ebt.setTextColor(creaColore(attrs));
			}
			return;
		}
		
		if(eName.equals(Tag.font)){
			ElementoBoxTesto ebt =(ElementoBoxTesto) ebox;
			ebt.setTextFont(attrs.getValue(Tag.att_font_nome));
			ebt.setFontSize(Integer.parseInt(attrs.getValue(Tag.att_font_size)));
			ebt.setFontStyle(Integer.parseInt(attrs.getValue(Tag.att_font_style)));
			inFont = true;
			return;
		}
		
		if(inLine){
			if(eName.equals(Tag.color)){
				ebox.setLineColor(creaColore(attrs));
			}
			return;
		}
		
		
		if(eName.equals(Tag.line)){
			ebox.setLineWeight(Integer.parseInt(attrs.getValue(Tag.att_line_Weight)));
			inLine = true;
			return;
		}
		
		
		if(eName.equals(Tag.elementoBox)){
			ebox.setXY(
					Integer.parseInt(attrs.getValue(Tag.att_EBO_TopX)),
					Integer.parseInt(attrs.getValue(Tag.att_EBO_TopY))
			);
			
			
			ebox.setHeight(
					Integer.parseInt(attrs.getValue(Tag.att_EBO_Height)));
			
			ebox.setWidth(
					Integer.parseInt(attrs.getValue(Tag.att_EBO_Width)));
		}
		
		
		//colore di background
		if(eName.equals(Tag.eBoxBkColor)){
			ebox.setBackgroundColor(creaColore(attrs));
			
		}
		
	}
	
	/**
	 * ritorna un colore dagli attributi passati come valore
	 * @param attrs
	 * @return
	 */
	private Color creaColore(Attributes attrs) {
		return 		new Color(Integer.parseInt(attrs.getValue(Tag.att_red)),
				Integer.parseInt(attrs.getValue(Tag.att_green)),
				Integer.parseInt(attrs.getValue(Tag.att_blue)));
	}
	
	/**
	 * setta i dati relativi al grafico link passato in argomento
	 * @param gl GraficoLink da settare
	 * @param eName Tag 
	 * @param attrs Attributi
	 */
	private void settaGraficoLink(GraficoLink gl,String eName, Attributes attrs ){
		if(inFont){
			if(eName.equals(Tag.color)){
				gl.updateTestoProprieta(creaColore(attrs), fontNome,
						fontStyle,fontSize );
			}
			return;
		}
		if(eName.equals(Tag.font)){
			fontNome = attrs.getValue(Tag.att_font_nome);
			fontSize = Integer.parseInt(attrs.getValue(Tag.att_font_size));
			fontStyle = Integer.parseInt(attrs.getValue(Tag.att_font_style));
			inFont = true;
			return;
		}
		
		if(inLine){
			if(eName.equals(Tag.color)){
				gl.updateLineaProprieta(creaColore(attrs),width,lineTheme );
			}
			return;
		}
		
		
		if(eName.equals(Tag.line)){
			width = Integer.parseInt(attrs.getValue(Tag.att_line_Weight));
			lineTheme = Integer.parseInt(attrs.getValue(Tag.att_line_Theme));
			inLine=true;
			return;
		}
		
	}
	
	
	
	/* DEFAULT ***************************************** */
	
	/**
	 * aggiorna i valori di default
	 * @param eName  tag da controllare
	 * @param attrs Attributi
	 * 
	 */
	private void createDefault(String eName, Attributes attrs){
		
		if(eName.equals(Tag.processo_def)){
			this.width = Integer.parseInt(attrs.getValue(Tag.att_DVA_Width));
			this.height = Integer.parseInt(attrs.getValue(Tag.att_DVA_Height));
			return;
		}
		
		if(eName.equals(Tag.store_def)){
			this.width = Integer.parseInt(attrs.getValue(Tag.att_DVA_Width));
			this.height = Integer.parseInt(attrs.getValue(Tag.att_DVA_Height));
			return;
		}
		if(eName.equals(Tag.start_def)){
			this.width = Integer.parseInt(attrs.getValue(Tag.att_DVA_Width));
			this.height = Integer.parseInt(attrs.getValue(Tag.att_DVA_Height));
			return;
		}
		if(eName.equals(Tag.build_def)){
			this.width = Integer.parseInt(attrs.getValue(Tag.att_DVA_Width));
			this.height = Integer.parseInt(attrs.getValue(Tag.att_DVA_Height));
			return;
		}
		
		if(eName.equals(Tag.class_def)){
			this.width = Integer.parseInt(attrs.getValue(Tag.att_DVA_Width));
			this.height = Integer.parseInt(attrs.getValue(Tag.att_DVA_Height));
			return;
		}
		
		if(eName.equals(Tag.link_def)){
			inLink = true;
			return;
		}
		
		if(eName.equals(Tag.line)){
			lineWeight = Integer.parseInt(attrs.getValue(Tag.att_line_Weight));
			if(inLink){
				lineTheme = Integer.parseInt(attrs.getValue(Tag.att_line_Theme));
			}
			else{
				lineTheme = 0;	
			}
			inLine = true;
			return;
		}
		
		if(eName.equals(Tag.font)){
			fontNome = attrs.getValue(Tag.att_font_nome);
			fontSize = Integer.parseInt(attrs.getValue(Tag.att_font_size));
			fontStyle = Integer.parseInt(attrs.getValue(Tag.att_font_style));
			inFont = true;
			return;
		}
		
		
		//Colore background
		if(eName.equals(Tag.eBoxBkColor)){
			this.backGr = this.creaColore(attrs);
		}
		
		
		//colore interno 
		if(eName.equals(Tag.color)){
			if(inLine){
				this.lineColor = this.creaColore(attrs);
				return;
			}
			if(inFont){
				this.fontColor = this.creaColore(attrs);
				return;
			}
		}
	}
	
	
	
	/**
	 * Crea SequenceElement
	 * @param name
	 * @param attrs
	 */
	private void createSequenceElement(String name, Attributes attrs) {
		if(bSequenceElement){
			/*
			 * crea i dati della lista time
			 */
			if(bListaTime){
				createListaTime(name, attrs);
				return;
			}
			
			/*
			 * crea i dati della listaConstraint --- PSC
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
			if(plugData!=null){
				sequenceElement = plugData.getListaDS().getElementById(
						util.strToId(attrs.getValue(Tag.att_SEE_id))
						
				);
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
			ElementoTime et = sequenceElement.getListaTime().getElementoById(
					util.strToId(attrs.getValue(Tag.att_ETI_id)));
			
			et.setLineNumber(Integer.parseInt(attrs.getValue(Tag.att_ETI_gr_lineNum)));
			et.setLineVisible(util.strToBool(attrs.getValue(Tag.att_ETI_gr_lineVisible)));
			et.setStringVisible(util.strToBool(attrs.getValue(Tag.att_ETI_gr_stringVisible)));
			et.setMaxY(Integer.parseInt(attrs.getValue(Tag.att_ETI_gr_maxY)));
			et.setMinY(Integer.parseInt(attrs.getValue(Tag.att_ETI_gr_minY)));
		}
	}
	
	/**
	 * carica la listaClasse
	 * @param name
	 * @param attrs
	 */
	private void createListaClasse(String eName, Attributes attrs) {
		
		if(eName.equals(Tag.elementoBox)){
			ebox = sequenceElement.getListaClasse().getElementoById(
					util.strToId(attrs.getValue(Tag.att_EBO_idref))).getGrafico();
		}
		settaElementoBox(ebox,eName,attrs );
		return;
	}
	
	/**
	 * ElementoSeqLink
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void createElementoSeqLink(String eName, Attributes attrs){
		if(eName.equals(Tag.graficoLink)){
			 
			gLink = sequenceElement.getListaSeqLink().getElementById(
					util.strToId(attrs.getValue(Tag.att_graficoLink_idref))).getGrafico();
		}
		settaGraficoLink(gLink,eName,attrs );
	}
	
	/**
	 * ElementoConstraint
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void createListaConstraint(String eName, Attributes attrs){
		if(!bElementoConstraint){
			
			//attributi
			id	= util.strToId(attrs.getValue(Tag.att_ECO_id));
			
			///controllo per compatibilità vecchi file, manca l'attributo tipo.
			if (attrs.getValue(Tag.att_ECO_tipo)==null)
				type_con = 0;
			else
				type_con = (new Integer(attrs.getValue(Tag.att_ECO_tipo))).intValue();
			
			bElementoConstraint = true;
			return;
		}
		
	}
	
	private void createListaSim(String eName, Attributes attrs){
		if(!bElementoSim){
			
			//attributi
			id	= util.strToId(attrs.getValue(Tag.att_ESI_id));
			
			///controllo per compatibilità vecchi file, manca l'attributo tipo.
			
			bElementoSim = true;
			return;
		}
		
	}
	
	private void createListaParallelo(String eName, Attributes attrs){
		if(!bElementoParallel){
			
			//if (eName.compareTo(Tag.e)==0)
			
			//attributi
			id	= util.strToId(attrs.getValue(Tag.att_EPA_id));
			if (attrs.getValue(Tag.att_EPA_PntStart)!=null)
			point_start_mid=(new Integer(attrs.getValue(Tag.att_EPA_PntStart))).intValue();			
			bElementoParallel = true;
			
			return;
		}
		
	
		
	}
	
	private void createListaLoop(String eName, Attributes attrs){
		if(!bElementoLoop){
			
			//attributi
			id	= util.strToId(attrs.getValue(Tag.att_ELO_id));
			
			///controllo per compatibilità vecchi file, manca l'attributo tipo.
			
			bElementoLoop = true;
			return;
		}
		
	}
	
	public void resetForNewFile() {
		// TODO Auto-generated method stub
		
	}
	
}
