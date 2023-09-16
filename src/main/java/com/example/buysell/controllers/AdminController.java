package com.example.buysell.controllers;


import org.springframework.web.bind.annotation.*;

@RestController //for return pages use @controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping()
    public String get() {
        return "admin-panel get ";
    }

    @PostMapping()
    public String post() {
        return "admin-panel post";
    }

    @PutMapping()
    public String put() {
        return "admin-panel put";
    }

    @DeleteMapping()
    public String delete() {
        return "admin-panel delete";
    }
}
