package day13;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import common.FileUtil;

public class Main {
	
	// --- Part 1 -------------------------------------------------------------
	// Part 1 is pretty straight-forward. Simply iterate through departure
	// times until one of them meets the criteria.
	
	public static void run(int arrival, int [] busIds) {
		int departure = -1;
		int busId = -1;
		
		int test = arrival;
		while (departure < 0) {
			
			for (int i=0; i<busIds.length; i++) {
				if (test % busIds[i] == 0) {
					departure = test;
					busId = busIds[i];
					break;
				}
			}
			
			test++;
		}
		
		System.out.println("Arrive at: " + arrival);
		System.out.println("Depart at: " + departure + " on bus " + busId);
		int result = (departure - arrival) * busId;
		System.out.println("Wait x busId = " + result);
	}
	
	public static void testPart1() {
		run(939, new int [] {7,13,59,31,19});
	}
	
	public static void solvePart1() throws Exception {
		List<String> input = FileUtil.readLinesFromFile(new File("files/day13/input.txt"));
		int arrival = Integer.parseInt(input.get(0));
		
		String [] parts = input.get(1).split(",");
		
		int [] busIds = new int[parts.length];
		int nextIndex = 0;
		for (int i=0; i<busIds.length; i++) {
			if (!parts[i].equals("x"))
				busIds[nextIndex++] = Integer.parseInt(parts[i]);
		}
		
		int [] busIdsFinal = new int[nextIndex];
		System.arraycopy(busIds, 0, busIdsFinal, 0, nextIndex);
		
		run(arrival, busIdsFinal);
	}
	
	// --- Part 2 -------------------------------------------------------------
	// Part 2 turned out to be a pain in the ass.  I started by building a set
	// of Test objects that could determine if t was valid and then iterate 
	// through all values of t to find the first valid value.  This was on track 
	// to take forever. I sped things up by arranging the tests in descending
	// order by busId so the biggest interval was first.  This hardly did anything
	// to improvement performance.
	//
	// While searching for methods for solving simulteneous modulo equations,
	// I came across references to the [Chinese Remainder Theorem](https://en.wikipedia.org/wiki/Chinese_remainder_theorem).
	// This allows you to solve a set of equations like the ones we have here
	// as long as the modulo values are all co-prime (which they are):
	//
	// x => -17 (mod 571)
	// x => -48 (mod 401)
	// x => -58 (mod 41)
	// x => -11 (mod 37)
	// x => -46 (mod 29)
	// x => -40 (mod 23)
	// x => -67 (mod 19)
	// x =>   0 (mod 17)
	// x => -35 (mod 13)
	//
	// The theorem allows you to combine 2 such equations:
	//
	// x => a(1)  (mod n(1))
	// x => a(2)  (mod n(2))
	//
	// To get a new equation:
	//
	// x => a(1,2)  (mod n(1)*n(2))
	//
	// A key step along the way involves using the [Extended Euclidean Algorithm](https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm)
	// to find the GCD of two numbers and to find the Bezout coefficients for those numbers.
	//
	// This was a pain, but I finally got it working - at least for all the test cases.
	// It still failed on my actual problem input.  I suspected this was due to overflow
	// errors (even though I was using long's).  I thus re-wrote part 2 (see: Main2.java)
	// using BigIntegers.  This did in fact solve the problem and give me a result that
	// I could verify was correct.  Wheww!!!
	//
	// P.S. After implementing my own version of the Chinese Remainder Theorem I
	// wondered how mad I'd be if that was already implemented somewhere that I 
	// could have just borrowed it from.  I found this awesome web page:
	//
	//  * https://rosettacode.org/wiki/Chinese_remainder_theorem
	//
	// It not only includes the algorithm description, it includes implementations in
	// dozens of languages (including Java and R).  I tried plugging in the R implementation
	// with my data and I got a nice "integer overflow" error.  That at least made me feel
	// a little bit better.
		
	static class Test {
		
		int busId;
		int tOffset;
		
		public Test(int busId, int tOffset) {
			this.busId = busId;
			this.tOffset = tOffset;
		}
		
		public boolean isValid(long t) {
			return (t + tOffset) % busId == 0;
		}
		
	}
	
	static class Tests {
		
		List<Test> tests = new ArrayList<>();
		
		public boolean isValid(long t) {
			for (Test test : tests) {
				if (!test.isValid(t))
					return false;
			}
			return true;
		}
		
		public void add(int busId, int tOffset) {
			tests.add(new Test(busId, tOffset));
			//System.out.println("(t + " + tOffset + ") % " + busId + " = 0");
		}
		
		public static Tests parseFrom(String text) {
			Tests tests = new Tests();
			
			String [] parts = text.split(",");
			for (int i=0; i<parts.length; i++) {
				if (!parts[i].equals("x")) {
					int busId = Integer.parseInt(parts[i]);
					tests.add(busId, i);
				}
			}
			return tests;
		}
		
	}
	
