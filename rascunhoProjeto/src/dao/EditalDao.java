package dao;

import models.EditalDeMonitoria;
import javax.persistence.EntityManager;

public class EditalDao extends GenericDao<EditalDeMonitoria> {

    // No construtor, eu recebo a conexão (EntityManager) e já repasso para o (GenericDao)
    // avisando que esta classe vai cuidar exclusivamente da entidade EditalDeMonitoria.
    public EditalDao(EntityManager em) {
        super(em, EditalDeMonitoria.class);
    }

    // Tirei a responsabilidade de buscar pelo ID lá da Central de Informações e trouxe pro DAO.
    // Usei o metodo find() nativo do JPA porque ele é mais otimizado do que escrever um SELECT manual na mão.

    public EditalDeMonitoria recuperarEditalPeloId(long id) {
        return getEm().find(EditalDeMonitoria.class, id);
    }

}