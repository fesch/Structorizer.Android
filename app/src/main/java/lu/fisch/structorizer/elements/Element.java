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

package lu.fisch.structorizer.elements;

/******************************************************************************************************
 *
 *      Author:         Bob Fisch
 *
 *      Description:    Abstract class for all Elements.
 *
 ******************************************************************************************************
 *
 *      Revision List
 *
 *      Author          Date			Description
 *      ------			----			-----------
 *      Bob Fisch       2007.12.09      First Issue
 *
 ******************************************************************************************************
 *
 *      Comment:		/
 *
 ******************************************************************************************************///


import android.graphics.Point;
import android.util.Log;
import lu.fisch.awt.Color;
import lu.fisch.utils.*;
import lu.fisch.graphics.*;

public abstract class Element 
{
	// Program CONSTANTS
	public static String E_VERSION = "3.22-08";
	public static String E_THANKS = "";
	public final static String E_CHANGELOG = "";
	//public final static String E_INI = "Structorizer.app/structorizer.ini";

	// some static constants
	static int E_PADDING = 20;
	static int E_INDENT = 2;
	public static Color E_DRAWCOLOR = Color.YELLOW;
	public static Color E_WAITCOLOR = new Color(255,255,210);
	static Color E_COMMENTCOLOR = Color.LIGHT_GRAY;
	public static boolean E_VARHIGHLIGHT = false;
	public static boolean E_SHOWCOMMENTS = true;
	public static boolean E_DIN = false;
	public static boolean E_ANALYSER = true;

	// some colors
	public static Color color0 = Color.decode("0xFFFFFF");
	public static Color color1 = Color.decode("0xFF8080");
	public static Color color2 = Color.decode("0xFFFF80");
	public static Color color3 = Color.decode("0x80FF80");
	public static Color color4 = Color.decode("0x80FFFF");
	public static Color color5 = Color.decode("0x0080FF");
	public static Color color6 = Color.decode("0xFF80C0");
	public static Color color7 = Color.decode("0xC0C0C0");
	public static Color color8 = Color.decode("0xFF8000");
	public static Color color9 = Color.decode("0x8080FF");

	// text "constants"
	public static String preAlt = "(?)";
	public static String preAltT = "T";
	public static String preAltF = "F";
	public static String preCase = "(?)\n?\n?\nelse";
	public static String preFor = "for ? <- ? to ?";
	public static String preWhile = "while (?)";
	public static String preRepeat = "until (?)";

	// element attributes
	StringList text = new StringList();
	public StringList comment = new StringList();
	public boolean rotated = false;

	public Element parent = null;
	public boolean selected = false;
	public boolean waited = false;
	private Color color = Color.WHITE;

	// used for drawing
	public Rect rect = new Rect();
	// used for selecting
	public Rect selectRect = new Rect();

	// abstract things
	public abstract Rect prepareDraw(Canvas _canvas);
	public abstract void draw(Canvas _canvas, Rect _top_left);
	public abstract Element copy();

    // draw point
    Point drawPoint = new Point(0,0);

    public Point getDrawPoint()
    {
        Element ele = this;
        while(ele.parent!=null) ele=ele.parent;
        return ele.drawPoint;
    }

    public void setDrawPoint(Point point)
    {
        Element ele = this;
        while(ele.parent!=null) ele=ele.parent;
        ele.drawPoint=point;
    }

	public String getHexColor()
	{
		String rgb = Integer.toHexString(color.getRGB());
		return rgb.substring(2, rgb.length());
	}    

    public Element()
	{
	}

	public Element(String _strings)
	{
		setText(_strings);
	}

	public Element(StringList _strings)
	{
		setText(_strings);
	}

	public void setText(String _text)
	{
		text.setText(_text);
	}

	public void setText(StringList _text)
	{
		text=_text;
	}

	public StringList getText()
	{
		return text;
	}

	public void setComment(String _comment)
	{
		comment.setText(_comment);
	}

	public void setComment(StringList _comment)
	{
		comment=_comment;
	}

