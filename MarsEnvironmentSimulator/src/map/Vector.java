package map;

public class Vector {
	public static float[] getNormal(float[] p1, float[] p2, float[] p3)
	{
		float[] v1 = getVector(p1, p2);
		float[] v2 = getVector(p1, p3);
		float[] normal = crossProduct(v1, v2);
		
		if(normal[1] < 0 )
		{
			normal[0] *= -1;
			normal[1] *= -1;
			normal[2] *= -1;
		}
		
		normal = normalize(normal);
		
		return normal;
	}
	
	private static float[] getVector(float[] p1, float[] p2)
	{
		float[] vector = {p2[0] - p1[0], p2[1] - p1[1], p2[2] - p1[2]};
		
		return vector;
	}
	
	private static float[] crossProduct(float[] v1, float[] v2)
	{
		float[] vector = {v1[1] * v2[2] - v1[2] * v2[1], v1[2] * v2[0] - v1[0] * v2[2], v1[0] * v2[1] - v1[1] * v2[0]};
		
		return vector;
	}
	
	private static float[] normalize(float[] v)
	{
		float mag = 0;
		
		for(float x : v)
		{
			mag += Math.pow(x, 2);
		}
		
		mag = (float) Math.sqrt(mag);
		
		v[0] /= mag;
		v[1] /= mag;
		v[2] /= mag;
		
		return v;
	}
}
