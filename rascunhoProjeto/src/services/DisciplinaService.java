package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DisciplinaMongoDao;
import models.Disciplina;
import redis.clients.jedis.Jedis;

public class DisciplinaService {
    private final DisciplinaMongoDao disciplinaMongoDao;
    private final Jedis jedis = new Jedis("localhost", 6379);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DisciplinaService(DisciplinaMongoDao disciplinaMongoDao) {
        this.disciplinaMongoDao = disciplinaMongoDao;
    }

    public void salvar(Disciplina disciplina) {
        disciplinaMongoDao.salvar(disciplina);

        // Atualiza o cache após salvar
        try {
            String chave = "disciplina:" + disciplina.getId();
            jedis.setex(chave, 3600, objectMapper.writeValueAsString(disciplina));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Disciplina buscarPorNome(String nome) throws JsonProcessingException {
        String chave = "disciplina" + nome;

        String cache = jedis.get(chave);

        if (cache != null) {
            System.out.println("Disciplina veio do cache redis!");
            return objectMapper.readValue(cache, Disciplina.class);
        }

        System.out.println("Disciplina veio do MongoDB");
        Disciplina disciplina = disciplinaMongoDao.buscarPorNome(nome);

        if (disciplina != null) {
            // Guarda no cache e expira em 1 hora
            jedis.setex(chave, 3600, objectMapper.writeValueAsString(disciplina));
        }

        return disciplina;
    }
}
