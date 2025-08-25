# Aliens in the snow!
## 3D Scene with Alien models, rotating spotlight and snowing backdrop made in JOGL

## Link to video demonstration: 
[![Demo]](demo.gif)
[![Watch the video]](https://youtu.be/RbSobcbRHb8)

## Overview
This project showcases a 3D scene featuring two alien models made using spheres, a spotlight that rotates around a set angle, and two backdrop planes (vertical and horizontal). This scene was created using JOGL (Java binding for the OpenGL graphics API).

## Prerequisites
Following needs to be installed before running the program:
- JDK
- JOGL Libraries


## Usage
Simply click and execute the Alien.bat file to run.
OR
Compile and run the Java application to view the 3D scene using:
```java
javac Alien.java
java Alien
```

## Features
- Two alien models made using spheres.
    - The alien model is made of 10 different spheres. 1 body, 1 head, 2 ears, 2 eyes, 2 arms, 1 head antenna and 1 head antenna ball.
    - The alien model is properly textured to show the details on the model.
    - The alien model can rock it's body along with other parts from side to side in a motion.
    - The head can also be rolled continuously from one side to another. Although only the head(including ears, eyes and antenna) is animated for this particular effect.

- Spotlight model made using spheres.
    - The spotlight model is made of 2 spheres: The spotlight standing bar and the upper spotlight part.
    - The upper part of the spotlight rotates around a specific cutoff in the scene.
    - Spotlight will have a spotlight-light effect that will be displayed on the models so that if the world lights are turned off, the spotlight will only show the area it is illuminating.

- Snowy Backdrop.
    - Two planes rendered are made using two triangles each.
    - The vertical and horizontal planes are using texture maps to match each other at the join.
    - The vertical plane has the snowing effect which is just a moving texture on top of the base texture.

- Two World Lights.
    - Two world lights are placed in the world to illuminate the entire scene.
    - These two lights can be turned on or off separately 

- Texture images(No Copyright images) are sourced from the internet.

## Controls
- Use the mouse and keyboard to navigate the camera view
- Rock or Roll both aliens synchronously.
- Stop the animation or reset the alien models to its initial place
- Turn on or off both the world lights
- Turn on or off the spotlight light. 

## File structure
- Alien.java: Main application file containing the main method to display the scene
- AlienGLEventListener: Class containing the scene setup and rendering logic
- AlienModel.java: Class containing the Alien model scene graph, shader and textures
- SpotlightModel.java: Class containing the Spotlight model scene graph, shader and textures.
- Floors.java: Class containing the plane models 

## Credits
- Alien, spotlight, plane models created by me: Prajwal Patil
- Portion of the code provided by Dr. Steve Maddock in the lab class material
