package map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class Rover {
	
	public Rover(double x, double y, double z){
		// Initial values
		double scale = 1.0;
		double upper_arm_angle = -90.0;
		double lower_arm_angle = 90.0;
		double upper_arm_rotate = 0.0;
		double moveX = 0.0;
		double moveY = 0.5;
		double moveZ = 0.0;
		boolean active = false;
		boolean wire = false;
		boolean coord = false;
		boolean hand_open = false;
		boolean animate = false;
		boolean move_camera = false;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		GL11.glRotated(90,0,1,0);
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		Sphere sphere = new Sphere();
		sphere.draw((float) .5,10,10);
			GL11.glPopMatrix();
		}
void renderRover(double x, double y, double z){

	GL11.glPushMatrix();
	GL11.glTranslated(x,y,z);
	GL11.glRotated(90,0,1,0);
	GL11.glColor3f(0.0f, 1.0f, 0.0f);
	Sphere sphere = new Sphere();
	sphere.draw((float) .5,10,10);
		GL11.glPopMatrix();
	}
	



}
