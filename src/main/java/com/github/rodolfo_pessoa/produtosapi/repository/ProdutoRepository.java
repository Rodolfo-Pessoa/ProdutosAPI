package com.github.rodolfo_pessoa.produtosapi.repository;

import com.github.rodolfo_pessoa.produtosapi.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository <Produto, String>{
}
