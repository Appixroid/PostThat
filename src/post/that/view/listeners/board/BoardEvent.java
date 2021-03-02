package post.that.view.listeners.board;

import java.awt.AWTEvent;

public class BoardEvent extends AWTEvent
{
	private static final long serialVersionUID = -5778135537234784721L;
	private static final int ID = 5901;

	public BoardEvent(Object source)
	{
		super(source, BoardEvent.ID);
	}
}
