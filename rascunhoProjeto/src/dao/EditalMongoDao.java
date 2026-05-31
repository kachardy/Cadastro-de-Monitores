package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import models.Disciplina;
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

        List<Document> disciplinasDocs = new ArrayList<>();
        for (Disciplina d : edital.getTodasAsDisciplinas()) {
            Document docDisc = new Document()
                    .append("nome", d.getNome())
                    .append("vagasRemuneradas", d.getVagasRemuneradas())
                    .append("vagasVoluntarias", d.getVagasVoluntarias());
            disciplinasDocs.add(docDisc);
        }

        Document doc = new Document()
                .append("id", edital.getId())
                .append("numeroEdital", edital.getNumeroEdital())
                .append("maxInscricoesPorAluno", edital.getMaxInscricoesPorAluno())
                .append("pesoCRE", edital.getPesoCRE())
                .append("pesoMedia", edital.getPesoMedia())
                .append("dataInicio", edital.getDataInicio().toString())
                .append("dataFim", edital.getDataFim().toString())
                .append("disciplinas", disciplinasDocs);

        collection.replaceOne(eq("id", edital.getId()), doc, new ReplaceOptions().upsert(true));
    }

    public EditalDeMonitoria buscarPorNumero(String numero) {

        Document doc = collection.find(
                eq("numeroEdital", numero)
        ).first();

        if(doc == null) {
            return null;
        }

        EditalDeMonitoria edital = new EditalDeMonitoria();
        edital.setId(doc.getLong("id"));
        edital.setNumeroEdital(doc.getString("numeroEdital"));
        edital.setMaxInscricoesPorAluno(doc.getInteger("maxInscricoesPorAluno"));
        edital.setPesoCRE(doc.getDouble("pesoCRE"));
        edital.setPesoMedia(doc.getDouble("pesoMedia"));
        edital.setDataInicio(LocalDate.parse(doc.getString("dataInicio")));
        edital.setDataFim(LocalDate.parse(doc.getString("dataFim")));

        // RECUPERANDO A LISTA DE DISCIPLINAS EMBUTIDA
        List<Document> disciplinasDocs = doc.getList("disciplinas", Document.class);
        if (disciplinasDocs != null) {
            for (Document docDisc : disciplinasDocs) {
                Disciplina d = new Disciplina(
                        docDisc.getString("nome"),
                        docDisc.getInteger("vagasRemuneradas"),
                        docDisc.getInteger("vagasVoluntarias")
                );
                edital.adicionarDisciplina(d);
            }
        }

        return edital;
    }

    public List<EditalDeMonitoria> listarTodos() {

        List<EditalDeMonitoria> editais = new ArrayList<>();

        for(Document doc : collection.find()) {

            EditalDeMonitoria edital = new EditalDeMonitoria();
            edital.setId(doc.getLong("id"));
            edital.setNumeroEdital(doc.getString("numeroEdital"));
            edital.setDataInicio(LocalDate.parse(doc.getString("dataInicio")));
            edital.setDataFim(LocalDate.parse(doc.getString("dataFim")));
            edital.setMaxInscricoesPorAluno(doc.getInteger("maxInscricoesPorAluno"));
            edital.setPesoCRE(doc.getDouble("pesoCRE"));
            edital.setPesoMedia(doc.getDouble("pesoMedia"));

            // RECUPERANDO A LISTA DE DISCIPLINAS EMBUTIDA
            List<Document> disciplinasDocs = doc.getList("disciplinas", Document.class);
            if (disciplinasDocs != null) {
                for (Document docDisc : disciplinasDocs) {
                    Disciplina d = new Disciplina(
                            docDisc.getString("nome"),
                            docDisc.getInteger("vagasRemuneradas"),
                            docDisc.getInteger("vagasVoluntarias")
                    );
                    edital.adicionarDisciplina(d);
                }
            }

            editais.add(edital);
        }

        return editais;
    }

    public EditalDeMonitoria recuperarEditalPeloId(long id) {

        Document doc = collection.find(
                eq("id", id)
        ).first();

        if(doc == null)
            return null;

        EditalDeMonitoria edital = new EditalDeMonitoria();
        edital.setId(doc.getLong("id"));
        edital.setNumeroEdital(doc.getString("numeroEdital"));
        edital.setDataInicio(LocalDate.parse(doc.getString("dataInicio")));
        edital.setDataFim(LocalDate.parse(doc.getString("dataFim")));
        edital.setMaxInscricoesPorAluno(doc.getInteger("maxInscricoesPorAluno"));
        edital.setPesoCRE(doc.getDouble("pesoCRE"));
        edital.setPesoMedia(doc.getDouble("pesoMedia"));

        // RECUPERANDO A LISTA DE DISCIPLINAS EMBUTIDA
        List<Document> disciplinasDocs = doc.getList("disciplinas", Document.class);
        if (disciplinasDocs != null) {
            for (Document docDisc : disciplinasDocs) {
                Disciplina d = new Disciplina(
                        docDisc.getString("nome"),
                        docDisc.getInteger("vagasRemuneradas"),
                        docDisc.getInteger("vagasVoluntarias")
                );
                edital.adicionarDisciplina(d);
            }
        }

        return edital;
    }

    // Método que o Redis usa para buscar a disciplina no Mongo se não achar no cache
    public Disciplina buscarDisciplinaEmbutidaPorNome(String nome) {
        // Busca no MongoDB o edital que tem essa disciplina na lista
        Document doc = collection.find(eq("disciplinas.nome", nome)).first();

        if (doc == null) return null;

        // Pega a lista de disciplinas embutidas
        List<Document> disciplinasDocs = doc.getList("disciplinas", Document.class);

        // Acha a disciplina certa e devolve
        if (disciplinasDocs != null) {
            for (Document docDisc : disciplinasDocs) {
                if (docDisc.getString("nome").equals(nome)) {
                    return new Disciplina(
                            docDisc.getString("nome"),
                            docDisc.getInteger("vagasRemuneradas"),
                            docDisc.getInteger("vagasVoluntarias")
                    );
                }
            }
        }
        return null;
    }

}