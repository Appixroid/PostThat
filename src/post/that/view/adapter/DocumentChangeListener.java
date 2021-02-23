package post.that.view.adapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface DocumentChangeListener extends DocumentListener
{
	@Override
	public default void changedUpdate(DocumentEvent e)
	{
		this.documentChanged(e);
	}

	@Override
	public default void removeUpdate(DocumentEvent e)
	{
		this.documentChanged(e);
	}

	@Override
	public default void insertUpdate(DocumentEvent e)
	{
		this.documentChanged(e);
	}

	public abstract void documentChanged(DocumentEvent e);
}
