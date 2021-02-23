package post.that.model;

import java.util.Random;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class PostThat
{
	public static final String POST_THAT_ELEMENT_NAME = "post-that";

	public static final String POST_THAT_ID_ATTR = "id";
	public static final String POST_THAT_X_ATTR = "x";
	public static final String POST_THAT_Y_ATTR = "y";
	public static final String POST_THAT_WIDTH_ATTR = "width";
	public static final String POST_THAT_HEIGHT_ATTR = "height";

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

	@Override
	public String toString()
	{
		return "{" + this.id + " : " + this.content + "}";
	}

	protected static String generateId()
	{
		String id = "";
		Random rand = new Random();

		for(int i = 0; i < 64; i++)
		{
			id += PostThat.CHARS_OF_IDS.charAt(rand.nextInt(PostThat.CHARS_OF_IDS.length()));
		}

		return id;
	}

	public static PostThat read(Node node)
	{
		NamedNodeMap attributes = node.getAttributes();

		String id = attributes.getNamedItem(PostThat.POST_THAT_ID_ATTR).getNodeValue();

		int x = Integer.parseInt(attributes.getNamedItem(PostThat.POST_THAT_X_ATTR).getNodeValue());
		int y = Integer.parseInt(attributes.getNamedItem(PostThat.POST_THAT_Y_ATTR).getNodeValue());
		int width = Integer.parseInt(attributes.getNamedItem(PostThat.POST_THAT_WIDTH_ATTR).getNodeValue());
		int height = Integer.parseInt(attributes.getNamedItem(PostThat.POST_THAT_HEIGHT_ATTR).getNodeValue());

		String content = node.getTextContent();

		PostThat postThat = new PostThat(id, x, y, width, height, content);

		return postThat;
	}

	public static Node toXML(Document document, PostThat postThat)
	{
		Node postThatElement = document.createElement(PostThat.POST_THAT_ELEMENT_NAME);
		NamedNodeMap attributes = postThatElement.getAttributes();

		Attr idAttribute = document.createAttribute(PostThat.POST_THAT_ID_ATTR);
		idAttribute.setNodeValue(postThat.getId());
		attributes.setNamedItem(idAttribute);

		Attr xAttribute = document.createAttribute(PostThat.POST_THAT_X_ATTR);
		xAttribute.setNodeValue(Integer.toString(postThat.getX()));
		attributes.setNamedItem(xAttribute);

		Attr yAttribute = document.createAttribute(PostThat.POST_THAT_Y_ATTR);
		yAttribute.setNodeValue(Integer.toString(postThat.getY()));
		attributes.setNamedItem(yAttribute);

		Attr widthAttribute = document.createAttribute(PostThat.POST_THAT_WIDTH_ATTR);
		widthAttribute.setNodeValue(Integer.toString(postThat.getWidth()));
		attributes.setNamedItem(widthAttribute);

		Attr heightAttribute = document.createAttribute(PostThat.POST_THAT_HEIGHT_ATTR);
		heightAttribute.setNodeValue(Integer.toString(postThat.getHeight()));
		attributes.setNamedItem(heightAttribute);

		postThatElement.setTextContent(postThat.content);

		return postThatElement;
	}
}
