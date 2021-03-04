package post.that.view.panes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import post.that.model.PostThat;
import post.that.utils.Translation.Internationalization;
import post.that.view.adapter.MouseAdapter;
import post.that.view.listeners.color.ColorListenable;
import post.that.view.listeners.color.ColorListener;
import post.that.view.ressources.Images;

public class PostThatFrame extends JInternalFrame implements ColorListenable
{
	private static final long serialVersionUID = 1834391632998832385L;
	private static final Dimension DEFAULT_SIZE = new Dimension(128, 128);

	private List<ColorListener> listeners = new ArrayList<ColorListener>();

	private PostThatTitleBar titleBar;
	private String id;
	private JTextArea content;

	public PostThatFrame(PostThat postThat)
	{
		this();
		this.setBounds(postThat.getX(), postThat.getY(), postThat.getWidth(), postThat.getHeight());
		this.content.setText(postThat.getContent());
		this.id = postThat.getId();
		this.changeBackground(postThat.getColor());
	}

	public PostThatFrame()
	{
		this.setup();
		this.build();
	}

	public String getId()
	{
		return this.id;
	}

	public String getText()
	{
		return this.content.getText();
	}

	public void addTextListener(TextListener listener)
	{
		this.content.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent event)
			{
				listener.textValueChanged(new TextEvent(PostThatFrame.this, TextEvent.TEXT_VALUE_CHANGED));
			}

			@Override
			public void insertUpdate(DocumentEvent event)
			{
				listener.textValueChanged(new TextEvent(PostThatFrame.this, TextEvent.TEXT_VALUE_CHANGED));
			}

			@Override
			public void removeUpdate(DocumentEvent event)
			{
				listener.textValueChanged(new TextEvent(PostThatFrame.this, TextEvent.TEXT_VALUE_CHANGED));
			}
		});
	}

	@Override
	public List<ColorListener> getColorListeners()
	{
		return this.listeners;
	}

	public void changeBackground(Color bg)
	{
		this.setBackground(bg);
		this.titleBar.setBackground(bg);
		this.content.setBackground(bg);

		this.notifyColorChangedToAll(this);
	}

	private void setup()
	{
		this.setSize(PostThatFrame.DEFAULT_SIZE);

		this.setResizable(true);
		this.setClosable(true);

		this.setVisible(true);
	}

	private void build()
	{
		this.hideFrameTitleBar();

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		this.titleBar = new PostThatTitleBar();
		contentPane.add(this.titleBar, BorderLayout.NORTH);

		this.content = new JTextArea();
		contentPane.add(this.content, BorderLayout.CENTER);

		this.setBorder(BorderFactory.createEmptyBorder());
	}

	private void hideFrameTitleBar()
	{
		BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
		ui.setNorthPane(null);
	}

	private class PostThatTitleBar extends JPanel implements MouseMotionListener
	{
		private static final long serialVersionUID = -2974103250560115326L;

		private Point dragOffset;

		public PostThatTitleBar()
		{
			this.addMouseMotionListener(this);
			this.build();
		}

		@Override
		public void mouseMoved(MouseEvent event)
		{
			Point origin = PostThatFrame.this.getLocationOnScreen();
			Point current = event.getLocationOnScreen();

			this.dragOffset = new Point(current.x - origin.x, current.y - origin.y);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}

		@Override
		public void mouseDragged(MouseEvent event)
		{
			Point windowOnScreen = PostThatFrame.this.getParent().getLocationOnScreen();
			Point mouseOnScreen = event.getLocationOnScreen();
			Point position = new Point(mouseOnScreen.x - windowOnScreen.x - this.dragOffset.x, mouseOnScreen.y - windowOnScreen.y - this.dragOffset.y);

			PostThatFrame.this.setLocation(position);
		}

		private void build()
		{
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

			this.add(this.createMoveLabel());
			this.add(Box.createHorizontalGlue());
			this.add(this.createChangeColorLabel());
			this.add(this.createDeleteLabel());

			this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		}

		private JLabel createMoveLabel()
		{
			return new JLabel(Images.PIN_ICON.getScaledIcon(16, 16));
		}

		private JLabel createChangeColorLabel()
		{
			JLabel changeColor = new JLabel(Images.PICKER_ICON.getScaledIcon(16, 16));
			changeColor.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent event)
				{
					PostThatTitleBar.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseClicked(MouseEvent event)
				{
					Color selectedColor = JColorChooser.showDialog(PostThatFrame.this, Internationalization.get("CHANGE_COLOR"), PostThatFrame.this.getBackground());
					PostThatFrame.this.changeBackground(selectedColor);
				}
			});
			return changeColor;
		}

		private JLabel createDeleteLabel()
		{
			JLabel delete = new JLabel(Images.TRASH_ICON.getScaledIcon(16, 16));
			delete.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent event)
				{
					PostThatTitleBar.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseClicked(MouseEvent event)
				{
					PostThatFrame.this.doDefaultCloseAction();
				}
			});
			return delete;
		}

	}
}
