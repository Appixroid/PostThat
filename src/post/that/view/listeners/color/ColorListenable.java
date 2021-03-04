package post.that.view.listeners.color;

import java.util.Collection;

public interface ColorListenable
{
	public abstract Collection<ColorListener> getColorListeners();

	public default void addColorListener(ColorListener listener)
	{
		this.getColorListeners().add(listener);
	}

	public default void removeColorListener(ColorListener listener)
	{
		this.getColorListeners().remove(listener);
	}

	public default void notifyColorChangedToAll(Object source)
	{
		ColorEvent event = new ColorEvent(source);
		for(ColorListener listener : this.getColorListeners())
		{
			listener.colorValueChanged(event);
		}
	}
}
