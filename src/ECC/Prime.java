package ECC;

//generates primes
public class Prime 
{
    
    private final int UPPER_LIMIT = 100;

    public void calculatePrimeNumbers() 
    {

        int i = 0;
        int primeNumberCounter = 0;

        while (++i <= UPPER_LIMIT) 
        {

            int i1 = (int) Math.ceil(Math.sqrt(i));

            boolean isPrimeNumber = false;

            while (i1 > 1) 
            {

                if ((i != i1) && (i % i1 == 0)) 
                {
                    isPrimeNumber = false;
                    break;
                } 
                else if (!isPrimeNumber) 
                {
                    isPrimeNumber = true;
                }

                --i1;
            }

            if (isPrimeNumber) 
            {
                System.out.println(i);
                ++primeNumberCounter;
            }
        }

        System.out.println("Nr of prime numbers found: " + primeNumberCounter);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        new Prime().calculatePrimeNumbers();
    }
}