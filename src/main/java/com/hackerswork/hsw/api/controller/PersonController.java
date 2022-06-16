package com.hackerswork.hsw.api.controller;

import com.hackerswork.hsw.dto.PersonDTO;
import com.hackerswork.hsw.mapper.PersonMapper;
import com.hackerswork.hsw.service.person.PersonCommandService;
import com.hackerswork.hsw.service.person.PersonQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/person")
@Api(value = "Person")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class PersonController {

    private final PersonCommandService personCommandService;
    private final PersonQueryService personQueryService;
    private final PersonMapper personMapper;

    @PostMapping(value = "/add")
    @ApiOperation(value = "Add person", notes = "Adding new person")
    public ResponseEntity<PersonDTO> add(@Valid @RequestBody PersonDTO dto) {
        return ResponseEntity.ok(personMapper.toDTO(personCommandService.add(personMapper.toEntity(dto))));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get person", notes = "Get person by id")
    public ResponseEntity<PersonDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(personMapper.toDTO(personQueryService.find(id)));
    }

    @GetMapping(value = "/search={text}")
    @ApiOperation(value = "Find persons", notes = "Find person by keyword")
    public ResponseEntity<List<PersonDTO>> get(@PathVariable String text) {
        var foundPersons = new ArrayList<PersonDTO>();
        foundPersons.addAll(personMapper.toDTOs(personQueryService.findByUserNameLike(text)));
        foundPersons.addAll(personMapper.toDTOs(personQueryService.findByNameLike(text)));

        return ResponseEntity.ok(foundPersons);
    }
}