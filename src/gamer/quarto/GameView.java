package gamer.quarto;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class GameView extends SurfaceView{
        
    private SurfaceHolder mHolder;
    protected GameLoopThread gameLoopThread;
    protected ArrayList<Sprite> mSprites;
    
    public long FPS = 30;
    
    public GameView(Context context,final int id) {
        super(context);

        mSprites = new ArrayList<Sprite>();
        gameLoopThread = new GameLoopThread(this);
        mHolder = getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {

        	public void surfaceDestroyed(SurfaceHolder holder) {
        		//boolean retry = true;
        		gameLoopThread.setRunning(false);
        		//while (retry) {
        			try {
        				gameLoopThread.join();
        				System.out.println("gameLoopThread finalizada." + id);
                        //retry = false;
        			} catch (InterruptedException e) {
        			}
        		//}
        	}

        	public void surfaceCreated(SurfaceHolder holder) {
        		if(gameLoopThread.getState() == Thread.State.TERMINATED){
        			System.out.println("gameLoopThread reinicializado" + id);
        			gameLoopThread = new GameLoopThread(GameView.this);
        		}else{
        			System.out.println("gameLoopThread inicializado " + id);
					onLoad();
        		}
        		gameLoopThread.setRunning(true);
        		gameLoopThread.start();
        	}

        	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        	}
        });
    }

    protected abstract void onLoad();

    @Override
    protected void onDraw(Canvas canvas) {
    	canvas.drawRGB(255, 255, 255);
    	//System.out.println("canvas");
    	for (Sprite sprite : mSprites) 
        sprite.onDraw(canvas);
    }
    
    //@Override
    public boolean onTouchEvent(MotionEvent event) {
    	synchronized (getHolder()) {
    		TouchEvents(event);
    	}
    	return true;
    }
    
    public abstract void TouchEvents(MotionEvent event);
    
    

    public void update() {
            for (Sprite sprite : mSprites) 
        sprite.update();
    }
     
}
