package com.amanjain.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;	// Texture is just the name given to images in gaming environment
	//ShapeRenderer shapeRenderer; // enables us to draw shapes
									// we need ShapeRenderer because Texture cannot do collision detection
	BitmapFont font;

	Texture birds[];
	int flapstate = 0, birdheight, birdwidth;
	float birdY, velocity = 0;
	int gamestate = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;

	Texture toptube;
	Texture bottomtube;
	int tubewidth, tubeheight;
	float gap = 200;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float tubeX[] = new float[numberOfTubes];
	float tubeOffset[] = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle topTubeRectangle[];
	Rectangle bottomTubeRectangle[];


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdheight = birds[flapstate].getHeight()/2;
		birdwidth = birds[flapstate].getWidth()/2;
		birdY = Gdx.graphics.getHeight()/2 - birdheight/2;


		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		tubeheight = toptube.getHeight() * 3/4;
		tubewidth = toptube.getWidth()/2;
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;

		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];
		for(int i = 0; i<numberOfTubes; i++)
		{
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - tubewidth/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();
		}

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

	}

	@Override
	public void render () {

		batch.begin();    //this just tells render method that we are going to display sprites now
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2 - tubewidth)
		{
			score ++;
			Gdx.app.log("Score", score + "");
			scoringTube = (scoringTube + 1) % 4;
		}

		if(gamestate == 1)
		{
			if(Gdx.input.isTouched()) {
				velocity = -10;
			}
			for(int i = 0; i<numberOfTubes; i++) {
				if(tubeX[i] < -tubewidth)
				{
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 100);
				}else {
					tubeX[i] -= tubeVelocity;
				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], tubewidth, tubeheight);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubeheight + tubeOffset[i], tubewidth, tubeheight);
				topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i], tubewidth, tubeheight);
				bottomTubeRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tubeheight + tubeOffset[i], tubewidth, tubeheight);

			}
			if(birdY > 0 || velocity < 0) {
				velocity++;
				birdY -= velocity;
			}
		}
		else {
			if(Gdx.input.justTouched())
				gamestate = 1;
		}

		if (flapstate == 0) {
			flapstate = 1;
		}
		else {
			flapstate = 0;
		}

		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birdwidth / 2, birdY, birdwidth, birdheight);
		font.draw(batch, String.valueOf(score), 50, 100);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birdheight/2, birdwidth/2);

		batch.end();

        /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);*/
		for(int i = 0;i < numberOfTubes; i++)
		{
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i], tubewidth, tubeheight);
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tubeheight + tubeOffset[i], tubewidth, tubeheight);

			if(Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[i]))
			{
				Gdx.app.log("Collision", "Yes!");
			}
		}
		//shapeRenderer.end();
	}
	
	/*@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
}
