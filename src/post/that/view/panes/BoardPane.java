package post.that.view.panes;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JDesktopPane;

import post.that.model.PostThat;
import post.that.model.PostThatBoard;
import post.that.view.ressources.Images;

public class BoardPane extends JDesktopPane
{
	private static final long serialVersionUID = 838829298589001150L;
	private static final Image BACKGROUND = Images.BOARD_BACKGROUND.getDefaultImage();

	private final PostThatBoard board = PostThatBoard.getSavedBoard();

	public BoardPane()
	{
		for(PostThat postThat : this.board.getPostThats())
		{
			this.add(new PostItPane(postThat));
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(BoardPane.BACKGROUND, 0, 0, null);
	}
}
