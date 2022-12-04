package com.home;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jfree.data.xy.XYSeries;

public class Superviseur extends Thread {
	
	private static Document doc_read;
	private static Element racine_read;
	private static Element tete_read;
	private static Element values_read;
	private static Element contents_read;
	///////////////////////////////////////////////
	private static Document doc_write;
	private static Element racine_write;
	private static Element tete_write;
	private static Element values_write;
	private static Element contents_write;
	//////////////////////////////////////////////
	private HashMap<Integer, Chromosome> Solutions = null;
	private static int current_generation = 0;
	private static Vector<XYSeries> series;
	private static Semaphore mutex = new Semaphore(1);
	private static int nombre_ag;
	private static int ville_depart;
	private static int taille_population;
	private static float taux_croisement;
	private static float taux_mutation;
	private static int nb_generation;
	private static int period_migration;
	private static char strtg_migration = '1';
	private static float taux_migration;
	private static String path_file_sauvg;
	
	public Superviseur() {
		
		Solutions = new HashMap<Integer, Chromosome>();
		start();
	}
	
	public static void init_sauvgard() {
		
		tete_write = new Element("tete");
		values_write = new Element("values");
		
		Element nombre_ag = new Element("nombre_ag");
		nombre_ag.setText(""+Superviseur.nombre_ag);
		Element ville_depart = new Element("ville_depart");
		ville_depart.setText(""+Superviseur.ville_depart);
		Element taille_population = new Element("taille_population");
		taille_population.setText(""+Superviseur.taille_population);
		Element taux_croisement = new Element("taux_croisement");
		taux_croisement.setText(""+Superviseur.taux_croisement);
		Element taux_mutation = new Element("taux_mutation");
		taux_mutation.setText(""+Superviseur.taux_mutation);
		Element nb_generation = new Element("nb_generation");
		nb_generation.setText(""+Superviseur.nb_generation);
		Element period_migration = new Element("period_migration");
		period_migration.setText(""+Superviseur.period_migration);
		Element strtg_migration = new Element("strtg_migration");
		strtg_migration.setText(""+Superviseur.strtg_migration);
		Element taux_migration = new Element("taux_migration");
		taux_migration.setText(""+Superviseur.taux_migration);
				
		tete_write.addContent(nombre_ag);
		tete_write.addContent(ville_depart);
		tete_write.addContent(taille_population);
		tete_write.addContent(taux_croisement);
		tete_write.addContent(taux_mutation);
		tete_write.addContent(nb_generation);
		tete_write.addContent(period_migration);
		tete_write.addContent(strtg_migration);
		tete_write.addContent(taux_migration);
	}
	
