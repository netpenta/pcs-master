package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {

    @GetMapping(value = "/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.status(200).body("Master Backend API Success");
    }
}
