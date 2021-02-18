package post.that.view.ressources;

import java.awt.Color;

public enum Colors
{
	YELLOW(255, 241, 118);

	private final int R;
	private final int G;
	private final int B;

	private Colors(int r, int g, int b)
	{
		this.R = r;
		this.G = g;
		this.B = b;
	}

	public Color toSwing()
	{
		return new Color(this.R, this.G, this.B);
	}

	@Override
	public String toString()
	{
		return this.name() + "(" + this.R + ", " + this.G + ", " + this.B + ")";
	}
}
