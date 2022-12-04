package com.home;

import java.util.Vector;

public class Chromosome {
	
	private Vector Genes;
	private float Fitness;
	public static Vector Codage;
	private boolean evaluated = false;
	
	public Chromosome() {
		Genes = new Vector();
	}
	
	public Chromosome(Vector codeReel) {
		Genes = new Vector();
		for (int i=0; i<codeReel.size(); i++) {
			Genes.add(Chromosome.Codage.indexOf(codeReel.elementAt(i)));
		}
	}
	
	public Chromosome instance() {
		Chromosome c = new Chromosome();
		c.setGenes((Vector)Genes.clone());
		if (isEvaluated())
			c.setFitness(this.Fitness);
		return c;
	}
	
	public void addGene(int gene) {
		Genes.add(gene);
	}
	
	public void setGene(int pos, int gene) {
		Genes.setElementAt(gene, pos);
	}
	
	public Vector Decodage() {
		Vector vector = new Vector();
		for (int i=0; i<Genes.size(); i++) {
			vector.add(Codage.elementAt((int)Genes.elementAt(i)));
		}
		return vector;
	}

	public Vector getGenes() {
		return Genes;
	}
	
	public void setGenes(Vector Genes) {
		this.Genes = Genes;
	}

	public float getFitness() {
		return Fitness;
	}

	public void setFitness(float fitness) {
		this.evaluated = true;
		this.Fitness = fitness;
	}
	
	public boolean isEvaluated() {
		return this.evaluated;
	}
	
	public int getNombreGenes() {
		return Genes.size();
	}

	public static void setCodage(Vector codeReel) {
		Codage = codeReel;
	}
}
