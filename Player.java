import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

//4 player game max
//Start with 7 cards in hand each player has a hand

public class Player
{
	private int Pnum; 		//player number
	private List<Card> Hand = new ArrayList<Card>();		//player hand
	private boolean HasUno = false;

	//Initalizing players hand will take place in the game
	public Player(int pnum)
	{
		Pnum = pnum;		
	}


	//now does handle empty stack of cards need to cover that in gameplay
	public Card TakeCard(Deck D)
	{
		D.ReshuffleIfEmpty();
		Card pickup = D.PickupCard();
		Hand.add(pickup);
		SortHand();
		return pickup;
	}


	//Index will be passed in from cursor clicking card in GUI
	//Handeling for correct discard will be handled in gameplay code
	public void Discard(Deck D, int index)
	{
		D.DiscardCard(Hand.remove(index));
	}

	//number of cards in hand
	public int NumCardsInHand()
	{
		return Hand.size();
	}

	//shows player hamd
	public void ShowHand()
	{
		for(int i=0; i<Hand.size();i++)
		{
			System.out.println(i + " " + Hand.get(i).printCard());
		}
	}


	//utilizes comparable in class Card and Collections list sort
	private void SortHand()
	{
		Collections.sort(Hand, new CardSort());
	}

	public int getPnum()
	{	
		return Pnum; 
	}

	//returns a card for comparing
	public final Card getCard(int index)
	{
		return Hand.get(index);
	}

	//deals 7 cards to player to populate their hand with cards
	public void dealCards(Deck D)
	{
		for(int i = 0; i < 7; i++)
		{
			TakeCard(D);
		}
	}

	//searches a player's hand for a card and returns the index
	public int findCardIndex(Card c)
	{
		int index = 0;
		for(int i = 0; i < Hand.size(); i++)
		{
			int order = c.compareTo(Hand.get(i));
			if(order == 0)	//if order == 0, they are the same and so the card was found.
				index = i;
		}
		return index;
	}

	public boolean showHasUno()
	{
		return HasUno;
	}
	
	public void setHasUno(boolean b)
	{
		HasUno = b;
	}




}
