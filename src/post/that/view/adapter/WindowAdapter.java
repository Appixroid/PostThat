package post.that.view.adapter;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public interface WindowAdapter extends WindowListener
{
	@Override
	public default void windowActivated(WindowEvent event)
	{
	}

	@Override
	public default void windowClosed(WindowEvent event)
	{
	}

	@Override
	public default void windowClosing(WindowEvent event)
	{
	}

	@Override
	public default void windowDeactivated(WindowEvent event)
	{
	}

	@Override
	public default void windowDeiconified(WindowEvent event)
	{
	}

	@Override
	public default void windowIconified(WindowEvent event)
	{
	}

	@Override
	public default void windowOpened(WindowEvent event)
	{
	}
}
