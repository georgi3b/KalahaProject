package ai;

import java.util.HashMap;
import java.util.Map;
import kalaha.GameState;

/* Initialisation of variables used. 
This class is used for simulating the game in AIClient */
public class GameSimulation {

    private GameState state;
    private int player;
    private boolean turn;
    private int bestChoice = 99;
    private boolean stopSearch=false;
    final int LIM = 4999;
    public GameSimulation(GameState state, int player, boolean turn){
        this.state = state.clone();
        this.player = player;
        this.turn = turn;
    }
    /* This method starts its search from 
    current game state and simulates the game,
    does an iterative deepening DFS to search for the best move by
    calling the alpha-beta function recursively and 
    returns best score after exceeding  set time limit */
    public int findBestMove(){
        long startTime = System.currentTimeMillis();
        int maxDepth=1;
        while(true){
            long currTime=System.currentTimeMillis();
            if(currTime-startTime>LIM){
                break;
            }
            int rootVal = Integer.MIN_VALUE;
            HashMap<Integer,Integer>  map = new HashMap<>();
            for(int i=1;i<7;i++){
                GameState nodeState = this.state.clone();
                if(nodeState.moveIsPossible(i)){
                    nodeState.makeMove(i);
                    Node childI = new Node(nodeState);
                    if(childI.state.getNextPlayer() != player){
                        turn = false;
                    }else{
                        turn = true;
                    }
                    int childVal=alphaBeta(childI,turn,1,maxDepth, Integer.MIN_VALUE,Integer.MAX_VALUE, startTime);
                    if(childVal > rootVal){
                        rootVal=childVal;
                    }
                    map.put(i,childVal);

                }
            }
            Map.Entry<Integer,Integer> maxEntry = null;
            
            if(!stopSearch){
                int keyMax=0;
                for (Map.Entry<Integer,Integer> entry : map.entrySet()){
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue())>0){
                        maxEntry = entry;
                        keyMax = maxEntry.getKey();
                    }        
                }
                bestChoice=keyMax;
            } 
            maxDepth++;
        }
        return bestChoice;
        
    }
  
    /* This method checks for min/max 
    value depending on player turn, 
    and returns node value depending on the turn
    */
    public int alphaBeta(Node n,boolean turn, int depth, int maxDepth, int alpha,int beta, long startTime){
        long currTime=System.currentTimeMillis();

        if(currTime-startTime>LIM){
            stopSearch=true;
        }
        
        if (stopSearch || n.state.gameEnded()||depth>=maxDepth  ){
            return n.val;
        } else  {
            if (turn){
                n.val = Integer.MIN_VALUE;
                int newD = ++depth;
                for (int i = 1; i < 7; i++){
                    GameState nodeState = n.state.clone();
                    if(nodeState.moveIsPossible(i)){
                        nodeState.makeMove(i);
                        Node childI = new Node(nodeState);
                        if(nodeState.getNextPlayer()!=player){
                                turn=false;
                        }else{
                                turn=true;
                        }
                        int childVal=alphaBeta(childI, turn,newD,maxDepth,alpha,beta,startTime);
                        if(childVal > n.val){
                            n.val = childVal;
                        }
                        if(n.val > alpha){
                            alpha=n.val;
                        }
                        if(alpha >= beta){
                           break;
                        }
                    }
                } 
            }
            else {
                
                n.val = Integer.MAX_VALUE;
                int newD = ++depth;
                for (int i = 1; i < 7; i++){
                    GameState nodeState = n.state.clone();
                    if(nodeState.moveIsPossible(i)){
                        nodeState.makeMove(i);
                        Node childI = new Node(nodeState);
                        if(nodeState.getNextPlayer()!=player){
                                turn=false;
                        }else{
                                turn=true;
                        } 
                        int childVal = alphaBeta(childI, turn,newD,maxDepth,alpha,beta,startTime);
                        if(childVal < n.val){
                            n.val = childVal;
                        }
                        if(n.val < beta){
                            beta=n.val; 
                        }
                        if(alpha >= beta){
                            break;
                        }
                    }
                }
            }
        }
        return n.val;
    }
/* Setting values and evaluating gamestate after every move */  
    private class Node{
        
        GameState state;
        int val;       
        
        public Node(GameState state){
            this.state = state;
            this.val = evaluateSimulation();
        }
        
        public int evaluateSimulation(){
            int score=0;
            String s=state.toString();
            String []tokens=s.split(";");
            if (player == 1){
                score = Integer.parseInt(tokens[7]);
            } else if (player == 2){
                score = Integer.parseInt(tokens[0]);
            }
            val = score;
            return score;
        }  
    }  
}
