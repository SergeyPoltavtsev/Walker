package client;

/**
 * Immutable point class for specifying location in world.
 */
public class Point {
	private int x;
	private int y;
		
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point old) {
		this.x = old.x;
		this.y = old.y;
	}

	public Point move(Command.dir dir) {
		Point p = new Point(this.getX(), this.getY());
		
		if(dir == null) return p;
		
		switch (dir) {
		case E:
			p.x++;
			break;
		case W:
			p.x--;
			break;
		case N:
			p.y--;
			break;
		case S:
			p.y++;
			break;
		}
		return p;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	/**
	 * Manhatten distance
	 * @param position
	 * @return
	 */
	public int distance(Point position) {
		return Math.abs(position.x - this.x) + Math.abs(position.y - this.y);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y+ ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Point)) {
			return false;
		}
		Point other = (Point)obj;
		return (other.x == this.x) && (other.y == this.y);
	}
	
	@Override
	public int hashCode() {
		final int prime = 127;
		return this.x * prime + this.y;
	}
}
