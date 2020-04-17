import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

public class Uno extends JFrame
{
	protected BorderLayout layout;
	protected HandPanel hand;	//for displaying the cards in a player's hand
	protected CardPane leadingPane;	//leading card in discard stack

	private static Deck d;
	private static int PSIZE = 2;	
	private static Player [] player = new Player[PSIZE];
	private static Card leading; //Top of discard pile
	private static Uno frame;
	private static int index;	//index is current player
	private static Card Wtemp;	//temp for wild card color choice

	public static void main(String[] args)
	{
		//Creating Deck of cards and the two players
		d = new Deck();
		index = 0;	//start with player 0

		for(int i=0; i<PSIZE; i++)
		{
			player[i] = new HumanPlayer(i+1);
			player[i].dealCards(d);
		}

		//Leading with the first card from the pickup stack. This is 3 lines for printing for testing
		leading = d.PickupCard();			//picking up the leading card in the PStack
		d.DiscardCard(leading);					//putting the card into the discard pile

		while(leading.getSuit() == Suit.WILD)	//first leading card cannot be wild
		{
			leading = d.PickupCard();
			d.DiscardCard(leading); 
		}
		
		//GUI Frame
		frame = new Uno(leading);	//passing in first card
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setVisible(true);
		

		//for Skip, Reverse, and Draw2 first leading card
		if(leading.getType() == TypeOfCard.SKIP)
		{
			index = Skip(index);
		}
		else if(leading.getType() == TypeOfCard.REVERSE)
		{
			Reverse(index);
		}
		else if(leading.getType() == TypeOfCard.DRAW2) //no stacking draw2's for first card
		{
			System.out.println("Unlucky, first card was a draw 2 and loss of turn.");
			DrawTwo(player[index++], d);
		}	
		//end main
		//most game logic was moved to MouseClickHandler
	}

	//-----------------------------------------------------------------------------
	public Uno(Card top)	//top is the top card in discard stack
	{
		super("Uno");
		layout = new BorderLayout();
		setLayout(layout);

		hand = new HandPanel(makeHand());
		add(hand, BorderLayout.SOUTH);
		leadingPane = makeCard(top);
		add(leadingPane, BorderLayout.CENTER);
		
	}

	//-----------------------------------------------------------------------------
	//helper function for turning a Card obj into a CardPane
	public CardPane makeCard(Card c)
	{
		return new CardPane(c);
	}

	//-----------------------------------------------------------------------------
	//makes a handpanel
	public ArrayList<CardPane> makeHand()
	{
		ArrayList<CardPane> hand = new ArrayList<CardPane>();
		for(int i = 0; i < player[index].NumCardsInHand(); i++)
		{
			hand.add(makeCard(player[index].getCard(i)));
		}
		return hand;
	}

	//-----------------------------------------------------------------------------
	public void nextPlayer()	//changes the cards shown for each player's turn
	{
		remove(hand);	//remove the old HandPanel to replace it with the new player's hand

		if((index+1 >= PSIZE))	//increment to next player, if last player,
			index = 0;			//go back to player 0
		else
			index++;

		hand = new HandPanel(makeHand());	//link handpanel to new player's hand
		add(hand, BorderLayout.SOUTH);
		validate();		//update the frame
	}

