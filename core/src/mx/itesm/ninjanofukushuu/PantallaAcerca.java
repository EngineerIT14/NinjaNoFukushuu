package mx.itesm.ninjanofukushuu;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Atem on 12/02/2016.
 */
public class PantallaAcerca {
    private OrthographicCamera camara;
    private Viewport vista;
    private SpriteBatch batch;
    private final Principal principal;

    public PantallaAcerca(Principal principal) {
        this.principal = principal;
    }
    @Override
    public void show() {
        camara=new OrthographicCamera(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO/2,Principal.ALTO_MUNDO/2,0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO,Principal.ALTO_MUNDO,camara);
        cargarTexturas();
        fondo=new Fondo(texturaFondo);
        //
    }
}
