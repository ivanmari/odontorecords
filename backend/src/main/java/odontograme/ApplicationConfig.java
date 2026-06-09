package odontograme;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Modernized ApplicationConfig
 * Restored EnableSpringDataWebSupport for Pageable resolution in Controllers.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
class ApplicationConfig {

}
