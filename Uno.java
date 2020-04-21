import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;

public class Uno extends JFrame
{
	protected static BorderLayout layout;
	protected HandPanel hand;	//for displaying the cards in a player's hand
	protected CardPane leadingPane;	//leading card in discard stack
	protected static JTextArea console;

	private static Deck d;
	private static int PSIZE = 2;	
	private static Player [] player;
	private static Card leading; //Top of discard pile
	private static Uno frame;
	private static int index;	//index is current player
	//private static Card Wtemp;	//temp for wild card color choice
	private static int numconsolemsg = 0; //number of console mesages (for clearing up console)
	private static boolean hasdrawtwo=false; //for stackable draw 2s
	private static int numdrawtwos = 0;		//for Draw2's method
	private static boolean IsFirstLeading = true;
	private static WildFrame wildframe;


	public static void main(String[] args)
	{

		if(args.length > 1)
		{
			System.out.println("Usage: java -jar hwx.jar [Number of Players]\nExample: java -jar hwx.jar 3");
			System.exit(0);
		}
		else if(args.length == 1)
		{
			if(args[0].equals("4"))
				PSIZE = 4;
			else if(args[0].equals("3"))
				PSIZE = 3;
			else if(args[0].equals('2'));
			else
				System.out.println("Defaulted players to 2\n");
		}

		player = new Player[PSIZE];

		//Creating Deck of cards and the two players
		d = new Deck();
		index = 0;	//start with player 0

		for(int i=0; i<PSIZE; i++)
		{
			player[i] = new Player(i+1);
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
	
		FirstLeading(); //moved this above the gui init becuase of bug
						//i ran into with skip and draw 2 for the first leading card so
						//it would initialize handpanel to correct player

		//GUI Frame
		frame = new Uno(leading);	//passing in first card
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setVisible(true);

		//can console append after gui init
		if(leading.getType() == TypeOfCard.SKIP)
		{
			console.append(">Player 1 was skipped!\n");
			numconsolemsg++;
		}
		else if(leading.getType() == TypeOfCard.REVERSE)
		{	
			console.append(">Reversed!\n");
			numconsolemsg++;
		}
		else if(leading.getType() == TypeOfCard.DRAW2) 
		{
			console.append(">Unlucky, first card was a draw 2 and loss of turn.\n");
			numconsolemsg++;
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
		console = new JTextArea(30, 16);
		console.setEditable(false);
		console.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		console.setLineWrap(true);
		console.setWrapStyleWord(true);
		add(console, BorderLayout.EAST);
	}


	//----------------------------------------------------------------------------

	public static void FirstLeading()
	{
		//for Skip, Reverse, and Draw2 first leading card
		if(leading.getType() == TypeOfCard.SKIP)
		{
			index = NextTurn(index); //sorted out bug this is needed for first leading card
		}
		else if(leading.getType() == TypeOfCard.REVERSE)
		{
			Reverse(index);
		}
		else if(leading.getType() == TypeOfCard.DRAW2) //no stacking draw2's for first card
		{
			DrawTwo(player[index], d);
			index = NextTurn(index);
		}
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
	//changing the top card
	public void changeDiscardTop(Card c)
	{
		leading = c;

		remove(leadingPane);
		leadingPane = makeCard(c);
		add(leadingPane, BorderLayout.CENTER);
		validate();	
	}

	//-----------------------------------------------------------------------------
	//refreshes HandPanel
	public void refreshHand()
	{
		remove(hand);	//remove the old HandPanel to replace it with the new player's hand
		hand = new HandPanel(makeHand());	//link handpanel to new player's hand
		add(hand, BorderLayout.SOUTH);
		validate();		//update the frame
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
	public static void DrawTwo(Player p, Deck D)
	{
		p.TakeCard(D);
		p.TakeCard(D);
		IsNotUno(p);
	}
	public static void DrawTwos(Player p, Deck D, int numdrawtwo)
	{
		console.append(">Player " +p.getPnum()+ " had to draw "+(numdrawtwo*2)+ " cards and lost a turn!\n");
		while(numdrawtwo-- != 0)
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
		console.append(">Player " + player[index].getPnum() + " was skipped!\n");
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
		IsNotUno(p);
	}


	//-----------------------------------------------------------------------------
	public static void IsNotUno(Player p)
	{
		if(p.NumCardsInHand() >= 2 && p.showHasUno())
			p.setHasUno(false);
	}



	//---------------------------------------------------------------------------------------------------------
	//HandPanel inner class for displaying a list of cards
	class HandPanel extends JPanel
	{
		ArrayList<CardPane> hand;	//stores the array of cardPanes that are displayed
		JPanel cardgrid;
		JLabel playerLabel;
		PlayerPanel playerpanel;	//holds player label and draw button
		JButton drawButton;
		JButton unoButton;
		JButton callButton;

		public HandPanel(ArrayList<CardPane> h)
		{
			super();
			hand = h;

			setLayout(new BorderLayout());	//Borderlayout to store hand and Label for which player

			cardgrid = new JPanel();		//card grid shows cards
			cardgrid.setLayout(new GridLayout(1,1));		//set cardgrid layout to gridlayout
			cardgrid.setBorder(BorderFactory.createLoweredBevelBorder());	//border around cards

			cardgrid.add(Box.createRigidArea(new Dimension(1,144)));
			//adding padding to make the cardgrid big enough

			playerpanel = new PlayerPanel();

			for(int i = 0; i < hand.size(); i++)
			{
				cardgrid.add(hand.get(i));
				hand.get(i).addMouseListener(new MouseClickHandler());
			}
			add(cardgrid);
			add(playerpanel, BorderLayout.NORTH);

			cardgrid.add(Box.createRigidArea(new Dimension(1,144)));
			//adding padding to make the cardgrid big enough
		}

		private class PlayerPanel extends JPanel implements ActionListener
		{
			public PlayerPanel()
			{
				super();
				setLayout(new FlowLayout());
				playerLabel = new JLabel("Player " + player[index].getPnum());
				add(playerLabel);
				drawButton = new JButton("Draw Card");
				drawButton.addActionListener(this);
				add(drawButton);
				unoButton = new JButton("Uno"); //we can use this for Uno 
				unoButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							if(player[index].NumCardsInHand() <= 2)
							{	
								for(int i = 0; i < player[index].NumCardsInHand(); i++)
								{
									if(isDiscardable(leading, player[index].getCard(i)))
									{
										player[index].setHasUno(true);
										console.append(">Player " + player[index].getPnum() + " says Uno!\n"); 
										numconsolemsg++; 
										break;
									}
								}
							}
						}
						});
				add(unoButton);
				callButton = new JButton("Call"); //we can use this for Uno 
				callButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e)
						{
							int temp = index;
							index = NextTurn(index);
							while(temp != index)
							{
								if(player[index].NumCardsInHand() == 1 && !player[index].showHasUno())
								{
									console.append(">Player " + player[index].getPnum() + " was caught for having but not saying Uno!\n");
									console.append(">Because they were caught, player " + player[index].getPnum() + " had to draw 2\n");
									numconsolemsg++;
									numconsolemsg++;
									DrawTwo(player[index],d);							
								}
								index = NextTurn(index);
							}
							index = temp;
						}
						});
				add(callButton);
			}

