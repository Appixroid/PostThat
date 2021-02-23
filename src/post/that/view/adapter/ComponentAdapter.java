package post.that.view.adapter;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public interface ComponentAdapter extends ComponentListener
{
	@Override
	public default void componentHidden(ComponentEvent event)
	{
	}

	@Override
	public default void componentMoved(ComponentEvent event)
	{
	}

	@Override
	public default void componentResized(ComponentEvent event)
	{
	}

	@Override
	public default void componentShown(ComponentEvent event)
	{
	}
}
