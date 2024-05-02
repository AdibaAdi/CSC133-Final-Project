package com.csc133.snakegame;

//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.graphics.Color;

public  class Hard extends Rectangle implements ObstacleMovable{
    public Status status;
    public  Hard(int x, int y, int width, int height, int color){
        super(x, y, width, height, color);
        initHard();
    }



    public void initHard(){
        int rand = (int)(Math.random() * 4 + 1);
        switch (rand){
            case 1:
                status = Status.UP;
                break;
            case 2:
                status = Status.DOWN;
                break;
            case 3:
                status = Status.LEFT;
                break;
            case 4:
                status = Status.RIGHT;
                break;
        }

    }

    public void move(){
        if (x <= 0 )
            status = Status.RIGHT;
        if (x >= WIDTH - width)
            status = Status.LEFT;
        if (y <= 0)
            status = Status.DOWN;
        if (y >= HEIGHT - height)
            status = Status.UP;
        switch (status){
            case UP:
                y -= Obstacle_speed;
                break;
            case DOWN:
                y += Obstacle_speed;
                break;
            case LEFT:
                x -= Obstacle_speed;
                break;
            case RIGHT:
                x += Obstacle_speed;
                break;
        }
    }

}
