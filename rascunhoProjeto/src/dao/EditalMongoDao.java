package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import models.EditalDeMonitoria;
import org.bson.Document;
import services.MongoConnection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class EditalMongoDao {

    private final MongoCollection<Document> collection;

    public EditalMongoDao() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("editais");
    }

    public void salvar(EditalDeMonitoria edital) {

        Document doc = new Document()
                .append(
                        "id",
                        edital.getId()
                )
                .append("numeroEdital", edital.getNumeroEdital())
                .append("maxInscricoesPorAluno", edital.getMaxInscricoesPorAluno())
                .append("pesoCRE", edital.getPesoCRE())
                .append("pesoMedia", edital.getPesoMedia())
                .append("dataInicio", edital.getDataInicio().toString())
                .append("dataFim", edital.getDataFim().toString());

        collection.replaceOne(
                eq(
                        "id",
                        edital.getId()
                ),
                doc,
                new ReplaceOptions()
                        .upsert(true)
        );
    }

    public EditalDeMonitoria buscarPorNumero(String numero) {

        Document doc = collection.find(
                eq("numeroEdital", numero)
        ).first();

        if(doc == null) {
            return null;
        }

        EditalDeMonitoria edital = new EditalDeMonitoria();

        edital.setNumeroEdital(
                doc.getString("numeroEdital")
        );

        edital.setMaxInscricoesPorAluno(
                doc.getInteger("maxInscricoesPorAluno")
        );

        edital.setPesoCRE(
                doc.getDouble("pesoCRE")
        );

        edital.setPesoMedia(
                doc.getDouble("pesoMedia")
        );

        edital.setDataInicio(
                LocalDate.parse(doc.getString("dataInicio"))
        );

        edital.setDataFim(
                LocalDate.parse(doc.getString("dataFim"))
        );

        return edital;
    }

    public List<EditalDeMonitoria> listarTodos() {

        List<EditalDeMonitoria> editais =
                new ArrayList<>();

        for(Document doc : collection.find()) {

            EditalDeMonitoria edital =
                    new EditalDeMonitoria();

            edital.setId(
                    doc.getLong("id")
            );

            edital.setNumeroEdital(
                    doc.getString("numeroEdital")
            );

            edital.setDataInicio(
                    LocalDate.parse(
                            doc.getString("dataInicio")
                    )
            );

            edital.setDataFim(
                    LocalDate.parse(
                            doc.getString("dataFim")
                    )
            );

            edital.setMaxInscricoesPorAluno(
                    doc.getInteger(
                            "maxInscricoesPorAluno"
                    )
            );

            edital.setPesoCRE(
                    doc.getDouble(
                            "pesoCRE"
                    )
            );

            edital.setPesoMedia(
                    doc.getDouble(
                            "pesoMedia"
                    )
            );

            editais.add(edital);
        }

        return editais;
    }

    public EditalDeMonitoria recuperarEditalPeloId(
            long id
    ) {

        Document doc =
                collection.find(
                        eq("id", id)
                ).first();

        if(doc == null)
            return null;

        EditalDeMonitoria edital =
                new EditalDeMonitoria();

        edital.setId(
                doc.getLong("id")
        );

        edital.setNumeroEdital(
                doc.getString(
                        "numeroEdital"
                )
        );

        edital.setDataInicio(
                LocalDate.parse(
                        doc.getString(
                                "dataInicio"
                        )
                )
        );

        edital.setDataFim(
                LocalDate.parse(
                        doc.getString(
                                "dataFim"
                        )
                )
        );

        edital.setMaxInscricoesPorAluno(
                doc.getInteger(
                        "maxInscricoesPorAluno"
                )
        );

        edital.setPesoCRE(
                doc.getDouble(
                        "pesoCRE"
                )
        );

        edital.setPesoMedia(
                doc.getDouble(
                        "pesoMedia"
                )
        );

        return edital;
    }
}