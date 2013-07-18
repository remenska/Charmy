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
    
package plugin.statediagram.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import plugin.statediagram.controllo.ThreadCheck;

/**
 * @author Patrizio
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ThreadCheckWindow extends JDialog {

	private ThreadCheck tc;

	public ThreadCheckWindow(JFrame f, String msg){
		super(f, "Specification Errors", true);
		getContentPane().setLayout(new BorderLayout());
		JTextArea errorTextArea=new JTextArea("");		
		JScrollPane jsp;
		if(!msg.equals("")){
			errorTextArea.append(msg);
			jsp = new JScrollPane(errorTextArea);
			jsp.setPreferredSize(new Dimension(350,140));
			jsp.setAutoscrolls(true);
				
		}else{
			JTextArea jl = new JTextArea("\nThere are no errors!");
			jl.setEditable(false);
			jsp = new JScrollPane(jl);
			jsp.setPreferredSize(new Dimension(150,60));
			jsp.setAutoscrolls(false);
		}
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout (new FlowLayout(FlowLayout.CENTER));
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				dispose();     				
			}
		});
		ok.setMargin(new Insets(1,10,1,10));
		buttonPanel.add(ok);
		
		getContentPane().add("Center",jsp);
		getContentPane().add("South",buttonPanel);
    
		pack();
		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = SystemKit.getScreenSize();
		Dimension DialogSize = getSize();
		setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
		setVisible(true);        
		
	}

}
