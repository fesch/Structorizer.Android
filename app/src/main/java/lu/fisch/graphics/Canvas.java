/*
    Structorizer
    A little tool which you can use to create Nassi-Schneiderman Diagrams (NSD)

    Copyright (C) 2009  Bob Fisch

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or any
    later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package lu.fisch.graphics;

/******************************************************************************************************
 *
 *      Author:         Bob Fisch
 *
 *      Description:    Class to represent a drawing canvas.
 *						Aims to work like a "TCanvas" in Delphi
 *
 ******************************************************************************************************
 *
 *      Revision List
 *
 *      Author          Date			Description
 *      ------			----			-----------
 *      Bob Fisch       2007.12.10      First Issue
 *
 ******************************************************************************************************
 *
 *      Comment:		/
 *
 ******************************************************************************************************/


import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.widget.TextView;
import lu.fisch.awt.Color;
import lu.fisch.awt.Graphics;
import lu.fisch.utils.BString;


public class Canvas  {
	
	protected Graphics canvas = null;
	private int x;
	private int y;
	
	public Canvas(Graphics _canvas)
	{
		canvas=_canvas;
	}
	
	public Graphics getGraphics()
	{
		return canvas;
	}
	
	public void setBackground(Color none)
	{
		
	}
	
	public static int stringWidth(String _string)
    {
		android.graphics.Rect bounds = new android.graphics.Rect();
		Paint textPaint = new Paint();
		textPaint.getTextBounds(_string,0,_string.length(),bounds);
		return bounds.width();
    }

	
	public static int stringHeight(String _string)
	{
		if(_string.equals("")) _string="O";
		android.graphics.Rect bounds = new android.graphics.Rect();
		Paint textPaint = new Paint();
		textPaint.getTextBounds(_string,0,_string.length(),bounds);
		return bounds.height()+4;
	}
		
	public void setColor(Color _color)
	{
		canvas.setColor(_color);
	}
	
	public void drawRect(Rect _rect)
	{
		canvas.drawRect(_rect.left, _rect.top, _rect.right-_rect.left, _rect.bottom-_rect.top);
	}	
	
	public void roundRect(Rect _rect)
	{
		canvas.drawRoundRect(_rect.left, _rect.top, _rect.right-_rect.left, _rect.bottom-_rect.top,30,30);
	}	
	
	public void fillRect(Rect _rect)
	{
		canvas.fillRect(_rect.left, _rect.top, _rect.right-_rect.left, _rect.bottom-_rect.top);
	}	
	
	public void writeOut(int _x, int _y, String _text)
	{
		String display = new String(_text);
		
		display = BString.replace(display, "<--","<-");
		display = BString.replace(display, "<-","\u2190");
		
		canvas.drawString(display, _x, _y);
	}
	
	public void moveTo(int _x, int _y)
	{
		x=_x;
		y=_y;
	}
	
	public void lineTo(int _x, int _y)
	{
		canvas.drawLine(x,y,_x,_y);
		moveTo(_x,_y);
	}
	
}
