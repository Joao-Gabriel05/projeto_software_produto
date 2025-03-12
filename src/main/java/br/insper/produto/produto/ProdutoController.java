package br.insper.produto.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Rota para criar um novo produto
    @PostMapping
    public Produto createProduto(@RequestBody Produto produto) {
        return produtoService.saveProduto(produto);
    }

    // Rota para consultar se um produto existe pelo seu id
    @GetMapping("/{id}")
    public Produto getProduto(@PathVariable String id) {
        return produtoService.findProdutoById(id);
    }

    // Rota para diminuir a quantidade de um produto no estoque
    // Exemplo de chamada: PATCH /api/produto/1/diminuir
    @PutMapping("/{id}/diminuir")
    public Produto diminuirEstoque(@PathVariable String id) {
        return produtoService.diminuirEstoque(id);
    }

    // Rota para listar todos os produtos disponíveis (por exemplo, com estoque > 0)
    @GetMapping("/disponiveis")
    public List<Produto> getProdutosDisponiveis() {
        return produtoService.getProdutosDisponiveis();
    }
}
