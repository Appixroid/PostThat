package post.that.view.panes;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import post.that.utils.Translation.Internationalization;
import post.that.view.listeners.board.BoardEvent;
import post.that.view.listeners.board.BoardListener;
import post.that.view.ressources.Images;

public class TabbedBoard extends JPanel implements BoardListener
{
	private static final long serialVersionUID = -9180673147053998822L;

	private JTabbedPane tabs = new JTabbedPane();

	public TabbedBoard()
	{
		this.setLayout(new BorderLayout());
		this.add(this.tabs, BorderLayout.CENTER);
	}

	public void addEmptyBoard()
	{
		this.addBoard(new BoardPane());
	}

	public void openBoard()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(Internationalization.get("OPEN_BOARD"));
		
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			this.addBoard(new BoardPane(chooser.getSelectedFile()));
		}
		
	}
	
	public void addBoard(BoardPane board)
	{
		this.tabs.addTab(board.getSource(), null, board);
		board.addBoardListener(this);
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
	}

	public void setModified(BoardPane board)
	{
		this.setIcon(board, Images.NEW_ICON.getScaledIcon(16, 16));
	}

	private void setIcon(BoardPane board, Icon icon)
	{
		this.tabs.setIconAt(this.getBoards().indexOf(board), icon);
	}
}
