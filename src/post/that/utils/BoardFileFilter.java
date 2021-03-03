package post.that.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import post.that.utils.Translation.Internationalization;

public class BoardFileFilter extends FileFilter
{
	private static final String POST_THAT_BOARD_FILE_EXTENSION = "pbx";
	@Override
	public boolean accept(File file)
	{
		if(file.isDirectory())
		{
			return true;
		}
		else
		{
			return file.getName().toLowerCase().endsWith("." + POST_THAT_BOARD_FILE_EXTENSION);
		}
	}

	@Override
	public String getDescription()
	{
		return Internationalization.get("POST_THAT_BOARD_FILE") + " (*." + POST_THAT_BOARD_FILE_EXTENSION + ")";
	}
}
