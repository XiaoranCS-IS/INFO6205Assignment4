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
public class WQUPC {
    private final int[] parent;   // parent[i] = parent of i
    private final int[] size;   // size[i] = size of subtree rooted at i
    private int count;  // number of components

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public WQUPC(int n) {
        count = n;
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], size[i]);
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
        int root = p;
        while (root != parent[root]) {
            root = parent[root];
        }
        while (p != root) {
            int newp = parent[p];
            parent[p] = root;
            p = newp;
        }
        return root;
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
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }

  //assignment4 
    public static WQUPC count(int n) {
    	WQUPC h = new WQUPC(n);
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
    
    public static void main(String[] args) {
		int[] num = {20000, 40000, 80000, 160000, 320000, 640000, 1280000, 2560000};
		for (int i = 0; i < num.length; i++) {
			int temp = num[i];
	    	Supplier<Integer> n1 = () -> temp;
			Consumer<Integer> fun1 = (t) -> {WQUPC_Simpler.count(t);};
			Benchmark_Timer<Integer> BenchmarkTimer1 = new Benchmark_Timer<Integer>("WQUPC_Simpler", fun1, null);
	    	final double result1 = BenchmarkTimer1.runFromSupplier(n1, 100);
	    	System.out.println("(WQUPC_Simpler:" + num[i] + "):" + result1);
	    	
			Supplier<Integer> n2 = () -> temp;
			Consumer<Integer> fun2 = (t) -> {WQUPC.count(t);};
			Benchmark_Timer<Integer> BenchmarkTimer2 = new Benchmark_Timer<Integer>("WQUPC", null, fun2, null);
	    	final double result2 = BenchmarkTimer2.runFromSupplier(n2, 100);
	    	System.out.println("(WQUPC:"  + num[i] + "):" + result2);
	    	
	    	Supplier<Integer> n3 = () -> temp;
			Consumer<Integer> fun3 = (t) -> {WQU_SIZE.count(t);};
			Benchmark_Timer<Integer> BenchmarkTimer3 = new Benchmark_Timer<Integer>("WQU_SIZE", null, fun3, null);
	    	final double result3 = BenchmarkTimer3.runFromSupplier(n3, 100);
	    	System.out.println("(WQU_SIZE:"  + num[i] + "):" + result3);
	    	
	    	Supplier<Integer> n4 = () -> temp;
			Consumer<Integer> fun4 = (t) -> {WQU_DEPTH.count(t);};
			Benchmark_Timer<Integer> BenchmarkTimer4 = new Benchmark_Timer<Integer>("WQU_DEPTH", null, fun4, null);
	    	final double result4 = BenchmarkTimer4.runFromSupplier(n4, 100);
	    	System.out.println("(WQU_DEPTH:"  + num[i] + "):" + result4);
	    	System.out.println();
		}
		
//		for (int i = 0; i < num.length; i++) {
//			int total = 0;
//			for (int j = 0; j < 100; j++) {
//				WQUPC_Simpler u = WQUPC_Simpler.count(num[i]);
//				int depthOfTree = 0;
//				for (int k = 0; k < u.parent.length; k++) {
//					int root = k;
//			        int tempDepth = 0;
//					while (root != u.parent[root]) {
//			            root = u.parent[root];
//			            tempDepth++;
//			        }
//					if (tempDepth > depthOfTree) {
//						depthOfTree = tempDepth;
//					}
//				}
//				total += depthOfTree;
//			}
//			System.out.println("WQUPC_Simpler Depth(n = " + num[i] + "): "  + total/100.00);
//		}
//		
//		for (int i = 0; i < num.length; i++) {
//			int total = 0;
//			for (int j = 0; j < 100; j++) {
//				WQUPC u = WQUPC.count(num[i]);
//				int depthOfTree = 0;
//				for (int k = 0; k < u.parent.length; k++) {
//					int root = k;
//			        int tempDepth = 0;
//					while (root != u.parent[root]) {
//			            root = u.parent[root];
//			            tempDepth++;
//			        }
//				
//					if (tempDepth > depthOfTree) {
//						depthOfTree = tempDepth;
//					}
//				}
//				total += depthOfTree;
//			}
//			System.out.println("WQUPC Depth(n = " + num[i] + "): "  + total/100.00);
//		}
//		
//		for (int i = 0; i < num.length; i++) {
//			int total = 0;
//			for (int j = 0; j < 100; j++) {
//				WQU_SIZE u = WQU_SIZE.count(num[i]);
//				int depthOfTree = 0;
//				for (int k = 0; k < u.parent.length; k++) {
//					int root = k;
//			        int tempDepth = 0;
//					while (root != u.parent[root]) {
//			            root = u.parent[root];
//			            tempDepth++;
//			        }
//				
//					if (tempDepth > depthOfTree) {
//						depthOfTree = tempDepth;
//					}
//				}
//				total += depthOfTree;
//			}
//			System.out.println("WQU_SIZE Depth(n = " + num[i] + "): "  + total/100.00);
//		}
//		
//		for (int i = 0; i < num.length; i++) {
//			int total = 0;
//			for (int j = 0; j < 100; j++) {
//				WQU_DEPTH u = WQU_DEPTH.count(num[i]);
//				int depthOfTree = 0;
//				for (int k = 0; k < u.parent.length; k++) {
//					int root = k;
//			        int tempDepth = 0;
//					while (root != u.parent[root]) {
//			            root = u.parent[root];
//			            tempDepth++;
//			        }
//				
//					if (tempDepth > depthOfTree) {
//						depthOfTree = tempDepth;
//					}
//				}
//				total += depthOfTree;
//			}
//			System.out.println("WQU_DEPTH Depth(n = " + num[i] + "): "  + total/100.00);
//		}
    		
	}
		
}
