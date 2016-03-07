package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
Desarrollador = Luis Fernando
Descripción: Esta clase es la encargada de representar la pantalla de "About", donde se muestran elementos para conocer acerca de los desarrolladores
Profesor: Roberto Martinez Román.
 */
public class PantallaAcerca implements Screen {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;
    /* //Abanico
     private Logotipo abanico;
     private Texture texturaAbanico;*/
    //Botones
    private Boton btnMia,btnNuri,btnIrvin,btnJavier, btnFer, btnRegresar;
    private Texture texturaBtnMia,texturaBtnNuri, texturaBtnIrvin,texturaBtnJavier, texturaBtnFer, texturaRegresar;
    private static final int anchoBoton = 180, altoBoton = 200; //anchoBoton1 = 480 , altoBoton1 = 160;
    //Presentaciones
    private Presentacion presentaciónIrvin, presentaciónMia, presentaciónJavier, presentaciónNuri, presentaciónFer;
    private  Texture texturaPresentacionIrvin, texturaPresentacionMia, texturaPresentacionJavier, texturaPresentacionNuri, texturaPresentacionFer;

    //Efectos y musica de fondo
    private Sound efectoClick;



    public PantallaAcerca(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void show() {
        //Crear camara
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2, 0);
        camara.update();

        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        cargarTexturas();
        //Crear fondo
        fondo = new Fondo(texturaFondo);
        // abanico=new Logotipo(texturaAbanico);
        //Crear botones
        btnMia = new Boton(texturaBtnMia);
        btnNuri = new  Boton(texturaBtnNuri);
        btnIrvin = new Boton(texturaBtnIrvin);
        btnJavier = new Boton(texturaBtnJavier);
        btnFer = new Boton(texturaBtnFer);
        btnRegresar = new Boton(texturaRegresar);

        //Crear presentaciones

        presentaciónIrvin = new Presentacion(texturaPresentacionIrvin);
        presentaciónMia = new Presentacion(texturaPresentacionMia);
        presentaciónJavier = new Presentacion(texturaPresentacionJavier);
        presentaciónNuri = new Presentacion(texturaPresentacionNuri);
        presentaciónFer = new Presentacion(texturaPresentacionFer);

        //Poscicionar objetos
        // abanico.setPosicion(0 - 20, 0 - 35);
        btnIrvin.setPosicion(Principal.ANCHO_MUNDO / 4 - 100, Principal.ALTO_MUNDO * 2 / 3 - 100);
        btnMia.setPosicion(Principal.ANCHO_MUNDO / 2 - 100, Principal.ALTO_MUNDO * 2 / 3 - 100);
        btnJavier.setPosicion(Principal.ANCHO_MUNDO * 3 / 4 - 100, Principal.ALTO_MUNDO * 2 / 3 - 100);
        btnNuri.setPosicion(Principal.ANCHO_MUNDO * 3 / 8 - 100, Principal.ALTO_MUNDO / 3 - 100);
        btnFer.setPosicion(Principal.ANCHO_MUNDO * 5 / 8 - 100, Principal.ALTO_MUNDO / 3 - 100);
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO * 7 / 8 , Principal.ALTO_MUNDO * 1 / 5 -150);
        presentaciónIrvin.setPosicion(0 - 20, 0 - 100);
        presentaciónMia.setPosicion(0 - 20, 0 - 100);
        presentaciónJavier.setPosicion(0 - 20, 0 - 100);
        presentaciónNuri.setPosicion(0 - 20, 0 - 100);
        presentaciónFer.setPosicion(0 - 20, 0 - 100);
        //Ajuste de tamaño
        //abanico.setTamanio(1280, 780);
        btnIrvin.setTamanio(anchoBoton, altoBoton);
        btnMia.setTamanio(anchoBoton, altoBoton);
        btnJavier.setTamanio(anchoBoton,altoBoton);
        btnNuri.setTamanio(anchoBoton,altoBoton);
        btnFer.setTamanio(anchoBoton,altoBoton);
        btnRegresar.setTamanio(anchoBoton-50,altoBoton-50);

        fondo.getSprite().setCenter(Principal.ANCHO_MUNDO / 2, Principal.ALTO_MUNDO / 2);
        fondo.getSprite().setOrigin(1500 / 2, 1500 / 2);
        presentaciónIrvin.setTamanio(1280,880);
        presentaciónMia.setTamanio(1280,880);
        presentaciónJavier.setTamanio(1280,880);
        presentaciónNuri.setTamanio(1280,880);
        presentaciónFer.setTamanio(1280, 880);

