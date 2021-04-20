package post.that.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

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
import post.that.view.panes.BoardPane;
import post.that.view.panes.TabbedBoard;
import post.that.view.preferences.Preferences;
import post.that.view.ressources.Images;

public class MainFrame extends JFrame implements WindowAdapter
{
	private static final long serialVersionUID = -5628864249328436672L;
	private static final String TITLE = Internationalization.get("APP_TITLE");
	private static final Dimension DEFAULT_SIZE = new Dimension(720, 640);

	private static final String SHOW_TOOL_BAR_PREFERENCE = "SHOW_TOOL_BAR";
	private static final String SHOW_MENU_BAR_PREFERENCE = "SHOW_MENU_BAR";

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
			this.tabs.saveOpenTabsSources();
			Preferences.getInstance().save();
			System.exit(0);
		}
		else
		{
			int userSelection = JOptionPane.showConfirmDialog(this, Internationalization.get("CONFIRM_EXIT_WITH_SAVE_ERROR"), Internationalization.get("SAVE_ERROR"), JOptionPane.ERROR_MESSAGE);
			if(userSelection == JOptionPane.YES_OPTION)
			{
				this.tabs.saveOpenTabsSources();
				Preferences.getInstance().save();
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

		this.setJMenuBar(this.buildMenuBar());

		this.setLayout(new BorderLayout());
		this.add(this.toolBar, BorderLayout.NORTH);
		this.add(this.tabs, BorderLayout.CENTER);
	}

	private JMenuBar buildMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(this.buildFileMenu());
		menuBar.add(this.buildBoardMenu());
		menuBar.add(this.buildViewMenu(menuBar));

		return menuBar;
	}

	private JMenu buildFileMenu()
	{
		JMenu fileMenu = new JMenu(Internationalization.get("FILE"));
		fileMenu.setMnemonic(KeyEvent.VK_P);

		fileMenu.add(this.createCreateItem());
		fileMenu.add(this.createOpenItem());
		fileMenu.add(this.createSaveItem());
		fileMenu.add(this.createSaveAllItem());
		fileMenu.add(this.createQuitItem());

		return fileMenu;
	}

	private JMenuItem createCreateItem()
	{
		JMenuItem createItem = new JMenuItem(Internationalization.get("CREATE_BOARD"));
		createItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		createItem.setIcon(Images.MODIFIED_ICON.getScaledIcon(16, 16));
		createItem.addActionListener(event -> {
			this.tabs.addEmpty();
		});
		return createItem;
	}

	private JMenuItem createOpenItem()
	{
		JMenuItem openItem = new JMenuItem(Internationalization.get("OPEN_BOARD"));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		openItem.setIcon(Images.IMPORT_ICON.getScaledIcon(16, 16));
		openItem.addActionListener(event -> {
			this.tabs.open();
		});
		return openItem;
	}

	private JMenuItem createSaveItem()
	{
		JMenuItem saveItem = new JMenuItem(Internationalization.get("SAVE"));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveItem.setIcon(Images.SAVE_ICON.getScaledIcon(16, 16));
		saveItem.addActionListener(event -> {
			this.saveCurrentBoard();
		});
		return saveItem;
	}

	private JMenuItem createSaveAllItem()
	{
		JMenuItem saveAllItem = new JMenuItem(Internationalization.get("SAVE_ALL"));
		saveAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		saveAllItem.setIcon(Images.SAVE_ALL_ICON.getScaledIcon(16, 16));
		saveAllItem.addActionListener(event -> {
			if(!this.tabs.saveAll())
			{
				JOptionPane.showMessageDialog(this, Internationalization.get("UNABLE_TO_SAVE_BOARD"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		});
		return saveAllItem;
	}
	
	private JMenuItem createQuitItem()
	{
		JMenuItem quitItem = new JMenuItem(Internationalization.get("QUIT"));
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		quitItem.setIcon(Images.EXIT_ICON.getScaledIcon(16, 16));
		quitItem.addActionListener(event -> {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
		return quitItem;
	}

	private JMenuItem buildBoardMenu()
	{
		JMenuItem boardMenu = new JMenu(Internationalization.get("BOARD"));
		boardMenu.setMnemonic(KeyEvent.VK_B);

		boardMenu.add(this.createCloseBoardItem());
		boardMenu.add(this.createAddPostThatItem());
		boardMenu.add(this.createClearItem());

		return boardMenu;
	}

	private JMenuItem createCloseBoardItem()
	{
		JMenuItem closeBoardItem = new JMenuItem(Internationalization.get("CLOSE_BOARD"));
		closeBoardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		closeBoardItem.setIcon(Images.CLOSE_ICON.getScaledIcon(16, 16));
		closeBoardItem.addActionListener(event -> {
			this.closeCurrentBoard();
		});
		return closeBoardItem;
	}

	private JMenuItem createAddPostThatItem()
	{
		JMenuItem addPostThatItem = new JMenuItem(Internationalization.get("CREATE_POST_THAT"));
		addPostThatItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		addPostThatItem.setIcon(Images.NOTE_ICON.getScaledIcon(16, 16));
		addPostThatItem.addActionListener(event -> {
			this.addEmptyToCurrentBoard();
		});
		return addPostThatItem;
	}

	private JMenuItem createClearItem()
	{
		JMenuItem clearItem = new JMenuItem(Internationalization.get("CLEAR_BOARD"));
		clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
		clearItem.setIcon(Images.CLEAR_ICON.getScaledIcon(16, 16));
		clearItem.addActionListener(event -> {
			this.clearCurrentBoard();
		});
		return clearItem;
	}

	private JMenu buildViewMenu(JMenuBar menuBar)
	{
		JMenu viewMenu = new JMenu(Internationalization.get("VIEW"));
		viewMenu.setMnemonic(KeyEvent.VK_P);

		viewMenu.add(this.createMenuBarVisibilityItem(menuBar));
		viewMenu.add(this.createToolBarVisibilityItem());

		return viewMenu;
	}

	private JCheckBoxMenuItem createMenuBarVisibilityItem(JMenuBar menuBar)
	{
		JCheckBoxMenuItem menuBarVisibility = new JCheckBoxMenuItem(Internationalization.get(MainFrame.SHOW_MENU_BAR_PREFERENCE));
		menuBarVisibility.setSelected(this.preferences.getBoolean(MainFrame.SHOW_MENU_BAR_PREFERENCE, true));
		menuBarVisibility.addActionListener(new CheckBoxMenuItemPreferenceListener(this, MainFrame.SHOW_MENU_BAR_PREFERENCE, selected -> {
			menuBar.setVisible(selected);
		}));
		return menuBarVisibility;
	}

	private JCheckBoxMenuItem createToolBarVisibilityItem()
	{
		JCheckBoxMenuItem toolBarVisibility = new JCheckBoxMenuItem(Internationalization.get(MainFrame.SHOW_TOOL_BAR_PREFERENCE));
		toolBarVisibility.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		toolBarVisibility.setSelected(this.preferences.getBoolean(MainFrame.SHOW_TOOL_BAR_PREFERENCE, true));
		toolBarVisibility.addActionListener(new CheckBoxMenuItemPreferenceListener(this, MainFrame.SHOW_TOOL_BAR_PREFERENCE, selected -> {
			MainFrame.this.toolBar.setVisible(selected);
		}));
		return toolBarVisibility;
	}

	private JToolBar buildToolBar()
	{
		JToolBar toolBar = new JToolBar(Internationalization.get("BOARD_MANAGEMENT"));

		toolBar.add(this.createAddPostThatButton());
		toolBar.add(this.createClearButton());
		toolBar.add(this.createSaveButton());
		toolBar.add(this.createCloseBoardButton());

		toolBar.setVisible(Preferences.getInstance().getBoolean(MainFrame.SHOW_TOOL_BAR_PREFERENCE, true));

		return toolBar;
	}

	private JButton createAddPostThatButton()
	{
		JButton addPostThatButton = new JButton(Images.NOTE_ICON.getScaledIcon(16, 16));
		addPostThatButton.addActionListener(event -> {
			this.addEmptyToCurrentBoard();
		});
		return addPostThatButton;
	}

	private JButton createClearButton()
	{
		JButton clearButton = new JButton(Images.CLEAR_ICON.getScaledIcon(16, 16));
		clearButton.addActionListener(event -> {
			this.clearCurrentBoard();
		});
		return clearButton;
	}

	private JButton createSaveButton()
	{
		JButton saveButton = new JButton(Images.SAVE_ICON.getScaledIcon(16, 16));
		saveButton.addActionListener(event -> {
			this.saveCurrentBoard();
		});
		return saveButton;
	}

	private JButton createCloseBoardButton()
	{
		JButton closeBoardButton = new JButton(Images.CLOSE_ICON.getScaledIcon(16, 16));
		closeBoardButton.addActionListener(event -> {
			this.closeCurrentBoard();
		});
		return closeBoardButton;
	}

	private void closeCurrentBoard()
	{
		this.applyOnCurrentBoard(currentBoard -> {
			this.tabs.close(currentBoard);
		});
	}

	private void addEmptyToCurrentBoard()
	{
		this.applyOnCurrentBoard(currentBoard -> {
			currentBoard.addEmpty();
		});
	}

	private void clearCurrentBoard()
	{
		this.applyOnCurrentBoard(currentBoard -> {
			currentBoard.clear();
		});
	}

	private void saveCurrentBoard()
	{
		this.applyOnCurrentBoard(currentBoard -> {
			if(!currentBoard.save())
			{
				JOptionPane.showMessageDialog(this, Internationalization.get("UNABLE_TO_SAVE_BOARD"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	private void applyOnCurrentBoard(Consumer<BoardPane> action)
	{
		BoardPane currentBoard = this.tabs.getCurrentBoard();

		if(currentBoard != null)
		{
			action.accept(currentBoard);
		}
	}

	private static class CheckBoxMenuItemPreferenceListener implements ActionListener
	{
		private static final Preferences preferences = Preferences.getInstance();

		private Component parent;
		private String preferenceName;
		private Consumer<Boolean> action;

		public CheckBoxMenuItemPreferenceListener(Component parent, String preferenceName, Consumer<Boolean> action)
		{
			this.parent = parent;
			this.preferenceName = preferenceName;
			this.action = action;
		}

		@Override
		public void actionPerformed(ActionEvent event)
		{
			boolean selected = this.getCheckBoxItem(event).isSelected();
			this.action.accept(selected);
			CheckBoxMenuItemPreferenceListener.preferences.setBoolean(this.preferenceName, selected);
			if(!CheckBoxMenuItemPreferenceListener.preferences.save())
			{
				JOptionPane.showMessageDialog(this.parent, Internationalization.get("UNABLE_TO_SAVE_PREFERENCES"), Internationalization.get("SAVE_ERROR"), JOptionPane.WARNING_MESSAGE);
			}
		}

		private JCheckBoxMenuItem getCheckBoxItem(ActionEvent event)
		{
			return (JCheckBoxMenuItem) event.getSource();
		}

	}
}
