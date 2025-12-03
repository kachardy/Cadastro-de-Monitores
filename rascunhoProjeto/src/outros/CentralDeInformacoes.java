package outros;
import pessoas.Aluno;
import pessoas.Coordenador;
import pessoas.Pessoa;

import java.util.ArrayList;

import erros.AlunoJaExisteException;
import erros.EditalJaExisteException;

public class CentralDeInformacoes {
	private ArrayList<Aluno> todosOsAlunos = new ArrayList<Aluno> ();
	private ArrayList<EditalDeMonitoria> todosOsEditais = new ArrayList<EditalDeMonitoria> ();
	private Coordenador coordenador;

	// Getters
	public ArrayList<Aluno> getTodosOsAlunos() {
        return todosOsAlunos;
    }
	
	public ArrayList<EditalDeMonitoria> getTodosOsEditais() {
		return todosOsEditais;
	}
	
	public Coordenador getCoordenador() {
		return coordenador;
	}
	
	// Setters
    public void setTodosOsAlunos(ArrayList<Aluno> todosOsAlunos) {
        this.todosOsAlunos = todosOsAlunos;
    }
    
    public void setTodosOsEditais(ArrayList<EditalDeMonitoria> todosOsEditais) {
		this.todosOsEditais = todosOsEditais;
	}
    
    public void setCoordenador(Coordenador coordenador) {
		this.coordenador = coordenador;
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
	
	public Pessoa recuperarPessoaPorEmail(String email) {
	    // Verifica se é o Coordenador
	    if (this.coordenador != null && this.coordenador.getEmail().equals(email)) {
	        return this.coordenador;
	    }
	    
	    // Verifica na lista de Alunos
	    for (Aluno a : this.todosOsAlunos) {
	        if (a.getEmail().equals(email)) {
	            return a;
	        }
	    }
	    
	    // Não achou ninguém
	    return null;
	}
	
	public boolean adicionarAluno(Aluno a) throws AlunoJaExisteException {
		for (Aluno aluno: todosOsAlunos) {
			if (a.getMatricula().equals(aluno.getMatricula())){
				throw new AlunoJaExisteException();
			} 
		}
		todosOsAlunos.add(a);
		return true;
	}
	
	public boolean adicionarCoordenador(Coordenador c) {
		System.out.println("Coordenador adicionado com sucesso!");
		setCoordenador(c);
		return true;
	}
	
	
	public boolean adicionarEdital(EditalDeMonitoria edital) throws EditalJaExisteException {
		if (todosOsEditais.isEmpty() == true) {
			System.out.println("Não existe nenhum edital, criando....");
		} else {
			for (EditalDeMonitoria e : todosOsEditais) {
		        if (e.getId() == edital.getId()) {
		            System.out.println("Edital já existe, não adicionado.");
		            throw new EditalJaExisteException();
		        }
		    }
		}
	    
	    todosOsEditais.add(edital);
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
