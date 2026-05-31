package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.EditalMongoDao;
import models.EditalDeMonitoria;
import redis.clients.jedis.Jedis;

import java.util.List;

public class EditalService {

    private final EditalMongoDao editalMongoDao;
    private final Jedis jedis;
    private final ObjectMapper objectMapper;

    public EditalService(EditalMongoDao editalMongoDao) {
        this.editalMongoDao = editalMongoDao;
        this.jedis = new Jedis("localhost", 6379);
        this.objectMapper = new ObjectMapper();
    }

    public void salvar(EditalDeMonitoria edital) {
        // Salva a versão mais recente no MongoDB
        editalMongoDao.salvar(edital);

        // Apaga o edital antigo do Cache!
        // Isso obriga o sistema a buscar a versão nova no MongoDB na próxima vez.
        String chave = "edital:" + edital.getId();
        jedis.del(chave);
    }

    public EditalDeMonitoria recuperarEditalPeloId(long id) {
        String chave = "edital:" + id;

        // Tenta pegar do Redis
        try {
            String cache = jedis.get(chave);
            if (cache != null) {
                System.out.println("EDITAL VEIO DO CACHE REDIS!");
                return objectMapper.readValue(cache, EditalDeMonitoria.class);
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler do Redis: " + e.getMessage());
        }

        // Não achou no cache? Busca no MongoDB (Banco Principal)
        System.out.println("EDITAL VEIO DO MONGODB!");
        EditalDeMonitoria edital = editalMongoDao.recuperarEditalPeloId(id);

        // Salva no cache por 1 hora para as próximas buscas
        if (edital != null) {
            try {
                jedis.setex(chave, 3600, objectMapper.writeValueAsString(edital));
            } catch (JsonProcessingException e) {
                System.out.println("Erro ao converter Edital para JSON no Redis: " + e.getMessage());
            }
        }

        return edital;
    }

    public List<EditalDeMonitoria> listarTodos() {
        // Listagens completas vão direto no Mongo para garantir que novos editais apareçam
        return editalMongoDao.listarTodos();
    }
}