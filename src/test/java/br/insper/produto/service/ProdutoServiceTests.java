package br.insper.produto.service;

import br.insper.produto.produto.Produto;
import br.insper.produto.produto.ProdutoRepository;
import br.insper.produto.produto.ProdutoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTests {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void test_findAllProdutosWhenProdutosIsEmpty() {
        // Preparação: retorna lista vazia
        Mockito.when(produtoRepository.findAll()).thenReturn(new ArrayList<>());

        // Chamada
        List<Produto> produtos = produtoService.getProdutos();

        // Verificação
        Assertions.assertEquals(0, produtos.size());
    }

    @Test
    void test_saveProdutoSuccessfully() {
        // Criação de um produto com todos os campos
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(50.0f);
        produto.setEstoque(10);

        Mockito.when(produtoRepository.save(produto)).thenReturn(produto);

        Produto produtoReturn = produtoService.saveProduto(produto);

        Assertions.assertEquals("Produto Teste", produtoReturn.getNome());
        Assertions.assertEquals(50.0f, produtoReturn.getPreco());
        Assertions.assertEquals(10, produtoReturn.getEstoque());
    }

    @Test
    void test_saveProdutoErrorNomeIsNull() {
        // Produto sem nome (campo obrigatório)
        Produto produto = new Produto();
        produto.setPreco(50.0f);
        produto.setEstoque(10);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> produtoService.saveProduto(produto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_saveProdutoErrorPrecoIsNull() {
        // Produto sem preço (campo obrigatório)
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setEstoque(10);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> produtoService.saveProduto(produto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_saveProdutoErrorEstoqueIsNull() {
        // Produto sem estoque (campo obrigatório)
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(50.0f);

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> produtoService.saveProduto(produto)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void test_findProdutoByIdSuccessfully() {
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto Teste");
        produto.setPreco(50.0f);
        produto.setEstoque(10);

        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        Produto produtoResp = produtoService.findProdutoById("1");

        Assertions.assertEquals("Produto Teste", produtoResp.getNome());
        Assertions.assertEquals(50.0f, produtoResp.getPreco());
        Assertions.assertEquals(10, produtoResp.getEstoque());
    }

    @Test
    void test_findProdutoByIdWhenNotFound() {
        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> produtoService.findProdutoById("1")
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void test_diminuirEstoqueSuccessfully() {
        // Produto com estoque 10
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Produto Teste");
        produto.setPreco(50.0f);
        produto.setEstoque(10);

        Mockito.when(produtoRepository.findById("1")).thenReturn(Optional.of(produto));

        // Após diminuir, espera-se que o estoque seja 9
        Produto produtoAfterUpdate = new Produto();
        produtoAfterUpdate.setId("1");
        produtoAfterUpdate.setNome("Produto Teste");
        produtoAfterUpdate.setPreco(50.0f);
        produtoAfterUpdate.setEstoque(9);

        Mockito.when(produtoRepository.save(produto)).thenReturn(produtoAfterUpdate);

        Produto result = produtoService.diminuirEstoque("1");

        Assertions.assertEquals(9, result.getEstoque());
    }
}
