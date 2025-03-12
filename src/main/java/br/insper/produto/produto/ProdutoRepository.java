package br.insper.produto.produto;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
    List<Produto> findByEstoqueGreaterThan(int estoque);
}
