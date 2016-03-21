package ECC;

/********************************************************
*    A short Java program to find the GCD of two integers.
* The "error checking" part of the code is significantly 
* longer than the actual code to find the GCD itself.  
*
*    The program should be invoked with two integer
* arguments like
*
*       java GCD 230 64
*
* The error checking consists of verifying there are at 
* least two (integer) arguments (and that these are the
* first arguments given to the program), and that the
* two inputs are not both 0, as gcd(0,0) is not defined.
*
*   Runnig time:  
* Since this is really just a  straightforward 
* implementation of the Extended Euclidean
* algorithm, as described in the lecture notes, the 
* running time of this algorithm is O(log (max ({a, b})).
*********************************************************/

public class gcd 
{
    public static void main (String args[])  // entry point from OS
    {
        int a=3589, b=32611, d;
        int sign_a = 1, sign_b = 1;   //  These are used to handle cases when a < 0
                                      //  and/or b < 0, as the extendedEuclid function
                                      //  assumes that a and b are nonnegative.  
        int[] ans = new int[3];
 
        /*  Check for errors, see if user gave enough arguments, etc.  */

        
       /*  Check for input of two zeros, and print error if that's that case  */
      
       if ((a == 0) && (b == 0)) {
          System.out.println("\n  Oops, both arguments are zero!  No GCD of zero!\n");
          System.exit(1);
       }

       /*  If a and/or b is negative, then track this information for later output, because
           the extendedEuclid function expects nonnegative input.  */  
       if ( a < 0 )
            {
               sign_a = -1;
               a = Math.abs(a);
            }       
       if ( b < 0 )
            {
               sign_b = -1;
               b = Math.abs(b);
            }      

       /*  Find the answer and print it out  */ 
       ans = ExtendedEuclid(a,b);
       System.out.println("\n   gcd(" + a*sign_a + "," + b*sign_b +") = " + ans[0]);
       System.out.print("   " + ans[0] + " = (" + sign_a*ans[1] + ") (" + sign_a*a +")");
       System.out.println(" + (" + sign_b*ans[2] + ") (" + sign_b*b + ")\n");

    }


    public static int[] ExtendedEuclid(int a, int b)
    /*  This function will perform the Extended Euclidean algorithm
        to find the GCD of a and b.  We assume here that a and b
        are non-negative (and not both zero).  This function also
        will return numbers j and k such that 
               d = j*a + k*b
        where d is the GCD of a and b.
    */
    { 
        int[] ans = new int[3];
        int q;

        if (b == 0)  {  /*  If b = 0, then we're done...  */
            ans[0] = a;
            ans[1] = 1;
            ans[2] = 0;
        }
        else
            {     /*  Otherwise, make a recursive function call  */ 
               q = a/b;
               ans = ExtendedEuclid (b, a % b);
               int temp = ans[1] - ans[2]*q;
               ans[1] = ans[2];
               ans[2] = temp;
            }

        return ans;
    }
 
}  /*  end of "main" program  */
