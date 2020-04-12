import java.util.Scanner;

public class Uno
{
	private static Deck d;
	private static Player one;
	private static Player two;

	public static void main(String[] args)
	{
		//Creating Deck of cards and the two players
		d = new Deck();
		one = new HumanPlayer(1);
		two = new HumanPlayer(2);

		one.dealCards(d);
		two.dealCards(d);

		//Leading with the first card from the pickup stack. This is 3 lines for printing for testing
		Card leading = d.PickupCard();			//picking up the leading card in the PStack
		d.DiscardCard(leading);					//putting the card into the discard pile
		System.out.println("the first card is: " + leading.printCard());

		//while neither is out of cards, loop for testing
		while(one.NumCardsInHand() != 0 && two.NumCardsInHand() != 0)
		{
			one.ShowHand();
			System.out.println("Player 1, play a card.");
			//Input from console
			Scanner input = new Scanner(System.in);
			int choice = input.nextInt();

		
			//testing the players choice vs. the top card of discard stack
			while(!isDiscardable(d.DiscardPile[d.DPsize()-1], one.getCard(choice)))
			{
				System.out.println("That card cannot be played. Please choose another card.");
				choice = input.nextInt();
			}
			one.Discard(d, choice);
			
			if(one.NumCardsInHand() == 0)	//this triggers if player one played last card
				break;

			two.ShowHand();
			System.out.println("Player 2, play a card.");
			choice = input.nextInt();
			while(!isDiscardable(d.DiscardPile[d.DPsize()-1], two.getCard(choice)))
			{
				System.out.println("That card cannot be played. Please choose another card.");
				choice = input.nextInt();
			}
			two.Discard(d, choice);

		}
	}


	//tests if a card is able to be played based on rules of the game
	public static boolean isDiscardable(Card top, Card test)
	{
		boolean discardable = false;	//will not be discardable if it doesn't satisfy
										//the below conditions to be playable
		if(test.getSuit() == Suit.WILD)	//if it's wild, it's always able to be played.
			discardable = true;
		else if(test.getSuit() == top.getSuit())	//Cards must be the same suit to be played
			discardable = true;
		else if(test.getType() == top.getType())	//OR must be same type 
			discardable = true;

		return discardable;

	}


	
	
}
