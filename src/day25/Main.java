package day25;

public class Main {
	
	static class CryptoObject {
		
		int loopSize;
		
		public CryptoObject(int loopSize) {
			this.loopSize = loopSize;
		}
		
		public long transform(long value) {
			long result = 1;
			for (int i=0; i<loopSize; i++) 
				result = (result * value) % 20201227;
			return result;
		}
		
		public static CryptoObject fromPublicKey(long publicKey) {
			long value = 7;
			long result = 1;
			int loopSize = 0;
			
			while(result != publicKey) {
				result = (result * value) % 20201227;
				loopSize++;
			}
			
			return new CryptoObject(loopSize);
		}
		
	}
	
	public static void testPart1() {
		CryptoObject obj = new CryptoObject(8);
		System.out.println(obj.transform(7));
		
		CryptoObject card = CryptoObject.fromPublicKey(5764801);
		System.out.println("Card loop size: " + card.loopSize);
		
		CryptoObject door = CryptoObject.fromPublicKey(17807724);
		System.out.println("Door loop size: " + door.loopSize);
		
		System.out.println(door.transform(5764801));
		System.out.println(card.transform(17807724));
	}
	
	public static void solvePart1() throws Exception {
		long publicKeyCard = 10212254;
		long publicKeyDoor = 12577395;
		
		CryptoObject card = CryptoObject.fromPublicKey(publicKeyCard);
		System.out.println("Card loop size: " + card.loopSize);
		
		CryptoObject door = CryptoObject.fromPublicKey(publicKeyDoor);
		System.out.println("Door loop size: " + door.loopSize);
		
		System.out.println("Encryption key: " + door.transform(publicKeyCard));
		System.out.println("Verified: " + card.transform(publicKeyDoor));
	}
	
	public static void solvePart2() throws Exception {
		//no part 2
	}
	
	public static void main(String [] args) {
		try {
			testPart1();
			solvePart1();
			//solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
