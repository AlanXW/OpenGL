import gmaths.*;

import java.nio.*;
import java.util.Random;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Anilamp_GLEventListener implements GLEventListener{

	private static final boolean DISPLAY_SHADERS = false;
	   
	public Anilamp_GLEventListener(Camera camera) {
	  this.camera = camera;
	  this.camera.setPosition(new Vec3(4f,12f,20f));
	}

	public void display(GLAutoDrawable drawable) {
	  GL3 gl = drawable.getGL().getGL3();
	  render(gl);
	}

	public void dispose(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
	    light.dispose(gl);
	    floor.dispose(gl);
	    sphere.dispose(gl);
	    sphere2.dispose(gl);
	    cube.dispose(gl);
	    cube2.dispose(gl);	    
	    cube3.dispose(gl);    
	}	
 
	  private boolean animation = false;
	  private double savedTime = 0;
	   
	  public void startAnimation() {
	    animation = true;
	    startTime = getSeconds()-savedTime;
	  }
	   
	  public void stopAnimation() {
	    animation = false;
	    double elapsedTime = getSeconds()-startTime;
	    savedTime = elapsedTime;
	  }
	   
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
	    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
	    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
	    gl.glClearDepth(1.0f);
	    gl.glEnable(GL.GL_DEPTH_TEST);
	    gl.glDepthFunc(GL.GL_LESS);
	    gl.glFrontFace(GL.GL_CCW);    
	    gl.glEnable(GL.GL_CULL_FACE); 
	    gl.glCullFace(GL.GL_BACK);   
	    initialise(gl);
	    startTime = getSeconds();
	}
	

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL3 gl = drawable.getGL().getGL3();
		gl.glViewport(x, y, width, height);
		float aspect = (float)width/(float)height;
		camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
	}
	
	private void render(GL3 gl) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	    light.setPosition(getLightPosition());  // changing light position each frame
	    light.render(gl);
	    floor.setModelMatrix(getMforTT1());
	    floor.render(gl);
	    wall.setModelMatrix(getMforTT2());
	    wall.render(gl);
	    tableRoot.draw(gl);
	    if (animation) updateLamp();
	    lampRoot.draw(gl);
	}	

	
	private void initialise(GL3 gl) {

		int[] textureId0 = TextureLibrary.loadTexture(gl, "src/textures/floor.jpg");
	    int[] textureId1 = TextureLibrary.loadTexture(gl, "src/textures/floor_specular.jpg");
	    int[] textureId2 = TextureLibrary.loadTexture(gl, "src/textures/wall.jpg");
	    int[] textureId3 = TextureLibrary.loadTexture(gl, "src/textures/wall_specular.jpg");
	    int[] textureId4 = TextureLibrary.loadTexture(gl, "src/textures/container2.jpg");
	    int[] textureId5 = TextureLibrary.loadTexture(gl, "src/textures/container2_specular.jpg");
	    int[] textureId6 = TextureLibrary.loadTexture(gl, "src/textures/cube.jpg");
	    int[] textureId7 = TextureLibrary.loadTexture(gl, "src/textures/cube_specular.jpg");
	    int[] textureId8 = TextureLibrary.loadTexture(gl, "src/textures/paperweight.jpg");
	    int[] textureId9 = TextureLibrary.loadTexture(gl, "src/textures/paperweight_specular.jpg");
	    int[] textureId10 = TextureLibrary.loadTexture(gl, "src/textures/metal.jpg");
	    int[] textureId11 = TextureLibrary.loadTexture(gl, "src/textures/metal_specular.jpg");
	            
	    light = new Light(gl);
	    light.setCamera(camera);
		
	    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
	    Shader shader = new Shader(gl, "src/vs_tt_05.txt", "src/fs_tt_05.txt");
	    Material material = new Material(new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.3f, 0.3f, 0.3f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
	    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
	    floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId0, textureId1);
	    
	    wall = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId2, textureId3);
	    
	    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
	    shader = new Shader(gl, "src/vs_cube_04.txt", "src/fs_cube_04.txt");
	    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
	    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
	    sphere = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10, textureId11);
	    
	    sphere2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId8, textureId9);
	    
	    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
	    shader = new Shader(gl, "src/vs_cube_04.txt", "src/fs_cube_04.txt");
	    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
	    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
	    cube = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId10, textureId11);
	    
	    cube2 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId4, textureId5);
	    
	    cube3 = new Model(gl, camera, light, shader, material, modelMatrix, mesh, textureId6, textureId7);

		float tableLegsLength = 4.0f;
		float tableLegsScale = 0.3f;
		float tableHeight = 0.3f;
		float tableWidth = 9.0f;
		float tableDepth = 7.0f;
		float cubescale = 0.3f;
		float labtopWdith = 0.7f;
		float labtopHeight = 0.03f;
		float labtopDepth = 0.5f;
		float paperweightScale = 0.3f;
		
		tableRoot = new NameNode("tableroot");
		tableMoveTranslate = new TransformNode("table transform",Mat4Transform.translate(0f,4.0f,-7.0f));
		NameNode table = new NameNode("table");
			Mat4 m = Mat4Transform.scale(tableWidth,tableHeight,tableDepth);
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		    TransformNode tableTransform = new TransformNode("table transform", m);
		    	ModelNode tableShape = new ModelNode("Cube(table)", cube2);
		    	
		NameNode leg1 = new NameNode("leg1");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate((tableWidth*0.4f)-(tableLegsScale*0.4f),0,(tableWidth*0.3f)-(tableLegsScale*0.3f)));
			m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
			m = Mat4.multiply(m, Mat4Transform.scale(tableLegsScale,tableLegsLength,tableLegsScale));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
			TransformNode leg1Transform = new TransformNode("leg1 transform", m);
	        	ModelNode Leg1Shape = new ModelNode("Cube(leg1)", cube2);
	        	
		NameNode leg2 = new NameNode("leg2");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate(-(tableWidth*0.4f)+(tableLegsScale*0.4f),0,(tableWidth*0.3f)-(tableLegsScale*0.3f)));
			m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
			m = Mat4.multiply(m, Mat4Transform.scale(tableLegsScale,tableLegsLength,tableLegsScale));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
				TransformNode leg2Transform = new TransformNode("leg2 transform", m);
				ModelNode Leg2Shape = new ModelNode("Cube(leg2)", cube2);
				
		NameNode leg3 = new NameNode("leg3");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate(-(tableWidth*0.4f)+(tableLegsScale*0.4f),0,-(tableWidth*0.3f)+(tableLegsScale*0.3f)));
			m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
			m = Mat4.multiply(m, Mat4Transform.scale(tableLegsScale,tableLegsLength,tableLegsScale));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
				TransformNode leg3Transform = new TransformNode("leg3 transform", m);
				ModelNode Leg3Shape = new ModelNode("Cube(leg3)", cube2);
				
		NameNode leg4 = new NameNode("leg4");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate((tableWidth*0.4f)-(tableLegsScale*0.4f),0,-(tableWidth*0.3f)+(tableLegsScale*0.3f)));
			m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
			m = Mat4.multiply(m, Mat4Transform.scale(tableLegsScale,tableLegsLength,tableLegsScale));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
				TransformNode leg4Transform = new TransformNode("leg4 transform", m);
				ModelNode Leg4Shape = new ModelNode("Cube(leg4)", cube2);
		
		NameNode mCube = new NameNode("cube");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate(-(tableWidth*0.3f)+(cubescale*0.23f),0.3f,-(tableWidth*0.15f)+(cubescale*0.17f)));
			m = Mat4.multiply(m, Mat4Transform.scale(cubescale,cubescale,cubescale));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
				TransformNode cubeTransform = new TransformNode("cube transform", m);
				ModelNode CubeShape = new ModelNode("Cube(cube)", cube3);
		NameNode labtop = new NameNode("labtop");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate(-(tableWidth*0.15f)+(cubescale*0.23f),0.3f,-(tableWidth*0.15f)+(labtopDepth*0.17f)));
			m = Mat4.multiply(m, Mat4Transform.scale(labtopWdith,labtopHeight,labtopDepth));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
				TransformNode labtopTransform = new TransformNode("labtop transform", m);
				ModelNode LabtopShape = new ModelNode("Cube(labtop)", cube);
		NameNode paperweight = new NameNode("paperweight");
			m = new Mat4(1);
			m = Mat4.multiply(m, Mat4Transform.translate(-(tableWidth*0.3f)+(paperweightScale*0.23f),0.3f,(tableWidth*0.15f)-(paperweightScale*0.17f)));
			m = Mat4.multiply(m, Mat4Transform.scale(paperweightScale,paperweightScale,paperweightScale));
			m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
				TransformNode paperweightTransform = new TransformNode("paperweight transform", m);
				ModelNode PaperweightShape = new ModelNode("sphere(paperweight)", sphere2);
		tableRoot.addChild(tableMoveTranslate);
		tableMoveTranslate.addChild(table);
			table.addChild(tableTransform);
			tableTransform.addChild(tableShape);
			table.addChild(leg1);
			leg1.addChild(leg1Transform);
			leg1Transform.addChild(Leg1Shape);
			table.addChild(leg2);
			leg2.addChild(leg2Transform);
			leg2Transform.addChild(Leg2Shape);
			table.addChild(leg3);
			leg3.addChild(leg3Transform);
			leg3Transform.addChild(Leg3Shape);
			table.addChild(leg4);
			leg4.addChild(leg4Transform);
			leg4Transform.addChild(Leg4Shape);
			table.addChild(mCube);
			mCube.addChild(cubeTransform);
			cubeTransform.addChild(CubeShape);
			table.addChild(labtop);
			labtop.addChild(labtopTransform);
			labtopTransform.addChild(LabtopShape);
			table.addChild(paperweight);
			paperweight.addChild(paperweightTransform);
			paperweightTransform.addChild(PaperweightShape);
		tableRoot.update();

		float botHeight = 0.1f;
		float botScale = 0.5f;
		float branchScale = 0.05f;
		float branchLength = 0.65f;
		float headWidth = 0.4f;
		float headScale = 0.2f;
		
		
		lampRoot = new NameNode("lamproot");
		lampMoveTranslate = new TransformNode("robot transform",Mat4Transform.translate(xPosition,yPosition,zPosition));
		rotateAll = new TransformNode("rotateAroundZ("+rotateAllAngle+")", Mat4Transform.rotateAroundZ(rotateAllAngle));
		
		NameNode bot = new NameNode("bot");
	    m = Mat4Transform.scale(botScale,botHeight,botScale);
	    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
	    TransformNode botTransform = new TransformNode("bot transform", m);
	    ModelNode BotShape = new ModelNode("Cube(bot)", cube);
	    
	    NameNode lowerBranch = new NameNode("lowerBranch");
	    m = Mat4Transform.scale(branchScale,branchLength,branchScale);
	    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
	    TransformNode lowerBranchScale = new TransformNode("lowerBranch scale", m);
	    ModelNode lowerBranchShape = new ModelNode("Sphere(lower branch)", sphere);
	    
	    TransformNode translateToUpper = new TransformNode("translateToUpper",Mat4Transform.translate(0,branchLength,0));
	    rotateTwo = new TransformNode("rotateTwo",Mat4Transform.rotateAroundZ(rotateTwoAngle));
	    
	    NameNode upperBranch = new NameNode("upperbranch");
	    m = Mat4Transform.scale(branchScale,branchLength,branchScale);
	    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
	    TransformNode upperBranchScale = new TransformNode("upperBranch scale", m);
		ModelNode upperBranchShape = new ModelNode("Sphere(upper branch)", sphere);
		
		TransformNode translateToTop = new TransformNode("translateToTop",Mat4Transform.translate(0,branchLength,0));
		rotateHead = new TransformNode("rotateHead",Mat4Transform.rotateAroundZ(rotateHeadAngle));
		
	    NameNode head = new NameNode("head");
	    m = Mat4Transform.scale(headWidth,headScale,headScale);
	    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode headBranchScale = new TransformNode("headBranch scale", m);
		ModelNode headBranchShape = new ModelNode("Cube(upper branch)", cube);
			
		lampRoot.addChild(lampMoveTranslate);
		  lampMoveTranslate.addChild(bot);
		  	bot.addChild(botTransform);
		  	  botTransform.addChild(BotShape);
		  	bot.addChild(rotateAll);
		  	  rotateAll.addChild(lowerBranch);
		  	    lowerBranch.addChild(lowerBranchScale);
		  	      lowerBranchScale.addChild(lowerBranchShape);
		  	    lowerBranch.addChild(translateToUpper);
		  	      translateToUpper.addChild(rotateTwo);
		  	        rotateTwo.addChild(upperBranch);
		  	          upperBranch.addChild(upperBranchScale);
		  	            upperBranchScale.addChild(upperBranchShape);
		  	          upperBranch.addChild(translateToTop);
		  	            translateToTop.addChild(rotateHead);
		  	              rotateHead.addChild(head);
		  	                head.addChild(headBranchScale);
		  	                  headBranchScale.addChild(headBranchShape);					
		lampRoot.update();
	}
	


	public void randomJump() {
		Random rand =new Random();
		float i;
		count = 0;
		i=rand.nextFloat()*6.0f-3.0f;
		xTarget = i;
		i=rand.nextFloat()*4.0f+2.5f;
	    zTarget = -i;
	    rotateAllAngle = rotateAllAngleStart;
		rotateTwoAngle = rotateTwoAngleStart;
		rotateHeadAngle = rotateHeadAngleStart;
	    startAnimation();
	}
	public void turnOff() {
		Material material = new Material();
		material.setAmbient(0.3f, 0.3f, 0.3f);
		material.setDiffuse(0.3f, 0.3f, 0.3f);
		material.setEmission(0, 0, 0);
		light.setMaterial(material);
	}
	
	public void turnOn() {
		Material material = new Material();
		material.setAmbient(0.5f, 0.5f, 0.5f);
		material.setDiffuse(0.9f, 0.9f, 1.0f);
		material.setEmission(1.0f, 1.0f, 1.0f);
		light.setMaterial(material);
	}

	private void updateLamp() {
		float distance_x = xPosition - xTarget;
		float distance_z = zPosition - zTarget;
		if(count<25) {
			rotateAll.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle++));
			rotateAll.update();
			rotateTwo.setTransform(Mat4Transform.rotateAroundZ(rotateTwoAngle--));
			rotateTwo.update();
			rotateHead.setTransform(Mat4Transform.rotateAroundZ(rotateHeadAngle--));
			rotateHead.update();			
		}
		else if (count<50) {
			rotateAll.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle--));
			rotateAll.update();
			rotateTwo.setTransform(Mat4Transform.rotateAroundZ(rotateTwoAngle++));
			rotateTwo.update();
			rotateHead.setTransform(Mat4Transform.rotateAroundZ(rotateHeadAngle++));
			rotateHead.update();
		}
		else if (count<100) {
			yPosition += 1.0f/50.0f;
			xPosition -= distance_x/100.0f;
			zPosition -= distance_z/100.0f;
			lampMoveTranslate.setTransform(Mat4Transform.translate(xPosition,yPosition,zPosition));
			lampMoveTranslate.update();
		}
		else {
			yPosition -= 1.0f/50.0f;
			xPosition -= distance_x/100.0f;
			zPosition -= distance_z/100.0f;
			lampMoveTranslate.setTransform(Mat4Transform.translate(xPosition,yPosition,zPosition));
			lampMoveTranslate.update();
		}					
		count ++;
		if (count >= 150) stopAnimation();
	}

	public void randomPose() {
		Random rand =new Random();
		int i;
		i=rand.nextInt(61);
		rotateAllAngle = i;		
		i=-rand.nextInt(121);
		rotateTwoAngle = i;
		i=-rand.nextInt(91)-45;
		rotateHeadAngle = i;
		rotateAll.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
		rotateAll.update();
		rotateTwo.setTransform(Mat4Transform.rotateAroundZ(rotateTwoAngle));
		rotateTwo.update();
		rotateHead.setTransform(Mat4Transform.rotateAroundZ(rotateHeadAngle));
		rotateHead.update();
	}
	
	 private Mat4 getMforTT1() {
	    float size = 16f;
	    Mat4 modelMatrix = new Mat4(1);
	    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
	    return modelMatrix;
	  }
		
	  private Mat4 getMforTT2() {
	    float size = 16f;
	    Mat4 modelMatrix = new Mat4(1);
	    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
	    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
	    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), modelMatrix);
	    return modelMatrix;
	  }
	
	  private Vec3 getLightPosition() {
		  
		    return new Vec3(0.2f,5.4f,-4.5f);   
		  }
	  
	  
		
	private Camera camera;
	private Model floor, wall, sphere, sphere2, cube, cube2, cube3;
	private Light light;
	private Light light2;
	private SGNode lampRoot,tableRoot;
	
	private float xPosition = 0;
	private float yPosition = 4.3f;
	private float zPosition = -4.5f;
	private float xTarget = 0;
	private float zTarget = 0;
	private float count = 0;
	private TransformNode lampMoveTranslate, tableMoveTranslate, rotateAll, rotateTwo, rotateHead;

	float rotateAllAngleStart = 45, rotateAllAngle = rotateAllAngleStart;
	float rotateTwoAngleStart = -105, rotateTwoAngle = rotateTwoAngleStart;
	float rotateHeadAngleStart = 0, rotateHeadAngle = rotateHeadAngleStart;
	  private double startTime;
	  
	  private double getSeconds() {
	    return System.currentTimeMillis()/1000.0;
	  }


	  
	
}
