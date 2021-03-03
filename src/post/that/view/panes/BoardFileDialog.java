package post.that.view.panes;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import post.that.utils.BoardFileFilter;

public class BoardFileDialog extends JFileChooser
{
	private static final long serialVersionUID = 4288052194176923464L;

	private BoardFileDialog()
	{
		this.setCurrentDirectory(new File(System.getProperty("user.home")));

		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.setAcceptAllFileFilterUsed(false);

		this.addChoosableFileFilter(new BoardFileFilter());
	}

	private File getFileForSave(JComponent parent)
	{
		return this.getFile(this.showSaveDialog(parent));
	}

	private File getFileForLoad(JComponent parent)
	{
		return this.getFile(this.showOpenDialog(parent));
	}

	private File getFile(int userSelection)
	{
		return userSelection == JFileChooser.APPROVE_OPTION ? this.getSelectedFile() : null;
	}

	public static File getSaveFile(JComponent parent)
	{
		return new BoardFileDialog().getFileForSave(parent);
	}

	public static File getLoadFile(JComponent parent)
	{
		return new BoardFileDialog().getFileForLoad(parent);
	}
}
