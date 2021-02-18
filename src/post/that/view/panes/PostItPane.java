package post.that.view.panes;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JTextArea;

public class PostItPane extends JInternalFrame
{
	private static final long serialVersionUID = 1834391632998832385L;
	private static final Dimension DEFAULT_SIZE = new Dimension(128, 128);

	public PostItPane()
	{
		this.setup();
		this.build();
	}

	private void setup()
	{
		this.setSize(PostItPane.DEFAULT_SIZE);

		this.setResizable(true);
		this.setClosable(true);

		this.setVisible(true);
	}

	private void build()
	{
		this.getContentPane().add(new JTextArea());
	}
}
