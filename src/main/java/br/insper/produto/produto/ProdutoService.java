package br.insper.produto.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto saveProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto findProdutoById(String id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        return produto.get();
    }

    public Produto diminuirEstoque(String id) {
        Produto produto = findProdutoById(id);
        int novoEstoque = produto.getEstoque() - 1;
        produto.setEstoque(novoEstoque < 0 ? 0 : novoEstoque);
        return produtoRepository.save(produto);
    }

    public List<Produto> getProdutosDisponiveis() {
        // É esperado que ProdutoRepository declare o método findByEstoqueGreaterThan(int estoque)
        return produtoRepository.findByEstoqueGreaterThan(0);
    }
}
