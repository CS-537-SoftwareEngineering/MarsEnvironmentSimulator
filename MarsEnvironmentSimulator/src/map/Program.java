package map;

import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.glColorMaterial;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


public class Program {
	//----------- Variables added for Lighting Test -----------//
	static FloatBuffer matSpecular;
	static FloatBuffer lightPosition;
	static FloatBuffer whiteLight; 
	static FloatBuffer lModelAmbient;
	//----------- END: Variables added for Lighting Test -----------//
	
	public static void main(String[] args) throws LWJGLException{
		//Setup and create the display (Default is 1280x720) (720p)
		Display.setDisplayMode(new DisplayMode(1280,600));
		Display.create();
		
		//Setup OpenGL for 3D projection
		initGL3();
		
		FloatBuffer position = floatBuffer(new float[]{20.0f, 50.0f, 50.0f, 0.0f});
		FloatBuffer lightIntensity = floatBuffer(new float[] {0.7f, 0.7f, 0.7f, 1.0f});
		FloatBuffer ambient = floatBuffer(new float[] {0.7f, 0.7f, 0.7f, 1.0f});
		FloatBuffer diffuse = floatBuffer(new float[] {0.6f, 0.6f, 0.6f, 1.0f});
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
    	GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightIntensity);
    	GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, ambient);
    	GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, diffuse);
    	GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE); 
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

		//Grab the mouse (should probably add a way to toggle this)
		Mouse.setGrabbed(true);
		game = new Game();
		
		//Main game loop.
		while(!Display.isCloseRequested()){
			//Clear the screen, reset translations
			clearGL();
			
			update();
			render();
			
			Display.update();
			Display.sync(60);
		}
		
		//Hastily destroy the program.
		Display.destroy();
		System.exit(0);
	}
	
	static Game game;
	static Vector3f cameraPosition = new Vector3f(0,1,0);
	static Vector3f cameraRotation = new Vector3f();
	static boolean top_view = false;
	 

	private static void update() {
		game.update();
	}
	
	private static void render() {
		initGL3();
		game.render();
	}

	private static void initGL3(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GLU.gluPerspective((float) 90, Display.getWidth() / Display.getHeight(), 0.001f, 1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
//		GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
		GL11.glClearColor(200/255f, 180/255f, 85/255f, 0.5f); // BACKGROUND COLOR
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		//----------- Variables & method calls added for Lighting Test -----------//
		initLightArrays();
		glShadeModel(GL_SMOOTH);
		glMaterial(GL_FRONT, GL_SPECULAR, matSpecular);				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
		
		glLight(GL_LIGHT0, GL_POSITION, lightPosition);				// sets light position
		glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);				// sets specular light to white
		glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);					// sets diffuse light to white
		glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);		// global ambient light 
		
		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0
		
		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
		//----------- END: Variables & method calls added for Lighting Test -----------//
	}
	
	private static void clearGL(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//Uncomment this if you want to see the map as a wireframe!
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glLoadIdentity();
	}
	
	private static FloatBuffer floatBuffer(float[] array) {
		ByteBuffer buff = ByteBuffer.allocateDirect(16);
		buff.order(ByteOrder.nativeOrder());
		
		return (FloatBuffer)buff.asFloatBuffer().put(array).flip();
	}
	
	//------- Added for Lighting Test----------//
	private static void initLightArrays() {
		matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
		
		whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
	}
}
