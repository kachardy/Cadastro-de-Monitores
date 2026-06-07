package factories;

import models.Coordenador;
import services.AlunoService;
import services.CoordenadorService;
import services.EditalService;
import services.PessoaService;

public class ServiceFactory {

    // Guardamos as instâncias para garantir que criamos apenas um Singleton de cada serviço
    private static PessoaService pessoaService;
    private static AlunoService alunoService;
    private static EditalService editalService;
    private static CoordenadorService coordenadorService;

    public static PessoaService getPessoaService() {
        if (pessoaService == null) {
            pessoaService = new PessoaService(DaoFactory.getPessoaDAO());
        }
        return pessoaService;
    }

    public static AlunoService getAlunoService() {
        if (alunoService == null) {
            // A fábrica injeta a dependência do PessoaService automaticamente
            alunoService = new AlunoService(DaoFactory.getAlunoDAO(), getPessoaService());
        }
        return alunoService;
    }

    public static EditalService getEditalService() {
        if (editalService == null) {
            editalService = new EditalService(DaoFactory.getEditalDAO());
        }
        return editalService;
    }

    public static CoordenadorService getCoordenadorService() {
        if (coordenadorService == null) {
            coordenadorService = new CoordenadorService(DaoFactory.getDAO(Coordenador.class));
        }
        return coordenadorService;
    }
}