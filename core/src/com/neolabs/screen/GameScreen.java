package com.neolabs.screen;

import com.neolabs.api.SocketApi;
import com.neolabs.main.MainGame;
import com.neolabs.model.Bullet;
import com.neolabs.model.FollowCamera;
import com.neolabs.model.Player;
import com.neolabs.model.PlayerT;
import com.neolabs.ui.TouchpadFire;
import com.neolabs.utils.Constants;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GameScreen implements Screen, InputProcessor, ApplicationListener {
    private final MainGame game;
    private FollowCamera camera;
    private Stage stageUI;
    private Stage stageWorld;
    private TouchpadFire rightJoystick;
    private Touchpad leftJoystick;
    private SpriteBatch batch;
    private TiledMapRenderer tiledMapRenderer;
    private Timer timer;
    private Player iam;
    private HashMap<String, Player> players;
    private HashMap<String, Bullet> bullets;
    private HashMap<Integer, Texture> playerLevels;
    private Texture bulletTexture;
    private String name;
    private Pool<MoveToAction> pool;
    private int passFrame = 0;
    private PlayerT myPlayerConfig;
    private int height = Gdx.graphics.getHeight();
    private int width = Gdx.graphics.getWidth();
    private Preferences prefs = Gdx.app.getPreferences("PlayerSettings");
    private String playerName;


    public GameScreen(final MainGame game) {
        this.game = game;
        try {
            if (prefs.getString("name").isEmpty()) {
                System.out.println("name is empty");
                playerName = "user_" + game.getActivity().getAndroidId();
            } else {
                playerName = prefs.getString("name");
            }
            JSONObject playerConfig = new JSONObject();
            playerConfig.put("id", game.getActivity().getAndroidId().toString());
            playerConfig.put("name", playerName);
            this.myPlayerConfig = new PlayerT(playerConfig);
        } catch (Exception e) {
            System.out.println("[ERR] GameScreen:" + e);
        }
    }

    @Override
    public void show() {
        this.playerLevels = new HashMap<Integer, Texture>();
        for (int i = 1; i <= 20; i++) {
            Texture playerTexture = new Texture(Gdx.files.internal("skin/" + i + ".png"));
            playerLevels.put(i, playerTexture);
        }

        this.bulletTexture = new Texture(Gdx.files.internal("booom.png"));
        this.iam = new Player(playerLevels.get(1), game.font);
        this.players = new HashMap<String, Player>();
        this.bullets = new HashMap<String, Bullet>();

        initCamera();
        initUiScene();
        initWorldScene();
        initHardBeat();
        initNetwork();

//        initDebug();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        iam.updateController(leftJoystick, rightJoystick);
        try {
            stageWorld.act(delta);
        } catch (Throwable ignored) {
            String qwe = "";
        }

        stageUI.act(delta);
        camera.follow(iam, delta);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        stageWorld.getViewport().setCamera(camera);
        stageWorld.draw();
        stageUI.draw();
    }


    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stageWorld.dispose();
        stageUI.dispose();
    }

    //region network
    private void onGameSetup(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
            System.out.println("gameSetup" + data);
            String id = data.getString("id");
            float radius = (float) data.getDouble("radius");
            float x = (float) data.getDouble("x");
            float y = (float) data.getDouble("y");
            String name = (String) data.getString("name");
            int level = (int) data.getInt("level");
            int score = (int) data.getInt("score");
            int life = (int) data.getInt("life");
            iam.setId(id);
            iam.setWidth(radius * 2);
            iam.setHeight(radius * 2);
            iam.setScale(2f);
//            iam.setOrigin(0.5f, 0.5f);
            iam.setPosition(x, y);
            iam.setCenterOrigin();
            iam.setTexture(playerLevels.get(level));
            iam.setScore(score);
            iam.setName(name);
            iam.setLife(life);
            players.put(id, iam);
            stageWorld.addActor(iam);
        } catch (Exception e) {
            System.out.println("gameSetup" + e);
        }
    }

    private void onConnect(Object... args) {
        timer.start();
    }

    private void onDisconnect(Object... args) {
        try {
            String eventDisconnect = args[0].toString();
            System.out.println(Socket.EVENT_DISCONNECT + " OK: " + eventDisconnect);
            game.showMenu();
        } catch (Exception e) {
            System.out.println(Socket.EVENT_DISCONNECT + " ERROR: " + e);
        }
    }

    private void onServerTick(Object... args) {

        /*if (passFrame < 2) {
            passFrame++;
            return;
        } else {
            passFrame = 0;
        }*/
        JSONArray dataPlayers = (JSONArray) args[0];
        JSONArray dataBullets = (JSONArray) args[1];
        movePlayers(dataPlayers);
        moveBullets(dataBullets);
    }

    private void onPLayerJoin(Object... args) {
        try {
            JSONObject data = (JSONObject) args[0];
            String id = data.getString("id");
            String name = data.getString("name");
            float x = (float) data.getDouble("x");
            float y = (float) data.getDouble("y");
            float radius = (float) data.getDouble("radius");
            int level = (int) data.getInt("level");
            int score = (int) data.getInt("score");
            int life = (int) data.getInt("life");
            float speed = 1f;
            Player newPlayer = new Player(playerLevels.get(1), game.font);
            newPlayer.setId(id);
            newPlayer.setWidth(radius * 2);
            newPlayer.setHeight(radius * 2);
            newPlayer.setScale(2f);
//            newPlayer.setOrigin(0.5f, 0.5f);
            newPlayer.setPosition(x, y);
            newPlayer.setCenterOrigin();
            newPlayer.setTexture(playerLevels.get(level));
            newPlayer.setScore(score);
            newPlayer.setLife(life);
            newPlayer.setName(name);
            players.put(id, newPlayer);
            stageWorld.addActor(newPlayer);
            System.out.println("playerJoin OK: " + id);
        } catch (Exception e) {
            System.out.println("playerJoin ERROR:" + e);
        }
    }


    private void movePlayers(JSONArray data) {
        try {

            for (int i = 0; i < data.length(); i++) {
                String id = data.getJSONObject(i).getString("id");
                String name = data.getJSONObject(i).getString("name");
                float x = (float) data.getJSONObject(i).getDouble("x");
                float y = (float) data.getJSONObject(i).getDouble("y");
                float r = (float) data.getJSONObject(i).getDouble("r");
                float radius = (float) data.getJSONObject(i).getDouble("radius");
                int level = (int) data.getJSONObject(i).getInt("radius");
                int life = (int) data.getJSONObject(i).getInt("life");
                int score = (int) data.getJSONObject(i).getInt("score");
                float angle = (float) Math.toDegrees(r);
                float speed = 1f;
                Player temp = players.get(id);
                if (temp == null) {
                    temp = new Player(playerLevels.get(level), game.font);
                    temp.setId(id);
                    temp.setWidth(radius * 2);
                    temp.setHeight(radius * 2);
                    temp.setScale(2f);
                    temp.setPosition(x, y);
                    temp.setCenterOrigin();
                    temp.setScore(score);
                    temp.setLife(life);
                    temp.setName(name);
                    players.put(id, temp);
                    stageWorld.addActor(temp);
                } else {
                    temp.setTexture(playerLevels.get(level));
                    temp.setScore(score);
                    temp.setLife(life);
                }

                temp.animateMovement(x, y, angle, speed, pool);
            }

        } catch (Exception e) {
            System.out.println("movePlayers ERROR:" + e);
        }
    }


    private void moveBullets(JSONArray data) {
        try {
            if (data.length() > 0) {
                for (int i = 0; i < data.length(); i++) {
                    String id = data.getJSONObject(i).getString("id");
                    float x = (float) data.getJSONObject(i).getDouble("x");
                    float y = (float) data.getJSONObject(i).getDouble("y");
                    int life = (int) data.getJSONObject(i).getInt("life");
                    float angle = (float) Math.toDegrees(data.getJSONObject(i).getDouble("angle"));

                    float speed = 0.1f;
                    Bullet tempBullet = bullets.get(id);
                    System.out.println("id:" + id + " rotation:" + angle);
                    if (tempBullet == null) {
                        tempBullet = new Bullet(bulletTexture);
                        tempBullet.setId(id);
                        tempBullet.setLife(life);
                        tempBullet.setWidth(20f);
                        tempBullet.setHeight(20f);
                        tempBullet.setScale(2f);
                        tempBullet.setPosition(x, y);
                        bullets.put(id, tempBullet);
                        stageWorld.addActor(tempBullet);
                    } else {
                        tempBullet.setLife(life);
                        tempBullet.setPosition(x, y);
                    }
                    tempBullet.animateMovement(x, y, angle, speed, pool);
                }
            }
        } catch (Exception e) {
            System.out.println("movePlayers ERROR:" + e);
        }
    }

    private void onDeadPlayers(Object... args) {
        JSONArray dataPlayers = (JSONArray) args[0];
        JSONArray dataBullets = (JSONArray) args[1];
        removePlayers(dataPlayers);
        removeBullets(dataBullets);
    }

    private void removePlayers(JSONArray data) {
        try {
            for (int i = 0; i < data.length(); i++) {
                String id = data.getJSONObject(i).getString("id");
                Player tempPlayer = players.get(id);
                if (tempPlayer == null) {

                } else {
                    players.remove(id);
                    tempPlayer.remove();
                }
            }
        } catch (Exception e) {
            System.out.println("removePlayers ERROR:" + e);
        }
    }

    private void removeBullets(JSONArray data) {
        try {
            for (int i = 0; i < data.length(); i++) {
                String id = data.getJSONObject(i).getString("id");
                Bullet tempBullet = bullets.get(id);
                if (tempBullet == null) {

                } else {
                    bullets.remove(id);
                    tempBullet.remove();
                }
            }
        } catch (Exception e) {
            System.out.println("removeBullets ERROR:" + e);
        }
    }

    private void onPlayerKick(Object... args) {
        try {
            game.showMenu();
        } catch (Exception e) {
            System.out.println("[ERR] onPlayerKick:" + e);
        }
    }


    private void onPlayerDisconnect(Object... args) {
        JSONObject data = (JSONObject) args[0];
        try {
            String id = data.getString("id");
            players.remove(id);
            System.out.println("playerDisconnect OK: " + id);
        } catch (JSONException e) {
            System.out.println("playerDisconnect ERROR: " + e);
        }
    }

    // endregion

    //region init

    private void initCamera() {
        Rectangle viewField = new Rectangle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 2000, 2000);
        camera = new FollowCamera(viewField);
        camera.setToOrtho(false);
        camera.viewportWidth = Gdx.graphics.getWidth() * 1f;
        camera.viewportHeight = Gdx.graphics.getHeight() * 1f;
    }

    private void initUiScene() {
        Skin skin = new Skin();
        skin.add("touchBackground", new Texture("touchBackground.png"));
        skin.add("touchKnob", new Texture("touchKnob.png"));
        Touchpad.TouchpadStyle style = new Touchpad.TouchpadStyle();
        Drawable touchBackground = skin.getDrawable("touchBackground");
        Drawable touchKnob = skin.getDrawable("touchKnob");
        style.background = touchBackground;
        style.knob = touchKnob;
        leftJoystick = new Touchpad(0, style);
        leftJoystick.setBounds(25, 25, width / 4, width / 4);


        TouchpadFire.TouchpadStyle styleFire = new TouchpadFire.TouchpadStyle();
        styleFire.background = touchBackground;
        styleFire.knob = touchKnob;
        rightJoystick = new TouchpadFire(0, styleFire);

        rightJoystick.setBounds((Gdx.graphics.getWidth() - (width / 4) - 25), 25, (width / 4), (width / 4));
        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageUI = new Stage(viewport, game.batch);
        stageUI.addActor(leftJoystick);
        stageUI.addActor(rightJoystick);
        Gdx.input.setInputProcessor(stageUI);

    }

    private void initWorldScene() {
        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageWorld = new Stage(viewport, game.batch);
        TiledMap tiledMap = new TmxMapLoader().load("map1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        pool = new Pool<MoveToAction>() {
            protected MoveToAction newObject() {
                return new MoveToAction();
            }
        };
    }

    private void initHardBeat() {
        this.timer = new Timer();
        Timer.Task hardBeat = new Timer.Task() {
            @Override
            public void run() {
                SocketApi.getInstance().getSocket().emit(Constants.HARD_BEAT, iam.getTargetGson());
            }
        };
        this.timer.scheduleTask(hardBeat, 0, Constants.HARDBEAT_INTERAVAL);
    }

    public void initNetwork() {
        SocketApi.getInstance().initConnection(myPlayerConfig);
        SocketApi.getInstance().getSocket()
                .on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onConnect(args);
                    }
                })
                .on(Constants.GAME_SETUP, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onGameSetup(args);
                    }
                })
                .on(Constants.PLAYER_JOIN, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onPLayerJoin(args);
                    }
                })
                .on(Constants.SERVER_TICK, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onServerTick(args);
                    }
                })
                .on(Constants.PLAYER_KICK, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onPlayerKick(args);
                    }
                })
                .on(Constants.SERVER_DEAD_PLAYERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onDeadPlayers(args);
                    }
                })
                .on(Constants.PLAYER_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onPlayerDisconnect(args);
                    }
                })
                .on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        onDisconnect(args);
                    }
                });
        SocketApi.getInstance().connect();
    }

    private void initDebug() {
        stageWorld.setDebugAll(true);
        stageWorld.setDebugInvisible(true);
        stageWorld.getDebugColor().r = 1;
        stageWorld.getDebugColor().b = 0;
        stageWorld.getDebugColor().g = 0;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown:" + keycode);
        if(keycode == Input.Keys.BACK){
            System.out.println("keyDown");
        }
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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

    //endregion

}

