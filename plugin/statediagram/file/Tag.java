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
    

package plugin.statediagram.file;

/**
 * @author michele
 * dati da utilizzare per la costruzione del file di salvataggio
 *
 */
public interface Tag {

		
	
	

	public static String idProcessoStato = "processoStato";

		
/* listaDP +++++++++++++++++++++++++++++++++++++++++ */	
	//ListaDP
	public static String listaDP ="ListaDP";
	
	public static String listaThread ="ListaThread";
	public static String att_LT_processo ="ElementoProcesso";
	
	
	public static String threadElement ="ThreadElement";
	public static String att_TE_nome ="nome";
	public static String att_TE_numStato ="numStato";
	
	public static String att_EPR_pre_id = "EPR_";
	
	//ListaStato
	public static String listaStato ="ListaStato";
	//ElementoStato
	public static String elementoStato ="ElementoStato";
	public static String onEntryCode ="OnEntryCode";
	public static String onExitCode ="OnExitCode";
	public static String att_EST_id ="id";	
	public static String att_EST_pre_id = "EST_";
	
	public static String att_EST_tipo ="tipo";
	public static String att_EST_nome ="nome";
	
	//ListaMessaggio
	public static String listaMessaggio ="Listamessaggio";
	//ElementoMessaggio
	public static String elementoMessaggio ="ElementoMessaggio";
	public static String elementoStatoFrom ="ElementoStatoFrom";
	public static String att_ESFrom_idref ="idref";	
	
	
	public static String elementoStatoTo ="ElementoStatoTo";
	public static String att_ESTo_idref ="idref";	
	
	public static String guard ="Guard";
	public static String operations ="Operations";
	public static String parametri ="Parametri";
	public static String elementoParam ="Parametro";
	
	
	
	public static String att_EME_id ="id";	
	public static String att_EME_pre_id = "EME_";
	
	public static String att_EME_tipo ="tipo";
	public static String att_EME_nome ="nome";
	public static String att_EME_rff ="rff";
	public static String att_EME_sendReceive ="sendReceive";
	public static String att_EME_flussodiretto ="flussodiretto";
	

	
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
