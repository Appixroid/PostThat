package post.that.view.lookAndFeel;

import javax.swing.BorderFactory;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import post.that.utils.Translation.Internationalization;
import post.that.view.ressources.Colors;
import post.that.view.ressources.Images;

public class PostThatUiManager
{
	public static void setBestLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new FlatLightLaf());

			UIManager.put("InternalFrame.icon", Images.POST_THAT_PANE_ICON.getScaledIcon(16, 16));
			UIManager.put("InternalFrame.closeIcon", Images.DELETE_ICON.getScaledIcon(16, 16));
			UIManager.put("InternalFrame.closeButtonToolTip", Internationalization.get("DELETE"));

			UIManager.put("InternalFrame.border", BorderFactory.createEmptyBorder());
			UIManager.put("InternalFrame.activeTitleBackground", Colors.YELLOW.toSwing());
			UIManager.put("InternalFrame.inactiveTitleBackground", Colors.YELLOW.toSwing());

			UIManager.put("TextArea.background", Colors.YELLOW.toSwing());
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
					e2.printStackTrace();
				}
			}
		}
	}
}
