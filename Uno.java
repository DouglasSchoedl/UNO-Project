import java.util.Scanner;

public class Uno
{
	private static Deck d;
	private static int PSIZE = 2;	
	private static Player [] player = new Player[PSIZE];
	private static Card leading; //Top of discard pile


	public static void main(String[] args)
	{
		//Creating Deck of cards and the two players
		d = new Deck();

		for(int i=0; i<PSIZE; i++)
		{
			player[i] = new HumanPlayer(i+1);
			player[i].dealCards(d);
		}

		//Leading with the first card from the pickup stack. This is 3 lines for printing for testing
		leading = d.PickupCard();			//picking up the leading card in the PStack
		d.DiscardCard(leading);					//putting the card into the discard pile
		
		while(leading.getSuit() == Suit.WILD)	//leading card cannot be wild
		{
			leading = d.PickupCard();
			d.DiscardCard(leading); 
		}

		System.out.println("the first card is: " + leading.printCard());
		int index = 0;	//start with player 1
		Scanner input = new Scanner(System.in);


		//while neither is out of cards, loop for testing
		while(player[index].NumCardsInHand() != 0)
		{
		
			player[index].ShowHand();
			leading = d.DiscardPile[d.DPsize()-1];
			System.out.println("DiscardTop: " + leading.printCard());	
			System.out.printf("Player %d, play a card or put -1 to draw.\n>", player[index].getPnum());

			int choice = input.nextInt();
			if(choice == -1)
			{
				DrawUntilPlayable(player[index],d,leading);
				continue;
			}
			
			//testing the players choice vs. the top card of discard stack
			if(!isDiscardable(leading, player[index].getCard(choice)))
			{
				System.out.println("That card cannot be played. Please choose another card.");
				continue;	
			}
			
			if(player[index].getCard(choice).getType() == TypeOfCard.REVERSE)
			{
				player[index].Discard(d, choice);
				Reverse(index);	
			}		
			else if(player[index].getCard(choice).getType() == TypeOfCard.DRAW2)
			{
				player[index].Discard(d, choice);
				index = NextTurn(index);
				DrawTwo(player[index],d);
				System.out.printf("Player %d, had to draw 2 and lost a turn\n", player[index].getPnum());
			}
			else
				player[index].Discard(d, choice);



			if(player[index].NumCardsInHand() == 0)	//this triggers if player[i] played last card
				break;

			index = NextTurn(index);
			

		}
	}

	
	//-----------------------------------------------------------------------------
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

		
	//-----------------------------------------------------------------------------
	public static int NextTurn(int index)
	{
		if(index == PSIZE-1)
			index = 0;
		else
			index++;
		return index;
	}





	//-----------------------------------------------------------------------------
	//Have to add wait functionallity to allow for multiple drawtwo's
	public static void DrawTwo(Player pnum, Deck D)
	{
		pnum.TakeCard(D);
		pnum.TakeCard(D);
	}
	public static void DrawTwos(Player pnum, Deck D, int numdrawtwos)
	{
		System.out.printf("Player %d had to draw %d cards!", pnum.getPnum(), (numdrawtwos*2));
		while(numdrawtwos-- != 0)
		{
			DrawTwo(pnum, D);
		}
	}

	//-----------------------------------------------------------------------------
	

	//a bit overly complicated but reverse player array starting at the index
	//Example {0,1,2,3} at index 2 -> {0,3,2,1}
	//So player 2 plays reverse then the next is player 1 then 0 then..
	//Example {0,3,2,1} at index 1 -> {2,3,0,1}
	public static void Reverse(int index)
	{
		int num;
		if(index == 0)
			num = PSIZE-1;
		else
			num = index-1;
		int i = index;
		Player[] temp = new Player[PSIZE];
		while(i != num)
		{
			temp[i] = player[index];
			if(i == PSIZE-1)
				i=0;
			else
				i++;
			if(index == 0)
				index = PSIZE-1;
			else
				index--;
		}
		temp[i] = player[index];

		player = temp;
	}


	//-----------------------------------------------------------------------------
	//Draws until playable
	public static void DrawUntilPlayable(Player p, Deck D, Card leading)
	{
		Card test = p.TakeCard(D);
		while(!isDiscardable(leading, test))
		{
			test = p.TakeCard(D);
		} 
	}


	//-----------------------------------------------------------------------------

}
