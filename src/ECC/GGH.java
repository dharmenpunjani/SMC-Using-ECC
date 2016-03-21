package ECC;

import Jama.Matrix;


class AliceGGH
{
	
	Matrix basis;
	double det;
	int dim;
	
	public AliceGGH(double[][] v)
	{
		basis = new Matrix(v);
	    det = Math.abs(basis.det());
	    System.out.println("Determinant of private basis: "+det);
	    dim = basis.getColumnDimension();
	    
	}
	
	
	
	//Returns the decrypted message
	//Message is decrypted using BabaiÕs Closest Vertex Algorithm
	public Matrix decryptBabai(Matrix e, Matrix publicBasis)
	{
		//find the coefficients to write e as a linear combination of the private basis
		/*
		 * basis = [v1]
		 *         [v2]
		 *         [v3]
		 * coefficient = [c1 c2 c3]
		 * 
		 * Write e = c1*v1+c2*v2+c3*v3
		 * 
		 * find the coefficient matrix
		 * 
		 */
        Matrix invBasis = basis.inverse();
        System.out.println("Private basis inverse->");
        invBasis.print(2, 2);
        
        Matrix coeff = e.times(invBasis);
        
        System.out.println("Coefficients->");
        coeff.print(2, 2);
        
        //round off the coefficients
        //roundCoeff = [rc1, rc2, rc3]
        Matrix roundedCoefficients = new Matrix(coeff.getRowDimension(), coeff.getColumnDimension());
        for(int i=0; i< coeff.getColumnDimension(); i++)
        {
        	roundedCoefficients.set(0,i,Math.round(coeff.get(0, i)));
        }
        
        System.out.println("Coefficients after rounding off->");
        roundedCoefficients.print(2, 2);
		
	    /*
	     * Compute a lattice vector
	     * lattice = roundedCoefficients*basis
	     *         = [rc1*v1, rc2*v2, rc3*v3]
	     */
        System.out.println("Computed lattice->");
        
        Matrix lattice = roundedCoefficients.times(basis);
	    lattice.print(2, 1);
	    
		//find inverse of publicBasis
		Matrix inverse = publicBasis.inverse();
		System.out.println("Public basis inverse->");
		inverse.print(2, 2);
        
		
		/*
		 * message = lattice*w^-1 
		 */
		Matrix message = lattice.times(inverse);
		
		
		return message;
		
		
	}
	
	
}

public class GGH {

	/**
	 * @param args
	 */

	public double getHadamardRatio(Matrix l)
	{
		double hr = 0;
		double det = l.det();
		int dim = l.getColumnDimension();
		
		double denom = 1;
		
		for(int i = 0;i<dim; i++)
		{
			double norm = 0;
			for(int j= 0; j<dim; j++)
			{
				double v=l.get(i, j);
				norm+=v*v;
			}
			norm = Math.sqrt(norm);
			denom*=norm;
				
		}
		double ratio = det/denom;
		
        if(ratio<0)
        	ratio*=-1.0;
		hr = Math.pow(ratio, (1.0/dim));
		
		return hr;
	}
	public static void main(String[] args) {
		
		GGH ggh = new GGH();
		
		//example 6.36, page 385 of the Intro to math. crypto.
		double [][] vExample = {{-97, 19, 19},
		         {-36, 30, 86},
		         {-184,-64, 78}};

		double[][] wExample = {{-4179163, -1882253, 583183},
		         {-3184353 ,-1434201, 444361},
		         {-5277320 ,-2376852, 736426}};
		AliceGGH aExample = new AliceGGH(vExample);
		double hr = ggh.getHadamardRatio(aExample.basis);
		System.out.println("Haramard ratio->"+hr);
		
		Matrix wmExample = new Matrix(wExample);
		double[][] encryptedExample = new double[][]{{-79081427,-35617462, 11035473}};
		Matrix encrypted = new Matrix(encryptedExample);
		
		System.out.println("Encrypted message:");
		encrypted.print(2, 2);
		
		System.out.println("**Message decryption starts**");
		Matrix msg = aExample.decryptBabai(encrypted, wmExample);
		System.out.println("Decrypted message with private basis->");
		msg.print(3, 3);
		
		System.out.println("Message decrypt message with Public basis");
		aExample.basis = wmExample;
		msg = aExample.decryptBabai(encrypted, wmExample);
		System.out.println("Decrypted message with public basis->");
		msg.print(3, 3);
		
		/************************excercise 6.19*********/
		//
		double [][] v = {{58, 53,-68},
		         {-110,-112, 35},
		         {-10,-119, 123}};


		double [][] w = {{324850,-1625176, 2734951},
		        {165782,-829409, 1395775},
		        {485054,-2426708, 4083804}};
		
		Matrix W = new Matrix(w);
		double detW = Math.abs(W.det());
		System.out.println("Determinant of public basis:"+detW);
		
		AliceGGH a = new AliceGGH(v);
		
		//get hr
		hr = ggh.getHadamardRatio(a.basis);
		System.out.println("Haramard ratio of private basis: "+hr);
		
		
		hr = ggh.getHadamardRatio(W);
		System.out.println("Haramard ratio public basis: "+hr);
		
		double[][] e2 = new double[][]{{8930810,-44681748, 75192665}};
		encrypted = new Matrix(e2);
		
		System.out.println("Encrypted message:");
		encrypted.print(2, 2);
		
		System.out.println("**Message decryption starts**");
		msg = a.decryptBabai(encrypted, W);
		System.out.println("Decrypted message->");
		msg.print(3, 3);
		
		Matrix perturbation = encrypted.minus(msg.times(W));
		System.out.println("Perturbation->");
		perturbation.print(2, 2);
		
		//decrypt bob's message with public basis
		System.out.println("Message decrypt message with public basis");
		a.basis = W;
		msg = a.decryptBabai(encrypted, W);
		System.out.println("Decrypted message with public basis->");
		msg.print(3, 3);
	}

}
