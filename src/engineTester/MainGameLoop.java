package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.ContextAttribs;

import rederEngine.DisplayManager;
import rederEngine.Loader;
import rederEngine.MasterRenderer;
import rederEngine.OBJLoader;
import rederEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

public class MainGameLoop {
	public static void main(String args[])
	{
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkflowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		/*float[] vertices = {
							-0.5f, 0.5f, 0f,
							-0.5f, -0.5f, 0f, 
							0.5f, -0.5f, 0f, 
							0.5f, 0.5f, 0f, 
							};
		int[] indices = {
						 0,1,3,
						 3,1,2
						};
		float[] textureCoords ={
				0,0,
				0,1,
				1,1,
				1,0
		};*/
		/*float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		RawModel model = loader.loadToVAO(vertices,textureCoords,indices);*/
		RawModel model = OBJLoader.loadObjModel("pine", loader);
		//ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel texturedModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("pine")));
		ModelTexture texture = texturedModel.getTexture();
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				fernTextureAtlas);
		
		fern.getTexture().setHasTransparency(true);
		texture.setShineDamper(1);
		texture.setReflectivity(0.5f);
		
		List<Entity> entities = new ArrayList<Entity>();
		Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap,"heightMap");
		Terrain terrain2 = new Terrain(-1,-1,loader,texturePack,blendMap,"heightMap");
		Random random = new Random();
		
		//StaticShader shader = new StaticShader();
		//Renderer renderer = new Renderer(shader);
		
		Entity entity = new Entity(texturedModel,new Vector3f(0,0,-50),0,0,0,2);
		
		Light sun = new Light(new Vector3f(0,100,-70),new Vector3f(0.4f,0.4f,0.4f));
		List<Light> lights = new ArrayList<Light>();
		lights.add(sun);
		lights.add(new Light(new Vector3f(185,6.5f,-293), new Vector3f(2,0,0), new Vector3f(1,0.01f,0.001f)));
		lights.add(new Light(new Vector3f(370,11,-300), new Vector3f(0,2,2), new Vector3f(1,0.01f,0.001f)));
		Light light = new Light(new Vector3f(293,7,-305), new Vector3f(2,2,0), new Vector3f(1,0.01f,0.001f));
		lights.add(light);
		
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
		entities.add(new Entity(lamp, new Vector3f(185,-4.7f,-293),0,0,0,1));
		entities.add(new Entity(lamp, new Vector3f(370,-1.2f,-300),0,0,0,1));
		//entities.add(new Entity(lamp, new Vector3f(293,-3.8f,-305),0,0,0,1));
		entities.add(new Entity(lamp, new Vector3f(0,0,0),0,0,0,1));
		Entity lampEntity = new Entity(lamp,new Vector3f(293,-6.8f,-305),0,0,0,1);
		entities.add(lampEntity);
	
		for(int i=0;i<500;i++)
		{
			float x1 = random.nextFloat()*800-400, x2 = random.nextFloat()*800-400, x3 = random.nextFloat()*800-400;
			float z1 = random.nextFloat()*800-400, z2 = random.nextFloat()*800-400, z3 = random.nextFloat()*800-400;
			float y1 = terrain.getHeightOfTerrain(x1,z1), y2 = terrain.getHeightOfTerrain(x2,z2), y3 = terrain.getHeightOfTerrain(x3,z3);
			entities.add(new Entity(texturedModel,new Vector3f(x1,y1,z1),
					0,0,0,1));
			entities.add(new Entity(grass,new Vector3f(x2,y2,z2),
					0,0,0,1));
			entities.add(new Entity(fern,random.nextInt(4),new Vector3f(x3,y3,z3),
					0,0,0,0.6f));
		}
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		RawModel bunnyModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
				loader.loadTexture("playerTexture")));
		Player player = new Player(stanfordBunny, new Vector3f(185,10,-295),0,180,0,0.6f);
		Camera camera = new Camera(player);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f));
		guis.add(gui);
		
		MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(),terrain);
		
		//GuiRenderer guiRenderer = new GuiRenderer(loader);
		while(!Display.isCloseRequested())
		{
			//entity.increasePosition(0,0,0f);
			entity.increaseRotation(0, 1, 0);
			camera.move();
			//camera.setYaw();
			
			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if(terrainPoint != null)
			{
				lampEntity.setPosition(terrainPoint);
				light.setPosition(new Vector3f(terrainPoint.x,terrainPoint.y+15,terrainPoint.z));
			}
			player.move(terrain);
			renderer.processEntity(player);
			//renderer.processTerrain(terrain2);
			renderer.processTerrain(terrain);
			for(int i=0;i<500;i++)
				renderer.processEntity(entities.get(i));
			//renderer.prepare();
			//shader.start();
			//shader.loadLight(light);
			//shader.loadViewMatrix(camera);
			//renderer.render(entity,shader);
			//shader.stop();
			renderer.render(lights, camera);
			//guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		//shader.cleanUp();
		//guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
