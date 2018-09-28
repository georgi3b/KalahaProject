/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import kalaha.GameState;

/**
 *
 * @author Georgiana
 */
public class GameSimulation {

    private GameState state;
    private int player;
    boolean turn;
    int bestChoice = 99;
    
    
    public GameSimulation(GameState state, int player, boolean turn){
        this.state = state.clone();
        this.player = player;
        this.turn = turn;
    }
    
    public int findBestMove(){
        int rootVal = Integer.MIN_VALUE;
        HashMap<Integer,Integer>  map = new HashMap<>();
        
        for(int i=1;i<7;i++){
            GameState nodeState = this.state.clone();
            if(nodeState.moveIsPossible(i)){
                nodeState.makeMove(i);
                Node childI=new Node(nodeState);
                if(childI.state.getNextPlayer() != player){
                    turn = false;
                }else{
                    turn = true;
                }
                int childVal=alphaBetaSearch(childI,turn,1,6, Integer.MIN_VALUE,Integer.MAX_VALUE);
                if(childVal > rootVal){
                    rootVal=childVal;
                }
                map.put(childVal,i);
                
            }
        }
        int maxValueInMap=(Collections.max(map.keySet()));
        int val = map.get(maxValueInMap);
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            System.out.println("Value " + entry.getKey() + " Key" + entry.getValue());
        }
        System.out.println("Best choice " + val);
        return val;
        
    }
    
    public int alphaBetaSearch(Node n,boolean turn, int depth, int maxDepth, int alpha,int beta){
        
        if (n.state.gameEnded()||depth>=maxDepth){
            n.evaluateSimulation();
            return n.val;
        } else {
            int newD = ++depth;
            for (int i = 1; i < 7; i++){
                GameState nodeState = n.state.clone();
                if(nodeState.moveIsPossible(i)){
                    nodeState.makeMove(i);
                    Node childI = new Node(nodeState);
                    //Maximizing player
                    if (turn){
                        if(nodeState.getNextPlayer()!=player){
                            turn=false;
                         }else{
                            turn=true;
                        }
                        if(n.val > beta){
                           //System.out.println("Pruned at depth " + depth + " Value returnd"+ n.val);
                           return n.val; 
                        }
                        int childVal=alphaBetaSearch(childI, turn,newD,maxDepth,alpha,beta);
                        if(childVal > n.val){
                            n.val = childVal;
                        }
                        if(n.val > alpha){
                            System.out.println("Apha is "  + n.val);
                            alpha=n.val;
                        }

                    }
                    //Minimizing player
                    else{
                        if(nodeState.getNextPlayer()!= player){
                            turn = false;
                         }else{
                            turn=true;
                        }
                        if(n.val < alpha){
                            //System.out.println("Pruned at depth " + depth + " Value returnd"+ n.val);
                            return n.val;
                        }
                        int childVal = alphaBetaSearch(childI, turn,newD,maxDepth,alpha,beta);
                        if(childVal < n.val){
                            n.val = childVal;
                        }
                        //Beta is updated in the wrong way...
                        if(n.val < beta){
                            System.out.println("Beta is "  + n.val);
                            beta=n.val; 
                        }
                    }
                }
            }
                
                return n.val;
            
        }
            
    }
      
    private class Node{
        
        GameState state;
        int val = Integer.MIN_VALUE;        
        
        public Node(GameState state){
            this.state = state;
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
