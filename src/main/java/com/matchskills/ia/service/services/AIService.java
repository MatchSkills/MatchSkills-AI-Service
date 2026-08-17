package com.matchskills.ia.service.services;

import com.matchskills.ia.service.dtos.ExtractHardskillsResponse;
import com.matchskills.ia.service.dtos.ExtractSoftskillsRequest;
import com.matchskills.ia.service.exceptions.customs.jobposting.JobPostingNotFoundException;
import com.matchskills.ia.service.repositorys.JobPostingRepository;
import com.matchskills.ia.service.repositorys.SoftskillRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    final private SoftskillRepository softskillRepository;
    final private ChatClient chatClient;
    final private String extractPrompt;
    final private Resource resultsPrompt;
    final private JobPostingRepository jobPostingRepository;

    public AIService(ChatClient.Builder builder,
                     @Value("${extract.hardskills.prompt.file}") String extractPrompt,
                     @Value("${get.softskills.prompt.file}") Resource resultsPrompt,
                     SoftskillRepository softskillRepository,
                     JobPostingRepository jobPostingRepository
    ) {
        this.chatClient = builder.build();
        this.resultsPrompt = resultsPrompt;
        this.extractPrompt = extractPrompt;
        this.softskillRepository = softskillRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    public ExtractHardskillsResponse extractHardskills(String curriculumUrl) throws MalformedURLException {

        Resource resource = new UrlResource(curriculumUrl);

        return chatClient.prompt()
                .user(u -> u
                        .text(extractPrompt)
                        .media(MimeTypeUtils.parseMimeType("application/pdf"), resource))
                .call()
                .entity(ExtractHardskillsResponse.class);

    }

    public String generateSoftSkillResults(ExtractSoftskillsRequest extractSoftskillsRequest) throws IOException {

        String prompt = new String(
                resultsPrompt.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        var targetSoftSkills = jobPostingRepository.findById(extractSoftskillsRequest.getJobpostingId())
                .orElseThrow(JobPostingNotFoundException::new);

        var targetSoftskillsSet = targetSoftSkills.getTargetSoftskills().keySet();

        var questionsAndAnswers = List.of(
                Map.of("Pergunta","Conte sobre uma ideia inovadora que você propôs e colocou em prática.",
                        "Resposta","MatchSkills, plataforma de seleção e recrutamento que ajuda aos recrutadores, no pouco tempo que tem de analise, a fazer a escolha mais assertiva nos candidatos, dando taxa de match de hard e softskills."),

                Map.of("Pergunta","Conte sobre uma vez que precisou mudar completamente sua abordagem no meio de um projeto.",
                        "Resposta","Uma vez, tive que mudar a minha comunicação com o time, já que tinha entrado naquele tempo, um colaborador com deficiência auditiva, mudei para uma comunicação mais objetiva e curta, mas tentando manter o entendimento entre todos. e falando pelo mesmo canal."),

                Map.of("Pergunta","Conte sobre uma vez que recebeu um feedback difícil de ouvir. Como você reagiu?",
                        "Resposta","infelizmente, falar sobre os erros meus ou de alguém é um assunto bem delicado, uma vez eu recebi um feedback bem grande sobre, e mesmo eu ficando triste, eu manti o foco para fizer que esse feedback foi algo para eu evoluir e melhorar, e não para me rebaixar."),

                Map.of("Pergunta","Conte sobre uma negociação difícil que você conduziu. Qual foi o resultado?",
                        "Resposta","tive que entender todos os problemas e necessidade do meu cliente, e fazer uma analise do que eu poderia fazer para chegar perto de suprir, falando com gestores, chegamos ao um acordo bom para ambos."),

                Map.of("Pergunta","Conte sobre um problema complexo que você precisou analisar. Como chegou à causa raiz?",
                        "Resposta","bem é como dizem, se vc consegue descrever bem um problema, metade dele já foi resolvido. foi exatamente o que eu fiz, analisei o comportamento e mensagens do problema, e fui rastreando a causa de todos, ate chegar em um lugar onde era a raiz")
        );

        var targetSoftSkillsInfo = softskillRepository.findByNameIgnoreCaseIn(targetSoftskillsSet);

        return chatClient.prompt(prompt + targetSoftSkillsInfo.toString() + extractSoftskillsRequest.getQuestionsAndAnswers())
                .call()
                .content();

    }

}
