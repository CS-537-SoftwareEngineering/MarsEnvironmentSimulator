package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
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
		// Initalize camera position
		vector.x = (float)Game.rover.x;
		vector.y = (float)Game.rover.y;
		vector.z = (float)Game.rover.z;
	}

	public void update() throws Exception {
		updatePreviousVector();
		updateMotion();
		input();
		animation();
	}

	// ==================== START OF AUTOMATION CODE ======================
	String oldCmd = "";
	String newCmd = "";
	String line = "";
	Queue<String> cmdList = new LinkedList<String>();
	int magnitudeValue = 0;
	int magnitudeCounter = 0;
	String[] arr;
	CallBack cb = new CallBack();
	
	public void animation() throws Exception {
		// Keep reading the cmd.txt file in the loop
		try {
			newCmd = commandReader();
		} catch (Exception e) {
			System.out.println("Error reading cmd.txt!");
		}
		
		// Only execute when cmd.txt changes
		if (!newCmd.equals(oldCmd)) {
			oldCmd = newCmd;
			
			BufferedReader cmdReader = new BufferedReader(new StringReader(oldCmd));
			
			while ((line = cmdReader.readLine()) != null) {
				// Loads command list
				cmdList.add(line);
			}
		}
		
		// Only execute when the command list is loaded and there are no remaining iterations of command to perform
		if (cmdList.size() > 0 && magnitudeCounter == 0) {
			System.out.println(cmdList.peek()); // output the currently executing command
			arr = cmdList.peek().split(" ");
			magnitudeValue = Integer.parseInt(arr[1]);
			magnitudeCounter = Math.abs(magnitudeValue);
		}
		
		if (cmdList.size() > 0 && magnitudeCounter > 0) {
			switch(arr[0]) {
			case "ROV_ROTATE":
				if (magnitudeValue > 0) { // rotate left
					Game.rover.roverAngle++;
					rotation.y--;
				} else if (magnitudeValue < 0) { // rotate right
					Game.rover.roverAngle--;
					rotation.y++;
				}
				break;
			case "ROV_MOVE":
				if (magnitudeValue > 0) { // move forward
					vector.x += Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					vector.z -= Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					
					Game.rover.x += Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					Game.rover.z -= Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					
					Game.rover.wheelAngle -= 3;
				} else if (magnitudeValue < 0) { // move backward
					vector.x -= Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					vector.z += Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					
					Game.rover.x -= Math.cos(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					Game.rover.z += Math.sin(Game.rover.roverAngle * Math.PI / 180) * speed * 0.1;
					
					Game.rover.wheelAngle += 3;
				}
				break;
			case "ROV_SHOULDER_HORIZONTAL":
				if (magnitudeValue > 0) { // shoulder left
					if (Game.rover.shoulderHorizontalAngle <= 90)
						Game.rover.shoulderHorizontalAngle++;
				} else if (magnitudeValue < 0) { // shoulder right
					if (Game.rover.shoulderHorizontalAngle >= -90)
						Game.rover.shoulderHorizontalAngle--;
				}
				break;
			case "ROV_SHOULDER_VERTICAL":
				if (magnitudeValue > 0) { // shoulder up
					if (Game.rover.shoulderVerticalAngle <= 90)
						Game.rover.shoulderVerticalAngle++;
				} else if (magnitudeValue < 0) { // shoulder down
					if (Game.rover.shoulderVerticalAngle >= -15)
						Game.rover.shoulderVerticalAngle--;
				}
				break;
			case "ROV_ELBOW":
				if (magnitudeValue > 0) { // elbow up
					Game.rover.elbowAngle++;
				} else if (magnitudeValue < 0) { // elbow down
					Game.rover.elbowAngle--;
				}
				break;
			case "ROV_WRIST":
				if (magnitudeValue > 0) {
					Game.rover.wristAngle++;
				} else if (magnitudeValue < 0) {
					Game.rover.wristAngle--;
				}
				break;
			case "ROV_CAMERA":
				if (magnitudeValue > 0) {
					Game.rover.cameraRotationAngle++;
				} else if (magnitudeValue < 0) {
					Game.rover.cameraRotationAngle--;
				}
				break;
			case "ROV_STANDBY":
				// Do nothing!
				break;
			case "ROV_LASER":
				Game.rover.laserOn = true;
				break;
			default:
				break;
			}
			
			magnitudeCounter--;
			
			if (magnitudeCounter == 0) {
				if (Game.rover.laserOn)
					Game.rover.laserOn = false;
				cmdList.remove();
			}
			
			if (cmdList.size() == 0) {
				System.out.println("Finished executing commands.");
				cb.done();
			}
		}
	}
	
	// Reads the command file and return 1 string variable that holds all commands in file
	public String commandReader() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("C:\\svn\\classes\\2015spring\\cs537\\cmd.txt"));
	    
		try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	// ==================== END OF AUTOMATION CODE ======================
	
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
			System.out.println("}");//*/
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
			forward = true;
		} else {
			forward = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			backward = true;
		} else {
			backward = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			left = true;
		} else {
			left = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			right = true;
		} else {
			right = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
			shoulderUp = true;
		} else {
			shoulderUp = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			shoulderDown = true;
		} else {
			shoulderDown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			shoulderLeft = true;
		} else {
			shoulderLeft = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			shoulderRight = true;
		} else {
			shoulderRight = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			elbowUp = true;
		} else {
			elbowUp = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
			elbowDown = true;
		} else {
			elbowDown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			wristUp = true;
		} else {
			wristUp = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
			wristDown = true;
		} else {
			wristDown = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			cameraLeft = true;
		} else {
			cameraLeft = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			cameraRight = true;
		} else {
			cameraRight = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			Game.rover.laserOn = true;
		} else {
			Game.rover.laserOn = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
			System.out.println("(" + Game.rover.x + ", " + Game.rover.z);
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
		
		// Rover controls
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
			Game.rover.roverAngle += 1.0;
			rotation.y -= 1;
		}
		
		if (right) {
			// rotate rover clockwise
			Game.rover.roverAngle -= 1.0;
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
		
		if (elbowUp)
			Game.rover.elbowAngle++;
		
		if (elbowDown)
			Game.rover.elbowAngle--;
		
		if (wristUp)
			Game.rover.wristAngle++;
		
		if (wristDown)
			Game.rover.wristAngle--;
		
		if (cameraLeft)
			Game.rover.cameraRotationAngle++;
		
		if (cameraRight)
			Game.rover.cameraRotationAngle--;
	}
}
