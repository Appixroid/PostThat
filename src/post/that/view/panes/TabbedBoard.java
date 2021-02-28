package post.that.view.panes;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import post.that.view.ressources.Images;

public class TabbedBoard extends JPanel
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
	
	public void addBoard(BoardPane board)
	{
		this.tabs.addTab(board.getSource(), Images.NEW_ICON.getScaledIcon(16, 16), board);
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
	
	public boolean saveAll()
	{
		boolean allSaved = true;
		
		for(BoardPane board : this.getBoards())
		{
			boolean saved = board.save();
			
			if(saved)
			{
				this.tabs.remove(board);
			}
			
			allSaved &= saved;
		}
		
		return allSaved;
	}
}
