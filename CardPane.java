import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
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
	private ImageIcon cardicon;
	
	public CardPane(Card c)
	{
		super();
		card = c;
		cardFileName = card.getSuit().toString() + card.getType().toString() + ".png";

		cardicon = new ImageIcon(getClass().getResource("IMAGES/" + cardFileName));
	}
	
	protected void paintComponent(Graphics g) {
	    BufferedImage scaledImage = getScaledImage();
	    super.paintComponent(g);
	    g.drawImage(scaledImage, 0, 0, null);
	}

	private BufferedImage getScaledImage()
	{
	    BufferedImage image = new BufferedImage(96, 144, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = (Graphics2D) image.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(cardicon.getImage(), 0, 0, 96, 144, null);

	    return image;
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
