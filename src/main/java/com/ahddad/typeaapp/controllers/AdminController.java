package com.ahddad.typeaapp.controllers;

import com.ahddad.typeaapp.models.Admin;
import com.ahddad.typeaapp.models.Manifestation;
import com.ahddad.typeaapp.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // la methode login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Admin admin) throws Exception {
        return adminService.loginAdmin(admin);
    }

    // la methode get all manifestions
    @GetMapping("/getManifestations")
    public List<Manifestation> getAllManifestations(){
        return adminService.returns_all_manifestations();
    }

    // la methode acceptRequest
    @GetMapping("/acceptRequest/{id}")
    public ResponseEntity<?> acceptRequest(@PathVariable int id){
        return adminService.acceptRequest(id);
    }

    // la methode deleteRequest
    @DeleteMapping("/deleteRequest/{id}")
    public Long deleteRequets(@PathVariable int id){
        return adminService.deleteRequest(id);
    }

    // la methode deleteUser
    @DeleteMapping("/deleteUser/{id}")
    public int deleteUser(@PathVariable int id){
        return adminService.deleteUser(id);
    }

}
