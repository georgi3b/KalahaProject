This ReadMe is meant to assist the reader to understand the functions used for the simulation. 

The GameSimulation class is used to clone the Game state and initialise all variables that were used. These variables are used in the functions that compute moves for the AI CLient.
The findBestMove method does the following step by step:
	- Checks if the search has exceeded time and exits if it has.
	- Otherwise traverses all the nodes on first level,
	  correpsonding to the moves that can be done on the starting game state,
	  up to the max level and saves the index and corresponding score of the node in a Hash Map.
	- When time runs out, we evaluate the best move we come across, and choose the respective ambo, to make the move.

The alphaBeta method attempts to predict the nodes, it will check the scores that these nodes lead to while traversing,
and prunes when it detects better choices. 
This method is done recursively, on successive nodes to update alpha/beta (depending on the players turn).

The Node class evaluates the score at every move, to check feasibility.