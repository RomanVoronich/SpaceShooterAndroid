package com.neolabs.screen;

import com.neolabs.main.MainGame;
import com.neolabs.ui.HorizontalSlidingPane;
import com.neolabs.ui.ShopItem;
import com.neolabs.ui.onClickListener;
import com.neolabs.utils.IAndroidActivity;
import com.neolabs.utils.IPurchaseFinished;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ShopScreen implements Screen, onClickListener {

    private final MainGame game;
    private Stage stage;
    private SpriteBatch batch;
    private Preferences prefs = Gdx.app.getPreferences("PlayerSettings");
    HorizontalSlidingPane slidingMenuPure;

    private Texture bg, naviPassive, naviActive;

    private int LINE_MENU_ITEM_COUNT = 4;
    private IAndroidActivity activity;

    public ShopScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        Viewport viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, game.batch);

        // Инициализируем наш контейнер
        slidingMenuPure = new HorizontalSlidingPane(this);

        // Считаем ширину иконки уровня исходя из желаемого количества иконок по ширине экрана
        // Ширина = Ширина экрана / желаемое количество - (отступ слева + отступ справа)
        float itemWidth = Gdx.app.getGraphics().getWidth() / LINE_MENU_ITEM_COUNT - 40;

        // Создаем 4 секции с иконками выбора уровня
        // В каждой секции будет 2 строки иконок по 6 в каждой
        // Расставляем иконки по сетке с помощью виджета Table
        for (int section = 0; section < 4; section++) {
            Table table = new Table();
            for (int i = 0; i < 1; i++) {
                table.row();
                for (int j = 1; j < 6; j++) {
                    // (20,20,60,20) - отступы сверху, слева, снизу, справа
                    int skin_level = (section * 4 + j);
                    table.add(new ShopItem(game.font, skin_level, itemWidth, itemWidth)).pad(20, 20, 20, 20);
                }
                table.row();
            }
            // Добавляем секцию в наш контейнер
            slidingMenuPure.addWidget(table);
        }

        stage.addActor(slidingMenuPure);
        Gdx.input.setInputProcessor(stage);

        // Инициализируем необходимые текстуры
        bg = new Texture(Gdx.files.internal("bg.png"));
        naviPassive = new Texture(Gdx.files.internal("naviPassive.png"));
        naviActive = new Texture(Gdx.files.internal("naviActive.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.137f, 0.188f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            game.showMenu();
        }
        game.batch.begin();
        // Рисуем фон
        game.batch.draw(bg, 0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());

        // Рисуем указатель текущей секции
        for (int i = 1; i <= slidingMenuPure.getSectionsCount(); i++) {
            if (i == slidingMenuPure.calculateCurrentSection()) {
                game.batch.draw(naviActive, Gdx.app.getGraphics().getWidth() / 2 - slidingMenuPure.getSectionsCount() * 20 / 2 + i * 20, 50);
            } else {
                game.batch.draw(naviPassive, Gdx.app.getGraphics().getWidth() / 2 - slidingMenuPure.getSectionsCount() * 20 / 2 + i * 20, 50);
            }
        }

        game.batch.end();

        // Просчитываем и отрисовываем анимацию
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }

    @Override
    public void onCLick(int id) {
        // TODO sku
        System.out.println("onClick " + id);
        game.getActivity().purchase("skin1", new IPurchaseFinished() {

            @Override
            public void onIabPurchaseFinished(boolean result, String sku) {
                System.out.println("onIabPurchaseFinished" + result + " SKU" + sku);
            }
        });
    }
}
