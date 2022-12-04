package com.home;

import java.awt.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int split1 = 1, split2 = 5;
		
		Vector parent1 = new Vector<>();
		Vector parent2 = new Vector<>();
		
		parent1.add(2);
		parent1.add(3);
		parent1.add(5);
		parent1.add(1);
		parent1.add(6);
		parent1.add(8);
		parent1.add(4);
		parent1.add(7);
		
		parent2.add(6);
		parent2.add(8);
		parent2.add(7);
		parent2.add(3);
		parent2.add(2);
		parent2.add(1);
		parent2.add(4);
		parent2.add(5);
		
		Vector child1 = new Vector();
		Vector child2 = new Vector();
		
		for (int k=0; k<parent1.size(); k++) {
			
			if ((k>split1) && (k<split2)) {
				child1.add(k, parent1.elementAt(k));
				child2.add(k, parent2.elementAt(k));
			}else {
				child1.add(k, -1);
				child2.add(k, -1);
			}
		}
		
		for (int k=0; k<parent1.size(); k++) {
			
			if ((k>split1) && (k<split2)) {
				
			}else {
				child1.set(k, parent2.elementAt(k));
				child2.set(k, parent1.elementAt(k));
			}
		}
		
		System.out.println("(parentID1)->" + parent1 + "  |  " + child1);
		System.out.println("(parentID2)->" + parent2 + "  |  " + child2);
		System.out.println("split1:"+split1+", split2:"+split2);
		System.out.println("====================================");
	}

}
