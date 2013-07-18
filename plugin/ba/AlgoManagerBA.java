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
    


package plugin.ba;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

import plugin.sequencediagram.SequenceWindow;
import plugin.statediagram.StateWindow;
import plugin.topologydiagram.TopologyWindow;
import core.internal.runtime.data.IPlugData;
import core.internal.ui.PlugDataWin;

/** Classe per la gestione degli algoritmi di conversione
	per calcolare le formule LTL e la specifica Promela_
	Non ancora implementata!! */

public class AlgoManagerBA
{
	
	/** Memorizza il riferimento alla finestra "S.A. Topology". */
	private TopologyWindow rifTopology;
	
	/** Memorizza il riferimento alla finestra "State Diagram". */
	private StateWindow rifState;
	
	/** Memorizza il riferimento alla finestra "Sequence Diagram". */
	private SequenceWindow rifSequence;
	
	private BASpecified risBA_text;
	
	private BASpecified risBA_graph;
	
	private IPlugData plugData;
	private PlugDataWin plugDataWin;
	
	private plugin.topologydiagram.data.PlugData pdTopology;
	private plugin.statediagram.data.PlugData pdThread;
	private plugin.sequencediagram.data.PlugData pdSequence;
	
	private BAWindow BAW;
	
	/** Costruttore. */
	public AlgoManagerBA (IPlugData plugDt,PlugDataWin pdw, BAWindow rifBA)
	{
		plugData = plugDt;
		pdTopology =(plugin.topologydiagram.data.PlugData )plugData.getPlugDataManager().getPlugData("charmy.plugin.topology");
		pdThread =(plugin.statediagram.data.PlugData )plugData.getPlugDataManager().getPlugData("charmy.plugin.state");
		pdSequence =(plugin.sequencediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.sequence");
		BAW = rifBA;
		plugDataWin = pdw;
//		check = new ControlloIntegrita();
	}
	
	
	
	
	/** Metodo per attivare il calcolo delle LTL formule. */
	public void BA()
	{
		risBA_text = new BASpecified(pdTopology.getListaCanale(),
				pdThread.getListaDP(),pdSequence.getListaDS(),  BAW);
	}

	

	public void SaveBA()
	{
		FileDialog fd;
		String strFile;
		FileOutputStream fos;
		DataOutputStream dos;
		LinkedList llBA; 
		String pathFile;
		String row;
		boolean aCapo;
		
		try
		{
			fd = new FileDialog((Frame)plugDataWin.getMainPanel().getTopLevelAncestor(), "Salva LTL:", 1);
			
			fd.show();
			strFile = fd.getFile();
			if(strFile != null)        	
			{			
				pathFile = fd.getDirectory() + strFile;
				fos = new FileOutputStream(pathFile);            	
				dos = new DataOutputStream(fos);
				Object[] no = BAW.getDati();
				
				for (int i = 0; i < no.length; i++)
				{
					dos.writeBytes((String)no[i]);
				}
				dos.flush();
				fos.close();
				dos.close();            			
			}
			fd.dispose();
		}
		catch (Exception ex)
		{
			System.out.println(ex);
			System.out.println("File not saved!");
		}		
	} 
	
	
				
}