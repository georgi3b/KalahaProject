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
    int bestC;

    public MinimaxTree(GameState state, int player, boolean turn) {
	
	this.state = state;
	this.player = player;
	this.turn = turn;
        buildTreeBFS(this.state);

	}
    
    public int getMove(){
        return bestC;
    }
    
	public void findBestMove() {
		             
            int score = Integer.MIN_VALUE;
            int bestScore = score;
            int choice = 1;
           
		
            if (this.state.gameEnded()){
                System.out.println("Returning root.");
                return;
            }
            Node child; 
            for (int j = 1; j < 7; j++) {
		GameState currState = this.state.clone();
		if (currState.moveIsPossible(j)) {
			currState.makeMove(j);	
			child = new Node(currState,1,j);
                               
                        if (child.getState().getNextPlayer() == player){
                            turn = true;
                        } else {
                            turn = false;
                        }
                                
                        alphaBetaMinimax(child,turn,1,6,Integer.MIN_VALUE,Integer.MAX_VALUE);
                                                           
                        if (child.getValue() > bestScore) {   
                            System.out.println("Best score = "+ child.getValue());
                            bestScore = child.getValue();
                            bestC = choice;
			}
				
                }
                        
                choice++;
            }
                                              

	}
	

	private void buildTreeBFS(GameState state) {
		int level = 0;
		root = new Node(state, level, 0);
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(root);
	
	}

	/**
	 * Evaluation function on a node; returns who is winning in the current board.
	 * @param n
	 * @return
	 */
	private int evaluateNode(Node n) {
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
	
	private int betterMinimax(Node n, boolean turn, int depth, int maxDepth) {
		
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
					max = Math.max(max, betterMinimax(new Node(currState,newD,j),turn,newD,maxDepth));
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
                        
//			System.out.println(max);
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
					min = Math.min(min, betterMinimax(new Node(currState,newD,j),turn,newD,maxDepth));
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
                        
			return min;
		}
		
	}      
        
    private int alphaBetaMinimax(Node n, boolean turn, int depth, int maxDepth, int alpha, int beta) {
        
        if (depth >= maxDepth || n.getState().gameEnded()) {
         //   System.out.println(depth);
            int ev = evaluateNode(n);
            n.setValue(ev);
            return ev;
        }
        else
        //Maximising player
        if (turn) {
            //depth first search
            // System.out.println("Max turn");
            // System.out.println("chosen move "+ bestC);
            int newD = ++depth;
            int val = Integer.MIN_VALUE;
            //check all the possible moves
            for (int j = 1; j < 7; j++) {
                GameState currState = n.getState().clone();
                if (currState.moveIsPossible(j)) {
                    
                    currState.makeMove(j);
                    if (currState.getNextPlayer() != player) {
                        turn = false;
                    } else {
                        turn = true;
                    }
                    
                    
                    if (!currState.gameEnded()){
                    int childVal = alphaBetaMinimax(new Node(currState, newD, j), turn, newD, maxDepth, alpha, beta);
                    
                    if (childVal > val){
                        val = childVal;
                        n.setValue(val);
                    } 
                    if (val > alpha){
                       alpha = val;
                    }
                    
                    
                    //System.out.println("Value of minimax " + val);
                    //If the value for max in this branch is greater than the best path for min
                    //we don't look for further solutions for max.
                    if (val > beta){
                        //System.out.println("Pruned at depth " + depth);
                        n.setValue(val);
                        return val;
                    }   
                    }else {
                        val = evaluateNode(n);
                        n.setValue(val);
                    }
                    
                } 
            }
        } else {
          //  System.out.println("Min turn");
            int newD = ++depth;
            int val = Integer.MAX_VALUE;
            //check all the possible moves
            for (int j = 1; j < 7; j++) {
                GameState currState = n.getState().clone();
                if (currState.moveIsPossible(j)) {
                    currState.makeMove(j);

                    if (currState.getNextPlayer() == player) {
                        turn = true;
                    } else {
                        turn = false;
                    }
                    if (!currState.gameEnded()){
                    int childVal = alphaBetaMinimax(new Node(currState, newD, j), turn, newD, maxDepth, alpha, beta);
                    if (childVal < val){
                        val = childVal;
                        n.setValue(val);
                    } 
                    if (val < beta){
                        beta = val;
                    }
                    if (val < alpha){
                       // System.out.println("Pruned at depth " + depth);
                        n.setValue(val);
                        return val;
                    }
                    } else {
                        val = evaluateNode(n);
                        n.setValue(val);
                    }
                    
                    
                } 

            }

        }
        return turn ? alpha : beta;

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

    int getValue() {
       return this.value;
    }
    }
