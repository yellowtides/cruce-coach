# cruce-coach
An andorid native app that teaches users how to get better at the Cross card game. Rules can be found at https://tromf.ro/tutorial.htm.

## Motivation

Felt like learning on my own since my highly seasoned teammates were frustrated with my obviously poor plays. >:(

## How?

Depth 10+ (could be higher on higher-end devices) minimax bruteforce. Best scoring cards are highlighted (suggested to the user) before the player's every turn. All other (computer) players play according to said suggestions.

## Future work

1. Move un-making would prove more efficient both time and memory wise.
2. Some MCTS heuristic near the start of the game would be more sensible than a naive bruteforce.
3. Symmetries are (as of now) unaccounted for. They should be. Hands are sets of cards, and so permutations do not matter.
4. Currently, the computer calculates the "best" plays by knowing every card in every player's hand. This is unrealistic. If the above 3 points are implemented and high depth calculations become feasible, then the move should be further decided on based on multiple bruteforces on random shuffles outside of the player's own hand.

## Extensions

1. User state configuration. Would make cheating at the game possible!
2. Implement something similar for 1-deck blackjack. Counting cards with style (and a little help).
