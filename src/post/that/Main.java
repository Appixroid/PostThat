package post.that;

import javax.swing.SwingUtilities;

import post.that.view.MainFrame;
import post.that.view.lookAndFeel.PostThatUiManager;

public class Main
{
	public static void main(String[] args)
	{
		PostThatUiManager.setBestLookAndFeel();

		SwingUtilities.invokeLater(() -> {
			new MainFrame().display();
		});
	}
}
