package lu.fisch.structorizer.helpers;


import java.util.Vector;

import lu.fisch.awt.Color;
import lu.fisch.awt.Graphics;
import lu.fisch.structorizer.elements.Alternative;
import lu.fisch.structorizer.elements.For;
import lu.fisch.structorizer.elements.Instruction;
import lu.fisch.structorizer.elements.Root;
import lu.fisch.structorizer.elements.While;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class DrawThread extends Thread {

	/** Handle to the surface manager object we interact with */
    private SurfaceHolder mSurfaceHolder;
    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;
    /** Message handler used by thread to interact with TextView */
    private Handler mHandler;
    
    // our dimensions
    private int height=0;
    private int width=0;
    
    private float originX = 0;
    private float originY = 0;
    
    private float scaleFactor = 1.f;
    
    // indicates weather we are running or not
    private boolean running = false;
    
    private Root root = null;
    private Cross cross = null;

    public DrawThread(SurfaceHolder surfaceHolder, 
			  Context context,
			  Handler handler) 
    {
		// get handles to some important objects
		mSurfaceHolder = surfaceHolder;
		mHandler = handler;
		mContext = context;
	}
    
	private void draw(Graphics g) 
	{
		if(running) // && runCounter>0)
		{
			// clean the background
	        g.setColor(Color.WHITE);
	        g.fillRect(0, 0, getWidth(), getHeight());

	        try // there may happen something .. but we don't want the application to quit!
	        {
		       if(root!=null) root.draw(g);
		       if(cross!=null) cross.draw(g);
	        }
	        catch (Exception e)
	        {
	        	
	        }
		}		
	}
	
	public void setRoot(Root root)
	{
		this.root=root;
	}
	
	public void setCross(Cross cross)
	{
		this.cross=cross;
	}
	
    @Override
    public void run() 
    {
    	while(running)// && runCounter>0)
    	{
	        Canvas c = null;
	        try 
	        {
	        	// get the surface
	            c = mSurfaceHolder.lockCanvas(null);
	            synchronized (mSurfaceHolder) 
	            {
	            	if(c!=null)
	            	{
	            		// update the dimensions
		            	/*width=2000; //c.getWidth();
		            	height=2000; //c.getHeight();*/
		            	width=(int)(c.getWidth()/getScaleFactor());
		            	height=(int)(c.getHeight()/getScaleFactor());
		            	// scroll to the 
		            	//c.translate(getOriginX(), getOriginY());
		            	c.scale(getScaleFactor(),getScaleFactor());
		            	// draw onto it
		            	draw(new Graphics(c));
		            	//c.restore();
	            	}
	            }
	        } 
	        finally 
	        {
	            // do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
	            if (c != null) 
	            {
	                mSurfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
	        
	        //runCounter--;
    	}
    }
	
    @Override
    public void start()
    {
    	//runCounter++;
    	setRunning(true);
    	if(!super.isAlive())
    		super.start();
    }
    
    /*
    public void start(int counter)
    {
    	runCounter+=counter;
    	setRunning(true);
    	if(!isAlive())
    		super.start();
    }*/
    

    public int getWidth()
    {
        return width;
    }
	
    public int getHeight()
    {
        return height;
    }

    public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public float getOriginY() {
		return originY;
	}

	public void setOriginY(float originY) {
		this.originY = originY;
		Point size = new Point();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
		float max = getHeight()-size.y;
		if(this.originY>0) this.originY=0;
		else if(this.originY<-max) this.originY=-max;
	}

	public void addOriginY(float originY) {
		setOriginY(this.originY + originY);
	}

	public float getOriginX() {
		return originX;
	}

	public void setOriginX(float originX) {
		this.originX = originX;
		Point size = new Point();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
		float max = getWidth()-size.x;
		if(this.originX>0) this.originX=0;
		else if(this.originX<-max) this.originX=-max;
	}

	public void addOriginX(float originX) {
		setOriginX(this.originX + originX);
	}

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
}