        //Batch
        batch = new SpriteBatch();
        cargarAudio();
    }

    //Metodo para cargar los efectos de sonido y la música de fondo
    private void cargarAudio() {
        efectoClick = Gdx.audio.newSound(Gdx.files.internal("sonidoVentana.wav"));

    }

    //Crgar texturas
    private void cargarTexturas() {
        //Fondo
        texturaFondo = new Texture(Gdx.files.internal("Fondo.jpg"));
        //Abanico
        //texturaAbanico = new Texture(Gdx.files.internal("Abanico.png"));
        //Botones
        texturaBtnMia = new Texture((Gdx.files.internal("M.png")));
        texturaBtnNuri = new Texture((Gdx.files.internal("N.png")));
        texturaBtnIrvin = new Texture((Gdx.files.internal("I.png")));
        texturaBtnJavier = new Texture((Gdx.files.internal("J.png")));
        texturaBtnFer = new Texture((Gdx.files.internal("F.png")));
        texturaRegresar = new Texture(Gdx.files.internal("return.png"));
        //Presentaciones
        texturaPresentacionIrvin = new Texture(Gdx.files.internal("Irvi.png"));
        texturaPresentacionMia = new Texture(Gdx.files.internal("Mare.png"));
        texturaPresentacionJavier = new Texture(Gdx.files.internal("Javo.png"));
        texturaPresentacionNuri = new Texture(Gdx.files.internal("Nuria.png"));
        texturaPresentacionFer = new Texture(Gdx.files.internal("Fercho.png"));
    }

    @Override
    public void render(float delta) {
        //Borrar la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camara.combined);

        //Metodo pare revisar si huba touch
        leerEntrada();

        fondo.getSprite().rotate(.1f);
        //DIBUJAR
        batch.begin();
        fondo.render(batch);
        //abanico.render(batch);
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
                    if (presentaciónIrvin.getEstado()!= Presentacion.Estado.APARECIDO){ //Estado no debe ser Aparecido
                        presentaciónIrvin.aparecer();
                        efectoClick.play();
                    }
                    break;
                case 2: //Caso para aparecer la presentación de Mia
                    if(presentaciónMia.getEstado() != Presentacion.Estado.APARECIDO){
                        presentaciónMia.aparecer();
                        efectoClick.play();
                    }
                    break;
                case 3: //Caso para aparecer la presentación de Javier
                    if(presentaciónJavier.getEstado() != Presentacion.Estado.APARECIDO){
                        presentaciónJavier.aparecer();
                        efectoClick.play();
                    }
                    break;
                case 4: //Caso para aparecer la presentación de Nuri
                    if(presentaciónNuri.getEstado() != Presentacion.Estado.APARECIDO){
                        presentaciónNuri.aparecer();
                        efectoClick.play();
                    }
                    break;
                case 5: //Caso para aparecer la presentación de Fer
                    if(presentaciónFer.getEstado() != Presentacion.Estado.APARECIDO){
                        presentaciónFer.aparecer();
                        efectoClick.play();
                    }
                    break;
                case 6: //Caso para volver al menú principal
                    principal.setScreen(new PantallaMenu(principal));
                    this.efectoClick.play();
                    break;
                case 7: //Caso para desaparecer la presentación de Irvin
                    presentaciónIrvin.desaparecer();
                    efectoClick.play();
                    break;
                case 8: //Caso para desaparecer la presentación de Mia
                    presentaciónMia.desaparecer();
                    efectoClick.play();
                    break;
                case 9: //Caso para desaparecer la presentación de Javier
                    presentaciónJavier.desaparecer();
                    efectoClick.play();
                    break;
                case 10: //Caso para desaparecer la presentación de Nuri
                    presentaciónNuri.desaparecer();
                    efectoClick.play();
                    break;
                case 11: //Caso para desaparecer la presentación de Fer
                    presentaciónFer.desaparecer();
                    efectoClick.play();
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
        //Verificar si se toco algun botón y no hay una prsentación mostrada
        if(presentaciónIrvin.getEstado() != Presentacion.Estado.APARECIDO && presentaciónMia.getEstado() != Presentacion.Estado.APARECIDO && presentaciónJavier.getEstado() != Presentacion.Estado.APARECIDO
                && presentaciónNuri.getEstado() != Presentacion.Estado.APARECIDO && presentaciónFer.getEstado() != Presentacion.Estado.APARECIDO){
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
        }

        //Para desaparecer las presentaciones se puede seleccionar cualquiere espacio e la pantalla
        else
        if(presentaciónIrvin.getEstado()== Presentacion.Estado.APARECIDO){
            if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                return 7;
            }
        }
        else
        if(presentaciónMia.getEstado() == Presentacion.Estado.APARECIDO){
            if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                return 8;
            }
        }
        else
        if(presentaciónJavier.getEstado() == Presentacion.Estado.APARECIDO){
            if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                return 9;
            }
        }
        else
        if(presentaciónNuri.getEstado() == Presentacion.Estado.APARECIDO){
            if(x>=0 && x<=Principal.ANCHO_MUNDO && y>=0 && y<=Principal.ALTO_MUNDO){
                return 10;
            }
        }
        else
        if(presentaciónFer.getEstado() == Presentacion.Estado.APARECIDO){
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
        principal.dispose();
        batch.dispose();

        //texturas
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
        //texturaAbanico.dispose();
        efectoClick.dispose();
    }


}