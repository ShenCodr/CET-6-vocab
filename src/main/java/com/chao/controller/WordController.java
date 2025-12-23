package com.chao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chao.entity.Word;
import com.chao.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
@CrossOrigin(origins = "*")
public class WordController {

    @Autowired
    private WordService wordService;

    @GetMapping
    public List<Word> getAllWords(@RequestParam(required = false) Integer status, @RequestParam(required = false) Integer userId) {
        LambdaQueryWrapper<Word> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(Word::getStatus, status);
        }
        if (userId != null) {
            queryWrapper.eq(Word::getUserId, userId);
        }
        queryWrapper.orderByDesc(Word::getCreateTime);
        return wordService.list(queryWrapper);
    }

    @PostMapping
    public boolean addWord(@RequestBody Word word) {
        return wordService.save(word);
    }

    @PutMapping("/{id}")
    public boolean updateWordStatus(@PathVariable Long id, @RequestBody Word word) {
        word.setId(id);
        return wordService.updateById(word);
    }

    @DeleteMapping("/{id}")
    public boolean deleteWord(@PathVariable Long id) {
        return wordService.removeById(id);
    }
}

