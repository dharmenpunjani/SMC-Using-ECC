package ECC;

import java.math.BigInteger;
import java.util.Random;



class Alice
{
	
	long a;
	long p;
	long g;
	
	public Alice(long a, long p, long g)
	{
		this.p = p;
		this.g = g;
	}
	public long createKey()
	{
		//compute public key, A = g^a (mod p)
		long A = crypto.fastSq(p, g, a);
		return A;
	}
	
	public long decrypt(long[] c)
	{
		//m = (c1^a)^-1 . c2 (mod p)
		
         //compute x = c1^a (mod p)
		long x = crypto.fastSq(p, c[0], a);
		
		//compute x_inverse = x^-1 (mod p)
		long xInverse = crypto.inverse(p, x);
		
		//c2*xInverse (mod p)
		long message = (c[1]*xInverse) % p;
		
		return message;
		
		
	}
	
}
class Bob
{
	
	long m;
	long p;
	long g;
	
	public Bob(long message, long p, long g)
	{
		m = message;
		this.p = p;
		this.g = g;
	}
	
	
	public long[] encrypt(long A)
	{
		//choose k
		long k = (new Random()).nextLong();
		long c[] = new long[2];
		c[0] = crypto.fastSq(p, g, k);
		long Ak = crypto.fastSq(p, A, k);
		c[1] = (m*Ak) % p;
		
		return c;
	}
}
public class crypto 
{

	/**
	 * @param args
	 */
	public static long gcd(long a, long b)
	{
		if(b==0)
			return a;
		else
			return gcd(b, a % b);
	}
	
	//returns g^A (mod N)
	public static long fastSq(long N, long g, long A)
	{
		long b = 1;
		long a = g;
		
		while(A>0)
		{
			if ( A % 2 == 1)
			{
				b = (b*a)%N;
			}
			a = (a*a)%N;
			
			A = A/2;
		}
		return b;
	}
	
	//returns g^-1 (mod p)
	public static long inverse (long p, long g)
	{
		//g inverse = g ^ (p-2) (mod p)

		long[] gInverse = ExtendedEuclid(p, g);//crypto.fastSq(p, g, p-2);
		//System.out.println(gInverse[1]+" "+gInverse[2]);
		if(gInverse[2]<0)
			return p+gInverse[2];
		return gInverse[2];
		
	}
	public static BigInteger inverseBig (BigInteger p, BigInteger g)
	{
		//g inverse = g ^ (p-2) (mod p)

		BigInteger[] gInverse = ExtendedEuclidBig(p, g);//crypto.fastSq(p, g, p-2);
		//System.out.println(gInverse[1]+" "+gInverse[2]);
		if(gInverse[2].compareTo(new BigInteger("0"))<0)
			return p.add(gInverse[2]);
		return gInverse[2];
		
	}
	void ElGamal(long message, long aliceInt, long prime, long primitiveRoot)
	{
		//long message = 2;
		
		Alice alice = new Alice(aliceInt,prime,primitiveRoot);
		Bob bob = new Bob(message,prime,primitiveRoot);
		
		long alicePublicKey = alice.createKey();
		long[] encryptedM = bob.encrypt(alicePublicKey);
		
		long decryptedM = alice.decrypt(encryptedM);
		
		if(message == decryptedM)
		{
			System.out.println("ElGamal is working! msg="+message+", decrypted->"+decryptedM);
		}
		else
			System.out.println("ElGamal is not working->"+message+"->"+decryptedM);
		
	}
	public static BigInteger[] ExtendedEuclidBig(BigInteger a, BigInteger b)
    /*  This function will perform the Extended Euclidean algorithm
        to find the GCD of a and b.  We assume here that a and b
        are non-negative (and not both zero).  This function also
        will return numbers j and k such that 
               d = j*a + k*b
        where d is the GCD of a and b.
    */
    { 
        BigInteger[] ans = new BigInteger[3];
        BigInteger q;

        if (b.compareTo(new BigInteger("0")) == 0)  {  /*  If b = 0, then we're done...  */
            ans[0] = a;
            ans[1] = new BigInteger("1");
            ans[2] = new BigInteger("0");;
        }
        else
            {     /*  Otherwise, make a recursive function call  */ 
               q = a.divide(b);
               ans = ExtendedEuclidBig (b, a.mod(b));
               BigInteger temp = ans[1].subtract( ans[2].multiply(q));
               ans[1] = ans[2];
               ans[2] = temp;
            }

        return ans;
    }
 

	 public static long[] ExtendedEuclid(long a, long b)
	    /*  This function will perform the Extended Euclidean algorithm
	        to find the GCD of a and b.  We assume here that a and b
	        are non-negative (and not both zero).  This function also
	        will return numbers j and k such that 
	               d = j*a + k*b
	        where d is the GCD of a and b.
	    */
	    { 
	        long[] ans = new long[3];
	        long q;

	        if (b == 0)  {  /*  If b = 0, then we're done...  */
	            ans[0] = a;
	            ans[1] = 1;
	            ans[2] = 0;
	        }
	        else
	            {     /*  Otherwise, make a recursive function call  */ 
	               q = a/b;
	               ans = ExtendedEuclid (b, a % b);
	               long temp = ans[1] - ans[2]*q;
	               ans[1] = ans[2];
	               ans[2] = temp;
	            }

	        return ans;
	    }
	 
	 //find x from x^e = c (mod p)
	 public static long findRoot(long e, long c, long p)
	 {
		 long x = 0;
		 
		 //find e^-1 (mod p-1)
		 long eInverse = crypto.inverse(p-1, e);
		 
		 //x = c^d (mod p)
		 x = crypto.fastSq(p, c, eInverse);
		 
		 return x;
	 }
	public static void main(String[] args) 
	{
		
		
//	    long resultGcd = gcd(3589,32610);
//		System.out.println(resultGcd);
//		
//		long rFastSq = fastSq(32611, 11111, 3589*15619); 
//		System.out.println(rFastSq);
//		
////		long rFastSq2 = crypto.fastSq(32611, 11111, 15619);
////		System.out.println(rFastSq2);
////		
//		long prime = 467;
//		long aliceInt = 153;
//		long primitiveRoot = 10;
//		long message = 339;
//		
//		//c.ElGamal(message, aliceInt, prime, primitiveRoot);
//		
//		//check inverse
//		long i = inverse(32610, 15619);
//		
//		//System.out.println("check prime->"+c.fastSq(31883, 2, 31881));
//		System.out.println("inverse->"+i);
		
		for(long i=0;i<1373; i++)
		{
			long y = crypto.fastSq(1373, 2, i);
			if(y==974)
				System.out.println(i);
		}
		
	}
	

}
