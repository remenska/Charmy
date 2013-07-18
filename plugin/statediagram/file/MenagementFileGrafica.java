package plugin.statediagram.file;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.Attributes;

import plugin.sequencediagram.SequenceWindow;
import plugin.statediagram.StateWindow;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.PlugData;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.graph.GraficoTipoBuild;
import plugin.statediagram.graph.GraficoTipoStart;
import plugin.topologydiagram.resource.graph.ElementoBox;
import plugin.topologydiagram.resource.graph.ElementoBoxTesto;
import plugin.topologydiagram.resource.graph.GraficoLink;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.Utility;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;


public class MenagementFileGrafica implements SerializableCharmyFile {

	private PlugData plugData;
	private StateWindow plug;
	
	private Utility util = new Utility();
	private boolean bListaDP = false;
	private boolean bThreadElement = false;
	private boolean bListaStato = false;
	private boolean bListaMessaggio = false;
	private ThreadElement threadElement;
	private ListaThread listaThread;
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

	private long idProcessoDP;

	public MenagementFileGrafica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SimpleValue getAttributeValue(SchemaEntry schemaEntry, String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		
		if ((elementName.compareTo(Tag.start_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Height)==0))
			return new SimpleValue(GraficoTipoStart.getDEFAULT_HEIGHT()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.start_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Width)==0))
			return new SimpleValue(GraficoTipoStart.getDEFAULT_WIDTH()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.build_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Height)==0))
			return new SimpleValue(GraficoTipoBuild.getDEFAULT_HEIGHT()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.build_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Width)==0))
			return new SimpleValue(GraficoTipoBuild.getDEFAULT_WIDTH()); ////PROBLEMA
		
		
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
		
		if ((elementName.compareTo(Tag.graficoLink)==0)&&(attributeName.compareTo(Tag.att_graficoLink_idref)==0))
			return new SimpleValue(Tag.att_EME_pre_id+ ((ElementoMessaggio)schemaEntry.getInstanceElement()).getId()); 
		
		if ((elementName.compareTo(Tag.listaThread)==0)&&(attributeName.compareTo(Tag.att_LT_processo)==0))
			return new SimpleValue(Tag.att_EPR_pre_id+((ListaThread)schemaEntry.getInstanceElement()).getIdProcesso()); 
		
		if ((elementName.compareTo(Tag.threadElement)==0)&&(attributeName.compareTo(Tag.att_TE_nome)==0))
			return new SimpleValue(((ThreadElement)schemaEntry.getInstanceElement()).getNomeThread()); 
		
		
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
		
		 
		if ((elementName.compareTo(Tag.elementoBox)==0) && (elementParentName.compareTo(Tag.listaStato)==0)){			
			
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
		
		if ((elementName.compareTo(Tag.graficoLink)==0) && (elementParentName.compareTo(Tag.listaMessaggio)==0)){			
			
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
		
		if ((elementName.compareTo(Tag.listaStato)==0)&& (elementParentName.compareTo(Tag.threadElement)==0))
			 return ((ThreadElement)parentInstanceElement).getListaStato();
		
		if ((elementName.compareTo(Tag.listaMessaggio)==0)&& (elementParentName.compareTo(Tag.threadElement)==0))
			 return ((ThreadElement)parentInstanceElement).getListaMessaggio();
		
	
		return null;
	}

	public Object getElement(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPlugin(IMainTabPanel plugin) {
		this.plug = (StateWindow)plugin;
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
					
					if (elementParentParentName.compareTo(Tag.start_def)==0)
						return this.getColoreValue(GraficoTipoStart.getDEFAULT_TEXT_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.build_def)==0)
						return this.getColoreValue(GraficoTipoBuild.getDEFAULT_TEXT_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.elementoBox)==0){					
						if(((ElementoStato)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico() instanceof  ElementoBoxTesto){
							
							return this.getColoreValue(((ElementoBoxTesto)((ElementoStato)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico()).getTextColor(),attributeName);						
						}
					}
					
					if (elementParentParentName.compareTo(Tag.graficoLink)==0)
						return this.getColoreValue(((ElementoMessaggio)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico().getTextColor(),attributeName);						
					
				}
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.line)==0){
				SchemaEntry schemaEntryParent = schemaEntry.getParent();
				String elementParentParentName = null;
				if (schemaEntryParent!=null)
					elementParentParentName = schemaEntryParent.getElementParentName();
				if (elementParentParentName!=null){
					
					if (elementParentParentName.compareTo(Tag.start_def)==0)
						return this.getColoreValue(GraficoTipoStart.getDEFAULT_LINE_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.build_def)==0)
						return this.getColoreValue(GraficoTipoBuild.getDEFAULT_LINE_COLOR(),attributeName);
					
					
					if (elementParentParentName.compareTo(Tag.elementoBox)==0)
						return this.getColoreValue(((ElementoBoxTesto)((ElementoStato)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico()).getLineColor(),attributeName);						
					
					if (elementParentParentName.compareTo(Tag.graficoLink)==0)
						return this.getColoreValue(((ElementoMessaggio)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico().getLineColor(),attributeName);						
					
				}
				
			}
		
		return null;
	}

	private SimpleValue getFontValue(SchemaEntry schemaEntry, String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.start_def)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(GraficoTipoStart.getDEFAULT_TEXT_FONT());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(GraficoTipoStart.getDEFAULT_FONT_SIZE());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(GraficoTipoStart.getDEFAULT_FONT_STYLE());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.build_def)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(GraficoTipoBuild.getDEFAULT_TEXT_FONT());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(GraficoTipoBuild.getDEFAULT_FONT_SIZE());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(GraficoTipoBuild.getDEFAULT_FONT_STYLE());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				ElementoBoxTesto ebt = null;
				if(((ElementoStato)schemaEntry.getParent().getInstanceElement()).getGrafico() instanceof  ElementoBoxTesto){
					
					ebt = (ElementoBoxTesto)((ElementoStato)schemaEntry.getParent().getInstanceElement()).getGrafico();
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
					return new SimpleValue(((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getGrafico().getTextFont());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getGrafico().getFontSize());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getGrafico().getFontStyle());
				
				
			}
		
		return null;
	}

	private SimpleValue getElementoBoxValue(SchemaEntry schemaEntry,String attributeName){
		
		if  (attributeName.compareTo(Tag.att_EBO_Height)==0)
			return new SimpleValue(((ElementoStato)schemaEntry.getInstanceElement()).getGrafico().getHeight());
		
		if  (attributeName.compareTo(Tag.att_EBO_Width)==0)
			return new SimpleValue(((ElementoStato)schemaEntry.getInstanceElement()).getGrafico().getWidth());
		
		if  (attributeName.compareTo(Tag.att_EBO_TopX)==0)
			return new SimpleValue(((ElementoStato)schemaEntry.getInstanceElement()).getGrafico().getX());
		
		if  (attributeName.compareTo(Tag.att_EBO_TopY)==0)
			return new SimpleValue(((ElementoStato)schemaEntry.getInstanceElement()).getGrafico().getY());
		
			if  (attributeName.compareTo(Tag.att_EBO_idref)==0)
		      return new SimpleValue(Tag.att_EPR_pre_id+((ElementoStato)schemaEntry.getInstanceElement()).getId());
		
		return null;
	}

	private SimpleValue getBkColorValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.start_def)==0)
				return this.getColoreValue(GraficoTipoStart.getDEFAULT_BACKGROUND_COLOR(),attributeName);
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.build_def)==0)
				return this.getColoreValue(GraficoTipoBuild.getDEFAULT_BACKGROUND_COLOR(),attributeName);
		
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				return this.getColoreValue(((ElementoStato)schemaEntry.getParent().getInstanceElement()).getGrafico().getBackgroundColor(),attributeName);
			}
		
		
		return null;
	}

	private SimpleValue getLineValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.start_def)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(GraficoTipoStart.getDEFAULT_LINE_WEIGHT());
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.build_def)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(GraficoTipoBuild.getDEFAULT_LINE_WEIGHT());
				
			}
		
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(((ElementoStato)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineWeight());
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.graficoLink)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineTheme());
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(((ElementoMessaggio)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineWeight());
				
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
		
		if(elementName.equals(Tag.start_def)){
			 GraficoTipoStart.setDEFAULT_PROPERTIES(width,
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
		
		if(elementName.equals(Tag.build_def)){
			 GraficoTipoBuild.setDEFAULT_PROPERTIES(width,
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
		
		
		if (elementName.equals(Tag.listaDP)) {
			bListaDP = false;
			return;
		}		
		
		if (elementName.equals(Tag.threadElement)) {
			bThreadElement = false;
			threadElement.getListaStato().restoreFromFile();
			threadElement.getListaMessaggio().restoreFromFile();
			return;
		}		
				
		if (elementName.equals(Tag.listaStato)) {
			bListaStato = false;
			return;
		}			
		
		//lista Messaggio
		if (elementName.equals(Tag.listaMessaggio)) {
			bListaMessaggio = false;
			return;
		}	
		
		if (elementName.equals(Tag.elementoMessaggio)) {
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

	/* COMUNI ********************************************/
	
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

	/* FINE TOPOLOGIA *************************************** */	
	
			
	/* INIZIO LISTADP   ********************************** */		
	
		/**
		 * controlla ThreadElement 
		 * @param eName  tag da controllare
		 * @param attrs Attributi
		 */
		private void createThreadElement(String eName, Attributes attrs){
	
			if(bThreadElement){
				
				if(bListaStato){
					if(eName.equals(Tag.elementoBox)){
						ebox = threadElement.getListaStato().getElementoById(
								util.strToId(attrs.getValue(Tag.att_EBO_idref))).getGrafico();
					}
					settaElementoBox(ebox,eName,attrs );
					return;
				}
				
				
				//sto nei messaggi
				if(bListaMessaggio){
					if(eName.equals(Tag.graficoLink)){
						gLink = threadElement.getListaMessaggio().getElementById(
								util.strToId(attrs.getValue(Tag.att_graficoLink_idref))).getGrafico();
					}
					settaGraficoLink(gLink,eName,attrs );
				    return;
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
				threadElement = listaThread.getThreadElement(attrs.getValue(Tag.att_TE_nome));
				bThreadElement = true;
				return;
			}
			
			//preleva il riferimento al processo
			if(eName.equals(Tag.listaThread)){
				idProcessoDP = util.strToId(attrs.getValue(Tag.att_LT_processo));
				if(plugData!=null){
					listaThread = plugData.getListaDP().getListaThread(idProcessoDP);
				}
				return;
			}
			
		}

		public void resetForNewFile() {
			// TODO Auto-generated method stub
			
		}

}
