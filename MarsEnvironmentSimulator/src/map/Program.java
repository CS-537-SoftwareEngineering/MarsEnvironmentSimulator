package map;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;


public class Program {

	public static void main(String[] args) throws LWJGLException{
		//Setup and create the display (Default is 1280x720) (720p)
		Display.setDisplayMode(new DisplayMode(1280,600));
		Display.create();
		
		//Setup OpenGL for 3D projection
		initGL3();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);

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
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	private static void clearGL(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//Uncomment this if you want to see the map as a wireframe!
		//GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glLoadIdentity();
	}
}