	//-----------------------------------------------------------------------------
	//similar function to changePlayer. This time changing the top card instead of hand
	public void changeDiscardTop(Card c)
	{
		leading = c;

		remove(leadingPane);
		leadingPane = makeCard(c);
		add(leadingPane, BorderLayout.CENTER);
		validate();	
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
	//make this into a button that only appears at 2 cards
	//When clicked returns value true
//	public static boolean SayUno(Scanner sc, Player p)
//	{
//	}
	

	//Button given to other players if one player dosnt announce uno and has it
//	public static boolean CallNoUno();
//  {
//  }


/* Commenting out Wild logic for now since just so it compiles. will have to add some UI elements for selecting 
 * desired color later
	//-----------------------------------------------------------------------------
	public static Card Wild(Scanner sc, TypeOfCard t)
	{
		Card temp = null;
		int i;
		System.out.println("Choose Color: 0=Red, 1=Yellow, 2=Green, 3=Blue.");
		while(true)
		{
			i = sc.nextInt();
			if(i<4 & i>=0)
				break;
			else
			{
				System.out.println("Not Valid Choice.");
				System.out.println("Choose Color: 0=Red, 1=Yellow, 2=Green, 3=Blue.");
			}
		}
		switch(i)
		{
			case 0: temp = new Card(Suit.RED, t); break;
			case 1: temp = new Card(Suit.YELLOW, t); break;
			case 2: temp = new Card(Suit.GREEN, t); break;
			case 3: temp = new Card(Suit.BLUE, t); break;
		}
		return temp;
	}


	//-----------------------------------------------------------------------------
	public static Card WildDrawFour(Scanner sc, Player p, Deck D)
	{
		Card temp = Wild(sc, TypeOfCard.DRAW4WILD);
		DrawTwos(p,D,2);
		return temp;
	}
*/




	//-----------------------------------------------------------------------------
	//Have to add wait functionallity to allow for multiple drawtwo's
	public static void DrawTwo(Player p, Deck D)
	{
		p.TakeCard(D);
		p.TakeCard(D);
	}
	public static void DrawTwos(Player p, Deck D, int numdrawtwos)
	{
		System.out.printf("Player %d had to draw %d cards and lost a turn!\n", p.getPnum(), (numdrawtwos*2));
		while(numdrawtwos-- != 0)
		{
			DrawTwo(p, D);
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
	public static int Skip(int index)
	{
		index = NextTurn(index);
		System.out.printf("Player %d was skipped!\n", player[index].getPnum());
		return index;
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


	//---------------------------------------------------------------------------------------------------------
	//HandPanel inner class for displaying a list of cards
	class HandPanel extends JPanel
	{
		GridLayout layout;
		ArrayList<CardPane> hand;	//stores the array of cardPanes that are displayed

		public HandPanel(ArrayList<CardPane> h)
		{
			super();
			hand = h;
			layout = new GridLayout(1,1);
			setLayout(layout);
			
			for(int i = 0; i < hand.size(); i++)
			{
				add(hand.get(i));
				hand.get(i).addMouseListener(new MouseClickHandler());
			}
		}

		private class MouseClickHandler extends MouseAdapter
		{
			public void mouseClicked(MouseEvent event)
			{
				CardPane clicked = (CardPane)(event.getComponent());	
				//downcasts component clicked to a CardPane
				Card played = clicked.getCard(); 	//cardpane clicked converted to card object
				int choice = player[index].findCardIndex(played);

				if(!isDiscardable(leading, played))
				{
					System.out.println("That card cannot be played. Please choose another card.");
					return;
				}

				if(player[index].getCard(choice).getType() == TypeOfCard.SKIP)
			{
				player[index].Discard(d, choice);
				index = Skip(index);	
			}
			else if(player[index].getCard(choice).getType() == TypeOfCard.REVERSE) //Reverse Block
			{
				player[index].Discard(d, choice);
				Reverse(index);	
			}		
			else if(player[index].getCard(choice).getType() == TypeOfCard.DRAW2) //Draw2 Block
			{
				int numdrawtwos = 1;
				boolean hasdrawtwo;
				while(true)
				{
					player[index].Discard(d, choice);
					index = NextTurn(index);
					hasdrawtwo = false;

					for(int i = 0; i<player[index].NumCardsInHand(); i++)
					{
						if(player[index].getCard(i).getType() == TypeOfCard.DRAW2)
							hasdrawtwo = true;
					}

					if(hasdrawtwo)
					{
						player[index].ShowHand();
						leading = d.DiscardPile[d.DPsize()-1];
						System.out.println("Total Cards: " + player[index].NumCardsInHand());
						System.out.println("DiscardTop: " + leading.printCard());	

						System.out.printf("Will player %d play another draw two?\n", player[index].getPnum());		
						System.out.print("If yes, select the card, otherwise choose -1.\n>");	

						while(true)
						{
							choice = 0;	//was choice = input.nextInt()
							if(choice == -1)
								break;
							else if(!isDiscardable(leading, player[index].getCard(choice)))
								System.out.print("That card is not a draw two.\n>");	
							else
								break;
						}

						if(choice != -1)
							numdrawtwos++;
					}
					else
						break;

					if(choice == -1)
						break;
				}
				DrawTwos(player[index], d, numdrawtwos);
			}
			else
				player[index].Discard(d, player[index].findCardIndex(played));

			if(player[index].NumCardsInHand() == 0)	//this triggers if player[i] played last card
			{
				System.out.printf("Player %d wins!\n",player[index].getPnum());
				return;
			}
			else
				nextPlayer();

				//player[index].Discard(d, player[index].findCardIndex(played));	//discard card from player's hand
				changeDiscardTop(played);		//update the top card Component
				//nextPlayer();	//switch to next player.
			}
		}
	}	
}
