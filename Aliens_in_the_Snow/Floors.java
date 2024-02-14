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

public class Floors {

    private Camera camera;
    private Light[] light;
    private Light spotlight;
    private float size = 30f;
    private Model floor, wall;
    public float offsetX;
    public float offsetY;
    public Shader shader, shader2;
    public boolean lightON = true;

    //Floors class Constructor which takes in array of lights, spotlight and multiple textures for a floor and a vertical wall 
    public Floors(GL3 gl, Camera camera, Light[] light, Light spotlight, Texture t1, Texture t2, Texture t3){
        this.camera = camera;
        this.light = light;
        this.spotlight = spotlight;

        startTime = getSeconds();

        floor = makeFloor(gl, t1);
        wall = makeWall(gl, t2, t3);
    }   


    //Method to create a horizontal plane 
    public Model makeFloor(GL3 gl, Texture t1){
        String name = "Floor";
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader2 = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_floor.txt");
        Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 matx = Mat4Transform.scale(size, 1f, size);
        floor = new Model(name, mesh, matx, shader2, material, light, spotlight, camera, t1);
        return floor;
    }

    //Method to make a Vertical plane
    public Model makeWall(GL3 gl, Texture t1, Texture t2){
        String name = "Back wall";
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        shader = new Shader(gl, "shaders/vs_backwall.txt", "shaders/fs_backwall.txt");
        Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 matx = Mat4Transform.scale(size, 1f, size);
        matx = Mat4.multiply(Mat4Transform.rotateAroundX(90), matx);
        matx = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*0.5f), matx);
        wall = new Model(name, mesh, matx, shader, material, light, spotlight, camera, t1, t2);
        return wall;
    }


    //Calculating time
    private double startTime;
    
    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    //For the light turn on or off
    public void setLightONOFF(boolean value){
      lightON = value;
    }

    //Render method
    public void render(GL3 gl){
        floor.render(gl);
        wall.render(gl);

        shader.use(gl);
        if(lightON==true){
          shader.setInt(gl, "lightOn", 1);
        }
        else if(lightON==false){
          shader.setInt(gl, "lightOn", 0);
        }
        //
        shader2.use(gl); 
        if(lightON==true){
          shader2.setInt(gl, "lightOn", 1);
        }
        else if(lightON==false){
          shader2.setInt(gl, "lightOn", 0);
        }
        
        shader.use(gl);
        //This code will supply the parameters to the fragment shader so that another animating texture of snow can be applied
        double elapsedTime = getSeconds() - startTime;
        double t = elapsedTime*0.1f; 
        offsetX = 0.0f;
        offsetY = (float)(t - Math.floor(t));
        
        shader.setFloat(gl, "offset1", offsetX, offsetY);

        offsetX = 0.2f;
        offsetY = (float)(t - Math.floor(t));
        shader.setFloat(gl, "offset2", offsetX, offsetY);

        shader.setFloat(gl, "offset3", offsetX, offsetY);

        shader.setInt(gl, "first_texture", 0);
        shader.setInt(gl, "second_texture", 1);


    }
    public void dispose(GL3 gl){
        floor.dispose(gl);
        wall.dispose(gl);
    }

}