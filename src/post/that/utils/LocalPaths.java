package post.that.utils;

import java.io.File;

public enum LocalPaths
{
	POST_THAT_DIRECTORY(System.getProperty("user.home") + File.separator + ".post-that"),
	CONFIG_FILE(POST_THAT_DIRECTORY + File.separator + "settings.conf");

	private final String path;

	private LocalPaths(String path)
	{
		this.path = path;
	}

	public LocalFile toLocalFile()
	{
		return new LocalFile(this.path);
	}

	@Override
	public String toString()
	{
		return this.path;
	}
}
