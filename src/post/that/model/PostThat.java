package post.that.model;

import java.awt.Color;
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
	private static final String POST_THAT_COLOR_ATTR = "color";

	private static final String CHARS_OF_IDS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_?!.,%$€.";
	private static final int DEFAULT_SIZE = 250;
	
	public static final Color DEFAULT_COLOR = new Color(255, 241, 118);


	private String id;

	private int x;
	private int y;

	private int width;
	private int height;

	private Color color;
	
	private String content;

	public PostThat()
	{
		this(0, 0, PostThat.DEFAULT_SIZE, PostThat.DEFAULT_SIZE, "", DEFAULT_COLOR);
	}

	public PostThat(int x, int y, int width, int height, String content, Color color)
	{
		this(PostThat.generateId(), x, y, width, height, content, color);
	}

	public PostThat(String id, int x, int y, int width, int height, String content, Color color)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
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
	
	public Color getColor()
	{
		return this.color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
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

		String color = attributes.getNamedItem(PostThat.POST_THAT_COLOR_ATTR).getNodeValue();
		String[] colorComponents = color.split(", ");
		int r = Integer.parseInt(colorComponents[0]);
		int g = Integer.parseInt(colorComponents[1]);
		int b = Integer.parseInt(colorComponents[2]);
		
		String content = node.getTextContent();

		PostThat postThat = new PostThat(id, x, y, width, height, content, new Color(r, g, b));

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
		
		Attr colorAttribute = document.createAttribute(PostThat.POST_THAT_COLOR_ATTR);
		Color color = postThat.getColor();
		colorAttribute.setNodeValue(Integer.toString(color.getRed()) + ", " + Integer.toString(color.getGreen()) + ", " + Integer.toString(color.getBlue()));
		attributes.setNamedItem(colorAttribute);

		postThatElement.setTextContent(postThat.content);

		return postThatElement;
	}
}
