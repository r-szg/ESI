package br.usp.projetoa.service;

import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.usp.projetoa.dto.ParecerDto;
import br.usp.projetoa.events.ParecerCriadoEvent;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.Parecer;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.repository.DocenteRepository;
import br.usp.projetoa.repository.ParecerRepository;
import br.usp.projetoa.repository.RelatorioRepository;
import br.usp.projetoa.security.AuthenticatedUserUtil;

@Service
public class ParecerService {
    private final ParecerRepository parecerRepository;
    private final RelatorioRepository relatorioRepository;
    private final DocenteRepository docenteRepository;

    private final ApplicationEventPublisher eventPublisher;

    public ParecerService(ParecerRepository parecerRepository, RelatorioRepository relatorioRepository, 
        ApplicationEventPublisher eventPublisher, DocenteRepository docenteRepository) {
        this.parecerRepository = parecerRepository;
        this.relatorioRepository = relatorioRepository;
        this.docenteRepository = docenteRepository;
        this.eventPublisher = eventPublisher;
    }

    public Long criarParecer(ParecerDto parecerDto) {
        Optional<Relatorio> relatorioOptional = relatorioRepository.findById(parecerDto.relatorioId());

        if (relatorioOptional.isEmpty()) return null;

        Parecer parecer = new Parecer(parecerDto.parecer(), parecerDto.desempenhoRelatorio());
        parecer.setRelatorio(relatorioOptional.get());

        // Pega o Docente autenticado
        Long numeroUspParecista = AuthenticatedUserUtil.getNumeroUspLogado();
        Optional<Docente> docenteOptional = docenteRepository.findById(numeroUspParecista);
        
        if (docenteOptional.isEmpty()) return null;

        parecer.setParecista(docenteOptional.get());

        Parecer parecerSalvo = parecerRepository.save(parecer);

        // Publica o evento após salvar o parecer
        eventPublisher.publishEvent(new ParecerCriadoEvent(parecerSalvo));

        return parecerSalvo.getId();
    }

    public Parecer atualizarParecer(Long parecerId, ParecerDto parecerDto) {
        Optional<Parecer> parecerOptional = parecerRepository.findById(parecerId);

        if (parecerOptional.isEmpty()) return null;

        Parecer parecer = parecerOptional.get();
        parecer.setParecer(parecerDto.parecer());
        parecer.setDesempenhoRelatorio(parecerDto.desempenhoRelatorio());

        return parecerRepository.save(parecer);
    }

    @Transactional
    public boolean deletarParecer(Long parecerId) {
        if (!parecerRepository.existsById(parecerId)) return false;

        parecerRepository.deleteById(parecerId);
        return true;
    }
}
