package com.dsgraham.landscape;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LandscapeTutorial extends ApplicationAdapter {

	PerspectiveCamera camera;
	Land land;
	
	@Override
	public void create () {
		camera = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0, 10, 0);
		camera.lookAt(0,0,0);
		camera.update();

		land = new Land();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		land.render(camera);
	}
	
	@Override
	public void dispose () {

	}
}