			public void actionPerformed(ActionEvent event)
			{
			DrawUntilPlayable(player[index], d, leading);	//draw cards

			//refresh the hand
			refreshHand();
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

				if(numconsolemsg >5)
				{
					console.setText("");
					numconsolemsg=0;
				}

				if(!isDiscardable(leading, played) && !hasdrawtwo)
				{
					console.append(">That card cannot be player. Please choose another card.\n");
					numconsolemsg++;
					return;
				}
				

				//since draw 2 is stackable it has the highest priority
				if(player[index].getCard(choice).getType() == TypeOfCard.DRAW2 || hasdrawtwo) //Draw2 Block
				{	
					//stacking block
					if(hasdrawtwo && player[index].getCard(choice).getType() == TypeOfCard.DRAW2)
					{
						player[index].Discard(d, choice); //stack draw2
						numdrawtwos++;
						index = NextTurn(index); //next player
						hasdrawtwo = false;
					}
					else if(hasdrawtwo && player[index].getCard(choice).getType() != TypeOfCard.DRAW2)
					{
						DrawTwos(player[index], d, numdrawtwos); //if dont want to play draw 2
						numconsolemsg++;
						index = NextTurn(index);
						numdrawtwos = 0;
						hasdrawtwo=false;
						refreshHand();
						return;
					}

					//starting block
					if(!hasdrawtwo && player[index].getCard(choice).getType() == TypeOfCard.DRAW2)
					{
						player[index].Discard(d, choice); //start draw2
						numdrawtwos++;
						index = NextTurn(index); //next player
					}

					hasdrawtwo = false;

					for(int i = 0; i<player[index].NumCardsInHand(); i++)
					{
						if(player[index].getCard(i).getType() == TypeOfCard.DRAW2)
							hasdrawtwo = true;
					}


					if(hasdrawtwo)
					{

						console.append(">Will player "+player[index].getPnum()+" stack another draw two?\n");
						numconsolemsg++;

						console.append(">If yes, select the draw2, otherwise select any other card.\n");
						numconsolemsg++;
					}
					else
					{
						DrawTwos(player[index], d, numdrawtwos);
						numconsolemsg++;
						numdrawtwos = 0;
					}
				}
				else if(player[index].getCard(choice).getType() == TypeOfCard.SKIP)
				{
					player[index].Discard(d, choice);
					index = Skip(index);

					numconsolemsg++;
				}
				else if(player[index].getCard(choice).getType() == TypeOfCard.REVERSE) //Reverse Block
				{
					player[index].Discard(d, choice);
					Reverse(index);	
					console.append(">Reversed!\n");
					numconsolemsg++;
				}
				else if(played.getType() == TypeOfCard.SELECTCOLOR)	//replaces the player's hand with color select
				{
					wildframe = new WildFrame();
					player[index].Discard(d, player[index].findCardIndex(played));
				}
				else if(played.getType() == TypeOfCard.DRAW4WILD)
				{
					wildframe = new WildFrame();
					player[index].Discard(d, player[index].findCardIndex(played));
					if(index == PSIZE-1)
						DrawTwos(player[0],d,2);
					else
						DrawTwos(player[index+1],d,2);
				}
				else if(isDiscardable(leading, played))
					player[index].Discard(d, player[index].findCardIndex(played));



				//if you dont say uno and dont get caught you can still win!
				if(player[index].NumCardsInHand() == 0)	//this triggers if player[i] played last card
				{
					console.append(">Player " + player[index].getPnum() + " wins!\n");
					refreshHand();
					return;
				}
				else if(hasdrawtwo){
					refreshHand();	//refresh the handpanel to reflect the next player's cards
				}
				else if(played.getType() == TypeOfCard.DRAW4WILD)
				{
					refreshHand();	//refreshing here to reflect that we played the draw4
					index = NextTurn(index);
				}
				else if(played.getSuit() == Suit.WILD){
					refreshHand();	//refreshing here to reflect that we played the wild
				}
				else{
					index = NextTurn(index);
					refreshHand();	//refresh the handpanel to reflect the next player's cards
				}

				changeDiscardTop(played);		//update the top card Component
			}
		}
	}	
	class WildFrame extends JFrame
	{
		ArrayList<JLabel> colors; //stores array of color Labels

		public WildFrame()
		{
			super("Set the color");
			setLayout(new GridLayout(2,2));
			setSize(300, 320);
			setVisible(true);

			colors = new ArrayList<JLabel>();
			
			for(Suit i : Suit.values())	//add labels to the array
			{
				if(i == Suit.WILD)	//we dont want to let them select the wild suit, only the colors
					break;
				JLabel colorlabel = new JLabel();
				ImageIcon icon = new ImageIcon(getClass().getResource("IMAGES/" + i.name() + ".png"));
				icon.setDescription(i.name());		//setting description for finding out which color clicked in mouseadapter
				colorlabel.setIcon(icon);
				colorlabel.setHorizontalAlignment(SwingConstants.CENTER);
				colors.add(colorlabel);
			}

			for(int i = 0; i < 4; i++)
			{
				add(colors.get(i));	//add the labels to the frame.
				colors.get(i).addMouseListener(new MouseClickHandler());
			}
		}

		private class MouseClickHandler extends MouseAdapter
		{
			public void mouseClicked(MouseEvent event)
			{
				JLabel clicked = (JLabel)(event.getComponent());	
				//downcasts component clicked to a JLabel
				String color = ( (ImageIcon)(clicked.getIcon()) ).getDescription();

				//sets the leading card internally, but does not change the icon
				leading = new Card(Suit.valueOf(color), leading.getType());
				console.append(">New color is " + color + "!\n");
				numconsolemsg++;
				setVisible(false);

				//after color is selected, switch to next player
				index = NextTurn(index);
				refreshHand();
			}
		}
	}
}
