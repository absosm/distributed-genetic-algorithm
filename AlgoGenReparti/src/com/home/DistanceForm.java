package com.home;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Element;

public class DistanceForm extends JFrame {

	private JPanel contentPane;
	private JTable Distance;
	
	private DefaultTableModel initializeDistance() {
		
        SAXBuilder saxBuilder = new SAXBuilder();
        List<Element> villes =  null;
        Document document = null;
        int nbVilles = 0;
        
        DefaultTableModel model = new DefaultTableModel();
        try {
            document = saxBuilder.build(new File("villes.xml"));
            villes = document.getRootElement().getChildren();
            nbVilles = villes.size();
        } catch (Exception ex) {
        	System.out.println(ex);
        }
		
		
		for (int i=0; i<nbVilles+1; i++) {
			model.addColumn("");
		}
		Vector vector = new Vector();
		for (int i=0; i<nbVilles+1; i++) {
			if (i != 0) vector.add(i-1);
			else vector.add("");
		}
		model.addRow(vector);
		
		for (int i=0; i<nbVilles; i++) {
			vector = new Vector();
			vector.add(i);
			model.addRow(vector);
		}
		
		for (int i=0; i<nbVilles; i++) {
			model.setValueAt(0, i+1, i+1);
		}
		
		int i=0;
		for (Element ville:villes) {
			List<Element> distances = ville.getChild("distances").getChildren();
			int j=i+1;
			for (Element value:distances) {
				model.setValueAt(value.getText(), i+1, j+1);
				j++;
			}
			i++;
		}
		
		for (i=0; i<nbVilles; i++) {
			for (int j=i+1; j<nbVilles; j++) {
				model.setValueAt(model.getValueAt(i+1, j+1), j+1, i+1);
			}
		}

		return model;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DistanceForm frame = new DistanceForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DistanceForm() {
		setTitle("Table des distances entre les villes");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1007, 526);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		Distance = new JTable();
		Distance.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		Distance.setRowSelectionAllowed(false);
		Distance.setModel(initializeDistance());
		Voyages.setDistances(((DefaultTableModel)Distance.getModel()).getDataVector());
		TableColumn column = null;
		for (int i = 0; i < Distance.getColumnCount(); i++) {
		    column = Distance.getColumnModel().getColumn(i);
		    column.setPreferredWidth(50);
		}
		Distance.setTableHeader(null);
		scrollPane.setViewportView(Distance);
		panel.setLayout(gl_panel);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(725, Short.MAX_VALUE)
					.addComponent(btnClose)
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 962, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnClose))
		);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
	}
}
