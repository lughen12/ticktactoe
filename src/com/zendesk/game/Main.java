package com.zendesk.game;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter game dimension:");
		int N = Integer.valueOf(scanner.nextLine());

		System.out.println("Enter name for Player 1:");
		String player1 = scanner.nextLine();
		System.out.println("Enter name for Player 2:");
		String player2 = scanner.nextLine();
		
		Game game = new Game(N, player1, player2);
		
		while (!game.isEnded()) {
			game.printBoard();
			game.printInstruction();
			game.placeSymbol(scanner.nextInt());
		}

		game.printBoard();
		game.printInstruction();
		
		scanner.close();
	}
}
