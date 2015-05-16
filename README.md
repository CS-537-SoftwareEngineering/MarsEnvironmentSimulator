# MarsEnvironmentSimulator
Mars Environment Simulator

maps/Program.java
- main 
- monitor size
- comment out GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE); to use texture

maps/Game.java
- heightmapExaggeration is the height exaggeration
- Heightmap takes in an image for to generate a terrain
- floor takes in an image for a texture

maps/Camera.java
- runSpeed has been increased to move faster when "walking"
- i added a q to terminate the program


Controls:
w - move up
S - move down
A - strafe left
D - Strafe right
Mouse - change camera view direction
Q - quit
