/*
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import edu.neu.coe.info6205.util.Benchmark_Timer;

/**
 * Weighted Quick Union with Path Compression
 */
public class WQU_DEPTH {
	public final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WQU_DEPTH(int n) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 0;
        }
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], height[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int count() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
    	validate(p);
        while (p != parent[p])
            p = parent[p];
        return p;
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
    
    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
    	int i = find(p);
        int j = find(q);
        if (i == j)
            return;

        // make shorter root point to taller one
        if      (height[i] < height[j]) parent[i] = j;
        else if (height[i] > height[j]) parent[j] = i;
        else {
            parent[j] = i;
            height[i]++;
        }
        count--;
    }

  //assignment4 
    public static WQU_DEPTH count(int n) {
    	WQU_DEPTH h = new WQU_DEPTH(n);
    	Random ran = new Random();
    	while (h.count > 1) {
    		int p = ran.nextInt(n);
            int q = ran.nextInt(n);
            if (!h.connected(p, q)) {
                h.union(p, q);
			}
		}
    	
    	return h;
	}
	
}
