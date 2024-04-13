package com.unirest.core.controllers.base;

import com.unirest.core.utils.IProviderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<Data extends IProviderId<?>, DataID, DTOClass, Repo extends JpaRepository<Data, DataID>> {

    protected final Repo repository;
    private final Class<Data> dataClass;
    private final Class<DTOClass> dtoClass;

    private final boolean hasDTO;

    public BaseController(Repo repository, Class<Data> dataClass, Class<DTOClass> dtoClass) {
        this.repository = repository;
        this.dataClass = dataClass;
        this.dtoClass = dtoClass;

        hasDTO = !dataClass.equals(dtoClass);
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam("id") DataID dataID) {
        Optional<Data> dataOptional = repository.findById(dataID);
        return dataOptional.map(data -> {
            if (hasDTO) {
                DTOClass object;
                try {
                    object = dtoClass.getDeclaredConstructor(dataClass).newInstance(data);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                return ResponseEntity.ok(object);
            } else {
                return ResponseEntity.ok(data);
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Data>> all() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody Data data) {
        return ResponseEntity.ok(repository.save(data).getId());
    }

    @PostMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam DataID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.status(202).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
