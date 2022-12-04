package com.home;

import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BasicStroke;
import java.util.List;
import java.util.Vector;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.io.File;
import java.net.URL;
import javax.swing.JTextField;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.util.DefaultShadowGenerator;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainForm {

	private JFrame frmProbl;
	private JTable villeTable;
	private JLabel label;
	private Object villeSelected;
	private JLabel lblVille;
	private JSpinner nbIndividus;
	private JTextField MutationRate;
	private JTextField CrossoverRate;
	private JSpinner generation;
	private JSpinner nbAlgoGen;
	private DistanceForm distanceView;
	private XYPlot plot;
	private Vector<XYSeries> series;
	private Vector<Population> populations;
	private JComboBox list_ag;
	private JSpinner periodMig;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField path_file;
	private JRadioButton RadioButton3;
	private JRadioButton RadioButton1;
	private JRadioButton RadioButton2;
	private JTextField migrationRate;
	private JRadioButton RadioButton4;
	

	/**
	 * Launch the application.
	 */
	
	public int getCityCount() {
		return villeTable.getRowCount();
	}
	
	public static void main(String[] args) {
		
//		int n = Runtime.getRuntime().availableProcessors();
//		
//		System.out.println(n);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frmProbl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		villeSelected = null;
		initialize();
		label.setText(""+getCityCount());
		frmProbl.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		frmProbl.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmImportConfiguration = new JMenuItem("Import configuration");
		mnFichier.add(mntmImportConfiguration);
		
		JMenuItem mntmTerminer = new JMenuItem("Terminer");
		mntmTerminer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFichier.add(mntmTerminer);
		
		JMenu mnEdit = new JMenu("Modifier");
		menuBar.add(mnEdit);
		
		JMenu mnNewMenu = new JMenu("Affichage");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Agrandissement graphique d'\u00E9volution");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Zoom analyse = new Zoom();
				analyse.setVisible(true);
			}
		});
		
		JMenuItem mntmDonne = new JMenuItem("Afficher les solutions");
		mntmDonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SolutionsData sd = new SolutionsData();
				sd.setPathSaveData(path_file.getText());
				sd.setVisible(true);
			}
		});
		mnNewMenu.add(mntmDonne);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Afficher le graphique m\u00E9diane");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MedianChart median = new MedianChart();
				median.setPathFile(path_file.getText());
				median.setVisible(true);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenu mnAide = new JMenu("Aide");
		menuBar.add(mnAide);
		
		JMenuItem mntmContenu = new JMenuItem("Contenu");
		mnAide.add(mntmContenu);
		
		JMenuItem mntmAPropos = new JMenuItem("Propos");
		mnAide.add(mntmAPropos);
		series = new Vector<>();
		//frmProbl.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private DefaultTableModel initializeVilles() {
		
        SAXBuilder saxBuilder = new SAXBuilder();
        List<Element> villes =  null;
        Document document = null;
        
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Les villes");
        try {
            document = saxBuilder.build(new File("villes.xml"));
            villes = document.getRootElement().getChildren();
            Vector Villes = new Vector();
            for (Element ville : villes) {
            	model.addRow(new String[] {ville.getChild("nom").getText()});
            	Villes.add(ville.getChild("nom").getText());
            }
    		Voyages.setVilles(Villes);
    		Chromosome.setCodage(Villes);
        } catch (Exception ex) {
        	System.out.println(ex);
        }
        
		return model;
	}
	
	private void initialize() {
		
        URL urlIcon = getClass().getResource("/images/logo.png");
        ImageIcon image = new ImageIcon(urlIcon);
        
		
		frmProbl = new JFrame();
		frmProbl.setTitle("Etude l'infuence de t\u00F4t de migration des g\u00E9n\u00E9omes sur la convergence des algorithmes g\u00E9n\u00E9tique r\u00E9parti");
		frmProbl.setBounds(100, 100, 793, 705);
		frmProbl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmProbl.setIconImage(image.getImage());
		distanceView = new DistanceForm();
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Les villes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JLabel lblNombreDeVille = new JLabel("Nombre de villes:");
		
		label = new JLabel("0");
		label.setForeground(Color.BLUE);
		
		JLabel lblVilleDpart = new JLabel("Ville de d\u00E9part:");
		
		lblVille = new JLabel("[s\u00E9l\u00E9ctionez ville]");
		lblVille.setForeground(Color.BLUE);
		
		JButton btnNewButton = new JButton("Afficher Matrix ");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				distanceView.setVisible(true);
			}
		});
		
		JPanel chartPanel = new JPanel();
		chartPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Graphique", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		//////////  plot //////////////////////
		
        final XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(s0);
