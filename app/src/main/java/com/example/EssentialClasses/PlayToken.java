package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public class PlayToken {
    public static final PlayToken DEFAULT_TOKEN = new PlayToken(1, 1);
    private int shape;
    private int color;

    public PlayToken(int shape, int color){
        this.shape = shape;
        this.color = color;
    }

    public void setShape(int shape) {
        this.shape = shape;
        updateShape();
    }

    public void setColor(int color) {
        this.color = color;
        updateColor();
    }

    private void updateShape(){
        switch (shape) {
            case 1:
                //MAKE IT LOOK LIKE A CIRCLE
                break;
            case 2:
                //MAKE IT LOOK LIKE A TRIANGLE
                break;
            case 3:
                //MAKE IT LOOK LIKE A SQUARE
                break;
            case 4:
                //MAKE IT LOOK LIKE A PENTAGON
                break;
            case 5:
                //MAKE IT LOOK LIKE A HEART
                break;
            case 6:
                //MAKE IT LOOK LIKE A TEDDY BEAR
                break;
            default:
                setShape(1);
                break;
        }
    }

    private void updateColor(){
        switch (color) {
            case 1:
                //MAKE IT LOOK BLUE
                break;
            case 2:
                //MAKE IT LOOK GREEN
                break;
            case 3:
                //MAKE IT LOOK RED
                break;
            case 4:
                //MAKE IT LOOK ORANGE
                break;
            case 5:
                //MAKE IT LOOK YELLOW
                break;
            case 6:
                //MAKE IT LOOK WHITE
                break;
            default:
                setColor(1);
                break;
        }
    }
}
