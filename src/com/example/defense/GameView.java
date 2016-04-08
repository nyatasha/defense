package com.example.defense;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable
{
	/**Объект класса GameLoopThread*/
	private GameThread mThread;
	
	private List<Ball> ball = new ArrayList<Ball>();
	
	private List<Enemy> enemy = new ArrayList<Enemy>();
	
	private Thread thred = new Thread(this);
	
	private Bitmap enemies;
	
	private Gun gun;
	
	private Bitmap guns;
	
	public int shotX;
    public int shotY; 
    
    /**Переменная запускающая поток рисования*/
    private boolean running = false;
	
  //-------------Start of GameThread--------------------------------------------------\\
    
	public class GameThread extends Thread
	{
		/**Объект класса*/
	    private GameView view;	 
	    
	    /**Конструктор класса*/
	    public GameThread(GameView view) 
	    {
	          this.view = view;
	    }

	    /**Задание состояния потока*/
	    public void setRunning(boolean run) 
	    {
	          running = run;
	    }

	    /** Действия, выполняемые в потоке */
	    public void run()
	    {
	        while (running)
	        {
	            Canvas canvas = null;
	            try
	            {
	                // подготовка Canvas-а
	                canvas = view.getHolder().lockCanvas();
	                synchronized (view.getHolder())
	                {
	                    // собственно рисование
	                    onDraw(canvas);
	                    testCollision();
	                }
	            }
	            catch (Exception e) { }
	            finally
	            {
	                if (canvas != null)
	                {
	                	view.getHolder().unlockCanvasAndPost(canvas);
	                }
	            }
	        }
	    }
}

	//-------------End of GameThread--------------------------------------------------\\
	
	public GameView(Context context) 
	{
		super(context);
		
		mThread = new GameThread(this);

		thred.start();
        
        /*Рисуем все наши объекты и все все все*/
        getHolder().addCallback(new SurfaceHolder.Callback() 
        {
      	  	 /*** Уничтожение области рисования */
               public void surfaceDestroyed(SurfaceHolder holder) 
               {
            	   boolean retry = true;
            	    mThread.setRunning(false);
            	    while (retry)
            	    {
            	        try
            	        {
            	            // ожидание завершение потока
            	            mThread.join();
            	            retry = false;
            	        }
            	        catch (InterruptedException e) { }
            	    }
               }

               /** Создание области рисования */
               public void surfaceCreated(SurfaceHolder holder) 
               {
            	   mThread.setRunning(true);
            	   mThread.start();
               }

               /** Изменение области рисования */
               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
               {
               }
        });
        
        enemies = BitmapFactory.decodeResource(getResources(), R.drawable.target);       
        enemy.add(new Enemy(this, enemies));
        
        guns = BitmapFactory.decodeResource(getResources(), R.drawable.player2);
        gun = new Gun(this, guns);
	}
	
	 /**Функция рисующая все спрайты и фон*/
    protected void onDraw(Canvas canvas) {     	
          canvas.drawColor(Color.WHITE);
          
          Iterator<Enemy> i = enemy.iterator();
          while(i.hasNext()) {
        	  Enemy e = i.next();
        	  if(e.x >= 1000 || e.x <= 1000) {
        		  e.onDraw(canvas);
        	  } else {
        		  i.remove();
        	  }
          }
          
          Iterator<Ball> j = ball.iterator();
          while(j.hasNext()) {
        	  Ball b = j.next();
        	  if(b.x >= 1000 || b.x <= 1000) {
        		  b.onDraw(canvas);
        	  } else {
        		  j.remove();
        	  }
          }
          
          //canvas.save();
          //canvas.rotate((float) angle);
          canvas.drawBitmap(guns, 5, 120, null);
          //canvas.restore();
          
          
    }
    
    /*Проверка на столкновения*/
    private void testCollision() {
		Iterator<Ball> b = ball.iterator();
		while(b.hasNext()) {
			Ball balls = b.next();
			Iterator<Enemy> i = enemy.iterator();
			while(i.hasNext()) {
	        	  Enemy enemies = i.next();
	        	  
	        	 if ((Math.abs(balls.x - enemies.x) <= (balls.width + enemies.width) / 2f)
	        			 && (Math.abs(balls.y - enemies.y) <= (balls.height + enemies.height) / 2f)) {
	        			   i.remove();
	        			   b.remove();
	        	 }
			}
        }
	}
    
    public Ball createSprite(int resouce) {
    	 Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
    	 return new Ball(this,bmp);
    }
    
    public boolean onTouchEvent(MotionEvent e) 
    {
    	shotX = (int) e.getX();
    	shotY = (int) e.getY();
    	
    	if(e.getAction() == MotionEvent.ACTION_DOWN)
    	ball.add(createSprite(R.drawable.bullet));
    	
		return true;
    }

	public void run() {
		while(true) {
			Random rnd = new Random();
			try {
				Thread.sleep(rnd.nextInt(2000));  
				enemy.add(new Enemy(this, enemies));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    
}