	/**
	 * This is the slow version that iterates through possible values
	 * of t.  This can solve some of the test inputs, but not the
	 * actual problem input.
	 */
	public static void runPart2(String input) {
		Tests tests = Tests.parseFrom(input);
		
		//sort so largest busId is first:
		tests.tests.sort(new Comparator<Test> () {
			public int compare(Test o1, Test o2) {
				return -Integer.compare(o1.busId, o2.busId);
			}
		});
		
		/*
		//step one t at a time:
		int t = 0;
		while (!tests.isValid(t))
			t++;
		*/
		
		//improvement to step by larger increment each time
		//(sorting Tests by busId enables this to be the fastest possible)
		int increment = tests.tests.get(0).busId;
		int offset = tests.tests.get(0).tOffset;
		
		int t = increment - offset;
		while (t < 0)
			t += increment;
		
		while (!tests.isValid(t))
			t += increment;
		
		System.out.println("t = " + (t));
	}
	
	/**
	 * Represents the congruence:
	 * 
	 * x = <value> (mod <modulo>)
	 * 
	 */
	static class Congruence {
		
		long modulo;
		long value;
		
		public Congruence(long modulo, long value) {
			this.modulo = modulo;
			this.value = value;
		}
		
		public Congruence combineWith(Congruence that) {
			ExtEuclideanResult result = ExtEuclideanResult.calculate(this.modulo, that.modulo);
			if (result.gcd != 1)
				throw new IllegalArgumentException("Modulos must be coprime");
			
			long value = this.value * that.modulo * result.coeffs[1] +
					     that.value * this.modulo * result.coeffs[0];
			
			long newModulo = this.modulo * that.modulo;
			//while (value <= 0)
			//	value += newModulo;
			
			return new Congruence(newModulo, value);
		}
		
		public Congruence simplify() {
			return new Congruence(modulo, value % modulo);
		}
		
		public String toString() {
			return "x = " + value + " (mod " + modulo + ")";
		}
		
	}
	
	/**
	 * Class that captures the results of the extended Euclidean algorithm (as 
	 * defined here: https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm).
	 * This returns the GCD of two numbers, a and b, as well as the Bezout 
	 * coefficients that allow us to express:
	 * 
	 * gcd(a,b) = a * coeffs[0] + b * coeffs[1]
	 */
	static class ExtEuclideanResult {
		
		long gcd;
		long [] coeffs;
		
		public ExtEuclideanResult(long gcd, long [] coeffs) {
			this.gcd = gcd;
			this.coeffs = coeffs;
		}
		
		public static ExtEuclideanResult calculate(long a, long b) {
			long [][] terms = new long[3][3];
			
			terms[1][0] = a;
			terms[1][1] = 1;
			terms[1][2] = 0;
			
			terms[2][0] = b;
			terms[2][1] = 0;
			terms[2][2] = 1;
			
			long [] tmp;
			long quotient;
			
			while (terms[2][0] != 0) {
								
				//shift arrays back
				//(we could zero out terms[2] but we will overwrite it anyway)
				tmp = terms[0];
				terms[0] = terms[1];
				terms[1] = terms[2];
				terms[2] = tmp;
				
				quotient = terms[0][0] / terms[1][0];	//integer division
				
				terms[2][0] = terms[0][0] - (quotient * terms[1][0]);
				terms[2][1] = terms[0][1] - (quotient * terms[1][1]);
				terms[2][2] = terms[0][2] - (quotient * terms[1][2]);
			}
			
			return new ExtEuclideanResult(terms[1][0], new long [] {
					terms[1][1], terms[1][2]
			});
		}
		
		public String toString() {
			return "gcd = " + gcd + ", coeffs = [" + coeffs[0] + ", " + coeffs[1] + "]";
		}
		
	}
	
	public static void runPart2Fast(String text) {
		System.out.println("INPUT: " + text);
		
		//parse input:
		List<Congruence> equations = new ArrayList<>();
		
		String [] parts = text.split(",");
		for (int i=0; i<parts.length; i++) {
			if (!parts[i].equals("x")) {
				int busId = Integer.parseInt(parts[i]);
				equations.add(new Congruence(busId, -i));
			}
		}
		
		/*
		//sort by biggest modulo
		equations.sort(new Comparator<Congruence> () {
			public int compare(Congruence o1, Congruence o2) {
				return -Long.compare(o1.modulo, o2.modulo);
			}
		});
		*/
		
		//flatten:
		Congruence result = equations.get(0);
		for (int i=1; i<equations.size(); i++)
			result = result.combineWith(equations.get(i)).simplify();
		
		
		System.out.println(result);
		long value = result.value % result.modulo;
		while (value < 0)
			value += result.modulo;
		System.out.println(value);
		
		Tests tests = Tests.parseFrom(text);
		System.out.println(tests.isValid(value));
	}
	
	public static void testPart2() {
		runPart2Fast("7,13,x,x,59,x,31,19");	//1068781
		runPart2Fast("17,x,13,19");	//3417
		
		runPart2Fast("67,7,59,61");// first occurs at timestamp 754018.
		runPart2Fast("67,x,7,59,61");// first occurs at timestamp 779210.
		runPart2Fast("67,7,x,59,61");//; first occurs at timestamp 1261476.
		runPart2Fast("1789,37,47,1889");// first occurs at timestamp 1202161486.
	}
	
	public static void solvePart2() throws Exception {
		List<String> input = FileUtil.readLinesFromFile(new File("files/day13/input.txt"));
		runPart2Fast(input.get(1));
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			testPart2();
			solvePart2();	//203240887943641 (wrong answer due to overflows)
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
