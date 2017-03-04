package com.zendesk.game;

public class Game {
	
	enum GameState {
		STATE_ON_GOING, STATE_ENDED
	}
	
	// Tick Tak Toe board
	private Board board;
	// Names of 2 players
	private String[] players;

	// Winner index (0 or 1). Use this variable to store result because the operation to check winner is costly
	private int winnerIndex;
	private GameState state;
	
	public Game(int N, String player1, String player2) {
		this.board = new Board(N);
		players = new String[]{player1, player2};
		
		state = GameState.STATE_ON_GOING;
		winnerIndex = -1;
	}
	
	/**
	 * Check if game is ended (Can be draw or someone is alrdy won)
	 * @return
	 */
	public boolean isEnded() {
		return state == GameState.STATE_ENDED;
	}
	
	/**
	 * Print the board in nice format
	 */
	public void printBoard() {
		board.printBoard();
	}

	/**
	 * Print instruction for players
	 */
	public void printInstruction() {
		if (state == GameState.STATE_ON_GOING) {
			System.out.println(players[board.getPlayerInTurn()] + ", choose a box to place an '" + board.getPlayerInTurnSignature() + "' into:");
			return;
		}
		
		if (winnerIndex != -1) {
			System.out.println("Congratulations " + players[winnerIndex] + "! You have won.");
		} else {
			System.out.println("Game ended, it is a draw.");
		}
	}

	public void placeSymbol(int cellIndex) {
		try {
			winnerIndex = board.placeSymbol(cellIndex);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
		if (board.isFull() || winnerIndex != -1) {
			state = GameState.STATE_ENDED;
		}
	}
}
