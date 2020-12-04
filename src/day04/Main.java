package day04;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Main {
	
	static class Record {
		
		Map<String, String> map = new HashMap<>(10);
		
		public void set(String key, String value) {
			map.put(key, value);
		}
		public String get(String key) {
			return map.get(key);
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("{");
			
			boolean needsComma = false;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (needsComma)
					s.append(", ");
				else
					needsComma = true;
				
				s.append(entry.getKey() + "=" + entry.getValue());
			}
			
			s.append("}");
			return s.toString();
		}
		
	}
	
	static interface Validator {
		boolean isValid(Record record);
	}
	
	static Validator PART1_VALIDATOR = new Validator() {
		
		String [] REQUIRED_FIELDS = new String [] {
				"byr", // (Birth Year)
				"iyr", // (Issue Year)
				"eyr", // (Expiration Year)
				"hgt", // (Height)
				"hcl", // (Hair Color)
				"ecl", // (Eye Color)
				"pid"  // (Passport ID)
				//"cid"  // (Country ID)
		};
		
		public boolean isValid(Record record) {
			Set<String> keys = record.map.keySet();
			for (String field : REQUIRED_FIELDS) {
				if (!keys.contains(field))
					return false;
			}
			return true;
		}
		
	};
	
	static Validator PART2_VALIDATOR = new Validator() {
		
		Pattern PATTERN_YEAR = Pattern.compile("^\\d{4}$");
		Pattern PATTERN_HGT  = Pattern.compile("^\\d+(cm|in)$");
		Pattern PATTERN_HCL  = Pattern.compile("^#[0-9a-f]{6}$");
		Pattern PATTERN_ECL  = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");
		Pattern PATTERN_PID  = Pattern.compile("^\\d{9}$");
		
		//byr (Birth Year) - four digits; at least 1920 and at most 2002.
		protected boolean testByr(String byrText) {
			if (!PATTERN_YEAR.matcher(byrText).matches())
				return false;
			int byr = Integer.parseInt(byrText);
			if (byr < 1920 || byr > 2002)
				return false;
			
			return true;
		}
		
		//iyr (Issue Year) - four digits; at least 2010 and at most 2020.
		protected boolean testIyr(String iyrText) {
			if (!PATTERN_YEAR.matcher(iyrText).matches())
				return false;
			int iyr = Integer.parseInt(iyrText);
			if (iyr < 2010 || iyr > 2020)
				return false;
			return true;
		}
		
		//eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
		protected boolean testEyr(String eyrText) {
			if (!PATTERN_YEAR.matcher(eyrText).matches())
				return false;
			int eyr = Integer.parseInt(eyrText);
			if (eyr < 2020 || eyr > 2030)
				return false;
			return true;
		}
		
		//hgt (Height) - a number followed by either cm or in:
		//If cm, the number must be at least 150 and at most 193.
		//If in, the number must be at least 59 and at most 76.
		protected boolean testHgt(String hgt) {
			if (!PATTERN_HGT.matcher(hgt).matches())
				return false;
			
			String hgtUnits = hgt.substring(hgt.length()-2,hgt.length());
			int hgtValue = Integer.parseInt(hgt.substring(0,hgt.length()-2));
			if (hgtUnits.equals("cm")) {
				if (hgtValue < 150 || hgtValue > 193)
					return false;
			} else if (hgtUnits.equals("in")) {
				if (hgtValue < 59 || hgtValue > 76)
					return false;
			}
			
			return true;
		}
		
		//hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
		protected boolean testHcl(String hcl) {
			return PATTERN_HCL.matcher(hcl).matches();
		}
		
		//ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
		protected boolean testEcl(String ecl) {
			return PATTERN_ECL.matcher(ecl).matches();
		}
		
		//pid (Passport ID) - a nine-digit number, including leading zeroes.
		protected boolean testPid(String pid) {
			return PATTERN_PID.matcher(pid).matches();
		}
		
		public boolean isValid(Record record) {
			//verify required fields exist:
			if (!PART1_VALIDATOR.isValid(record))
				return false;
			
			String byr = record.get("byr");
			if (!testByr(byr)) {
				System.out.println("Invalid byr: " + byr);
				return false;
			}
			
			String iyr = record.get("iyr");
			if (!testIyr(iyr)) {
				System.out.println("Invalid iyr: " + iyr);
				return false;
			}
			
			String eyr = record.get("eyr");
			if (!testEyr(eyr)) {
				System.out.println("Invalid eyr: " + eyr);
				return false;
			}

			String hgt = record.get("hgt");
			if (!testHgt(hgt)) {
				System.out.println("Invalid hgt: " + hgt);
				return false;
			}
			
			String hcl = record.get("hcl");
			if (!testHcl(hcl)) {
				System.out.println("Invalid hcl: " + hcl);
				return false;
			}
			
			String ecl = record.get("ecl");
			if (!testEcl(ecl)) {
				System.out.println("Invalid ecl: " + ecl);
				return false;
			}
			
			String pid = record.get("pid");
			if (!testPid(pid)) {
				System.out.println("Invalid pid: " + pid);
				return false;
			}
			
			//cid (Country ID) - ignored, missing or not.
			
			return true;
		}
		
	};
	
	static class RecordReader {
		
		BufferedReader in;
		
		public RecordReader(File file) throws IOException {
			this.in = new BufferedReader(new FileReader(file));
		}
		
		public void close() throws IOException {
			in.close();
		}
		
		public Record nextRecord() throws IOException {
			//find first non-empty line:
			String line;
			while((line = in.readLine()) != null) {
				if (!line.trim().isEmpty())
					break;
			}
			
			//check for EOF
			if (line == null)
				return null;
			
			Record record = new Record();
			
			do {
				//empty line indicates end of record:
				if (line.trim().isEmpty())
					break;
				
				//sample input:
				//iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
				String [] parts = line.split("\\s+");
				for (String part : parts) {
					String [] keyValue = part.split(":");
					record.set(keyValue[0], keyValue[1]);
				}
			} while ((line = in.readLine()) != null);
			
			return record;
		}
		
	}
	
	public static void runValidator(Validator validator) throws Exception {
		int totalCount = 0;
		int validCount = 0;
		
		RecordReader in = new RecordReader(new File("files/day04/input.txt"));
		try {
			Record record;
			while ((record = in.nextRecord()) != null) {
				
				//System.out.println(record.toString());
				//break;
				
				totalCount++;
				if (validator.isValid(record))
					validCount++;
			}
		}
		finally {
			in.close();
		}
		
		System.out.println(validCount + " out of " + totalCount + " records are valid");
	}
	
	public static void solvePart1() throws Exception {
		runValidator(PART1_VALIDATOR);
	}
	
	public static void solvePart2() throws Exception {
		runValidator(PART2_VALIDATOR);
	}
	
	public static void main(String [] args) {
		try {
			//solvePart1();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
