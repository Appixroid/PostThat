package post.that.view.lookAndFeel;

import java.io.IOException;
import java.text.ParseException;
import java.util.MissingResourceException;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

import post.that.utils.LocalFile;

public class PostThatUiManager
{
	private static final String POSTTHAT_LOOK_AND_FEEL_PATH = "./assets/postit_look_and_feel.xml";

	public static LookAndFeel getPostThatLookAndFeel() throws MissingResourceException
	{
		
		try
		{
			SynthLookAndFeel laf = new SynthLookAndFeel();
			laf.load(LocalFile.parseURL(POSTTHAT_LOOK_AND_FEEL_PATH));
			return laf;
		}
		catch(ParseException | IOException e)
		{
			throw new MissingResourceException("Cannot load PostThat Look and Feel", SynthLookAndFeel.class.getName(), POSTTHAT_LOOK_AND_FEEL_PATH);
		}
		
	}
	
	public static void setBestLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(PostThatUiManager.getPostThatLookAndFeel());
		}
		catch (MissingResourceException | UnsupportedLookAndFeelException e)
		{
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1)
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				}
				catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e2)
				{
					e2.printStackTrace();
				}
			}
		}
	}
}
