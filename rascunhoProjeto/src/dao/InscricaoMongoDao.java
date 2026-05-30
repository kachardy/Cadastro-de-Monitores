package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.Inscricao;
import org.bson.Document;
import services.MongoConnection;

import static com.mongodb.client.model.Filters.eq;

public class InscricaoMongoDao {

    private final MongoCollection<Document> collection;

    public InscricaoMongoDao() {

        MongoDatabase db =
                MongoConnection.getDatabase();

        collection =
                db.getCollection("inscricoes");
    }

    public void salvar(Inscricao inscricao) {

        Document doc =
                new Document()

                        .append(
                                "disciplina",
                                inscricao.getDisciplina().getNome()
                        )

                        //.append(
                        //        "edital",
                        //        inscricao.getEdital().getTitulo()
                        //)

                        .append(
                                "candidato",
                                inscricao.getCandidato().getNome()
                        )

                        .append(
                                "cre",
                                inscricao.getCre()
                        )

                        .append(
                                "media",
                                inscricao.getMedia()
                        );

        collection.insertOne(doc);
    }

    public Inscricao buscarPorCandidato(
            String nome
    ) {

        Document doc =
                collection.find(
                        eq(
                                "candidato",
                                nome
                        )
                ).first();

        if(doc == null)
            return null;

        DisciplinaMongoDao disciplinaDao =
                new DisciplinaMongoDao();

        EditalMongoDao editalDao =
                new EditalMongoDao();

        Inscricao i =
                new Inscricao();

        i.setCre(
                doc.getDouble("cre")
        );

        i.setMedia(
                doc.getDouble("media")
        );;

        i.setDisciplina(
                disciplinaDao.buscarPorNome(
                        doc.getString(
                                "disciplina"
                        )
                )
        );

        i.setEdital(
                editalDao.buscarPorNumero(
                        doc.getString(
                                "edital"
                        )
                )
        );

        return i;
    }

}