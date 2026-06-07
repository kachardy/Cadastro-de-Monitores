package services;

import dao.GenericDao;
import models.Coordenador;

import java.util.List;

public class CoordenadorService {
    private GenericDao<Coordenador> coordenadorDao;

    public CoordenadorService(GenericDao<Coordenador> coordenadorDao) {
        this.coordenadorDao = coordenadorDao;
    }

    public Coordenador getCoordenador() {
        List<Coordenador> lista = coordenadorDao.listarTodos();
        return lista.isEmpty() ? null : lista.get(0);
    }

    public boolean adicionarCoordenador(Coordenador c) {
        coordenadorDao.salvar(c);
        return true;
    }
}
