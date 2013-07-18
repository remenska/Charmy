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

import plugin.topologydiagram.resource.data.ImpElementoId;

/**
 *
 * @author  FLAMEL 2005
 */

/**
 *
 * questa classe implementa il concetto di transizione 
 */

public class Transition extends ImpElementoId
{
    private Node Index;
    
    private Node Target;
    
    private String Label;
    
    
    private long idIndex =0;
    private long idTarget = 0;
    
    private long instanceIde=0;
    
    //**Memorizza le istanze**//
    private static int TrInstanceNumber = 0;
    
        
    public Transition(Node index,Node target,String label)
    {
        this.Index=index;
        this.Target=target;
        this.Label=label;
        this.idIndex = index.getId();
        this.idTarget = target.getId(); 
        instanceIde = incTrInstanceNumber();
        
    }
    
    
    public Node getNodeIndex()
    {
        return Index;
    }
    
    public long getIdIndex()
    {
        return Index.getId();
    }
    
    public long getIdTarget()
    {
        return Target.getId();
    }
    
    
    public long getId()
    {
        return instanceIde;
    }
    
    public Node getNodeTarget()
    {
        return Target;
    }
    
    public String getLabel()
    {
        return Label;
    }
    
    public void setLabel(String label)
    {
        this.Label = label;
    }
    
    public static int incTrInstanceNumber()
    {
            return TrInstanceNumber++;
    }
    
    public void setIndex(Node index)
    {
        this.Index = index;
    }
    
    public void setTarget(Node target)
    {
        this.Target = target;
    }
    
    public boolean isSelf()
    {
        if(this.getIdIndex()==this.getIdTarget())
            return true;
        else 
            return false;
    }
    
    
}

