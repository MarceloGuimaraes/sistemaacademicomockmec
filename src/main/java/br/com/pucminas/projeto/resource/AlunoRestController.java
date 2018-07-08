package br.com.pucminas.projeto.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.projeto.event.RecursoCriadoEvent;
import br.com.pucminas.projeto.model.Aluno;
import br.com.pucminas.projeto.service.CadastroAlunoService;

@RestController
@RequestMapping("/mecalunosync")
public class AlunoRestController {

	@Autowired
	private CadastroAlunoService cadastroAlunoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Aluno> alunoList = cadastroAlunoService.findAll();
		return !alunoList.isEmpty() ? ResponseEntity.ok(alunoList) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping
	public ResponseEntity<Aluno> criar(@Valid @RequestBody Aluno aluno, HttpServletResponse response) {
		Aluno alunoSalvo = cadastroAlunoService.save(aluno);
		
		/* Codigo comentado para utilização de event Listener 
		 * 
		 * URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(alunoSalvo.getCodigo()).toUri();
		 * response.setHeader("Location", uri.toASCIIString());
		 *	return ResponseEntity.created(uri).body(alunoSalvo);
		 */
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, alunoSalvo.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(alunoSalvo);
		
	}
	
	
	
}
