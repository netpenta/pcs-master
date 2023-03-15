package th.co.pentasol.pcs.master.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties(prefix = "pcs.default.config")
public class PcsConfig {
    private Integer effectDate;
}
