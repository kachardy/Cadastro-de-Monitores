package outros;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import pessoas.Aluno;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Persistencia {

    private static XStream xstream = new XStream(new StaxDriver());

    static {
        // Define os alias para as classes
        xstream.alias("central", CentralDeInformacoes.class);
        xstream.alias("aluno", Aluno.class);
        xstream.alias("edital", EditalDeMonitoria.class);
        xstream.allowTypesByWildcard(new String[] { "outros.*", "pessoas.*"});
        
        xstream.setMode(XStream.NO_REFERENCES);

    }

    public void salvarCentral(CentralDeInformacoes central, String nomeArquivo) {
        try (FileWriter escritor = new FileWriter(nomeArquivo)) {
            xstream.toXML(central, escritor);
            System.out.println("Arquivo salvo com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CentralDeInformacoes recuperarCentral(String nomeArquivo) {
        try (FileReader leitor = new FileReader(nomeArquivo)) {
            CentralDeInformacoes central = (CentralDeInformacoes) xstream.fromXML(leitor);
            System.out.println("Central carregada com sucesso!");
            return central;
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado. Criando nova central...");
            return new CentralDeInformacoes(); // se o arquivo não existe
        } catch (Exception e) {
            e.printStackTrace();
            return new CentralDeInformacoes(); // se ocorrer outro erro
        }
    }
}
