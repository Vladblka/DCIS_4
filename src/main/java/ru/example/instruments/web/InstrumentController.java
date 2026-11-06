package ru.example.instruments.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.example.instruments.model.MusicalInstrument;
import ru.example.instruments.service.MusicalInstrumentService;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

@Controller
@RequestMapping("/instruments")
public class InstrumentController {
    private final MusicalInstrumentService service;

    public InstrumentController(MusicalInstrumentService service) {
        this.service = service;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                String value = text == null ? "" : text.trim().replace(',', '.');
                setValue(value.isEmpty() ? null : new BigDecimal(value));
            }
        });
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("instruments", service.all());
        return "instruments/list";
    }

    @GetMapping("/new")
    public String addForm(Model model) {
        model.addAttribute("instrument", new MusicalInstrument());
        return "instruments/form";
    }

    @PostMapping
    public String add(@Valid @ModelAttribute("instrument") MusicalInstrument instrument,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "instruments/form";
        }
        MusicalInstrument saved = service.add(instrument);
        redirectAttributes.addFlashAttribute("message", "Запись №" + saved.getId() + " добавлена.");
        return "redirect:/instruments";
    }


    @GetMapping("/edit")
    public String editSelection(Model model) {
        model.addAttribute("instruments", service.all());
        return "instruments/select-edit";
    }


    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable long id, Model model) {
        model.addAttribute("instrument", findOrThrow(id));
        return "instruments/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable long id,
                        @Valid @ModelAttribute("instrument") MusicalInstrument instrument,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        instrument.setId(id);
        if (bindingResult.hasErrors()) {
            return "instruments/form";
        }
        if (!service.update(instrument)) {
            throw notFound(id);
        }
        redirectAttributes.addFlashAttribute("message", "Запись №" + id + " изменена.");
        return "redirect:/instruments";
    }


    @GetMapping("/delete")
    public String deleteSelection(Model model) {
        model.addAttribute("instruments", service.all());
        return "instruments/select-delete";
    }


    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable long id, Model model) {
        model.addAttribute("instrument", findOrThrow(id));
        return "instruments/delete";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        if (!service.delete(id)) {
            throw notFound(id);
        }
        redirectAttributes.addFlashAttribute("message", "Запись №" + id + " удалена.");
        return "redirect:/instruments";
    }


    @GetMapping("/search")
    public String search(@RequestParam(required = false) String brand, Model model) {
        model.addAttribute("brand", brand == null ? "" : brand);
        if (brand != null) {
            String query = brand.trim();
            if (query.isEmpty()) {
                model.addAttribute("error", "Введите часть названия бренда для поиска");
            } else {
                model.addAttribute("instruments", service.searchByBrand(query));
            }
        }
        return "search";
    }

    private MusicalInstrument findOrThrow(long id) {
        return service.find(id).orElseThrow(() -> notFound(id));
    }

    private ResponseStatusException notFound(long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись №" + id + " не найдена");
    }
}
