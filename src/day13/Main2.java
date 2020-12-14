package day13;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import common.FileUtil;

/**
 * This is the BigInteger implementation of the Part2 logic in Main.java.
 * This was needed to avoid integer overflow errors (even though we were
 * using longs!)
 */
public class Main2 {
	
	/**
	 * Represents the congruence:
	 * 
	 * x = <value> (mod <modulo>)
	 * 
	 */
	static class Congruence {
		
		BigInteger modulo;
		BigInteger value;
		
		public Congruence(BigInteger modulo, BigInteger value) {
			this.modulo = modulo;
			this.value = value;
		}
		
		public Congruence combineWith(Congruence that) {
			ExtEuclideanResult result = ExtEuclideanResult.calculate(this.modulo, that.modulo);
			if (!result.gcd.equals(BigInteger.ONE))
				throw new IllegalArgumentException("Modulos must be coprime");
			
			BigInteger value = this.value.multiply(that.modulo).multiply(result.coeffs[1])
					           .add(
					        		that.value.multiply(this.modulo).multiply(result.coeffs[0])
					        	);
			
			BigInteger newModulo = this.modulo.multiply(that.modulo);
			//while (value <= 0)
			//	value += newModulo;
			
			return new Congruence(newModulo, value);
		}
		
		public Congruence simplify() {
			return new Congruence(modulo, value.mod(modulo));
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
		
		BigInteger gcd;
		BigInteger [] coeffs;
		
		public ExtEuclideanResult(BigInteger gcd, BigInteger [] coeffs) {
			this.gcd = gcd;
			this.coeffs = coeffs;
		}
		
		public static ExtEuclideanResult calculate(BigInteger a, BigInteger b) {
			BigInteger [][] terms = new BigInteger[3][3];
			
			terms[1][0] = a;
			terms[1][1] = BigInteger.ONE;
			terms[1][2] = BigInteger.ZERO;
			
			terms[2][0] = b;
			terms[2][1] = BigInteger.ZERO;
			terms[2][2] = BigInteger.ONE;
			
			BigInteger [] tmp;
			BigInteger quotient;
			
			while (!terms[2][0].equals(BigInteger.ZERO)) {
								
				//shift arrays back
				//(we could zero out terms[2] but we will overwrite it anyway)
				tmp = terms[0];
				terms[0] = terms[1];
				terms[1] = terms[2];
				terms[2] = tmp;
				
				quotient = terms[0][0].divide(terms[1][0]);	//integer division
				
				terms[2][0] = terms[0][0].subtract(quotient.multiply(terms[1][0]));
				terms[2][1] = terms[0][1].subtract(quotient.multiply(terms[1][1]));
				terms[2][2] = terms[0][2].subtract(quotient.multiply(terms[1][2]));
			}
			
			return new ExtEuclideanResult(terms[1][0], new BigInteger [] {
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
				equations.add(new Congruence(BigInteger.valueOf(busId), BigInteger.valueOf(-i)));
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
		BigInteger value = result.value.mod(result.modulo);
		while (value.signum() < 0)
			value = value.add(result.modulo);
		System.out.println(value);
		
		Main.Tests tests = Main.Tests.parseFrom(text);
		System.out.println(tests.isValid(value.longValue()));
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
			solvePart2();	//226845233210288 (CORRECT!)
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
