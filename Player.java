import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

//4 player game max
//Start with 7 cards in hand each player has a hand

public abstract class Player
{
	protected int Pnum; 		//player number
	protected List<Card> Hand = new ArrayList<Card>();		//player hand


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
			System.out.println(Hand.get(i).printCard());
		}
	}


	//utilizes comparable in class Card and Collections list sort
	protected void SortHand()
	{
		Collections.sort(Hand, new CardSort());
	}

	protected int getHandSize()
	{
		return Hand.size();
	}

	protected int getPnum()
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









}