//        dataset.addSeries(s1);

        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Evolution des générations",          // chart title
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
        
        ChartPanel chp = new ChartPanel(chart);
        chp.setPreferredSize(new java.awt.Dimension(500, 270));
		
		///////////////////////////////////////
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblVilleDpart)
								.addComponent(lblNombreDeVille, Alignment.LEADING))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblVille)
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDeVille)
						.addComponent(label))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVilleDpart)
						.addComponent(lblVille))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addGap(30))
		);
		
		villeTable = new JTable();
		villeTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (villeTable.getSelectedRow()>-1) {
					villeSelected = villeTable.getValueAt(villeTable.getSelectedRow(), 0);
					lblVille.setText(villeSelected+"");
				}
			}
		});
		villeTable.setModel(initializeVilles());
		villeTable.setRowSelectionInterval(6, 6);
		villeSelected = villeTable.getValueAt(6, 0);
		lblVille.setText(villeSelected+"");
		scrollPane.setViewportView(villeTable);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Evolution", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Politique de migration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		
		GroupLayout groupLayout = new GroupLayout(frmProbl.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
						.addComponent(panel_2, 0, 0, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chartPanel, GroupLayout.PREFERRED_SIZE, 372, GroupLayout.PREFERRED_SIZE)
					.addGap(68))
		);
		
		JLabel lblFaireLaM = new JLabel("P\u00E9riode de migration:");
		
		periodMig = new JSpinner();
		periodMig.setModel(new SpinnerNumberModel(new Integer(50), new Integer(0), null, new Integer(1)));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Strat\u00E9gie de migration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		migrationRate = new JTextField();
		migrationRate.setText("0.05");
		migrationRate.setColumns(10);
		
		JLabel lblTauxDeMigration = new JLabel("Taux de migration:");
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblTauxDeMigration)
								.addComponent(lblFaireLaM))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(migrationRate, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
								.addComponent(periodMig, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFaireLaM)
						.addComponent(periodMig, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(migrationRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTauxDeMigration))
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
					.addGap(25))
		);
		
		RadioButton1 = new JRadioButton("meilleur vers les autres");
		buttonGroup.add(RadioButton1);
		RadioButton1.setMnemonic('1');
		RadioButton1.setSelected(true);
		
		RadioButton2 = new JRadioButton("al\u00E9atoire vers les autres");
		buttonGroup.add(RadioButton2);
		RadioButton2.setMnemonic('2');
		
		RadioButton3 = new JRadioButton("Sans migration");
		RadioButton3.setMnemonic('3');
		buttonGroup.add(RadioButton3);
		
		RadioButton4 = new JRadioButton("mauvaise vers les autres");
		RadioButton4.setMnemonic('4');
		buttonGroup.add(RadioButton4);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(RadioButton3)
						.addComponent(RadioButton1)
						.addComponent(RadioButton2)
						.addComponent(RadioButton4))
					.addContainerGap(81, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addComponent(RadioButton3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(RadioButton1)
					.addGap(1)
					.addComponent(RadioButton4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(RadioButton2)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);
		panel_2.setLayout(gl_panel_2);
		
		list_ag = new JComboBox();
		
		JButton btnAfficherPopulation = new JButton("Afficher population");
		btnAfficherPopulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = list_ag.getSelectedIndex();
				//viewPopulation(populations.get(index));
				ViewPopForm vpf = new ViewPopForm("Affichage population de AG"+index, populations.get(index));
				vpf.setVisible(true);
			}
		});
		
		path_file = new JTextField();
		path_file.setText("data.xml");
		path_file.setColumns(10);
		
		JLabel lblFichierDeSauvgarde = new JLabel("Fichier de sauvgarde:");
		
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				int returnVal = fc.showOpenDialog(frmProbl);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					path_file.setText(fc.getSelectedFile().getPath());
				}
			}
		});
		GroupLayout gl_chartPanel = new GroupLayout(chartPanel);
		gl_chartPanel.setHorizontalGroup(
			gl_chartPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chartPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_chartPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(chp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
						.addGroup(gl_chartPanel.createSequentialGroup()
							.addComponent(lblFichierDeSauvgarde)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(path_file, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(button)
							.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
							.addComponent(btnAfficherPopulation)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(list_ag, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_chartPanel.setVerticalGroup(
			gl_chartPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_chartPanel.createSequentialGroup()
					.addGroup(gl_chartPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(list_ag, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAfficherPopulation)
						.addComponent(path_file, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFichierDeSauvgarde)
						.addComponent(button))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chp, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		chartPanel.setLayout(gl_chartPanel);
		
		JLabel lblNombreDesCouples = new JLabel("La taille d'un population :");
		
		nbIndividus = new JSpinner();
		nbIndividus.setModel(new SpinnerNumberModel(30, 1, 1000, 1));
		
		JLabel lblTauxDeCroisement = new JLabel("Taux de Croisement:");
		
		JLabel lblTauxDeMutation = new JLabel("Taux de Mutation:");
		
		MutationRate = new JTextField();
		MutationRate.setText("0.04");
		MutationRate.setColumns(10);
		
		CrossoverRate = new JTextField();
		CrossoverRate.setText("0.8");
		CrossoverRate.setColumns(10);
		
		JButton btnShow = new JButton("D\u00E9marer\r\n");
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Superviseur.setCurrentGeneration(0);
				// comments
				int nbAG = (int)nbAlgoGen.getValue();
				
				/* affecter le ville de départ qui faire 
				le voyage à partir de cette ville */
				Voyages.setVilleDepart(villeSelected);
				
				AlgorithmGenetique.init();
				
				// conmments
				AlgorithmGenetique.setNombreGeneration((int)generation.getValue());
				
				populations = new Vector<>();
				for (int i=0; i<nbAG; i++) {
					
					// Générer Population Initiale à chaque itération
					Population tmpPop = new Population();
					tmpPop.setTaillePopulation((int)nbIndividus.getValue());
					for (int j=0; j<tmpPop.getTaillePopulation(); j++) {
						
						// Générer un nouvelle chromosome selon le codage réel de voyage 
						Chromosome chromosome = new Chromosome(Voyages.faireVoyage());
						
						// cette boucle est éviter la duplication des chromosomes dans la population
						while (!tmpPop.AjouterChromosome(chromosome)) {
							chromosome = new Chromosome(Voyages.faireVoyage());
						}
					}
					tmpPop.setCrossoverRate(Float.parseFloat(CrossoverRate.getText()));
					tmpPop.setMutationRate(Float.parseFloat(MutationRate.getText()));
					
					populations.add(tmpPop);
				}

				// Initialiser les series qui dessin les graphes GAs.
				series = new Vector<>();
				XYSeriesCollection dataset = new XYSeriesCollection();
				
				DefaultComboBoxModel model = new DefaultComboBoxModel();
				// lance l'evolution avec GA
				for (int i=0; i<nbAG; i++) {
					
					AlgorithmGenetique tmpAG = new AlgorithmGenetique(populations.get(i));
					model.addElement("AG "+i);
					XYSeries s = new XYSeries("AG"+tmpAG.getIndex());
					series.add(s);
					dataset.addSeries(s);
				}
				
				list_ag.setModel(model);
				
				// affecter les BD qui contient les coordonnée des corps
				plot.setDataset(dataset);
				
				for (int i = 0; i < AlgorithmGenetique.getTout().size(); i++) {
					 plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
				}
				
				Superviseur.setSeries(series);
				Superviseur.setPeriodMigration((int)periodMig.getValue());
				char x = (char)buttonGroup.getSelection().getMnemonic();
				Superviseur.setStategie(x);
				Superviseur.setTaux_migration(Float.parseFloat(migrationRate.getText()));
				Superviseur.setVille_depart(villeTable.getSelectedRow());
				Superviseur.setPath_file_sauvg(path_file.getText());
				Superviseur.setTaille_population(Integer.parseInt(nbIndividus.getValue().toString()));
				Superviseur.setTaux_croisement(Float.parseFloat(CrossoverRate.getText()));
				Superviseur.setTaux_mutation(Float.parseFloat(MutationRate.getText()));
				Superviseur.setNb_generation(Integer.parseInt(generation.getValue().toString()));
				Superviseur.setNombre_ag(Integer.parseInt(nbAlgoGen.getValue().toString()));
				
				Superviseur.init_sauvgard();
				
				// lancer les thread qui faire les tâches de l'GAs
				AlgorithmGenetique.EvolutionTout();
			}
		});
		
		JLabel lblNombreDeGnration = new JLabel("Nombre de g\u00E9n\u00E9ration:");
		
		generation = new JSpinner();
		generation.setModel(new SpinnerNumberModel(500, 0, 1000000000, 1));
		
		nbAlgoGen = new JSpinner();
		nbAlgoGen.setModel(new SpinnerNumberModel(5, 1, 100, 1));
		
		JLabel lblNombreDalgorithmeGntique = new JLabel("Nombre d'algorithme g\u00E9n\u00E9tique:");
		
		JButton btnNewButton_1 = new JButton("Suspendre");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Superviseur.Suspendre();
			}
		});
		
		JButton btnNewButton_2 = new JButton("R\u00E9sume");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Superviseur.setPath_file_sauvg(path_file.getText());
				Superviseur.Resume();
				nbAlgoGen.setValue(Superviseur.getNombre_ag());
				int sel_v = Superviseur.getVille_depart();
				villeTable.setRowSelectionInterval(sel_v, sel_v);
				villeSelected = villeTable.getValueAt(sel_v, 0);
				lblVille.setText(villeSelected+"");
				nbIndividus.setValue(Superviseur.getTaille_population());
				CrossoverRate.setText(""+Superviseur.getTaux_croisement());
				MutationRate.setText(""+Superviseur.getTaux_mutation());
				generation.setValue(Superviseur.getNb_generation());
				periodMig.setValue(Superviseur.getPeriod_migration());
				migrationRate.setText(""+Superviseur.getTaux_migration());
				
				switch (Superviseur.getStrtg_migration()) {
				case '1':
					buttonGroup.setSelected(RadioButton1.getModel(), true); break;
				case '2': 
					buttonGroup.setSelected(RadioButton2.getModel(), true); break;
				case '3':
					buttonGroup.setSelected(RadioButton3.getModel(), true); break;
				case '4':
					buttonGroup.setSelected(RadioButton4.getModel(), true); break;
				}
				
				// ================================================================
				// comments
				int nbAG = (int)nbAlgoGen.getValue();
				
				/* affecter le ville de départ qui faire 
				le voyage à partir de cette ville */
				Voyages.setVilleDepart(villeSelected);
				
				AlgorithmGenetique.init();
				
				// conmments
				AlgorithmGenetique.setNombreGeneration((int)generation.getValue());
				
				SAXBuilder saxBuilder = new SAXBuilder();
				try {
		            Document document = saxBuilder.build(new File(path_file.getText()));
		            Element racine = document.getRootElement();
		            Element contents = racine.getChild("contents");
		            List<Element> e_popolations = contents.getChildren();
		            populations = new Vector<>();
		            for (Element p:e_popolations) {
		            	
		            	List<Element> e_chromosomes = p.getChildren();
		            	
						// Générer Population Initiale à chaque itération
						Population tmpPop = new Population();
						tmpPop.setTaillePopulation((int)nbIndividus.getValue());
						
						
						for (Element c:e_chromosomes) {
							
							List<Element> e_genes = c.getChildren();
							Chromosome chromosome = new Chromosome();
							
							for (Element g:e_genes) {
								chromosome.addGene(Integer.parseInt(g.getText()));
							}
							tmpPop.AjouterChromosome(chromosome);
						}
						
						tmpPop.setCrossoverRate(Float.parseFloat(CrossoverRate.getText()));
						tmpPop.setMutationRate(Float.parseFloat(MutationRate.getText()));
						
						populations.add(tmpPop);
		            }
		            
					// Initialiser les series qui dessin les corps GAs.
					series = new Vector<>();
					XYSeriesCollection dataset = new XYSeriesCollection();
					
					DefaultComboBoxModel model = new DefaultComboBoxModel();
					// lance l'evolution avec GA
					for (int i=0; i<nbAG; i++) {
						
						AlgorithmGenetique tmpAG = new AlgorithmGenetique(populations.get(i));
						model.addElement("AG "+i);
						XYSeries s = new XYSeries("AG"+tmpAG.getIndex());
						series.add(s);
						dataset.addSeries(s);
					}
					
					Element values = racine.getChild("values");
					Superviseur.setValuesWrite(values);
					List<Element> e_generation = values.getChildren();
					int id = 0;
					for (Element gen:e_generation) {
						
						id = Integer.parseInt(gen.getAttributeValue("id"));
						
						List<Element> ags = gen.getChildren();
						
						for (Element ag : ags) {
							series.get(Integer.parseInt(ag.getAttributeValue("id"))).
							add(id, Float.parseFloat(ag.getText()));
						}
					}
					
					list_ag.setModel(model);
					
					// affecter les BD qui contient les coordonnée des corps
					plot.setDataset(dataset);
					
					for (int i = 0; i < AlgorithmGenetique.getTout().size(); i++) {
						 plot.getRenderer().setSeriesStroke(i, new BasicStroke(2));
					}
		       
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
				
				Superviseur.setSeries(series);
				Superviseur.setPeriodMigration((int)periodMig.getValue());
				char x = (char)buttonGroup.getSelection().getMnemonic();
				Superviseur.setStategie(x);
				Superviseur.setVille_depart(villeTable.getSelectedRow());
				Superviseur.setPath_file_sauvg(path_file.getText());
				Superviseur.setTaille_population(Integer.parseInt(nbIndividus.getValue().toString()));
				Superviseur.setTaux_croisement(Float.parseFloat(CrossoverRate.getText()));
				Superviseur.setTaux_mutation(Float.parseFloat(MutationRate.getText()));
				Superviseur.setNb_generation(Integer.parseInt(generation.getValue().toString()));
				Superviseur.setNombre_ag(Integer.parseInt(nbAlgoGen.getValue().toString()));
				
				// lancer les thread qui faire les tâches de l'GAs
				if (Superviseur.getCurrentGeneration() < Superviseur.getNb_generation())
					AlgorithmGenetique.EvolutionTout();
			}
		});

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(40)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblTauxDeMutation)
								.addComponent(lblTauxDeCroisement)
								.addComponent(lblNombreDesCouples)
								.addComponent(lblNombreDeGnration)))
						.addComponent(lblNombreDalgorithmeGntique)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btnShow)
							.addGap(18)
							.addComponent(btnNewButton_1)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addComponent(nbAlgoGen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(nbIndividus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(CrossoverRate, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
							.addComponent(MutationRate, 0, 0, Short.MAX_VALUE)
							.addComponent(generation, 0, 0, Short.MAX_VALUE))
						.addComponent(btnNewButton_2))
					.addContainerGap(530, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDesCouples)
						.addComponent(nbIndividus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(CrossoverRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTauxDeCroisement))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(MutationRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTauxDeMutation))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDeGnration)
						.addComponent(generation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(1)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(nbAlgoGen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNombreDalgorithmeGntique))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnShow)
						.addComponent(btnNewButton_2)
						.addComponent(btnNewButton_1))
					.addGap(59))
		);
		panel_1.setLayout(gl_panel_1);
		frmProbl.getContentPane().setLayout(groupLayout);
	}
}
