package com.home;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JTable;

public class ViewPopForm extends JFrame {

	private JPanel contentPane;
	private JTable populationTable;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewPopForm frame = new ViewPopForm("");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ViewPopForm(String title, Population population) {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 823, 383);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle(title);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnFermer = new JButton("Fermer");
		btnFermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
						.addComponent(btnFermer, Alignment.TRAILING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnFermer)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		populationTable = new JTable();
		populationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		populationTable.setTableHeader(null);
		scrollPane.setViewportView(populationTable);
		contentPane.setLayout(gl_contentPane);
		
		DefaultTableModel modelPopulation = (DefaultTableModel)populationTable.getModel();
		modelPopulation.setColumnCount(0);
		for (int i=0; i<population.getChromosome(0).getNombreGenes(); i++) {
			modelPopulation.addColumn(i);
		}
		modelPopulation.setRowCount(0);
		for (int i=0; i<population.getNombreChromosomes(); i++) {
			
			Vector row = population.getChromosome(i).Decodage();
			Vector tmp = new Vector();
			for (int j=0; j<row.size(); j++) {
				tmp.add(row.elementAt(j));
			}
			modelPopulation.addRow(tmp);
		}
		TableColumn column = null;
		for (int i = 0; i < populationTable.getColumnCount(); i++) {
		    column = populationTable.getColumnModel().getColumn(i);
		    column.setPreferredWidth(75);
		}
	}
}
