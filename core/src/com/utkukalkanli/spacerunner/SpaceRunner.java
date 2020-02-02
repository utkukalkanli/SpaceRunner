package com.utkukalkanli.spacerunner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SpaceRunner extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture ufo;
	Texture enemy1;
	Texture enemy2;
	Texture enemy3;
	float ufoX = 0;
	float ufoY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.5f;
	float enemyVelocity = 2;
	Random random;
	int score = 0;
	int scoredEnemy = 0;
	BitmapFont font;
	BitmapFont font2;

	Circle ufoCircle;

	ShapeRenderer shapeRenderer;

	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];

	float [] enemyOffset = new float[numberOfEnemies];
	float [] enemyOffset2 = new float[numberOfEnemies];
	float [] enemyOffset3 = new float[numberOfEnemies];

	float distance = 0;

	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("Background-1.jpg");
		ufo = new Texture("skull_ufo.png");
		enemy1 = new Texture("enemy_ufo.png");
		enemy2 = new Texture("enemy_ufo.png");
		enemy3 = new Texture("enemy_ufo.png");

		distance = Gdx.graphics.getWidth() / 2;

		random = new Random();

		ufoX = Gdx.graphics.getWidth() / 2 - ufo.getWidth() / 2;
		ufoY = Gdx.graphics.getWidth() / 3;

		shapeRenderer = new ShapeRenderer();

		ufoCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(6);

		for(int i = 0; i < numberOfEnemies; i++){

			enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2 + i * distance;

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}

	}

	@Override
	public void render () {
		// oyunun dinamik kısmı, ekrana giren objeler akış vs.
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState == 1){

			if(enemyX[scoredEnemy] < Gdx.graphics.getWidth()/2 - ufo.getWidth()/2){
				score++;
				if(scoredEnemy < numberOfEnemies - 1){
					scoredEnemy++;
				}else{
					scoredEnemy = 0;
				}
			}

			if(Gdx.input.justTouched()){
				velocity = -15;
			}

			for (int i = 0; i < numberOfEnemies; i++){

				if(enemyX[i] < Gdx.graphics.getWidth() / 15){
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;
					enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				}else{
					enemyX[i] = enemyX[i] - enemyVelocity;
				}

				enemyX[i] = enemyX[i] - enemyVelocity;
				batch.draw(enemy1, enemyX[i], Gdx.graphics.getHeight()/2 + enemyOffset[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(enemy2, enemyX[i], Gdx.graphics.getHeight()/2 + enemyOffset2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(enemy3, enemyX[i], Gdx.graphics.getHeight()/2 + enemyOffset3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

				enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOffset[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
				enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
				enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
			}

			if(ufoY > 0){
					velocity = velocity + gravity;
					ufoY = ufoY - velocity;
			}
			else{
				gameState = 2;
			}

		} else if (gameState == 0){
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if(gameState == 2){
			font2.draw(batch,"Game Over ! Tap to play again.",100,Gdx.graphics.getHeight() / 2);
			if(Gdx.input.justTouched()){
				gameState = 1;
				ufoY = Gdx.graphics.getWidth() / 3;

				for(int i = 0; i < numberOfEnemies; i++){

					enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - enemy1.getWidth() / 2 + i * distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0;
				scoredEnemy = 0;
				score = 0;
			}
		}



		batch.draw(ufo, ufoX, ufoY, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);

		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		ufoCircle.set(ufoX + Gdx.graphics.getWidth() / 20, ufoY + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(ufoCircle.x,ufoCircle.y, ufoCircle.radius);

		for(int i = 0; i < numberOfEnemies; i++){
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOffset[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOffset2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight()/2 + enemyOffset3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);

			if(Intersector.overlaps(ufoCircle, enemyCircles[i]) || Intersector.overlaps(ufoCircle, enemyCircles2[i]) || Intersector.overlaps(ufoCircle, enemyCircles3[i])){
				System.out.println("collision detected");
				gameState = 2;
			}


		}
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
