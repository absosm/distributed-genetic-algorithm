package com.home;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SolutionsData extends JFrame {

	private JPanel contentPane;
	private JTextField tf_path;
	private JTable table;
	
	public void setPathSaveData(String path) {
		tf_path.setText(path);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SolutionsData frame = new SolutionsData();
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
	public SolutionsData() {
		setTitle("Affichage des solution obtenu de chaque g\u00E9n\u00E9ration");
		
		URL urlIcon = getClass().getResource("/images/logo.png");
		ImageIcon icon = new ImageIcon(urlIcon);
		setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 678, 474);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Tables des solutions obtenu", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton button = new JButton("Terminer");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
					.addGap(11)
					.addComponent(button))
		);
		
		JLabel lblFichierDeSauvgarde = new JLabel("Fichier de sauvgarde :");
		
		tf_path = new JTextField();
		tf_path.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnAfficher = new JButton("Afficher");
		btnAfficher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				SAXBuilder saxBuilder = new SAXBuilder();
				try {
		            Document document = saxBuilder.build(new File(tf_path.getText()));
		            Element racine = document.getRootElement();
					Element tete = racine.getChild("tete");
					int nb_ag = Integer.parseInt(tete.getChildText("nombre_ag"));
					DefaultTableModel model = new DefaultTableModel();
					model.addColumn("Génération");
					for (int i=0; i<nb_ag; i++) {
						model.addColumn("AG"+i);
					}
					model.addColumn("Moyenne");
					Element values = racine.getChild("values");
					List<Element> e_generation = values.getChildren();
					int id = 0;
					for (Element gen:e_generation) {
						
						id = Integer.parseInt(gen.getAttributeValue("id"));
						Vector<String> vector = new Vector<>();
						List<Element> ags = gen.getChildren();
						vector.add(""+id);
						float somme = 0;
						for (Element ag : ags) {
							vector.add(ag.getText());
							somme += Float.parseFloat(ag.getText());
						}
						vector.add(""+(somme/nb_ag));
						model.addRow(vector);
					}
					
					table.setModel(model);
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
				
				
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblFichierDeSauvgarde)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tf_path, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnAfficher)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFichierDeSauvgarde)
						.addComponent(tf_path, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAfficher))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		setLocationRelativeTo(null);
	}
}
