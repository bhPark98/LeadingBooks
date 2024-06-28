package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.services.CheckOutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/v1")
public class CheckOutController {
    private final CheckOutService checkOutService;

    // 도서 대여
    @GetMapping("/borrow/books")
    public ResponseEntity<?> borrowBooks(@RequestParam("bId")Long bId, @RequestParam("mId")Long mId) {
        checkOutService.CheckOutBooks(bId, mId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
