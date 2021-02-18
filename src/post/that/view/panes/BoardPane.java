package post.that.view.panes;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JDesktopPane;

import post.that.view.ressources.Images;

public class BoardPane extends JDesktopPane
{
	private static final long serialVersionUID = 838829298589001150L;
	private static final Image BACKGROUND = Images.BOARD_BACKGROUND.getDefaultImage();

	public BoardPane()
	{
		this.add(new PostItPane());
		this.add(new PostItPane());
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(BoardPane.BACKGROUND, 0, 0, null);
	}
}