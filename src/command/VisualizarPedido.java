package command;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Cliente;
import model.Compra;
import model.Produto;
import service.CarrinhoService;
import service.CompraService;

public class VisualizarPedido implements Command {

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// carrega os dados do pedido (admin)
		
		// instância os services
		CompraService serviceCompra = new CompraService();
		CarrinhoService serviceCarrinho = new CarrinhoService();
		
		// pega o número do pedido
		int numPedido = Integer.parseInt(request.getParameter("NumPedido"));
		
		// carrega os dados do pedido, do cliente e os produtos comprados
		Compra pedido = serviceCompra.carregar(numPedido);
		Cliente cliente = serviceCarrinho.carregarClienteDoPedido(numPedido);
		ArrayList<Produto> carrinhoBanco = serviceCarrinho.carregarProdutosDoPedido(numPedido);
		ArrayList<Produto> carrinho = new ArrayList<>();
		
		// verifica se já existem produtos iguais no ArrayList, se sim, ele aumenta a quantidade, se não, ele simplesmente adiciona
		for (int i = 0; i < carrinhoBanco.size(); i++) {
			int id = Calcula.indiceProduto(carrinho, carrinhoBanco.get(i));
			if (id != -1) carrinho.get(id).aumentaQuantidade();
			else carrinho.add(carrinhoBanco.get(i));
		}
		
		session.setAttribute("pedidoCliente", pedido);
		session.setAttribute("clientePedido", cliente);
		session.setAttribute("carrinho", carrinho);
		
		RequestDispatcher view = request.getRequestDispatcher("VisualizarPedido.jsp");		
		view.forward(request, response);
		
	}
	
}
