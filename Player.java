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


	//does not handle empty stack of cards need to cover that in gameplay
	public void TakeCard(Deck D)
	{
		Hand.add(D.PickupCard());
		SortHand();
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











}
