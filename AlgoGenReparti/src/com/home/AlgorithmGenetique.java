package com.home;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class AlgorithmGenetique extends Thread{
	
	private int id;
	private Population population;
	private static int NombreGeneration;
	private static Vector<AlgorithmGenetique> Tout;
	private static Superviseur superviseur;
	private Semaphore sem = new Semaphore(0);
	
	public AlgorithmGenetique(Population population) {
		
		this.population = population;
		
		if (AlgorithmGenetique.Tout == null)
			AlgorithmGenetique.Tout = new Vector<>();
			
		if (AlgorithmGenetique.superviseur == null)
			AlgorithmGenetique.superviseur = new Superviseur();
		
		this.id = AlgorithmGenetique.Tout.size();
		
		AlgorithmGenetique.Tout.add(this);
		
		int MaxNbGene = population.getChromosome(0).getNombreGenes();
		
		int x = 5 + Rndm.nextInt((MaxNbGene/2)-6);
		int y = (MaxNbGene/2) + Rndm.nextInt(MaxNbGene-(MaxNbGene/2))+1;
		while ((y-x<10) || (y-x>30)) {
			x = 5 + Rndm.nextInt((MaxNbGene/2)-6);
			y = (MaxNbGene/2) + Rndm.nextInt(MaxNbGene-(MaxNbGene/2))+1;
		}
		
		// générer un seuil aléatoire pour faire l'opération du croisement
		population.setSill(1 + Rndm.nextInt(population.getChromosome(0).getNombreGenes()-2));
		
		start();
	}
	
	public static void init() {
		Tout = null;
		superviseur = null;
	}
	
	private void Evaluation() {
		
		for (int i=0; i<population.getNombreChromosomes(); i++) {
			Chromosome chromosome = population.getChromosome(i);
			if (!chromosome.isEvaluated()) {
				Vector listVille = chromosome.Decodage();
				chromosome.setFitness(1/Voyages.getDistance(listVille));
			}
		}		
	}
	
	public void Evolution() {
		sem.release();
	}
	
	public void Evolution(int g) {			
		
		// affecter les fitness de chaque chromosome pour faire opï¿½ration du sï¿½lï¿½ction
		Evaluation();
		
		// opï¿½ration du sï¿½lï¿½ction
		population.Selection();
		
		// aprï¿½s l'opï¿½ration du sï¿½lï¿½ction les rï¿½sutats sont des chromosome 
		// les plus chance pour faire la croisement et la mutation
		
		// Opï¿½ration de Croisement
		int i=0,j;
		while (i<(population.getCrossoverSelected().size()-1)) {
			j = i+1;
			while (j<(population.getCrossoverSelected().size())) {
				int parentID1 = (int)population.getCrossoverSelected().elementAt(i);
				int parentID2 = (int)population.getCrossoverSelected().elementAt(j);
				population.Crossover(parentID1, parentID2);
				j++;
			}
			i++;
		}
		
		// Opï¿½ration de Mutation
		for (i=0; i<population.getMutationSelected().size(); i++) {
			population.Mutation((int)population.getMutationSelected().elementAt(i));
		}
		
		// affecter les fitness de chaque chromosome pour faire opï¿½ration du sï¿½lï¿½ction
		Evaluation();
		
		// pour remplacer les chromosomes selon la pï¿½riotitï¿½ dans nouvelle gï¿½nï¿½ration
		population.Remplacement();
		
		AlgorithmGenetique.superviseur.putSolution(id, getMeilleurSolution());
		
//		System.out.println("AG: "+id+" Generation"+g+": D="+ 1/population.getFitness()
//				+ "(Km), Fitness=" +  population.getFitness());
	}

	public static int getNombreGeneration() {
		return AlgorithmGenetique.NombreGeneration;
	}

	public static void setNombreGeneration(int nombreGeneration) {
		AlgorithmGenetique.NombreGeneration = nombreGeneration;
	}
	
	public int getIndex() {
		return this.id;
	}
	
	public static Vector<AlgorithmGenetique> getTout() {
		return Tout;
	}
	
	public static void EvolutionTout() {
		
		for (AlgorithmGenetique ag : Tout) {
			ag.Evolution();
		}
	}
	
	public Chromosome getMeilleurSolution() {
		return this.population.getChromosome(0).instance();
	}
	
	public Population getPopulation() {
		return population;
	}
	
	@Override
	public void run() {
		
		for (int g=0; g<AlgorithmGenetique.NombreGeneration; g++) {
			
			try {
				sem.acquire();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Evolution(g);
		}
	}
}
