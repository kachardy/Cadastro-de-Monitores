import controllers.AuthController;
import services.CentralDeInformacoes;

public class Programa {

    public static void main(String[] args) {
        CentralDeInformacoes central = new CentralDeInformacoes();

        // O AuthController só precisa da central (facade) para iniciar o programa
        AuthController auth = new AuthController(central);
        auth.iniciar();
    }
}