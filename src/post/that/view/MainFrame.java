package post.that.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import post.that.utils.Translation.Internationalization;
import post.that.view.adapter.WindowAdapter;
import post.that.view.panes.BoardPane;

public class MainFrame extends JFrame implements WindowAdapter
{
	private static final long serialVersionUID = -5628864249328436672L;
	private static final String TITLE = Internationalization.get("APP_TITLE");
	private static final Dimension DEFAULT_SIZE = new Dimension(720, 640);
	private BoardPane board;

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
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(this);
	}

	private void build()
	{
		this.board = new BoardPane();

		this.setLayout(new BorderLayout());
		this.add(this.board, BorderLayout.CENTER);
	}

	@Override
	public void windowClosing(WindowEvent event)
	{
		if(this.board.confirmClose())
		{
			System.exit(0);
		}
	}
}
