package com.cleverpixel.catchapple;

public class Bee 
{
    public int px = 0;
    public int py = 0;
    public int w = 0;
    public int h = 0;
    
    public int status = 0;
    
    public Boolean pyflag = true; 
    
    public void move(int w)
    {
        px = px + 10;
        if (px > w) px = 0;
        
        if(pyflag)
        {
        	py=py + 5;
        	if (py > 120) pyflag = false;
        }
        else
        {
        	py=py - 5;
        	if (py < 5) pyflag = true;
        }
    }
}
