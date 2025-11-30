package outros;
import java.util.ArrayList;
import java.time.LocalDate;
import pessoas.Aluno;

public class EditalDeMonitoria {
	private long id;
	private String numeroEdital;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	private ArrayList<Disciplina> todasAsDisciplinas = new ArrayList<Disciplina> ();
	
	// Construtores
	
	public EditalDeMonitoria() {
		this.id = System.currentTimeMillis();
	}
	
	public EditalDeMonitoria (String numeroEdital, LocalDate dataInicio, LocalDate dataFim) {
		this.id = System.currentTimeMillis();
		this.numeroEdital = numeroEdital;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}
		
	public long getId() {
		return id;
	}

	public String getNumeroEdital() {
		return numeroEdital;
	}

	public void setNumeroEdital(String numeroEdital) {
		this.numeroEdital = numeroEdital;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public ArrayList<Disciplina> getTodasAsDisciplinas() {
		return todasAsDisciplinas;
	}
	
	public void setTodasAsDisciplinas(ArrayList<Disciplina> todasAsDisciplinas) {
		this.todasAsDisciplinas = todasAsDisciplinas;
	}
	
	public void adicionarDisciplina(Disciplina d) {		
		this.todasAsDisciplinas.add(d);
	}
	
	public boolean inscrever(Aluno aluno, Disciplina disciplina) {
		
		LocalDate hoje = LocalDate.now();
		
		if ((hoje.isAfter(dataInicio) || hoje.isEqual(dataInicio)) && (hoje.isBefore(dataFim) || hoje.isEqual(dataFim))) {
				if (todasAsDisciplinas.contains(disciplina)) {
					if (disciplina.getQtdVagas() == 0) {
						System.out.println("Vagas Esgotadas");
						return false;
					}
					disciplina.adicionarAluno(aluno);
					System.out.println("Aluno inscrito!");
					Mensageiro.enviarEmail(aluno.getEmail());
				    return true;
				}
			}
		return false;

	}
	
	public boolean jaAcabou() {
		LocalDate hoje = LocalDate.now();
		if (hoje.isAfter(dataFim)) {
			return true;
		}
		return false;
	}
	
	public String percorrerDisciplinas() {
	    String resultado = "";
	    if (todasAsDisciplinas.isEmpty() == true) {
	    	return "Nenhuma disciplina cadastrada!";
	    }
	    for (Disciplina d : todasAsDisciplinas) {
	        resultado += "\n - " + d.getNome() + " (" + d.getQtdVagas() + " vagas)";
	    }
	    
	    return resultado;
	}
	
	public Disciplina recuperarDisciplinaPeloNome(String nomeDisciplina) {
		if (todasAsDisciplinas.isEmpty() == true) {
			return null;
		}
		for (Disciplina d : todasAsDisciplinas) {
	        if (d.getNome().equals(nomeDisciplina)) {
	        	return d;
	        }
		}
		return null;
	}
	public String toString() {
	    String status = jaAcabou() ? "Fechado" : "Aberto";
	    
	    String resultado = "Edital de Monitoria " + id;
	    resultado += "\nDisciplinas:" + percorrerDisciplinas();
	    resultado += "\nStatus: " + status;
	    
	    return resultado;
	}

	
}
