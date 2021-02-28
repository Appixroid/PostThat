package post.that.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import post.that.utils.Translation.Internationalization;
import post.that.view.adapter.WindowAdapter;
import post.that.view.panes.TabbedBoard;
import post.that.view.preferences.Preferences;
import post.that.view.ressources.Images;

public class MainFrame extends JFrame implements WindowAdapter
{
	private static final long serialVersionUID = -5628864249328436672L;
	private static final String TITLE = Internationalization.get("APP_TITLE");
	private static final Dimension DEFAULT_SIZE = new Dimension(720, 640);

	private Preferences preferences = Preferences.getInstance();
	private JToolBar toolBar;
	private TabbedBoard tabs;

	public MainFrame()
	{
		this.setup();
		this.build();
	}

	public void display()
	{
		this.setVisible(true);
	}

	@Override
	public void windowClosing(WindowEvent event)
	{
		if(this.tabs.saveAll())
		{
			System.exit(0);
		}
		else
		{
			int userSelection = JOptionPane.showConfirmDialog(this, Internationalization.get("CONFIRM_EXIT_WITH_SAVE_ERROR"), Internationalization.get("SAVE_ERROR"), JOptionPane.ERROR_MESSAGE);
			if(userSelection == JOptionPane.YES_OPTION)
			{
				System.exit(1);
			}
		}
	}

	private void setup()
	{
		this.setTitle(MainFrame.TITLE);
		this.setIconImage(Images.NOTE_ICON.getDefaultImage());

		this.setSize(MainFrame.DEFAULT_SIZE);

		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(this);
	}

	private void build()
	{
		this.toolBar = this.buildToolBar();
		this.tabs = new TabbedBoard();
		this.tabs.addEmptyBoard();

		this.setJMenuBar(this.buildMenuBar());

		this.setLayout(new BorderLayout());
		this.add(this.toolBar, BorderLayout.NORTH);
		this.add(this.tabs, BorderLayout.CENTER);

	}

	private JMenuBar buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(Internationalization.get("FILE"));
		fileMenu.setMnemonic(KeyEvent.VK_P);
		menuBar.add(fileMenu);

		JMenuItem addPostThatItem = new JMenuItem(Internationalization.get("CREATE_POST_THAT"));
		addPostThatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		addPostThatItem.setIcon(Images.NOTE_ICON.getScaledIcon(16, 16));
		addPostThatItem.addActionListener(event -> {
			this.tabs.getCurrentBoard().createEmptyPostThat();
		});
		fileMenu.add(addPostThatItem);

		JMenuItem clearItem = new JMenuItem(Internationalization.get("CLEAR_BOARD"));
		clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
		clearItem.setIcon(Images.CLEAR_ICON.getScaledIcon(16, 16));
		clearItem.addActionListener(event -> {
			this.tabs.getCurrentBoard().clear();
		});
		fileMenu.add(clearItem);

		JMenuItem saveItem = new JMenuItem(Internationalization.get("SAVE"));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveItem.setIcon(Images.SAVE_ICON.getScaledIcon(16, 16));
		saveItem.addActionListener(event -> {
			if(!this.tabs.getCurrentBoard().save())
			{
				JOptionPane.showMessageDialog(this, Internationalization.get("UNABLE_TO_SAVE_BOARD"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		});
		fileMenu.add(saveItem);

		JMenu viewMenu = new JMenu(Internationalization.get("VIEW"));
		viewMenu.setMnemonic(KeyEvent.VK_P);
		menuBar.add(viewMenu);

		JCheckBoxMenuItem menuBarVisibility = new JCheckBoxMenuItem(Internationalization.get("SHOW_MENU_BAR"));
		menuBarVisibility.setSelected(preferences.getBoolean("SHOW_MENU_BAR", true));
		menuBarVisibility.addActionListener(event -> {
			boolean selected = menuBarVisibility.isSelected();
			menuBar.setVisible(selected);
			preferences.setBoolean("SHOW_MENU_BAR", selected);
			if(!preferences.save())
			{
				JOptionPane.showMessageDialog(this, Internationalization.get("UNABLE_TO_SAVE_PREFERENCES"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		});
		viewMenu.add(menuBarVisibility);

		JCheckBoxMenuItem toolBarVisibility = new JCheckBoxMenuItem(Internationalization.get("SHOW_TOOL_BAR"));
		toolBarVisibility.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		toolBarVisibility.setSelected(preferences.getBoolean("SHOW_TOOL_BAR", true));
		toolBarVisibility.addActionListener(event -> {
			boolean selected = toolBarVisibility.isSelected();
			MainFrame.this.toolBar.setVisible(selected);
			preferences.setBoolean("SHOW_TOOL_BAR", selected);
			if(!preferences.save())
			{
				JOptionPane.showMessageDialog(this, Internationalization.get("UNABLE_TO_SAVE_PREFERENCES"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		});
		viewMenu.add(toolBarVisibility);

		return menuBar;
	}

	private JToolBar buildToolBar()
	{
		JToolBar toolBar = new JToolBar(Internationalization.get("BOARD_MANAGEMENT"));

		JButton addPostThatButton = new JButton(Images.NOTE_ICON.getScaledIcon(16, 16));
		addPostThatButton.addActionListener(event -> {
			this.tabs.getCurrentBoard().createEmptyPostThat();
		});
		toolBar.add(addPostThatButton);

		JButton clearButton = new JButton(Images.CLEAR_ICON.getScaledIcon(16, 16));
		clearButton.addActionListener(event -> {
			this.tabs.getCurrentBoard().clear();
		});
		toolBar.add(clearButton);

		JButton saveButton = new JButton(Images.SAVE_ICON.getScaledIcon(16, 16));
		saveButton.addActionListener(event -> {
			if(!this.tabs.getCurrentBoard().save())
			{
				JOptionPane.showMessageDialog(this, Internationalization.get("UNABLE_TO_SAVE_BOARD"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		});
		toolBar.add(saveButton);

		toolBar.setVisible(Preferences.getInstance().getBoolean("SHOW_TOOL_BAR", true));

		return toolBar;
	}
}
