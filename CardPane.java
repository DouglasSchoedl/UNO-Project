import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

//This class is what displays a single card
class CardPane extends JPanel
{
	private Card card;
	private String cardFileName;
	
	public CardPane(Card c)
	{
		super();
		card = c;
		cardFileName = card.getSuit().toString() + card.getType().toString() + ".png";

		JLabel cardlabel = new JLabel();
		Icon card = new ImageIcon(getClass().getResource("images/" + cardFileName));
		cardlabel.setIcon(card);
		cardlabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(cardlabel);

	}

	public Card getCard()
	{
		return card;
	}

	public String getCardName()
	{
		return cardFileName;
	}

	
}
