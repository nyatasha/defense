package com.example.defense;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Ball 
{
	/**��������*/
    private Bitmap bmp;
    
    /**������� �� �=0*/
    public int x;
    public int y;
    
    /**�������� �� �=15*/
    private int mSpeed=25;
    
    public double angle;
    
    /**������*/
    public int width;
    
    /**�������*/
    public  int height;
    
	public GameView gameView;
      
       /**�����������*/
       public Ball(GameView gameView, Bitmap bmp) {
             this.gameView=gameView;
             this.bmp=bmp;
             
             this.x = 0;
             this.y = 120;
             this.width = 27;
             this.height = 40;
             
             angle = Math.atan((double)(y - gameView.shotY) / (x - gameView.shotX));
       }
 
       /**����������� �������, ��� �����������*/
       private void update() {           
    	   x += mSpeed * Math.cos(angle);
    	   y += mSpeed * Math.sin(angle);
       }

      /**������ ���� �������*/
       public void onDraw(Canvas canvas) {
            update();
            canvas.drawBitmap(bmp, x, y, null);
       }
}
