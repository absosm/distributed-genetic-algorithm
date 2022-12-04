package com.home;

import java.util.Vector;

import javax.swing.JOptionPane;


public class Rollete {
	
	private Vector Probabilite;
	
	public Rollete(Vector<Chromosome> Chromosomes) {
		
		float sommeFitness=0;
		
		for (int i=0; i<Chromosomes.size(); i++) {
			sommeFitness += Chromosomes.elementAt(i).getFitness();
		}
		Probabilite = new Vector();
		for (int i=0; i<Chromosomes.size(); i++) {
			float prob = Chromosomes.elementAt(i).getFitness()/sommeFitness;
			Probabilite.add(prob);
		}
	}
	
	public int getLuck() {
		
		
		float luck = Rndm.nextFloat();
		int chromosome_id = 0;
		float sumProp = (float)Probabilite.elementAt(chromosome_id);
		
		while (sumProp<luck) {
			chromosome_id++;
			sumProp += (float)Probabilite.elementAt(chromosome_id);
		}
		
		return chromosome_id;
	}
}
