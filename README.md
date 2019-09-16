# 3-Connect-Game
This project was written in Java in Fall 2018.

The main goal of this project is to create code that uses AI algorithms to solve a variant of the famous [Connect 4 Game](https://en.wikipedia.org/wiki/Connect_Four).  This version is quite same to the other game with the exception that only three pieces from the same player are needed in a row to win the game.  The algorithm used in this code is the [MiniMax algorithm](https://www.javatpoint.com/mini-max-algorithm-in-ai) which deals with the utulity of each person's move in relation to the whole game, thus trying to minimize utility of one player and maximize the other players.  

The code will work by having a game board or game state or a current game being passed to the progam.  From there, the program will display what the utulity of that situation is (in relation to player one) and print out the board for the next optimal move for the current player.  
