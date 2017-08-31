package com.dsgraham.landscape;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

public class LandscapeTutorial extends ApplicationAdapter implements InputProcessor {

	PerspectiveCamera camera;
	Land land;
	private int mouseX;
	private int mouseY;
	
	@Override
	public void create () {
		camera = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(60, 50, 80);
		camera.lookAt(0,0,0);
		camera.far = 1000;
		camera.update();

		mouseX = 0;
		mouseY = 0;
		Gdx.input.setInputProcessor(this);
		land = new Land();
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		land.render(camera);
	}
	
	@Override
	public void dispose () {

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseX = screenX;
		mouseY = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}


	private float rotation = .5f;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		int magX = Math.abs(mouseX - screenX);
		int magY = Math.abs(mouseY - screenY);

		if (mouseX > screenX) {
			camera.rotate(Vector3.Y, 1 * magX * rotation);
			camera.update();
		}

		if (mouseX < screenX) {
			camera.rotate(Vector3.Y, -1 * magX * rotation);
			camera.update();
		}

		if (mouseY < screenY) {
			if (camera.direction.y > -0.965)
				camera.rotate(camera.direction.cpy().crs(Vector3.Y), -1 * magY * rotation);
			camera.update();
		}

		if (mouseY > screenY) {

			if (camera.direction.y < 0.965)
				camera.rotate(camera.direction.cpy().crs(Vector3.Y), 1 * magY * rotation);
			camera.update();
		}

		mouseX = screenX;
		mouseY = screenY;

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
