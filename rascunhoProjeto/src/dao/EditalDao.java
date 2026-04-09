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

    // Adicionei esta busca customizada usando JPQL caso precise localizar um edital
    // diretamente pelo seu número de identificação (ex: "Edital 01/2025") nas telas de listagem.
    public EditalDeMonitoria buscarEditalPorNumero(String numeroEdital) {
        try {
            return getEm().createQuery("SELECT e FROM EditalDeMonitoria e WHERE e.numeroEdital = :numero", EditalDeMonitoria.class)
                    .setParameter("numero", numeroEdital)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            // Se a query não encontrar nenhum edital com esse número, o JPA lança uma exceção.
            // Eu capturo ela aqui e devolvo null pra evitar que o programa quebre na tela do usuário.
            return null;
        }
    }
}