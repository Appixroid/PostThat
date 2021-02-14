package post.that.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFile
{
	private String path;
	
	public LocalFile(String path)
	{
		this.path = path;
	}
	
	public URL toURL()
	{
		try
		{
			return new URL(new URL("file:"), path);
		}
		catch(MalformedURLException e)
		{
			throw new InvalidPathException(this.path, e.getMessage());
		}
	}
	
	public URI toURI()
	{
		try
		{
			return new URI(this.path);
		}
		catch(URISyntaxException e)
		{
			throw new InvalidPathException(this.path, e.getMessage());
		}
	}
	
	public Path toPath()
	{
		return Paths.get(this.path);
	}
	
	public File asFile()
	{
		return new File(this.path);
	}
	
	public static URL parseURL(String path)
	{
		return new LocalFile(path).toURL();
	}
	
	public static URI parseURI(String path)
	{
		return new LocalFile(path).toURI();
	}
	
	public static Path parsePath(String path)
	{
		return new LocalFile(path).toPath();
	}
	
	public static File getFile(String path)
	{
		return new LocalFile(path).asFile();
	}
}
