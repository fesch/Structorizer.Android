package lu.fisch.structorizer.helpers;

import lu.fisch.awt.Color;
import lu.fisch.awt.Graphics;

public class Cross 
{
	private float x;
	private float y;
	
	public Cross(float x, float y)
	{
		this.setX(x);
		this.setY(y);
	}
	
	public void draw(Graphics g)
	{
		if(x>0 && y>0)
		{
			int size = 70;
			//g.setColor(Color.YELLOW);
			//g.fillOval(getX()-size, getY()-size, 2*size, 2*size);
			g.setColor(Color.BLUE);
			g.drawLine(getX(), getY()-size, getX(), getY()+size);
			g.drawLine(getX()-size, getY(), getX()+size, getY());
		}
	}

	public void setPosition(float x, float y)
	{
		setX(x);
		setY(y);
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
