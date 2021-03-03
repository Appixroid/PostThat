package post.that.view.listeners.board;

import java.util.List;

public interface BoardListenable
{
	public abstract List<BoardListener> getBoardListeners();

	public default void addBoardListener(BoardListener listener)
	{
		this.getBoardListeners().add(listener);
	}

	public default void removeBoardListener(BoardListener listener)
	{
		this.getBoardListeners().remove(listener);
	}

	public default void notifyBoardSavedToAll(Object source)
	{
		BoardEvent event = new BoardEvent(source);
		for(BoardListener listener : this.getBoardListeners())
		{
			listener.saved(event);
		}
	}

	public default void notifyBoardChangedToAll(Object source)
	{
		BoardEvent event = new BoardEvent(source);
		for(BoardListener listener : this.getBoardListeners())
		{
			listener.changed(event);
		}
	}
}
