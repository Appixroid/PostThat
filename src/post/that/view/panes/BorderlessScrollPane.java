package post.that.view.panes;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class BorderlessScrollPane extends JScrollPane
{	private static final long serialVersionUID = -2794589291332063907L;

	public BorderlessScrollPane(Component content)
	{
		super(content);
		this.setBorder(BorderFactory.createEmptyBorder());
	}
}
