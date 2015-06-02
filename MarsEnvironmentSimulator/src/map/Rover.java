package map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public class Rover {
	double x, y, z;
	double roverAngle, shoulderHorizontalAngle, shoulderVerticalAngle, elbowAngle, wristAngle, cameraAngle, cameraRotationAngle, wheelAngle;
	boolean cameraOn;
	Cylinder cylinder;
	Sphere joint;

	public Rover() {
		// Initial values
		this.x = 129;
		this.y = 0;
		this.z = 65;
		this.roverAngle = 0;
		this.shoulderHorizontalAngle = -90;
		this.shoulderVerticalAngle = 17;
		this.elbowAngle = 163;
		this.wristAngle = 0;
		this.cameraAngle = 0;
		this.cameraRotationAngle = 0;
		this.wheelAngle = 0;
		this.cameraOn = true;
		
		cylinder = new Cylinder();
		joint = new Sphere();

//		GL11.glPushMatrix();
//		GL11.glTranslated(x,y,z);
//		GL11.glRotated(90,0,1,0);
//		GL11.glColor3f(0.0f, 1.0f, 0.0f);
//		sphere = new Sphere();
//		sphere.draw((float) .5, 10, 10);
//		GL11.glPopMatrix();
	}

	void renderRover(double height) {
		this.y = height;

		GL11.glPushMatrix(); // push for whole thing
		GL11.glTranslated(this.x, this.y, this.z); // ROVER MOVEMENT
		GL11.glTranslated(0, 0.25, 0); // elevate the body off the ground a bit
		GL11.glRotated(roverAngle, 0, 1, 0); // ROVER ROTATION
		// GL11.glRotated(90, 0, 1, 0);
		// GL11.glColor3f(1.0f, 1.0f, 1.0f);
		// sphere = new Sphere();
		// sphere.draw((float) .5, 10, 10);
			
		// body
		colorWhite();
		GL11.glPushMatrix();
		GL11.glScaled(1, 0.5, 0.7);
		drawCube(0.5f);
		GL11.glPopMatrix();
		
		// wheels
		colorBlack();
		GL11.glPushMatrix();
		GL11.glTranslated(-0.35, -0.25, 0.3);
		GL11.glRotated(wheelAngle, 0, 0, 1);
		drawWheel();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0, -0.25, 0.3);
		GL11.glRotated(wheelAngle, 0, 0, 1);
		drawWheel();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0.35, -0.25, 0.3);
		GL11.glRotated(wheelAngle, 0, 0, 1);
		drawWheel();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(-0.35, -0.25, -0.5);
		GL11.glRotated(wheelAngle, 0, 0, 1);
		drawWheel();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0, -0.25, -0.5);
		GL11.glRotated(wheelAngle, 0, 0, 1);
		drawWheel();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0.35, -0.25, -0.5);
		GL11.glRotated(wheelAngle, 0, 0, 1);
		drawWheel();
		GL11.glPopMatrix();
		
		// shoulder
		colorBlack();
		GL11.glPushMatrix(); // push for shoulder
		GL11.glTranslated(0.55, 0, -0.2);
		GL11.glRotated(shoulderHorizontalAngle, 0, 1, 0); // SHOULDER HORIZONTAL ROTATION
		GL11.glRotated(shoulderVerticalAngle, 0, 0, 1); // SHOULDER VERTICAL ROTATION
		joint.draw(0.1f, 10, 10);
		
		// upper arm
		colorWhite();
		GL11.glPushMatrix();
		GL11.glRotated(90, 0, 1, 0);
		cylinder.draw(0.035f, 0.035f, 0.4f, 10, 10);
		GL11.glPopMatrix();
		
		// elbow
		colorBlack();
		GL11.glPushMatrix(); // push for elbow
		GL11.glTranslated(0.45, 0, 0);
		GL11.glRotated(elbowAngle, 0, 0, 1); // ELBOW VERTICAL ROTATION
		joint.draw(0.07f, 10, 10);
		
		// lower arm
		colorWhite();
		GL11.glPushMatrix();
		GL11.glRotated(90, 0, 1, 0);
		cylinder.draw(0.03f, 0.03f, 0.4f, 10, 10);
		GL11.glPopMatrix();
		
		// wrist
		colorBlack();
		GL11.glPushMatrix(); // push for wrist
		GL11.glTranslated(0.4, 0, 0);
		GL11.glRotated(wristAngle, 0, 0, 1); // WRIST VERTICAL ROTATION
		joint.draw(0.04f, 10, 10);
		
		// arm tip
		colorBlack();
		GL11.glPushMatrix();
		GL11.glTranslated(0.1, 0, 0);
		drawCube(0.07f);
		GL11.glPopMatrix();
		
		GL11.glPopMatrix(); // pop for wrist
		GL11.glPopMatrix(); // pop for elbow
		GL11.glPopMatrix(); // pop for shoulder
		
		// top camera joint
		colorSilver();
		GL11.glPushMatrix(); // camera joint push
		GL11.glTranslated(0.4, 0.27, 0.25);
		GL11.glRotated(cameraRotationAngle, 0, 1, 0); // CAMERA HEAD ROTATION
		GL11.glRotated(cameraAngle, 0, 0, 1); // CAMERA ON/OFF POSITIONS
		joint.draw(0.06f, 10, 10);
		
		// top camera stick
		colorBlack();
		GL11.glPushMatrix();
		GL11.glTranslated(0,  0.05, 0);
		GL11.glRotated(-90, 1, 0, 0);
		cylinder.draw(0.03f, 0.03f, 0.35f, 10, 10);
		GL11.glPopMatrix();
		
		// top camera head
		colorSilver();
		GL11.glPushMatrix();
		GL11.glTranslated(0, 0.39, 0);
		GL11.glScaled(0.7, 1, 1.2);
		drawCube(0.07f);
		GL11.glPopMatrix();
		
		GL11.glPopMatrix(); // camera joint pop
		GL11.glPopMatrix(); // pop for whole thing
	}
	
	void drawCube(float side) {
		GL11.glBegin(GL11.GL_QUADS);
		
		// top
		GL11.glNormal3f(0f, 1f, 0f);
		GL11.glVertex3f(side, side, -side);
		GL11.glVertex3f(-side, side, -side);
		GL11.glVertex3f(-side, side, side);
		GL11.glVertex3f(side, side, side);

		// bottom
		GL11.glNormal3f(0f, -1f, 0f);
		GL11.glVertex3f(side, -side, side);
		GL11.glVertex3f(-side, -side, side);
		GL11.glVertex3f(-side, -side, -side);
		GL11.glVertex3f(side, -side, -side);

		// near
		GL11.glNormal3f(0f, 0f, 1f);
		GL11.glVertex3f(side, side, side);
		GL11.glVertex3f(-side, side, side);
		GL11.glVertex3f(-side, -side, side);
		GL11.glVertex3f(side, -side, side);

		// far
		GL11.glNormal3f(0f, 0f, -1f);
		GL11.glVertex3f(side, -side, -side);
		GL11.glVertex3f(-side, -side, -side);
		GL11.glVertex3f(-side, side, -side);
		GL11.glVertex3f(side, side, -side);
		
		// left
		GL11.glNormal3f(-1f, 0f, 0f);
		GL11.glVertex3f(-side, side, side);
		GL11.glVertex3f(-side, side, -side);
		GL11.glVertex3f(-side, -side, -side);
		GL11.glVertex3f(-side, -side, side);

		// right
		GL11.glNormal3f(1f, 0f, 0f);
		GL11.glVertex3f(side, side, -side);
		GL11.glVertex3f(side, side, side);
		GL11.glVertex3f(side, -side, side);
		GL11.glVertex3f(side, -side, -side);
		
		GL11.glEnd();
	}
	
	void drawWheel() {
		cylinder.draw(0.15f, 0.15f, 0.2f, 10, 10);
	}
	
	void colorSilver() {
		GL11.glColor3f(0.7f, 0.7f, 0.7f); // SILVER
	}
	
	void colorBlack() {
		GL11.glColor3f(0.0f, 0.0f, 0.0f); // BLACK
	}
	
	void colorWhite() {
		GL11.glColor3f(1.0f, 0.5f, 0.0f); // WHITE
	}
}