	public StringList getComment()
	{
		return comment;
	}

	public boolean getSelected()
	{
		return selected;
	}

	public void setSelected(boolean _sel)
	{
		selected=_sel;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color _color)
	{
		color = _color;
	}

	public Element selectElementByCoord(int _x, int _y)
	{
        Point pt=getDrawPoint();
		//Log.d("bob","Me at  ("+(selectRect.left-pt.x)+","+(selectRect.top-pt.y)+","+(selectRect.right-pt.x)+","+(selectRect.bottom-pt.y)+") "+this.getClass().getName());
		//Log.d("bob","Me at  ("+(selectRect.left)+","+(selectRect.top)+","+(selectRect.right)+","+(selectRect.bottom)+") "+this.getClass().getName());

        //if ((selectRect.left-pt.x<=_x)&&(_x<=selectRect.right-pt.x)&&
        //        (selectRect.top-pt.y<=_y)&&(_y<=selectRect.bottom-pt.y))
        if ((selectRect.left<=_x)&&(_x<=selectRect.right)&&
            (selectRect.top<=_y)&&(_y<=selectRect.bottom))
        {
                return this;
        }
        else
        {
                selected=false;
                return null;
        }
	}

	public Element getElementByCoord(int _x, int _y)
	{
            Point pt=getDrawPoint();

            if ((rect.left-pt.x<_x)&&(_x<rect.right-pt.x)&&
                    (rect.top-pt.y<_y)&&(_y<rect.bottom-pt.y))
            {
                    return this;
            }
            else
            {
                    return null;
            }
	}

    public Rect getRect()
    {
        return new Rect(rect.left,rect.top,rect.right,rect.bottom);
    }

    public Root getRoot()
	{
		Element now = this;
		while(now.parent!=null)
		{
			now=now.parent;
		}
		return (Root) now;
	}
    
