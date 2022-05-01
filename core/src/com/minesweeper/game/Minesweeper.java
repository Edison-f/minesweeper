package com.minesweeper.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

public class Minesweeper extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	private static final float SCALE = 2.0f;
	public static final float PIXEL_PER_METER = 32f;

	public static int[] touchInputs = new int[2];
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	BitmapFont font;
	
	int width;
	int height;

	@Override
	public void create () {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE, Gdx.graphics.getHeight() / SCALE);
		
		font = new BitmapFont();

		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		// img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		periodic();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.LIGHT_GRAY);
		shapeRenderer.rect(width / 20, height / 20, (width - width / 10), (height - height / 10));
		shapeRenderer.end();
		batch.begin();
		font.draw(batch, "x: " + touchInputs[0], 30, 50);
		font.draw(batch, "y: " + touchInputs[1], 30, 30);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void setTouchInputs() {
		if(Gdx.input.isTouched()){
			touchInputs[0] = Gdx.input.getX();
			touchInputs[1] = Gdx.input.getY();
		} else {
			touchInputs[0] = -1;
			touchInputs[1] = -1;
		}
	}
	
	public void periodic() {
		setTouchInputs();
	}
}
	
	
	


