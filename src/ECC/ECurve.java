package ECC;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

public class ECurve 
{
	public BigInteger p = new BigInteger("11");
	public BigInteger a = new BigInteger("1");
	public BigInteger b = new BigInteger("6");
	public ECFieldFp efp = new ECFieldFp(p);
	public EllipticCurve ec = new EllipticCurve(efp, a, b);
	public ECPoint gen = new ECPoint(new BigInteger("2"), new BigInteger("7"));
	
	
	public ECPoint add(BigInteger x1,BigInteger y1,BigInteger x2,BigInteger y2) 
	{
		ECPoint ecp;
		BigInteger x = new BigInteger("0");
		BigInteger y = new BigInteger("0");
		BigInteger alp = (y1.subtract(y2).abs()).divide(x1.subtract(x2).abs());
		x = alp.multiply(alp).subtract(x1).subtract(x2);
		y = alp.multiply(x.subtract(x1)).add(y1);
		ecp = new ECPoint(x, y);
		System.out.println("X:"+ecp.getAffineX());
		System.out.println("Y:"+ecp.getAffineY());
		return ecp;
			
	}
	
	
}