    public void writeOutVariables(Canvas _canvas, int _x, int _y, String _text)
	{
		StringList parts = new StringList();
		parts.add(_text);

		StringList splits = new StringList();
		Root root = getRoot();

		if(root!=null)
		{
			if (root.hightlightVars==true)
			{

				// split
				parts=StringList.explodeWithDelimiter(parts," ");
				parts=StringList.explodeWithDelimiter(parts,".");
				parts=StringList.explodeWithDelimiter(parts,",");
				parts=StringList.explodeWithDelimiter(parts,"(");
				parts=StringList.explodeWithDelimiter(parts,")");
				parts=StringList.explodeWithDelimiter(parts,"[");
				parts=StringList.explodeWithDelimiter(parts,"]");
				parts=StringList.explodeWithDelimiter(parts,"-");
				parts=StringList.explodeWithDelimiter(parts,"+");
				parts=StringList.explodeWithDelimiter(parts,"/");
				parts=StringList.explodeWithDelimiter(parts,"*");
				parts=StringList.explodeWithDelimiter(parts," mod ");
				parts=StringList.explodeWithDelimiter(parts," div ");
				parts=StringList.explodeWithDelimiter(parts,">");
				parts=StringList.explodeWithDelimiter(parts,"<");
				parts=StringList.explodeWithDelimiter(parts,"=");
				parts=StringList.explodeWithDelimiter(parts,":");
				parts=StringList.explodeWithDelimiter(parts,"!");
				parts=StringList.explodeWithDelimiter(parts,"'");
				parts=StringList.explodeWithDelimiter(parts,"\"");

				parts=StringList.explodeWithDelimiter(parts,"\\");
                                parts=StringList.explodeWithDelimiter(parts,"%");

				/*
				String s = parts.getCommaText();
				//s=cutOut(s,",");
				s=cutOut(s," ");
				s=cutOut(s,".");
				s=cutOut(s,"(");
				s=cutOut(s,")");
				s=cutOut(s,"[");
				s=cutOut(s,"]");
				s=cutOut(s,"-");
				s=cutOut(s,"+");
				s=cutOut(s,"/");
				s=cutOut(s,"*");
				s=cutOut(s,"mod");
				s=cutOut(s,"div");
				s=cutOut(s,"<");
				s=cutOut(s,">");
				s=cutOut(s,"=");
				s=cutOut(s,":");
				s=cutOut(s,"'");
				s=cutOut(s,"\"");
				parts.setCommaText(s);*/

				//reassamble
				int i = 0;
				while (i<parts.count())
				{
					if(i<parts.count()-2)
					{
						if(parts.get(i).equals("<") && parts.get(i+1).equals("-") && parts.get(i+2).equals("-") )
						{
							parts.set(i,"<-");
							parts.delete(i+1);
							parts.delete(i+1);
						}
						else if(parts.get(i).equals("<") && parts.get(i+1).equals("-"))
						{
							parts.set(i,"<-");
							parts.delete(i+1);
						}
						else if(parts.get(i).equals(":") && parts.get(i+1).equals("="))
						{
							parts.set(i,":=");
							parts.delete(i+1);
						}
                                                else if(parts.get(i).equals("!") && parts.get(i+1).equals("="))
                                                {
                                                        parts.set(i,"!=");
                                                        parts.delete(i+1);
                                                }
                                                else if(parts.get(i).equals("<") && parts.get(i+1).equals(">"))
                                                {
                                                        parts.set(i,"<>");
                                                        parts.delete(i+1);
                                                }
					}
					else if(i<parts.count()-1)
					{
						if(parts.get(i).equals("<") && parts.get(i+1).equals("-"))
						{
							parts.set(i,"<-");
							parts.delete(i+1);
						}
						else if(parts.get(i).equals(":") && parts.get(i+1).equals("="))
						{
							parts.set(i,":=");
							parts.delete(i+1);
						}
                                                else if(parts.get(i).equals("!") && parts.get(i+1).equals("="))
                                                {
                                                        parts.set(i,"!=");
                                                        parts.delete(i+1);
                                                }
                                                else if(parts.get(i).equals("<") && parts.get(i+1).equals(">"))
                                                {
                                                        parts.set(i,"<>");
                                                        parts.delete(i+1);
                                                }
					}
					i++;
				}

				StringList specialSigns = new StringList();
				specialSigns.add(".");
				specialSigns.add("[");
				specialSigns.add("]");
				//specialSigns.add("<-");
				//specialSigns.add("<--");
				specialSigns.add("\u2190");
				specialSigns.add(":=");

				specialSigns.add("+");
				specialSigns.add("/");
				specialSigns.add("*");
				specialSigns.add("-");
				specialSigns.add("var");
				specialSigns.add("mod");
				specialSigns.add("div");
				specialSigns.add("<");
				specialSigns.add(">");
				specialSigns.add("=");
				specialSigns.add("!");

				specialSigns.add("'");
				specialSigns.add("\"");

				StringList ioSigns = new StringList();

				for(i=0;i<parts.count();i++)
				{
					String display = parts.get(i);

					display = BString.replace(display, "<--","<-");
					display = BString.replace(display, "<-","\u2190");

					if(!display.equals(""))
					{
						// if this part has to be colored
						if(root.variables.contains(display))
						{
							// set color
							_canvas.setColor(Color.decode("0x000099"));
						}
						// if this part has to be colored with special color
						else if(specialSigns.contains(display))
						{
							// set color
							_canvas.setColor(Color.decode("0x990000"));
						}
						// if this part has to be colored with io color
						else if(ioSigns.contains(display))
						{
							// set color
							_canvas.setColor(Color.decode("0x007700"));
						}

						// write out text
						_canvas.writeOut(_x,_y,display);

						// update width
						_x+=Canvas.stringWidth(display);

						// reset color
						_canvas.setColor(Color.BLACK);

					}
				}
				//System.out.println(parts.getCommaText());
			}
			else
			{
				_canvas.writeOut(_x,_y,_text);
			}
		}
	}    

    public int getWidthOutVariables(Canvas _canvas, String _text)
    {
    	return _canvas.stringWidth(_text);
    }
    
}
