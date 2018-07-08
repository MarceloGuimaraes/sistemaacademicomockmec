package br.com.pucminas.projeto.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.pucminas.projeto.model.Curso;
import br.com.pucminas.projeto.repository.filter.CursoFilter;
import br.com.pucminas.projeto.service.CursoServices;

@Controller
@RequestMapping("/cursos")
public class CursoController {
	

	@Autowired
	private CursoServices cursoServices;
	
	

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroCurso");
		return mv;
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") CursoFilter filtro) {
		List<Curso> todosCursos = cursoServices.filtrar(filtro);
		
		ModelAndView mv = new ModelAndView("Cursos");
		mv.addObject("cursos", todosCursos);
		return mv;
	}
	

	
}
