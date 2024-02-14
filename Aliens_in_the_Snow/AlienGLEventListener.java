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

public class AlienGLEventListener implements GLEventListener{
    
    private static final boolean DISPLAY_SHADERS = false;

    public AlienGLEventListener(Camera camera){
        this.camera = camera;
        this.camera.setPosition(new Vec3(0f,10f,25f));
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

    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        light[0].dispose(gl);
        light[1].dispose(gl);
        spotlightLight.dispose(gl);
        floor.dispose(gl);
        alien1.dispose(gl);
        alien2.dispose(gl);
        spotlight.dispose(gl);
    }

    //Window events
    private boolean rockAnimation = false;
    private boolean rollAnimation = false;
    private boolean resetAlien = false;
    private boolean light1On = true;
    private boolean light2On = true;
    private boolean spotlightLightOn = true;
    private double savedTime = 0;

    public void startRockAnimation(){
        rollAnimation = false;
        rockAnimation = true;
    }

    public void startRollAnimation(){
        rollAnimation = true;
        rockAnimation = false;
    }

    public void stopAnimation(){
        rockAnimation = false;
        rollAnimation = false;
        resetAlien = false;
    }

    public void reset(){
        rockAnimation = false;
        rollAnimation = false;
        resetAlien = true;
    }

    public void light1Off(){
        light1On = false;
    }
    public void light1ON(){
        light1On = true;
    }
    public void light2Off(){
        light2On = false;
    }
    public void light2ON(){
        light2On = true;
    }
    public void spotlightsOn(){
        spotlightLightOn = true;
    }
    public void spotlightsOff(){
        spotlightLightOn = false;
    }

    //Scene
    private TextureLibrary textures;

    private Camera camera;
    private Mat4 perspective;
    private Light[] light = new Light[2];
    private Light spotlightLight;

    //Floor
    private Floors floor;

    //Aliens
    private AlienModel alien1, alien2;

    //Spotlight
    private SpotlightModel spotlight;

    private void initialise(GL3 gl){
        textures = new TextureLibrary();
        textures.add(gl, "body_texture1", "textures/body_texture1.jpg");
        textures.add(gl, "body_texture2", "textures/body_texture2.jpg");
        textures.add(gl, "headball", "textures/headball.jpg");
        textures.add(gl, "headball_specular", "textures/headball_specular.jpg");
        textures.add(gl, "snow_floor","textures/snow_floor.jpg");
        textures.add(gl, "snow_wall", "textures/snow_wall.jpg"); 
        textures.add(gl, "snow", "textures/snow.jpg");

        light[0] = new Light(gl);
        light[0].setCamera(camera);
        light[1] = new Light(gl);
        light[1].setCamera(camera);
        spotlightLight = new Light(gl);
        spotlightLight.setCamera(camera);

        //Floors
        floor = new Floors(gl, camera, light, spotlightLight, textures.get("snow_floor"), textures.get("snow_wall"), textures.get("snow"));

        //Alien 1
        alien1 = new AlienModel(gl, camera, light, spotlightLight, -3, 0, 2, textures.get("body_texture1"), textures.get("body_texture1"), textures.get("headball"), textures.get("headball_specular"), textures.get("eyes"));

        //Alien 2
        alien2 = new AlienModel(gl, camera, light, spotlightLight, 3, 0, 2, textures.get("body_texture2"), textures.get("body_texture2"), textures.get("headball"), textures.get("headball_specular"), textures.get("eyes"));

        //Spotlight
        spotlight = new SpotlightModel(gl, camera, light, spotlightLight, textures.get("headball"));
    }

    private void render(GL3 gl){
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        if(light1On){
            light[0].setPosition(new Vec3(-7.2f, 13, 25));
            alien1.setLightONOFF(true);
            alien2.setLightONOFF(true);
            spotlight.setLightONOFF(true);
            floor.setLightONOFF(true);
            light[0].render(gl);
        }
        if(!light1On){
            light[0].setPosition(new Vec3(-1700, -1003, -2500));
            alien1.setLightONOFF(false);
            alien2.setLightONOFF(false);
            spotlight.setLightONOFF(false);
            floor.setLightONOFF(false);
            light[0].render(gl);
        }
        if(light2On){
            light[1].setPosition(new Vec3(7.2f, 13, 25));
            alien1.setLightONOFF(true);
            alien2.setLightONOFF(true);
            spotlight.setLightONOFF(true);
            floor.setLightONOFF(true);
            light[1].render(gl);
        }
        if(!light2On){
            light[1].setPosition(new Vec3(1700, 1003, 2500));
            alien1.setLightONOFF(false);
            alien2.setLightONOFF(false);
            spotlight.setLightONOFF(false);
            spotlightLight.render(gl);
            floor.setLightONOFF(false);
            light[1].render(gl);
        }
        if(spotlightLightOn){
            spotlightLight.setPosition(spotlight.getLightPosition());
            alien1.setSpotlightONOFF(true);
            alien2.setSpotlightONOFF(true);
            // spotlightLight.render(gl);
        }
        else if(!spotlightLightOn){
            spotlightLight.setPosition(new Vec3(1700, 1003, 2500));
            alien1.setSpotlightONOFF(false);
            alien2.setSpotlightONOFF(false);
        }


        floor.render(gl);
        alien1.render(gl);
        alien2.render(gl);
        spotlight.render(gl);

        if(rockAnimation){
            double elapsedTime = getSeconds()-startTime;
            alien1.alienAnimation(elapsedTime);
            alien2.alienAnimation(elapsedTime);
        }
        else if(rollAnimation){
            double elapsedTime = getSeconds()-startTime;
            alien1.rollHeadAnimation(elapsedTime);
            alien2.rollHeadAnimation(elapsedTime);
        }
        else if(resetAlien){
            alien1.resetAlien();
            alien2.resetAlien();
        }
    }

    private Vec3 getLightPosition() {
        double elapsedTime = getSeconds()-startTime;
        float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
        float y = 2.7f;
        float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
        return new Vec3(x,y,z);   
    }


// ***************************************************
  /* Calculating Time */ 
  
   public double startTime;
    
   private double getSeconds() {
       return System.currentTimeMillis()/1000.0;
   }

    

}
