package com.home;

import java.util.Random;

public class Rndm {
	
	private static Random random = null;
	
	public static int nextInt() {
		if (random == null)
			random = new Random();
		return random.nextInt();
	}
	
	public static int nextInt(int x) {
		if (random == null)
			random = new Random();
		return random.nextInt(x);
	}
	
	public static float nextFloat() {
		if (random == null)
			random = new Random();
		return random.nextFloat();
	}
}
