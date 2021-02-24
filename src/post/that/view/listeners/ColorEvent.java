package post.that.view.listeners;

import java.awt.AWTEvent;

public class ColorEvent extends AWTEvent
{
	private static final long serialVersionUID = -5712807287399073552L;
	private static final int ID = 9510;
	
	public ColorEvent(Object source)
	{
		super(source, ID);
	}
}
