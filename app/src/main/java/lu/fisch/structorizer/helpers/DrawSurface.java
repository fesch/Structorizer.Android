package lu.fisch.structorizer.helpers;


import java.util.Timer;
import java.util.TimerTask;

import lu.fisch.awt.Color;
import lu.fisch.structorizer.R;
import lu.fisch.structorizer.R.drawable;
import lu.fisch.structorizer.elements.Alternative;
import lu.fisch.structorizer.elements.Case;
import lu.fisch.structorizer.elements.Element;
import lu.fisch.structorizer.elements.For;
import lu.fisch.structorizer.elements.Forever;
import lu.fisch.structorizer.elements.Instruction;
import lu.fisch.structorizer.elements.Repeat;
import lu.fisch.structorizer.elements.Root;
import lu.fisch.structorizer.elements.While;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;

public class DrawSurface extends SurfaceView implements SurfaceHolder.Callback {

	private Context context;
	private DrawThread drawThread = null;
	
	private Root root = new Root();
	private Cross cross = new Cross(-1,-1);
	
	private int mouseX;
	private int mouseY;
	
	private Element selected;
	private Element selectedDown;
	private Element selectedUp;
	private Element selectedMoved;
	private boolean mouseMove;
	
	private float scaleFactor = 1.f;
	
	public void setRoot(Root root)
	{
		this.root=root;
		if(drawThread!=null) drawThread.setRoot(root);
	}
	
	public Root getRoot()
	{
		return root;
	}
	
	public DrawSurface(Context context) {
		super(context);
		init(context);
	}
	
