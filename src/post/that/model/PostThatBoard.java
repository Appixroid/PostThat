package post.that.model;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
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

	private final Map<String, PostThat> postThats;
	private boolean saved;
	private File source = new File("");

	public PostThatBoard(Collection<PostThat> postThats)
	{
		this();
		this.addAll(postThats);
		this.saved = true;
	}

	public PostThatBoard()
	{
		this.postThats = new HashMap<String, PostThat>();
		this.saved = true;
	}

	public void setSource(File source)
	{
		this.source = source;
	}

	public File getSource()
	{
		return this.source;
	}

	public String getFullSource()
	{
		return this.source.getAbsolutePath();
	}

	public String getSourceName()
	{
		return this.source.getName();
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

	public String add(int x, int y, int width, int height, String content, Color color)
	{
		PostThat postThat = new PostThat(x, y, width, height, content, color);

		if(this.add(postThat))
		{
			return postThat.getId();
		}
		else
		{
			return "";
		}
	}

	public boolean add(PostThat postThat)
	{
		if(!this.postThats.containsKey(postThat.getId()))
		{
			this.postThats.put(postThat.getId(), postThat);
			this.saved = false;

			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean safeGet(String id, Consumer<PostThat> toDo)
	{
		PostThat postThat = this.get(id);
		
		if(postThat == null)
		{
			return false;
		}
		else
		{
			toDo.accept(postThat);
			return true;
		}
	}

	public PostThat get(String id)
	{
		return this.postThats.get(id);
	}

	public boolean remove(String id)
	{
		return this.safeGet(id, postThat -> {
			this.postThats.remove(id, postThat);
			this.saved = false;
		});
	}

	public boolean resize(String id, int newWidth, int newHeight)
	{
		return this.safeGet(id, postThat -> {
			postThat.resize(newWidth, newHeight);
			this.saved = false;			
		});
	}

	public boolean move(String id, int newX, int newY)
	{
		return this.safeGet(id, postThat -> {
			postThat.move(newX, newY);
			this.saved = false;			
		});
	}

	public boolean changeContent(String id, String content)
	{
		return this.safeGet(id, postThat -> {
			postThat.setContent(content);
			this.saved = false;			
		});
	}

	public boolean changeColor(String id, Color color)
	{
		return this.safeGet(id, postThat -> {
			postThat.setColor(color);
			this.saved = false;			
		});
	}

	public Collection<PostThat> getPostThats()
	{
		return this.postThats.values();
	}

	public void clear()
	{
		if(this.postThats.size() > 0)
		{
			this.postThats.clear();
			this.saved = false;
		}
	}

	public boolean isSaved()
	{
		return this.saved;
	}

	public boolean save()
	{
		try
		{
			if(this.createSaveFile())
			{
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				transformer.transform(new DOMSource(PostThatBoard.toXML(this)), new StreamResult(new FileWriter(this.source)));

				this.saved = true;
				return true;
			}
			else
			{
				return false;
			}
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

	private boolean createSaveFile()
	{
		if(this.source.exists())
		{
			return true;
		}
		else
		{
			return this.createSourceFileWithParents();
		}
	}

	private boolean createSourceFileWithParents()
	{
		if(this.source.getParentFile() != null)
		{
			if(this.source.getParentFile().exists() || this.source.getParentFile().mkdirs())
			{
				try
				{
					return this.source.createNewFile();
				}
				catch(IOException e)
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	public static PostThatBoard getSavedBoard(File saveFile)
	{
		PostThatBoard board = new PostThatBoard();

		if(saveFile.exists())
		{
			board.addAll(PostThatBoard.parse(saveFile));
		}

		board.setSource(saveFile);
		board.saved = true;

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
