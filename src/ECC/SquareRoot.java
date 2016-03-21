package ECC;

import java.util.ArrayList;
import java.util.List;


public class SquareRoot {

	    public static List<Long> factor(long n) { 

	       // System.out.print("The prime factorization of " + n + " is: ");
            List<Long> all = new ArrayList<Long>();
	        // for each potential factor i
	        for (long i = 2; i <= n / i; i++) {

	            // if i is a factor of N, repeatedly divide it out
	            while (n % i == 0) {
	            	//System.out.print(i+", ");
	            	if(!all.contains(i)){
	            		all.add(i);
	            		
	            	}
	            		 
	                n = n / i;
	            }
	        }

	        // if biggest factor occurs only once, n > 1
	        if (n > 1) 
	        	{
	        		all.add(n);
	        		//System.out.print(n);
	        	}
	        
	       // System.out.println("\n");
	        if(all.size()==1)
	        	all.add(Long.parseLong("1"));
	        return all;

	    }
	    
	public static List<Long> squareRoot(long n,long p)
	{
		List<Long> sq = new ArrayList<Long>();
		long y2, y ;
		
		
		//factor the number
		List<Long> factors = factor(p);
		long[] a = new long[factors.size()];
		long[] m = new long[factors.size()];
		
		int i = 0;
		for(long aFactor:factors)
		{
			
			m[i] = aFactor;
			
			//find mod
			y2 = n % aFactor;
			if(y2==1)
				a[i] = 1;
			else
               a[i] = getSquareRoot(y2, aFactor);
			
            
			i++;
		  	
		}
		
		//generate all square roots
		int nRoots = (int) Math.pow(2, a.length);
		long[] tempA = new long[a.length];
		//String bi = Integer.toBinaryString(nRoots);
		long root = 0;
		
		for(int k = nRoots-1; k >=0; k--)
		{
			String bi = Integer.toBinaryString(k);
			while(bi.length()<a.length)
			{
				bi = "0"+bi;
			}
			
			for(int zero = 0; zero<bi.length(); zero++)
			{
				if(bi.charAt(zero)=='0')
				{
					tempA[zero] = -a[zero] ;
					
				}
				else
					tempA[zero] = a[zero];
				
			}
			//System.out.println("*******Steps of the Chinese Remainder Theorem*******");
			root = ChineseRemainder.findSolution(tempA, m);
			if(root<0)
				root+=p;
			if(!sq.contains(root))
				sq.add(root);
			
		}
		
		return sq;
		
	}
	

	private static long getSquareRoot(long y2, long aFactor) {
		
		    long sqRoot = 1;
			//check if aFactor is 3 mod 4
			if(aFactor % 4 ==3)
			{
				//square root = y2 ^ (aFactor+1)/4 mod aFactor
				sqRoot = crypto.fastSq(aFactor, y2, (aFactor+1)/4);
				
			}
			else
			{
				//trial and error
				for(long j=0;j<y2; j++)
				{
					long test = crypto.fastSq(aFactor, j, 2);
					if(test==y2)
					{
						//System.out.println("trial->"+j);
						sqRoot = j;
						break;
					}
				}
			}
			return sqRoot;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long n = 2;
		long p = 13;
		List<Long> sr = squareRoot(n, p);
		
		System.out.print("Square roots of "+n+" modulo "+p+" are : ");
		for(long aRoot:sr)
			System.out.print(aRoot+", ");
		
	}

}
