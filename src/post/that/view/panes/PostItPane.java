package post.that.view.panes;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JTextArea;

import post.that.model.PostThat;

public class PostItPane extends JInternalFrame
{
	private static final long serialVersionUID = 1834391632998832385L;
	private static final Dimension DEFAULT_SIZE = new Dimension(128, 128);

	private String id;
	private JTextArea content;

	public PostItPane(PostThat postThat)
	{
		this();
		this.setBounds(postThat.getX(), postThat.getY(), postThat.getWidth(), postThat.getHeight());
		this.content.setText(postThat.getContent());
		this.id = postThat.getId();
	}

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
		this.content = new JTextArea();
		this.getContentPane().add(this.content);
	}
}
