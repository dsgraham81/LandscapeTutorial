package com.dsgraham.landscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Doug Graham on 8/30/2017.
 */
public class Land {

    //Position attribute - (x, y, z)
    public static final int POSITION_COMPONENTS = 3;

    //Color attribute - (r, g, b, a)
    public static final int COLOR_COMPONENTS = 4;

    //Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;

    //The "size" (total number of floats) for a single triangle
    public static final int PRIMITIVE_SIZE = 3 * NUM_COMPONENTS;

    //The maximum number of triangles our mesh will hold
    public static final int MAX_TRIS = 2;

    //The maximum number of vertices our mesh will hold
    public static final int MAX_VERTS = MAX_TRIS * 3;

    //The array which holds all the data, interleaved like so:
//    x, y, r, g, b, a
//    x, y, r, g, b, a,
//    x, y, r, g, b, a,
//    ... etc ...
    protected float[] verts;

    protected short[] indices;

    private Mesh mesh;
    private int width;
    private int height;
    private float[][] heightData;
    private ShaderProgram shader;


    public  Land() {
        shader = new ShaderProgram(Gdx.files.internal("vertex.vert"),
                Gdx.files.internal("fragment.frag"));


        loadHeightData();
        setUpVertices();
        setUpIndices();

        mesh = new Mesh(true, width * height * NUM_COMPONENTS, (width -1) * (height -1) * 6,
                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        mesh.setVertices(verts);
        mesh.setIndices(indices);
    }

    void setUpVertices(){
        verts = new float[width * height * NUM_COMPONENTS];
        int index = 0;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                verts[index++] = x;
                verts[index++] = heightData[x][y];
                verts[index++] = y;

                verts[index++] = 1.0f;
                verts[index++] = 1.0f;
                verts[index++] = 1.0f;
                verts[index++] = 1.0f;

            }
        }
    }

    void setUpIndices(){
        indices = new short[(width -1) * (height -1) * 6];
        int index = 0;
        for (int y =0; y < height -1; y++){
            for (int x = 0; x < width -1; x++){
                short lowerLeft = (short) (x + y*width);
                short lowerRight = (short) ((x + 1) + y*width);
                short topLeft = (short) (x + (y + 1) * width);
                short topRight = (short) ((x + 1) + (y + 1) * width);

                indices[index++] = lowerLeft;
                indices[index++] = topLeft;
                indices[index++] = lowerRight;

                indices[index++] = topLeft;
                indices[index++] = topRight;
                indices[index++] = lowerRight;
            }
        }
    }

    void loadHeightData(){
        Texture heightTexture = new Texture("heightmap.png");
        width = heightTexture.getWidth();
        height = heightTexture.getHeight();

        heightData = new float[width][height];
        heightTexture.getTextureData().prepare();
        Pixmap pixels = heightTexture.getTextureData().consumePixmap();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int pixelData = (pixels.getPixel(x, y) & 0xff000000) >> 24;
                heightData[x][y] = pixelData/5f;
            }
        }

    }

    public void render(Camera cam){
        shader.begin();
        shader.setUniformMatrix("u_projTrans", cam.combined);

        mesh.render(shader, GL20.GL_TRIANGLES);

        shader.end();
    }


}
