package post.that.view.ressources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public enum Images
{
	BOARD_BACKGROUND("assets/background.jpg"),
	PIN_ICON("assets/pin.png"),
	TRASH_ICON("assets/trash.png"),
	NOTE_ICON("assets/note.png"),
	SAVE_ICON("assets/save.png"),
	CLEAR_ICON("assets/clear.png"),
	PICKER_ICON("assets/picker.png"),
	NEW_ICON("assets/new.png"),
	IMPORT_ICON("assets/import.png"),
	CLOSE_TAB_ICON("assets/close_tab.png"),
	EXIT_ICON("assets/exit.png");

	private BufferedImage image;
	private boolean loaded;

	private Images(String path)
	{
		try
		{
			this.image = ImageIO.read(new File(path));
			this.loaded = true;
		}
		catch(IOException e)
		{
			this.image = new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
			this.loaded = false;
		}
	}

	public Icon getDefaultIcon()
	{
		return new ImageIcon(this.getDefaultImage());
	}

	public Image getDefaultImage()
	{
		return this.getScaledImage(this.image.getWidth(), this.image.getHeight());
	}

	public Icon getScaledIcon(int width, int height)
	{
		return new ImageIcon(this.getScaledImage(width, height));
	}

	public Image getScaledImage(int width, int height)
	{
		return this.image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	public boolean isLoaded()
	{
		return this.loaded;
	}
}
