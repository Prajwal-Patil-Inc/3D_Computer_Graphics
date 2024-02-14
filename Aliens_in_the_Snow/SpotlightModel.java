/* I declare that this code is my own work */
/* Author: Prajwal Patil, Email: pmpatil1@sheffield.ac.uk */

import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;

public class SpotlightModel{

    private Camera camera;
    private Light[] light;
    private Light spotlightLight;
    private Model sphere;
    private SGNode spotlightRoot;
    private float xPosition = 0;
    private TransformNode spotlightTranslate, spotlightUpperRotate;
    private float spotlightRotateStartAngle=-25, spotlightRotateAngle;
    public Shader shader;
    public boolean lightON = true;
    public TransformNode spotlightUpperTransform;

     //Spotlight Constructor, taking in array of lights, spotlight and a single texture for the entire spotlight body
    public SpotlightModel(GL3 gl, Camera camera, Light[] light, Light spotlightLight,Texture t1){
    
        this.camera = camera;
        this.light = light;
        this.spotlightLight = spotlightLight;

        //Making the sphere object
        sphere = makeSphere(gl, t1);
        
        //Declaring the spotlight height
        float spotlightHeightScale = 12f;

        spotlightRoot = new NameNode("Spotlight-Root");

        //This translate defines where the spotlight will be situated in the world matrix
        spotlightTranslate = new TransformNode("Spotlight Translate",Mat4Transform.translate(-8,-0.5f,2));

        //Spotlight upper rotation
        spotlightUpperRotate = new TransformNode("Spotlight Upper Rotate", Mat4Transform.rotateAroundZ(spotlightRotateStartAngle));

        //Spotlight - ROOT
        NameNode spotlightLower = new NameNode("Spotlight Lower");
        Mat4 matx = Mat4Transform.scale(0.4f, spotlightHeightScale, 0.4f);
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode spotlightLowerTransform = new TransformNode("Spotlight lower", matx);
        ModelNode spotlightLowerShape = new ModelNode("Sphere(Lower)", sphere);

        // Spotlight Upper part
        NameNode spotlightUpper = new NameNode("Spotlight Upper");
        TransformNode spotlightUpperTranslate = new TransformNode("Spotlight upper translate", Mat4Transform.translate(0, spotlightHeightScale, 0));
        matx = new Mat4(1); 
        matx = Mat4.multiply(matx, Mat4Transform.rotateAroundZ(-25));
        matx = Mat4.multiply(matx, Mat4Transform.scale(2.5f, 0.9f, 0.9f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        spotlightUpperTransform = new TransformNode("Spotlight Upper transform", matx);
        ModelNode spotlightUpperShape = new ModelNode("Sphere(Upper)",sphere);

        //Scene Graph
        spotlightRoot.addChild(spotlightTranslate);
         spotlightTranslate.addChild(spotlightLower);
         spotlightLower.addChild(spotlightLowerTransform);
         spotlightLowerTransform.addChild(spotlightLowerShape);
        spotlightLower.addChild(spotlightUpper);
         spotlightUpper.addChild(spotlightUpperTranslate);
         spotlightUpperTranslate.addChild(spotlightUpperRotate);
         spotlightUpperRotate.addChild(spotlightUpperTransform);
         spotlightUpperTransform.addChild(spotlightUpperShape);

        spotlightRoot.update();

    }

    //Method to create a sphere model
     private Model makeSphere(GL3 gl, Texture t1){
        String name = "Sphere=";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0, 0.5f, 0));
        Model sphere = new Model(name, mesh, modelMatrix, shader, material, light, spotlightLight, camera, t1);
        return sphere;
    }

    //Spotlight Animation method, this will control how the spotlight upper part rotates
    private void spotlightAnimation(){
        double elapsedTime = getSeconds()-startTime;
        spotlightRotateAngle = 25*(float)Math.sin(elapsedTime);
        spotlightUpperRotate.setTransform(Mat4Transform.rotateAroundY(spotlightRotateAngle));
        spotlightRoot.update();
    }

    //Method to set the lightON value to turn the world lighting 'on' and 'off' 
    public void setLightONOFF(boolean value){
      lightON = value;
    }

    public void render(GL3 gl){
        spotlightRoot.draw(gl);
        spotlightAnimation();

        //Using the shader to send the data to the fragment shader to control world lightig
        shader.use(gl);
        if(lightON==true){
          shader.setInt(gl, "lightOn", 1);
        }
        else if(lightON==false){
          shader.setInt(gl, "lightOn", 0);
        }
    }

    public void dispose(GL3 gl){
        sphere.dispose(gl);
    }

    //Method to get the light position, returns a Vec3 with x,y,z co-ordinates
    public Vec3 getLightPosition(){
        double elapsedTime = getSeconds()-startTime;
        float x = -7.6f;
        float y = 12.3f;
        float z = 4.0f*(float)(Math.cos(Math.toRadians(elapsedTime*75)));
        return new Vec3(x,y,z);
    }

// ***************************************************
/* Calculating Time */ 
  
    private double startTime;
    
    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }


}