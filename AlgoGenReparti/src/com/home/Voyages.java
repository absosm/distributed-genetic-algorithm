package com.home;

import java.util.Vector;

public class Voyages {
	
	private static Vector<Vector> Distances;
	private static Vector Villes;
	private static Object villeDepart;
	private static Vector voyTmp;
	
	public static void setVilleDepart(Object villeDepart) {
		Voyages.villeDepart = villeDepart;
	}

	public static Vector faireVoyage() {
		
		voyTmp = new Vector();
		int Count = Villes.size()-1;
		
		for (int i = 0; i < Count; i++) {
			int indice = Rndm.nextInt(Villes.size());
			Object ville = Villes.elementAt(indice);
			while (voyTmp.contains(ville) || villeDepart.equals(ville)) {
				indice = Rndm.nextInt(Villes.size());
				ville = Villes.elementAt(indice);
			}
			voyTmp.add(ville);
		}
		
		return voyTmp;
	}

	public static void setVilles(Vector villes) {
		Villes = villes;
	}
	
	public static float getDistance2Ville(Object depuis, Object vers) {
		Vector row = Voyages.Distances.elementAt(Voyages.Villes.indexOf(depuis)+1);
		return Float.parseFloat(row.elementAt(Voyages.Villes.indexOf(vers)+1).toString());
	}
	
	public static float getDistance() {
		
		float d = getDistance2Ville(villeDepart, voyTmp.elementAt(0));
		
		for (int i=0; i<voyTmp.size()-1; i++) {
			d = d + getDistance2Ville(voyTmp.elementAt(i), voyTmp.elementAt(i+1));
		}
		
		d = d + getDistance2Ville(voyTmp.elementAt(voyTmp.size()-1), villeDepart);
		
		return d;
	}
	
	public static float getDistance(Vector voyage) {
		
		float d = getDistance2Ville(villeDepart, voyage.elementAt(0));
		
		for (int i=0; i<voyage.size()-1; i++) {
			d = d + getDistance2Ville(voyage.elementAt(i), voyage.elementAt(i+1));
		}
		
		d = d + getDistance2Ville(voyage.elementAt(voyage.size()-1), villeDepart);
		
		return d;
	}

	public static void setDistances(Vector distances) {
		Distances = distances;
	}
}
