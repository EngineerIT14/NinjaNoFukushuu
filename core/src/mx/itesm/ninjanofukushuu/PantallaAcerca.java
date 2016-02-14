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
        //Crear camara
        camara=new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        cargarTexturas();
        //Crear fondo
        fondo=new Fondo(texturaFondo);
        //Crear botones
        btnMia = new Boton(texturaBtnMia);
        btnNuri = new  Boton(texturaBtnNuri);
        btnIrvin = new Boton(texturaBtnIrvin);
        btnJavier = new Boton(texturaBtnJavier);
        btnFer = new Boton(texturaBtnFer);
        btnRegresar = new Boton(texturaRegresar);
        //Crear presentaciones
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
        //Ajuste de tamaño
        btnIrvin.setTamanio(anchoBoton, altoBoton);
        btnMia.setTamanio(anchoBoton, altoBoton);
        btnJavier.setTamanio(anchoBoton,altoBoton);
        btnNuri.setTamanio(anchoBoton,altoBoton);
        btnFer.setTamanio(anchoBoton,altoBoton);
        btnRegresar.setTamanio(anchoBoton1,altoBoton1);
        //Batch
        batch=new SpriteBatch();
    }

    //Crgar texturas
    private void cargarTexturas() {
        //Fondo
        texturaFondo = new Texture(Gdx.files.internal("Abanico.png"));
        //Botones
        texturaBtnMia = new Texture((Gdx.files.internal("M.png")));
        texturaBtnNuri = new Texture((Gdx.files.internal("N.png")));
        texturaBtnIrvin = new Texture((Gdx.files.internal("I.png")));
        texturaBtnJavier = new Texture((Gdx.files.internal("J.png")));
        texturaBtnFer = new Texture((Gdx.files.internal("F.png")));
        texturaRegresar = new Texture(Gdx.files.internal("return.png"));
        //Presentaciones
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
        //Metodo pare revisar si huba touch
        leerEntrada();
        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        btnIrvin.render(batch);
        btnMia.render(batch);
        btnJavier.render(batch);
        btnNuri.render(batch);
        btnFer.render(batch);
        btnRegresar.render(batch);
        //Se dibujaran las ´resentaciones si y solo si el estado es APERECIDO
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

    //Metodo para leer entradas
    private void leerEntrada() {
        //Hubo toque
        if (Gdx.input.justTouched()){
            Vector3 coordernadas = new Vector3();
            coordernadas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordernadas);
            float x = coordernadas.x;
            float y = coordernadas.y;
            switch ( verifcarBoton(x,y)){
                case 1: //Caso para aparecer la presentación de Irvin
                    if (presentaciónIrvin.getEstado()!=Presentación.Estado.APARECIDO){ //Estado no debe ser Aparecido
                        presentaciónIrvin.aparecer();
                    }
                    break;
                case 2: //Caso para aparecer la presentación de Mia
                    if(presentaciónMia.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónMia.aparecer();
                    }
                    break;
                case 3: //Caso para aparecer la presentación de Javier
                    if(presentaciónJavier.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónJavier.aparecer();
                    }
                    break;
                case 4: //Caso para aparecer la presentación de Nuri
                    if(presentaciónNuri.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónNuri.aparecer();
                    }
                    break;
                case 5: //Caso para aparecer la presentación de Fer
                    if(presentaciónFer.getEstado() != Presentación.Estado.APARECIDO){
                        presentaciónFer.aparecer();
                    }
                    break;
                case 6: //Caso para volver al menú principal
                    principal.setScreen(new PantallaMenu(principal));
                    break;
                case 7: //Caso para desaparecer la presentación de Irvin
                    presentaciónIrvin.desaparecer();
                    break;
                case 8: //Caso para desaparecer la presentación de Mia
                    presentaciónMia.desaparecer();
                    break;
                case 9: //Caso para desaparecer la presentación de Javier
                    presentaciónJavier.desaparecer();
                    break;
                case 10: //Caso para desaparecer la presentación de Nuri
                    presentaciónNuri.desaparecer();
                    break;
                case 11: //Caso para desaparecer la presentación de Fer
                    presentaciónFer.desaparecer();
                    break;
                default: //Caso para no hacer nada, bonus
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
        //Verificar si se toco algun botón
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
        //Para desaparecer las presentaciones se puede seleccionar cualquiere espacio e la pantalla
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
        //Eliminar basura
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
