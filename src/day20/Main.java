package day20;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	static class PuzzlePiece {
		
		int id;
		int [][] values;
		int size;
		
		public PuzzlePiece(int id, int [][] values) {
			this.id = id;
			this.values = values;
			this.size = values.length;
		}
		
		public int getPositiveValueCount() {
			int count = 0;
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					if (values[i][j] > 0)
						count++;
				}
			}
			return count;
		}
		
	}
	
	static class OrientedPuzzlePiece {
		
		PuzzlePiece piece;
		int orientation = 0;
		
		public OrientedPuzzlePiece(PuzzlePiece piece, int orientation) {
			this.piece = piece;
			this.orientation = orientation;
		}
		
		public int getPieceId() {
			return piece.id;
		}
		public int size() {
			return piece.size;
		}
		
		/**
		 * Get the value at coordinate (i,j) using the current orientation:
		 */
		public int get(int i, int j) {
			switch(orientation) {
			case 0: return piece.values[i][j];
			case 1: return piece.values[j][i];
			case 2: return piece.values[i][piece.size-j-1];
			case 3: return piece.values[j][piece.size-i-1];
			case 4: return piece.values[piece.size-i-1][j];
			case 5: return piece.values[piece.size-j-1][i];
			case 6: return piece.values[piece.size-i-1][piece.size-j-1];
			case 7: return piece.values[piece.size-j-1][piece.size-i-1];
			default:
				throw new IllegalStateException("Invalid orientation");
			}
		}
		
		public int getTop(int x) {
			return get(0, x);
		}
		public int getBottom(int x) {
			return get(piece.size-1, x);
		}
		public int getLeft(int x) {
			return get(x, 0);
		}
		public int getRight(int x) {
			return get(x, piece.size-1);
		}
		
	}
	
	static class Puzzle {
		
		OrientedPuzzlePiece [][] pieces;
		int size;		//NOTE: I'm assuming puzzle is square
		
		public Puzzle(int size) {
			this.pieces = new OrientedPuzzlePiece[size][size];
			this.size = size;
		}
		
		public void set(int i, int j, OrientedPuzzlePiece piece) {
			this.pieces[i][j] = piece;
		}
		
		public boolean fits(int i, int j, OrientedPuzzlePiece piece) {
			//check piece above:
			if (hasPiece(i-1, j)) {
				OrientedPuzzlePiece above = pieces[i-1][j];
				for (int x=0; x<piece.size(); x++) {
					if (above.getBottom(x) != piece.getTop(x))
						return false;
				}
			}
			
			//check piece below:
			if (hasPiece(i+1, j)) {
				OrientedPuzzlePiece below = pieces[i+1][j];
				for (int x=0; x<piece.size(); x++) {
					if (below.getTop(x) != piece.getBottom(x))
						return false;
				}
			}
			
			//check piece to left:
			if (hasPiece(i, j-1)) {
				OrientedPuzzlePiece left = pieces[i][j-1];
				for (int x=0; x<piece.size(); x++) {
					if (left.getRight(x) != piece.getLeft(x))
						return false;
				}
			}
			
			//check piece to right:
			if (hasPiece(i, j+1)) {
				OrientedPuzzlePiece right = pieces[i][j+1];
				for (int x=0; x<piece.size(); x++) {
					if (right.getLeft(x) != piece.getRight(x))
						return false;
				}
			}
			
			return true;
		}
		
		protected boolean hasPiece(int i, int j) {
			return (i >= 0 && i < size && j>= 0 && j < size) && (pieces[i][j] != null);
		}
		
		public boolean solvePuzzle(List<PuzzlePiece> pieces) {	
			return addPieceRecursive(pieces);
		}
		
		public boolean addPieceRecursive(List<PuzzlePiece> pieceList) {
			
			//get next open location:
			int i = 0;
			int j = 0;
			while (pieces[i][j] != null) {
				//System.out.println("(" + i + "," + j + ") is full");
				j++;
				if (j >= size) {
					j = 0;
					i++;
					if (i >= size) {
						System.out.println("Puzzle filled before pieces ran out");
						printFinalInfo();
						return true;
					}
				}
			}
			
			//try all pieces in all orientations:
			for (PuzzlePiece piece : pieceList) {
				for (int orientation=0; orientation<8; orientation++) {
					OrientedPuzzlePiece oPiece = new OrientedPuzzlePiece(piece, orientation);
					if (this.fits(i, j, oPiece)) {
						//System.out.println("Setting (" + i + "," + j + ") = " + piece.id + " (orientation = " + orientation + ")");
						this.set(i, j, oPiece);
						
						if (pieceList.size() == 1) {
							printFinalInfo();
							return true;
						}
						else {
							
							ArrayList<PuzzlePiece> remainingPieces = new ArrayList<>(pieceList);
							remainingPieces.remove(piece);
							boolean solutionFound = addPieceRecursive(remainingPieces);
							if (solutionFound)
								return true;
							
						}
						
						this.set(i, j, null);
					}
				}
			}
			
			return false;
		}
		
		protected void printFinalInfo() {
			int [] corners = new int[4];
			corners[0] = pieces[0][0].getPieceId();
			corners[1] = pieces[0][size-1].getPieceId();
			corners[2] = pieces[size-1][0].getPieceId();
			corners[3] = pieces[size-1][size-1].getPieceId();
			
			long product = 1;
			
			System.out.println("Corners: ");
			for (int i=0; i<4; i++) {
				System.out.println(corners[i]);
				product *= corners[i];
			}
			
			System.out.println("Product = " + product);
		}
		
		public PuzzlePiece getFinalImage() {
			int inputPieceSize = pieces[0][0].size();
			int outputPieceSize = inputPieceSize - 2;
			int imageSize = this.size * outputPieceSize;
			
			int [][] values = new int[imageSize][imageSize];
			
			for (int iPiece=0; iPiece<size; iPiece++) {
				for (int jPiece=0; jPiece<size; jPiece++) {
				
					OrientedPuzzlePiece piece = pieces[iPiece][jPiece];
					
					for (int i=0; i<outputPieceSize; i++) {
						for (int j=0; j<outputPieceSize; j++) {
							values[iPiece*outputPieceSize+i][jPiece*outputPieceSize+j] = piece.get(i+1, j+1);
						}
					}
					
				}
			}
			
			return new PuzzlePiece(0, values);
		}
		
	}
	
	static class Input {
		
		List<PuzzlePiece> pieces = new ArrayList<>();
		
		public static Input readFromFile(File file) throws Exception {
			Input input = new Input();
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			try {
				String line;
				
				String puzzlePieceId = null;
				List<String> lines = new ArrayList<>();
				
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty() && puzzlePieceId != null) {
						input.pieces.add(toPuzzlePiece(puzzlePieceId, lines));
						puzzlePieceId = null;
						lines.clear();
					} else {
						if (puzzlePieceId == null)
							puzzlePieceId = line;
						else
							lines.add(line);
					}
				}
				
				if (puzzlePieceId != null)
					input.pieces.add(toPuzzlePiece(puzzlePieceId, lines));
			}
			finally {
				in.close();
			}
			
			return input;
		}
		
		protected static PuzzlePiece toPuzzlePiece(String idLine, List<String> lines) {
			//example: "Tile 2311:"
			int id = Integer.parseInt(idLine.substring("Tile ".length(), idLine.length()-1));
			
			int [][] values = new int[lines.size()][];
			for (int i=0; i<values.length; i++) {
				String line = lines.get(i);
				char [] chars = line.toCharArray();
				values[i] = new int[chars.length];
				for (int j=0; j<chars.length; j++)
					values[i][j] = chars[j] == '#' ? 1 : 0;
			}
			
			return new PuzzlePiece(id, values);
		}
		
	}
	
	public static void testPart1() throws Exception {
		Input input = Input.readFromFile(new File("files/day20/test.txt"));
		int pieceCount = input.pieces.size();
		int puzzleSize = (int)Math.sqrt(pieceCount);
		Puzzle puzzle = new Puzzle(puzzleSize);
		puzzle.solvePuzzle(input.pieces);
	}
	
	public static void solvePart1() throws Exception {
		Input input = Input.readFromFile(new File("files/day20/input.txt"));
		int pieceCount = input.pieces.size();
		int puzzleSize = (int)Math.sqrt(pieceCount);
		Puzzle puzzle = new Puzzle(puzzleSize);
		puzzle.solvePuzzle(input.pieces);
	}
	
	static class Pattern {
		
		int [][] values;
		int height;
		int width;

		public Pattern(int [][] values) {
			this.values = values;
			this.height = values.length;
			this.width = values[0].length;
		}
		
		public static Pattern parseFrom(List<String> lines) {
			int [][] values = new int[lines.size()][];
			
			for (int i=0; i<lines.size(); i++) {
				char [] chars = lines.get(i).toCharArray();
				values[i] = new int[chars.length];
				for (int j=0; j<chars.length; j++)
					values[i][j] = chars[j] == '#' ? 1 : 0;
			}
			
			return new Pattern(values);
		}
		
		/**
		 * Checks to see if this pattern matches the given image beginning at
		 * iOffset, jOffset.
		 */
		public boolean matches(OrientedPuzzlePiece piece, int iOffset, int jOffset) {
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (values[i][j] > 0 && piece.get(iOffset+i, jOffset+j) == 0)
						return false;
				}
			}
			
			return true;
		}
		
		public void findAllMatches(OrientedPuzzlePiece piece) {
			int matchCount = 0;
			
			for (int iOffset=0; iOffset<=piece.size()-this.height; iOffset++) {
				for (int jOffset=0; jOffset<=piece.size()-this.width; jOffset++) {
					if (this.matches(piece, iOffset, jOffset))
						matchCount++;
				}
			}
			
			System.out.println("Matches = " + matchCount);
			if (matchCount > 0) {
				//assuming patterns do not overlap we can calculate a score easily:
				int score = piece.piece.getPositiveValueCount() - (matchCount * this.getPositiveValueCount());
				System.out.println("Score = " + score);
			}
		}
		
		public void findAllMatches(PuzzlePiece piece) {
			for (int orientation=0; orientation<8; orientation++) {
				System.out.println("Orientation: " + orientation);
				OrientedPuzzlePiece oPiece = new OrientedPuzzlePiece(piece, orientation);
				findAllMatches(oPiece);
				System.out.println();
			}
		}
		
		public int getPositiveValueCount() {
			int count = 0;
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (values[i][j] > 0)
						count++;
				}
			}
			return count;
		}
		
	}
	
	public static void findSeaMonsters(PuzzlePiece image) {
		List<String> lines = new ArrayList<>();
		lines.add("                  # ");
		lines.add("#    ##    ##    ###");
		lines.add(" #  #  #  #  #  #   ");
        
		Pattern pattern = Pattern.parseFrom(lines);
		pattern.findAllMatches(image);
	}
	
	public static void testPart2() throws Exception {
		Input input = Input.readFromFile(new File("files/day20/test.txt"));
		int pieceCount = input.pieces.size();
		int puzzleSize = (int)Math.sqrt(pieceCount);
		Puzzle puzzle = new Puzzle(puzzleSize);
		puzzle.solvePuzzle(input.pieces);
		
		PuzzlePiece image = puzzle.getFinalImage();
		findSeaMonsters(image);
	}
	
	public static void solvePart2() throws Exception {
		Input input = Input.readFromFile(new File("files/day20/input.txt"));
		int pieceCount = input.pieces.size();
		int puzzleSize = (int)Math.sqrt(pieceCount);
		Puzzle puzzle = new Puzzle(puzzleSize);
		puzzle.solvePuzzle(input.pieces);
		
		PuzzlePiece image = puzzle.getFinalImage();
		findSeaMonsters(image);
	}
	
	public static void main(String [] args) {
		try {
			//testPart1();
			//solvePart1();
			//testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
