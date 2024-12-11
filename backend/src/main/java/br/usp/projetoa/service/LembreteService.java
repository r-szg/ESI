package br.usp.projetoa.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.mail.MessagingException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;
import br.usp.projetoa.util.SemestreUtil;

@Service
public class LembreteService {
    private final StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;
    private final EmailService emailService;

    public LembreteService(StatusEntregaRelatorioRepository statusEntregaRelatorioRepository,
            EmailService emailService) {
        this.statusEntregaRelatorioRepository = statusEntregaRelatorioRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 1 7,1 *")
    public void enviarLembretes() {
        String semestreAnoAtual = SemestreUtil.determinaSemestreAnoAtual();

        List<StatusEntregaRelatorio> pendentes = statusEntregaRelatorioRepository
            .findBySemestreAnoAndSituacaoEntregaRelatorio(semestreAnoAtual, SituacaoEntregaRelatorio.PENDENTE);

        for (StatusEntregaRelatorio statusEntrega : pendentes) {
            if (statusEntrega.getDataUltimoLembrete() == null 
                || statusEntrega.getDataUltimoLembrete().isBefore(LocalDate.now().minusDays(30))) {

                try {
                    emailService.enviarEmail(statusEntrega.getAluno().getEmail(),
                        "Lembrete: Prazo de 30 dias para Entrega do Relatório", 
                        "Prezado aluno,\n\n"
                        + "Gostaríamos de lembrar que o prazo final para a entrega do relatório está se"
                        + " aproximando. Faltam 30 dias para a data de envio, portanto, pedimos que"
                        + " revisem o progresso atual e planejem as etapas restantes para garantir o"
                        + " cumprimento do prazo.\n\n"
                        + "Se precisarem de algum apoio ou tiverem dúvidas em relação ao conteúdo, por"
                        + " favor, não hesitem em nos contatar.\n\n"
                        + "Agradecemos desde já pela atenção e comprometimento.");

                    statusEntrega.setDataUltimoLembrete(LocalDate.now());
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
