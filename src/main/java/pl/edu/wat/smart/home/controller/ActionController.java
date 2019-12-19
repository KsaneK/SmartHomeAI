package pl.edu.wat.smart.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.smart.home.dto.DtoAction;
import pl.edu.wat.smart.home.dto.DtoActionNotify;
import pl.edu.wat.smart.home.dto.DtoCreateAction;
import pl.edu.wat.smart.home.service.ActionService;

import java.util.List;

@RestController()
@RequestMapping("/action")
public class ActionController {

    @Autowired
    ActionService service;

    @GetMapping("")
    public List<DtoAction> getAll() {
        return service.getAllActions();
    }

    @PostMapping("")
    public long addAction(@RequestBody DtoCreateAction dtoCreateAction) {
        return service.addAction(dtoCreateAction);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/notify")
    public void setActionNotify(@RequestBody DtoActionNotify dto) {
        service.setActionNotify(dto);
    }
}
