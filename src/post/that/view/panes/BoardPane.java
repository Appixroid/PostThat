package post.that.view.panes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;

import post.that.model.PostThat;
import post.that.model.PostThatBoard;
import post.that.view.adapter.ComponentAdapter;
import post.that.view.adapter.InternalFrameAdapter;
import post.that.view.ressources.Images;

public class BoardPane extends JDesktopPane implements InternalFrameAdapter, ComponentAdapter, TextListener
{
	private static final long serialVersionUID = 838829298589001150L;
	private static final Image BACKGROUND = Images.BOARD_BACKGROUND.getDefaultImage();

	private final PostThatBoard board = PostThatBoard.getSavedBoard();

	public BoardPane()
	{
		for(PostThat postThat : this.board.getPostThats())
		{
			this.addPostThat(postThat);
		}
	}

	public boolean save()
	{
		return this.board.save();
	}
	
	public void clear()
	{
		this.board.clear();
		
		for(JInternalFrame frame : this.getAllFrames())
		{
			this.getDesktopManager().closeFrame(frame);
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(BoardPane.BACKGROUND, 0, 0, null);
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.remove(frame.getId());
	}

	@Override
	public void componentMoved(ComponentEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.move(frame.getId(), frame.getX(), frame.getY());
	}

	@Override
	public void componentResized(ComponentEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.resize(frame.getId(), frame.getWidth(), frame.getHeight());
	}

	@Override
	public void textValueChanged(TextEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.changeContent(frame.getId(), frame.getText());
	}

	public void createEmptyPostThat()
	{
		PostThat postThat = new PostThat();
		this.board.add(postThat);
		this.addPostThat(postThat);
	}

	private void addPostThat(PostThat postThat)
	{
		PostThatFrame postThatPane = new PostThatFrame(postThat);
		postThatPane.addInternalFrameListener(this);
		postThatPane.addComponentListener(this);
		postThatPane.addTextListener(this);

		this.add(postThatPane);
	}
}
