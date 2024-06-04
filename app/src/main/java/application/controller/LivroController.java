package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.model.Genero;
import application.model.Livro;
import application.repository.GeneroRepository;
import application.repository.LivroRepository;

@Controller
@RequestMapping("/livros")
public class LivroController {
    @Autowired
    private LivroRepository livrosRepo;
    @Autowired
    private GeneroRepository generosRepo;

    @RequestMapping("/list")
    public String list(Model ui){
        ui.addAttribute("livros", livrosRepo.findAll());
        return "/livro/list";
    }

    @RequestMapping("/insert")
    public String insert(Model ui) {
        ui.addAttribute("generos", generosRepo.findAll());
        return "/livro/insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(
        @RequestParam("titulo") String titulo,
        @RequestParam("genero") long generoId) {
            
        Optional<Genero> resultado = generosRepo.findById(generoId);
        if(resultado.isPresent()) {
            Livro livro = new Livro();
            livro.setTitulo(titulo);
            livro.setGenero(resultado.get());

            livrosRepo.save(livro);
        }
        
        return "redirect:/livros/list";
    }
    @RequestMapping("/update")
    public String showUpdateForm(@RequestParam("id") long id, Model model) {
        Optional<Livro> result = livrosRepo.findById(id);
        if (result.isPresent()) {
            model.addAttribute("livro", result.get());
            model.addAttribute("generos", generosRepo.findAll());
            return "/livro/update";
        } else {
            return "redirect:/livros/list";
        }
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam("id") long id, @RequestParam("titulo") String titulo, @RequestParam("genero") long generoId) {
        Optional<Livro> result = livrosRepo.findById(id);
        if (result.isPresent()) {
            Optional<Genero> resultGenero = generosRepo.findById(generoId);
            if (resultGenero.isPresent()) {
                Livro livro = result.get();
                livro.setTitulo(titulo);
                livro.setGenero(resultGenero.get());
                livrosRepo.save(livro);
            }
        }
        return "redirect:/livros/list";
    }
}
