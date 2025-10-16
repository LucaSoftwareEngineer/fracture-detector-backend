package fracture.detector.backend.analisi.frattura;

import fracture.detector.backend.exceptions.AnalisiFratturaException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/analisi/frattura")
@RequiredArgsConstructor
public class AnalisiFratturaController {

    private final AnalisiFratturaService analisiFratturaService;

    @PostMapping("/new")
    public ResponseEntity<AnalisiFratturaResponse> newAnalisiFrattura(AnalisiFratturaRequest json) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(analisiFratturaService.newAnalisiFrattura(json));
        } catch (AnalisiFratturaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AnalisiFratturaListResponse>> getAnalisiFrattureById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(analisiFratturaService.getAnalisiFratture(id));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
