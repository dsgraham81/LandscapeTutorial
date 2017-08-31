package com.dsgraham.landscape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Doug Graham on 8/30/2017.
 */
public class Land {

    //Position attribute - (x, y, z)
    public static final int POSITION_COMPONENTS = 3;

    //Color attribute - (r, g, b, a)
    public static final int COLOR_COMPONENTS = 1;

    public static final int NORMAL_COMPONENTS = 3;

    //Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS + NORMAL_COMPONENTS;

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
    private float minHeight;
    private float maxHeight;
    private ShaderProgram shader;
    private Matrix4 worldTransform;


    public  Land() {
        shader = new ShaderProgram(Gdx.files.internal("vertex.vert"),
                Gdx.files.internal("fragment.frag"));
        worldTransform = new Matrix4();

        loadHeightData();
        setUpVertices();
        setUpIndices();

        mesh = new Mesh(true, width * height * NUM_COMPONENTS, (width -1) * (height -1) * 6,
                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, "a_color"),
                new VertexAttribute(VertexAttributes.Usage.Normal, NORMAL_COMPONENTS, "a_normal"));
        mesh.setVertices(verts);
        mesh.setIndices(indices);
    }

    Vector3 tempNormal = new Vector3();
    void setUpVertices(){
        verts = new float[width * height * NUM_COMPONENTS];
        int index = 0;
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                verts[index++] = x;
                verts[index++] = heightData[x][y];
                verts[index++] = y;

                verts[index++] = getVertexColor(x, y);

                calculateNormal(x, y, tempNormal);
                verts[index++] = tempNormal.x;
                verts[index++] = tempNormal.y;
                verts[index++] = tempNormal.z;

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
        minHeight = Float.MAX_VALUE;
        maxHeight = Float.MIN_VALUE;

        heightData = new float[width][height];
        heightTexture.getTextureData().prepare();
        Pixmap pixels = heightTexture.getTextureData().consumePixmap();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int pixelData = (pixels.getPixel(x, y) & 0xff000000) >>> 24;
                float height = pixelData/5f;
                if (maxHeight < height) maxHeight = height;
                if (minHeight > height) minHeight = height;
                heightData[x][y] = height;
            }
        }

    }

    Color tempColor = new Color();
    private float getVertexColor(int x, int y){
        float height = heightData[x][y];
        float delta = (maxHeight - minHeight)/4f;
        if (height < minHeight + delta) tempColor.set(0,0,1,1); // Blue Waters
        else if (height < minHeight + 2 * delta) tempColor.set(0,.5f,0,1); // Green Grass
        else if (height < minHeight + 3 * delta) tempColor.set(.6f,.2f,.2f,1); // Brown Mountain
        else tempColor.set(1,1,1,1); // White snow tops
        return tempColor.toFloatBits();
    }

    Vector3 tmpvec = new Vector3();
    Vector3 tmpvec2 = new Vector3();
    void calculateNormal(int x, int y, Vector3 normal){
        int prevX = x;
        if (x > 0) prevX = x -1;

        int nextX = x;
        if (x < width -1) nextX = x + 1;

        int prevY = y;
        if (y > 0) prevY = y -1;

        int nextY = y;
        if (y < height -1) nextY = y + 1;

        tmpvec.set(prevX, heightData[prevX][y], y).sub(nextX, heightData[nextX][y], y);
        tmpvec2.set(x, heightData[x][prevY], prevY).sub(x, heightData[x][nextY], nextY);

        tmpvec.crs(tmpvec2);
        normal.set(tmpvec);
    }

    float accum = 0;
    public void render(Camera cam){
        float dt = Gdx.graphics.getDeltaTime();
        accum += dt * 30f;
        shader.begin();
        worldTransform.idt();
        worldTransform.translate(width/2f, 0, height/2f);
        worldTransform.rotate(Vector3.Y, accum);
        worldTransform.translate(-width/2f, 0, -height/2f);
        shader.setUniformMatrix("u_projTrans", cam.combined);
        shader.setUniformMatrix("u_worldTrans", worldTransform);
        mesh.render(shader, GL20.GL_TRIANGLES);

        shader.end();
    }


}
