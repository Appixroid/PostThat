package post.that.view.panes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import post.that.view.listeners.board.BoardListenable;
import post.that.view.listeners.board.BoardListener;
import post.that.view.listeners.color.ColorEvent;
import post.that.view.listeners.color.ColorListener;
import post.that.view.ressources.Images;

public class BoardPane extends JDesktopPane implements InternalFrameAdapter, ComponentAdapter, TextListener, ColorListener, BoardListenable
{
	private static final long serialVersionUID = 838829298589001150L;
	private static final Image BACKGROUND = Images.BOARD_BACKGROUND.getDefaultImage();

	private List<BoardListener> listeners;
	private PostThatBoard board;

	public BoardPane()
	{
		this(new PostThatBoard());
	}

	public BoardPane(File sourceBoard)
	{
		this(PostThatBoard.getSavedBoard(sourceBoard));
	}
	
	public BoardPane(PostThatBoard board)
	{
		this.listeners = new ArrayList<BoardListener>();
		this.board = board;
		this.addAll(this.board.getPostThats());
	}
	
	@Override
	public List<BoardListener> getBoardListeners()
	{
		return this.listeners;
	}

	public boolean save()
	{
		boolean saved = false;
		
		if(this.board.isSaved())
		{
			saved = true;
		}
		else
		{
			boolean saveAchieved = this.board.save();
			if(!saveAchieved)
			{
				int userSelection = JOptionPane.showConfirmDialog(this, Internationalization.get("AUTOMATIC_SAVE_FAIL_KEEP_SAVING"), Internationalization.get("SAVE_ERROR"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if(userSelection == JOptionPane.YES_OPTION)
				{
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle(Internationalization.get("SAVE_BOARD"));

					if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
					{
						this.board.setSource(fileChooser.getSelectedFile());
						saved = this.save();
					}
					else
					{
						saved = false;
					}
				}
				else
				{
					saved = false;
				}
			}
			else
			{
				saved = true;
			}
		}
		
		if(saved)
		{
			this.notifyBoardSavedToAll(this);
		}
		
		return saved;
	}

	public void clear()
	{
		this.board.clear();

		for(JInternalFrame frame : this.getAllFrames())
		{
			this.getDesktopManager().closeFrame(frame);
		}
		
		this.notifyBoardChangedToAll(this);
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
		this.notifyBoardChangedToAll(this);
	}

	@Override
	public void componentMoved(ComponentEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.move(frame.getId(), frame.getX(), frame.getY());
		this.notifyBoardChangedToAll(this);
	}

	@Override
	public void componentResized(ComponentEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.resize(frame.getId(), frame.getWidth(), frame.getHeight());
		this.notifyBoardChangedToAll(this);
	}

	@Override
	public void textValueChanged(TextEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.changeContent(frame.getId(), frame.getText());
		this.notifyBoardChangedToAll(this);
	}

	@Override
	public void colorValueChanged(ColorEvent event)
	{
		PostThatFrame frame = (PostThatFrame) event.getSource();
		this.board.changeColor(frame.getId(), frame.getBackground());
		this.notifyBoardChangedToAll(this);
	}

	public void createEmptyPostThat()
	{
		PostThat postThat = new PostThat();
		this.board.add(postThat);
		this.add(postThat);
		this.notifyBoardChangedToAll(this);
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
	
	private void addAll(Collection<PostThat> postThats)
	{
		for(PostThat postThat : postThats)
		{
			this.add(postThat);
		}
	}

	private void add(PostThat postThat)
	{
		PostThatFrame postThatPane = new PostThatFrame(postThat);
		postThatPane.addInternalFrameListener(this);
		postThatPane.addComponentListener(this);
		postThatPane.addTextListener(this);
		postThatPane.addColorListener(this);

		this.add(postThatPane);
	}
}
