package day22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
	
	static enum State {PLAYER1_WINS, PLAYER2_WINS, KEEP_PLAYING};
	
	static class Game {
		
		List<Integer> player1 = new ArrayList<>();
		List<Integer> player2 = new ArrayList<>();
		
		Set<String> priorHands = new HashSet<>();
		
		/**
		 * Play round according to rules from Part 1.  This returns
		 * TRUE if the game is still being played and false if
		 * the game is over and someone has won.
		 */
		public boolean playRound() {
			int card1 = player1.remove(0).intValue();
			int card2 = player2.remove(0).intValue();
			
			if (card1 > card2) {
				player1.add(card1);
				player1.add(card2);
			} else if (card2 > card1) {
				player2.add(card2);
				player2.add(card1);
			} else {
				throw new IllegalStateException("Tie");
			}
			
			return !(player1.isEmpty() || player2.isEmpty());
		}
		
		public long getWinningScore() {
			long score = 0;
			List<Integer> winner = player1.isEmpty() ? player2 : player1;
			for (int i=0; i<winner.size(); i++) {
				score += (i + 1) * winner.get(winner.size()-i-1).intValue();
			}
			return score;
		}
		
		/**
		 * Play a round of the game using the rules from Part 2.  This
		 * returns a State value to indicate if the game is still in
		 * progress or to indicate who won.  (This is necessary since
		 * we can't always infer from the cards who won.)
		 */
		public State playRound2() {
			//if we've seen this before, player1 wins:
			String hash = hashPlayerState();
			if (!priorHands.add(hash)) {
				return State.PLAYER1_WINS;
			}
			
			int card1 = player1.remove(0).intValue();
			int card2 = player2.remove(0).intValue();
			
			boolean playSubGame = (player1.size() >= card1 && player2.size() >= card2);
			if (playSubGame) {
				System.out.println("Playing subgame");
				
				Game subGame = new Game();
				subGame.player1.addAll(player1.subList(0, card1));
				subGame.player2.addAll(player2.subList(0, card2));
				
				State result;
				while ((result = subGame.playRound2()) == State.KEEP_PLAYING) {
					// do nothing
				}
				
				if (result == State.PLAYER1_WINS) {
					System.out.println("Player 1 wins subgame");
					player1.add(card1);
					player1.add(card2);
				} else if (result == State.PLAYER2_WINS) {
					System.out.println("Player 2 wins subgame");
					player2.add(card2);
					player2.add(card1);
				}
				
			} else {
				//normal game (like part 1):
				if (card1 > card2) {
					player1.add(card1);
					player1.add(card2);
				} else if (card2 > card1) {
					player2.add(card2);
					player2.add(card1);
				} else {
					throw new IllegalStateException("Tie");
				}
			}
			
			if (player1.isEmpty())
				return State.PLAYER2_WINS;
			else if (player2.isEmpty())
				return State.PLAYER1_WINS;
			else
				return State.KEEP_PLAYING;
		}
		
		/**
		 * Create a string that uniquely encodes the current state.
		 * This is just a comma-separated list of each players' cards
		 * joined together with a comma.
		 */
		protected String hashPlayerState() {
			StringBuilder s = new StringBuilder();
			for (int i=0; i<player1.size(); i++) {
				if (i > 0)
					s.append(",");
				s.append(player1.get(i));
			}
			s.append(":");
			for (int i=0; i<player2.size(); i++) {
				if (i > 0)
					s.append(",");
				s.append(player2.get(i));
			}
			return s.toString();
		}
		
		public static Game readFromFile(File file) throws IOException {
			Game game = new Game();
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			try {
				in.readLine();	//"Player 1:"
				
				String line;
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty())
						break;
					
					game.player1.add(Integer.parseInt(line));
				}
				
				in.readLine();	//"Player 2:"
				while ((line = in.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty())
						break;
					
					game.player2.add(Integer.parseInt(line));
				}
			}
			finally {
				in.close();
			}
			
			return game;
		}
		
		public void print(PrintStream out) {
			StringBuilder s = new StringBuilder();
			s.append("Player 1: ");
			for (int i=0; i<player1.size(); i++) {
				if (i > 0)
					s.append(", ");
				s.append(player1.get(i));
			}
			out.println(s.toString());
			
			s.setLength(0);
			s.append("Player 2: ");
			for (int i=0; i<player2.size(); i++) {
				if (i > 0)
					s.append(", ");
				s.append(player2.get(i));
			}
			out.println(s.toString());
		}
		
	}
	
	public static void solvePart1() throws Exception {
		Game game = Game.readFromFile(new File("files/day22/input.txt"));
		int count = 0;
		while (game.playRound())
			count++;
		
		System.out.println(count);
		System.out.println(game.getWinningScore());
	}
	
	public static void testPart2() throws Exception {
		Game game = Game.readFromFile(new File("files/day22/test.txt"));
		game.print(System.out);
		System.out.println();
		
		int count = 0;
		while (game.playRound2() == State.KEEP_PLAYING) {
			count++;
			
			System.out.println("After " + count + " rounds");
			game.print(System.out);
			System.out.println();
		}
		
		System.out.println(count);
		System.out.println(game.getWinningScore());
	}
	
	public static void solvePart2() throws Exception {
		Game game = Game.readFromFile(new File("files/day22/input.txt"));
		game.print(System.out);
		System.out.println();
		
		int count = 0;
		while (game.playRound2() == State.KEEP_PLAYING) {
			count++;
			
			System.out.println("After " + count + " rounds");
			game.print(System.out);
			System.out.println();
		}
		
		System.out.println(count);
		System.out.println(game.getWinningScore());
	}
	
	public static void main(String [] args) {
		try {
			//solvePart1();
			//testPart2();
			solvePart2();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
