public class Point {
    private int x,y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Point))return false;
        Point p = (Point) obj;
        return p.getX()==x&&p.getY()==y;
    }
}
