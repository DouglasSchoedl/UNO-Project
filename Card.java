







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


