package com.zendesk.game;

import java.util.Arrays;

/**
 * This class represent the tick tac toe game board
 * The board assume that there is only 2 players:
 * 		Player 0 with index 0 and sign 'x'
 * 		Player 1 with index 1 and sign '0'
 */
public class Board {
	
	private static final int EMPTY = -1;
	private static final int LENGTH_TO_WIN = 3;
	
	private static final char[] PLAYER_SIGNATURES = new char[]{'x', 'o'};
	
	// Dimension of the board. The board dimension will be N*N
	private int N;
	// The main board with 2D dimension, -1 is empty slot, 0 is Player 0 ('x'), 1 is Player 1 ('o')
	private int[][] board; 
	// Which player is in turn now, only take value 0 and 1
	private int playerInTurn; 
	// Empty cell count
	private int emptyCell;
	
	public Board(int N) {
		this.N = N;
		this.board = initBoard(N);
		this.emptyCell = N*N;
		this.playerInTurn = 0;
	}
	
	/**
	 * Place symbol of player who is in turn to the provided cell index, and switch turn immediately
	 * @param cellIndex
	 * @return The winner after cell placement, or -1 if there is no winner yet
	 */
	public int placeSymbol(int cellIndex) throws Exception {
		// Invalid cell
		if (cellIndex < 1 || cellIndex > N*N) {
			throw new Exception("Oops, invalid cell.");
		}
		
		int row = getRowFromIndex(cellIndex, N);
		int col = getColFromIndex(cellIndex, N);

		// Cell occupied
		if (board[row][col] != EMPTY) {
			throw new Exception("Oops, the cell is occupied.");
		}
		
		// Update board
		board[row][col] = playerInTurn;
		playerInTurn = (playerInTurn+1)%2;
		emptyCell--;

		return checkWinnerWithCell(cellIndex);
	}
	
