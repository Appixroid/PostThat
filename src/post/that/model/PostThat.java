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
	private static final String COLOR_COMPONENT_SEPARATOR = ", ";

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
		this(0, 0, PostThat.DEFAULT_SIZE, PostThat.DEFAULT_SIZE, "", PostThat.DEFAULT_COLOR);
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

		String id = PostThat.getAttributeValue(attributes, PostThat.POST_THAT_ID_ATTR);

		int x = PostThat.getIntegerAttributeValue(attributes, PostThat.POST_THAT_X_ATTR);
		int y = PostThat.getIntegerAttributeValue(attributes, PostThat.POST_THAT_Y_ATTR);
		int width = PostThat.getIntegerAttributeValue(attributes, PostThat.POST_THAT_WIDTH_ATTR);
		int height = PostThat.getIntegerAttributeValue(attributes, PostThat.POST_THAT_HEIGHT_ATTR);

		Color c = PostThat.getColorAttributeValue(attributes, PostThat.POST_THAT_COLOR_ATTR);

		String content = node.getTextContent();

		return new PostThat(id, x, y, width, height, content, c);
	}

	private static Color getColorAttributeValue(NamedNodeMap attributes, String name)
	{
		String color = PostThat.getAttributeValue(attributes, name);
		String[] colorComponents = color.split(PostThat.COLOR_COMPONENT_SEPARATOR);
		int r = Integer.parseInt(colorComponents[0]);
		int g = Integer.parseInt(colorComponents[1]);
		int b = Integer.parseInt(colorComponents[2]);
		return new Color(r, g, b);
	}

	private static int getIntegerAttributeValue(NamedNodeMap attributes, String name)
	{
		return Integer.parseInt(PostThat.getAttributeValue(attributes, name));
	}

	private static String getAttributeValue(NamedNodeMap attributes, String name)
	{
		return attributes.getNamedItem(name).getNodeValue();
	}

	public static Node toXML(Document document, PostThat postThat)
	{
		Node postThatElement = document.createElement(PostThat.POST_THAT_ELEMENT_NAME);
		NamedNodeMap attributes = postThatElement.getAttributes();

		attributes.setNamedItem(PostThat.createNamedAttribute(document, PostThat.POST_THAT_ID_ATTR, postThat.getId()));
		attributes.setNamedItem(PostThat.createNamedIntegerAttribute(document, PostThat.POST_THAT_X_ATTR, postThat.getX()));
		attributes.setNamedItem(PostThat.createNamedIntegerAttribute(document, PostThat.POST_THAT_Y_ATTR, postThat.getY()));
		attributes.setNamedItem(PostThat.createNamedIntegerAttribute(document, PostThat.POST_THAT_WIDTH_ATTR, postThat.getWidth()));
		attributes.setNamedItem(PostThat.createNamedIntegerAttribute(document, PostThat.POST_THAT_HEIGHT_ATTR, postThat.getHeight()));

		attributes.setNamedItem(PostThat.createNamedAttribute(document, PostThat.POST_THAT_COLOR_ATTR, PostThat.colorToAttrValue(postThat.getColor())));

		postThatElement.setTextContent(postThat.content);
		return postThatElement;
	}

	private static Attr createNamedIntegerAttribute(Document document, String name, int value)
	{
		return PostThat.createNamedAttribute(document, name, Integer.toString(value));
	}

	private static Attr createNamedAttribute(Document document, String name, String value)
	{
		Attr colorAttribute = document.createAttribute(name);
		colorAttribute.setNodeValue(value);
		return colorAttribute;
	}

	private static String colorToAttrValue(Color color)
	{
		return Integer.toString(color.getRed()) + PostThat.COLOR_COMPONENT_SEPARATOR + Integer.toString(color.getGreen()) + PostThat.COLOR_COMPONENT_SEPARATOR + Integer.toString(color.getBlue());
	}
}
