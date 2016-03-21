package ECC;

import java.io.Serializable;
import java.math.BigInteger;

public class Point implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public BigInteger X;
	public BigInteger Y;
	public double x;
	public double y;
	static Point Zero = new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
	static Point ZeroBig = new Point(BigInteger.valueOf(Long.MIN_VALUE), BigInteger.valueOf(Long.MIN_VALUE));
	public Point(BigInteger x, BigInteger y)
	{
		this.X = x;
		this.Y = y;
	}
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	boolean isEqualBig(Point p2)
	{
		if(p2!=null)
		{
			if(this.X.equals(p2.X) && this.Y.equals(p2.Y))
				return true;
		}
		return false;
	}
	boolean isEqual(Point p2)
	{
		if(p2!=null)
		{
			if(this.x==p2.x && this.y==p2.y)
				return true;
		}
		return false;
	}
	public void setYBig(BigInteger n)
	{
		this.Y = n;
	}
	public void setY(double n)
	{
		this.y = n;
	}
	public void setXBig(BigInteger n)
	{
		this.X = n;
	}
	public void setX(double n)
	{
		this.x = n;
	}
	String print()
	{
		return "("+this.x+","+this.y+")";
	}
}