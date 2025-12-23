package com.chao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.entity.Word;
import com.chao.mapper.WordMapper;
import com.chao.service.WordService;
import org.springframework.stereotype.Service;

@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {
}