	public DrawSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public DrawSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context)
	{
		this.context=context;
        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        // make sure we get key events
        setFocusable(true); 
        // this class is controlls the paintings, so 
        // we might use a time here ...
        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// do something
			}
		}, 10, 10);*/
        
        // static diagram
        /*
        root.setText("Test");
        
        Alternative a = new Alternative();
        a.setText("color ==" +
        		" red || color == green");
        root.children.addElement(a);
        
        Instruction i = new Instruction();
        i.setText("instruction()");
        a.qTrue.addElement(i.copy());
        a.qFalse.addElement(i.copy());
        
        For f = new For();
        f.setText("for i <- 1 to 10");
        a.qFalse.addElement(f);
        f.q.addElement(i.copy());
        
        While w = new While();
        w.setText("while (someCondition)");
        a.qTrue.addElement(w);
        w.q.addElement(i.copy());
        
        Repeat r = new Repeat();
        r.setText("Until finished");
        f.q.addElement(r);
        
        Forever fe  = new Forever();
        fe.setText("Forever");
        w.q.addElement(fe);
        
        Case c = new Case();
        c.setText("i\n1\n2\n3");
        fe.q.addElement(c);
        
        a = new Alternative();
        a.setText("A < B");
        c.qs.get(0).addElement(a);
        */
  	}

	private double initDist = 0;
	private double ds = 0;
	private Point relative = new Point();
	private long lastTouchTime = -1;
	
	@Override
    public boolean onTouchEvent(MotionEvent event)
    {
		//Log.d("DEBUG > ",event.getPointerCount()+" > "+event.getAction());
		// handle single things
		if(event.getPointerCount()==1)
		{
			if(event.getAction()==MotionEvent.ACTION_DOWN)
			{
				// get the first touch position (needed for move)
				relative = new Point((int)(event.getX()-root.getOffset().x),
									 (int)(event.getY()-root.getOffset().y));
				//Log.d("bob",relative.x+","+relative.y);
				mousePressed(event);
			}
			else if(event.getAction()==MotionEvent.ACTION_UP )
			{
				mouseReleased(event);
				/*if(selected==null || selected==root)
				{
					// move
					root.setOffset(new Point((int)(event.getX()-relative.x),
							                 (int)(event.getY()-relative.y)));
					//Log.d("bob","--> "+root.getOffset().x+","+root.getOffset().y);
				}*/
			}
			else if(event.getAction()==MotionEvent.ACTION_MOVE)
			{
				mouseDragged(event);
				/*if(selected==null || selected==root)
				{
					// move
					root.setOffset(new Point((int)(event.getX()-relative.x),
			                 (int)(event.getY()-relative.y)));
					//Log.d("bob","--> "+root.getOffset().x+","+root.getOffset().y);
				}*/
			}
		}
		// move & scale
		
		else if(event.getPointerCount()==2) // DOWN
		{
			cross.setPosition(-1,-1);
			//Log.d("bob",event.getActionIndex()+"  >  "+event.getAction());
			if(event.getAction()==261)
			{
				// nothing to do yet for move
				// get the position of the second finger for scaling
				initDist = Math.sqrt(Math.pow(event.getX(0)-event.getX(1), 2)+
									 Math.pow(event.getY(0)-event.getY(1), 2));
				setScaleFactor(getScaleFactor()-1+(float) (initDist/initDist));
				ds=1;
			}
			else if(event.getAction()==262) // UP
			{
				// move
				root.setOffset(new Point((int)(event.getX()-relative.x),
		                 (int)(event.getY()-relative.y)));
			}
			else if(event.getAction()==MotionEvent.ACTION_MOVE)
			{
				// move
				root.setOffset(new Point((int)(event.getX()-relative.x),
		                 (int)(event.getY()-relative.y)));

				// scale
				double nowDist = Math.sqrt(Math.pow(event.getX(0)-event.getX(1), 2)+
			             		 Math.pow(event.getY(0)-event.getY(1), 2));
				addScaleFactor((float) (-ds+nowDist/initDist));
				ds=nowDist/initDist;
			}
		}	
		else if(event.getPointerCount()==3) 
		{
			//Log.d("bob",drawThread.getWidth()+" <--> "+root.width);
			relative = new Point((int)(event.getX(0)-root.getOffset().x),
					             (int)(event.getY(0)-root.getOffset().y));
			root.setOffset(new Point(
					(int)((drawThread.getWidth()-root.width)/2),
					(int)((drawThread.getHeight()-root.height)/2)
			));
		}

		
		return true; 
    }
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) 
	{
		// as the constructor is not being called if
		// we resume from a previous state, we need
		// to create the drawing thread here.
        if(drawThread==null)
        {
	        drawThread = new DrawThread(getHolder(), getContext(), new Handler() {
	            @Override
	            public void handleMessage(Message m) {
	            }
	        });
	        // call the setter for the pointer to the model
	        drawThread.setRoot(root);
	        drawThread.setCross(cross);
	        setScaleFactor(getScaleFactor());
        }
        // start if not yet or still alive
		if(!drawThread.isAlive())
			drawThread.start();
	}
	
	/*
	public void repaint(int counter)
	{
		drawThread.start(counter);
	}*/

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) 
	{
		drawThread.setRunning(false);
		// stop the drawThread properly
        boolean retry = true;
        while (retry) 
        {
            try 
            {
            	// wait for it to finish
            	drawThread.join();
                retry = false;
            } 
            catch (InterruptedException e) 
            {
            	// ignore any error
            }
        }
        // set it to null, so that a new one can be created in case of a resume
        drawThread=null;
	}

	private void mousePressed(MotionEvent e)
	{
		int ex = (int) ((e.getX()/scaleFactor));
		int ey = (int) ((e.getY()/scaleFactor));
		cross.setPosition(ex, ey);

		mouseX = ex;
		mouseY = ey;
		
		Element.E_DRAWCOLOR=Color.YELLOW;
		Element ele = root.selectElementByCoord(ex,ey);
		
		long thisTime = System.currentTimeMillis();
		Log.d("bob","Time = "+(thisTime-lastTouchTime));
	    if ((ele!=null) && (ele==getSelected()) && (thisTime-lastTouchTime<250))
	    {
	    	edit(getSelected());
	    }
	    else 
	    {
	    	lastTouchTime = thisTime;
	    	if (ele!=null && ele!=getSelected())
			{
				if(getSelected()!=null && getSelected()!=ele) getSelected().setSelected(false);
				ele.setSelected(true);
				selected=ele;
				selectedDown=ele;
				selectedUp=ele;
			}
	
			if(getSelected()!=null)
			{
				if (!getSelected().getClass().getSimpleName().equals("Subqueue") &&
					!getSelected().getClass().getSimpleName().equals("Root"))
				{
					mouseMove=false;
				}
			}
	    }
    }

    private void mouseReleased(MotionEvent e)
	{
		int ex = (int) ((e.getX()/scaleFactor));
		int ey = (int) ((e.getY()/scaleFactor));
		cross.setPosition(-1,-1);
    	
		if ((mouseMove==true) && (selectedDown!=null))
		{
			Element.E_DRAWCOLOR=Color.YELLOW;
			if ( !selectedDown.getClass().getSimpleName().equals("Subqueue") &&
				!selectedDown.getClass().getSimpleName().equals("Root"))
			{
				selectedUp = root.selectElementByCoord(ex,ey);
				if(selectedUp!=null)
				{
					selectedUp.setSelected(false);
					if( !selectedUp.getClass().getSimpleName().equals("Root") &&
					   selectedUp!=selectedDown &&
					   root.checkChild(selectedUp,selectedDown)==false
					   )
					{
						root.addUndo();

						root.removeElement(selectedDown);
						selectedUp.setSelected(false);
						root.addAfter(selectedUp,selectedDown);
						selectedDown.setSelected(true);
					}
					else
					{
						selectedUp.setSelected(false);
						selectedDown.setSelected(true);
					}
				}
			}
			else
			{
				selectedUp = root.selectElementByCoord(ex,ey);
                                    if(selectedUp!=null) selectedUp.setSelected(false);
			}
		}

		mouseMove=false;
	}
	
	private void mouseDragged(MotionEvent e)
	{
		int ex = (int) ((e.getX()/scaleFactor));
		int ey = (int) ((e.getY()/scaleFactor));
		cross.setPosition(ex, ey);
		
		Element bSome = root.selectElementByCoord(ex,ey);

		if(bSome!=null)
		{
			bSome.setSelected(true);
			if(selectedDown!=null)
			{
				selectedDown.setSelected(true);
	
				if((selectedDown!=null) && ((int)ex!=(int)mouseX) && ((int)ey!=(int)mouseY) && (selectedMoved!=bSome))
				{
					mouseMove=true;
					if(selectedDown.getClass().getSimpleName().equals("Root") ||
					   selectedDown.getClass().getSimpleName().equals("Subqueue") ||
					   bSome.getClass().getSimpleName().equals("Root")||
					   root.checkChild(bSome, selectedDown))
					{
						Element.E_DRAWCOLOR=Color.RED;
					}
					else
					{
						Element.E_DRAWCOLOR=Color.GREEN;
					}
				}
			}

		}
		selectedMoved = bSome;
	}

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		// Don't let the object get too small or too large.
		scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
		this.scaleFactor = scaleFactor;
		if(drawThread!=null) drawThread.setScaleFactor(scaleFactor);
	}
	
	public void addScaleFactor(float scaleFactor) {
		setScaleFactor(this.scaleFactor+scaleFactor);
	}
	
    public Bundle saveState(Bundle map) {
        synchronized (getHolder()) 
        {
            if (map != null) 
            {
               map.putFloat("scaleFactor", scaleFactor);
               map.putInt("offsetX",root.getOffset().x);
               map.putInt("offsetY",root.getOffset().y);
            }
        }
        return map;
    }
	
    public void restore(Bundle savedState) {
        synchronized (getHolder()) 
        {
            setScaleFactor(savedState.getFloat("scaleFactor"));
            root.getOffset().x=savedState.getInt("offsetX");
            root.getOffset().y=savedState.getInt("offsetY");
        }
    }

	public Element getSelected() {
		return selected;
	}
	
	private void edit(Element ele)
	{
		AlertDialog editDialog = (new AlertDialog.Builder(context)).create();
		editDialog.setTitle("Edit");
		final EditText input = new EditText(context);
		final Element element = ele;
		input.setLines(4);
		input.setText(ele.getText().getText());
		editDialog.setView(input);
		editDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	element.setText(input.getText().toString());
	        }
	    });

		editDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });
		editDialog.setIcon(R.drawable.ic_launcher);
		editDialog.show();		
	}

	public void addNewElement(Element _ele, String _title, String _pre, boolean _after)
	{
		if (getSelected()!=null)
		{
			AlertDialog editDialog = (new AlertDialog.Builder(context)).create();
			editDialog.setTitle(_title);
			final EditText input = new EditText(context);
			final Element element = _ele;
			final boolean after = _after;
			input.setLines(4);
			input.setText(_ele.getText().getText());
			editDialog.setView(input);
			editDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		        	if (!(element instanceof Forever))
					{
		        		element.setText(input.getText().toString());
					}
					root.addUndo();
					if(after==true)
					{
						root.addAfter(getSelected(),element);
					}
					else
					{
						root.addBefore(getSelected(),element);
					}
					element.setSelected(true);
					selected=element;
	        	}
		    });

			editDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            dialog.cancel();
		        }
		    });
			editDialog.setIcon(R.drawable.ic_launcher);
			editDialog.show();		
		}
	}
	
	
    

}
