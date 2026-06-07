package services;

import dao.EditalDao;
import erros.EditalJaExisteException;
import models.EditalDeMonitoria;

import java.util.List;

public class EditalService {
    private EditalDao editalDao;

    public EditalService(EditalDao editalDao) {
        this.editalDao = editalDao;
    }

    public List<EditalDeMonitoria> getTodosOsEditais() {
        return editalDao.listarTodos();
    }

    public EditalDeMonitoria recuperarEditalPeloId(long id) {
        return editalDao.recuperarEditalPeloId(id);
    }

    public void adicionarEdital(EditalDeMonitoria edital) throws EditalJaExisteException {
        if (recuperarEditalPeloId(edital.getId()) != null) {
            throw new EditalJaExisteException();
        }
        editalDao.salvar(edital);
    }

    public void salvarEdital(EditalDeMonitoria edital) {
        editalDao.salvar(edital);
    }
}
