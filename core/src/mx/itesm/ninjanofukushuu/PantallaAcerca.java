package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Atem on 12/02/2016.
 */
public class PantallaAcerca {

    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    //Fondo
    private Fondo fondo;
    private Texture texturaFondo;
    //Botones
    private Boton btnMia,btnNuri,btnIrvin,btnJavier, btnFer, btnRegresar;
    private Texture texturaBtnMia,texturaBtnNuri, texturaBtnIrvin,texturaBtnJavier, texturaBtnFer, texturaRegresar;



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
        //
        btnIrvin.setPosicion(Principal.ANCHO_MUNDO/4,Principal.ALTO_MUNDO/3);
        btnMia.setPosicion(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/3);
        btnJavier.setPosicion(Principal.ANCHO_MUNDO*3/2,Principal.ALTO_MUNDO/3);
        btnNuri.setPosicion(Principal.ANCHO_MUNDO*3/8,Principal.ALTO_MUNDO*2/3);
        btnFer.setPosicion(Principal.ANCHO_MUNDO*5/2,Principal.ALTO_MUNDO*2/3);
        btnRegresar.setPosicion(Principal.ANCHO_MUNDO*7/8,Principal.ALTO_MUNDO*4/5);

    }

    private void cargarTexturas() {
        texturaFondo = new Texture(Gdx.files.internal(""));
        texturaBtnMia = new Texture((Gdx.files.internal("")));
        texturaBtnNuri = new Texture((Gdx.files.internal("")));
        texturaBtnIrvin = new Texture((Gdx.files.internal("")));
        texturaBtnJavier = new Texture((Gdx.files.internal("")));
        texturaBtnFer = new Texture((Gdx.files.internal("")));
        texturaRegresar = new Texture(Gdx.files.internal(""));
    }
}
