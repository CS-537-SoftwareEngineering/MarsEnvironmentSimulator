package map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	
	/*
	 *  NOTE ------------
	 *  This is actually a rip of code from the qjuake engine
	 *  that has been ripped up to rid of the gravity and jumping.
	 *  Unfortunately, the variables for gravity/jumping are still here
	 *  doing nothing at all. Just ignore them. Or re-implement jumping/gravity.
	 *  That should be fun.
	 */
	
	static final float walkSpeed = 0.035f, runSpeed = 0.5f, sprintSpeed = 0.18f;
	static float speed = runSpeed;

	Vector3f vector = new Vector3f();
	Vector3f rotation = new Vector3f();
	Vector3f previous = new Vector3f();
	boolean moveForward=false, moveBackward=false, strafeLeft=false, strafeRight=false;
	boolean onGround=false, jumping=false, falling=false;
	boolean walking=false, running=true, sprinting=false;
	Game world;
	float yBottom=0;
	float yMotion=0;
	final float gravity = 0.0155f;

	public Camera(Game app){
		world = app;
	}

	public void update(){
		updatePreviousVector();
		updateMotion();
		input();
	}

	public void input(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			moveForward = true;
		}else{
			moveForward = false;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			moveBackward = true;
		}else{
			moveBackward = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			strafeLeft = true;
		}else{
			strafeLeft = false;
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			strafeRight = true;
		}else{
			strafeRight = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			Display.destroy();
			System.exit(0);
		}
		if(Mouse.isGrabbed()){
			float mouseDX = Mouse.getDX() * 0.8f * 0.16f;
			float mouseDY = Mouse.getDY() * 0.8f * 0.16f;
			if (rotation.y + mouseDX >= 360) {
				rotation.y = rotation.y + mouseDX - 360;
			} else if (rotation.y + mouseDX < 0) {
				rotation.y = 360 - rotation.y + mouseDX;
			} else {
				rotation.y += mouseDX;
			}
			if (rotation.x - mouseDY >= -89 && rotation.x - mouseDY <= 89) {
				rotation.x += -mouseDY;
			} else if (rotation.x - mouseDY < -89) {
				rotation.x = -89;
			} else if (rotation.x - mouseDY > 89) {
				rotation.x = 89;
			}
		}
	}

	public void updatePreviousVector(){
		previous.x = vector.x;
		previous.y = vector.y;
		previous.z = vector.z;
	}

	public void updateMotion(){
		if(walking && !sprinting){
			speed = walkSpeed;
		}else if(!walking && sprinting){
			speed = sprintSpeed;
		}else if(running){
			speed = runSpeed;
		}
		if(moveForward){
			vector.x += Math.sin(rotation.y*Math.PI/180)*speed;
			vector.z += -Math.cos(rotation.y*Math.PI/180)*speed;
		}
		if(moveBackward){
			vector.x -= Math.sin(rotation.y*Math.PI/180)*speed;
			vector.z -= -Math.cos(rotation.y*Math.PI/180)*speed;
		}
		if(strafeLeft){
			vector.x += Math.sin((rotation.y-90)*Math.PI/180)*speed;
			vector.z += -Math.cos((rotation.y-90)*Math.PI/180)*speed;
		}
		if(strafeRight){
			vector.x += Math.sin((rotation.y+90)*Math.PI/180)*speed;
			vector.z += -Math.cos((rotation.y+90)*Math.PI/180)*speed;
		}
	}
}
