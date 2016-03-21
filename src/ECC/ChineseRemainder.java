package ECC;


public class ChineseRemainder {

	//find x given x = a1 (mod m1), x = a2 (mod m2) ...
	public static long findSolution(long[] a, long[] m)
	{
		if(a==null || m ==null)
		   {
		     System.out.println("a or m must have at least 2 elements");
		     return -1;
		   }
		if(a.length<2 || m.length<2)
		{
			System.out.println("Length of a or m are less than 2");
			return -1;
		}
		if(a.length != m.length)
		{
			System.out.println("Length of a and m are not same");
			return -1;
		}
		
		long x = a[0];
		long mInverse, y;
		long mi = 1;
		int i = 0;
		
		
		while(i< a.length-1)
		{
		
			mi*=m[i];
			
			//check if the m's are relatively prime
			if(crypto.gcd(m[i+1],mi)!=1)
			{
				System.out.println("The m's are not relatively prime->"+m[i+1]+","+mi);
				return -1;
			}
				
			//find mi_inverse (mod m[i+1])
			mInverse = crypto.inverse(m[i+1], mi);
			if(mInverse<0)
				mInverse+= m[i+1];
		
			y = ((a[i+1] - x)*mInverse)% m[i+1];
			if(y<0)
				y+= m[i+1];
			
			//System.out.print("Step:"+i+"->x="+x+"+"+mi+"*"+y+"=");
			
			x = x + mi*y;
			
			
			//System.out.println(x);
			
			i++;
		}
		
		return x;
	}
	
	public static void main(String[] args)
	{
		ChineseRemainder cr = new ChineseRemainder();
		long a[] = new long[]{2,3,0,0};
		long m[] = new long[]{3,7,2,5};
		
		long x = cr.findSolution(a, m);
		
		System.out.println("Solution:"+x);
	}
}
