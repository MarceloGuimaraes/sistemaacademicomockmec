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
import br.com.pucminas.projeto.model.Curso;
import br.com.pucminas.projeto.repository.Cursos;
import br.com.pucminas.projeto.service.SyncLegadoData;

@RestController
@RequestMapping("/meccursosync")
public class CursoRestController {

	@Autowired
	private Cursos cursoServices;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private SyncLegadoData syncLegadoData;
	
	@GetMapping
	public ResponseEntity<?> listar() {
		List<Curso> cursoList = cursoServices.findAll();
		return !cursoList.isEmpty() ? ResponseEntity.ok(cursoList) : ResponseEntity.noContent().build();
	}
	
	
	@PostMapping
	public ResponseEntity<Curso> criar(@Valid @RequestBody Curso curso, HttpServletResponse response) {
		Curso cursoSalvo = cursoServices.save(curso);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, cursoSalvo.getCodigo()));
		
		syncLegadoData.enviar(false, curso.toString());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
		
	}
	
	
	
}
