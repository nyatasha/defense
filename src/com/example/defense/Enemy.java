package com.example.defense;


import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy 
{
	/**Х и У коорданаты*/
	public int x; 
	public int y; 
	
	/**Скорость*/
	public int speed;
	
	/**Выосота и ширина спрайта*/
	public int width;
	public int height;
	
	public GameView gameView;
	public Bitmap bmp;
	
	/**Конструктор класса*/
	public Enemy(GameView gameView, Bitmap bmp)
	{
		this.gameView = gameView;
		this.bmp = bmp;
		
		Random rnd = new Random();
		this.x = 900;
		this.y = rnd.nextInt(300);
		this.speed = rnd.nextInt(10);
		
        this.width = 9;
        this.height = 8;
	}
	
	public void update()
	{
		x -= speed;
	}
	
	public void onDraw(Canvas c)
	{
		update();
		c.drawBitmap(bmp, x, y, null);
	}
}
