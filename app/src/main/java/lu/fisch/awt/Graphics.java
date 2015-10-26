package lu.fisch.awt;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Graphics 
{
	private Canvas canvas;
	private Color color = Color.BLACK;
	private Paint paint = new Paint();
	
	public Graphics(Canvas canvas)
	{
		this.canvas=canvas;
		paint.setAntiAlias(true);
	}
	
	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public int getWidth()
	{
		return canvas.getWidth();
	}
	
	public int getHeight()
	{
		return canvas.getHeight();
	}
	
	public void fillOval(int x, int y, int width, int height)
	{
		fillOval((float)x,(float)y,(float)width,(float)height);
	}
	
	public void fillOval(float x, float y, float width, float height)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.FILL);
		RectF oval = new RectF(x,y,x+width,y+height);
		canvas.drawOval(oval, paint);
	}
	
	public void fillRect(int x, int y, int width, int height)
	{
		fillRect((float)x,(float)y,(float)width,(float)height);
	}
	
	public void fillRect(float x, float y, float width, float height)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.FILL);
		RectF rect = new RectF(x,y,x+width,y+height);
		canvas.drawRect(rect, paint);
	}
	
	public void drawRect(int x, int y, int width, int height)
	{
		drawRect((float)x,(float)y,(float)width,(float)height);
	}
	
	public void drawRect(float x, float y, float width, float height)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.STROKE);
		RectF rect = new RectF(x,y,x+width,y+height);
		canvas.drawRect(rect, paint);
	}
	
	public void drawRoundRect(int x, int y, int width, int height, int rx, int ry)
	{
		drawRoundRect((float)x, (float)y, (float)width, (float)height, (float)rx, (float)ry);
	}
	
	public void drawRoundRect(float x, float y, float width, float height, float rx, float ry)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.STROKE);
		RectF rect = new RectF(x,y,x+width,y+height);
		canvas.drawRoundRect(rect, rx, ry, paint);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2)
	{
		drawLine((float)x1, (float)y1, (float)x2, (float)y2);
	}
	
	public void drawLine(float x1, float y1, float x2, float y2)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawLine(x1, y1, x2, y2, paint);
	}
	
	public void drawString(String text, int x, int y)
	{
		drawString(text, (float)x, (float)y);
	}
	
	public void drawString(String text, float x, float y)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.FILL);
		canvas.drawText(text,x,y, paint);
	}
	
	public void drawString(String text, int x, int y, int size)
	{
		drawString(text, (float)x, (float)y, (float)size);
	}
	
	public void drawString(String text, float x, float y, float size)
	{
		paint.setColor(color.getAndroidColor());
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(size);
		canvas.drawText(text,x,y, paint);
	}
	
	public void setColor(Color color)
	{
		this.color=color;
	}
	
	public Color getColor(Color color)
	{
		return color;
	}
}
