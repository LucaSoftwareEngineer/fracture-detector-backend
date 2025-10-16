package fracture.detector.backend.analisi.frattura;

import fracture.detector.backend.components.Path;
import fracture.detector.backend.components.Uploader;
import fracture.detector.backend.config.JwtUtil;
import fracture.detector.backend.exceptions.AnalisiFratturaException;
import fracture.detector.backend.exceptions.ApiModelloCnnException;
import fracture.detector.backend.user.User;
import fracture.detector.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AnalisiFratturaService {

    @Value("${endpoint.api.cnn}")
    private String endpointApiCnn;

    private final AnalisiFratturaRepository analisiFratturaRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final Uploader uploader;
    private final RestTemplate restTemplate;
    private final Path path;
    private final ModelMapper modelMapper;

    public AnalisiFratturaResponse newAnalisiFrattura(AnalisiFratturaRequest json) throws AnalisiFratturaException, IOException {
        String nomeFileLastraUnivoco = creaNomeFileLastraUnivoco(json);
        uploader.upload(json.getFileLastra(), nomeFileLastraUnivoco);
        AnalisiFrattura analisiFrattura = new AnalisiFrattura();
        analisiFrattura.setDataAnalisi(json.getDataAnalisi());
        analisiFrattura.setNomeFileLastra(nomeFileLastraUnivoco);

        Boolean esito;
        try {
            esito = this.callHttpApiCnn(json, nomeFileLastraUnivoco);
        } catch (ApiModelloCnnException e) {
            throw new RuntimeException(e);
        }

        analisiFrattura.setEsito(esito);
        User user = userRepository.findById(json.getUserId()).get();
        if (user == null) {
            throw new UsernameNotFoundException("user not exist");
        }

        analisiFrattura.setUser(user);

        try {
            analisiFratturaRepository.save(analisiFrattura);
        } catch (Exception e) {
            throw new AnalisiFratturaException();
        }

        return new AnalisiFratturaResponse(esito);
    }

    public static String creaNomeFileLastraUnivoco(AnalisiFratturaRequest json) {
        Random random = new Random();
        int numeroCasuale = random.nextInt(900000) + 100000;
        int lunghezza = json.getFileLastra().toString().toLowerCase().length();
        String estensione = ".png";
        return json
                .getDataAnalisi().toString().replace(":","")
                .concat("-")
                .concat(String.valueOf(json.getUserId()))
                .concat("-")
                .concat(String.valueOf(numeroCasuale))
                .concat(estensione);
    }


    public Boolean callHttpApiCnn(AnalisiFratturaRequest json, String filePath) throws ApiModelloCnnException {

        File imageFile = new File(path.getCloudPath().concat(filePath));

        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new ApiModelloCnnException("File immagine non trovato o percorso non valido: " + filePath);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        FileSystemResource fileResource = new FileSystemResource(imageFile);
        body.add("fileLastra", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            AnalisiFratturaResponse response = restTemplate.postForObject(
                    endpointApiCnn,
                    requestEntity,
                    AnalisiFratturaResponse.class
            );

            if (response != null && response.isFrattura()) {
                return true;
            }
            return false;

        } catch (HttpClientErrorException e) {
            throw new ApiModelloCnnException("Errore Client API Flask: " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new ApiModelloCnnException("Errore Server API Flask: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new ApiModelloCnnException("Errore generale nella chiamata all'API Flask: " + e.getMessage());
        }

    }


    public List<AnalisiFratturaListResponse> getAnalisiFratture(Long userId) throws UsernameNotFoundException {

        User user = userRepository.findById(userId).get();
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        List<AnalisiFrattura> lista = analisiFratturaRepository.findByUser(user);

        Type listType = new TypeToken<List<AnalisiFratturaListResponse>>() {}.getType();
        List<AnalisiFratturaListResponse> listaAnalisiFratture = modelMapper.map(lista, listType);

        return listaAnalisiFratture;
    }

    public Long getNumeroAnalisiFratture(User user) {
        return analisiFratturaRepository.contaNumeroAnalisiByUserId(user);
    }

    public Long getNumeroAnalisiFrattureConFrattura(User user) {
        return analisiFratturaRepository.contaNumeroAnalisiConFratturaByUserId(user);
    }

    public Long getNumeroAnalisiFrattureSenzaFrattura(User user) {
        return analisiFratturaRepository.contaNumeroAnalisiSenzaFratturaByUserId(user);
    }

    public Long[] getAnalisiPerMeseDiAnnoCorrente(User user) {
        Long[] conteggi = new Long[12];
        int anno = Year.now().getValue();
        int i = 0;
        while (i<12) {
            int mese=i+1;
            conteggi[i] = analisiFratturaRepository.contaAnalisiMeseDiAnnoCorrente(user, mese, anno);
            i++;
        }
        return conteggi;
    }

    public void deleteAnalisiFrattura(Long idAnalisi, Long idUser, String token) throws AnalisiFratturaException {
        try {
            User userById = userRepository.findById(idUser).get();
            User userByToken = userRepository.findByUsername(jwtUtil.extractUsername(token)).get();
            if ((userByToken == userById) && userById != null) {
                analisiFratturaRepository.deleteById(idAnalisi);
            } else {
                throw new AnalisiFratturaException();
            }
        } catch (Exception e) {
            throw new AnalisiFratturaException();
        }
    }

}
