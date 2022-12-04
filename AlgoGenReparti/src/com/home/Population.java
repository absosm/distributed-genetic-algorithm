package com.home;

import java.util.Collections;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Population {
	
	private Vector<Chromosome> Chromosomes;
	private Vector<Vector> contain;
	private int taillePopulation;
	private float Fitness;
	private float crossoverRate;
	private float mutationRate;
	private int sill;           // seuil de croisement
	private Vector crossoverSelected;
	private Vector mutationSelected;
	
	public Population() {
		Chromosomes = new Vector<>();
		contain = new Vector<>();
		Fitness = 0;
	}
	
	public boolean AjouterChromosome(Chromosome chromosome) {
		
		if (!contain.contains(chromosome.getGenes())) {
			contain.add(chromosome.getGenes());
			Chromosomes.add(chromosome);
			if (chromosome.getFitness() > Fitness) Fitness = chromosome.getFitness();
			return true;
		}else {
			return false;
		}
	}
	
	public void AjouterSousPopulation (Population newPopulation) {
		
		for (int i=0; i<newPopulation.getNombreChromosomes(); i++) {
			AjouterChromosome(newPopulation.getChromosome(i));
		}
	}
	
	public int getNombreChromosomes() {
		return Chromosomes.size();
	}

	public int getTaillePopulation() {
		return taillePopulation;
	}

	public void setTaillePopulation(int taillePopulation) {
		this.taillePopulation = taillePopulation;
	}
	
	public Chromosome getChromosome(int index) {
		return Chromosomes.elementAt(index);
	}

	public float getFitness() {
		return Fitness;
	}
	
	public float getCrossoverRate() {
		return crossoverRate;
	}

	public void setCrossoverRate(float crossoverRate) {
		this.crossoverRate = crossoverRate;
	}

	public float getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(float mutationRate) {
		this.mutationRate = mutationRate;
	}

	public Vector getCrossoverSelected() {
		return crossoverSelected;
	}

	public Vector getMutationSelected() {
		return mutationSelected;
	}
	
	public int getSill() {
		return sill;
	}

	public void setSill(int sill) {
		this.sill = sill;
	}

	public void Selection() {
		
		crossoverSelected = new Vector();
		mutationSelected = new Vector();
		
		Rollete rollete = new Rollete(Chromosomes);
		
		for (int i=0; i<getNombreChromosomes()*crossoverRate; i++) {
			
			int chromosome = rollete.getLuck();
			while (crossoverSelected.contains((int)chromosome)) {
				chromosome = rollete.getLuck();
			}
			crossoverSelected.add(chromosome);
		}
		
		for (int i=0; i<getNombreChromosomes()*mutationRate; i++) {
			
			int chromosome = rollete.getLuck();
			while (mutationSelected.contains((int)chromosome)) {
				chromosome = rollete.getLuck();
			}
			mutationSelected.add(chromosome);
		}
	}

	public void Crossover(int parentID1, int parentID2) {
		
		Vector parent1 = Chromosomes.elementAt(parentID1).getGenes();
		Vector parent2 = Chromosomes.elementAt(parentID2).getGenes();
		
		Chromosome child1 = new Chromosome();
		Chromosome child2 = new Chromosome();
		
		for (int k=0; k<parent1.size(); k++) {
			if (k<sill) {
				child1.addGene((int)parent1.elementAt(k));
				child2.addGene((int)parent2.elementAt(k));
			}else {
				int l = k;
				while (child1.getNombreGenes()<parent2.size()) {
					int m = l % parent1.size();
					while (child1.getGenes().contains(parent2.elementAt(m))) {
						l++;	
						m = l % parent1.size();
					}
					child1.addGene((int)parent2.elementAt(m));
				}
				l = k;
				while (child2.getNombreGenes()<parent1.size()) {
					int m = l % parent1.size();
					while (child2.getGenes().contains(parent1.elementAt(m))) {
						l++;	
						m = l % parent1.size();
					}
					child2.addGene((int)parent1.elementAt(m));
				}
				k=parent1.size();
			}
		}
		AjouterChromosome(child1);
		AjouterChromosome(child2);
		System.out.println("("+ parentID1 + ")->" + parent1 + "  |  " + child1.getGenes());
		System.out.println("("+ parentID2 + ")->" + parent2 + "  |  " + child2.getGenes());
		System.out.println("====================================");
	}
	
	public void Mutation(int parentID) {
		
		Vector parent = Chromosomes.elementAt(parentID).getGenes();
		Vector child = (Vector)parent.clone();
		int jump1, jump2;
		jump1 = Rndm.nextInt(child.size());
		jump2 = Rndm.nextInt(child.size());
		while (jump2 == jump1)
			jump2 = Rndm.nextInt(child.size());
		Collections.swap(child, jump1, jump2);
		Chromosome chromosome = new Chromosome();
		chromosome.setGenes(child);
		AjouterChromosome(chromosome);
//		System.out.println("("+ parentID + ")->" + parent + "  |  " + child);
	}
	
	public void Remplacement() {
		
		int i = 0;
		int j, max;
		
		while (i<Chromosomes.size()) {
			j = i;
			max = i;
			while (j<Chromosomes.size()) {
				
				if ((float)Chromosomes.elementAt(max).getFitness()
						<(float)Chromosomes.elementAt(j).getFitness()) {
					max = j;
				}
//				System.out.println(i+"->max("+Chromosomes.elementAt(max).getFitness()+", "+
//				Chromosomes.elementAt(j).getFitness()+")");
				j++;
			}
			Chromosome chromosome = Chromosomes.elementAt(max);
			Chromosomes.remove(max);
			Chromosomes.add(i, chromosome);
			i++;
		}
		
		Chromosomes.setSize(taillePopulation);
		
		Fitness = Chromosomes.elementAt(0).getFitness();
	}
}
