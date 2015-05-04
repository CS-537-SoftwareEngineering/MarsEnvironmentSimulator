package map;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Heightmap {

	//An array of points to represent the y value at a given point (x,z)
	float[][] height;
	
	public Heightmap(String fname){
		try {
			BufferedImage image = ImageIO.read(new File(fname));
			
			height = new float[image.getWidth()][image.getHeight()];
			for(int x=0;x<image.getWidth();x++){
				for(int y=0;y<image.getHeight();y++){
					//Set the Y value. We can use Red because we expect the heightmap
					//to be black and white, so all the RGB values would be the same.
					//(Red value = Blue value = Green value) We just use red cause it's cool.
					Color col = new Color(image.getRGB(x, y));
					height[x][y] = (col.getRed())/255f;
					//That also means this would spit out 'true':
					//System.out.println(col.getRed() == col.getBlue() && col.getRed() == col.getGreen());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getHeightAt(int x, int z){
		//Just returns the y value at the point x,z
		try{
			return height[x][z];
		}catch (Exception e) {
			return 0;
		}
	}

	public float calculateHeight(float x, float z){
		//And here we see my initial attempt at what the professionals call "bi-linear interpolation".
		/*Vector3f[] quad = new Vector3f[4];
		float[] dist = new float[4];
		float[] contrib = new float[4];
		float totalDist;
		quad[0] = new Vector3f((float) Math.floor(x), getHeightAt((int) Math.floor(x), (int) Math.floor(z)), (float) Math.floor(z));
		quad[1] = new Vector3f((float) Math.floor(x+1), getHeightAt((int) Math.floor(x+1), (int) Math.floor(z)), (float) Math.floor(z));
		quad[2] = new Vector3f((float) Math.floor(x+1), getHeightAt((int) Math.floor(x+1), (int) Math.floor(z+1)), (float) Math.floor(z+1));
		quad[3] = new Vector3f((float) Math.floor(x), getHeightAt((int) Math.floor(x), (int) Math.floor(z+1)), (float) Math.floor(z+1));
		
		dist[0] = (float) Math.hypot(x-quad[0].x, z-quad[0].z);
		dist[1] = (float) Math.hypot(x-quad[1].x, z-quad[1].z);
		dist[2] = (float) Math.hypot(x-quad[0].x, z-quad[2].z);
		dist[3] = (float) Math.hypot(x-quad[0].x, z-quad[3].z);
		totalDist = dist[0] + dist[1] + dist[2] + dist[3];
		
		contrib[0] = (dist[0] / totalDist);
		contrib[1] = (dist[1] / totalDist);
		contrib[2] = (dist[2] / totalDist);
		contrib[3] = (dist[3] / totalDist);
		
		float out = contrib[0]*quad[0].y + contrib[1]*quad[1].y + contrib[2]*quad[2].y + contrib[3]*quad[3].y;
		return out;*/
		/*return getHeightAt((int) Math.floor(x), (int) Math.floor(z))*(1-x)*(1-z) + getHeightAt((int) Math.floor(x+1), (int) Math.floor(z))*x*(1-z) +
				getHeightAt((int) Math.floor(x), (int) Math.floor(z+1))*(1-x)*z + getHeightAt((int) Math.floor(x+1), (int) Math.floor(z+1))*x*z;*/
		
		//And here we see what professionals would know as bi-linear interpolation.
		//It was a lot of fun playing with this algorithm, I suggest you google it!
		float x1, x2, y1, y2, Q11, Q12, Q21, Q22;
		x1 = (int) Math.floor(x);
		x2 = (int) Math.floor(x+1);
		y1 = (int) Math.floor(z);
		y2 = (int) Math.floor(z+1);
		
		Q11 = getHeightAt((int) x1, (int) y1);
		Q12 = getHeightAt((int) x1, (int) y2);
		Q21 = getHeightAt((int) x2, (int) y1);
		Q22 = getHeightAt((int) x2, (int) y2);
		
		return (1f/( (x2-x1)*(y2-y1) ))*( Q11*(x2-x)*(y2-z)+
				Q21*(x-x1)*(y2-z)+
				Q12*(x2-x)*(z-y1)+
				Q22*(x-x1)*(z-y1));
	}
	
	public float calculateHeightRounded(float x, float z) {
		return getHeightAt(Math.round(x), Math.round(z));
	}
}
