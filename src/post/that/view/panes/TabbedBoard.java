package post.that.view.panes;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import post.that.view.listeners.board.BoardEvent;
import post.that.view.listeners.board.BoardListener;
import post.that.view.preferences.Preferences;
import post.that.view.ressources.Images;

public class TabbedBoard extends JPanel implements BoardListener
{
	private static final String OPEN_BOARDS_SETTINGS = "OPEN_BOARDS";

	private static final long serialVersionUID = -9180673147053998822L;

	private JTabbedPane tabs = new JTabbedPane();

	public TabbedBoard()
	{
		this.setLayout(new BorderLayout());
		this.add(this.tabs, BorderLayout.CENTER);

		this.openSavedBoards();
	}

	public void addEmptyBoard()
	{
		this.addBoard(new BoardPane());
	}

	public void openBoard()
	{
		File boardFile = BoardFileDialog.getLoadFile(this);
		if(boardFile != null)
		{
			this.addBoard(new BoardPane(boardFile));
		}
	}

	public void addBoard(BoardPane board)
	{
		this.tabs.addTab(board.getDisplaySource(), null, board);
		board.addBoardListener(this);
	}

	public void addAll(List<BoardPane> boards)
	{
		for(BoardPane board : boards)
		{
			this.addBoard(board);
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

	public void saveTabsSources()
	{
		Preferences.getInstance().setCollection(TabbedBoard.OPEN_BOARDS_SETTINGS, this.getTabsSources());
	}

	public List<String> getTabsSources()
	{
		List<String> sources = new ArrayList<String>();

		for(BoardPane board : this.getBoards())
		{
			String source = board.getSource();
			if(source != null)
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
		this.setIcon(board, null);
		this.tabs.setTitleAt(this.getBoards().indexOf(board), board.getDisplaySource());
	}

	public void setModified(BoardPane board)
	{
		this.setIcon(board, Images.NEW_ICON.getScaledIcon(16, 16));
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
			this.addEmptyBoard();
		}
	}

	private void addAllBoardsFromPath(Collection<String> savedBoards)
	{
		this.addAll(savedBoards.stream().map(boardPath -> {
			return new BoardPane(new File(boardPath));
		}).collect(Collectors.toList()));
	}

	private void setIcon(BoardPane board, Icon icon)
	{
		this.tabs.setIconAt(this.getBoards().indexOf(board), icon);
	}
}
