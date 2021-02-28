package post.that.view.panes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;

import post.that.model.PostThat;
import post.that.model.PostThatBoard;
import post.that.utils.Translation.Internationalization;
import post.that.view.adapter.ComponentAdapter;
import post.that.view.adapter.InternalFrameAdapter;
import post.that.view.listeners.ColorEvent;
import post.that.view.listeners.ColorListener;
import post.that.view.ressources.Images;

public class BoardPane extends JDesktopPane implements InternalFrameAdapter, ComponentAdapter, TextListener, ColorListener
{
	private static final long serialVersionUID = 838829298589001150L;
	private static final Image BACKGROUND = Images.BOARD_BACKGROUND.getDefaultImage();

	private PostThatBoard board;

	public BoardPane()
	{
		this(new PostThatBoard());
	}

	public BoardPane(PostThatBoard board)
	{
		this.board = board;
		for(PostThat postThat : this.board.getPostThats())
		{
			this.addPostThat(postThat);
		}
	}

	public boolean save()
	{
		if(!this.board.save())
		{
			int userSelection = JOptionPane.showConfirmDialog(this, Internationalization.get("AUTOMATIC_SAVE_FAIL_KEEP_SAVING"), Internationalization.get("SAVE_ERROR"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if(userSelection == JOptionPane.YES_OPTION)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle(Internationalization.get("SAVE_BOARD"));

				if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				{
					this.board.setSource(fileChooser.getSelectedFile());
					return this.save();
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
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

	@Override
	public void colorValueChanged(ColorEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.changeColor(frame.getId(), frame.getBackground());
	}

	public void createEmptyPostThat()
	{
		PostThat postThat = new PostThat();
		this.board.add(postThat);
		this.addPostThat(postThat);
	}

	public String getSource()
	{
		String source = this.board.getSourceName();
		if(source == null)
		{
			return Internationalization.get("NEW_BOARD");
		}
		else
		{
			return source;
		}
	}

	public boolean isSaved()
	{
		return this.board.isSaved();
	}

	private void addPostThat(PostThat postThat)
	{
		PostThatFrame postThatPane = new PostThatFrame(postThat);
		postThatPane.addInternalFrameListener(this);
		postThatPane.addComponentListener(this);
		postThatPane.addTextListener(this);
		postThatPane.addColorListener(this);

		this.add(postThatPane);
	}
}
