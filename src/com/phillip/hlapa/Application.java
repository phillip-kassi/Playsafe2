package com.phillip.hlapa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Application {
	
	private static Scanner read = new Scanner(System.in);
	private final static String PLAYERS = "players.txt";
	private static final String SPACE_SEPERATOR = " ";
	private static ArrayList<Player> players = new ArrayList<Application.Player>();
	private static long pastTime = 0;
	private static long time = 1000 * 30; //30 seconds
	
	public static void main(String[] args) {
		
		try {
			ArrayList<String> playersList = readPlayersFile();
			for(String player: playersList) {
				System.out.println(player);
			}
			pastTime = System.currentTimeMillis(); //start time
			while(thereIsStillTime()) {
				String playerDetails = read.nextLine();
				String inputDetails[] = playerDetails.split(SPACE_SEPERATOR);
				if(inputDetails.length == 3) {
					Player player = new Player();
					boolean saveBet = true;
					player.name = inputDetails[0].trim();
					player.bet = inputDetails[1].trim();
					try {
						player.betAmount = Double.parseDouble(inputDetails[2].trim());
					} catch (Exception e) {
						saveBet = false;
						System.err.println("Could not place bet... please enter a valid number (Bet Amount)");
					}
					if(playersList.contains(player.name) && saveBet)  {
						players.add(player);	
					}
					else if (saveBet){
						System.err.println("Could not place bet... player not known");
					}
						
				} else {
					System.err.println("Could not place bet... Please enter correct values");
				}
			} 
			if(timeIsUp()) {
				decideWinners(players);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static boolean timeIsUp() {
		return System.currentTimeMillis() -  pastTime > (time);
	}
	private static boolean thereIsStillTime() {
		return System.currentTimeMillis() -  pastTime < (time);
	}
	private static int getRandomNumber() {
		int rand = (int)(Math.random() * 36 + 1);
		if(rand > 0) 
			return rand;
		else 
			return getRandomNumber();
	}
	private static void decideWinners(ArrayList<Player> players) {
		int randomNumber = getRandomNumber();
		for(Player player: players) {
			if(player.bet.equals(String.valueOf(randomNumber))) {
				player.betAmount = player.betAmount * 36;
				player.win = "WIN";
			}
			else if(player.bet.equalsIgnoreCase("EVEN") && (randomNumber % 2 == 0)) {
				player.betAmount = player.betAmount * 2;
				player.win = "WIN";
			}
			else if(player.bet.equalsIgnoreCase("ODD") && !(randomNumber % 2 == 0)) {
				player.betAmount = player.betAmount * 2;
				player.win = "WIN";
			} else {
				player.betAmount = 0.0;
				player.win = "LOSE";
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\nNumber: " + randomNumber + "\n");
		sb.append("Player\tBet\tOutcome\tWinnings" );
		sb.append("\n---\n");
		for(Player currentPlayer: players) {
			sb.append(currentPlayer.name + "\t" + currentPlayer.bet + "\t" + currentPlayer.win + "\t" + currentPlayer.betAmount + "\n");
		}
		System.out.println(sb.toString());
	}


	public static ArrayList<String> readPlayersFile() throws IOException {
		ArrayList<String> playersList = new ArrayList<>();
		File file = new File(PLAYERS);
		if(!file.exists()) {
			file.createNewFile();
		}
		java.util.Scanner read = new java.util.Scanner(file);
		while(read.hasNext()) {
			playersList.add(read.nextLine().trim());
		}
		if(read != null) {
			read.close();
		}
		return playersList;
	}
	
	static class Player
	{
	    public String name; 
	    public String bet;  
	    public double  betAmount; 
	    public String win = "LOSE";
	 };
}
