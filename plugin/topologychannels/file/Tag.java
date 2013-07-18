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
    

package plugin.topologychannels.file;

/**
 * @author michele
 * dati da utilizzare per la costruzione del file di salvataggio
 *
 */
public interface Tag {

	public static String identificatori = "Identificatori";
	
	public static String idCanaleMessaggio = "canaleMessaggio";
	public static String idProcessoStato = "processoStato";
	
	//albero relativo alla topologia
	public static String topology = "Topology";

	//lista processi
	public static String listaProcesso ="ListaProcesso";
	
	//Elemento Processo
	public static String elementoProcesso = "ElementoProcesso";
	public static String att_EPR_modality = "modality";
	public static String att_EPR_dummy = "dummy";
	public static String att_EPR_appartenenza = "appartenenza";
	public static String att_EPR_tipo = "tipo";
	public static String att_EPR_id = "id";
		
	public static String att_EPR_pre_id = "EPR_";
	
	public static String att_EPR_nome = "nome";
		
	//listaCanale
	public static String listaCanale ="ListaCanale";
	
	//ElementoCanale
	public static String elementoCanale ="ElementoCanale";
	public static String att_ECA_modality = "modality";
	public static String att_ECA_tipo ="tipo";
	public static String att_ECA_nome ="nome";
	public static String att_ECA_flussodiretto ="flussodiretto";
	public static String att_ECA_id ="id";
	public static String att_ECA_pre_id = "ECA_";
	
	public static String elementoProcessoFrom ="ElementoProcessoFrom";
	public static String att_EPFrom_idref ="idref";

	public static String elementoProcessoTo ="ElementoProcessoTo";
	public static String att_EPTo_idref ="idref";	
	
	

/* INIZIO GRAFICA ++++++++++++++++++++++++++++++++++++ */	

	
	public static String elementoBox ="ElementoBox";
	public static String att_EBO_idref= "idref";
	
	public static String att_EBO_TopX ="TopX";
	public static String att_EBO_TopY ="TopY";
	
	public static String att_EBO_Height ="Height";
	public static String att_EBO_Width  ="Width";
	
	public static String eBoxBkColor  ="BkColor";
	public static String att_eBoxBkColor_red  ="red";
	public static String att_eBoxBkColor_green  ="green";
	public static String att_eBoxBkColor_blue  ="blue";
	
	public static String font  ="Font";
	public static String att_font_nome  ="nome";
	public static String att_font_size  ="size";
	public static String att_font_style  ="style";
	
	public static String line  ="Line";
	public static String att_line_Theme  ="theme";
	public static String att_line_Weight  ="weight";
	
	//comune colore
	public static String color  ="Colore";
	public static String att_red  ="red";
	public static String att_green  ="green";
	public static String att_blue  ="blue";
	public static String value ="value";
	
	public static String graficoLink ="GraficoLink";
	public static String att_graficoLink_idref ="idref";
	
	//ElementoTime
//<ElementoTime lineNum="numero" stringVisible="true/false" maxY="numero" minY="numero" lineVisible="true/false" id="ETI_numero" />
	
	public static String att_ETI_gr_minY ="minY";
	public static String att_ETI_gr_maxY ="maxY";
	public static String att_ETI_gr_lineNum ="lineNum";
	public static String att_ETI_gr_stringVisible ="stringVisible";
	public static String att_ETI_gr_lineVisible ="lineVisible";
	
//default	
	public static String defaultConf ="Default";
	
	public static String processo_def ="Processo";
	public static String store_def ="Store";
	public static String start_def ="Start";
	public static String build_def ="Build";
	public static String class_def ="Class";

	public static String defValue ="DefBox";
	public static String att_DVA_Height ="Height";
	public static String att_DVA_Width  ="Width";
	
	public static String link_def ="Link";

/* FINE GRAFICA +++++++++++++++++++++++++++++++++++++ */	

}
