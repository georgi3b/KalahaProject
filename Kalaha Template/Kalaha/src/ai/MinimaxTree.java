/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

/**
 *
 * @author Georgiana
 */
import java.util.ArrayList;
import java.util.LinkedList;
import kalaha.GameState;

/**
 *
 * @author Georgiana
 */
public class MinimaxTree {
    private Node root;
    private GameState state;
    private int depth;
    private int player;
    boolean turn;

    public MinimaxTree(GameState state, int player, boolean turn) {
	
	this.state = state;
	this.player = player;
	this.turn = turn;
        buildTreeBFS(this.state);

	}
	public int findBestMove() {
		int score = -10000;
		int bestScore = -10000;
    		int choice = 1, bestC = 1;
		
                if (this.state.gameEnded()){
                    System.out.println("Returning root.");
                    return root.getChoice();
                }
               
		for (int j = 1; j < 7; j++) {
			GameState currState = this.state.clone();
			if (currState.moveIsPossible(j)) {
				currState.makeMove(j);	
				score = Math.max(bestScore,alphaBetaMinimax(new Node(currState,1,choice), true, 1, 5,Integer.MIN_VALUE,Integer.MAX_VALUE));
				if (score > bestScore) {
					bestScore = score;
					bestC = choice;
				}
				
			}
                        choice++;
			}
                                
                if (score > bestScore) {
				bestScore = score;
				bestC = choice;
		}
                 
                return bestC;
                
               

	}
	

	private void buildTreeBFS(GameState state) {
		int level = 0;
		root = new Node(state, level, 0);
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(root);
                /*
		int max_depth = 4;
		for (int i = 0; i < max_depth; i++) {

			level++;

			LinkedList<Node> nextLevel = new LinkedList<>();

			for (Node current : queue) {
				// simulates all the possible moves on this node
				for (int j = 1; j < 7; j++) {
					GameState currState = current.getState().clone();
					if (currState.moveIsPossible(j)) {
						currState.makeMove(j);
						Node childI = new Node(currState, level, 0);
//						childI.getParent() = current;
						current.getChildren().add(childI);
						nextLevel.add(childI);
					}
				}
			}

			queue = nextLevel;

		}*/
	
	}

	/**
	 * Evaluation function on a node; returns who is winning in the current board.
	 * @param n
	 * @return
	 */
	private int evaluateNode(Node n) {
		// +5 is Max wins, -5 if Min wins
	//	System.out.println("Node evaluated: " + n.toString());
		if (player == 1) {
			if (n.getScoreS()> n.getScoreN()) {
				return n.getScoreS();
			} else return n.getScoreN();
		} else if (player == 2) {
			if (n.getScoreN()>n.getScoreS()) {
				return n.getScoreN();
			} else return n.getScoreS();
		}
		return 0;
		
	}
	
	      
        private int alphaBetaMinimax(Node n, boolean turn, int depth, int maxDepth, int alpha, int beta) {
            		
                        
		if (depth >= maxDepth || n.getState().gameEnded()) {
			System.out.println(depth);
			return evaluateNode(n);
			
		}
		
		//Maximising player
		if (turn) {
			int max = -10000;
			//depth first search
			System.out.println("Max turn");
			int newD = ++depth;
			//check all the possible moves
                        GameState end = n.getState().clone();
			for (int j = 1; j < 7; j++) {
				GameState currState = n.getState().clone();
				if (currState.moveIsPossible(j)) {
					currState.makeMove(j);	
                                        if (j == 6){
                                            end.makeMove(j);
                                        }
					if(currState.getNextPlayer()!=player) {
						turn = false;
					}
					if (!currState.gameEnded()){
					max = Math.max(max, alphaBetaMinimax(new Node(currState,newD,j),turn,newD,maxDepth,alpha,beta));
                                        alpha = Math.max(alpha, max);
                                        if(beta <= alpha)
                                        break;
      
                                        } else {
                                            max = evaluateNode(new Node(currState,newD,j));
                                        }
                                }       
			}
                        /*
                        if (!end.gameEnded()){
                            max = Math.max(max, betterMinimax(new Node(end,newD,6),turn,newD,maxDepth));
                                        } else {
                                            max = evaluateNode(new Node(end,newD,6));
                                        
                        }
                        */
                        
			System.out.println("This is max:"+max);
			return max;
			
		}
		else {
			int min = 10000;
			System.out.println("Min turn");
			int newD = ++depth;
                   
                         GameState end = n.getState().clone();
			//check all the possible moves
                        for (int j = 1; j < 7; j++) {
				GameState currState = n.getState().clone();
				if (currState.moveIsPossible(j)) {
					currState.makeMove(j);
					 if (j == 6){
                                            end.makeMove(j);
                                        }
					if(currState.getNextPlayer()==player) {
						turn = true;
					}
					if (!currState.gameEnded()){
					min = Math.min(min, alphaBetaMinimax(new Node(currState,newD,j),turn,newD,maxDepth, alpha, beta));
                                        beta= Math.min(beta, min);
                                        if (beta >= alpha)
                                                break;
                                        } else 
                                            min = evaluateNode(new Node(currState,newD,j));
					
				}
				
			}
                        /*
                        if (!end.gameEnded()){
                            min = Math.min(min, betterMinimax(new Node(end,newD,6),turn,newD,maxDepth));
                                        } else {
                                            min = evaluateNode(new Node(end,newD,6));
                                        
                        }
                        */
                        System.out.println("This is min:" +min);
			return min;
		}
		
	}
}

class Node{
        private int level;
        private int scoreS, scoreN;
        private int choice;
        private int value;
        private GameState state;
        private Node parent;
        private ArrayList<Node> children; 
        
        public Node(GameState state, int level, int choice){
            this.state=state;
            this.level=level;
            this.choice=choice;
            this.parent = null;
            this.children = new ArrayList<>();
            
            String s=state.toString();
            String []tokens=s.split(";");
            scoreN=Integer.parseInt(tokens[0]);
            scoreS=Integer.parseInt(tokens[7]);
        }
        
		
		public int getScoreS() {
			return scoreS;
		}
        
		public int getScoreN() {
			return scoreN;
		}
		
		public GameState getState() {
			return state;
		}
		
		public void setState(GameState gs) {
			this.state = gs;
		}
		
		public ArrayList<Node> getChildren() {
			return this.children;
		}
		
		public Node getParent() {
			return this.parent;
		}
		
		public int getLevel() {
			return level;
		}
                
                public int getChoice(){
                    return choice;
                }
		
		public void setValue(int val) {
			this.value = val;
		}
           
		public String toString() {
			return this.getState() + ";Level: " + level;
		}
    }
