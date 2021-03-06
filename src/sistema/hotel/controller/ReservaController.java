package sistema.hotel.controller;

import java.time.LocalDate;
import java.util.List;

import sistema.hotel.modelo.Promocao;
import sistema.hotel.modelo.Quarto;
import sistema.hotel.modelo.Reserva;
import sistema.hotel.servicos.PromocaoServico;
import sistema.hotel.servicos.QuartoServico;
import sistema.hotel.servicos.ReservaServico;
import sistema.hotel.servicos.implementacao.PromocaoServicoImpl;
import sistema.hotel.servicos.implementacao.QuartoServicoImpl;
import sistema.hotel.servicos.implementacao.ReservaServicoImpl;

/**
 * Classe Reserva Controller
 * 
 * @author Amanda da Silva Ito
 * @version 1.0
 *
 */
public class ReservaController {

	private ReservaServico reservaServico;

	private QuartoServico quartoServico;

	private PromocaoServico promocaoServico;

	public ReservaController() {
		reservaServico = new ReservaServicoImpl();
		quartoServico = new QuartoServicoImpl();
		promocaoServico = new PromocaoServicoImpl();
	}

	/**
	 * M?todo cadastrarReserva verifica se os paramentros s?o v?lidos, instancia um
	 * objeto reserva com os paramentros passados e realiza o cadastro da do objeto
	 * reserva criado.
	 * 
	 * @param idQuarto
	 * @param idPromocao
	 * @param idCliente
	 * @param valor
	 * @param dataEntrada
	 * @param dataSaida
	 * @return true se a reserva for cadastrada com sucesso e false se n?o for
	 *         cadastrado.
	 */
	public boolean cadastrarReserva(int idQuarto, int idPromocao, int idCliente, LocalDate dataEntrada,
			LocalDate dataSaida) {

		if (!reservaServico.validarReserva(idQuarto, idPromocao, idCliente, dataEntrada, dataSaida)) {
			return false;
		}
		if (!reservaServico.verificarDisponibilidade(dataEntrada, dataSaida, idQuarto)) {
			return false;
		}

		Quarto q = quartoServico.getQuarto(idQuarto);
		if (q == null) {
			return false;
		}

		double valorReserva = reservaServico.calcularValorReserva(dataEntrada, dataSaida, q.getValor());
		double totalRerva = valorReserva;

		Promocao p = promocaoServico.getPromocao(idPromocao);
		if (p != null && promocaoServico.validarPromocao(p.getNome(), p.getDataValidade())) {
			totalRerva = reservaServico.aplicarDescontoReserva(valorReserva, p.getValor());
		}

		Reserva reserva = new Reserva(idQuarto, idPromocao, idCliente, totalRerva, dataEntrada, dataSaida);
		return reservaServico.cadastrarReserva(reserva);
	}

	/**
	 * M?todo atualizar reserva valida se a reserva e o idReserva a ser atualizada.
	 * Atualiza a reserva se os paramentros forem v?lidos
	 * 
	 * @param reserva
	 * @param idReserva
	 * @return true se a reserva for atualizada e false se n?o for atualizada.
	 */
	public boolean atualizarReserva(Reserva reserva, int idReserva) {
		if (idReserva <= 0) {
			return false;
		}
		if (!reservaServico.validarReserva(reserva.getIdQuarto(), idReserva, reserva.getIdCliente(),
				reserva.getDataEntrada(), reserva.getDataSaida())) {
			return false;
		}

		return reservaServico.atualizarReserva(reserva, idReserva);
	}

	/**
	 * M?todo verifica se o idReserva passado ? v?lido e deleta a reserva
	 * correspondente.
	 * 
	 * @param idReserva
	 * @return true se a reserva for deletada com sucesso.
	 */
	public boolean deletarReserva(int idReserva) {
		if (idReserva <= 0) {
			return false;
		}
		return reservaServico.deletarReserva(idReserva);
	}

	/**
	 * M?todo lista todas as reservas cadastradas no sistema
	 * 
	 * @return lista de reservas
	 */
	public List<Reserva> listarReservas() {
		return reservaServico.getReservas();
	}

	public Reserva getReserva(int idReserva) {
		if (idReserva <= 0) {
			return null;
		}
		return reservaServico.getReserva(idReserva);
	}
}
