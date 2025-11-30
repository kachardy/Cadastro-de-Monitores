package outros;
import pessoas.Aluno;
import java.util.ArrayList;

public class CentralDeInformacoes {
	private ArrayList<Aluno> todosOsAlunos = new ArrayList<Aluno> ();
	private ArrayList<EditalDeMonitoria> todosOsEditais = new ArrayList<EditalDeMonitoria> ();

	// Getters
	public ArrayList<Aluno> getTodosOsAlunos() {
        return todosOsAlunos;
    }
	
	public ArrayList<EditalDeMonitoria> getTodosOsEditais() {
		return todosOsEditais;
	}
	
	// Setters
    public void setTodosOsAlunos(ArrayList<Aluno> todosOsAlunos) {
        this.todosOsAlunos = todosOsAlunos;
    }
    
    public void setTodosOsEditais(ArrayList<EditalDeMonitoria> todosOsEditais) {
		this.todosOsEditais = todosOsEditais;
	}

	
	public Aluno recuperarAlunoPorMatricula(String numMat) {
		for (Aluno aluno: todosOsAlunos) {
			if (numMat.equals(aluno.getMatricula())){
				System.out.println("Aluno encontrado!");
				return aluno;
			}	
		}
		return null;
	}
	
	public boolean adicionarAluno(Aluno a) {
		for (Aluno aluno: todosOsAlunos) {
			if (a.getMatricula().equals(aluno.getMatricula())){
				System.out.println("Aluno já existe, não adicionado.");
				return false;
			} 
		}
		System.out.println("Aluno adicionado com sucesso!");
		todosOsAlunos.add(a);
		return true;
	}
	
	public boolean adicionarEdital(EditalDeMonitoria edital) {
		if (todosOsEditais.isEmpty() == true) {
			System.out.println("Não existe nenhum edital, criando....");
		} else {
			for (EditalDeMonitoria e : todosOsEditais) {
		        if (e.getId() == edital.getId()) {
		            System.out.println("Edital já existe, não adicionado.");
		            return false;
		        }
		    }
		}
	    
	    todosOsEditais.add(edital);
	    System.out.println("Edital adicionado com sucesso!");
	    return true;
	}
	
	public String percorrerEditais() {
		String resultado = "";
		if (todosOsEditais.isEmpty() == true) {
			return "Nenhum edital";
		}
		for (EditalDeMonitoria e: todosOsEditais) {
			resultado += "\n" + e.toString();
		}	
		
		
		return resultado;
	}
	
	public EditalDeMonitoria recuperarEditalPeloId(long id) {
		if (todosOsEditais.isEmpty() == true) {
			System.out.println("Erro! Não existe nenhum edital!");
			return null;
		} else {
			for (EditalDeMonitoria e: todosOsEditais) {
				if (e.getId() == id){
					System.out.println("Edital encontrado!");
					return e;
				}	
			}
		}
		return null;
	}
	
	public ArrayList<Disciplina> recuperarInscricoesDeUmAlunoEmUmEdital(String matriculaAluno, long idEdital) {
		Aluno alunoEncontrado = recuperarAlunoPorMatricula(matriculaAluno);
		EditalDeMonitoria editalEncontrado = recuperarEditalPeloId(idEdital);
		    
		if (alunoEncontrado == null || editalEncontrado == null) {
			return null;
		}
		    
		ArrayList<Disciplina> inscricoesDoAluno = new ArrayList<>();
	
		for (Disciplina d : editalEncontrado.getTodasAsDisciplinas()) {
			if (d.getAlunosInscritos().contains(alunoEncontrado)) {
				inscricoesDoAluno.add(d);
		    }
		}
	
		return inscricoesDoAluno;
	}

}
