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
    


package plugin.promela;

/**
 *
 * @author  Patrizio
 */
public class RecordDueCampi {
    
    private String campo1;
    private String campo2;
    
    /** Creates a new instance of RecordDueCampi */
    public RecordDueCampi() {
    }
    
    public void setCampo1(String s)
    {
        campo1=s;
    }
    
    public void setCampo2(String s)
    {
        campo2=s;
    }
    
    public String getCampo1()
    {
        return campo1;
    }
    
    public String getCampo2()
    {
        return campo2;
    }
}
