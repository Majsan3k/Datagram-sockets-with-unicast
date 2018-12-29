import java.awt.*;


public class ColorPoint extends Point {
    private Color color;
    private int size;

    public ColorPoint(int x, int y, Color color, int size){
        super(x, y);
        this.color = color;
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
