package post.that.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import post.that.view.panes.BoardPane;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = -5628864249328436672L;
	private static final String TITLE = "Post That";
	private static final Dimension DEFAULT_SIZE = new Dimension(720, 640);

	public MainFrame()
	{
		this.setup();
		this.build();
	}

	public void display()
	{
		this.setVisible(true);
	}

	private void setup()
	{
		this.setTitle(MainFrame.TITLE);
		this.setSize(MainFrame.DEFAULT_SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void build()
	{
		this.setLayout(new BorderLayout());
		this.add(new BoardPane(), BorderLayout.CENTER);
	}
}
