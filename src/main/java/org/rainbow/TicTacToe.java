//package org.rainbow;
//
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import org.eclipse.jetty.server.HttpConfiguration;
//import org.eclipse.jetty.server.HttpConnectionFactory;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.servlet.ServletContextHandler;
//import org.eclipse.jetty.servlet.ServletHolder;
//import org.rainbow.handlers.RecordServletV1;
//import org.rainbow.handlers.RecordServletV2;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class TicTacToe {
///*
//
//Implement a simulated Tic-tac-toe game that is played between two players
//on a 3 x 3 grid.
//
//You may assume the following rules:
//- There are two players that play against each other, X and O.
//- X will always go first.
//- X and O should make random, valid moves.
//
//After a player makes a move, the board state should be printed out like this:
//
//|X| | |
//| | | |
//| | | |
//
//|X| |O|
//| | | |
//| | | |
//
//If the game ends, the simulation should stop and print out the result.
//
//Possible results are: X Wins!, O Wins!, Draw
//
//*/
//
//
//
//    /*
//     * Click `Run` to execute the snippet below!
//     */
//
//import java.io.*;
//import java.util.*;
//
//    /*
//     * To execute Java, please define "static void main" on a class
//     * named Solution.
//     *
//     * If you need more classes, simply define them inline.
//     */
//
//    class Solution {
//
//        public static final int size = 3;
//        public static int[][] grid;
//        public static int spotAvailable = size *size;
//
//        public static boolean gameOver = false;
//
//        public static class Move {
//            public int x;
//            public int y;
//
//            public Move(int x, int y) {
//                this.x = x;
//                this.y = y;
//            }
//        }
//
//
//        public static void initGrid() {
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//                    grid[i][j] = -1;
//                }
//            }
//        }
//
//
//        public static boolean isValidMove(int type, Move m) {
//
//            if (grid[m.x][m.y] == -1) {
//                grid[m.x][m.y] = type;
//                spotAvailable--;
//                return true;
//            }
//            return false;
//        }
//
//        public static void hasWon() {
//
//        }
//
//        public static Move pickRandomSpot() {
//            //Move move = new Move(-1,-1);
//
//            Random rand = new Random();
//
//            int x = Math.abs(rand.nextInt()) % size;
//            int y = Math.abs(rand.nextInt()) % size;
//
//
//            Move move = new Move (x, y);
//            return move;
//        }
//
//        public static boolean checkForWin(int player) {
//
//            boolean hasWon = false;
//            // check rows
//
//            for (int i = 0; i < size; i++) {
//
//                if (grid[i][0] == player && grid[i][1] == player && grid[i][2] == player) {
//                    hasWon = true;
//                }
//
//                if (hasWon == true) {
//                    return hasWon;
//                }
//
//            }
//
//            // check columns
//            for (int i = 0; i < size; i++) {
//
//                if (grid[0][i] == player && grid[1][i] == player && grid[2][i] == player) {
//                    hasWon = true;
//                }
//
//                if (hasWon == true) {
//                    return hasWon;
//                }
//            }
//
//            // check diagnols
//
//            // left/rigth
//            if (grid[0][0] == player && grid[1][1] == player && grid[2][2] == player) {
//                hasWon = true;
//            }
//
//            if (grid[0][2] == player && grid[1][1] == player && grid[2][0] == player) {
//                hasWon = true;
//            }
//
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//
//                }
//            }
//
//
//            return hasWon;
//        }
//
//
//        public static void printGrid() {
//
//            for (int i = 0; i < size; i++) {
//                for (int j = 0; j < size; j++) {
//                    System.out.print("|" + (char)grid[i][j]);
//                }
//                System.out.print("|");
//                System.out.println();
//            }
//
//            System.out.println("==========================");
//
//        }
//
//        public static void main(String[] args) {
//
//            grid = new int[size][size];
//            initGrid();
//
//            int winner = -1;
//
//            printGrid();
//            int player = 'x';
//
//            while (!gameOver && spotAvailable > 0) {
//
//                Move m = pickRandomSpot();
//                // apply the move to the board
//                while (!isValidMove(player, m)) {
//                    m = pickRandomSpot();
//                }
//
//                // check for the win
//                boolean won = checkForWin(player);
//                if (won) {
//                    winner = player;
//                    System.out.println("Player " + (char)player + " has won this round!");
//                    break;
//                }
//
//                printGrid();
//
//                // otherwise we have had a valid move so switch players
//                if (player == 'x')
//                    player = 'o';
//                else
//                    player = 'x';
//            }
//
//            if (winner == -1) {
//                System.out.println("The game has ended in a draw!");
//            }
//
//            System.out.println("The game has ended");
//            printGrid();
//
//
//
//
//            // ArrayList<String> strings = new ArrayList<String>();
//            // strings.add("Hello, World!");
//            // strings.add("Welcome to CoderPad.");
//            // strings.add("This pad is running Java " + Runtime.version().feature());
//
//            // for (String string : strings) {
//            //   System.out.println(string);
//            // }
//        }
//    }
//
//
//}