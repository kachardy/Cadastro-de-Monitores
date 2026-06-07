package services;

import dao.PessoaDao;
import models.Pessoa;

public class PessoaService {
    private PessoaDao pessoaDao;

    public PessoaService(PessoaDao pessoaDao) {
        this.pessoaDao = pessoaDao;
    }

    public Pessoa buscarPorEmail(String email) {
        return pessoaDao.recuperarPessoaPorEmail(email);
    }
}