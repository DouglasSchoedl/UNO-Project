import java.util.Random;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.Stack;
import java.util.EmptyStackException;

//Deck is a deck of UNO cards that has a pickup pile that utilizes stack and a discard pile that is
//an array of cards that will eventually be shuffled and turned into a pickup pile stack

class Deck
{
	private int PSCursize; //Current size of Pickup stack
	public Card[] DiscardPile;	//dicard pile card array
	private int DPCursize;
	final static int SIZE = 108; //max posible size pile
	private Suit[] suits;
	private TypeOfCard [] types;
	private Stack<Card> PStack;

//---------------------------------------------------------------------------------
	//initialize the deck
	public Deck()
	{
		DiscardPile = new Card[SIZE]; //just using discard pile to build PStack 
		PSCursize = SIZE;
		DPCursize = 0;			//real size of dicard pile is 0
		suits = Suit.values();
		types = TypeOfCard.values();
		PStack = new Stack<Card>(); //Pickup Stack

		//96 Cards made here
		for(int t = 0; t < 12; t++)	//walking through the types enum (1 to draw 2)
		{
			for(int s = 0; s < suits.length-1; s++)	//walking through suits enum (red, blue, green, yellow)
			{
				DiscardPile[(8*(t))+(2*s)] = new Card(suits[s], types[t+1]);
				DiscardPile[(8*(t))+(2*s)+1] = new Card(suits[s], types[t+1]);
			}
		}

		//Making the 0's. There's only 4 zeroes as opposed to 8
		DiscardPile[96] = new Card(Suit.RED, TypeOfCard.ZERO);
		DiscardPile[97] = new Card(Suit.BLUE, TypeOfCard.ZERO);
		DiscardPile[98] = new Card(Suit.GREEN, TypeOfCard.ZERO);
		DiscardPile[99] = new Card(Suit.YELLOW, TypeOfCard.ZERO);

		//making wild cards
		for(int i = 100; i < 104; i++)
		{
			DiscardPile[i] = new Card(Suit.WILD, TypeOfCard.SELECTCOLOR);
		}
		for(int i = 104; i < 108; i++)
		{
			DiscardPile[i] = new Card(Suit.WILD, TypeOfCard.DRAW4WILD);
		}

		shuffle(SIZE); //shuffle array

		for(int i=0; i<SIZE;i++) //put into stack
			PStack.push(DiscardPile[i]);

	}

//Need to work on this one/may just get rid of it entirely
//---------------------------------------------------------------------------------

	public void printDeck()
	{
		System.out.println(PSCursize);
	}


//---------------------------------------------------------------------------------

	public int StackSize()
	{
		return PSCursize;
	}

//---------------------------------------------------------------------------------
	public Card[] shuffle(int size)
	{
		Card[] shuffledDeck = new Card[size];

		for(int i = 0; i < size; i++)	//picking a random number and replacing it with null to get a random order
		{
			Random r = new Random();
			int rnum = r.nextInt(size);
			while(DiscardPile[rnum] == null)
			{
				rnum = r.nextInt(size);
			}

			shuffledDeck[i] = DiscardPile[rnum];
			DiscardPile[rnum] = null;
		}
		DiscardPile = shuffledDeck;
		return shuffledDeck;
	}


//---------------------------------------------------------------------------------
	public Card PickupCard()
	{
		Card c = null;
		try
		{
			c = PStack.pop();
			PSCursize--;
		}
		catch(EmptyStackException e)
		{
			e.printStackTrace();
		}
	//	System.out.println(c.printCard() + "\n--"); 
		return c;
	}



	//the discarded card will have to be removed from player hand
	//before put into this function(it wont remove the card from hand).
	//It puts the input card into discard pile.
	public void DiscardCard(Card c)
	{
		DiscardPile[DPCursize++] = c;
	}

	public void DPtoPStack()
	{
		shuffle(DPCursize);
		for(int i = 0; i<DPCursize; i++)
			PStack.push(DiscardPile[i]);
		PSCursize = DPCursize;	
		DPCursize = 0;
	}

//--------------------------------------------------------------------------------
	public int DPsize()
	{
		return DPCursize;
	}


}
