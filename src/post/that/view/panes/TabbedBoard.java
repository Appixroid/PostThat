package post.that.view.panes;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import post.that.view.adapter.MouseAdapter;
import post.that.view.listeners.board.BoardEvent;
import post.that.view.listeners.board.BoardListener;
import post.that.view.preferences.Preferences;
import post.that.view.ressources.Images;

public class TabbedBoard extends JPanel implements BoardListener
{
	private static final String OPEN_BOARDS_SETTINGS = "OPEN_BOARDS";
	private static final Icon MODIFIED_ICON = Images.NEW_ICON.getScaledIcon(16, 16);

	private static final long serialVersionUID = -9180673147053998822L;

	private JTabbedPane tabs = new JTabbedPane();

	public TabbedBoard()
	{
		this.setLayout(new BorderLayout());
		this.add(this.tabs, BorderLayout.CENTER);

		this.openSavedBoards();
	}

	public void addEmpty()
	{
		this.add(new BoardPane());
	}

	public void open()
	{
		File boardFile = BoardFileDialog.getLoadFile(this);
		if(boardFile.exists())
		{
			this.add(new BoardPane(boardFile));
		}
	}

	public void add(BoardPane board)
	{
		board.addBoardListener(this);
		
		this.tabs.addTab(board.getDisplaySource(), board);		
		this.tabs.setTabComponentAt(this.getBoards().indexOf(board), new BoardTabComponent(board));
	}

	public void addAll(Collection<BoardPane> boards)
	{
		for(BoardPane board : boards)
		{
			this.add(board);
		}
	}

	public BoardPane getCurrentBoard()
	{
		return (BoardPane) this.tabs.getSelectedComponent();
	}

	public BoardPane getBoard(int index)
	{
		return (BoardPane) this.tabs.getComponentAt(index);
	}

	public List<BoardPane> getBoards()
	{
		List<BoardPane> boards = new ArrayList<BoardPane>();

		for(int i = 0; i < this.tabs.getTabCount(); i++)
		{
			boards.add(this.getBoard(i));
		}

		return boards;
	}

	public boolean closeAll()
	{
		boolean allClosed = true;

		for(BoardPane board : this.getBoards())
		{
			allClosed &= this.close(board);
		}

		return allClosed;
	}

	public boolean close(BoardPane board)
	{
		if(!board.isSaved())
		{
			if(board.save())
			{
				this.tabs.remove(board);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			this.tabs.remove(board);
			return true;
		}
	}

	public boolean saveAll()
	{
		boolean allSaved = true;

		for(BoardPane board : this.getBoards())
		{
			allSaved &= board.save();
		}

		return allSaved;
	}

	public void saveOpenTabsSources()
	{
		Preferences.getInstance().setCollection(TabbedBoard.OPEN_BOARDS_SETTINGS, this.getTabsSources());
	}

	public Collection<String> getTabsSources()
	{
		Collection<String> sources = new ArrayList<String>();

		for(BoardPane board : this.getBoards())
		{
			String source = board.getSource();
			if(source.length() > 0)
			{
				sources.add(source);
			}
		}

		return sources;
	}

	@Override
	public void saved(BoardEvent event)
	{
		this.setSaved((BoardPane) event.getSource());
	}

	@Override
	public void changed(BoardEvent event)
	{
		this.setModified((BoardPane) event.getSource());
	}

	public void setSaved(BoardPane board)
	{
		this.getTabComponentFor(board).setSaved();
	}

	public void setModified(BoardPane board)
	{
		this.getTabComponentFor(board).setModified();
	}

	private void openSavedBoards()
	{
		Collection<String> savedBoards = Preferences.getInstance().getCollection(TabbedBoard.OPEN_BOARDS_SETTINGS);

		if(savedBoards.size() > 0)
		{
			this.addAllBoardsFromPath(savedBoards);
		}
		else
		{
			this.addEmpty();
		}
	}

	private void addAllBoardsFromPath(Collection<String> savedBoards)
	{
		List<BoardPane> boards = savedBoards.stream().map(boardPath -> {
			return new File(boardPath);
		}).filter(file -> {
			return file.exists();
		}).map(file -> {
			return new BoardPane(file);
		}).collect(Collectors.toList());

		this.addAll(boards);
	}
	
	private BoardTabComponent getTabComponentFor(BoardPane board)
	{
		return (BoardTabComponent) this.tabs.getTabComponentAt(this.getBoards().indexOf(board));
	}
	
	private class BoardTabComponent extends JPanel implements MouseAdapter
	{
		private static final long serialVersionUID = -6959815063623471506L;
				
		private BoardPane contentPane;
		
		private JLabel tabIcon;
		private JLabel tabTitle;
		private JLabel tabButton;
		
		public BoardTabComponent(BoardPane board)
		{			
			this.contentPane = board;
			this.setOpaque(false);
			this.build(board);
		}

		private void build(BoardPane board)
		{
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
			
			this.tabIcon = new JLabel();
			this.add(this.tabIcon);
			
			this.add(Box.createRigidArea(new Dimension(8, 0)));
			
			this.tabTitle = new JLabel(board.getDisplaySource());
			this.add(this.tabTitle);

			this.add(Box.createRigidArea(new Dimension(8, 0)));
			
			this.tabButton = new JLabel(Images.CLOSE_ICON.getScaledIcon(16, 16));
			this.tabButton.addMouseListener(this);
			this.tabButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.add(this.tabButton);
			
		}
		
		public void setSaved()
		{
			this.tabIcon.setIcon(null);
			this.tabTitle.setText(this.contentPane.getDisplaySource());
		}
		
		public void setModified()
		{
			this.tabIcon.setIcon(TabbedBoard.MODIFIED_ICON);
		}
		
		@Override
		public void mouseClicked(MouseEvent event)
		{
			TabbedBoard.this.close(this.contentPane);
		}
	}
}
