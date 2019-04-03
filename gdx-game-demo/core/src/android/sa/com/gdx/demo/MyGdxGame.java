package android.sa.com.gdx.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture coinTexture;
	private Texture bombTexture;
	private Texture manDizzy;
	private Texture[] man = new Texture[4];
	private int manState;
	private int manPause;
	private float gravity = .2f;
	private float velocity = 0f;
	private int manY = 0;
	private int manX = 0;
	private boolean doTouch = true;
	private List<GridPoint2> coinXYs = new ArrayList<>();
	private List<GridPoint2> bombXYs = new ArrayList<>();
	private int spawnCoinPause;
	private int spawnBombPause;
	private Random random;
	private Rectangle manRect;
	private Rectangle rectangle;
	private int score;
	private BitmapFont scoreBimap;
	private int gameState = -1;
	private final int RUNNING = 0;
	private final int WAITING_TO_START = -1;
	private final int GAMEOVER = 1;
	private int bombDelay;
	private int coinDelay;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		coinTexture = new Texture("coin.png");
		bombTexture = new Texture("bomb.png");
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manDizzy = new Texture("dizzy-1.png");
		scoreBimap = new BitmapFont();
		scoreBimap.setColor(Color.WHITE);
		scoreBimap.getData().scale(8);
		gameInit();
	}

	private void gameInit() {
		gameState = WAITING_TO_START;
		manState = 0;
		score = 0;
		velocity = 0;
        manPause = 0;
		manY = Gdx.graphics.getHeight()/2;
		manX = Gdx.graphics.getWidth() / 2 - man[0].getWidth() / 2;
		random = new Random(System.currentTimeMillis());
		manRect = new Rectangle(0,0,man[0].getWidth(),man[0].getHeight());
		coinXYs.clear();
		bombXYs.clear();
        spawnCoinPause = 0;
        spawnBombPause = 0;
		bombDelay = 250;
		coinDelay = 100;
		gravity = .2f;
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        Texture currentMan = getCurrentMan();
        String msg = String.valueOf(score);
		switch (gameState){
            case 0: // running
                if( Gdx.input.justTouched() && doTouch) {
                    velocity = -10;
                }
                if(manY != 0) { //if man is not at bottom
                    velocity += gravity;
                }
                manY -=velocity;
                if (manY < 0) {
					manY = 0;
					velocity = 0;
                }
                if (manY > Gdx.graphics.getHeight() - currentMan.getHeight() ) {//when man reaches top
                    manY = Gdx.graphics.getHeight() - currentMan.getHeight();
                    doTouch = false;
                } else {
                    doTouch = true;
                }
                drawCoins();
                drawBombs();
                collisionCheck();
                break;
            case 1: //game over
                if( Gdx.input.justTouched() ) {
					gameInit();
                }
                break;
            default: // start
                if( Gdx.input.justTouched() ) {
                    gameState = RUNNING;
                }
				msg = "Touch to start..";
        }
		batch.draw(currentMan, manX, manY);
        scoreBimap.draw(batch, msg,100,200);
		batch.end();
	}

    private void collisionCheck() {
        manRect.x = manX;
        manRect.y = manY;
        for (int i = 0; i < coinXYs.size(); i++) {
            GridPoint2 pt = coinXYs.get(i);
            rectangle = new Rectangle(pt.x,pt.y,coinTexture.getWidth(),coinTexture.getWidth());
            if( Intersector.overlaps(manRect,rectangle) ){
            score++;
            coinXYs.remove(i);
            }
        }
        for (int i = 0; i < bombXYs.size(); i++) {
            GridPoint2 pt = bombXYs.get(i);
            rectangle = new Rectangle(pt.x,pt.y,bombTexture.getWidth(),bombTexture.getWidth());
            if( Intersector.overlaps(manRect,rectangle) ){
            bombXYs.remove(i);
            gameState = GAMEOVER;
            }
        }
    }

    private void drawCoins() {
		if(spawnCoinPause < coinDelay){
			spawnCoinPause++;
		} else {
			spawnCoinPause = 0;
			coinXYs.add( new GridPoint2(Gdx.graphics.getWidth(),random.nextInt(Gdx.graphics.getHeight())) );
		}

		for (int i = 0; i < coinXYs.size(); i++) {
			GridPoint2 pt = coinXYs.get(i);
			if (pt.x > 0) {
				pt.x -= 10;
				batch.draw(coinTexture, pt.x, pt.y);
			} else {
				coinXYs.remove(i);
			}
		}
	}

	private void drawBombs() {
		if(spawnBombPause < bombDelay) {
			spawnBombPause++;
		} else {
			spawnBombPause = 0;
			bombXYs.add( new GridPoint2(Gdx.graphics.getWidth(),random.nextInt(Gdx.graphics.getHeight())) );
		}

		for (int i = 0; i < bombXYs.size(); i++) {
			GridPoint2 pt = bombXYs.get(i);
			if (pt.x > 0) {
				pt.x -= 10;
				batch.draw(bombTexture, pt.x, pt.y);
			} else {
				bombXYs.remove(i);
			}
		}
	}

	private Texture getCurrentMan() {
	    if(gameState == GAMEOVER){
	        return manDizzy;
        }
		if(manPause < 10){
			manPause++;
		}else {
			manPause =0;
			if(manState < 3) {
				manState++;
			} else {
				manState = 0;
			}
		}
		return man[manState];
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
