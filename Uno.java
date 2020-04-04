import java.util.Random;
import javax.swing.JFrame;
import java.awt.Dimension;

enum Suit 	//the color of the card. didn't call it color to avoid issues with swing libraries
{
	RED,
	GREEN,
	BLUE,
	YELLOW,
	WILD
}	
enum TypeOfCard	//the symbol or number on the card
{
	ZERO,
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN,
	EIGHT,
	NINE,
	SKIP,
	REVERSE,
	DRAW2,
	SELECTCOLOR,
	DRAW4WILD
}

public class Uno
{
	public static void main(String[] args)
	{
		Deck d = new Deck();
		d.printDeck();
	}
}

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

class Card// implements Comparable - havent done this yet
	{	
		private Suit suit;
		private TypeOfCard type;

		public Card(Suit s, TypeOfCard t)
		{
			suit = s;
			type = t;
		}

		public String printCard()
		{
			String name = "";
			switch(suit)
			{
				case RED: name+="Red"; break;
				case GREEN: name+="Green"; break;
				case BLUE: name+="Blue"; break;
				case YELLOW: name+="Yellow"; break;
				case WILD: name+="Wild"; break;
			}
			name+=" ";
			switch(type)
			{
				case ZERO: name+= "0"; break;
				case ONE: name+= "1"; break;
				case TWO: name+= "2"; break;
				case THREE: name+= "3"; break;
				case FOUR: name+= "4"; break;
				case FIVE: name+= "5"; break;
				case SIX: name+= "6"; break;
				case SEVEN: name+= "7"; break;
				case EIGHT: name+= "8"; break;
				case NINE: name+= "9"; break;
				case SKIP: name+= "Skip"; break;
				case REVERSE: name+= "Reverse"; break;
				case DRAW2: name+= "Draw 2"; break;
				case SELECTCOLOR: name+= "Select Color"; break;
				case DRAW4WILD: name+= "Draw 4"; break;
			}
			System.out.println(name);
			return name;
		}

		public Suit getSuit()
		{
			return suit;
		}

		public TypeOfCard getType()
		{
			return type;
		}

		public int compareTo()
		{
			return 0;
		}
	}


