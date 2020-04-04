import java.util.Random;
import javax.swing.JFrame;
import java.awt.Dimension;



class Deck
{
	public Card[] cards;
	final static int SIZE = 108;
	

	public Deck()
	{
		cards = new Card[SIZE];
		Suit[] suits = Suit.values();
		TypeOfCard[] types = TypeOfCard.values();

		//96 Cards made here
		for(int t = 0; t < 12; t++)	//walking through the types enum (1 to draw 2)
		{
			for(int s = 0; s < suits.length-1; s++)	//walking through suits enum (red, blue, green, yellow)
			{
				cards[(8*(t))+(2*s)] = new Card(suits[s], types[t+1]);
				cards[(8*(t))+(2*s)+1] = new Card(suits[s], types[t+1]);
			}
		}

		//Making the 0's. There's only 4 zeroes as opposed to 8
		cards[96] = new Card(Suit.RED, TypeOfCard.ZERO);
		cards[97] = new Card(Suit.BLUE, TypeOfCard.ZERO);
		cards[98] = new Card(Suit.GREEN, TypeOfCard.ZERO);
		cards[99] = new Card(Suit.YELLOW, TypeOfCard.ZERO);

		//making wild cards
		for(int i = 100; i < 104; i++)
		{
			cards[i] = new Card(Suit.WILD, TypeOfCard.SELECTCOLOR);
		}
		for(int i = 104; i < 108; i++)
		{
			cards[i] = new Card(Suit.WILD, TypeOfCard.DRAW4WILD);
		}

		shuffle();
	}

	public void printDeck()
	{
		for(int i = 0; i < SIZE; i++)
		{
			cards[i].printCard();
		}
	}

	public Card[] shuffle()
	{
		Card[] shuffledDeck = new Card[SIZE];
		Card blank = new Card(Suit.WILD, TypeOfCard.ZERO);	//making a "blank" card to make sure we move every card

		for(int i = 0; i < SIZE; i++)	//picking a random number and replacing it with a "blank" card to get a random order
		{
			Random r = new Random();
			int rnum = r.nextInt(SIZE);
			while(cards[rnum] == blank){
				rnum = r.nextInt(SIZE);
			}
			shuffledDeck[i] = cards[rnum];
				cards[rnum] = blank;
		}
		cards = shuffledDeck;
		return shuffledDeck;
	}
	
}
