package post.that.model;

import java.util.Random;

public class PostThat
{
	private static final String CHARS_OF_IDS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_?!.,%$€.";
	private static final int DEFAULT_SIZE = 250;

	private String id;

	private int x;
	private int y;

	private int width;
	private int height;

	private String content;

	public PostThat()
	{
		this(0, 0, PostThat.DEFAULT_SIZE, PostThat.DEFAULT_SIZE, "");
	}

	public PostThat(int x, int y, int width, int height, String content)
	{
		this(PostThat.generateId(), x, y, width, height, content);
	}

	public PostThat(String id, int x, int y, int width, int height, String content)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.content = content;
	}

	public String getId()
	{
		return this.id;
	}

	public void move(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public static String generateId()
	{
		String id = "";
		Random rand = new Random();

		for(int i = 0; i < 64; i++)
		{
			id += PostThat.CHARS_OF_IDS.charAt(rand.nextInt(PostThat.CHARS_OF_IDS.length()));
		}

		return id;
	}
}
