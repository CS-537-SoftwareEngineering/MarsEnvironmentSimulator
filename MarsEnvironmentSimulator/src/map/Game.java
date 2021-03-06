package map;
import java.io.IOException;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Game {

	Random rng = new Random();
	Camera camera = new Camera(this);
	static Heightmap heightmap;
	Texture floor;
	static Rover rover = new Rover();
	
	int terrainPtr;
	public static final int heightmapExaggeration = 7;

	public Game(){
		//Load the heightmap from file height.jpg (See Heightmap.java)
		heightmap = new Heightmap("img/map2.jpg");
		System.out.println("Loaded heightmap.");
		
		//Generate the terrain and store in video memory
		generateLists();
		System.out.println("Terrain generated and stored.");
		
		//Load grass texture
		try {
			floor = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("img/sand.jpg"));
			System.out.println("Textures loaded successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		camera.update();
	}

	public void render() {
		//Translate the screen to the camera
		if(Program.top_view){ 
			GLU.gluLookAt(0, 50, 0, 0, 0, 0, 0, 0, -1);
		}
		else{
			GL11.glRotatef(camera.rotation.x, 1, 0, 0);
			GL11.glRotatef(camera.rotation.y, 0, 1, 0);
			GL11.glRotatef(camera.rotation.z, 0, 0, 1);
		}
		//Note: Camera's head is 1.4px above it's y value.
		GL11.glTranslatef(-camera.vector.x, -camera.vector.y-1.4f, -camera.vector.z);
		
		//Bi-linear interpolation to calculate where the player should be vertically on the terrain.
		camera.vector.y = heightmap.calculateHeight(camera.vector.x*4, camera.vector.z*4)*heightmapExaggeration;

		
		rover.renderRover(camera.vector.y+.5f);
		
		//Bind the grass texture, and call the terrain from video memory.
		floor.bind();
		GL11.glCallList(terrainPtr);
		
		
		
	}
	
	public void generateLists(){
		//We store the whole terrain here in memory so that we don't need
		//to calculate it every frame. Using a display list, we calculate it
		//once, and only have to render it every frame.
		//Doing this sped things up a lot, even for my 3.5GHz 6-core CPU, 1GB GTX 650 Ti setup.
		terrainPtr = GL11.glGenLists(1);
		GL11.glNewList(terrainPtr, GL11.GL_COMPILE);
		
		GL11.glBegin(GL11.GL_QUADS);
		for(int x=0;x<heightmap.height.length;x++){
			for(int y=0;y<heightmap.height[x].length;y++){
				//float c = 0.1f+(rng.nextFloat()/5f);
				float color = heightmap.getHeightAt(x, y);
				float scale = (float) .25;
				GL11.glColor3f(color, color, color);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(x*scale, heightmap.getHeightAt(x, y)*heightmapExaggeration, y*scale);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f((x+1)*scale, heightmap.getHeightAt(x+1, y)*heightmapExaggeration, y*scale);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f((x+1)*scale, heightmap.getHeightAt(x+1, y+1)*heightmapExaggeration, (y+1)*scale);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(x*scale, heightmap.getHeightAt(x, y+1)*heightmapExaggeration, (y+1)*scale);
			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}
}