	/**
	 * Print the current board state
	 */
	public void printBoard() {
		int maxDigitCount = getDigitCount(N*N);

		System.out.print("\n");
		
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				String cellContent = (col == 0) ? " " : " | ";
				cellContent += getCellContent(row, col, maxDigitCount);
				System.out.print(cellContent);
			}
			System.out.print(" \n");
			if (row < N-1) {
				printLine(N, maxDigitCount);
			}
		}
		System.out.print("\n");
	}
	
	public int getPlayerInTurn() {
		return playerInTurn;
	}
	
	public Character getPlayerInTurnSignature() {
		return PLAYER_SIGNATURES[playerInTurn];
	}
	
	public boolean isFull() {
		return emptyCell == 0;
	}
	
	/**
	 * Check who is the winner after a placement to cell index. The function only discover winning line contains cell index.
	 * @param cellIndex
	 * @return winner or -1 if there is no winner yet
	 */
	private int checkWinnerWithCell(int cellIndex) {
		int row = getRowFromIndex(cellIndex, N);
		int col = getColFromIndex(cellIndex, N);
		
		// Check if cell form a line in either row, col or diagonal
		if (checkRowContainCell(row, col) || checkColContainCell(row, col) || checkDiagonalContainCell(row, col)) {
			return board[row][col];
		}
		
		return -1;	
	}
	
	/**
	 * Check if the cell form any row with 3 similar symbols
	 * @param row
	 * @param col
	 * @return 
	 */
	private boolean checkRowContainCell(int row, int col) {
		int cellValue = board[row][col];
		int contiguosCount = 0;
		
		int lowerBound = Math.max(col-LENGTH_TO_WIN+1, 0);// Inclusive
		int upperBound = Math.min(col+LENGTH_TO_WIN, N);// Exclusive
		
		for (int i = lowerBound; i < upperBound && contiguosCount < LENGTH_TO_WIN; i++) {
			contiguosCount = (board[row][i] == cellValue) ? contiguosCount+1 : 0;
		}
		
		return contiguosCount == LENGTH_TO_WIN;
	}
	
	/**
	 * Check if the cell form any column with 3 similar symbols
	 * @param row
	 * @param col
	 * @return 
	 */
	private boolean checkColContainCell(int row, int col) {
		int cellValue = board[row][col];
		int contiguosCount = 0;
		
		int lowerBound = Math.max(row-LENGTH_TO_WIN+1, 0); // Inclusive
		int upperBound = Math.min(row+LENGTH_TO_WIN, N); // Exclusive
		
		for (int i = lowerBound; i < upperBound && contiguosCount < LENGTH_TO_WIN; i++) {
			contiguosCount = (board[i][col] == cellValue) ? contiguosCount+1 : 0;
		}
		
		return contiguosCount == LENGTH_TO_WIN;
	}
	
	/**
	 * Check if the cell form any diagonal line with 3 similar symbols
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean checkDiagonalContainCell(int row, int col) {
		int cellValue = board[row][col];
		
		// Check right diagonal
		for (int i = 0; i < LENGTH_TO_WIN; i++) {
			int fromRowIndex = row-i;
			int fromColIndex = col-i;

			int toRowIndex = fromRowIndex+LENGTH_TO_WIN;// Exclusive
			int toColIndex = fromColIndex+LENGTH_TO_WIN;// Exclusive
			
			if (fromRowIndex >= 0 && fromColIndex >= 0 && toRowIndex <= N && toColIndex <= N) {
				boolean isMatched = true;
				while (fromRowIndex < toRowIndex && fromColIndex < toColIndex && isMatched) {
					isMatched &= (board[fromRowIndex][fromColIndex] == cellValue);
					fromRowIndex++;
					fromColIndex++;
				}
				
				if (isMatched) { return true; }
			}
		}
		
		// Check left diagonal
		for (int i = 0; i < LENGTH_TO_WIN; i++) {
			int fromRowIndex = row-i;
			int fromColIndex = col+i;

			int toRowIndex = fromRowIndex+LENGTH_TO_WIN;
			int toColIndex = fromColIndex-LENGTH_TO_WIN;
			
			if (fromRowIndex >= 0 && toRowIndex <= N && toColIndex >= -1 && fromColIndex < N) {
				boolean isMatched = true;
				while (fromRowIndex < toRowIndex && fromColIndex > toColIndex && isMatched) {
					isMatched &= (board[fromRowIndex][fromColIndex] == cellValue);
					fromRowIndex++;
					fromColIndex--;
				}
				
				if (isMatched) { return true; }
			}
		}
		
		return false;
	}
	
	/**
	 * Get content of the cell, including sufficient white space. Content can be 'x', 'o' or cell index if cell is not occupied
	 * @param row
	 * @param col
	 * @param spaceCount
	 * @return
	 */
	private String getCellContent(int row, int col, int spaceCount) {
		int digitCount = 1;
		String cellValue = "";
		String precedeSpaces = "";
		
		if (board[row][col] >= 0) {
			cellValue = PLAYER_SIGNATURES[board[row][col]] + "";
		} else {
			int index = N*row+col+1;
			digitCount = getDigitCount(index);
			cellValue = index + "";
		}
		
		for (int i = 0; i < spaceCount-digitCount; i++) {
			precedeSpaces += " ";
		}
		
		return precedeSpaces + cellValue;
	}
	
	private int getRowFromIndex(int index, int N) {
		return (index-1)/N;
	}
	
	private int getColFromIndex(int index, int N) {
		return (index-1)%N;
	}
	
	private static int[][] initBoard(int N) {
		int[][] board = new int[N][N];
		for (int i = 0; i < N; i++) {
			Arrays.fill(board[i], EMPTY);
		}
		
		return board;
	}
	
	private static void printLine(int N, int maxDigitCount) {
		for (int i = 0; i < (maxDigitCount+3)*N-1; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
	}
	
	/**
	 * Get digit count from number
	 * @param number
	 * @return
	 */
	private static int getDigitCount(int number) {
		int digits = 1;
		number /= 10;
		
		while (number > 0) {
			digits++;
			number /= 10;
		}
		
		return digits;
	}
}
