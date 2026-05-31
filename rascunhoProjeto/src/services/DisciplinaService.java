package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.EditalMongoDao;
import models.Disciplina;
import redis.clients.jedis.Jedis;

public class DisciplinaService {
    // Agora ele depende do DAO do Edital, pois a disciplina mora lá dentro
    private final EditalMongoDao editalMongoDao;
    private final Jedis jedis = new Jedis("localhost", 6379);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DisciplinaService(EditalMongoDao editalMongoDao) {
        this.editalMongoDao = editalMongoDao;
    }

    public Disciplina buscarPorNome(String nome) throws JsonProcessingException {
        String chave = "disciplina:" + nome;

        String cache = jedis.get(chave);

        if (cache != null) {
            System.out.println("Disciplina veio do cache redis!");
            return objectMapper.readValue(cache, Disciplina.class);
        }

        System.out.println("Disciplina veio do MongoDB");
        // Usa o novo método do EditalMongoDao para pescar a disciplina
        Disciplina disciplina = editalMongoDao.buscarDisciplinaEmbutidaPorNome(nome);

        if (disciplina != null) {
            // Guarda no cache e expira em 1 hora
            jedis.setex(chave, 3600, objectMapper.writeValueAsString(disciplina));
        }

        return disciplina;
    }
}