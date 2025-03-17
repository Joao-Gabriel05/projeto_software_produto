package br.insper.iam.produto.controller;

import br.insper.produto.produto.Produto;
import br.insper.produto.produto.ProdutoController;
import br.insper.produto.produto.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTests {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void test_GetProdutos() throws Exception {
        // Criação de produtos com os atributos da classe Produto
        Produto produto1 = new Produto();
        produto1.setId("1");
        produto1.setNome("Produto A");
        produto1.setPreco(10.0f);
        produto1.setEstoque(50);

        Produto produto2 = new Produto();
        produto2.setId("2");
        produto2.setNome("Produto B");
        produto2.setPreco(20.0f);
        produto2.setEstoque(30);

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        Mockito.when(produtoService.getProdutos()).thenReturn(produtos);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produto"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produtos)));
    }

    @Test
    void test_PostProduto() throws Exception {
        // Produto recebido na requisição (sem id)
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setPreco(15.0f);
        produto.setEstoque(40);

        // Produto retornado após salvar (com id atribuído)
        Produto produtoSalvo = new Produto();
        produtoSalvo.setId("123");
        produtoSalvo.setNome("Produto Teste");
        produtoSalvo.setPreco(15.0f);
        produtoSalvo.setEstoque(40);

        Mockito.when(produtoService.saveProduto(Mockito.any())).thenReturn(produtoSalvo);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/produto")
                                .content(objectMapper.writeValueAsString(produto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produtoSalvo)));
    }
}
