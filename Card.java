import java.util.Comparator; 


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


class Card implements Comparable<Card> 
{	
	private Suit suit;
	private TypeOfCard type;
	private int Priority; //for comparable purposes

	public Card(Suit s, TypeOfCard t)
	{
		suit = s;
		type = t;
		switch(suit)
		{
			case RED: Priority = 41; break;
			case YELLOW: Priority = 28; break;
			case GREEN: Priority = 15; break;
			case BLUE: Priority = 2; break;
			case WILD: Priority = 0; break;
		}
		switch(type)
		{
			case ZERO: Priority+=12; break;
			case ONE: Priority+=11; break;
			case TWO: Priority+=10; break;
			case THREE: Priority+=9; break;
			case FOUR: Priority+=8; break;
			case FIVE: Priority+=7; break;
			case SIX: Priority+=6; break;
			case SEVEN: Priority+=5; break;
			case EIGHT: Priority+=4; break;
			case NINE: Priority+=3; break;
			case SKIP: Priority+=2; break;
			case REVERSE: Priority+=1; break;
			case DRAW2: break;
			case SELECTCOLOR: Priority+=1; break;
			case DRAW4WILD: break;
		}
	}

	public String printCard()
	{
		String name = "";
		switch(suit)
		{
			case RED: name+="Red"; break;
			case YELLOW: name+="Yellow"; break;
			case GREEN: name+="Green"; break;
			case BLUE: name+="Blue"; break;
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
		//System.out.println(name);
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

	public int compareTo(Card C)
	{
		int order;
		order = Priority - C.Priority;
		
		if(order>0)
			order = -1;
		else if(order<0)
			order = 1;

		return order;
	}

}


class CardSort implements Comparator<Card>
{
	public int compare(Card C1,Card C2)
	{
		return C1.compareTo(C2);
	}
}
