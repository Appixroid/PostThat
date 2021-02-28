package post.that.view.lookAndFeel;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class PostThatUiManager
{
	public static void setBestLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new FlatLightLaf());
		}
		catch(Exception e)
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch(Exception e1)
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				}
				catch(Exception e2)
				{
				}
			}
		}
	}
}
