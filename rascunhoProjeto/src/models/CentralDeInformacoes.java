package models;
import java.util.ArrayList;

import erros.AlunoJaExisteException;
import erros.EditalJaExisteException;
import erros.UsuarioJaExisteException;

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
				return aluno;
			}
		}
		return null;
	}

	public Pessoa recuperarPessoaPorEmail(String email) {
		if (this.coordenador != null && this.coordenador.getEmail().equals(email)) {
			return this.coordenador;
		}

		for (Aluno a : this.todosOsAlunos) {
			if (a.getEmail().equals(email)) {
				return a;
			}
		}

		return null;
	}

	public boolean adicionarAluno(Aluno a) throws AlunoJaExisteException, UsuarioJaExisteException {
		for (Aluno aluno: todosOsAlunos) {
			if (a.getMatricula().equals(aluno.getMatricula()) || (a.getEmail().equals(aluno.getEmail()))){
				throw new AlunoJaExisteException();
			}else if ((a.getMatricula().equals(this.coordenador.getMatricula()) || a.getEmail().equals(this.coordenador.getEmail()))) {
				throw new UsuarioJaExisteException();
			}
		}
		todosOsAlunos.add(a);
		return true;
	}

	public boolean adicionarCoordenador(Coordenador c) {
		setCoordenador(c);
		return true;
	}


	public boolean adicionarEdital(EditalDeMonitoria edital) throws EditalJaExisteException {
		for (EditalDeMonitoria e : todosOsEditais) {
			if (e.getId() == edital.getId()) {
				throw new EditalJaExisteException();
			}
		}

		todosOsEditais.add(edital);
		return true;
	}

	public String percorrerEditais() {
		String resultado = "";
		if (todosOsEditais.isEmpty()) {
			return "Nenhum edital";
		}
		for (EditalDeMonitoria e: todosOsEditais) {
			resultado += "\n" + e.toString();
		}

		return resultado;
	}

	public EditalDeMonitoria recuperarEditalPeloId(long id) {
		for (EditalDeMonitoria e: todosOsEditais) {
			if (e.getId() == id){
				return e;
			}
		}
		return null;
	}

    // Retorna as incrições de um aluno no edital
	public ArrayList<Disciplina> recuperarInscricoesDeUmAlunoEmUmEdital(String matriculaAluno, long idEdital) {
		Aluno alunoEncontrado = recuperarAlunoPorMatricula(matriculaAluno);
		EditalDeMonitoria editalEncontrado = recuperarEditalPeloId(idEdital);

		if (alunoEncontrado == null || editalEncontrado == null) {
			return null;
		}

        return editalEncontrado.getGerenciador().getDisciplinasPorAluno(matriculaAluno);
	}
}