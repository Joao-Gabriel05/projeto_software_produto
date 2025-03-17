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
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo nome é obrigatório");
        }
        if (produto.getPreco() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo preco é obrigatório");
        }
        if (produto.getEstoque() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo estoque é obrigatório");
        }
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

    // Nova função para retornar todos os produtos
    public List<Produto> getProdutos() {
        return produtoRepository.findAll();
    }
}
