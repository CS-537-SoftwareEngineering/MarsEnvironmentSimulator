package map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	/*
	 * NOTE ------------ This is actually a rip of code from the qjuake engine
	 * that has been ripped up to rid of the gravity and jumping. Unfortunately,
	 * the variables for gravity/jumping are still here doing nothing at all.
	 * Just ignore them. Or re-implement jumping/gravity. That should be fun.
	 */

	static final float walkSpeed = 0.035f, runSpeed = 0.5f, sprintSpeed = 0.18f;
	static float speed = runSpeed;

	Vector3f vector = new Vector3f();
	Vector3f rotation = new Vector3f();
	Vector3f previous = new Vector3f();
	boolean moveForward = false, moveBackward = false, strafeLeft = false, strafeRight = false;
	boolean onGround = false, jumping = false, falling = false;
	boolean walking = false, running = true, sprinting = false;
	Game world;
	float yBottom = 0;
	float yMotion = 0;
	final float gravity = 0.0155f;
	boolean forward = false, backward = false, left = false, right = false;
	boolean shoulderUp = false, shoulderDown = false, shoulderLeft = false, shoulderRight = false, elbowUp = false, 
			elbowDown = false, wristUp = false, wristDown = false, cameraLeft = false, cameraRight = false;

	public Camera(Game app) {
		world = app;
	}

	public void update() {
		updatePreviousVector();
		updateMotion();
		input();
	}

	// Animated controls
	public void autoInput(int commandNumber, int magnitude) {
		// commandNumber dictionary
		// 1 - roverAngle
		// 2 - shoulderHorizontalAngle
		// 3 - shoulderVerticalAngle
		// 4 - elbowAngle
		// 5 - wristAngle
		// 6 - cameraRotationAngle
		// 7 - forward
		// 8 - backward
		
		int counter = 0;
		
		switch(commandNumber) {
		case 1:
			// degree to turn, + for left turn, - for right turn
			while (counter != magnitude) {
				if (counter < magnitude) { // left turn
					left = true;
					counter++;
				} else if (counter > magnitude) { // right turn
					right = true;
					counter--;
				}
			}
			left = false;
			right = false;
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		}
	}
	
	// Manual controls
	public void input() {
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveForward = true;
		} else {
			moveForward = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveBackward = true;
		} else {
			moveBackward = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			strafeLeft = true;
		} else {
			strafeLeft = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			strafeRight = true;
		} else {
			strafeRight = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			Display.destroy();
			System.exit(0);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
			if (Program.top_view) {
				Program.top_view = false;
			} else {
				Program.top_view = true;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
		//	System.out.println(Game.heightmap.toString());
			System.out.println("{");
			for (int x = 0; x < Game.heightmap.height.length; x++) {
				System.out.print("{");
				for (int z = 0; z < Game.heightmap.height[x].length-1; z++) {
					System.out.print(Game.heightmap.getHeightAt(x, z)*Game.heightmapExaggeration + ",");
				}
				System.out.print(Game.heightmap.getHeightAt(x,Game.heightmap.height[x].length)*Game.heightmapExaggeration);
				System.out.println("}");
			}
			System.out.println("}");
		}
		
		if (Mouse.isGrabbed()) {
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
		
		// Rover controls
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			System.out.println("Up key pressed");
			forward = true;
		} else {
			forward = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			System.out.println("Down key pressed");
			backward = true;
		} else {
			backward = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			System.out.println("Left key pressed");
			left = true;
		} else {
			left = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			System.out.println("Right key pressed");
			right = true;
		} else {
			right = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
			System.out.println("I key pressed");
			shoulderUp = true;
		} else {
			shoulderUp = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			System.out.println("K key pressed");
			shoulderDown = true;
		} else {
			shoulderDown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			System.out.println("J key pressed");
			shoulderLeft = true;
		} else {
			shoulderLeft = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			System.out.println("L key pressed");
			shoulderRight = true;
		} else {
			shoulderRight = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			System.out.println("H key pressed");
			elbowUp = true;
		} else {
			elbowUp = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			System.out.println("N key pressed");
			elbowDown = true;
		} else {
			elbowDown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			System.out.println("G key pressed");
			wristUp = true;
		} else {
			wristUp = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			System.out.println("B key pressed");
			wristDown = true;
		} else {
			wristDown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			System.out.println("Y key pressed");
			cameraLeft = true;
		} else {
			cameraLeft = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			System.out.println("U key pressed");
			cameraRight = true;
		} else {
			cameraRight = false;
		}
	}

	public void updatePreviousVector() {
		previous.x = vector.x;
		previous.y = vector.y;
		previous.z = vector.z;
	}

	public void updateMotion() {
		if (walking && !sprinting) {
			speed = walkSpeed;
		} else if (!walking && sprinting) {
			speed = sprintSpeed;
		} else if (running) {
			speed = runSpeed;
		}
		
		if (moveForward) {
			vector.x += Math.sin(rotation.y * Math.PI / 180) * speed;
			vector.z += -Math.cos(rotation.y * Math.PI / 180) * speed;

			Game.rover.x += Math.sin(rotation.y * Math.PI / 180) * speed;
			Game.rover.z += -Math.cos(rotation.y * Math.PI / 180) * speed;
		}
		
		if (moveBackward) {
			vector.x -= Math.sin(rotation.y * Math.PI / 180) * speed;
			vector.z -= -Math.cos(rotation.y * Math.PI / 180) * speed;

			Game.rover.x -= Math.sin(rotation.y * Math.PI / 180) * speed;
			Game.rover.z -= -Math.cos(rotation.y * Math.PI / 180) * speed;
		}
		
		if (strafeLeft) {
			vector.x += Math.sin((rotation.y - 90) * Math.PI / 180) * speed;
			vector.z += -Math.cos((rotation.y - 90) * Math.PI / 180) * speed;

			Game.rover.x += Math.sin((rotation.y - 90) * Math.PI / 180) * speed;
			Game.rover.z += -Math.cos((rotation.y - 90) * Math.PI / 180) * speed;
		}
		
		if (strafeRight) {
			vector.x += Math.sin((rotation.y + 90) * Math.PI / 180) * speed;
			vector.z += -Math.cos((rotation.y + 90) * Math.PI / 180) * speed;

			Game.rover.x += Math.sin((rotation.y + 90) * Math.PI / 180) * speed;
			Game.rover.z += -Math.cos((rotation.y + 90) * Math.PI / 180) * speed;

		}
		
		// Rover contorls
		if (forward) {
			// move rover to direction of head
			vector.x += Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			vector.z -= Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			
			Game.rover.x += Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			Game.rover.z -= Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			
			Game.rover.wheelAngle -= 3;
		}
		
		if (backward) {
			// move rover to opposite direction of head
			vector.x -= Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			vector.z += Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			
			Game.rover.x -= Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			Game.rover.z += Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
			
			Game.rover.wheelAngle += 3;
		}
		
		if (left) {
			// rotate rover counter-clockwise
			Game.rover.roverAngle += 1;
			rotation.y -= 1;
		}
		
		if (right) {
			// rotate rover clockwise
			Game.rover.roverAngle -= 1;
			rotation.y += 1;
		}
		
		if (shoulderUp) {
			if (Game.rover.shoulderVerticalAngle <= 90)
				Game.rover.shoulderVerticalAngle += 1;
		}
		
		if (shoulderDown) {
			if (Game.rover.shoulderVerticalAngle >= -15)
				Game.rover.shoulderVerticalAngle -= 1;
		}
		
		if (shoulderLeft) {
			if (Game.rover.shoulderHorizontalAngle <= 90)
				Game.rover.shoulderHorizontalAngle += 1;
		}
		
		if (shoulderRight) {
			if (Game.rover.shoulderHorizontalAngle >= -90)
				Game.rover.shoulderHorizontalAngle -= 1;
		}
		
		if (elbowUp) {
			Game.rover.elbowAngle += 1;
		}
		
		if (elbowDown) {
			Game.rover.elbowAngle -= 1;
		}
		
		if (wristUp) {
			Game.rover.wristAngle += 1;
		}
		
		if (wristDown) {
			Game.rover.wristAngle -= 1;
		}
		
		if (cameraLeft) {
			Game.rover.cameraRotationAngle += 1;
		}
		
		if (cameraRight) {
			Game.rover.cameraRotationAngle -= 1;
		}
	}
}
