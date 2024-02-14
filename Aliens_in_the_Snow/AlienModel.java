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

//This class creates the Alien Model
public class AlienModel {

    private Camera camera;
    private Light[] light;
    private Light spotlight;
    public boolean lightON = true;
    public boolean spotlightON = true;

    private Model sphere, sphere2, eyes;

    private SGNode alienRoot;
    private float xPosition = 0, yPosition = 0, zPosition = 0;
    private TransformNode translateX, alienMoveTranslate, leftArmRotate, rightArmRotate, leftEarRotate, rightEarRotate, antennaRotate, antennaSphereRotate, rotateAll, rollHead;
    private float rotateAllAngleStart = 0, rotateAllAngle = rotateAllAngleStart;
    private float rollHeadAngleStart = 0, rollHeadAngle = rollHeadAngleStart;
    private float rotateArmsAngle = 0;
    public Shader shader, shader2, shader3;



    public AlienModel(GL3 gl, Camera camera, Light[] light, Light spotlight, float x, float y, float z,Texture t1, Texture t2, Texture t3, Texture t4, Texture t5){
        this.camera = camera;
        this.light = light;
        this.spotlight = spotlight;

        //World Position parameters
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;

        //Creating models for body, head, arms, ears, antenna and eyes
        sphere = makeSphere(gl, t1);
        sphere2 = makeSphereAntennaBall(gl, t3, t4);
        eyes = makeSphereEyes(gl, t5);

        //alien body parameters;
        float bodyScale = 3f;
        float headScale = 2f;

        alienRoot = new NameNode("Alien-Root");
        alienMoveTranslate = new TransformNode("Alien Translate", Mat4Transform.translate(x,y,z));
        
        //Full body animation
        rotateAll = new TransformNode("Rotate All",Mat4Transform.rotateAroundZ(rotateAllAngle));

        //Head Roll animation
        rollHead = new TransformNode("Roll Head", Mat4Transform.rotateAroundZ(rollHeadAngle));

        //Body = Root
        NameNode body = new NameNode("Body");
        Mat4 matx = Mat4Transform.scale(bodyScale, bodyScale, bodyScale);
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode bodyTransform = new TransformNode("Body Transform", matx);
        ModelNode bodyShape = new ModelNode("Sphere(Body)",sphere);

        //Head
        NameNode head = new NameNode("Head");
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 2.85f, 0));
        matx = Mat4.multiply(matx, Mat4Transform.scale(headScale, headScale, headScale));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode headTransform = new TransformNode("Head Transform", matx);
        ModelNode headShape = new ModelNode("Sphere(head)",sphere);

        //Left arm
        NameNode leftArm = new NameNode("Left arm");
        TransformNode leftArmTranslate = new TransformNode("Left arm translate", Mat4Transform.translate((bodyScale*0.5f),(bodyScale*0.5f),0));
        leftArmRotate = new TransformNode("Left arm rotate", Mat4Transform.rotateAroundZ(-35));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.4f, 1.9f, 0.6f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode leftArmTransform = new TransformNode("Left Arm Transform", matx);
        ModelNode leftArmShape = new ModelNode("Sphere(left arm)",sphere);

        //Right Arm
        NameNode rightArm = new NameNode("Right arm");
        TransformNode rightArmTranslate = new TransformNode("Right arm translate", Mat4Transform.translate((-(bodyScale*0.5f)),(bodyScale*0.5f),0));
        rightArmRotate = new TransformNode("Right arm rotate", Mat4Transform.rotateAroundZ(35));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.4f, 1.9f, 0.6f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode rightArmTransform = new TransformNode("Right Arm Transform", matx);
        ModelNode rightArmShape = new ModelNode("Sphere(Right arm)",sphere);

        //Right Ear
        NameNode rightEar = new NameNode("Right Ear");
        TransformNode rightEarTranslate = new TransformNode("Right Ear translate", Mat4Transform.translate((-(headScale*0.5f)), (bodyScale+headScale*1.2f),0));
        rightEarRotate = new TransformNode("Right ear rotate", Mat4Transform.rotateAroundZ(180));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.2f, 1.5f, 0.6f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode rightEarTransform = new TransformNode("Right ear transform", matx);
        ModelNode rightEarShape = new ModelNode("Sphere(right ear)",sphere);

        //Left Ear
        NameNode leftEar = new NameNode("Left Ear");
        TransformNode leftEarTranslate = new TransformNode("Left Ear", Mat4Transform.translate((headScale*0.5f),(bodyScale+headScale*1.2f),0));
        leftEarRotate = new TransformNode("Left ear rotate", Mat4Transform.rotateAroundZ(180));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.2f, 1.5f, 0.6f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode leftEarTransform = new TransformNode("Left ear transform", matx);
        ModelNode leftEarShape = new ModelNode("Sphere(left ear)",sphere);

        //Antenna 
        NameNode antenna = new NameNode("Antenna");
        TransformNode antennaTranslate = new TransformNode("Antenna translate", Mat4Transform.translate((headScale*0.005f),(bodyScale+headScale*1.2f),0));
        antennaRotate = new TransformNode("Antenna rotate", Mat4Transform.rotateAroundZ(180));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.05f, 0.7f, 0));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode antennaTransform = new TransformNode("Antenna transform", matx);
        ModelNode antennaShape = new ModelNode("Spherers(antenna)", sphere);

        //Antenna ball
        NameNode antennaBall = new NameNode("Antenna Ball");
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.translate((headScale*0.005f), (bodyScale+headScale*1.2f), 0));
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.2f,0.2f,0.2f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode antennaBallTransform = new TransformNode("Antenna Ball transform", matx);
        ModelNode antennaBallShape = new ModelNode("Sphere", sphere2);

        //Left Eye
        NameNode leftEye = new NameNode("Left Eye");
        TransformNode leftEyeTranslate = new TransformNode("Left eye", Mat4Transform.translate(-(headScale*0.2f), (bodyScale+headScale*0.4f), 0.9f));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.4f, 0.4f, 0.4f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode leftEyeTransform = new TransformNode("Left eye transform", matx);
        ModelNode leftEyeShape = new ModelNode("Left eye", eyes);

        //Right Eye
        NameNode rightEye = new NameNode("Right Eye");
        TransformNode rightEyeTranslate = new TransformNode("Right eye", Mat4Transform.translate((headScale*0.2f), (bodyScale+headScale*0.4f), 0.9f));
        matx = new Mat4(1);
        matx = Mat4.multiply(matx, Mat4Transform.scale(0.4f, 0.4f, 0.4f));
        matx = Mat4.multiply(matx, Mat4Transform.translate(0, 0.5f, 0));
        TransformNode rightEyeTransform = new TransformNode("Right eye transform", matx);
        ModelNode rightEyeShape = new ModelNode("Right eye", eyes);

        alienRoot.addChild(alienMoveTranslate);
         alienMoveTranslate.addChild(rotateAll);
          rotateAll.addChild(body);
            body.addChild(bodyTransform);
              bodyTransform.addChild(bodyShape);
              body.addChild(rollHead);
            rollHead.addChild(head);
              rollHead.addChild(headTransform);
              headTransform.addChild(headShape);
            head.addChild(leftEye);
              leftEye.addChild(leftEyeTranslate);
              leftEyeTranslate.addChild(leftEyeTransform);
              leftEyeTransform.addChild(leftEyeShape);
            head.addChild(rightEye);
              rightEye.addChild(rightEyeTranslate);
              rightEyeTranslate.addChild(rightEyeTransform);
              rightEyeTransform.addChild(rightEyeShape);
            head.addChild(rightEar);
              rightEar.addChild(rightEarTranslate);
              rightEarTranslate.addChild(rightEarRotate);
              rightEarRotate.addChild(rightEarTransform);
              rightEarTransform.addChild(rightEarShape);
            head.addChild(leftEar);
              leftEar.addChild(leftEarTranslate);
              leftEarTranslate.addChild(leftEarRotate);
              leftEarRotate.addChild(leftEarTransform);
              leftEarTransform.addChild(leftEarShape);
            head.addChild(antenna);
              antenna.addChild(antennaTranslate);
              antennaTranslate.addChild(antennaRotate);
              antennaRotate.addChild(antennaTransform);
              antennaTransform.addChild(antennaShape);
            antenna.addChild(antennaBall);
              antennaBall.addChild(antennaBallTransform);
              antennaBallTransform.addChild(antennaBallShape);
            body.addChild(leftArm);
              leftArm.addChild(leftArmTranslate);
              leftArmTranslate.addChild(leftArmRotate);
              leftArmRotate.addChild(leftArmTransform);
              leftArmTransform.addChild(leftArmShape);
            body.addChild(rightArm);
              rightArm.addChild(rightArmTranslate);
              rightArmTranslate.addChild(rightArmRotate);
              rightArmRotate.addChild(rightArmTransform);
              rightArmTransform.addChild(rightArmShape);
            

        alienRoot.update();
    }

    private Model makeSphereAntennaBall(GL3 gl, Texture t1, Texture t2){
        String name = "Sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0, 0.5f, 0));
        Model sphere = new Model(name, mesh, modelMatrix, shader, material, light, spotlight, camera, t1, t2);
        return sphere;
    }
    private Model makeSphere(GL3 gl, Texture t1){
        String name = "Sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader2 = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0, 0.5f, 0));
        Model sphere = new Model(name, mesh, modelMatrix, shader2, material, light, spotlight, camera, t1);
        return sphere;
    }
    private Model makeSphereEyes(GL3 gl, Texture t1){
        String name = "Sphere Eyes";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        shader3 = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_eyes.txt");
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0, 0.5f, 0));
        Model sphere = new Model(name, mesh, modelMatrix, shader3, material, light, spotlight, camera, t1);
        return sphere;
    }

    //Method to rock animate alien body
    public void alienAnimation(double elapsedTime){
      rotateAllAngle = 25*(float)Math.sin(elapsedTime);
      rotateAll.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngle));
      alienRoot.update();
    }

    //Method to handle alien head roll animation 
    public void rollHeadAnimation(double elapsedTime){
      rollHeadAngle = 20*(float)Math.sin(elapsedTime);
      rollHead.setTransform(Mat4Transform.rotateAroundZ(rollHeadAngle));
      alienRoot.update();
    }

    //Resets alien to initial location
    public void resetAlien(){
      rotateAll.setTransform(Mat4Transform.rotateAroundZ(0));
      alienRoot.update();
      rollHead.setTransform(Mat4Transform.rotateAroundZ(0));
      alienRoot.update();
    }

    //Method to set the lightON value to turn the world lighting 'on' and 'off' 
    public void setLightONOFF(boolean value){
      lightON = value;
    }

    //Method to set the spotlighON value to turn the spot light 'on' and 'off' 
    public void setSpotlightONOFF(boolean value){
      spotlightON = value;
    }

    public void render(GL3 gl){
        alienRoot.draw(gl);
        shader.use(gl);
        if(lightON==true){
          shader.setInt(gl, "lightOn", 1);
        }
        else if(lightON==false){
          shader.setInt(gl, "lightOn", 0);
        }
        
        shader2.use(gl); 
        if(lightON==true){
          shader2.setInt(gl, "lightOn", 1);
        }
        else if(lightON==false){
          shader2.setInt(gl, "lightOn", 0);
        }

        // Spotlight on off effect on the alien
        if (spotlightON == false) {
          shader.use(gl);
          shader.setInt(gl, "spotlightOn", 0);
        } else if (spotlightON == true) {
          shader.use(gl);
          shader.setInt(gl, "spotlightOn", 1);
        }

        if (spotlightON == false) {
          shader2.use(gl);
          shader2.setInt(gl, "spotlightOn", 0);
        }
        if (spotlightON == true) {
          shader2.use(gl);
          shader2.setInt(gl, "spotlightOn", 1);
        }

    }

    public void dispose(GL3 gl){
        sphere.dispose(gl);
    }

  // ***************************************************
  /* Calculating Time  */ 
  
    private double startTime;
    
    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }
    
}