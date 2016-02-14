package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Atem on 12/02/2016.
 */
public class PantallaAcerca implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;
    //Botones
    private Boton btnMia,btnNuri,btnIrvin,btnJavier, btnFer, btnRegresar;
    private Texture texturaBtnMia,texturaBtnNuri, texturaBtnIrvin,texturaBtnJavier, texturaBtnFer, texturaRegresar, texturaIrv, texturaMar, texturaJav, texturaNur, texturaFer;
    private static final int anchoBoton = 180, altoBoton = 200, anchoBoton1 = 480 , altoBoton1 = 160;
    //Presentaciones
    private Presentación presentaciónIrvin, presentaciónMia, presentaciónJavier, presentaciónNuri, presentaciónFer;
    private  Texture texturaPresentacionIrvin, texturaPresentacionMia, texturaPresentacionJavier, texturaPresentacionNuri, texturaPresentacionFer;
    public PantallaAcerca(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void show() {
        camara=new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        cargarTexturas();
        fondo=new Fondo(texturaFondo);
        btnMia = new Boton(texturaBtnMia);
        btnNuri = new  Boton(texturaBtnNuri);
        btnIrvin = new Boton(texturaBtnIrvin);
        btnJavier = new Boton(texturaBtnJavier);
        btnFer = new Boton(texturaBtnFer);
        btnRegresar = new Boton(texturaRegresar);
        presentaciónIrvin = new Presentación(texturaPresentacionIrvin);
        presentaciónMia = new Presentación(texturaPresentacionMia);
        presentaciónJavier = new Presentación(texturaPresentacionJavier);
        presentaciónNuri = new Presentación(texturaPresentacionNuri);
        presentaciónFer = new Presentación(texturaPresentacionFer);
        //Poscicionar objetos
        btnIrvin.setPosicion(Principal.ANCHO_MUNDO/4,Principal.ALTO_MUNDO/3);
        btnMia.setPosicion(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/3);
        btnJavier.setPosicion(Principal.ANCHO_MUNDO*3/2,Principal.ALTO_MUNDO/3);
        btnNuri.setPosicion(Principal.ANCHO_MUNDO*3/8,Principal.ALTO_MUNDO*2/3);
        btnFer.setPosicion(Principal.ANCHO_MUNDO*5/2,Principal.ALTO_MUNDO*2/3);
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO*7/8,Principal.ALTO_MUNDO*4/5);
        presentaciónIrvin.setPosicion(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/2);
        presentaciónMia.setPosicion(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/2);
        presentaciónJavier.setPosicion(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/2);
        presentaciónNuri.setPosicion(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/2);
        presentaciónFer.setPosicion(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        //
        btnIrvin.setTamanio(anchoBoton, altoBoton);
        btnMia.setTamanio(anchoBoton, altoBoton);
        btnJavier.setTamanio(anchoBoton,altoBoton);
        btnNuri.setTamanio(anchoBoton,altoBoton);
        btnFer.setTamanio(anchoBoton,altoBoton);
        btnRegresar.setTamanio(anchoBoton1,altoBoton1);
        batch=new SpriteBatch();
    }

    private void cargarTexturas() {
        texturaFondo = new Texture(Gdx.files.internal("Abanico.png"));
        texturaBtnMia = new Texture((Gdx.files.internal("")));
        texturaBtnNuri = new Texture((Gdx.files.internal("")));
        texturaBtnIrvin = new Texture((Gdx.files.internal("")));
        texturaBtnJavier = new Texture((Gdx.files.internal("J.png")));
        texturaBtnFer = new Texture((Gdx.files.internal("F.png")));
        texturaRegresar = new Texture(Gdx.files.internal(""));
        texturaPresentacionIrvin = new Texture(Gdx.files.internal("Irv.png"));
        texturaPresentacionMia = new Texture(Gdx.files.internal("Mar.png"));
        texturaPresentacionJavier = new Texture(Gdx.files.internal("Jav.png"));
        texturaPresentacionNuri = new Texture(Gdx.files.internal("Nur.png"));
        texturaPresentacionFer = new Texture(Gdx.files.internal("Fer.png"));
    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camara.combined);
        leerEntrada();
        //DIBUJAR, primero las cosas que van atras....
        batch.begin();
        fondo.render(batch);
        btnIrvin.render(batch);
        btnMia.render(batch);
        btnJavier.render(batch);
        btnNuri.render(batch);
        btnFer.render(batch);
        btnRegresar.render(batch);
        if(presentaciónIrvin.actualizar()){
            presentaciónIrvin.render(batch);
        }
        if(presentaciónMia.actualizar()){
            presentaciónMia.render(batch);
        }
        if(presentaciónJavier.actualizar()){
            presentaciónJavier.render(batch);
        }
        if(presentaciónNuri.actualizar()){
            presentaciónNuri.render(batch);
        }
        if(presentaciónFer.actualizar()){
            presentaciónFer.render(batch);
        }
        batch.end();
    }

    private void leerEntrada() {
        if (Gdx.input.justTouched()){
            Vector3 coordernadas = new Vector3();
            coordernadas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordernadas);
            float x = coordernadas.x;
            float y = coordernadas.y;
            switch ( verifcarBoton(x,y)){
                case 1:
                    if (presentaciónIrvin.getEstado()!=Presentación.Estado.APARECIDO){
                        presentaciónIrvin.aparecer();
                    }
                    break;
                case 2:
                    if(presentaciónMia.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónMia.aparecer();
                    }
                    break;
                case 3:
                    if(presentaciónJavier.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónJavier.aparecer();
                    }
                    break;
                case 4:
                    if(presentaciónNuri.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónNuri.aparecer();
                    }
                    break;
                case 5:
                    if(presentaciónFer.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónFer.aparecer();
                    }
                    break;
                case 6:
                    principal.setScreen(new PantallaMenu(principal));
                    break;
                case 7:
                    presentaciónIrvin.desaparecer();
                    break;
                case 8:
                    presentaciónMia.desaparecer();
                    break;
                case 9:
                    presentaciónJavier.desaparecer();
                    break;
                case 10:
                    presentaciónNuri.desaparecer();
                    break;
                case 11:
                    presentaciónFer.desaparecer();
                    break;
                default:
                    break;
            }
        }
    }

    private int verifcarBoton(float x, float y) {
        Sprite spriteBtnIrvin = btnIrvin.getSprite();
        Sprite spriteBtnMia = btnMia.getSprite();
        Sprite spriteBtnJavier = btnJavier.getSprite();
        Sprite spriteBtnNuri = btnNuri.getSprite();
        Sprite spriteBtnFer = btnFer.getSprite();
        Sprite spriteBtnRegresar = btnRegresar.getSprite();
        //Verificar
        if(x>=spriteBtnIrvin.getX() && x<=spriteBtnIrvin.getX()+spriteBtnIrvin.getWidth() &&
                y>=spriteBtnIrvin.getY() && y<=spriteBtnIrvin.getY()+spriteBtnIrvin.getHeight()){
            return 1;
        }
        else
        if (x>=spriteBtnMia.getX() && x<=spriteBtnMia.getX()+spriteBtnMia.getWidth() &&
                y>=spriteBtnMia.getY() && y<=spriteBtnMia.getY()+spriteBtnMia.getHeight()){
            return 2;
        }
        else
        if(x>=spriteBtnJavier.getX() && x<=spriteBtnJavier.getX()+spriteBtnJavier.getWidth() &&
                y>=spriteBtnJavier.getY() && y<=spriteBtnJavier.getY()+spriteBtnJavier.getHeight()){
            return 3;
        }
        else
        if(x>=spriteBtnNuri.getX() && x<=spriteBtnNuri.getX()+spriteBtnNuri.getWidth() &&
                y>=spriteBtnNuri.getY() && y<=spriteBtnNuri.getY()+spriteBtnNuri.getHeight()){
            return 4;
        }
        else
        if(x>=spriteBtnFer.getX() && x<=spriteBtnFer.getX()+spriteBtnFer.getWidth() &&
                y>=spriteBtnFer.getY() && y<=spriteBtnFer.getY()+spriteBtnFer.getHeight()){
            return 5;
        }
        else
        if (x>=spriteBtnRegresar.getX() && x<=spriteBtnRegresar.getX()+spriteBtnRegresar.getWidth() &&
                y>=spriteBtnRegresar.getY() && y<=spriteBtnRegresar.getY()+spriteBtnRegresar.getHeight()){
            return 6;
        }
        else
            if(presentaciónIrvin.getEstado()== Presentación.Estado.APARECIDO){
                if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                    return 7;
                }
            }
        else
            if(presentaciónMia.getEstado() == Presentación.Estado.APARECIDO){
                if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                    return 8;
                }
            }
        else
            if(presentaciónJavier.getEstado() == Presentación.Estado.APARECIDO){
                if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                    return 9;
                }
            }
        else
            if(presentaciónNuri.getEstado() == Presentación.Estado.APARECIDO){
                    if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                        return 10;
                    }
                }
        else
             if(presentaciónFer.getEstado() == Presentación.Estado.APARECIDO){
                 if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                     return 11;
                 }
             }
            return 0;
    }
    @Override
    public void resize(int width, int height) {
        vista.update(width,height);

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
        texturaIrv.dispose();
        texturaMar.dispose();
        texturaJav.dispose();
        texturaNur.dispose();
        texturaFer.dispose();
        texturaBtnIrvin.dispose();
        texturaBtnMia.dispose();
        texturaBtnJavier.dispose();
        texturaBtnNuri.dispose();
        texturaBtnFer.dispose();
        texturaPresentacionIrvin.dispose();
        texturaPresentacionMia.dispose();
        texturaPresentacionJavier.dispose();
        texturaPresentacionNuri.dispose();
        texturaPresentacionFer.dispose();
        texturaRegresar.dispose();
        texturaFondo.dispose();
    }


}
