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

package lu.fisch.structorizer.generators;

import lu.fisch.structorizer.elements.*;
import lu.fisch.utils.StringList;

/******************************************************************************************************
 *
 *      Author:         Bob Fisch
 *
 *      Description:    Abstract class for any code generator.
 *
 ******************************************************************************************************
 *
 *      Revision List
 *
 *      Author          Date			Description
 *      ------		----			-----------
 *      Bob Fisch       2007.12.27              First Issue
 *	Bob Fisch	2008.04.12		Plugin Interface
 *
 ******************************************************************************************************
 *
 *      Comment:		
 ******************************************************************************************************///


public abstract class Generator
{
 	protected StringList code = new StringList();

	/************ Fields ***********************/
	protected abstract String getDialogTitle();
	protected abstract String getFileDescription();
	protected abstract String getIndent();
	protected abstract String[] getFileExtensions();
	
	
	/************ Code Generation **************/
        protected boolean insertComment(Element _element, String _indent, String _symbol)
        {
            return false;
        }
        
        
	protected void generateCode(Instruction _inst, String _indent)
	{
            //
	}
	
	protected void generateCode(Alternative _alt, String _indent)
	{
		// code.add(_indent+"");
		generateCode(_alt.qTrue,_indent+_indent.substring(0,1));
		// code.add(_indent+"");
		generateCode(_alt.qFalse,_indent+_indent.substring(0,1));
		// code.add(_indent+"");
	}

	protected void generateCode(Case _case, String _indent)
	{
		// code.add(_indent+"");
		for(int i=0;i<_case.qs.size();i++)
		{
			// code.add(_indent+"");
			generateCode((Subqueue) _case.qs.get(i),_indent+_indent.substring(0,1));
			// code.add(_indent+"");
		}
		// code.add(_indent+"");
	}

	protected void generateCode(For _for, String _indent)
	{
		// code.add(_indent+"");
		generateCode(_for.q,_indent+_indent.substring(0,1));
		// code.add(_indent+"");
	}

	protected void generateCode(While _while, String _indent)
	{
		// code.add(_indent+"");
		generateCode(_while.q,_indent+_indent.substring(0,1));
		// code.add(_indent+"");
	}

	protected void generateCode(Repeat _repeat, String _indent)
	{
		// code.add(_indent+"");
		generateCode(_repeat.q,_indent+_indent.substring(0,1));
		// code.add(_indent+"");
	}

	protected void generateCode(Forever _forever, String _indent)
	{
		// code.add(_indent+"");
		generateCode(_forever.q,_indent+_indent.substring(0,1));
		// code.add(_indent+"");
	}
	
	protected void generateCode(Call _call, String _indent)
	{
		// code.add(_indent+"");
	}

	protected void generateCode(Jump _jump, String _indent)
	{
		// code.add(_indent+"");
	}

	protected void generateCode(Parallel _para, String _indent)
	{
		// code.add(_indent+"");
	}

	protected void generateCode(Element _ele, String _indent)
	{
		if(_ele.getClass().getSimpleName().equals("Instruction"))
		{
			generateCode((Instruction) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Alternative"))
		{
			generateCode((Alternative) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Case"))
		{
			generateCode((Case) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Parallel"))
		{
			generateCode((Parallel) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("For"))
		{
			generateCode((For) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("While"))
		{
			generateCode((While) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Repeat"))
		{
			generateCode((Repeat) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Forever"))
		{
			generateCode((Forever) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Call"))
		{
			generateCode((Call) _ele,_indent);
		}
		else if(_ele.getClass().getSimpleName().equals("Jump"))
		{
			generateCode((Jump) _ele,_indent);
		}
	}
	
	protected void generateCode(Subqueue _subqueue, String _indent)
	{
		// code.add(_indent+"");
		for(int i=0;i<_subqueue.children.size();i++)
		{
			generateCode((Element) _subqueue.children.get(i),_indent);
		}
		// code.add(_indent+"");
	}

	/******** Public Methods *************/

	public String generateCode(Root _root, String _indent)
	{
		// code.add("");
		generateCode(_root.children,_indent+_indent.substring(0,1));
		// code.add("");

		return code.getText();
	}
	
	
	
	

	/******* Constructor ******************/

	public Generator()
	{
	}
	
}
