package com.home;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
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

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.util.DefaultShadowGenerator;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class MedianChart extends JFrame {

	private JPanel contentPane;
	private XYPlot plot;
	private Vector<XYSeries> series;
	
	public void setPathFile(String path_file) {
		
		series = new Vector<>();
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		SAXBuilder saxBuilder = new SAXBuilder();
		try {
            Document document = saxBuilder.build(new File(path_file));
            Element racine = document.getRootElement();
			Element tete = racine.getChild("tete");
			int nb_ag = Integer.parseInt(tete.getChildText("nombre_ag"));
			XYSeries s = new XYSeries("Médiane");
			series.add(s);
			dataset.addSeries(s);
			
			// affecter les BD qui contient les coordonnée des corps
			plot.setDataset(dataset);
			plot.getRenderer().setSeriesStroke(0, new BasicStroke(2));
			
			Element values = racine.getChild("values");
			List<Element> e_generation = values.getChildren();
			int id = 0;
			float median = 0;
			for (Element gen:e_generation) {
				id = Integer.parseInt(gen.getAttributeValue("id"));
				List<Element> ags = gen.getChildren();
				float somme = 0;
				for (Element ag : ags) {
					somme += Float.parseFloat(ag.getText());
				}
				median = somme/nb_ag;
				series.get(0).add(id, median);
			}
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MedianChart frame = new MedianChart();
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
	public MedianChart() {
		URL iconUrl = getClass().getResource("/images/logo.png");
		ImageIcon icon = new ImageIcon(iconUrl);
		setIconImage(icon.getImage());
		setResizable(false);
		setTitle("Graphique médiane d'évolution");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 815, 427);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		//////////  plot //////////////////////
		
        final XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(s0);
//        dataset.addSeries(s1);

        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Graphique médiane d'évolution",          // chart title
            "Génération",               // domain axis label
            "Distance",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,
            true
        );

        plot = chart.getXYPlot();
        final NumberAxis domainAxis = new NumberAxis("Génération");
        final NumberAxis rangeAxis = new NumberAxis("Distance (km)");
        chart.setBackgroundPaint(Color.white);
        plot.setDomainAxis(domainAxis);
        plot.setRangeAxis(rangeAxis);
        plot.setOutlinePaint(Color.black);
        //plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color. black);
        plot.setRangeGridlinePaint(Color.black);
        plot.setShadowGenerator(new DefaultShadowGenerator());
		
		///////////////////////////////////////
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnTerminer = new JButton("Terminer");
		btnTerminer.addActionListener(new ActionListener() {
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
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
						.addComponent(btnTerminer, Alignment.TRAILING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnTerminer)
					.addContainerGap(17, Short.MAX_VALUE))
		);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		setLocationRelativeTo(null);
	}
}
