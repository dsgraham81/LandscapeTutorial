package com.dsgraham.landscape;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

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
    protected float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];

    //The current index that we are pushing triangles into the array
    protected int idx = 0;

    private Mesh mesh;
    private int width;
    private int height;


    public  Land() {
        mesh = new Mesh(true, MAX_VERTS, 6,
                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));

    }


}
