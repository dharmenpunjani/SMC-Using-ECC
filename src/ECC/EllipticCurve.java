package ECC;



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

	
	public class EllipticCurve 
	{

		/**
		 * @param args
		 */
		BigInteger a,b;
		BigInteger n;
		int A, B;
		int ordr = 0;
		Point gen ;
		Point gBig;
		public EllipticCurve(BigInteger A, BigInteger B,Point gen,BigInteger ordr)
		{
			//y2 = x3 + Ax+ B
			this.a = A;
			this.b = B;
			this.gBig= gen;
			this.n = ordr;
		}
		public EllipticCurve(int A, int B,Point gen,int ordr)
		{
			//y2 = x3 + Ax+ B
			this.A = A;
			this.B = B;
			this.gen= gen;
			this.ordr = ordr;
		}
		boolean isValidBig()
		{
			BigInteger determinant = a.multiply(new BigInteger("4")).multiply(a).multiply(a).add(b.multiply(b).multiply(b).multiply(new BigInteger("27")));//4*a*a*a + 27*b*b;
			if(determinant.intValue()==0)
				return false;
			return true;
		}
		boolean isValid()
		{
			double determinant = 4*A*A*A + 27*B*B;
			if(determinant==0)
				return false;
			return true;
		}
		public boolean isInverseBig(Point p1,Point p2,BigInteger fp)
		{
			boolean ans = false;
			
			if(p1.Y.compareTo(new BigInteger("0"))==-1)
				p1.Y = p1.Y.multiply(new BigInteger("-1"));
			if(p2.Y.compareTo(new BigInteger("0"))==-1)
				p2.Y = p2.Y.multiply(new BigInteger("-1"));
			
			if(p1.X.equals(p2.X))
			{
				if(p1.Y.add(p2.Y).equals(new BigInteger(fp+"")))
					ans = true;
			}
			return ans;
		}
		public boolean isInverse(Point p1,Point p2,int fp)
		{
			boolean ans = false;
			
			if(p1.y<0)
				p1.y *= -1;
			if(p2.y<0)
				p2.y *= -1;
			
			if(p1.x == p2.x)
			{
				if(p1.y +p2.y == fp )
					ans = true;
			}
			return ans;
		}
		
		public Point additionBig(Point p1, Point p2, BigInteger fp) 
		{
			Point p3;
			BigInteger x3,y3;
			//double x3, y3;
			BigInteger numerator;
			BigInteger denominator;
			
			if(p1==null || p2 ==null)
				return null;
			if(p1.isEqualBig(Point.ZeroBig))
				return p2;
			else if(p2.isEqualBig(Point.ZeroBig))
				return p1;
			else
			{
				
				if(p1.X.equals(p2.X) && p1.Y.add(p2.Y).equals(fp))
					return doubleAndAddBig(gBig, n, fp);
				
				else
				{
					if(p1.Y.compareTo(new BigInteger("0"))==-1)
						p1.Y = p1.Y.multiply(new BigInteger("-1"));
					if(p2.Y.compareTo(new BigInteger("0"))==-1)
						p2.Y = p2.Y.multiply(new BigInteger("-1"));
					
					BigInteger slope = new BigInteger("0");
					if(p1.isEqualBig(p2))
					{
						
						numerator = p1.X.multiply(p1.X).multiply(new BigInteger("3")).add(a);//3*p1.x*p1.x + A;
						denominator = p1.Y.multiply(new BigInteger("2"));//2*p1.y;
						
					}
					else
					{
						numerator = p2.Y.subtract(p1.Y);//p2.y-p1.y;
						denominator =p2.X.subtract(p1.X); //p2.x-p1.x;
						
						
					}
					if(fp.compareTo(new BigInteger("0"))!=0)
					{
						if(denominator.compareTo(new BigInteger("0"))<0)
							denominator = denominator.add(fp);
						denominator = crypto.inverseBig(fp, denominator);
						
					}
					else
					{
						denominator = new BigInteger("1").divide(denominator);
					}
					slope = numerator.multiply(denominator);
					if(fp.compareTo(new BigInteger("0"))!=0)
						slope = slope.mod(fp);
					x3 = slope.multiply(slope).subtract(p1.X).subtract(p2.X);//slope*slope - p1.x - p2.x;
					//System.out.println("X3:"+x3);
					if(fp.compareTo(new BigInteger("0"))!=0)
					{
						x3 = x3.mod(fp);
						//System.out.println("X3:"+x3);
						/*if(x3.compareTo(new BigInteger("0"))==-1)
							x3 = x3.add(fp);*/
					}
					y3 = slope.multiply(p1.X.subtract(x3)).subtract(p1.Y);//slope*(p1.x - x3) - p1.y;
					//System.out.println("Y3:"+y3);
					if(fp.compareTo(new BigInteger("0"))!=0)
					{
						y3 = y3.mod(fp);
						//System.out.println("Y3:"+y3);
						/*if(y3.compareTo(new BigInteger("0"))==-1)
							y3= y3.add(fp);*/
					}
					
					p3 = new Point(x3, y3);
					
					return p3;
				}
			}
		}
		
		//if fp==0 then do general addition, otherwise do fp addition
		public Point addition(Point p1, Point p2, long fp) 
		{
			Point p3;
			double x3, y3;
			double numerator;
			double denominator;
			
			if(p1==null || p2 ==null)
				return null;
			if(p1.isEqual(Point.Zero))
				return p2;
			else if(p2.isEqual(Point.Zero))
				return p1;
			else
			{
				
				if(p1.x==p2.x && p1.y==-p2.y)
					return doubleAndAdd(gBig, ordr, fp);
				
				else
				{
					if(p1.y<0)
						p1.y *= -1;
					if(p2.y<0)
						p2.y *= -1;
					
					double slope = 0;
					if(p1.isEqual(p2))
					{
						numerator = 3*p1.x*p1.x + A;
						denominator = 2*p1.y;
						
					}
					else
					{
						numerator = p2.y-p1.y;
						denominator = p2.x-p1.x;
						
						
					}
					if(fp!=0)
					{
						if(denominator<0)
							denominator+=fp;
						denominator = crypto.inverse(fp, (long)denominator);
						
					}
					else
					{
						denominator = 1/denominator;
					}
					slope = numerator*denominator;
					if(fp!=0)
						slope = slope%fp;
					x3 = slope*slope - p1.x - p2.x;
					
					if(fp!=0)
					{
						x3 = x3%fp;
						if(x3<0)
							x3+=fp;
					}
					y3 = slope*(p1.x - x3) - p1.y;
					
					if(fp!=0)
					{
						y3 = y3%fp;
						
						if(y3<0)
							y3+=fp;
					}
					if(x3<0)
						x3 += fp;
					if(y3<0)
						y3 += fp;
					p3 = new Point(x3, y3);
					
					return p3;
				}
			}
		}
		//get points in Fp field
		public List<Point> getPoints(int fp)
		{
			long y2 = 0;
			List<Point> allPoints = new ArrayList<Point>();
			allPoints.add(Point.Zero);
			
			for(int x=0; x<fp; x++)
			{
				//get y
				y2 = x*x*x + this.A*x+this.B;
				List<Long> sr = SquareRoot.squareRoot(y2, fp);
				for(long y:sr)
				{
					    if(y==1||y==fp-1) continue;
						Point p = new Point(x,y);
						allPoints.add(p);
				}
				
			}
			return allPoints;
		}
		
		
		//double-and-add algorithm for elliptic curves
		//returns r = n*p mod fp
		
		public Point doubleAndAddBig(Point p, BigInteger n1, BigInteger fp)
		{
			Point r = Point.ZeroBig;
			Point q = p;
			while(n1.compareTo(new BigInteger("0"))>0)
			{
				
				
				if(n1.mod(new BigInteger("2")).equals(new BigInteger("1")))
				{
					//System.out.print("yup");
					r = additionBig(r, q, fp);
					
				}
				//System.out.println(n1+"&("+q.X+","+q.Y+")&("+r.X+","+r.Y+")");
				q = additionBig(q,q,fp);
				
				n1 = n1.divide(new BigInteger("2"));
				//System.out.println("the N:"+n1);
				
			}
			
			//r = addition(r, q, fp);
			return r;
			
		}
		public Point doubleAndAdd(Point p, long n, long fp)
		{
			Point r = Point.Zero;
			Point q = p;
			while(n>0)
			{
				//System.out.println(n+"&("+q.x+","+q.y+")&("+r.x+","+r.y+")");
				
				if(n%2==1)
				{
					r = addition(r, q, fp);
					
				}
				q = addition(q,q,fp);
				
				n = n/2;
				
			}
			
			//r = addition(r, q, fp);
			return r;
			
		}
		
		
		
		public static void main(String[] args) 
		{
			int A = 1;
			int test = 0xB;
			BigInteger a = new BigInteger("602060795339016484143949354474116343558714560760195967177227");
			BigInteger b = new BigInteger("42352398925875963961914576668959717307495502739895755462837");
			
			BigInteger fpBig = new BigInteger("1253565240884932978867327564152117754558173168651037992019849");
			BigInteger n = new BigInteger("125356524088493297886732756415014869282522690604601115185239211");	
			ECC.Point genBig = new ECC.Point(new BigInteger("-308713420171208600284839010152791755784186567349039150579062"),new BigInteger("494952139410905522093322548267656122025755798264866184081137"));
			int B = 6;
			int fp = 11;
			int ordr = 13;
			int infiInt = ordr%fp;
			//System.out.println(infiInt);
			int infCount = 0;
			Point gen = new Point(2.0, 7.0);
			BigInteger a1 = new BigInteger("602060795339016484143949354474116343558714560760195967177227");
			//System.out.println("ByteValues: "+a1.bitLength());
			//Point genBig = new Point(new BigInteger("-308713420171208600284839010152791755784186567349039150579062"),new BigInteger("494952139410905522093322548267656122025755798264866184081137"));
			EllipticCurve ec = new EllipticCurve(A, B,gen,ordr);
			EllipticCurve ecBig = new EllipticCurve(a, b, genBig, n);
			Integer testi = new Integer(1);
			
			//Point fillerPoint = ec.doubleAndAdd(gen, infiInt, fp);
			Point fillerPointBig = ecBig.doubleAndAddBig(genBig, new BigInteger(infiInt+""), fpBig);
			System.out.println("Filler Point : ("+fillerPointBig.X+","+fillerPointBig.Y+")");
			//HashMap<String, Integer> mapAllInt = new HashMap<String,Integer>();
			HashMap<String, BigInteger> mapAllIntBig = new HashMap<String,BigInteger>();
	        List<Point> allPoints = ec.getPoints(fp);
	       /* System.out.println("Points in E(F"+fp+") are :");
	        for(Point p:allPoints)
	        {
	        	System.out.println("("+p.x+","+p.y+")");
	        }*/
	        
	        System.out.println("Mapping Scalar Value to the Point On the Elliptic Curve:");
	        BigInteger counter = new BigInteger("0");
	        BigInteger condition = new BigInteger("10000");// Total number of points to be generated and maaped on the curve
	        for( ;counter.compareTo(condition)<1;counter=counter.add(new BigInteger("1")))//(fp+1+2*Math.floor(Math.sqrt(fp)));i++)
	        {
	        	Point map =  ecBig.doubleAndAddBig(genBig, counter, fpBig);
	        	//mapAllInt.put(map.x+":"+map.y, i);
	        	mapAllIntBig.put(map.X+":"+ map.Y+"",counter);
	        	//System.out.println("The Scalar Value: "+counter+" : "+" Cordinate: "+map.X+" Y-Cordinate: "+map.Y);
	        }
	        //addition test p1 is the first secret value p2 is second seceret value p3 is third secret value
	        Point p1 = ecBig.doubleAndAddBig(genBig, new BigInteger("189"), fpBig);//new Point(new BigInteger("246144621912899490727808978672382138005956013348544094940708"),new BigInteger("1230572732779919380702879843480696237057724696259585774133547"));
	        Point p2 = ecBig.doubleAndAddBig(genBig, new BigInteger("48"), fpBig);//new Point(new BigInteger("291922471560294002110507216303521966886274717961380676375211"),new BigInteger("586734597560060904513793577946206953024311656660376795561160"));
	        Point p3 = ecBig.doubleAndAddBig(genBig, new BigInteger("546"), fpBig);//new Point(new BigInteger("246144621912899490727808978672382138005956013348544094940708"),new BigInteger("1230572732779919380702879843480696237057724696259585774133547")); 
	        int k1 = 4,k2=2,k3=3;
	        int x1 = 6,x2=4,x3=9;
	        
	        long start = System.nanoTime();
	        Point Y1 = ecBig.doubleAndAddBig(genBig, new BigInteger(x1+""), fpBig);
	        long end = System.nanoTime();
	      //  System.out.println("Time Diff is: "+ (end - start) + " ms");
	        Point Y2 = ecBig.doubleAndAddBig(genBig, new BigInteger(x2+""), fpBig);
	        Point Y3 = ecBig.doubleAndAddBig(genBig, new BigInteger(x3+""), fpBig);
	        Point k1Y1 = ecBig.doubleAndAddBig(Y1, new BigInteger(k1+""), fpBig);
	        Point k2Y2 = ecBig.doubleAndAddBig(Y2, new BigInteger(k2+""), fpBig);
	        //Point k3Y3 = ec.doubleAndAddBig(Y3,new BigInteger(k3+""), fpBig);
	        
	        while(ec.isInverseBig(k1Y1, p1, fpBig)|| x1==0 || k1==0)
	        {
	        	Random r = new Random();
	        	k1 = r.nextInt() % (fp/2);
	        	x1 = r.nextInt() % (fp/2);
	        	if(k1<0)
	        		k1 *= -1;
	        	if(x1<0)
	        		x1 *= -1;
	        	Y1 = ecBig.doubleAndAddBig(genBig, new BigInteger(x1+""), fpBig);
	            k1Y1 = ecBig.doubleAndAddBig(Y1, new BigInteger(k1+""), fpBig);
	           // System.out.println("k1: "+ k1+" X1: "+x1);
	        }
	        Point msg1 = ecBig.additionBig(p1, k1Y1, fpBig);
	       /* if(msg1.x == Double.NEGATIVE_INFINITY)
	        {
	        	msg1 = fillerPoint;
	        	infCount++;
	        }*/
	        Point msg2 = ecBig.additionBig(p2, k2Y2, fpBig);
	      
	        Point k1G = ecBig.doubleAndAddBig(genBig, new BigInteger(k1+""), fpBig);
	        Point k2G = ecBig.doubleAndAddBig(genBig, new BigInteger(k2+""), fpBig);
	        Point k3G = ecBig.doubleAndAddBig(genBig, new BigInteger(k3+""), fpBig);
	        Point p1Sub = ecBig.doubleAndAddBig(k1G, new BigInteger(x1+""), fpBig);
	       // System.out.println("k1G ("+k1G.X+","+k1G.Y+")");
	        start = System.nanoTime();
	        Point testTime = ecBig.doubleAndAddBig(genBig, new BigInteger("1234567890"), fpBig);
	        end = System.nanoTime();
	     //   System.out.println("Diff New: "+((double)(end-start)/1000000.0)+" ns");
	        Point p2Sub = ecBig.doubleAndAddBig(k2G, new BigInteger(x2+""), fpBig);
	        //System.out.println("k2G ("+k2G.X+","+k2G.Y+")");
	        
	        
	     //   Point p3Sub = ec.doubleAndAddBig(k3G, new BigInteger(x3+""), fpBig);
	      //  System.out.println("k3G ("+k3G.X+","+k3G.Y+")");
	       
	        Point sumAllmsg = new Point(new BigInteger("0"),new BigInteger("0"));
	        if(ec.isInverseBig(msg1, p2, fpBig))
	        {
	        	sumAllmsg = fillerPointBig;
	        	infCount ++;
	        }
	        else
	        {
	        	sumAllmsg = ecBig.additionBig(msg1, p2, fpBig);
	        }
	        
	        System.out.println("Adding first two: "+sumAllmsg.X+","+sumAllmsg.Y);
	        if(ec.isInverseBig(sumAllmsg, p3, fpBig))
	        {
	        	sumAllmsg = fillerPointBig;
	        	infCount ++;
	        }
	        else
	        {
	        	
	        	sumAllmsg = ecBig.additionBig(sumAllmsg, p3, fpBig);
	        	
	        }
	        p1Sub.setYBig(p1Sub.Y.multiply(new BigInteger("-1")).add(fpBig));//p1Sub.Y*-1 + fp);
	        p2Sub.setY(p2Sub.y*-1);
	      //  p3Sub.setY(p3Sub.y*-1);
	        System.out.println("Sum: "+sumAllmsg.X+","+sumAllmsg.Y);
	        System.out.println("p1Sub ("+p1Sub.X+","+p1Sub.Y+")");
	        System.out.println("p2Sub ("+p2Sub.X+","+p2Sub.Y+")");
	    //    System.out.println("p3Sub ("+p3Sub.x+","+p3Sub.y+")");
	        
	        start = System.nanoTime();
	        sumAllmsg = ecBig.additionBig(sumAllmsg, p1Sub, fpBig);
	        end = System.nanoTime();
	        System.out.println("After Removing Noise: "+sumAllmsg.X+","+sumAllmsg.Y);//+"\nDiff Addition: "+(end-start));
	        fillerPointBig.setYBig(fillerPointBig.Y.multiply(new BigInteger("-1")).add(fpBig));//fillerPoint.y*-1 + fp);
	        while(infCount>0)
	        {
	        	System.out.println("Inside");
	        	sumAllmsg  = ecBig.additionBig(sumAllmsg, fillerPointBig, fpBig);
	        	infCount--;
	        }
	        
	      //  sumAllmsg = ec.addition(sumAllmsg, p2Sub, fp);
	        //System.out.println(sumAllmsg.x+","+sumAllmsg.y);
	        //sumAllmsg = ec.addition(sumAllmsg, p3Sub, fp);
	        System.out.println(sumAllmsg.X+","+sumAllmsg.Y);
	      //  sumAllmsg = ec.addition(sumAllmsg, p2Sub, fp);
	       // sumAllmsg = ec.addition(sumAllmsg, p3Sub, fp);
	       // Point q = ec.addition(p1, new Point(5,9), fp);
	        //q = ec.addition(q, p3, fp);
	        System.out.println("Addition: ");
	        System.out.println("("+sumAllmsg.X+","+sumAllmsg.Y+")");// + ("+p2.X+","+p2.Y+") ="+"("+q.X+","+q.Y+")");
	        System.out.println("The Sum is: "+mapAllIntBig.get(sumAllmsg.X+":"+sumAllmsg.Y));
	        //double and add : nP mod p
	       
//	        Point P = new Point(6, 730);
//	        EllipticCurve ec2 = new EllipticCurve(14, 19,gen,10);
//			
//			//System.out.println("double-and-add algorithm for elliptic curve->");

		}

	}