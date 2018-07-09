package br.com.pucminas.projeto.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SyncLegadoData {

	private static final String CONVIVENCIA_WRITE_IO_EXCEPTION = "SyncLegacyCobol: Write: IOException: ";

	private static final String servidor = "10.2.3.4";
	private static final int porta = 86;

	protected Logger LOGGER = Logger.getLogger(getClass());

	public void enviar(Boolean conectionActive, String strObjetct) {
		
		if(!conectionActive) return;
		
		// Criacao dos objetos
		Socket smtpSocket = null;
		DataOutputStream os = null;
		DataInputStream is = null;

		try {
			// Abre a conxeao
			smtpSocket = new Socket(servidor, porta);
			// Streams de comunicacao
			os = new DataOutputStream(smtpSocket.getOutputStream());
			is = new DataInputStream(smtpSocket.getInputStream());

			// Verifica se esta conectado
			if (smtpSocket != null && os != null && is != null) {

				// Envia as mensagems de dados
				enviarMensagem(os, strObjetct);

			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Enviar mensagem.
	 *
	 * @param os
	 *            the os
	 * @param mensagem
	 *            the mensagem
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void enviarMensagem(DataOutputStream os, String mensagem) throws IOException {
		// Envia mensagem
		byte[] mensagemFinal = null;
		try {
			// Monta header
			byte[] headerEnvio = Header.createHeader(mensagem.length());

			// COnverte mensagem para array de bytes
			byte[] mensagemBytes = mensagem.getBytes();
			mensagemFinal = new byte[headerEnvio.length + mensagemBytes.length];
			System.arraycopy(headerEnvio, 0, mensagemFinal, 0, headerEnvio.length);
			System.arraycopy(mensagemBytes, 0, mensagemFinal, headerEnvio.length, mensagemBytes.length);

			// Grava
			os.write(mensagemFinal);

		} catch (IOException e) {
			LOGGER.error(CONVIVENCIA_WRITE_IO_EXCEPTION + mensagemFinal);
			throw e;
		}
	}

}
