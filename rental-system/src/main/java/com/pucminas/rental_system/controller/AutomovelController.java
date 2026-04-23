package com.pucminas.rental_system.controller;

import com.pucminas.rental_system.model.Automovel;
import com.pucminas.rental_system.repository.AutomovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/automoveis")
public class AutomovelController {

    @Autowired
    private AutomovelRepository automovelRepository;

    @GetMapping("/novo")
    public String showNovoForm(Model model) {
        model.addAttribute("automovel", new Automovel());
        return "automovel-form";
    }

    @PostMapping("/novo")
    public String criarAutomovel(@ModelAttribute Automovel automovel, 
                                RedirectAttributes redirectAttributes) {
        try {
            automovelRepository.save(automovel);
            redirectAttributes.addFlashAttribute("successMessage", "Veículo cadastrado com sucesso!");
            return "redirect:/agente/automoveis";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar veículo: " + e.getMessage());
            return "redirect:/automoveis/novo";
        }
    }

    @GetMapping("/editar/{id}")
    public String showEditarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Automovel automovel = automovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            model.addAttribute("automovel", automovel);
            return "automovel-edit-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao carregar veículo: " + e.getMessage());
            return "redirect:/agente/automoveis";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarAutomovel(@PathVariable Long id, 
                                 @ModelAttribute Automovel automovelAtualizado,
                                 RedirectAttributes redirectAttributes) {
        try {
            Automovel automovel = automovelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            
            automovel.setMatricula(automovelAtualizado.getMatricula());
            automovel.setAno(automovelAtualizado.getAno());
            automovel.setMarca(automovelAtualizado.getMarca());
            automovel.setModelo(automovelAtualizado.getModelo());
            automovel.setPlaca(automovelAtualizado.getPlaca());
            automovel.setCor(automovelAtualizado.getCor());
            automovel.setValorAluguelDiario(automovelAtualizado.getValorAluguelDiario());
            
            automovelRepository.save(automovel);
            redirectAttributes.addFlashAttribute("successMessage", "Veículo atualizado com sucesso!");
            return "redirect:/agente/automoveis";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar veículo: " + e.getMessage());
            return "redirect:/automoveis/editar/" + id;
        }
    }

}
