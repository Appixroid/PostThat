package post.that.view.listeners.board;

import java.util.List;

public interface BoardListenable
{
	public abstract List<BoardListener> getBoardListeners();
	
	public default void addBoardListener(BoardListener listener)
	{
		this.getBoardListeners().add(listener);
	}
	
	public default void notifyBoardSavedToAll(Object source)
	{
		for(BoardListener listener : this.getBoardListeners())
		{
			listener.saved(new BoardEvent(source));
		}
	}
	
	public default void notifyBoardChangedToAll(Object source)
	{
		for(BoardListener listener : this.getBoardListeners())
		{
			listener.changed(new BoardEvent(source));
		}
	}
}
