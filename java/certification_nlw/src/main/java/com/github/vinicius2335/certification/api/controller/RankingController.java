package com.github.vinicius2335.certification.api.controller;

import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.service.TopRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {
    private final TopRankingService topRankingService;

    @GetMapping("/top10")
    public List<Certification> topRanking(){
        return topRankingService.execute();
    }
}
