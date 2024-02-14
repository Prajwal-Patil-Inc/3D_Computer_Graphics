import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;

public class Model {
  
  private String name;
  private Mesh mesh;
  private Mat4 modelMatrix;
  private Shader shader;
  private Material material;
  private Camera camera;
  private Light[] lights;
  private Light spotlight;  //Declaring another light source for spotlight effect
  private Texture diffuse;
  private Texture specular;

  //Declaring the spotlight light direction, cutoff and outerCutoff values
  private Vec3 spotlightDirection = new Vec3 (2.5f, -5.0f, 0.5f);
  private float spotlightCutOff = (float) Math.cos(Math.toRadians(15));
  private float spotlightOuterCutOff = (float) Math.cos(Math.toRadians(25));

  public Model() {
    name = null;
    mesh = null;
    modelMatrix = null;
    material = null;
    camera = null;
    lights = null;
    spotlight = null;
    shader = null;
  }
  
  public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] light, Light spotlight,Camera camera, Texture diffuse, Texture specular) {
    this.name = name;
    this.mesh = mesh;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.material = material;
    this.lights = light;
    this.spotlight = spotlight;
    this.camera = camera;
    this.diffuse = diffuse;
    this.specular = specular;
  }
  
  public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] light, Light spotlight, Camera camera, Texture diffuse) {
    this(name, mesh, modelMatrix, shader, material, light, spotlight, camera, diffuse, null);
  }
  
  public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] light, Camera camera, Texture t1) {
      this.name = name;
      this.mesh = mesh;
      this.modelMatrix = modelMatrix;
      this.shader = shader;
      this.material = material;
      this.lights = light;
      this.camera = camera;
      this.diffuse = t1;
      this.specular = null;
  }

  public void setName(String s) {
    this.name = s;
  }

  public void setMesh(Mesh m) {
    this.mesh = m;
  }

  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setMaterial(Material material) {
    this.material = material;
  }

  public void setShader(Shader shader) {
    this.shader = shader;
  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setLight(Light[] light) {
    this.lights = light;
  }
  public void setLight(Light spotlight) {
    this.spotlight = spotlight;
  }

  public void setDiffuse(Texture t) {
    this.diffuse = t;
  }

  public void setSpecular(Texture t) {
    this.specular = t;
  }

  public void renderName(GL3 gl) {
    System.out.println("Name = "+name);  
  }

  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }

  // second version of render is so that modelMatrix can be overriden with a new parameter
  public void render(GL3 gl, Mat4 modelMatrix) {
    if (mesh_null()) {
      System.out.println("Error: null in model render");
      return;
    }

    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));

    // set shader variables. Be careful that these variables exist in the shader

    shader.use(gl);

    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    shader.setInt(gl, "numLights", lights.length);

    for(int i=0; i<lights.length; i++){
      shader.setVec3(gl, "lights["+i+"].position", lights[i].getPosition());
      shader.setVec3(gl, "lights["+i+"].ambient", lights[i].getMaterial().getAmbient());
      shader.setVec3(gl, "lights["+i+"].diffuse", lights[i].getMaterial().getDiffuse());
      shader.setVec3(gl, "lights["+i+"].specular", lights[i].getMaterial().getSpecular());
    }
     
    //Sending the values to the fragment shader for the spotlight effect
    shader.setVec3(gl, "spotLight.position", spotlight.getPosition());
    shader.setVec3(gl, "spotLight.direction", spotlightDirection);
    shader.setVec3(gl, "spotLight.ambient", new Vec3(1.1f, 1.1f, 1.1f));
    shader.setVec3(gl, "spotLight.diffuse", new Vec3(0.8f, 0.8f, 0.8f));
    shader.setVec3(gl, "spotLight.specular", new Vec3(1.5f, 1.5f, 1.5f));
    shader.setFloat(gl, "spotLight.constant", 1.0f);
    shader.setFloat(gl, "spotLight.linear", 0.027f);
    shader.setFloat(gl, "spotLight.quadratic", 0.0028f);
    shader.setFloat(gl, "spotLight.cutOff", spotlightCutOff);
    shader.setFloat(gl, "spotLight.outerCutOff", spotlightOuterCutOff);   

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    // If there is a mismatch between the number of textures the shader expects and the number we try to set here, then there will be problems.
    // Assumption is the user supplied the right shader and the right number of textures for the model

    if (diffuse!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      diffuse.bind(gl);
    }
    if (specular!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      specular.bind(gl);
    }

    // then render the mesh
    mesh.render(gl);
  } 
  
  private boolean mesh_null() {
    return (mesh==null);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);  // only need to dispose of mesh
  }
  
}