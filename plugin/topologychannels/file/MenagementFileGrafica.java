package plugin.topologychannels.file;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.xml.sax.Attributes;

import plugin.topologychannels.TopologyWindow;
import plugin.topologychannels.data.ElementoCanale;
import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.data.ListaCanale;
import plugin.topologychannels.data.ListaProcesso;
import plugin.topologychannels.data.PlugData;
import plugin.topologychannels.graph.GraficoTipoProcesso;
import plugin.topologychannels.graph.GraficoTipoStore;
import plugin.topologychannels.resource.graph.ElementoBox;
import plugin.topologychannels.resource.graph.ElementoBoxTesto;
import plugin.topologychannels.resource.graph.GraficoLink;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.Utility;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;


public class MenagementFileGrafica implements SerializableCharmyFile {
	
	
	private PlugData plugData;
	private TopologyWindow plug;
	

	private Utility util = new Utility();
	private boolean bTopology = false;
	private boolean bListaProcesso = false;
	private boolean bListaCanale = false;
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
	
	public MenagementFileGrafica() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SimpleValue getAttributeValue(SchemaEntry schemaEntry,String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		
		if ((elementName.compareTo(Tag.processo_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Height)==0))
			return new SimpleValue(GraficoTipoProcesso.getDEFAULT_HEIGHT()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.processo_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Width)==0))
			return new SimpleValue(GraficoTipoProcesso.getDEFAULT_WIDTH()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.store_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Height)==0))
			return new SimpleValue(GraficoTipoStore.getDEFAULT_HEIGHT()); ////PROBLEMA
		
		if ((elementName.compareTo(Tag.store_def)==0)&&(attributeName.compareTo(Tag.att_DVA_Width)==0))
			return new SimpleValue(GraficoTipoStore.getDEFAULT_WIDTH()); ////PROBLEMA
						
		
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
			return new SimpleValue(Tag.att_ECA_pre_id+ ((ElementoCanale)schemaEntry.getInstanceElement()).getId()); ////PROBLEMA
		
	
		
		return null;
	}
	
	public Object[] getList(SchemaEntry schemaEntry) {
		
		String elementName=schemaEntry.getElementName();
		Object parentInstance = schemaEntry.getParent().getInstanceElement();
		
		if (elementName.compareTo(Tag.elementoBox)==0){
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
		
		if (elementName.compareTo(Tag.graficoLink)==0){
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
					
					if (elementParentParentName.compareTo(Tag.processo_def)==0)
						return this.getColoreValue(GraficoTipoProcesso.getDEFAULT_TEXT_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.store_def)==0)
						return this.getColoreValue(GraficoTipoStore.getDEFAULT_TEXT_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.elementoBox)==0){					
						if(((ElementoProcesso)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico() instanceof  ElementoBoxTesto){
							
							return this.getColoreValue(((ElementoBoxTesto)((ElementoProcesso)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico()).getTextColor(),attributeName);						
						}
					}
					
					if (elementParentParentName.compareTo(Tag.graficoLink)==0)
						return this.getColoreValue(((ElementoCanale)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico().getTextColor(),attributeName);						
					
				}
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.line)==0){
				SchemaEntry schemaEntryParent = schemaEntry.getParent();
				String elementParentParentName = null;
				if (schemaEntryParent!=null)
					elementParentParentName = schemaEntryParent.getElementParentName();
				if (elementParentParentName!=null){ 
					
					if (elementParentParentName.compareTo(Tag.processo_def)==0)
						return this.getColoreValue(GraficoTipoProcesso.getDEFAULT_LINE_COLOR(),attributeName);
					
					if (elementParentParentName.compareTo(Tag.store_def)==0)
						return this.getColoreValue(GraficoTipoStore.getDEFAULT_LINE_COLOR(),attributeName);
					
					
					if (elementParentParentName.compareTo(Tag.elementoBox)==0)
						return this.getColoreValue(((ElementoBox)((ElementoProcesso)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico()).getLineColor(),attributeName);						
					
					if (elementParentParentName.compareTo(Tag.graficoLink)==0)
						return this.getColoreValue(((ElementoCanale)schemaEntry.getParent().getParent().getInstanceElement()).getGrafico().getLineColor(),attributeName);						
					
				}
				
			}
		
		return null;
	}
	
	private SimpleValue getFontValue(SchemaEntry schemaEntry, String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.processo_def)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(GraficoTipoProcesso.getDEFAULT_TEXT_FONT());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(GraficoTipoProcesso.getDEFAULT_FONT_SIZE());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(GraficoTipoProcesso.getDEFAULT_FONT_STYLE());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.store_def)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(GraficoTipoStore.getDEFAULT_TEXT_FONT());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(GraficoTipoStore.getDEFAULT_FONT_SIZE());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(GraficoTipoStore.getDEFAULT_FONT_STYLE());
				
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				ElementoBoxTesto ebt = null;
				if(((ElementoProcesso)schemaEntry.getParent().getInstanceElement()).getGrafico() instanceof  ElementoBoxTesto){
					
					ebt = (ElementoBoxTesto)((ElementoProcesso)schemaEntry.getParent().getInstanceElement()).getGrafico();
								
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(ebt.getTextFont());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(ebt.getFontSize());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(ebt.getFontStyle());
				
				}
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.graficoLink)==0){
				
				if (attributeName.compareTo(Tag.att_font_nome)==0)
					return new SimpleValue(((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getGrafico().getTextFont());
				
				if (attributeName.compareTo(Tag.att_font_size)==0)
					return new SimpleValue(((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getGrafico().getFontSize());
				
				if (attributeName.compareTo(Tag.att_font_style)==0)
					return new SimpleValue(((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getGrafico().getFontStyle());
				
				
			}
		
		return null;
	}
	
	
	private SimpleValue getElementoBoxValue(SchemaEntry schemaEntry,String attributeName){
		
		if  (attributeName.compareTo(Tag.att_EBO_Height)==0)
			return new SimpleValue(((ElementoProcesso)schemaEntry.getInstanceElement()).getGrafico().getHeight());
		
		if  (attributeName.compareTo(Tag.att_EBO_Width)==0)
			return new SimpleValue(((ElementoProcesso)schemaEntry.getInstanceElement()).getGrafico().getWidth());
		
		if  (attributeName.compareTo(Tag.att_EBO_TopX)==0)
			return new SimpleValue(((ElementoProcesso)schemaEntry.getInstanceElement()).getGrafico().getX());
		
		if  (attributeName.compareTo(Tag.att_EBO_TopY)==0)
			return new SimpleValue(((ElementoProcesso)schemaEntry.getInstanceElement()).getGrafico().getY());
		
			if  (attributeName.compareTo(Tag.att_EBO_idref)==0)
		      return new SimpleValue(Tag.att_EPR_pre_id+((ElementoProcesso)schemaEntry.getInstanceElement()).getId());
		
		return null;
	}
	
	private SimpleValue getBkColorValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.processo_def)==0)
				return this.getColoreValue(GraficoTipoProcesso.getDEFAULT_BACKGROUND_COLOR(),attributeName);
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.store_def)==0)
				return this.getColoreValue(GraficoTipoStore.getDEFAULT_BACKGROUND_COLOR(),attributeName);
		
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				return this.getColoreValue(((ElementoProcesso)schemaEntry.getParent().getInstanceElement()).getGrafico().getBackgroundColor(),attributeName);
			}
		
		
		return null;
	}
	
	private SimpleValue getLineValue(SchemaEntry schemaEntry,String attributeName){
		
		String elementParentName=schemaEntry.getElementParentName();
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.processo_def)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(GraficoTipoProcesso.getDEFAULT_LINE_WEIGHT());
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.store_def)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(GraficoTipoStore.getDEFAULT_LINE_WEIGHT());
				
			}
		
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.elementoBox)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(0);
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(((ElementoProcesso)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineWeight());
				
			}
		
		if (elementParentName!=null)
			if (elementParentName.compareTo(Tag.graficoLink)==0){
				
				if  (attributeName.compareTo(Tag.att_line_Theme)==0)
					return new SimpleValue(((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineTheme());
				
				if  (attributeName.compareTo(Tag.att_line_Weight)==0)
					return new SimpleValue(((ElementoCanale)schemaEntry.getParent().getInstanceElement()).getGrafico().getLineWeight());
				
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

		
		if(elementName.equals(Tag.processo_def)){
			 GraficoTipoProcesso.setDEFAULT_PROPERTIES(width,
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
		
		if(elementName.equals(Tag.store_def)){
			 GraficoTipoStore.setDEFAULT_PROPERTIES(width,
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
		
		if (elementName.equals(Tag.topology)) {
			bTopology = false;	
			if(plugData!=null){			
				plugData.getListaProcesso().restoreFromFile();
				plugData.getListaCanale().restoreFromFile();
				for(int i=0;i<plugData.getListaProcesso().size();i++){
					plugData.getListaCanale().updateListaCanalePosizione(plugData.getListaProcesso().getElement(i));
				}
			}
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
		
		
		
	}

	public void startElement(String elementName, Attributes attributes) {
//		sto nella topologia
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
		 * topology
		 */
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
		//	NULLLLLLL
			this.width = Integer.parseInt(attrs.getValue(Tag.att_DVA_Width));
			this.height = Integer.parseInt(attrs.getValue(Tag.att_DVA_Height));
			return;
		}
		
		if(eName.equals(Tag.store_def)){
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

	/* FINE DEFAULT ***************************************** */
		
		
	/* TOPOLOGIA *************************************** */	
	
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
			
			if(bListaProcesso){
				creaElementoProcesso(eName,attrs);
			}
			
			/* listaCanale */
			if(!bListaCanale && eName.equals(Tag.listaCanale)){
				bListaCanale = true;
				return;
			}
			
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
		if(eName.equals(Tag.elementoBox)){
	
			if(plugData!=null){
				ebox = plugData.getListaProcesso().getElementoById(
						util.strToId(attrs.getValue(Tag.att_EBO_idref))).getGrafico();
			}
		}
		settaElementoBox(ebox,eName,attrs );
	}

	/**
	 * ElementoCanale
	 * @param eName String Tag
	 * @param attrs Attributi
	 */
	private void creaElementoCanale(String eName, Attributes attrs){
	
		if(eName.equals(Tag.graficoLink)){
			if(plugData!=null){
				gLink = plugData.getListaCanale().getCanaleById(
						util.strToId(attrs.getValue(Tag.att_graficoLink_idref))).getGrafico();
			}
		}
		settaGraficoLink(gLink,eName,attrs );
	
	}

	public void resetForNewFile() {
		// TODO Auto-generated method stub
		
	}

}
