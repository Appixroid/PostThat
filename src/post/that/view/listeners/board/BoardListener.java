package post.that.view.listeners.board;

public interface BoardListener
{
	public abstract void changed(BoardEvent event);

	public abstract void saved(BoardEvent event);
}
