package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.EditalDeMonitoria;
import org.bson.Document;
import services.MongoConnection;

import java.time.LocalDate;

import static com.mongodb.client.model.Filters.eq;

public class EditalMongoDao {

    private final MongoCollection<Document> collection;

    public EditalMongoDao() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("editais");
    }

    public void salvar(EditalDeMonitoria edital) {

        Document doc = new Document()
                .append("titulo", edital.getTitulo())
                .append("descricao", edital.getDescricao())
                .append("dataInicio", edital.getDataInicio().toString())
                .append("dataFim", edital.getDataFim().toString());

        collection.insertOne(doc);
    }

    public EditalDeMonitoria buscarPorTitulo(String titulo) {

        Document doc = collection.find(eq("titulo", titulo)).first();

        if(doc == null) {
            return null;
        }

        EditalDeMonitoria edital = new EditalDeMonitoria();

        edital.setTitulo(doc.getString("titulo"));
        edital.setDescricao(doc.getString("descricao"));

        edital.setDataInicio(
                LocalDate.parse(doc.getString("dataInicio"))
        );

        edital.setDataFim(
                LocalDate.parse(doc.getString("dataFim"))
        );

        return edital;
    }
}