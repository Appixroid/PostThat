package post.that.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PostThatBoard
{
	private static final String SAVE_FILE = System.getProperty("user.home") + File.pathSeparator + ".post-that" + File.pathSeparator + "board.pbs";

	private static final String ROOT_ELEMENT_NAME = "board";
	private static final String POST_THAT_ELEMENT_NAME = "post-that";

	private static final String POST_THAT_ID_ATTR = "id";
	private static final String POST_THAT_X_ATTR = "x";
	private static final String POST_THAT_Y_ATTR = "y";
	private static final String POST_THAT_WIDTH_ATTR = "width";
	private static final String POST_THAT_HEIGHT_ATTR = "height";

	private final Map<String, PostThat> postThats;

	private PostThatBoard(Collection<PostThat> postThats)
	{
		this();
		this.addAll(postThats);
	}

	private PostThatBoard()
	{
		this.postThats = new HashMap<String, PostThat>();
	}

	public boolean addAll(Collection<PostThat> postThats)
	{
		boolean allAdded = true;

		for(PostThat postThat : postThats)
		{
			allAdded &= this.add(postThat);
		}

		return allAdded;
	}

	public String add(int x, int y, int width, int height, String content)
	{
		PostThat postThat = new PostThat(x, y, width, height, content);

		if(this.add(postThat))
		{
			return postThat.getId();
		}
		else
		{
			return null;
		}
	}

	public boolean add(PostThat postThat)
	{
		if(!this.postThats.containsKey(postThat.getId()))
		{
			this.postThats.put(postThat.getId(), postThat);
			return true;
		}
		else
		{
			return false;
		}
	}

	public PostThat get(String id)
	{
		return this.postThats.get(id);
	}

	public PostThat remove(String id)
	{
		return this.postThats.remove(id);
	}

	public Collection<PostThat> getPostThats()
	{
		return this.postThats.values();
	}

	public static PostThatBoard getSavedBoard()
	{
		PostThatBoard board = new PostThatBoard();

		board.addAll(PostThatBoard.parse());

		return board;
	}

	private static Collection<PostThat> parse()
	{
		try
		{
			File file = new File(PostThatBoard.SAVE_FILE);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			if(file.exists())
			{
				return PostThatBoard.readAll(builder.parse(file));
			}
			else
			{
				return Collections.emptyList();
			}
		}
		catch(ParserConfigurationException | SAXException | IOException e)
		{
			return Collections.emptyList();
		}
	}

	private static Collection<PostThat> readAll(Document boardDocument)
	{
		Element root = boardDocument.getDocumentElement();

		if(PostThatBoard.ROOT_ELEMENT_NAME.equals(root.getTagName()))
		{
			Collection<PostThat> postThats = new ArrayList<PostThat>();
			NodeList list = root.getElementsByTagName(PostThatBoard.POST_THAT_ELEMENT_NAME);

			for(int i = 0; i < list.getLength(); i++)
			{
				postThats.add(PostThatBoard.read(list.item(i)));
			}

			return postThats;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	private static PostThat read(Node node)
	{
		NamedNodeMap attributes = node.getAttributes();

		String id = attributes.getNamedItem(PostThatBoard.POST_THAT_ID_ATTR).getNodeValue();

		int x = Integer.parseInt(attributes.getNamedItem(PostThatBoard.POST_THAT_X_ATTR).getNodeValue());
		int y = Integer.parseInt(attributes.getNamedItem(PostThatBoard.POST_THAT_Y_ATTR).getNodeValue());
		int width = Integer.parseInt(attributes.getNamedItem(PostThatBoard.POST_THAT_WIDTH_ATTR).getNodeValue());
		int height = Integer.parseInt(attributes.getNamedItem(PostThatBoard.POST_THAT_HEIGHT_ATTR).getNodeValue());

		String content = node.getTextContent();

		PostThat postThat = new PostThat(id, x, y, width, height, content);

		return postThat;
	}
}
