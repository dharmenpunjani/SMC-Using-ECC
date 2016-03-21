package ECC;


public class MillerRabinTest {

	public static int Composite = 1;
	
	public int millerRabinTest(long n, long a)
	{
		long gcd = crypto.gcd(a, n);
		if(n%2==0 || (gcd>1 && gcd<n))//if n is even
		  return Composite;
		long n_1 = n-1;
		//long q 
		int k = 0;
		while(n_1%2==0)
		{
			
			k++;
			n_1 = n_1/2;
			
			
		}
		long q =(long) n_1;
		
		a = crypto.fastSq(n, a, q);
		if(a%n==1){
			return 0;
			
		}
		
		for(int i = 0; i<k;i++)
		{
			
			if(a%n ==n-1) 
				return 0;
			a = (a*a)%n;
		}
		
		return Composite;
	}
	public static void main(String[] args)
	{
		MillerRabinTest mr = new MillerRabinTest();
		long[] allNumbers = {1105,294409,118901509,118901521, 118901527,118915387}; 
		for(long n:allNumbers)
		{
			
			//long n = Long.parseLong(aNumber);
			long a = 1;
			int composite = 0;
			while(composite == 0 && a<100)
			{
			  a++;
			  composite = mr.millerRabinTest(n, a);
			  //System.out.println(a+" "+composite);
			}
			
			if(composite == 0)
				System.out.println(n+" is prime");
			else
			
				System.out.println(n+" is composite with witness "+a);
		}
	}
}
