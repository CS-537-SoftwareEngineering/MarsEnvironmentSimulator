package map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class Rover {
	double x;
	double y;
	double z;
	Sphere sphere;
	
	public Rover(){
		// Initial values
		this.x = 0;
		this.y = 0;
		this.z = 0;
		
		GL11.glPushMatrix();
		//GL11.glTranslated(x,y,z);
		//GL11.glRotated(90,0,1,0);
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		sphere = new Sphere();
		sphere.draw((float) .5,10,10);
			GL11.glPopMatrix();
		}
void renderRover(double height){

	GL11.glPushMatrix();
	this.y = height;
	GL11.glTranslated(this.x,this.y,this.z);
	GL11.glRotated(90,0,1,0);
	GL11.glColor3f(0.0f, 1.0f, 0.0f);
	Sphere sphere = new Sphere();
	sphere.draw((float) .5,10,10);
	GL11.glPopMatrix();
	}
	



}
