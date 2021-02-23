package post.that.view.panes;

import java.awt.Dimension;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import post.that.model.PostThat;

public class PostThatFrame extends JInternalFrame
{
	private static final long serialVersionUID = 1834391632998832385L;
	private static final Dimension DEFAULT_SIZE = new Dimension(128, 128);

	private String id;
	private JTextArea content;

	public PostThatFrame(PostThat postThat)
	{
		this();
		this.setBounds(postThat.getX(), postThat.getY(), postThat.getWidth(), postThat.getHeight());
		this.content.setText(postThat.getContent());
		this.id = postThat.getId();
	}

	public PostThatFrame()
	{
		this.setup();
		this.build();
	}

	public String getId()
	{
		return this.id;
	}

	public String getText()
	{
		return this.content.getText();
	}

	public void addTextListener(TextListener listener)
	{
		this.content.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent event)
			{
				listener.textValueChanged(new TextEvent(PostThatFrame.this, TextEvent.TEXT_VALUE_CHANGED));
			}

			@Override
			public void insertUpdate(DocumentEvent event)
			{
				listener.textValueChanged(new TextEvent(PostThatFrame.this, TextEvent.TEXT_VALUE_CHANGED));
			}

			@Override
			public void removeUpdate(DocumentEvent event)
			{
				listener.textValueChanged(new TextEvent(PostThatFrame.this, TextEvent.TEXT_VALUE_CHANGED));
			}
		});
	}

	private void setup()
	{
		this.setSize(PostThatFrame.DEFAULT_SIZE);

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
