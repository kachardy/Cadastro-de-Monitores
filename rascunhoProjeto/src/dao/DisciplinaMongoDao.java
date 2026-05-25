package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.Disciplina;
import org.bson.Document;
import services.MongoConnection;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class DisciplinaMongoDao {

    private final MongoCollection<Document> collection;

    // CACHE ASIDE
    private static final Map<String, Disciplina> cache = new HashMap<>();

    public DisciplinaMongoDao() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("disciplinas");
    }

    public void salvar(Disciplina disciplina) {

        Document doc = new Document()
                .append("nome", disciplina.getNome())
                .append("vagasRemuneradas", disciplina.getVagasRemuneradas())
                .append("vagasVoluntarias", disciplina.getVagasVoluntarias());

        collection.insertOne(doc);

        // atualiza cache
        cache.put(disciplina.getNome(), disciplina);
    }

    public Disciplina buscarPorNome(String nome) {

        // CACHE ASIDE
        if(cache.containsKey(nome)) {
            System.out.println("DISCIPLINA VEIO DO CACHE");
            return cache.get(nome);
        }

        System.out.println("DISCIPLINA VEIO DO MONGODB");

        Document doc = collection.find(eq("nome", nome)).first();

        if(doc == null) {
            return null;
        }

        Disciplina disciplina = new Disciplina(
                doc.getString("nome"),
                doc.getInteger("vagasRemuneradas"),
                doc.getInteger("vagasVoluntarias")
        );

        cache.put(nome, disciplina);

        return disciplina;
    }
}