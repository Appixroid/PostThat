package post.that.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PostThatBoard
{
	public static final String ROOT_ELEMENT_NAME = "board";

	private static final String SAVE_FILE_PATH = System.getProperty("user.home") + File.separator + ".post-that" + File.separator + "board.pbs";
	private static final File SAVE_FILE = new File(PostThatBoard.SAVE_FILE_PATH);

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

	public boolean resize(String id, int newWidth, int newHeight)
	{
		PostThat postThat = this.get(id);
		if(postThat != null)
		{
			postThat.resize(newWidth, newHeight);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean move(String id, int newX, int newY)
	{
		PostThat postThat = this.get(id);
		if(postThat != null)
		{
			postThat.move(newX, newY);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean changeContent(String id, String content)
	{
		PostThat postThat = this.get(id);
		if(postThat != null)
		{
			postThat.setContent(content);
			return true;
		}
		else
		{
			return false;
		}
	}

	public Collection<PostThat> getPostThats()
	{
		return this.postThats.values();
	}

	public boolean save()
	{
		try
		{
			this.createSaveFile();

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			transformer.transform(new DOMSource(PostThatBoard.toXML(this)), new StreamResult(new FileWriter(PostThatBoard.SAVE_FILE)));

			return true;
		}
		catch(IOException | TransformerException | TransformerFactoryConfigurationError e)
		{
			return false;
		}
	}

	@Override
	public String toString()
	{
		return this.getPostThats().stream().map((postThat) -> {
			return postThat.toString();
		}).collect(Collectors.joining(", ", "[", "]"));
	}

	private void createSaveFile() throws IOException
	{
		if(!PostThatBoard.SAVE_FILE.exists())
		{
			if(!PostThatBoard.SAVE_FILE.getParentFile().mkdirs())
			{
				throw new IOException("Dirs " + PostThatBoard.SAVE_FILE.getParent() + " cannot be created");
			}

			if(!PostThatBoard.SAVE_FILE.createNewFile())
			{
				throw new IOException("File " + PostThatBoard.SAVE_FILE.getAbsolutePath() + " already");
			}
		}
	}

	public static PostThatBoard getSavedBoard()
	{
		PostThatBoard board = new PostThatBoard();

		if(PostThatBoard.SAVE_FILE.exists())
		{
			board.addAll(PostThatBoard.parse(PostThatBoard.SAVE_FILE));
		}

		return board;
	}

	private static Collection<PostThat> parse(File xmlFile)
	{
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return PostThatBoard.readAll(builder.parse(xmlFile));
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
			NodeList list = root.getElementsByTagName(PostThat.POST_THAT_ELEMENT_NAME);

			for(int i = 0; i < list.getLength(); i++)
			{
				postThats.add(PostThat.read(list.item(i)));
			}

			return postThats;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	private static Document toXML(PostThatBoard board)
	{
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document boardDocument = builder.newDocument();
			Element root = boardDocument.createElement(PostThatBoard.ROOT_ELEMENT_NAME);

			for(PostThat postThat : board.getPostThats())
			{
				root.appendChild(PostThat.toXML(boardDocument, postThat));
			}

			boardDocument.appendChild(root);
			return boardDocument;
		}
		catch(ParserConfigurationException e)
		{
			return null;
		}
	}
}