	private void P() {
		
		try {
			Superviseur.mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void V() {
		Superviseur.mutex.release();
	}
	
	private void Strategie1() {
		
		float max = 0;
		Object ag_id=0;
		
		Set cles = Solutions.keySet();
		Iterator iterator = cles.iterator();
		
		while (iterator.hasNext()) {
			Object i = iterator.next();
			if (Solutions.get(i).getFitness()>max) {
				max = Solutions.get(i).getFitness();
				ag_id = i;
			}
		}
		
		int nbSousPopulation = (int)(taille_population * taux_migration);
		
		Population bestPopulation = AlgorithmGenetique.getTout().elementAt((int)ag_id).getPopulation();
		Population popMigree = new Population();
		for (int i=0; i<nbSousPopulation; i++) {
			popMigree.AjouterChromosome(bestPopulation.getChromosome(i).instance());
		}
		
		Solutions.remove(ag_id);
		
		iterator = cles.iterator();
		
		System.out.println("(Statégie 1): (" + nbSousPopulation + ") chromosomes migrée " 
				+ "depuis AG"+ag_id+", vers les AGs:");
		
		while (iterator.hasNext()) {
			Object i = iterator.next();
			System.out.print("AG"+i+"; ");
			Population p = AlgorithmGenetique.getTout().get((int)i).getPopulation();
			p.AjouterSousPopulation(popMigree);
		}
//		System.out.println("");
		
//		System.out.println("meilleur processus est: AG"+ag_id+", de resultat:"
//				+(1/chromosomeMigree.getFitness())+"(km)");
	}
	
	private void Strategie2() {
				
		Set cles = Solutions.keySet();
		Iterator iterator = cles.iterator();
		int luck = Rndm.nextInt(Solutions.size());
		/*Object ag_id = iterator.next();
		for (int i=0; i<luck; i++) {
			if (iterator.hasNext())
				ag_id = iterator.next();
		}*/
		int ag_id = luck;
		
		int nbSousPopulation = (int)(taille_population * taux_migration);
		
		Population selPopulation = AlgorithmGenetique.getTout().elementAt(ag_id).getPopulation();
		Population popMigree = new Population();
		for (int i=0; i<nbSousPopulation; i++) {
			popMigree.AjouterChromosome(selPopulation.getChromosome(i).instance());
		}
		
		Solutions.remove(ag_id);
		
		System.out.println("(Statégie2): (" + nbSousPopulation + ") chromosomes migrée " 
						+ "depuis AG"+ag_id+", vers les AGs:");
		
		iterator = cles.iterator();
		while (iterator.hasNext()) {
			Object i = iterator.next();
			System.out.print("AG"+i+"; ");
			Population p = AlgorithmGenetique.getTout().get((int)i).getPopulation();
			p.AjouterSousPopulation(popMigree);
		}
		System.out.println("");
		
//		System.out.println("meilleur processus est: AG"+ag_id+", de resultat:"
//				+(1/chromosomeMigree.getFitness())+"(km)");
	}
	
	private void Strategie4() {
		
		float min = 0;
		Object ag_id=0;
		
		Set cles = Solutions.keySet();
		Iterator iterator = cles.iterator();
		
		while (iterator.hasNext()) {
			Object i = iterator.next();
			if (Solutions.get(i).getFitness()<min) {
				min = Solutions.get(i).getFitness();
				ag_id = i;
			}
		}
		
		int nbSousPopulation = (int)(taille_population * taux_migration);
		
		Population selPopulation = AlgorithmGenetique.getTout().elementAt((int)ag_id).getPopulation();
		Population popMigree = new Population();
		for (int i=0; i<nbSousPopulation; i++) {
			popMigree.AjouterChromosome(selPopulation.getChromosome(i).instance());
		}
		
		Solutions.remove(ag_id);
		
		iterator = cles.iterator();
		
		System.out.println("(Statégie 4): (" + nbSousPopulation + ") chromosomes migrée " 
				+ "depuis AG"+ag_id+", vers les AGs:");
		
		while (iterator.hasNext()) {
			Object i = iterator.next();
			System.out.print("AG"+i+"; ");
			Population p = AlgorithmGenetique.getTout().get((int)i).getPopulation();
			p.AjouterSousPopulation(popMigree);
		}
		System.out.println("");
		
//		System.out.println("meilleur processus est: AG"+ag_id+", de resultat:"
//				+(1/chromosomeMigree.getFitness())+"(km)");
	}
	
	public void putSolution(int id_ga, Chromosome chromosome) {
		
		/* en utilise le méthode P() et V() pour respecter principe
		 *  MUTEX dans CS 'Solution'
		 */
		P();
		
		// remlire les solution dans un dictionnaire appelé 'Solution'
		Solutions.put(id_ga, chromosome);
		if (AlgorithmGenetique.getTout().size() == Solutions.size()) {
			
			/* générer un élément et affecter les attributs et les information 
			 * pour utiliser de génération de fichier xml
			 */
			Element e_generation = new Element("generation");
			Attribute attr_generation = new Attribute("id",""+current_generation);
			e_generation.setAttribute(attr_generation);
			
			// Sauvgarde des fitness de chaque génération			
			for (int i=0; i<Solutions.size(); i++) {
				float distance = 1/(Solutions.get((int)i).getFitness());
				series.get(i).add(current_generation, distance);
				Element ag = new Element("ag");
				Attribute attr_ag = new Attribute("id",""+i);
				ag.setAttribute(attr_ag);
				ag.setText(""+distance);
				e_generation.addContent(ag);
			}
			values_write.addContent(e_generation);
			if ((current_generation>0) && (current_generation%period_migration == 0)) {
				
				switch (Superviseur.strtg_migration) {
				case '1': Strategie1(); break;
				case '2': Strategie2(); break;
				case '4': Strategie4(); break;
				default: break;
				}
			}
			current_generation++;
			Solutions.clear();
			
			if (current_generation<nb_generation)
				AlgorithmGenetique.EvolutionTout();
			else
				Superviseur.Suspendre();
		}
		
		V();
	}
	
	public static void setPeriodMigration(int period_migration) {
		Superviseur.period_migration = period_migration;
	}
	
	public static void setSeries(Vector<XYSeries> series) {
		Superviseur.series = series;
	}
	
	public static void setStategie(char strtg_migration) {
		Superviseur.strtg_migration = strtg_migration;
	}
	
	public static void setNombre_ag(int nombre_ag) {
		Superviseur.nombre_ag = nombre_ag;
	}

	public static void setVille_depart(int ville_depart) {
		Superviseur.ville_depart = ville_depart;
	}

	public static void setTaille_population(int taille_population) {
		Superviseur.taille_population = taille_population;
	}

	public static void setTaux_croisement(float taux_croisement) {
		Superviseur.taux_croisement = taux_croisement;
	}

	public static void setTaux_mutation(float taux_mutation) {
		Superviseur.taux_mutation = taux_mutation;
	}

	public static void setNb_generation(int nb_generation) {
		Superviseur.nb_generation = nb_generation;
	}

	public static void setPath_file_sauvg(String path_file_sauvg) {
		Superviseur.path_file_sauvg = path_file_sauvg;
	}

	public static void setTaux_migration(float taux_migration) {
		Superviseur.taux_migration = taux_migration;
	}

	public static int getNombre_ag() {
		return nombre_ag;
	}

	public static int getVille_depart() {
		return ville_depart;
	}

	public static int getTaille_population() {
		return taille_population;
	}

	public static float getTaux_croisement() {
		return taux_croisement;
	}

	public static float getTaux_mutation() {
		return taux_mutation;
	}

	public static int getNb_generation() {
		return nb_generation;
	}

	public static int getPeriod_migration() {
		return period_migration;
	}

	public static char getStrtg_migration() {
		return strtg_migration;
	}

	public static int getCurrentGeneration() {
		return current_generation;
	}

	public static void setCurrentGeneration(int generation) {
		Superviseur.current_generation = generation;
	}
	
	public static float getTaux_migration() {
		return taux_migration;
	}

	public static void Suspendre() {
		
		contents_write = new Element("contents");
		
		for (int i=0; i<AlgorithmGenetique.getTout().size(); i++) {
			
			Population population = AlgorithmGenetique.getTout()
					.elementAt(i).getPopulation();
			
			Element e_population = new Element("population");
			Attribute attrib_p = new Attribute("id", ""+i);
			e_population.setAttribute(attrib_p);
			
			for (int j=0; j<population.getTaillePopulation(); j++) {
				
				Chromosome chromosome = population.getChromosome(j);
				Element e_chromosome = new Element("chromosome");
				Attribute attrib_c = new Attribute("id", ""+j);
				e_chromosome.setAttribute(attrib_c);
				
				for (int k=0; k<chromosome.getNombreGenes(); k++) {
					Element g = new Element("g");
					Attribute attrib_g = new Attribute("id", ""+k);
					g.setAttribute(attrib_g);
					g.setText(""+chromosome.getGenes().elementAt(k));
					e_chromosome.addContent(g);
				}
				
				e_population.addContent(e_chromosome);
			}
			
			contents_write.addContent(e_population);
		}
		
		Element last_generation = new Element("last_generation");
		last_generation.setText(""+ current_generation);
		
		if (tete_write == null) {
			
			tete_write = new Element("tete");
			tete_write.setContent(tete_read.cloneContent());
			tete_write.removeChild("last_generation");
		}
		tete_write.addContent(last_generation);
		
		racine_write = new Element("ag_reparti");
		racine_write.addContent(tete_write);
		racine_write.addContent(values_write);
		racine_write.addContent(contents_write);
		
		doc_write = new Document(racine_write);
		
        try {
        	XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        	sortie.output(doc_write, new FileOutputStream(path_file_sauvg));
        }
        catch (Exception e){
        	e.printStackTrace();
        }
        
        tete_write = null;
        
		for (AlgorithmGenetique ag : AlgorithmGenetique.getTout()) {
			ag.suspend();
		}
	}
	
	public static void Resume() {
		
		SAXBuilder saxBuilder = new SAXBuilder();
		
		try {
            doc_read = saxBuilder.build(new File(path_file_sauvg));
            racine_read = doc_read.getRootElement();
            tete_read = racine_read.getChild("tete");
            values_read = racine_read.getChild("values");
            contents_read = racine_read.getChild("contents");
            
            nombre_ag = Integer.parseInt(tete_read.getChildText("nombre_ag"));
            ville_depart = Integer.parseInt(tete_read.getChildText("ville_depart"));
            taille_population = Integer.parseInt(tete_read.getChildText("taille_population"));
            taux_croisement = Float.parseFloat(tete_read.getChildText("taux_croisement"));
            taux_mutation = Float.parseFloat(tete_read.getChildText("taux_mutation"));
            nb_generation = Integer.parseInt(tete_read.getChildText("nb_generation"));
            period_migration = Integer.parseInt(tete_read.getChildText("period_migration"));
            strtg_migration = tete_read.getChildText("strtg_migration").charAt(0);
            taux_migration = Float.parseFloat(tete_read.getChildText("taux_migration"));
            current_generation = Integer.parseInt(tete_read.getChildText("last_generation"));
            
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	public static void setValuesWrite(Element val_write) {
		values_write = new Element("values");
		values_write.setContent(val_write.cloneContent());
	}
	
	@Override
	public void run() {
		
		while (true)
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
