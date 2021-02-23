package post.that.view.adapter;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public interface InternalFrameAdapter extends InternalFrameListener
{
	@Override
	public default void internalFrameActivated(InternalFrameEvent event)
	{
	}

	@Override
	public default void internalFrameClosed(InternalFrameEvent event)
	{
	}

	@Override
	public default void internalFrameClosing(InternalFrameEvent event)
	{
	}

	@Override
	public default void internalFrameDeactivated(InternalFrameEvent event)
	{
	}

	@Override
	public default void internalFrameDeiconified(InternalFrameEvent event)
	{
	}

	@Override
	public default void internalFrameIconified(InternalFrameEvent event)
	{
	}

	@Override
	public default void internalFrameOpened(InternalFrameEvent event)
	{
	}
}
