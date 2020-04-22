# UNO-Project
Lets make Uno work
Made By: Jeffrey Knappman and Douglas Schoedl


Rules and Startup: We implimented Uno that plays just like the game. Colored number cards that include Reverse, Skip, Draw2, Wild, and Draw4 wild. Players will all be human and the number (from 2 up to 4) will be passed in as first arg.

Example java -jar hwx.jar 3		(For a 3 player game)


InterFace: Players will start with a hand of 7 cards at the bottom, click a card that is legal to put down.
The card on the top is the top of the discard pile and the deck will reshuffle automatically. 
On the right side text will appear showing what moves have been played. 
The Draw Button draws cards up to the point where the next legal move is picked. 
The Uno button will allow the player to say uno on their turn when there are 2 cards and at least one is legal to play. 
The call button will iterate through all the players searching for a player that is in Uno and has not said it to get that player to draw 2 cards for not calling uno.

Extra Features: Draw 2 stacking feature. Instead of playing a single draw 2, if multiple players have a draw 2, they have the ability to play another if they wish. Try it in the game to see how it works!




Split up of work:
Jeffrey Knappman - Made card.java, deck.java, CardPane.java, made most of the GUI, adding some game logic, tweaked game logic to work with GUI. 


Douglas Schoedl - Made player.java, most of the game logic in Uno.java, Draw 2 extra feature, some rework in deck.java involving turning deck into a stack, the Uno and Call Button logic and inclusion.





 
